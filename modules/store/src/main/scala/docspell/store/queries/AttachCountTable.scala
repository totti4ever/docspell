/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.store.queries

import docspell.common.Ident
import docspell.store.qb.Column
import docspell.store.qb.TableDef

final case class AttachCountTable(aliasName: String) extends TableDef {
  val tableName             = "attachs"
  val alias: Option[String] = Some(aliasName)

  val num    = Column[Int]("num", this)
  val itemId = Column[Ident]("item_id", this)

  def as(alias: String): AttachCountTable =
    copy(aliasName = alias)
}
