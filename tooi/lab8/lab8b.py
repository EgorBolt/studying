import xml.etree.ElementTree as etree

class Corpus():
    sentence_list = []
    def importXML(self, root):
        for source in root.iter('source'):
            s = Sentence()
            s.setSentence(source.text)
            self.sentence_list.append(s)

class Sentence():
    def setSentence(self, sentence):
        self.sentence = sentence
    def printSentence(self):
        print(self.sentence + '\n')

tree = etree.parse('opcorpora.xml')
root = tree.getroot()

corpus = Corpus()
corpus.importXML(root)

for i in corpus.sentence_list:
    i.printSentence()