package fix.scala213

import scalafix.v1._

import scala.meta._

object Any2StringAdd {
  val any2stringaddPlusString = SymbolMatcher.exact("scala/Predef.any2stringadd#`+`().")
  val primitivePlusString = SymbolMatcher.exact(
    "scala/Byte#`+`().",
    "scala/Short#`+`().",
    "scala/Char#`+`().",
    "scala/Int#`+`().",
    "scala/Long#`+`().",
    "scala/Float#`+`().",
    "scala/Double#`+`().",
  )
}

final class Any2StringAdd extends SemanticRule("fix.scala213.Any2StringAdd") {
  import Any2StringAdd._

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case any2stringaddPlusString(ap @ Term.ApplyInfix(lhs, _, _, Seq(arg))) =>
        lhs.symbol.info.map(_.signature) match {
          case Some(MethodSignature(_, _, TypeRef(_, sym, _))) if sym.value == "scala/Boolean#" =>
            interpolation(lhs, ap, arg)
          case _ =>
            addToString(lhs)
        }
      case primitivePlusString(ap @ Term.ApplyInfix(lhs, _, _, Seq(arg))) =>
        interpolation(lhs, ap, arg)
    }.asPatch
  }

  private def addToString(term: Term)(implicit d: SemanticDocument) = term match {
    case _: Term.Name | _: Term.Select | _: Term.Block =>
      Patch.addRight(term, ".toString")
    case _ =>
      Patch.addLeft(term, "(") + Patch.addRight(term, ").toString")
  }

  private def interpolation(term: Term, ap: Term, arg: Term) = {
    arg match {
      case Lit.String(s) =>
        Patch.replaceTree(
          ap,
          "s\"${" + term + "}" + s + "\""
        )
      case _ =>
        Patch.replaceTree(
          ap,
          "s\"${" + term + "}${" + arg + "}\""
        )
    }
  }
}
