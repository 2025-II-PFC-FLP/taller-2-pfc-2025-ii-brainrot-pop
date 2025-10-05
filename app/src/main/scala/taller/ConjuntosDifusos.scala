package app

class ConjuntosDifusos() {

  type ConjDifuso = Int => Double

  def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)

  def grande(d: Int, e: Int): ConjDifuso = {
    (x: Int) => math.pow(x.toDouble / (x + d).toDouble, e.toDouble)
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    (x: Int) => 1.0 - c(x)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => math.max(cd1(x), cd2(x))
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    (x: Int) => math.min(cd1(x), cd2(x))
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    def aux(i: Int): Boolean = i match {
      case n if n > 1000 => true
      case n if cd1(n) > cd2(n) => false
      case _ => aux(i + 1)
    }

    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    // Implementaci´on de la funci´on igualdad
    def aux(i: Int): Boolean = {
      if (i > 1000) true
      else if (cd1(i) == cd2(i)) aux(i + 1)
      else false
    }
  }
}
