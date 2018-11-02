import nltk

from nltk.grammar import PCFG, induce_pcfg, toy_pcfg1, toy_pcfg2
from nltk.parse import ViterbiParser

tokens = "Jack saw Bob with my cookie".split()
grammar = toy_pcfg2
print grammar

parser = ViterbiParser(grammar)
for t in parser.parse(tokens):
	print(t)