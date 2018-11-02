import json
import operator

with open('RomeoAndJuliet.json', encoding='utf-8') as file:
    data = json.loads(file.read())

characters = {}
phrase_length = 0

for i in range(len(data['acts'])):
    for j in range(len(data['acts'][i]['scenes'])):
        for k in range(len(data['acts'][i]['scenes'][j]['action'])):
            new_phrase_length = 0
            character = data['acts'][i]['scenes'][j]['action'][k]['character']
            for l in range(len(data['acts'][i]['scenes'][j]['action'][k]['says'])):
                new_phrase_length += len(data['acts'][i]['scenes'][j]['action'][k]['says'][l])
            if phrase_length < new_phrase_length:
                phrase_length = new_phrase_length
                name_phrase_length = character
            if character not in characters:
                characters[character] = 1
            else:
                characters[character] += 1

character_max = max(characters, key=characters.get)
phrases_max = characters.get(character_max)

print(character_max, "сказал больше всего реплик, их количество", phrases_max)
print(name_phrase_length, "сказал самую длинную фразу длиной", phrase_length, "символов.")

new_json = {}
new_json['datatypes'] = []
new_json['datatypes'].append('int')
new_json['datatypes'].append('float')
new_json['datatypes'].append('char')
new_json['datatypes'].append('long')

with open('new_json.json', 'w') as out:
    json.dump(new_json, out)