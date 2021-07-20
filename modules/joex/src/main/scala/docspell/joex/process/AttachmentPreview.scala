/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.joex.process

import cats.Functor
import cats.data.OptionT
import cats.effect._
import cats.implicits._
import fs2.Stream

import docspell.common._
import docspell.convert._
import docspell.extract.pdfbox.PdfboxPreview
import docspell.extract.pdfbox.PreviewConfig
import docspell.joex.scheduler._
import docspell.store.queries.QAttachment
import docspell.store.records.RAttachment
import docspell.store.records._
import docspell.store.syntax.MimeTypes._

import bitpeace.{Mimetype, MimetypeHint, RangeDef}

/** Goes through all attachments that must be already converted into a
  * pdf. If it is a pdf, the first page is converted into a small
  * preview png image and linked to the attachment.
  */
object AttachmentPreview {

  def apply[F[_]: Sync](cfg: ConvertConfig, pcfg: PreviewConfig)(
      item: ItemData
  ): Task[F, ProcessItemArgs, ItemData] =
    Task { ctx =>
      for {
        _ <- ctx.logger.info(
          s"Creating preview images for ${item.attachments.size} files…"
        )
        preview <- PdfboxPreview(pcfg)
        _ <- item.attachments
          .traverse(createPreview(ctx, preview, cfg.chunkSize))
          .attempt
          .flatMap {
            case Right(_) => ().pure[F]
            case Left(ex) =>
              ctx.logger.error(ex)(
                s"Creating preview images failed, continuing without it."
              )
          }
      } yield item
    }

  def createPreview[F[_]: Sync](
      ctx: Context[F, _],
      preview: PdfboxPreview[F],
      chunkSize: Int
  )(
      ra: RAttachment
  ): F[Option[RAttachmentPreview]] =
    findMime[F](ctx)(ra).flatMap {
      case MimeType.PdfMatch(_) =>
        preview.previewPNG(loadFile(ctx)(ra)).flatMap {
          case Some(out) =>
            ctx.logger.debug("Preview generated, saving to database…") *>
              createRecord(ctx, out, ra, chunkSize).map(_.some)
          case None =>
            ctx.logger
              .info(s"Preview could not be generated. Maybe the pdf has no pages?") *>
              (None: Option[RAttachmentPreview]).pure[F]
        }

      case mt =>
        ctx.logger.warn(s"Not a pdf file, but ${mt.asString}, cannot get page count.") *>
          (None: Option[RAttachmentPreview]).pure[F]
    }

  private def createRecord[F[_]: Sync](
      ctx: Context[F, _],
      png: Stream[F, Byte],
      ra: RAttachment,
      chunkSize: Int
  ): F[RAttachmentPreview] = {
    val name = ra.name
      .map(FileName.apply)
      .map(_.withPart("preview", '_').withExtension("png"))
    for {
      fileMeta <- ctx.store.bitpeace
        .saveNew(
          png,
          chunkSize,
          MimetypeHint(name.map(_.fullName), Some("image/png"))
        )
        .compile
        .lastOrError
      now <- Timestamp.current[F]
      rp = RAttachmentPreview(ra.id, Ident.unsafe(fileMeta.id), name.map(_.fullName), now)
      _ <- QAttachment.deletePreview(ctx.store)(ra.id)
      _ <- ctx.store.transact(RAttachmentPreview.insert(rp))
    } yield rp
  }

  def findMime[F[_]: Functor](ctx: Context[F, _])(ra: RAttachment): F[MimeType] =
    OptionT(ctx.store.transact(RFileMeta.findById(ra.fileId)))
      .map(_.mimetype)
      .getOrElse(Mimetype.applicationOctetStream)
      .map(_.toLocal)

  def loadFile[F[_]](ctx: Context[F, _])(ra: RAttachment): Stream[F, Byte] =
    ctx.store.bitpeace
      .get(ra.fileId.id)
      .unNoneTerminate
      .through(ctx.store.bitpeace.fetchData2(RangeDef.all))

}
