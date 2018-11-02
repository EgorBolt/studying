import numpy as np
print("#1")
zero_array = np.zeros(10)
print(zero_array)
print()

print("#2")
zero_array_with_1 = np.zeros(10)
zero_array_with_1[3] = 1
print(zero_array_with_1, '\n')


print("#3")
array_148 = np.full(10, 14.8)
print(array_148, '\n')

print("#4")
arange = np.arange(23,39)
print(arange, '\n')

print("#5")
arange_backwards = arange[::-1]
print(arange_backwards, '\n')

print("#6")
array_for_matrix = np.arange(10,19)
print(array_for_matrix)
matrix = array_for_matrix.reshape(3,3)
print(matrix, '\n')

print("#7")
matrix308 = np.arange(9).reshape(3,3)
print(matrix308, '\n')

print("#8")
find_zero = np.nonzero([1,2,0,0,4,0])
print(find_zero, '\n')

print("#9")
matrix_minmax = np.random.randint(101, size=(10,10))
print(matrix_minmax)
min = matrix_minmax.min()
max = matrix_minmax.max()
print("min =", min, "max =", max, '\n')

print("#10")
vector15 = np.random.randint(101, size=15)
print(vector15)
min = vector15.argmin()
max = vector15.argmax()
print("min =", min + 1, "max =", max + 1, '\n')

print("#11")
vector30 = np.random.randint(10, size=30)
print(vector30)
mean = vector30.mean()
print(mean, '\n')

print("#12")
matrix310 = np.random.randint(5, size=(3,10))
print(matrix310)
column_array = np.mean(matrix310, axis=0)
row_array = np.mean(matrix310, axis=1)
print(column_array)
print(row_array, '\n')

print("#13")
matrix1 = np.ones(4).reshape(2,2)
print(matrix1)
matrix1 = np.pad(matrix1, ((1,1),(1,1)), 'constant')
print(matrix1, '\n')

print("#14")
mult_matrix = np.dot(np.ones((5,3)), np.ones((3,2)))
print(mult_matrix, '\n')

print("#15")
matrix_c1 = np.random.randint(11, size=(4,4))
matrix_c2 = np.random.randint(11, size=(4,4))
print(matrix_c1)
print(matrix_c2)
print(np.intersect1d(matrix_c1,matrix_c2), '\n')

print("#16")
sort_vector = np.random.randint(31, size=10)
print(sort_vector)
sort_vector.sort()
print(sort_vector, '\n')

print('#17')
vector_chg_max = np.random.randint(101, size=10)
print(vector_chg_max)
vector_chg_max[np.argmax(vector_chg_max)] = 0
print(vector_chg_max, '\n')

print('#18')
matrix_flat = np.random.randint(11, size=(3,3))
print(matrix_flat)
print(np.ndarray.flatten(matrix_flat))
print(matrix_flat.reshape(1,9), '\n')

print('#19')
matrix_44 = np.random.randint(16, size=(4,4))
print(matrix_44)
matr_max = matrix_44.max()
print("max =", matr_max)
matrix_44 -= matr_max
print(matrix_44)

print('#20')
data = np.random.rand(5)
data.tofile("matrix.txt")
f = open("matrix.txt", "rb")
data_file = np.fromfile(f, dtype=np.float)
print(data_file)
data_file.sort()
print(data_file)