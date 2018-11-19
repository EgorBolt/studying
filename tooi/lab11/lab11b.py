class BankAccount:
    __password = "12345"
    _balance = 5000
    name = "Egor"
    number = 123

    def printBalance(self, password, balance):
        if (password == self.__password):
            self._balance = balance
            print("Новый баланс счёта:", self._balance)
        else:
            print("Неверный пароль")


    def __privateFunc(self):
            print("Это приватный метод")

    def publicFunc(self):
        self.__privateFunc()

    def setPassword(self, newPassword):
        if (newPassword != "aaa"):
            self.__password = newPassword
            print("Новый пароль:", self.__password)
        else:
            print("Пароль не может быть aaa")

a = BankAccount()
a.printBalance("12345", 3000)
# a.printBalance("456", 10000)

a.publicFunc()
a._BankAccount__privateFunc() #фича

a.setPassword("10000000")
a.setPassword("aaa")