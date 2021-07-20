/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.joex.hk

import cats.effect._
import cats.implicits._

import docspell.common._
import docspell.joex.scheduler.{Context, Task}
import docspell.store.records._

import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client

object CheckNodesTask {

  def apply[F[_]: Async](
      cfg: HouseKeepingConfig.CheckNodes
  ): Task[F, Unit, Unit] =
    Task { ctx =>
      if (cfg.enabled)
        for {
          _ <- ctx.logger.info("Check nodes reachability")
          ec = scala.concurrent.ExecutionContext.global
          _ <- BlazeClientBuilder[F](ec).resource.use { client =>
            checkNodes(ctx, client)
          }
          _ <- ctx.logger.info(
            s"Remove nodes not found more than ${cfg.minNotFound} times"
          )
          n <- removeNodes(ctx, cfg)
          _ <- ctx.logger.info(s"Removed $n nodes")
        } yield ()
      else
        ctx.logger.info("CheckNodes task is disabled in the configuration")
    }

  def checkNodes[F[_]: Async](ctx: Context[F, _], client: Client[F]): F[Unit] =
    ctx.store
      .transact(RNode.streamAll)
      .evalMap(node =>
        checkNode(ctx.logger, client)(node.url)
          .flatMap(seen =>
            if (seen) ctx.store.transact(RNode.resetNotFound(node.id))
            else ctx.store.transact(RNode.incrementNotFound(node.id))
          )
      )
      .compile
      .drain

  def checkNode[F[_]: Async](logger: Logger[F], client: Client[F])(
      url: LenientUri
  ): F[Boolean] = {
    val apiVersion = url / "api" / "info" / "version"
    for {
      res <- client.expect[String](apiVersion.asString).attempt
      _ <- res.fold(
        ex => logger.info(s"Node ${url.asString} not found: ${ex.getMessage}"),
        _ => logger.info(s"Node ${url.asString} is reachable")
      )
    } yield res.isRight
  }

  def removeNodes[F[_]](
      ctx: Context[F, _],
      cfg: HouseKeepingConfig.CheckNodes
  ): F[Int] =
    ctx.store.transact(RNode.deleteNotFound(cfg.minNotFound))

}
