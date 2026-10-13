/*
 * Higher-Order Functions and Currying in Scala
 * Programming Language Paradigms project - Viola Zeneli, Sidrit Rrustemi, Denisa Korra
 *
 * Scala supports currying natively: a method can have several parameter lists, f(a)(b)(c),
 * every function value has .curried / Function.uncurried, and "_" creates partial applications.
 */
import java.util.Locale

object HofCurrying {

  // 1. Higher-order functions: functions that take functions as parameters
  def applyOperation(operation: (Int, Int) => Int, x: Int, y: Int): Int = operation(x, y)

  def applyTwice(f: Int => Int, x: Int): Int = f(f(x))

  def add(a: Int, b: Int): Int = a + b
  def multiply(a: Int, b: Int): Int = a * b

  val increment: Int => Int = x => x + 1
  val double: Int => Int = x => x * 2
  val squareOf: Int => Int = x => x * x

  // 2. Functions returning functions (closures)
  def power(base: Int, exponent: Int): Int = (1 to exponent).foldLeft(1)((acc, _) => acc * base)

  def makePowerFunction(exponent: Int): Int => Int = base => power(base, exponent)

  def makeCounter(): () => Int = {
    var count = 0                    // captured by the function: the closure keeps its own state
    () => { count += 1; count }
  }

  // 3. map / filter / reduce (fold)
  val numbers: List[Int] = List(1, 2, 3, 4, 5)
  val squared: List[Int] = numbers.map(x => x * x)
  val even: List[Int] = numbers.filter(_ % 2 == 0)
  val total: Int = numbers.foldLeft(0)(_ + _)
  val product: Int = numbers.reduce(_ * _)

  // 4. Currying - native: several parameter lists
  def curriedAdd(a: Int)(b: Int)(c: Int): Int = a + b + c

  val multiplyF: (Int, Int) => Int = multiply
  val curriedMultiply: Int => Int => Int = multiplyF.curried                      // built in
  val uncurriedMultiply: (Int, Int) => Int = Function.uncurried(curriedMultiply)  // built in
  val addF: (Int, Int) => Int = add
  val add10: Int => Int = addF.curried(10)

  // 5. Partial application with "_"
  val square: Int => Int = power(_, 2)
  val cube: Int => Int = power(_, 3)

  // 6. Function composition - built in as compose / andThen
  def pipeline[T](functions: (T => T)*): T => T = x => functions.foldLeft(x)((acc, f) => f(acc))

  // 7. Real-world example: a curried logger (the log level is fixed once)
  def log(level: String)(message: String): String = s"[$level] $message"

  val logError: String => String = log("ERROR")
  val logInfo: String => String = log("INFO")

  // 8. Real-world example: shopping cart prices as a pipeline of curried functions
  def discount(percent: Double)(price: Double): Double = price * (1 - percent / 100)
  def addTax(rate: Double)(price: Double): Double = price * (1 + rate / 100)

  val prices: List[Double] = List(20.00, 45.50, 9.99)
  val checkout: Double => Double = (discount(10) _).andThen(addTax(20))

  def fmt(values: List[Double]): String = values.map(v => "%.2f".formatLocal(Locale.US, v)).mkString("[", ", ", "]")
  def ints(values: List[Int]): String = values.mkString("[", ", ", "]")

  def main(args: Array[String]): Unit = {
    // The labels are the same in the Python, Kotlin and Scala versions, so their outputs can be compared.
    println("=== Scala ===")
    println("== 1. Higher-order functions ==")
    println(s"apply add to 3, 4 = ${applyOperation(add, 3, 4)}")
    println(s"apply multiply to 3, 4 = ${applyOperation(multiply, 3, 4)}")
    println(s"apply increment twice to 5 = ${applyTwice(increment, 5)}")

    println("== 2. Functions returning functions (closures) ==")
    println(s"power function with exponent 3, applied to 2 = ${makePowerFunction(3)(2)}")
    val counter = makeCounter()
    println(s"counter called three times = ${counter()} ${counter()} ${counter()}")

    println("== 3. map / filter / reduce ==")
    println(s"numbers = ${ints(numbers)}")
    println(s"squared = ${ints(squared)}")
    println(s"even = ${ints(even)}")
    println(s"sum = $total")
    println(s"product = $product")

    println("== 4. Currying ==")
    println(s"curried add 1, 2, 3 = ${curriedAdd(1)(2)(3)}")
    println(s"curry(multiply) with 6, then 7 = ${curriedMultiply(6)(7)}")
    println(s"uncurry it again with 6, 7 = ${uncurriedMultiply(6, 7)}")
    println(s"add10 = curry(add)(10); add10(5) = ${add10(5)}")

    println("== 5. Partial application ==")
    println(s"square(5) = ${square(5)}")
    println(s"cube(2) = ${cube(2)}")

    println("== 6. Function composition ==")
    println(s"compose(double, increment)(4) = ${double.compose(increment)(4)}")
    println(s"andThen(double, increment)(4) = ${double.andThen(increment)(4)}")
    println(s"pipeline(increment, double, square)(4) = ${pipeline(increment, double, squareOf)(4)}")

    println("== 7. Real-world: curried logger ==")
    println(logError("Database connection lost"))
    println(logError("Disk almost full"))
    println(logInfo("User viola logged in"))

    println("== 8. Real-world: shopping cart pipeline ==")
    println(s"prices = ${fmt(prices)}")
    println(s"after 10% discount = ${fmt(prices.map(discount(10)))}")
    println(s"final (discount, then 20% tax) = ${fmt(prices.map(checkout))}")
    println(s"total = ${"%.2f".formatLocal(Locale.US, prices.map(checkout).foldLeft(0.0)(_ + _))}")
  }
}
