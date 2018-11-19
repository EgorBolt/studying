class Employee:
    salary = 10000
    def printSalary(self):
        print(self.salary)
    def __init__(self, FIO, city, department):
        self.FIO = FIO
        self.city = city
        self.department = department
    def printInfo(self):
        print("FIO: ", self.FIO)
        print("City: ", self.city)
        print("Department: ", self.department)
        try:
            print("Stocks: ", self.stocks)
        except:
            print("This employee doesn't have stocks")
        try:
            print("Car: ", self.car)
        except:
            print("This employee doesn't have a car")
        print()

class Manager(Employee):
    salary = 20000
    stocks = 10
    def printStocks(self):
        print(self.stocks)
    def changeStocks(self, newStocks):
        self.stocks = newStocks


class Director(Manager):
    salary = 30000
    stocks = 100
    car = "Ferrari"
    def printCar(self):
        print(self.car)
    def changeCar(self, newCar):
        self.car = newCar

e = Employee("Иванов Иван Иванович", "Москва", "IT")
m = Manager("Петров Петр Петрович", "Москва", "Транспорт")
d = Director("Олегов Олег Олегович", "Новосибирск", "IT")

e.printInfo()
m.printInfo()
d.printInfo()