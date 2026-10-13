# Higher-Order Functions and Currying in Python, Kotlin and Scala

Programming Language Paradigms course project (2024–2025).

The project compares how three languages support two core ideas of functional programming:

- **Higher-order functions (HOFs):** functions that take other functions as arguments or return a function.
- **Currying:** turning a function with several arguments into a chain of one-argument functions, `add(a, b)` → `add(a)(b)`. This makes **partial application** possible: fix some arguments now and give the rest later.

The same program is written in all three languages, so the differences between them are easy to see. All three versions print exactly the same output.

---

## What's in the repository

| Folder / file | Contents |
|---|---|
| `code/python/hof_currying.py` | Python version |
| `code/kotlin/HofCurrying.kt` | Kotlin version |
| `code/scala/HofCurrying.scala` | Scala version |
| `tests/test_hof_currying.py` | Unit tests for the Python version |
| `tests/expected_output.txt` | The output all three versions must print |
| `run_all.sh` | Runs the three versions and checks their output |
| `docs/` | Project report (Word, PDF) and presentation (PowerPoint, PDF) |

---

## What each program shows

Each file has the same eight sections:

1. **Higher-order functions:** `applyOperation(add, 3, 4)`, `applyTwice(increment, 5)`
2. **Functions returning functions (closures):** a power function factory and a counter that keeps its own state
3. **map / filter / reduce** on a list of numbers
4. **Currying:** a curried `add(1)(2)(3)`, plus generic `curry` and `uncurry` helpers
5. **Partial application:** `square` and `cube` made from `power(base, exponent)`
6. **Function composition:** `compose`, `andThen` and a `pipeline` of functions
7. **Real-world example, a curried logger:** the log level is fixed once, `logError = log("ERROR")`
8. **Real-world example, a shopping cart:** prices go through a pipeline of curried functions (discount, then tax)

---

## How the three languages compare

| Feature | Python | Kotlin | Scala |
|---|---|---|---|
| Functions as values | Yes, every function is an object | Yes, function types like `(Int, Int) -> Int` and references like `::add` | Yes, function types like `(Int, Int) => Int` |
| Lambdas | `lambda x: x + 1` (one expression only) | `{ x -> x + 1 }`, `it`, trailing lambdas | `x => x + 1`, `_ + 1` |
| Closures with state | `nonlocal` needed to change a captured variable | Captured `var` can be changed directly | Captured `var` can be changed directly |
| map / filter / reduce | Built-in `map`, `filter`, `functools.reduce` | Collection methods `map`, `filter`, `fold`, `reduce` | Collection methods `map`, `filter`, `foldLeft`, `reduce` |
| Currying | Not native: simulated with nested functions | Not native: functions returning lambdas | **Native:** several parameter lists `f(a)(b)(c)`, plus `.curried` / `Function.uncurried` |
| Partial application | `functools.partial(power, exponent=2)` | Written by hand (`partialRight`) | Built in with `_`: `power(_, 2)` |
| Composition | Written by hand | Written by hand | Built in: `f.compose(g)`, `f.andThen(g)` |
| Type checking | Dynamic (at run time) | Static, with type inference | Static, with type inference |

**In short:** Python makes functional code easy to write but has no native currying. Kotlin is type-safe and concise, but currying and composition are written by hand. Scala supports all of these concepts natively and is the most "functional" of the three.

---

## How to run

**Python 3:**
```
python3 code/python/hof_currying.py
python3 -m unittest discover tests
```

**Kotlin** (needs `kotlinc`, e.g. `brew install kotlin`):
```
kotlinc code/kotlin/HofCurrying.kt -include-runtime -d HofCurrying.jar
java -jar HofCurrying.jar
```

**Scala 3** (e.g. `brew install scala`):
```
scala run code/scala/HofCurrying.scala
```

**All three at once**, also checking that every version prints `tests/expected_output.txt`:
```
./run_all.sh
```

You can also open the files in an IDE: IntelliJ IDEA for Kotlin and Scala, PyCharm or VS Code for Python.

---

## Team

Viola Zeneli (Kotlin), Sidrit Rrustemi (Scala), Denisa Korra (Python)

## Lecturer

Dr. Erind Bedalli
