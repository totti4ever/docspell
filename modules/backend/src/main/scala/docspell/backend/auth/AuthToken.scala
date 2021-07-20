/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.backend.auth

import java.time.Instant

import cats.effect._
import cats.implicits._

import docspell.backend.Common
import docspell.common._

import scodec.bits.ByteVector

case class AuthToken(nowMillis: Long, account: AccountId, salt: String, sig: String) {
  def asString = s"$nowMillis-${TokenUtil.b64enc(account.asString)}-$salt-$sig"

  def sigValid(key: ByteVector): Boolean = {
    val newSig = TokenUtil.sign(this, key)
    TokenUtil.constTimeEq(sig, newSig)
  }
  def sigInvalid(key: ByteVector): Boolean =
    !sigValid(key)

  def notExpired(validity: Duration): Boolean =
    !isExpired(validity)

  def isExpired(validity: Duration): Boolean = {
    val ends = Instant.ofEpochMilli(nowMillis).plusMillis(validity.millis)
    Instant.now.isAfter(ends)
  }

  def validate(key: ByteVector, validity: Duration): Boolean =
    sigValid(key) && notExpired(validity)

}

object AuthToken {

  def fromString(s: String): Either[String, AuthToken] =
    s.split("\\-", 4) match {
      case Array(ms, as, salt, sig) =>
        for {
          millis <- TokenUtil.asInt(ms).toRight("Cannot read authenticator data")
          acc    <- TokenUtil.b64dec(as).toRight("Cannot read authenticator data")
          accId  <- AccountId.parse(acc)
        } yield AuthToken(millis, accId, salt, sig)

      case _ =>
        Left("Invalid authenticator")
    }

  def user[F[_]: Sync](accountId: AccountId, key: ByteVector): F[AuthToken] =
    for {
      salt <- Common.genSaltString[F]
      millis = Instant.now.toEpochMilli
      cd     = AuthToken(millis, accountId, salt, "")
      sig    = TokenUtil.sign(cd, key)
    } yield cd.copy(sig = sig)

  def update[F[_]: Sync](token: AuthToken, key: ByteVector): F[AuthToken] =
    for {
      now  <- Timestamp.current[F]
      salt <- Common.genSaltString[F]
      data = AuthToken(now.toMillis, token.account, salt, "")
      sig  = TokenUtil.sign(data, key)
    } yield data.copy(sig = sig)
}
