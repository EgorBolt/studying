import time

print("Введите дату рождения в формате дд.мм.гггг:")
date = input()
date = date.replace('.', ' ')
birthday_date = time.strptime(date, "%d %m %Y")
current_date = time.localtime(time.time())
print(birthday_date)
print(current_date)

days_passed = 0
days_passed = days_passed - birthday_date.tm_yday + current_date.tm_yday
for y in range(birthday_date.tm_year, current_date.tm_year):
    if y % 4 != 0 or (y % 100 == 0 and y % 400 != 0):
        days_passed += 365
    else:
        days_passed += 366

print("Прошло дней между датами:", days_passed)
