import numpy as np
import pandas as pd
from numpy import log2 as log
import pprint

eps = np.finfo(float).eps # для дебага

def find_entropy(df):
    Class = df.keys()[-1]
    entropy = 0
    values = df[Class].unique() #достать все значения атрибута
    for value in values:
        fraction = df[Class].value_counts()[value] / len(df[Class])
        entropy += -fraction * np.log2(fraction)
    return entropy


def find_entropy_attribute(df, attribute):
    Class = df.keys()[-1] # Даёт все "Да" или "Нет"
    target_variables = df[Class].unique()  # This gives all 'Да' and 'Нет'
    variables = df[
        attribute].unique()  # Неконечные значения атрибутов (не "Да" или "Нет" - они конечные,
    entropy2 = 0 # а, например, 'Горячий' и 'Холодный' в атрибуте "Температура"
    for variable in variables:
        entropy = 0
        for target_variable in target_variables:
            num = len(df[attribute][df[attribute] == variable][df[Class] == target_variable])
            den = len(df[attribute][df[attribute] == variable])
            fraction = num / (den + eps)
            entropy += -fraction * log(fraction + eps)
        fraction2 = den / len(df)
        entropy2 += -fraction2 * entropy
    return abs(entropy2)

#Здесь мы считаем максимальный информационный вес для всех атрибутов
def find_winner(df):
    IG = []
    for key in df.keys()[:-1]:
        IG.append(find_entropy(df) - find_entropy_attribute(df, key))
    return df.keys()[:-1][np.argmax(IG)]


def get_subtable(df, node, value):
    return df[df[node] == value].reset_index(drop=True)

def buildTree(df, tree=None):

    # Получаем атрибут с максимальным информационным весом
    node = find_winner(df)

    # Получаем все значения атрибута (например для Вкуса значения Солёный, Острый, Сладкий)
    attValue = np.unique(df[node])

    # Создаём дерево и словарь для начального узла (потом рекурсивно будем создавать узлы этой же функцией)
    if tree is None:
        tree = {}
        tree[node] = {}

    # Будем строить дерево, рекурсивно вызывая эту функцию,
    # пока не убедимся, не дошли до конца ветки, где по последнему атрибуту можно точно определить ответ "Да" или "Нет"
    for value in attValue:

        subtable = get_subtable(df, node, value)
        clValue, counts = np.unique(subtable['Будет ли есть'], return_counts=True)

        if len(counts) == 1:  # Дошли до конца
            tree[node][value] = clValue[0]
        else:
            tree[node][value] = buildTree(subtable)

    return tree

def predict(inst, tree):
    # Предсказываем результат запрос
    # Рекурсивно проходим по дереву
    for nodes in tree.keys():

        value = inst[nodes]
        tree = tree[nodes][value]
        prediction = 0

        if type(tree) is dict: #Если в узле - словарь, то он не терминальный, значит идём дальше
            prediction = predict(inst, tree)
        else:
            prediction = tree # В узле находится "Да" или "Нет"
            break;

    return prediction

dataset = {
    'Вкус':['Солёный','Острый','Острый','Острый','Острый','Сладкий','Солёный','Сладкий','Острый','Солёный'],
    'Температура':['Горячий','Горячий','Горячий','Холодный','Горячий','Холодный','Холодный','Горячий','Холодный','Горячий'],
    'Текстура':['Мягкий','Мягкий','Твёрдый','Твёрдый','Твёрдый','Мягкий','Мягкий','Мягкий','Мягкий','Твёрдый'],
    'Будет ли есть':['Нет','Нет','Да','Нет','Да','Да','Нет','Да','Да','Да']
}

df = pd.DataFrame(dataset, columns=['Вкус','Температура','Текстура','Будет ли есть'])

print(df)

tree = buildTree(df)
pprint.pprint(tree)

data = {'Вкус':'Сладкий', 'Temperatue':'Холодный', 'Текстура':'Твёрдый'}
inst = pd.Series(data)
prediction = predict(inst, tree)
print(prediction)