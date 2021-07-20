/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.store.generator

import java.time.LocalDate

import docspell.common._
import docspell.query.ItemQueryParser
import docspell.store.qb.DSL._
import docspell.store.qb.generator.{ItemQueryGenerator, Tables}
import docspell.store.queries.AttachCountTable
import docspell.store.records._

import munit._

class ItemQueryGeneratorTest extends FunSuite {
  import docspell.store.impl.DoobieMeta._

  val tables = Tables(
    RItem.as("i"),
    ROrganization.as("co"),
    RPerson.as("cp"),
    RPerson.as("np"),
    REquipment.as("ne"),
    RFolder.as("f"),
    RAttachment.as("a"),
    RAttachmentMeta.as("m"),
    AttachCountTable("cta")
  )
  val now: LocalDate = LocalDate.of(2021, 2, 25)

  def mkTimestamp(year: Int, month: Int, day: Int): Timestamp =
    Timestamp.atUtc(LocalDate.of(year, month, day).atStartOfDay())

  test("basic test") {
    val q = ItemQueryParser
      .parseUnsafe("(& name:hello date>=2020-02-01 (| source:expense* folder=test ))")
    val cond = ItemQueryGenerator(now, tables, Ident.unsafe("coll"))(q)
    val expect =
      tables.item.name.like("hello") &&
        coalesce(tables.item.itemDate.s, tables.item.created.s) >=
        mkTimestamp(2020, 2, 1) &&
        (tables.item.source.like("expense%") || tables.folder.name === "test")

    assertEquals(cond, expect)
  }

  test("!conc:*") {
    val q      = ItemQueryParser.parseUnsafe("!conc:*")
    val cond   = ItemQueryGenerator(now, tables, Ident.unsafe("coll"))(q)
    val expect = not(tables.concPers.name.like("%") || tables.concEquip.name.like("%"))
    assertEquals(cond, expect)
  }

}
