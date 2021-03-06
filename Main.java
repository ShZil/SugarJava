import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;  // Import this class to handle errors
import java.io.FileWriter;  // Import the FileWriter class
import java.util.Scanner;  // Import the Scanner class to read text files
import java.util.Arrays;  // import the Arrays class
import java.io.File;     // Import the File class

// THIS IS BROKEN. SOME REGEXS RETURN `true` ALTHOUGH THEY ARE NOT CORRECT, AND IT MESSES STUFF UP!
// Also, you gotta create int[] prespace to save the indents, and use them when writing to the file.
// BTW, you can opne the folder as an InteliJ Project for easy debugging.

public class Main {
    public static void main(String[] args) {
        String[] code = readFile("code.txt").split("\n");
        System.out.println(Arrays.deepToString(code));
        String[][] syntax = new String[][]{
            new String[]{"for (int 0 over 0)", "for (int {0} = 0; {0} < {1}.length; {0}++)"},
            new String[]{"0@0 0;", "{0}[{1}] {2};"}, // I think this one is the cause of problems since it has a parameter at the start.
            new String[]{"print 0;", "System.out.println({0});"},
            new String[]{"0 = .0(0);", "{0} = {0}.{1}({2});"}
        };
        for (int i = 0; i < code.length; i++) {
            System.out.println("line " + i);
            if (code[i].trim().equals("")) continue;
            for (String[] strings : syntax) {
                String[] names = solveRegex(code[i], strings[0]);
                if (names == null) continue;
                System.out.println(Arrays.deepToString(names));
                code[i] = substitute(strings[1], names);
            }
        }
        sugarConstructor(code);
        System.out.println(Arrays.deepToString(code));
        writeToFile("compiled.txt", code);
    }

    public static void sugarConstructor(String[] code) {
        // Special Case: Sugar Constructor.
        for (int i = 0; i < code.length; i++) {
            String[] foundMatchingStatement = solveRegex(code[i], "this.* = *;");
            if (foundMatchingStatement == null) continue;
            String[] constructor = solveRegex(code[i-1], "#(0) {");
            if (constructor == null) {
                System.out.println("WARNING: Didn't compile line " + i + " because no constructor was found above.");
                continue;
            }
            System.out.println("Found constructor at " + (i-1));
            String[] argus = constructor[0].split(",");
            code[i] = "";
            for (int j = 0; j < argus.length; j++) {
                argus[j] = argus[j].trim();
                String[] arg = solveRegex(argus[j].trim() + ',', "0 0,");
                if (arg == null) {
                    System.out.println("WARNING: Invalid parameter syntax at line " + i);
                    continue;
                }
                code[i] += substitute("this.{1} = {1};", arg);
                System.out.println(substitute("this.{1} = {1};", arg));
                if (j < argus.length - 1) {
                    code[i] += '\n';
                }
            }
        }
    }

    public static String readFile(String path) {
        String out = "";
        try {
            File file = new File(path);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                out += scan.nextLine().trim();
                out += "\n";
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred - File Not Found.");
            e.printStackTrace();
        }
        return out;
    }

    public static void writeToFile(String path, String[] data) {
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter write = new FileWriter(path);
            write.write(String.join("\n", data));
            write.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String[] solveRegex(String line, String regex) {
        final char all = '#';
        final char parameter = '0';
        final char any = '_';
        final char end = ';';

        int index = 0;
        int regexIndex = 0;
        int parameterIndex = 0;
        boolean matched = true;
        String[] parameters = new String[5];
        Arrays.fill(parameters, "");
        int length = line.length();

        if (regex == null || line == null || regex == "" || line.trim() == "") return null;
        if (regex.charAt(regex.length()-1) != end) System.out.println("WARNING: Make sure you have a closing character. \"" + regex + "\"");

        while (index < length) {
            char current = line.charAt(index);
            if (regexIndex >= regex.length()) {
                matched = false;
                break;
            }
            char match = regex.charAt(regexIndex);

            if (match == all || match == parameter) {
                boolean isParameter = match != all;
                char nextMatch = ';';
                if (regexIndex + 1 <= regex.length()) {
                    try {
                        nextMatch = regex.charAt(regexIndex + 1);
                    } catch (Exception e) {
                        System.out.println("Exception!");
                    }

                }
                while (current != nextMatch) {
                    index++;
                    if (index > length) {
                        matched = false;
                        break;
                    }
                    if (isParameter) parameters[parameterIndex] += current;
                    if (index >= length) {
                        System.out.println("Uhh... I ran out of characters to check...");
                        System.out.println("index, regexIndex: " + index + ", " + regexIndex);
                        if (regexIndex != regex.length() - 1) matched = false;
                        System.out.println("are we at last regex char? " + (regexIndex != regex.length() - 1));
                        break;
                    }
                    current = line.charAt(index);
                }
                regexIndex++;
                if (isParameter) parameterIndex++;
            } else if (match == any) {
                index++;
                regexIndex++;
                continue;
            } else if (match != current) {
                System.out.println(match + " should match " + current);
                matched = false;
                break;
            }
            index++;
            regexIndex++;
        }
        System.out.println(matched + "\n" + (matched ? Arrays.deepToString(parameters) : ""));
        if (matched) System.out.println(regex + " should fit " + line);
        if (matched) return parameters;
        return null;
    }

    public static String substitute(String format, String[] params) {
        for (int i = 0; i < params.length; i++) {
            format = format.replaceAll("\\{" + i + "\\}", params[i]);
        }
        return format;
    }
}
