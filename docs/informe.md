## Diseño y representación

Elegimos representar un conjunto difuso como una función:

```scala
type ConjDifuso = Int => Double
```

Esto cumple con la definición matemática de conjunto difuso: para cada elemento $x$ del universo (aquí enteros), la función devuelve $\mu_S(x) \in [0,1]$, el grado de pertenencia.

### Ventajas de esta representación

* Es pura: no hay estructura mutable, ni listas gigantes que iterar.
* Permite componer funciones (altísimo uso de funciones de orden superior).
* Se adapta al estilo funcional pedido por el profe y facilita tests unitarios.

## Explicación detallada de cada función

A continuación explico cada función del archivo `ConjuntosDifusos.scala` y su motivación.

### `pertenece(elem: Int, s: ConjDifuso): Double`

Devuelve el grado de pertenencia de `elem` en el conjunto `s` llamando a la función característica:

```scala
def pertenece(elem: Int, s: ConjDifuso): Double = s(elem)
```

Es simplemente un alias semántico que mejora la legibilidad del código y del informe.

### `grande(d: Int, e: Int): ConjDifuso`

Esta función construye un conjunto difuso que modela la idea "números grandes" mediante la fórmula:

[
\mu(n) = \left(\frac{n}{n + d}\right)^{e}
]

donde $d \ge 1$ controla la suavidad del umbral y $e \ge 1$ enfatiza la cercanía a 1. En el código se implementa como:

```scala
def grande(d: Int, e: Int): ConjDifuso =
  (x: Int) => math.pow(x.toDouble / (x + d).toDouble, e.toDouble)
```

**Interpretación:** para $n$ muy grande en comparación con $d$, la fracción $n/(n+d) \approx 1$, y por tanto $\mu(n) \approx 1$. Si $n$ es pequeño (cercano a 0), entonces $\mu(n)$ será cercano a 0.

**Precaución:** `d` debe ser al menos 1 para evitar división por cero. Si se quisiera protección extra, se podría asegurar `d >= 1` antes de construir la función, pero el enunciado asume valores razonables.

### `complemento(c: ConjDifuso): ConjDifuso`

El complemento de un conjunto difuso $S$ se define como $\mu_{\neg S}(x) = 1 - \mu_S(x)$. En código:

```scala
def complemento(c: ConjDifuso): ConjDifuso =
  (x: Int) => 1.0 - c(x)
```

Esto garantiza que si un elemento pertenece totalmente a `S` ($\mu_S(x)=1$), en el complemento su grado es 0, y viceversa.

### `union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso`

La unión en teoría de conjuntos difusos clásica se define con el máximo punto a punto:

[
\mu_{S_1 \cup S_2}(x) = \max\big(\mu_{S_1}(x), \mu_{S_2}(x)\big)
]

Implementación:

```scala
def union(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso =
  (x: Int) => math.max(cd1(x), cd2(x))
```

### `interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso`

Análogo a la unión, la intersección usa el mínimo punto a punto:

[
\mu_{S_1 \cap S_2}(x) = \min\big(\mu_{S_1}(x), \mu_{S_2}(x)\big)
]

Código:

```scala
def interseccion(cd1: ConjDifuso, cd2: ConjDifuso): ConjDifuso =
  (x: Int) => math.min(cd1(x), cd2(x))
```

### `inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean`

Matemáticamente: $S_1 \subseteq S_2$ si y solo si para todo $x$:

[
\mu_{S_1}(x) \le \mu_{S_2}(x)
]

Como no podemos iterar sobre los enteros infinitos, el enunciado fija el universo de búsqueda en $[0,1000]$. La implementación recorre este intervalo con recursión (estilo del profe):

```scala
def inclusion(cd1: ConjDifuso, cd2: ConjDifuso): Boolean = {
  def aux(i: Int): Boolean = i match {
    case n if n > 1000 => true
    case n if cd1(n) > cd2(n) => false
    case _ => aux(i + 1)
  }
  aux(0)
}
```

**Estado de la pila de llamados (ejemplo):** supongamos que al evaluar `inclusion(cd1, cd2)` los primeros valores cumplen la desigualdad y en `i = 3` encontramos un contraejemplo. La pila ficticia sería:

```mermaid
graph TD
  subgraph Pila_de_llamados
    F0[aux(0): esperando]
    F1[aux(1): esperando]
    F2[aux(2): esperando]
    F3[aux(3): detecta cd1(3) > cd2(3) => false]
  end
  F0 --> F1
  F1 --> F2
  F2 --> F3
```

En la ejecución real la llamada `aux(0)` empuja `aux(1)`, que empuja `aux(2)`, etc. Al encontrar `false`, la función retorna y se desapilan las llamadas.

### `igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean`

Se define por doble inclusión:

[
S_1 = S_2 \iff S_1 \subseteq S_2 \land S_2 \subseteq S_1
]

Implementación:

```scala
def igualdad(cd1: ConjDifuso, cd2: ConjDifuso): Boolean =
  inclusion(cd1, cd2) && inclusion(cd2, cd1)
```


## Análisis de complejidad

* `pertenece`, `grande`, `complemento`, `union`, `interseccion`: tiempo $O(1)$ por evaluación en un entero dado (solo operaciones aritméticas y calls a `math`).
* `inclusion`: en el peor caso revisa 1001 valores (0..1000), por tanto $O(1000) = O(1)$ si lo vemos como constante límite, pero más útil decir $O(N)$ con $N=1001$ en término práctico.


