/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.common

import java.time.{Duration => JDur}
import java.util.concurrent.TimeUnit

import scala.concurrent.duration.{Duration => SDur, FiniteDuration}

import cats.effect.Sync
import cats.implicits._

import io.circe._

case class Duration(nanos: Long) {

  def millis: Long = nanos / 1000000

  def seconds: Long = millis / 1000

  def minutes: Long = seconds / 60

  def hours: Long = minutes / 60

  def >(other: Duration): Boolean =
    nanos > other.nanos

  def <(other: Duration): Boolean =
    nanos < other.nanos

  def toScala: FiniteDuration =
    FiniteDuration(nanos, TimeUnit.NANOSECONDS)

  def toJava: JDur =
    JDur.ofNanos(nanos)

  def formatExact: String =
    s"$millis ms"

  override def toString(): String =
    s"Duration(${millis}ms)"
}

object Duration {
  val zero = Duration(0L)

  def apply(d: SDur): Duration =
    Duration(d.toNanos)

  def apply(d: JDur): Duration =
    Duration(d.toNanos)

  def seconds(n: Long): Duration =
    apply(JDur.ofSeconds(n))

  def millis(n: Long): Duration =
    apply(JDur.ofMillis(n))

  def minutes(n: Long): Duration =
    apply(JDur.ofMinutes(n))

  def hours(n: Long): Duration =
    apply(JDur.ofHours(n))

  def days(n: Long): Duration =
    apply(JDur.ofDays(n))

  def years(n: Long): Duration =
    days(n * 365)

  def nanos(n: Long): Duration =
    Duration(n)

  def between(start: Timestamp, end: Timestamp): Duration =
    apply(JDur.between(start.value, end.value))

  def stopTime[F[_]: Sync]: F[F[Duration]] =
    for {
      now <- Timestamp.current[F]
      end = Timestamp.current[F]
    } yield end.map(e => Duration.millis(e.toMillis - now.toMillis))

  implicit val jsonEncoder: Encoder[Duration] =
    Encoder.encodeLong.contramap(_.millis)

  implicit val jsonDecoder: Decoder[Duration] =
    Decoder.decodeLong.map(Duration.millis)
}
