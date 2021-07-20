/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.common

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class NerLabel(label: String, tag: NerTag, startPosition: Int, endPosition: Int) {}

object NerLabel {
  implicit val jsonEncoder: Encoder[NerLabel] = deriveEncoder[NerLabel]
  implicit val jsonDecoder: Decoder[NerLabel] = deriveDecoder[NerLabel]
}
