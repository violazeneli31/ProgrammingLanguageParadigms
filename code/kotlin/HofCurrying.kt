/*
 * Higher-Order Functions and Currying in Kotlin
 * Programming Language Paradigms project - Viola Zeneli, Sidrit Rrustemi, Denisa Korra
 *
 * Kotlin has function types like (Int, Int) -> Int, lambdas and function references (::add).
 * It has no built-in currying, but a function can return another function,
 * so currying and partial application are written by hand.
 */
import java.util.Locale

// 1. Higher-order functions: functions that take functions as parameters
fun applyOperation(operation: (Int, Int) -> Int, x: Int, y: Int): Int = operation(x, y)

fun applyTwice(f: (Int) -> Int, x: Int): Int = f(f(x))

fun add(a: Int, b: Int) = a + b
fun multiply(a: Int, b: Int) = a * b

val increment = { x: Int -> x + 1 }
val double = { x: Int -> x * 2 }
val squareOf = { x: Int -> x * x }

// 2. Functions returning functions (closures)
fun power(base: Int, exponent: Int): Int {
    var result = 1
    repeat(exponent) { result *= base }
    return result
}

fun makePowerFunction(exponent: Int): (Int) -> Int = { base -> power(base, exponent) }

fun makeCounter(): () -> Int {
    var count = 0                    // captured by the lambda: the closure keeps its own state
    return { ++count }
}

// 3. map / filter / reduce (fold)
val numbers = listOf(1, 2, 3, 4, 5)
val squared = numbers.map { it * it }
val even = numbers.filter { it % 2 == 0 }
val total = numbers.fold(0) { acc, x -> acc + x }
val product = numbers.reduce { acc, x -> acc * x }

// 4. Currying
fun curriedAdd(a: Int): (Int) -> (Int) -> Int = { b -> { c -> a + b + c } }

/** Turns f(a, b) into f(a)(b). */
fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = { a -> { b -> f(a, b) } }

/** Turns f(a)(b) back into f(a, b). */
fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C = { a, b -> f(a)(b) }

val curriedMultiply = curry(::multiply)
val add10 = curry(::add)(10)

// 5. Partial application: fix the second argument now, give the first later
fun <A, B, C> partialRight(f: (A, B) -> C, b: B): (A) -> C = { a -> f(a, b) }

val square = partialRight(::power, 2)
val cube = partialRight(::power, 3)

// 6. Function composition
/** compose(f, g)(x) = f(g(x)) - g runs first. */
fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { x -> f(g(x)) }

/** andThen(f, g)(x) = g(f(x)) - f runs first. */
fun <A, B, C> andThen(f: (A) -> B, g: (B) -> C): (A) -> C = { x -> g(f(x)) }

/** Runs the functions from left to right. */
fun <T> pipeline(vararg functions: (T) -> T): (T) -> T = { x -> functions.fold(x) { acc, f -> f(acc) } }

// 7. Real-world example: a curried logger (the log level is fixed once)
fun log(level: String): (String) -> String = { message -> "[$level] $message" }

val logError = log("ERROR")
val logInfo = log("INFO")

// 8. Real-world example: shopping cart prices as a pipeline of curried functions
fun discount(percent: Double): (Double) -> Double = { price -> price * (1 - percent / 100) }
fun addTax(rate: Double): (Double) -> Double = { price -> price * (1 + rate / 100) }

val prices = listOf(20.00, 45.50, 9.99)
val checkout = andThen(discount(10.0), addTax(20.0))

fun fmt(values: List<Double>) = values.joinToString(", ", "[", "]") { String.format(Locale.US, "%.2f", it) }
fun ints(values: List<Int>) = values.joinToString(", ", "[", "]")

fun main() {
    // The labels are the same in the Python, Kotlin and Scala versions, so their outputs can be compared.
    println("=== Kotlin ===")
    println("== 1. Higher-order functions ==")
    println("apply add to 3, 4 = ${applyOperation(::add, 3, 4)}")
    println("apply multiply to 3, 4 = ${applyOperation(::multiply, 3, 4)}")
    println("apply increment twice to 5 = ${applyTwice(increment, 5)}")

    println("== 2. Functions returning functions (closures) ==")
    println("power function with exponent 3, applied to 2 = ${makePowerFunction(3)(2)}")
    val counter = makeCounter()
    println("counter called three times = ${counter()} ${counter()} ${counter()}")

    println("== 3. map / filter / reduce ==")
    println("numbers = ${ints(numbers)}")
    println("squared = ${ints(squared)}")
    println("even = ${ints(even)}")
    println("sum = $total")
    println("product = $product")

    println("== 4. Currying ==")
    println("curried add 1, 2, 3 = ${curriedAdd(1)(2)(3)}")
    println("curry(multiply) with 6, then 7 = ${curriedMultiply(6)(7)}")
    println("uncurry it again with 6, 7 = ${uncurry(curriedMultiply)(6, 7)}")
    println("add10 = curry(add)(10); add10(5) = ${add10(5)}")

    println("== 5. Partial application ==")
    println("square(5) = ${square(5)}")
    println("cube(2) = ${cube(2)}")

    println("== 6. Function composition ==")
    println("compose(double, increment)(4) = ${compose(double, increment)(4)}")
    println("andThen(double, increment)(4) = ${andThen(double, increment)(4)}")
    println("pipeline(increment, double, square)(4) = ${pipeline(increment, double, squareOf)(4)}")

    println("== 7. Real-world: curried logger ==")
    println(logError("Database connection lost"))
    println(logError("Disk almost full"))
    println(logInfo("User viola logged in"))

    println("== 8. Real-world: shopping cart pipeline ==")
    println("prices = ${fmt(prices)}")
    println("after 10% discount = ${fmt(prices.map(discount(10.0)))}")
    println("final (discount, then 20% tax) = ${fmt(prices.map(checkout))}")
    println("total = ${String.format(Locale.US, "%.2f", prices.map(checkout).fold(0.0) { acc, p -> acc + p })}")
}
