class Things:
    def __init__(self, n, t):
        self.namething = n
        self.total = t


th1 = Things("table", 5)
th2 = Things("computer", 7)

print(th1.namething, th1.total)  # Вывод: table 5
print(th2.namething, th2.total)  # Вывод: computer 7

th1.color = "green"  # новое свойство объекта th1
th2.color = "blue" # данной строчкой я исправляю ошибку

print(th1.color)  # Вывод: green
print(th2.color)  # ОШИБКА: у объекта th2 нет свойства color!