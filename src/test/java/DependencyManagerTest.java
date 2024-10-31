import org.junit.jupiter.api.Test;

import javax.tools.ForwardingFileObject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class DependencyManagerTest {
    @Test
    public void testSortFilesNoCycle() throws Exception{
        Path tempDir = Files.createTempDirectory("testDir");
        File file1 = Files.createFile(tempDir.resolve("file1.txt")).toFile();
        File file2 = Files.createFile(tempDir.resolve("file2.txt")).toFile();
        Files.writeString(file1.toPath(), "*require 'file2.txt'*");

        DependencyManager manager = new DependencyManager();
        manager.analyzeDependencies(List.of(file1, file2));
        List<File> sortedFiles = manager.sortFiles();
        assertEquals(List.of(file2, file1), sortedFiles);

        Files.deleteIfExists(file1.toPath());
        Files.deleteIfExists(file2.toPath());
        Files.deleteIfExists(tempDir);
    }
}
