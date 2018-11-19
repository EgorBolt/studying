import re

def funcPlus(a, b):
    return a + b

def funcAdd(list, a):
    list.append(a)
    return list

def funcMult(a, b):
    return a * b

def funcDate(str):
    str_list = str.split(' ')
    indexes = []
    numbers_list = re.findall(r'[\d]+', str)
    time_list = ["год", "году", "года", "годах", "век", "г", "веке", "веку", "лет",
                 "январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август",
                 "сентябрь", "октябрь", "ноябрь", "декабрь", "января", "февраля", "марта",
                 "апреля", "мая", "июня", "июля", "августа",
                 "сентября", "октября", "ноября", "декабря"]

    for i in range(len(numbers_list)):
        indexes += [j for j, e in enumerate(str_list) if e == numbers_list[i]]

    for i in range(len(indexes)):
        if indexes[i] + 1 < len(str_list):
            if str_list[indexes[i] + 1] in time_list:
                return True
            elif (str_list[indexes[i] + 1] == "до") and (str_list[indexes[i] + 2] == "нашей") and (str_list[indexes[i] + 3] == "эры"):
                return True

    return False