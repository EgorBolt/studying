import time

start = time.clock()
i = 0

for i in range(25000000):
    i += 1

end = time.clock()

print(end - start, "seconds")