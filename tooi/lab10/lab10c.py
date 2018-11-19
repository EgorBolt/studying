class Figure:
    colour = "white"
    def changeColour(self, newColour):
        self.colour = newColour
    def printParameters(self):
        pass

class Oval(Figure):
    def __init__(self, major, minor):
        self.major = major
        self.minor = minor
    def printParameters(self):
        print("Object's name: ", self.__class__.__name__)
        print("Semi-major axis: ", self.major)
        print("Semi-minor axis: ", self.minor)
        print("Colour: ", self.colour)
        print()

class Square(Figure):
    def __init__(self, side):
        self.side = side
    def printParameters(self):
        print("Object's name: ", self.__class__.__name__)
        print("Semi-major axis: ", self.side)
        print("Colour: ", self.colour)
        print()

o = Oval(5, 2)
s = Square(3)

o.printParameters()
s.printParameters()

o.changeColour("blue")
s.changeColour("green")

o.printParameters()
s.printParameters()
