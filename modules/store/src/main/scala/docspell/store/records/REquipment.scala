/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.store.records

import cats.data.NonEmptyList

import docspell.common._
import docspell.store.qb.DSL._
import docspell.store.qb._

import doobie._
import doobie.implicits._

case class REquipment(
    eid: Ident,
    cid: Ident,
    name: String,
    created: Timestamp,
    updated: Timestamp,
    notes: Option[String],
    use: EquipmentUse
) {}

object REquipment {
  final case class Table(alias: Option[String]) extends TableDef {
    val tableName = "equipment"

    val eid     = Column[Ident]("eid", this)
    val cid     = Column[Ident]("cid", this)
    val name    = Column[String]("name", this)
    val created = Column[Timestamp]("created", this)
    val updated = Column[Timestamp]("updated", this)
    val notes   = Column[String]("notes", this)
    val use     = Column[EquipmentUse]("equip_use", this)
    val all     = NonEmptyList.of[Column[_]](eid, cid, name, created, updated, notes, use)
  }

  val T = Table(None)
  def as(alias: String): Table =
    Table(Some(alias))

  def insert(v: REquipment): ConnectionIO[Int] = {
    val t = Table(None)
    DML
      .insert(
        t,
        t.all,
        fr"${v.eid},${v.cid},${v.name},${v.created},${v.updated},${v.notes},${v.use}"
      )
  }

  def update(v: REquipment): ConnectionIO[Int] = {
    val t = Table(None)
    for {
      now <- Timestamp.current[ConnectionIO]
      n <- DML
        .update(
          t,
          where(t.eid === v.eid, t.cid === v.cid),
          DML.set(
            t.cid.setTo(v.cid),
            t.name.setTo(v.name),
            t.updated.setTo(now),
            t.notes.setTo(v.notes),
            t.use.setTo(v.use)
          )
        )
    } yield n
  }

  def existsByName(coll: Ident, ename: String): ConnectionIO[Boolean] = {
    val t   = Table(None)
    val sql = run(select(count(t.eid)), from(t), where(t.cid === coll, t.name === ename))
    sql.query[Int].unique.map(_ > 0)
  }

  def findById(id: Ident): ConnectionIO[Option[REquipment]] = {
    val t   = Table(None)
    val sql = run(select(t.all), from(t), t.eid === id)
    sql.query[REquipment].option
  }

  def findAll(
      coll: Ident,
      nameQ: Option[String],
      order: Table => Column[_]
  ): ConnectionIO[Vector[REquipment]] = {
    val t = Table(None)

    val q = t.cid === coll &&? nameQ
      .map(str => s"%${str.toLowerCase}%")
      .map(v => t.name.like(v))

    val sql = Select(select(t.all), from(t), q).orderBy(order(t)).build
    sql.query[REquipment].to[Vector]
  }

  def findLike(
      coll: Ident,
      equipName: String,
      use: NonEmptyList[EquipmentUse]
  ): ConnectionIO[Vector[IdRef]] = {
    val t = Table(None)
    run(
      select(t.eid, t.name),
      from(t),
      t.cid === coll && t.name.like(equipName) && t.use.in(use)
    )
      .query[IdRef]
      .to[Vector]
  }

  def delete(id: Ident, coll: Ident): ConnectionIO[Int] = {
    val t = Table(None)
    DML.delete(t, t.eid === id && t.cid === coll)
  }
}
