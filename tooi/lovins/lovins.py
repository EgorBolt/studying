import lovins
words = 'hello this is a test of the emergency broadcast system'.split()
for w in words:
    print(w, lovins.stem(w))