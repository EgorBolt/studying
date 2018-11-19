import xml.etree.ElementTree as etree
import sys
import collections
import math
import pickle

class Work:
    __textWords = collections.Counter()
    __TF = collections.Counter()
    __amountWords_text = 0
    __corpuses = []
    TFIDF = collections.Counter()
    def __init__(self):
        pass

    def __deletePunctuation(self, phrase):
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
        phrase_nopunc = phrase_nopunc.replace('\n', "")
        return phrase_nopunc

    def __getWords(self, fname):
        for line in open(fname):
            phrase = self.__deletePunctuation(line)
            wordList = phrase.split(' ')
            for word in wordList:
                wordResult = word.lower()
                self.__textWords[wordResult] += 1
        self.__textWords.pop('', None)

    def __prepareCorpuses(self):
        tree = etree.parse('opcorpora.xml')
        root = tree.getroot()
        for source in root.iter('source'):
            phrase = source.text
            phrase = self.__deletePunctuation(phrase)
            wordList = phrase.split(' ')
            self.__corpuses.append(wordList)

    def __returnTFIDF(self):
        od = collections.OrderedDict(self.__textWords.items())
        self.__amountWords_text = sum(od.values())
        for word in self.__textWords:
            self.TFIDF[word] = (self.__textWords[word] / self.__amountWords_text) * (math.log10((len(self.__corpuses) + 1) / (sum([1.0 for i in self.__corpuses if word in i]) + 1)))

    def doJob(self, fname):
        try:
            with open('result.txt', 'rb') as filehandle:
                self.TFIDF = pickle.load(filehandle)
        except:
            self.__getWords(fname)
            self.__prepareCorpuses()
            self.__returnTFIDF()
            with open('result.txt', 'wb') as filehandle:
                pickle.dump(self.TFIDF, filehandle)

        return self.TFIDF

worker = Work()
TFIDF = worker.doJob(sys.argv[1])
print(TFIDF)

# TF термина а = (Количество раз, когда термин а встретился в тексте / количество всех слов в тексте)
# IDF термина а = логарифм(Общее количество документов / Количество документов, в которых встречается термин а)
# TFIDF = TF(a) * IDF(a)
