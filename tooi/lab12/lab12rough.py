#
# textWords = collections.Counter()
# fname = sys.argv[1]
#
# for line in open(fname):
#     phrase = deletePunctuation(line)
#     wordList = phrase.split(' ')
#     for word in wordList:
#         wordResult = word.lower()
#         textWords[wordResult] += 1
# textWords.pop('', None)
# od = collections.OrderedDict(textWords.items())
# amountWords_text = sum(od.values())
#
# TF = collections.Counter()
# for word in textWords:
#     TF[word] = textWords[word] / amountWords_text
#
# count_words = collections.Counter()
# corpuses = []
#
# tree = etree.parse('opcorpora.xml')
# root = tree.getroot()
# amountWords = 0
# amountCorpus = 0
#
# IDF = collections.Counter()
# TFIDF = collections.Counter()
# for source in root.iter('source'):
#     phrase = source.text
#     # одна фраза - один корпус
#     amountCorpus += 1
#     phrase = deletePunctuation(phrase)
#     wordList = phrase.split(' ')
#     corpuses.append(wordList)
#
# for word in textWords:
#     TFIDF[word] = TF[word] * math.log10((len(corpuses) + 1) / (sum([1.0 for i in corpuses if word in i]) + 1))
#
# print(TFIDF)
