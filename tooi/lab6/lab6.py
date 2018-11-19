import json
import csv
import nltk
import collections
import xml.etree.ElementTree as etree

def deletePunctuation(phrase):
    phrase_nopunc = phrase.replace(',', "")
    phrase_nopunc = phrase_nopunc.replace('\"', "")
    phrase_nopunc = phrase_nopunc.replace('\'', "")
    phrase_nopunc = phrase_nopunc.replace('—', "")
    phrase_nopunc = phrase_nopunc.replace('•', "")
    phrase_nopunc = phrase_nopunc.replace('«', "")
    phrase_nopunc = phrase_nopunc.replace('»', "")
    phrase_nopunc = phrase_nopunc.replace('.', "")
    phrase_nopunc = phrase_nopunc.replace('!', "")
    phrase_nopunc = phrase_nopunc.replace('?', "")
    phrase_nopunc = phrase_nopunc.replace(':', "")
    phrase_nopunc = phrase_nopunc.replace('--', " ")
    phrase_nopunc = phrase_nopunc.replace(';', "")
    phrase_nopunc = phrase_nopunc.replace('(', "")
    phrase_nopunc = phrase_nopunc.replace(')', "")
    return phrase_nopunc

def printResults(count_words, count_biwords):
    count_words.pop('', None)
    print("a) 20 самых частых слов:")
    print(count_words.most_common(20), '\n')

    print("b) 20 самых редких слов:")
    print(count_words.most_common()[:-20:-1], '\n')

    print("c) 20 самых частых биграмм (два слова, идущих подряд):")
    print(count_biwords.most_common(20), '\n')

    od = collections.OrderedDict(sorted(count_words.items(), key=lambda t: len(t[0]), reverse=True))
    listd = list(od.items())
    print("d) самое длинное слово:")
    print(listd[0], '\n')

    print("e) Количество уникальных слов:")
    print(len(count_words), '\n')

    print("f) Общее количество слов:")
    print(sum(od.values()), '\n')

    od.clear()

def doJob(count_words, count_biwords, new_phrase):
    for word in new_phrase:
        count_words[word] += 1
        if new_phrase.index(word) < len(new_phrase) - 1:
            biword = word + ' ' + new_phrase[new_phrase.index(word) + 1]
            count_biwords[biword] += 1

def preparePhrase(phrase):
    phrase = deletePunctuation(phrase)
    words = nltk.word_tokenize(phrase)
    functors_pos = {'CONJ', 'PREP'}
    new_phrase = [word for word, pos in nltk.pos_tag(words, lang='rus') if pos not in functors_pos]
    return new_phrase


count_words = collections.Counter()
count_biwords = collections.Counter()

#БЛОК XML
print("1) Файл opencorpora.xml")
tree = etree.parse('opcorpora.xml')
root = tree.getroot()
for source in root.iter('source'):
    phrase = source.text
    new_phrase = preparePhrase(phrase)
    doJob(count_words, count_biwords, new_phrase)

printResults(count_words, count_biwords)

#БЛОК CSV
count_words.clear()
count_biwords.clear()

print("2) Файл stage3_test.csv")

with open('stage3_test.csv', mode='r') as csv_file:
    data_csv = csv.reader(csv_file, delimiter=',')
    for row in range(1):
        next(data_csv)
    for row in data_csv:
        phrase = row[2] + ' ' + row[3]
        new_phrase = preparePhrase(phrase)
        doJob(count_words, count_biwords, new_phrase)

printResults(count_words, count_biwords)

# БЛОК JSON
count_words.clear()
count_biwords.clear()

print("3) Файл RomeoAndJuliet.json")
with open('RomeoAndJuliet.json', encoding='utf-8') as json_file:
    data = json.loads(json_file.read())

for i in range(len(data['acts'])):
    for j in range(len(data['acts'][i]['scenes'])):
        for k in range(len(data['acts'][i]['scenes'][j]['action'])):
            phrase = data['acts'][i]['scenes'][j]['action'][k]['says'][0]
            new_phrase = preparePhrase(phrase)
            doJob(count_words, count_biwords, new_phrase)

printResults(count_words, count_biwords)

with open('stage3_test.csv', mode='r') as csv_file:
    data_csv = csv.reader(csv_file, delimiter=',')
    d = collections.OrderedDict()
    for row in range(1):
        next(data_csv)
    for row in data_csv:
        header = row[2]
        cost = row[4]
        d[header] = cost
    d = collections.OrderedDict(sorted(d.items(), key=lambda t: float(t[1])))
    with open('avito_mintomax.csv', 'a') as csv_out:
        writer = csv.writer(csv_out, delimiter=",")
        writer.writerow(["Заголовок", "Цена"])
        listd = list(d.items())
        for i in range(len(listd)):
            new_row = [listd[i][0], listd[i][1]]
            writer.writerow(new_row)
    d = collections.OrderedDict(sorted(d.items(), key=lambda t: float(t[1]), reverse=True))
    with open('avito_maxtomin.csv', 'a') as csv_out:
        writer = csv.writer(csv_out, delimiter=",")
        writer.writerow(["Заголовок", "Цена"])
        listd = list(d.items())
        for i in range(len(listd)):
            new_row = [listd[i][0], listd[i][1]]
            writer.writerow(new_row)