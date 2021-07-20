/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.backend

import docspell.backend.signup.{Config => SignupConfig}
import docspell.common._
import docspell.store.JdbcConfig

case class Config(
    mailDebug: Boolean,
    jdbc: JdbcConfig,
    signup: SignupConfig,
    files: Config.Files
) {}

object Config {

  case class Files(chunkSize: Int, validMimeTypes: Seq[MimeType])

}
