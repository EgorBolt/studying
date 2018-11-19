class A:
    def __init__(self, number):
        self.number = number

class B:
    def __init__(self, number):
        self.number = number
    def changeNumber(self, c, newValue):
        c.number = newValue

a = A(5)
a2 = A(100)
b = B(10)

b.changeNumber(a, 11)
print(a.number)
print(a2.number)



