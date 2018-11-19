import unittest
import functions as f
import numpy as np

class Tests(unittest.TestCase):
    def test_assertTrue(self):
        res = f.funcPlus(3, 5)
        self.assertTrue(res == 8)

    def test_assertFalse(self):
        res = f.funcPlus(3, 5)
        self.assertFalse(res == 10)

    def test_assertIn(self):
        list = [1, 2, 3]
        f.funcAdd(list, 4)
        self.assertIn(4, list)

    def test_assertNotIn(self):
        list = [1, 2, 3]
        f.funcAdd(list, 5)
        self.assertNotIn(4, list)

    def test_assertGreater(self):
        res = f.funcMult(5, 5)
        self.assertGreater(res, 24)

    def test_assertLess(self):
        res = f.funcMult(5, 5)
        self.assertLess(res, 26)

    def test_assertCountEqual(self):
        list1 = [1, 2, 3]
        list2 = [2, 1, 3]
        f.funcAdd(list1, 4)
        f.funcAdd(list2, 4)
        self.assertCountEqual(list1, list2)

    def test_generating05(self):
        vector = np.random.uniform(0.5, 1, 10)
        for i in range(10):
            self.assertGreaterEqual(vector[i], 0.5)

    def test_findDateTrue(self):
        str_tests_yes = ['с тех пор прошло 3 года',
                         'бюджет в 2018 году',
                         'в 6 веке',
                         '5 марта',
                         'почти половина мужчин не доживают до 65 лет',
                         'в 1897 г',
                         '1978 до нашей эры',
                         '‘в мае 2018 года']

        for i in range(len(str_tests_yes)):
            self.assertTrue(f.funcDate(str_tests_yes[i]))


    def test_findDateFalse(self):
        str_tests_no = ['с тех пор прошло 3 помидора',
                        'бюджет в 2018',
                        'в 6 ПРИВЕТ',
                        '5 щебня',
                        'почти половина мужчин не доживают до 65 стульев',
                        'в 1897 окне',
                        '1978 до эры',
                        '‘в мае 2018']

        for i in range(len(str_tests_no)):
            self.assertFalse(f.funcDate(str_tests_no[i]))

if __name__ == '__main__':
    unittest.main()

# python3 -m unittest -v main