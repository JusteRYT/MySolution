import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileConcatenatorTest {

    @Test
    public void testConcatenateFiles() throws Exception{
        Path tempDir = Files.createTempDirectory("testConcat");
        File file1 = Files.createFile(tempDir.resolve("file1.txt")).toFile();
        File file2 = Files.createFile(tempDir.resolve("file2.txt")).toFile();
        File outputFile = tempDir.resolve("output.txt").toFile();

        Files.writeString(file1.toPath(), "Content of file 1");
        Files.writeString(file2.toPath(), "Content of file 2");

        FileConcatenator concatenator = new FileConcatenator();
        concatenator.concatenateFiles(List.of(file1, file2), outputFile.getPath());

        String expectedContent = "Content of file 1" + System.lineSeparator() +
                "Content of file 2" + System.lineSeparator();
        String actualContent = Files.readString(outputFile.toPath());
        assertEquals(expectedContent, actualContent);

        Files.deleteIfExists(file1.toPath());
        Files.deleteIfExists(file2.toPath());
        Files.deleteIfExists(outputFile.toPath());
        Files.deleteIfExists(tempDir);
    }
}
