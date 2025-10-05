package taller

import org.scalatest.funsuite.AnyFunSuite
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ConjuntosDifusosTest extends AnyFunSuite {

  val cd = new ConjuntosDifusos()

  // --- Pruebas para grande(d, e) ---
  test("grande debe retornar valores crecientes conforme aumenta x") {
    val g1 = cd.grande(2, 2)
    assert(g1(1) < g1(5))
    assert(g1(5) < g1(10))
  }

  // --- Pruebas para complemento ---
  test("complemento debe invertir correctamente los valores") {
    val g1 = cd.grande(2, 2)
    val c1 = cd.complemento(g1)
    val x = 5
    assert(math.abs(c1(x) - (1.0 - g1(x))) < 0.0001)
  }

  // --- Pruebas para unión ---
  test("union debe devolver el máximo valor entre los conjuntos") {
    val g1 = cd.grande(2, 2)
    val g2 = cd.grande(5, 3)
    val unionSet = cd.union(g1, g2)
    val x = 5
    assert(unionSet(x) == math.max(g1(x), g2(x)))
  }

  // --- Pruebas para intersección ---
  test("interseccion debe devolver el mínimo valor entre los conjuntos") {
    val g1 = cd.grande(2, 2)
    val g2 = cd.grande(5, 3)
    val interSet = cd.interseccion(g1, g2)
    val x = 5
    assert(interSet(x) == math.min(g1(x), g2(x)))
  }

  // --- Pruebas para inclusión ---
  test("inclusion debe retornar true si todos los valores de cd1 <= cd2") {
    val g1 = cd.grande(2, 2)
    val g2 = cd.grande(5, 3)
    val interSet = cd.interseccion(g1, g2)
    assert(cd.inclusion(interSet, g1)) // true
    assert(!cd.inclusion(g1, interSet)) // false
  }

  // --- Pruebas para igualdad ---
  test("igualdad debe retornar true para conjuntos iguales") {
    val g1 = cd.grande(2, 2)
    val g2 = cd.grande(2, 2)
    assert(cd.igualdad(g1, g2))
  }

  test("igualdad debe retornar false para conjuntos diferentes") {
    val g1 = cd.grande(2, 2)
    val g2 = cd.grande(5, 3)
    assert(!cd.igualdad(g1, g2))
  }
}
