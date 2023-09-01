package Lambdas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteToFile implements WritingToFile {
    private final String filePath;

    public WriteToFile(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void write(String content) throws IOException {

        try {
            File file = new File(filePath);

            boolean fileExists = file.exists();
            FileWriter fileWriter = new FileWriter(file, true);

            // If the file didn't exist before, add a header or any initial content here
            if (!fileExists) {
                // For example, add a header line
                fileWriter.write("User Logins:\n");
            }

            // Write the data (a new line for a user login, for example)
            fileWriter.write(content + "\n");

            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
}

