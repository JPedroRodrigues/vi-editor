/****************************************************************************************************
- Authors:
    João Pedro Rodrigues Vieira         32281730
    Sabrina Midori F. T. de Carvalho    42249511
- Creation Date:
    03.11.2023
- Project Description:
    A simple text editor similar to Linux VI.
 ****************************************************************************************************/

package viEditor;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Variables that must be initialized outside of do-while loop
        List list = null, copy = null;
        String[] op;
        boolean exit = false, selectedText = false;
        int firstLine = 0, lastLine = 0;

        showHeader();

        // Do-while option isn't to end program
        do {
            Scanner scanner = new Scanner(System.in);

            // Read operation to be performed
            System.out.print("\nCommand: ");
            op = scanner.nextLine().trim().split(" ");
            System.out.println();

            // Switch statement
            switch (op[0]) {

            /*==========================================================================================================
            Open file, read its content and store each line in a list
            ==========================================================================================================*/
                case ":e" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Create list to store file content
                    list = new List();

                    // Filepath
                    String filePath = op[1];

                    // Check if filepath exists and is a file
                    File file = new File(filePath);
                    boolean exists = file.exists() && file.isFile();

                    // If filepath exists
                    if (exists) {
                        // Check if file could be open and read and add its content to the list
                        if (readFile(list, filePath)) {
                            System.out.print("File's content successfully added to the list.\nType 'Y' to see the stored content or 'N' to go back to menu: ");
                            String ans;

                            // Do-while answer is invalid
                            do {
                                ans = scanner.nextLine().trim().toUpperCase();

                                // If answer is "Y", call function to show list's content
                                if (ans.equals("Y")) list.print(1, list.getCount());
                                else if (!ans.equals("N")) System.out.print("Invalid option. Type 'Y' to see the stored content or 'N' to go back to menu: ");

                            } while (!ans.equals("Y") && !ans.equals("N"));
                        }
                    // If filepath does not exist
                    } else System.out.println("File does not exist.");
                }

            /*==========================================================================================================
            Write list's content to a file
            ==========================================================================================================*/
                case ":w" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    // Filepath
                    String filePath = op[1];

                    // Write in file
                    if (writeFile(list, filePath)) System.out.println("List's content successfully added to the file " + filePath);
                    else System.out.println("Error while storing list's content in the file " + filePath);
                }

            /*==========================================================================================================
            Select and print a piece of the list, from first to last line
            ==========================================================================================================*/
                case ":v" -> {

                    // Check if input is valid
                    if (op.length != 3) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    // Try-catch block to check if input is valid
                    try {
                        // Values of first and last line
                        int x = Integer.parseInt(op[1]);    // first line
                        int y = Integer.parseInt(op[2]);    // last line

                        // If first line and last line are within the bounds of the list
                        if (verifyBounds(list, x, y)) {
                            System.out.println("The provided limit is valid.");
                            selectedText = true;

                            firstLine = x;
                            lastLine = y;

                            // If they are, call method to select and print given interval of the list
                            list.print(firstLine, lastLine);

                        // If first line and last line are not within the bounds of the list
                        } else System.out.printf("The provided limit is not valid. Values must be between 1-%d.\n", list.getCount());

                    } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }
                }

            /*==========================================================================================================
            Copy selected text to a copy list
            ==========================================================================================================*/
                case ":y" -> {

                    // Check if input is valid
                    if (op.length != 1) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // If the interval was previously selected
                    if (selectedText) {
                        copy = new List();

                        // Call function to copy selected content to new list
                        if (copySelectedText(list, copy, firstLine, lastLine)) {
                            // If content is successfully copied, print a message
                            System.out.println("Text successfully copied to new list.");

                            // test - comment out after
                            // copy.print(1, copy.getCount());

                        // If content could not be copied, print a message
                        } else System.out.println("Error while copying selected text to new list.");

                    // If the interval was not previously selected
                    } else System.out.println("No text selected. To select an interval, type :v <startLine> <endLine>.");
                }

            /*==========================================================================================================
            Cut (remove) selected text
            ==========================================================================================================*/
                case ":c" -> {

                    // Check if input is valid
                    if (op.length != 1) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // If the interval was previously selected
                    if (selectedText) {
                        // Call function to remove selected content of the list
                        if (list.removeLines(firstLine, lastLine, "standard")) {
                            // If content is successfully removed from the original list, print a message
                            System.out.println("Text successfully cut.");

                        // If content could not be removed, print a message
                        } else System.out.println("Error while cutting selected text from list.");

                    // If the interval was not previously selected
                    } else System.out.println("No text selected. To select an interval, type :v <startLine> <endLine>.");
                }

            /*==========================================================================================================
            Paste text starting at given line
            ==========================================================================================================*/
                case ":p" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // If copy list is not empty
                    if (copy != null) {
                        // Try-catch block to check if input is valid
                        try {
                            // Value of start line
                            int start = Integer.parseInt(op[1]);

                            if (list.paste(copy, start)) System.out.println("The selected text was successfully pasted to the list.");
                            else System.out.println("Could not paste the selected text to the list.");

                        } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }

                    // If copy list is empty
                    } else System.out.println("No text was copied. To copy previously selected text, type :y.");
                }

            /*==========================================================================================================
            Display source code (entire or from first line to last line), every 10 lines
            ==========================================================================================================*/
                case ":s" -> {

                    // Check if input is valid
                    if (op.length != 1 && op.length != 3) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    // Command :s
                    int startLine = 1, endLine = list.getCount();

                    // Command :s <startLine> <endLine>
                    if (op.length == 3)
                        // Try-catch block to check if input is valid
                        try {
                            startLine = Integer.parseInt(op[1]);        // first line
                            endLine = Integer.parseInt(op[2]);          // last line

                            // If first line and last line are within the bounds of the list
                            if (verifyBounds(list, startLine, endLine)) System.out.println("The provided limit is valid.");
                            // If first line and last line are not within the bounds of the list
                            else {
                                System.out.printf("The provided limit is not valid. Values must be between 1-%d.\n", list.getCount());
                                continue;
                            }
                        } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }

                    System.out.println("Printing code...\n");

                    // While loop
                    while (true) {
                        // Print list content from first line to last line
                        list.printEvery10Lines(startLine, endLine);

                        // Increment first line in 10 and check if its greater than last line
                        startLine += 10;
                        if (startLine > endLine) break;

                        String ans;

                        // Do-while answer is invalid
                        do {
                            System.out.printf("\nThere are %d lines left. Do you want to continue?\n", endLine - startLine + 1);
                            System.out.print("Your choice [Y/N]: ");
                            ans = scanner.nextLine().trim().toUpperCase();
                            System.out.println();

                            if (!ans.equals("Y") && !ans.equals("N")) System.out.print("Invalid option. Type 'Y' to continue or 'N' to go back to menu.\n");
                        } while (!ans.equals("Y") && !ans.equals("N"));

                        // Break out of while loop
                        if (ans.equals("N")) break;
                    }
                }

            /*==========================================================================================================
            Remove given line
            ==========================================================================================================*/
                case ":x" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    int line = 0;

                    // Try-catch block to check if input is valid
                    try {
                        line = Integer.parseInt(op[1]);        // line to be removed

                        // Check if line to be removed is valid
                        if (line < 1 || line > list.getCount()) {
                            System.out.printf("The provided limit is not valid. Values must be between 1-%d.\n", list.getCount());
                            continue;
                        }
                    } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }

                    // Remove only given line
                    if (list.removeLine(line)) System.out.println("Removal done successfully.");
                    else System.out.println("Error while removing from list.");
                }

            /*==========================================================================================================
            Remove from given line until the end of the list
            ==========================================================================================================*/
                case ":xG" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    int line = 0;

                    // Try-catch block to check if input is valid
                    try {
                        line = Integer.parseInt(op[1]);        // first line to be removed

                        // Check if first line to be removed is valid
                        if (line < 1 || line > list.getCount()) {
                            System.out.printf("The provided limit is not valid. Values must be between 1-%d.\n", list.getCount());
                            continue;
                        }
                    } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }

                    // Remove from given line until the end of the list
                    if (list.removeLines(line, list.getCount(), "standard")) System.out.println("Removal done successfully.");
                    else System.out.println("Error while removing from the list.");
                }

            /*==========================================================================================================
            Remove from beginning of the list until given line
            ==========================================================================================================*/
                case ":XG" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    int line = 0;

                    // Try-catch block to check if input is valid
                    try {
                        line = Integer.parseInt(op[1]);        // last line to be removed

                        // Check if last line to be removed is valid
                        if (line < 1 || line > list.getCount()) {
                            System.out.printf("The provided limit is not valid. Values must be between 1-%d.\n", list.getCount());
                            continue;
                        }
                    } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }

                    // Remove from beginning of the list until given line
                    if (list.removeLines(0, line, "reverse")) System.out.println("Removal done successfully.");
                    else System.out.println("Error while removing from the list.");
                }

            /*==========================================================================================================
            Display the lines that contain given element or swap first element for second element
            ==========================================================================================================*/
                case ":/" -> {

                    // Check if input is valid
                    if (op.length != 2 && op.length != 3) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // Check if file was read and list is not null
                    if (list == null || list.isEmpty()) {
                        System.out.println("List is empty. Type :e <fileName.ext> to read the file and fill the list.");
                        continue;
                    }

                    String element, replacement;

                    // Command :/ <element>
                    if (op.length == 2) {
                        element = op[1];
                        System.out.printf("Searching for \"%s\" element...\n\n", element);

                        // Feedback
                        if (list.searchElement(element)) System.out.printf("\nElement \"%s\" was successfully found.\n", element);
                        else System.out.printf("\n\"%s\" not found.\n", element);

                    // Command :/ <element> <replacement>
                    } else {
                        element = op[1];
                        replacement = op[2];

                        System.out.printf("Searching for \"%s\" element and replacing it with \"%s\"...\n", element, replacement);

                        // Feedback
                        if (list.replaceElement(element, replacement)) System.out.println("\nAll lines successfully updated.\n");
                        else System.out.printf("\nElement \"%s\" not found.\n", element);
                    }
                }

            /*==========================================================================================================
            Add one or more lines and insert them after given line
            ==========================================================================================================*/
                case ":a" -> {

                    // Check if input is valid
                    if (op.length != 2) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // If list was not created yet
                    if (list == null) list = new List();

                    // Try-catch block to check if input is valid
                    try {
                        // Value of position to insert new lines
                        int pos = Integer.parseInt(op[1]);

                        if (pos < 0 || pos > list.getCount()) {
                            System.out.printf("Invalid position. Value must be between 0-%d.\n", list.getCount());
                            continue;
                        }

                        String data;    // Data to be inserted
                        int i = 0;      // Counter

                        // Do-while input is not :a
                        do {
                            System.out.print("Enter new data [Type :a to stop]: ");
                            data = scanner.nextLine();

                            // If option is to stop adding, continue
                            if (data.equals(":a")) continue;

                            // If list is empty, append data and increase counter
                            else if (list.isEmpty()) {
                                list.append(data);
                                System.out.println("New data successfully added to the list.\n");
                                ++i;

                            // If list is not empty
                            } else if (list.addLineAfter(data, pos + i)) {
                                System.out.println("New data successfully added to the list.\n");
                                ++i;
                            } else {
                                System.out.println("Could not add new data to the list.");
                                break;
                            }
                        } while (!data.equals(":a"));

                    } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }
                }

            /*==========================================================================================================
            Insert line before given position
            ==========================================================================================================*/
                case ":i" -> {

                    // Check if input is valid
                    if (op.length < 3) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }

                    // If list was not created yet
                    if (list == null) list = new List();

                    // Try-catch block to check if input is valid
                    try {
                        // Value of position to insert new line
                        int pos = Integer.parseInt(op[1]);

                        if (pos < 1 || pos > list.getCount() + 1) {
                            System.out.printf("Invalid position. Value must be between 1-%d.\n", list.getCount() + 1);
                            continue;
                        }

                        // Get whole text
                        String data = String.join(" ", Arrays.copyOfRange(op, 2, op.length));

                        // If list is empty, append data
                        if (list.isEmpty()) {
                            list.append(data);
                            System.out.println("New data successfully added to the list.");
                        }
                        else if (list.addLineBefore(data, pos)) System.out.println("New data successfully added to the list.");
                        else System.out.println("Could not add new data to the list.");

                    } catch (NumberFormatException e) { System.out.println("Invalid input. Type :help to se all available commands."); }
                }

            /*==========================================================================================================
            Show all available commands
            ==========================================================================================================*/
                case ":help" -> {

                    // Check if input is valid
                    if (op.length != 1) {
                        System.out.println("Invalid input. Type :help to se all available commands.");
                        continue;
                    }
                    showCommands();
                }

            /*==========================================================================================================
            End program
            ==========================================================================================================*/
                case ":q!" -> {
                    if (op.length != 1) System.out.println("Invalid input. Type :help to se all available commands.");
                    else if (quit()) {
                        scanner.close();
                        exit = true;
                    }
                }

            /*==========================================================================================================
            Invalid input
            ==========================================================================================================*/
                default -> System.out.println("Invalid input. Type :help to se all available commands.");
            }
        } while (!exit);
    }

    public static boolean readFile(List list, String filePath) {

        int lineNumber = 1;

        try {
            FileReader fileReader = new FileReader(filePath);
            Scanner scanner = new Scanner(fileReader);

            // Read file line by line and store its content in the list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Inform if line could not be added
                if (!list.append(line)) System.out.println("Could not add line " + lineNumber + " to the list.");

                // test - comment out after
                // if (list.append(line)) System.out.println("Line " + lineNumber + " successfully added to list.");
                // else System.out.println("Could not add line " + lineNumber + " to the list.");

                ++lineNumber;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }

    public static boolean writeFile(List list, String filePath) {

        // Try-catch to open file and write list's content on it
        try (FileWriter fileWriter = new FileWriter(filePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Start at head
            Node current = list.getHead();
            int i = 0;

            // Traverse the list writing its content to the file
            while (i < list.getCount()) {
                // Write each node's content in the file
                bufferedWriter.write(current.getData());
                // Write newline character
                bufferedWriter.newLine();

                // Update current node and counter
                current = current.getNext();
                ++i;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static boolean verifyBounds(List list, int x, int y) {
        // First line cannot be less than 0
        // Last line cannot be more than list length
        // First line must be less than or equal last line
        return (x > 0 && y <= list.getCount() && x <= y);
    }

    public static boolean copySelectedText(List list, List copy, int x, int y) {

        // Start at head
        Node current = list.getHead();
        int i = 1;

        // Traverse the list, from x to y, copying its content
        try {
            while (i < x) {
                // Update current node and counter
                current = current.getNext();
                ++i;
            }
            while (i <= y) {
                copy.append(current.getData());
                // Update current node and counter
                current = current.getNext();
                ++i;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static void showCommands() {

        String[] text = {
                ":e filename.ext",
                "\tOpen the file named \"filename.ext,\" read its content, and store each line in a node of a circular (singly or doubly) linked list. Provide an appropriate message to the editor's user or display the entire stored text.",
                "\n:w filename.ext",
                "\tSave the content of the circular (singly or doubly) linked list to a file named \"filename.ext.\" Then, display a coherent message to the editor's user.",
                "\n:q!",
                "\tExit the editor without saving the modifications made. Before exiting, request confirmation and inform the editor's user with a message.",
                "\n:v LineStart LineEnd",
                "\tMark a section of text in the list (for copying or cutting) from LineStart to LineEnd. It should be checked whether the range [LineStart, LineEnd] is valid. If it's not valid, inform the editor's user. Otherwise, perform the operation, display the marked source code content, and provide an appropriate message to the user.",
                "\n:y",
                "\tCopy the marked text to a Copy list and display a suitable message to the user.",
                "\n:c",
                "\tCut the marked text and display a suitable message to the user.",
                "\n:p LineStartPaste",
                "\tPaste the marked text starting from the initial line (LineStartPaste). It should be checked whether LineStartPaste is valid. If it's not valid, inform the editor's user. Otherwise, display an appropriate message to the user.",
                "\n:s",
                "\tDisplay the complete source code content on the screen, 10 lines at a time.",
                "\n:s LineStart LineEnd",
                "\tDisplay the source code content from the list, ranging from LineStart to LineEnd, 10 lines at a time. The line number of each line of code should be shown alongside it (blank lines, if any, count as valid lines).",
                "\n:x Line",
                "\tDelete the line at position \"Line\" from the list and display an appropriate message.",
                "\n:xG Line",
                "\tDelete from the line \"Line\" to the end of the list and display an appropriate message.",
                "\n:XG Line",
                "\tDelete from the line \"Line\" to the beginning of the list and display an appropriate message.",
                "\n:/ element",
                "\tTraverse the list, locate the line(s) (with the corresponding line number) where the \"element\" is found, and display them.",
                "\n:/ elem elemToReplace",
                "\tTraverse the list, locate \"elem,\" and replace it with \"elemToReplace\" in all lines of the source code. Display an appropriate message upon completion.",
                "\n:a LinePosition",
                "\tAllow the editing of one or more new lines and insert them into the list after position LinePosition. The input is terminated by a \":a\" on an empty line. When the list is empty, insert from the beginning of the list. Display an appropriate message upon completion.",
                "\n:i LinePosition [new line content]",
                "\tAllow the insertion of the line \"[new line content]\" and insert it into the list before position LinePosition. When the list is empty, insert at the beginning of the list. Display an appropriate message upon completion."
        };
        System.out.println("\n---------------------------------------------------------------------------------\n");

        // Print each 4 lines
        int lines = 4;
        int current = 0;
        Scanner scanner = new Scanner(System.in);

        for (String s : text) {
            System.out.println(s);
            current++;

            if (current == lines) {
                current = 0;
                System.out.print("\nPress any key to continue or type Q to go back to menu. ");
                String option = scanner.nextLine().trim().toUpperCase();

                if (option.equals("Q")) break;
            }
        }
        System.out.println("\n---------------------------------------------------------------------------------\n");
    }

    public static void showHeader() {
        System.out.println("""
                =================================================================================
                                                    VI EDITOR
                =================================================================================   
                """);
    }

    public static boolean quit() {

        System.out.print("Are you sure you want to exit? [Y/N] ");
        String ans;
        Scanner s = new Scanner(System.in);

        while (true) {
            ans = s.nextLine().trim().toUpperCase();

            if (ans.equals("Y")) {
                System.out.println("\n---------------------------------------------------------------------------------\n");
                System.out.println("\nEnding Program. . .\nE N D");
                return true;

            } else if (ans.equals("N")) return false;

            else System.out.print("Invalid option. Type 'Y' to exit program or 'N' to go back to menu: ");
        }
    }
}
