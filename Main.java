import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;  // Import this class to handle errors
import java.io.FileWriter;   // Import the FileWriter class
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Arrays; // import the Arrays class

public class Main {
    public static void main(String[] args) {
        String[] code = readFile("code.txt").split("\n");
        code = solveForOver(code);
        code = solveArrayAccesses(code);
        System.out.println(Arrays.deepToString(code));
        writeToFile("compiled.txt", code);
        // solveRegex("for (int index over arr)", "*for*(int 0 over 0 )*");
    }

    public static String[] solveForOver(String[] code) {
        System.out.println("solveForOver:");
        // for (int index over array)
        for (int i = 0; i < code.length; i++) {
            String[] names = solveRegex(code[i], "for (int 0 over 0)");
            if (names == null) continue;
            code[i] = substitute("for (int {0} = 0; {0} < {1}.length; {0}++)", names);
        }
        return code;
    }

    public static String[] solveArrayAccesses(String[] code) {
        System.out.println("solveArrayAccesses:");
        // array@index
        for (int i = 0; i < code.length; i++) {
            String[] names = solveRegex(code[i], "0@0 0;");
            if (names == null) continue;
            code[i] = substitute("{0}[{1}] {2};", names);
        }
        return code;
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
            System.out.println("An error occurred.");
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
        final char all = '*';
        final char parameter = '0';
        final char any = '_';

        int index = 0;
        int regexIndex = 0;
        int parameterIndex = 0;
        boolean matched = true;
        String[] parameters = new String[5];
        Arrays.fill(parameters, "");
        int length = line.length();

        if (regex.charAt(regex.length()-1) != ';') {
            System.out.println("WARNING: Make sure you have a closing character. \"" + regex + "\"");
        }

        while (index < length) {
            char current = line.charAt(index);
            if (regexIndex >= regex.length()) break;
            char match = regex.charAt(regexIndex);

            if (match == all || match == parameter) {
                boolean isParameter = match != all;
                char nextMatch = regex.charAt(regexIndex + 1);
                while (current != nextMatch) {
                    index++;
                    if (index > length) {
                        matched = false;
                        break;
                    }
                    if (isParameter) parameters[parameterIndex] += current;
                    if (index >= length) {
                        System.out.println("Uhh... I ran out of characters to check...");
                        matched = false;
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
        System.out.println(matched);
        if (matched) System.out.println(Arrays.deepToString(parameters));
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
