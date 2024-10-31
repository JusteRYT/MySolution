import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileScannerTest {
    @Test
    public void testFindTextFiles() throws Exception{
        Path tempDir = Files.createTempDirectory("testDir");
        File file1 = Files.createFile(tempDir.resolve("file1.txt")).toFile();
        File file2 = Files.createFile(tempDir.resolve("file2.txt")).toFile();
        File nonTextFile = Files.createFile(tempDir.resolve("image.jpg")).toFile();

        FileScanner scanner = new FileScanner();
        List<File> files = scanner.findTextFiles(tempDir.toFile());

        assertEquals(2, files.size());
        assertTrue(files.contains(file1));
        assertTrue(files.contains(file2));

        Files.deleteIfExists(file1.toPath());
        Files.deleteIfExists(file2.toPath());
        Files.deleteIfExists(nonTextFile.toPath());
        Files.deleteIfExists(tempDir);
    }
}
