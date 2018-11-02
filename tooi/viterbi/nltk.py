
# coding: utf-8

# http://www.nltk.org/book/
# 
# Install nltk by typing this in your command line:
# 
#     conda install -c anaconda nltk
# 
# Then let's import it it:

# In[ ]:

import nltk


# Then let's try to download some text corpora:

# In[ ]:

nltk.download()


# Select "book" and press "download".

# We can now take a look at what we have downloaded.

# In[ ]:

from nltk.book import *


# In[ ]:

text1


# We can find contexts of a specific word:

# In[ ]:

text1.concordance('monstrous')


# Or words that occur with it most frequently:

# In[ ]:

text1.similar('monstrous')


# NLTK treats texts as lists of tokens. If variable "text" is an nltk text, what do the following expressions return?
# 
#     len(text)
#     set(text)
#     len(set(text))

# In[ ]:

text = text1[:100]
print(len(text))
print(set(text))
print(len(set(text)))


# Now we consider different stemming algorithms, see http://www.nltk.org/api/nltk.stem.html

# In[ ]:

text4


# In[ ]:

text = text4[500:600]


# In[ ]:

print(' '.join(text))
desired_output = []

port = nltk.stem.porter.PorterStemmer()
text_port = [port.stem(word) for word in text]
print("Porter stemmer:", ' '.join(text_port))

snowball = nltk.stem.snowball.SnowballStemmer(language = 'english')
text_snowball = [snowball.stem(word) for word in text]
print("Snowball stemmer: ", ' '.join(text_snowball))

lancaster = nltk.stem.lancaster.LancasterStemmer()
text_lanc = [lancaster.stem(word) for word in text]
print('Paice/Husk stemmer:', ' '.join(text_lanc))

from nltk.stem import WordNetLemmatizer
wnl = WordNetLemmatizer()
text_wnl = [wnl.lemmatize(word) for word in text]
print('WordNet lemmatizer:', ' '.join(text_wnl))


# Now let's try to implement a function that counts the overstemming index and the understemming index.

# In[ ]:

def overstemming(input_text, real_output, desired_output):
    pass
    #your turn!

def understemming(input_text, real_output, desired_output):
    pass
    #your turn!

for stemmed_text in [text_port, text_snowball, text_lanc, text_wnl]:
    overstemming_index = overstemming(text, stemmed_text, desired_output)
    print('overstemming index:', overstemming_index)
    understemming_index = understemming(text, stemmed_text, desired_output)
    print('understemming index: ', understemming_index)


# We can compute MVC and Index Compression Factor.
# 
# MVC = N / S, 
# 
# ICF = (N - S) / N, 
# 
# where 
# 
# N is the number of unique tokens in the original text,
# 
# S is the number of unique tokens in the stemmed text.

# In[ ]:

text = text4[:]

port = nltk.stem.porter.PorterStemmer()
text_port = [port.stem(word) for word in text]

snowball = nltk.stem.snowball.SnowballStemmer(language = 'english')
text_snowball = [snowball.stem(word) for word in text]

lancaster = nltk.stem.lancaster.LancasterStemmer()
text_lanc = [lancaster.stem(word) for word in text]

from nltk.stem import WordNetLemmatizer
wnl = WordNetLemmatizer()
text_wnl = [wnl.lemmatize(word) for word in text]

#your turn! Compute the ICF and MVC


# NLTK also has tools for word tokenization and part-of-speech tagging: 

# In[ ]:

from nltk import word_tokenize
text = word_tokenize('The quick brown fox jumps over the lazy dog!')
print(nltk.pos_tag(text))


# What is the most frequent part of speech in the book of Genesis?

# In[ ]:

from collections import Counter

tagged_Genesis = nltk.pos_tag(text3)
tags = [tag for word, tag in tagged_Genesis]
counter = Counter(tags)
print(counter)


# We can count frequencies of bigrams like this:

# In[ ]:

counter = Counter(nltk.bigrams(text3))
print(counter.most_common(20))


# Can you count which parts of speech are likely to occur together?

# In[ ]:

#your turn!


# How do we count the percentage of proper nouns? Of singular nouns? Of verbs in present tense?
# First you need to get a hint about POS-tags like this:

# In[ ]:

nltk.help.upenn_tagset('N') #noun tags
nltk.help.upenn_tagset('V') #verb tags


# Then you can count the percentage:

# In[ ]:

#your turn!

