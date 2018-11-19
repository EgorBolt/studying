class Ticket:
    def __init__(self, FIO, carriage, seat, cost, date):
        self.FIO = FIO
        self.carriage = carriage
        self.seat = seat
        self.cost = cost
        self.date = date
    def printInfo(self):
        print("Билет приобретен! Проверьте следующие данные: ")
        print("ФИО: ", self.FIO)
        print("Вагон: ", self.carriage)
        print("Кресло: ", self.seat)
        print("Цена: ", self.cost, "рублей")
        print("Дата: ", self.date)

class Buying:
    def buyTicket(self, payment, change):
        if payment < 1000:
            print("Стоимость билета - 1000 рублей, недостаточно средств.")
            exit(-1)
        else:
            print("Введите ФИО: ")
            FIO = input()
            print("Введите номер вагона: ")
            carriage = int(input())
            print("Введите номер кресла: ")
            seat = int(input())
            print("Введите дату: ")
            date = input()
            cost = 1000
            change = payment - cost
            print("Стоимость билета 1000 рублей, Вы дали", payment, "рублей, Ваша сдача:", change, "рублей.")
            t = Ticket(FIO, carriage, seat, cost, date)
            return t

print("Вы дали денег: ")
money = int(input())
change = 0
b = Buying()
t = b.buyTicket(money, change)

print(t.printInfo())