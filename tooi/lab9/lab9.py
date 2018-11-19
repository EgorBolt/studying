class Figure:
    def __init__(self, colour, volume):
        self.colour = colour
        self.volume = volume

class Sphere(Figure):
    pass

class Cube(Figure):
    pass

class Pyramid(Figure):
    pass

class Box:
    def __init__(self, box):
        self.box = box
    def addToBox(self, f):
        if isinstance(f, Figure):
            self.box.append(f)
            print("Figure was added into the box.")
        else:
            print("Can't add this object, it's not a known figure.\n")


c1 = Cube("red", 5)
c2 = Cube("purple", 7)
s1 = Sphere("red", 10)
s2 = Sphere("green", 15)
p1 = Pyramid("yellow", 1)
p2 = Pyramid("black", 100)
b = Box([])

b.addToBox(p1)
b.addToBox(p2)
b.addToBox(s1)
b.addToBox(s2)
b.addToBox(c1)
b.addToBox(c2)

b1 = Box([])
b.addToBox(b1)

for i in b.box:
    print(i.__class__.__name__)