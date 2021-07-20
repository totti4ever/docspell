/*
 * Copyright 2020 Docspell Contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package docspell.store.qb.impl

import docspell.store.qb._

import doobie._
import doobie.implicits._

object SelectExprBuilder extends CommonBuilder {

  def build(expr: SelectExpr): Fragment =
    expr match {
      case SelectExpr.SelectColumn(col, alias) =>
        column(col) ++ appendAs(alias)

      case s @ SelectExpr.SelectConstant(value, aliasOpt) =>
        ConditionBuilder.buildValue(value)(s.P) ++ appendAs(aliasOpt)

      case SelectExpr.SelectLiteral(value, alias) =>
        Fragment.const(value) ++ appendAs(alias)

      case SelectExpr.SelectFun(fun, alias) =>
        DBFunctionBuilder.build(fun) ++ appendAs(alias)

      case SelectExpr.SelectQuery(query, alias) =>
        sql"(" ++ SelectBuilder.build(query) ++ sql")" ++ appendAs(alias)

      case SelectExpr.SelectCondition(cond, alias) =>
        sql"(" ++ ConditionBuilder.build(cond) ++ sql")" ++ appendAs(alias)
    }

}
