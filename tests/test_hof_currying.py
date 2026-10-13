"""Unit tests for the Python version.  Run from the project folder:  python3 -m unittest discover tests"""
import os
import sys
import unittest

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "code", "python"))
import hof_currying as h  # noqa: E402


class HigherOrderFunctionsTest(unittest.TestCase):
    def test_functions_as_arguments(self):
        self.assertEqual(h.apply_operation(h.add, 3, 4), 7)
        self.assertEqual(h.apply_operation(h.multiply, 3, 4), 12)
        self.assertEqual(h.apply_twice(h.increment, 5), 7)

    def test_closures(self):
        self.assertEqual(h.make_power_function(3)(2), 8)
        counter = h.make_counter()
        self.assertEqual([counter(), counter(), counter()], [1, 2, 3])
        self.assertEqual(h.make_counter()(), 1)          # a new counter has its own state

    def test_map_filter_reduce(self):
        self.assertEqual(h.squared, [1, 4, 9, 16, 25])
        self.assertEqual(h.even, [2, 4])
        self.assertEqual((h.total, h.product), (15, 120))


class CurryingTest(unittest.TestCase):
    def test_curried_function(self):
        self.assertEqual(h.curried_add(1)(2)(3), 6)
        self.assertTrue(callable(h.curried_add(1)))       # every step returns a function

    def test_curry_and_uncurry_are_opposites(self):
        self.assertEqual(h.curry(h.multiply)(6)(7), 42)
        self.assertEqual(h.uncurry(h.curry(h.multiply))(6, 7), h.multiply(6, 7))
        self.assertEqual(h.add_10(5), 15)

    def test_partial_application(self):
        self.assertEqual(h.square(5), 25)
        self.assertEqual(h.cube(2), 8)


class CompositionTest(unittest.TestCase):
    def test_order_of_composition(self):
        self.assertEqual(h.compose(h.double, h.increment)(4), 10)   # double(increment(4))
        self.assertEqual(h.and_then(h.double, h.increment)(4), 9)   # increment(double(4))
        self.assertEqual(h.pipeline(h.increment, h.double, h.square_of)(4), 100)


class RealWorldTest(unittest.TestCase):
    def test_logger(self):
        self.assertEqual(h.log_error("Disk full"), "[ERROR] Disk full")
        self.assertEqual(h.log("WARN")("Low memory"), "[WARN] Low memory")

    def test_shopping_cart(self):
        self.assertAlmostEqual(h.discount(10)(20.0), 18.0)
        self.assertAlmostEqual(h.add_tax(20)(18.0), 21.6)
        self.assertAlmostEqual(sum(map(h.checkout, h.prices)), 81.53, places=2)


if __name__ == "__main__":
    unittest.main()
