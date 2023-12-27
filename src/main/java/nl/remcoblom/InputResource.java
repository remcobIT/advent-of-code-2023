package nl.remcoblom;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputResource {

    public static BufferedReader asBufferedReader(String resource) throws IOException {
        Path inputFilePath = Path.of("src/main/resources/" + resource);
        return Files.newBufferedReader(inputFilePath);
    }
}
