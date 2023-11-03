import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader fileScan = new BufferedReader(new FileReader(
                    "C:\\Users\\jpedr\\IdeaProjects\\ed1\\lab\\vi-editor\\src\\sabrinaaCoderss.c"
            ));

            String line = fileScan.readLine();

            while (line != null) {
                System.out.println(line);
                line = fileScan.readLine();
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
