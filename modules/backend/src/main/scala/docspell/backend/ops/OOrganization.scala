/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.backend.ops

import cats.effect.{Async, Resource}
import cats.implicits._

import docspell.backend.ops.OOrganization._
import docspell.common._
import docspell.store._
import docspell.store.queries.QOrganization
import docspell.store.records._

trait OOrganization[F[_]] {
  def findAllOrg(account: AccountId, query: Option[String]): F[Vector[OrgAndContacts]]
  def findOrg(account: AccountId, orgId: Ident): F[Option[OrgAndContacts]]

  def findAllOrgRefs(account: AccountId, nameQuery: Option[String]): F[Vector[IdRef]]

  def addOrg(s: OrgAndContacts): F[AddResult]

  def updateOrg(s: OrgAndContacts): F[AddResult]

  def findAllPerson(
      account: AccountId,
      query: Option[String]
  ): F[Vector[PersonAndContacts]]

  def findPerson(account: AccountId, persId: Ident): F[Option[PersonAndContacts]]

  def findAllPersonRefs(account: AccountId, nameQuery: Option[String]): F[Vector[IdRef]]

  /** Add a new person with their contacts. The additional organization is ignored. */
  def addPerson(s: PersonAndContacts): F[AddResult]

  /** Update a person with their contacts. The additional organization is ignored. */
  def updatePerson(s: PersonAndContacts): F[AddResult]

  def deleteOrg(orgId: Ident, collective: Ident): F[AddResult]

  def deletePerson(personId: Ident, collective: Ident): F[AddResult]
}

object OOrganization {

  case class OrgAndContacts(org: ROrganization, contacts: Seq[RContact])

  case class PersonAndContacts(
      person: RPerson,
      org: Option[ROrganization],
      contacts: Seq[RContact]
  )

  def apply[F[_]: Async](store: Store[F]): Resource[F, OOrganization[F]] =
    Resource.pure[F, OOrganization[F]](new OOrganization[F] {

      def findAllOrg(
          account: AccountId,
          query: Option[String]
      ): F[Vector[OrgAndContacts]] =
        store
          .transact(QOrganization.findOrgAndContact(account.collective, query, _.name))
          .map({ case (org, cont) => OrgAndContacts(org, cont) })
          .compile
          .toVector

      def findOrg(account: AccountId, orgId: Ident): F[Option[OrgAndContacts]] =
        store
          .transact(QOrganization.getOrgAndContact(account.collective, orgId))
          .map(_.map({ case (org, cont) => OrgAndContacts(org, cont) }))

      def findAllOrgRefs(
          account: AccountId,
          nameQuery: Option[String]
      ): F[Vector[IdRef]] =
        store.transact(ROrganization.findAllRef(account.collective, nameQuery, _.name))

      def addOrg(s: OrgAndContacts): F[AddResult] =
        QOrganization.addOrg(s.org, s.contacts, s.org.cid)(store)

      def updateOrg(s: OrgAndContacts): F[AddResult] =
        QOrganization.updateOrg(s.org, s.contacts, s.org.cid)(store)

      def findAllPerson(
          account: AccountId,
          query: Option[String]
      ): F[Vector[PersonAndContacts]] =
        store
          .transact(QOrganization.findPersonAndContact(account.collective, query, _.name))
          .map({ case (person, org, cont) => PersonAndContacts(person, org, cont) })
          .compile
          .toVector

      def findPerson(account: AccountId, persId: Ident): F[Option[PersonAndContacts]] =
        store
          .transact(QOrganization.getPersonAndContact(account.collective, persId))
          .map(_.map({ case (pers, org, cont) => PersonAndContacts(pers, org, cont) }))

      def findAllPersonRefs(
          account: AccountId,
          nameQuery: Option[String]
      ): F[Vector[IdRef]] =
        store.transact(RPerson.findAllRef(account.collective, nameQuery, _.name))

      def addPerson(s: PersonAndContacts): F[AddResult] =
        QOrganization.addPerson(s.person, s.contacts, s.person.cid)(store)

      def updatePerson(s: PersonAndContacts): F[AddResult] =
        QOrganization.updatePerson(s.person, s.contacts, s.person.cid)(store)

      def deleteOrg(orgId: Ident, collective: Ident): F[AddResult] =
        store
          .transact(QOrganization.deleteOrg(orgId, collective))
          .attempt
          .map(AddResult.fromUpdate)

      def deletePerson(personId: Ident, collective: Ident): F[AddResult] =
        store
          .transact(QOrganization.deletePerson(personId, collective))
          .attempt
          .map(AddResult.fromUpdate)

    })
}
