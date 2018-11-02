import csv

FILENAME = "users.csv"

users = [
    ["Ivan", "Ivanov", 28, 190],
    ["Petr", "Petrov", 28, 190],
    ["Egor", "Boldyrev", 21, 193],
    ["Dmitri", "Boldyrev", 49, 191],
    ["Oleg", "Olegov", 20, 187],
    ["Oleg", "Borisov", 40, 170]
]

with open(FILENAME, "w", newline="") as file:
    writer = csv.writer(file)
    writer.writerows(users)

with open('users.csv') as fd:
    reader = csv.reader(fd)
    p = []
    for i in range(len(users)):
        a = users[i]
        for j in range(i + 1, len(users)):
            b = users[j]
            for k in range(4):
                if (a[k] == b[k]):
                    p.append(k)
            if p:
                print("Строка ", i, " совпадает со строкой ", j, "по полям: ", p)
                p = []



