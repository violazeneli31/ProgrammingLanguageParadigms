"""
Higher-Order Functions and Currying in Python
Programming Language Paradigms project - Viola Zeneli, Sidrit Rrustemi, Denisa Korra

Python has first-class functions but no built-in currying,
so currying is simulated with closures and partial application uses functools.partial.
"""
from functools import partial, reduce


# 1. Higher-order functions: functions that take functions as arguments
def apply_operation(operation, x, y):
    return operation(x, y)


def apply_twice(f, x):
    return f(f(x))


def add(a, b):
    return a + b


def multiply(a, b):
    return a * b


def increment(x):
    return x + 1


def double(x):
    return x * 2


def square_of(x):
    return x * x


# 2. Functions returning functions (closures)
def make_power_function(exponent):
    def power_of(base):
        return base ** exponent          # 'exponent' is remembered by the closure
    return power_of


def make_counter():
    count = 0

    def counter():
        nonlocal count                   # the closure keeps its own state
        count += 1
        return count
    return counter


# 3. map / filter / reduce
numbers = [1, 2, 3, 4, 5]
squared = list(map(lambda x: x ** 2, numbers))
even = list(filter(lambda x: x % 2 == 0, numbers))
total = reduce(lambda acc, x: acc + x, numbers, 0)
product = reduce(lambda acc, x: acc * x, numbers, 1)


# 4. Currying (simulated with nested closures)
def curried_add(a):
    def add_b(b):
        def add_c(c):
            return a + b + c
        return add_c
    return add_b


def curry(f):
    """Turns f(a, b) into f(a)(b)."""
    return lambda a: lambda b: f(a, b)


def uncurry(f):
    """Turns f(a)(b) back into f(a, b)."""
    return lambda a, b: f(a)(b)


curried_multiply = curry(multiply)
add_10 = curry(add)(10)


# 5. Partial application: fix some arguments now, give the rest later
def power(base, exponent):
    return base ** exponent


square = partial(power, exponent=2)
cube = partial(power, exponent=3)


# 6. Function composition
def compose(f, g):
    """compose(f, g)(x) = f(g(x)) - g runs first."""
    return lambda x: f(g(x))


def and_then(f, g):
    """and_then(f, g)(x) = g(f(x)) - f runs first."""
    return lambda x: g(f(x))


def pipeline(*functions):
    """Runs the functions from left to right."""
    return lambda x: reduce(lambda acc, f: f(acc), functions, x)


# 7. Real-world example: a curried logger (the log level is fixed once)
def log(level):
    def with_message(message):
        return f"[{level}] {message}"
    return with_message


log_error = log("ERROR")
log_info = log("INFO")


# 8. Real-world example: shopping cart prices as a pipeline of curried functions
def discount(percent):
    return lambda price: price * (1 - percent / 100)


def add_tax(rate):
    return lambda price: price * (1 + rate / 100)


prices = [20.00, 45.50, 9.99]
checkout = and_then(discount(10), add_tax(20))


def fmt(values):
    return "[" + ", ".join(f"{v:.2f}" for v in values) + "]"


def ints(values):
    return "[" + ", ".join(str(v) for v in values) + "]"


def main():
    # The labels are the same in the Python, Kotlin and Scala versions, so their outputs can be compared.
    print("=== Python ===")
    print("== 1. Higher-order functions ==")
    print("apply add to 3, 4 =", apply_operation(add, 3, 4))
    print("apply multiply to 3, 4 =", apply_operation(multiply, 3, 4))
    print("apply increment twice to 5 =", apply_twice(increment, 5))

    print("== 2. Functions returning functions (closures) ==")
    print("power function with exponent 3, applied to 2 =", make_power_function(3)(2))
    counter = make_counter()
    print("counter called three times =", counter(), counter(), counter())

    print("== 3. map / filter / reduce ==")
    print("numbers =", ints(numbers))
    print("squared =", ints(squared))
    print("even =", ints(even))
    print("sum =", total)
    print("product =", product)

    print("== 4. Currying ==")
    print("curried add 1, 2, 3 =", curried_add(1)(2)(3))
    print("curry(multiply) with 6, then 7 =", curried_multiply(6)(7))
    print("uncurry it again with 6, 7 =", uncurry(curried_multiply)(6, 7))
    print("add10 = curry(add)(10); add10(5) =", add_10(5))

    print("== 5. Partial application ==")
    print("square(5) =", square(5))
    print("cube(2) =", cube(2))

    print("== 6. Function composition ==")
    print("compose(double, increment)(4) =", compose(double, increment)(4))
    print("andThen(double, increment)(4) =", and_then(double, increment)(4))
    print("pipeline(increment, double, square)(4) =", pipeline(increment, double, square_of)(4))

    print("== 7. Real-world: curried logger ==")
    print(log_error("Database connection lost"))
    print(log_error("Disk almost full"))
    print(log_info("User viola logged in"))

    print("== 8. Real-world: shopping cart pipeline ==")
    print("prices =", fmt(prices))
    print("after 10% discount =", fmt(map(discount(10), prices)))
    print("final (discount, then 20% tax) =", fmt(map(checkout, prices)))
    print(f"total = {reduce(lambda acc, p: acc + p, map(checkout, prices), 0.0):.2f}")


if __name__ == "__main__":
    main()
