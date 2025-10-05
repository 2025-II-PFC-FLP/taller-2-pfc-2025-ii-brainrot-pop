package taller

class ConjuntosDifusos {

  type ConjDifuso = Int => Double

  def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)

  def grande(d: Int, e: Int): ConjDifuso = {
    x => math.pow(x.toDouble / (x + d).toDouble, e.toDouble)
  }

  def complemento(c: ConjDifuso): ConjDifuso = {
    x => 1.0 - c(x)
  }

  def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    x => math.max(cd1(x), cd2(x))
  }

  def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso = {
    x => math.min(cd1(x), cd2(x))
  }

  def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    @annotation.tailrec
    def aux(i: Int): Boolean = {
      if (i > 1000) true
      else if (cd1(i) > cd2(i)) false
      else aux(i + 1)
    }
    aux(0)
  }

  def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
    inclusion(cd1, cd2) && inclusion(cd2, cd1)
  }
}
