package fix.scala213

abstract class Any2StringAdd {
  // Strings: leave as-is, both literal strings and non-literal
  def s = "bob"
  def str1 = s + s
  def str2 = s + "bob"
  def str3 = "bob" + s
  def str4 = "bob" + "fred"

  // Non-strings: add toString
  def nil = Nil.toString + s

  // Non-string, generic type: add toString
  type A
  def x: A
  def generic = x.toString + "bob"

  // Primitives: add toString
  def unit = ()
  def bool = true
  def byte = 1.toByte
  def short = 1.toShort
  def char = 'a'
  def int = 1
  def long = 1L
  def float = 1.0F
  def double = 1.0
  //
  def unit1 = unit.toString + s
  def bool1 = s"${bool}${s}"
  def bool2 = s"${bool}x"
  def byte1 = s"${byte}${s}"
  def byte2 = byte + byte
  def byte3 = s"${byte}x"
  def short1 = s"${short}${s}"
  def short2 = short + short
  def short3 = s"${short}x"
  def char1 = s"${char}${s}"
  def char2 = char + char
  def char3 = s"${char}x"
  def int1 = s"${int}${s}"
  def int2 = int + int
  def int3 = s"${int}x"
  def long1 = s"${long}${s}"
  def long2 = long + long
  def long3 = s"${long}x"
  def float1 = s"${float}${s}"
  def float2 = float + float
  def float3 = s"${float}x"
  def double1 = s"${double}${s}"
  def double2 = double + double
  def double3 = s"${double}x"

  // With infix operators, make sure to use parens
  def parens1 = (Nil ++ Nil).toString + s
  def parens2 = s"${int + int}${s}"
  def parens3 = {Nil ++ Nil}.toString + s
}
