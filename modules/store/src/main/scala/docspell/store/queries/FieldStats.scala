/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.store.queries

import docspell.store.records.RCustomField

case class FieldStats(
    field: RCustomField,
    count: Int,
    avg: BigDecimal,
    sum: BigDecimal,
    max: BigDecimal,
    min: BigDecimal
)

object FieldStats {
  def apply(field: RCustomField, count: Int): FieldStats =
    FieldStats(field, count, BigDecimal(0), BigDecimal(0), BigDecimal(0), BigDecimal(0))
}
