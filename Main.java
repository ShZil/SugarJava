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
  }

  public static String[] solveForOver(String[] code) {
    System.out.println("solveForOver:");
    // for (type variable over array)
    // for (E e over a) -> min length = 16
    // for (type index = 0; index < array.length; index++)
    for (int i = 0; i < code.length; i++) {
      if (code[i].length() < 16) {
        continue;
      }
      if (code[i].startsWith("for")) {
        String[] words = separateWords(code[i]);
        if (words[3].startsWith("over")) {
          String type = words[1];
          String index = words[2];
          String array = words[4];
          code[i] = "for (" + type + " " + index + " = 0; " + index + " < " + array + ".length; " + index + "++) {";
        }
      }
    }
    return code;
  }

  public static String[] solveArrayAccesses(String[] code) {
    System.out.println("solveArrayAccesses:");
    // _____array@index______
    // _____array[index]_____
    for (int i = 0; i < code.length; i++) {
      String[] words = separateWords(code[i]);
      for (int j = 0; j < words.length; j++) {
        if (contains(words[j].toCharArray(), '@')) {
          System.out.println("Word found = " + words[j]);
          String[] expression = words[j].split("@");
          String array = expression[0];
          String index = expression[1];
          code[i] = code[i].replace(words[j], array + "[" + index + "]");
        }
      }
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

  public static String[] separateWords(String line) {
    char[] nonWordCharacters = " {}();".toCharArray();
    String[] out = new String[10];
    for (int o = 0; o < out.length; o++) {
      out[o] = "";
    }
    int index = 0;
    for (int i = 0; i < line.length(); i++) {
      if (contains(nonWordCharacters, line.charAt(i))) {
        if (out[index].length() > 0) {
          index++;
        }
      } else {
        out[index] += line.charAt(i);
      }
    }
    return out;
  }

  public static boolean contains(char[] chars, char c) {
    for (char ch : chars) {
      if (ch == c) {
        return true;
      }
    }
    return false;
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
}


// public static String[] solveRegex(String line, String regex)
// if line doesn't match regex, return null.
// String regex = "for ({0} over {1}) *"
// Check if line matches regex:
// int regexIndex = 0, index = 0;
// while (index < length) {
//    char current = line.charAt(index)
//    char match = regex.charAt(regexIndex)
//    if (match == '*') {
//       while (current != nextMatch) {
//          index++
//          reEval current
//          if (index > length) {
//            matched = false;
//            break;
//          }
//       }
//    }
//    else if (match != regex) {
//
//    }
// }
