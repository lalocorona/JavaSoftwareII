package Lambdas;

import java.io.IOException;

/**
 * This interface is used to write to a file
 */
public interface WritingToFile {
    void write(String content) throws IOException;
}
