# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.


def main():
    regex = "for*(0 over 1)*"
    line = "for (i over arr)"
    index, regexindex, parameterindex = 0, 0, 0
    matched = True
    parameters = [""] * 10
    while index < len(line):
        current = line[index]
        match = regex[regexindex]
        if match == '*' or match == '0' or match == '1':
            nextmatch = regex[regexindex + 1]
            while current != nextmatch:
                index += 1
                if index > len(line):
                    matched = False
                    break
                if match != '*':
                    parameters[parameterindex] += current
                current = line[index]
            regexindex += 1
            if match != '*':
                parameterindex += 1
        elif match != current:
            print(match, 'should match', current)
            matched = False
        index += 1
        regexindex += 1
    print(matched)
    print(parameters)
    pass


if __name__ == '__main__':
    main()

