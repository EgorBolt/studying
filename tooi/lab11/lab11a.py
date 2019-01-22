class A:
    a1 = 0
    _a2 = 1
    __a3 = 2

    def func1(self):
        print("Это функция без подчёркиваний")
    def _func2(self):
        print("Это функция с одним подчёркиванием")
    def __func3(self):
        print("Это функция с двумя подчёркиваниями")

    def printA1(self):
        print(self.a1)
    def printA2(self):
        print(self._a2)
    def printA3(self):
        print(self.__a3)

    def setA1(self, v):
        self.a1 = v
    def setA2(self, v):
        self._a2 = v

    def setA3(self, v):
        self.__a3 = v

a = A()
a.func1()
a._func2()
a.__func3()

a.printA1()
a.printA2()
a.printA3()

a.setA1(3)
a.setA2(4)
a.setA3(5)

a.printA1()
a.printA2()
a.printA3()

a.__a3 = 7 # НЕ ИЗМЕНИТ ЗНАЧЕНИЕ

a.printA3()
