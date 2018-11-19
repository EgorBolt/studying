import time

print('Введите число: ')
i = int(input())
while i > 0:
    time.sleep(i)
    print('Введите число: ')
    i = int(input())

