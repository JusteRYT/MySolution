import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class DependencyManagerTest {
    private DependencyManager manager;

    @BeforeEach
    void setUp(){
        manager = new DependencyManager();
    }
    @Test
    void testIsTextFile() {
        // Позитивные тесты
        assertTrue(manager.isTextFile(new File("test.txt")));
        assertTrue(manager.isTextFile(new File("document.md")));
        assertTrue(manager.isTextFile(new File("notes.log")));
        assertTrue(manager.isTextFile(new File("data.xml")));
        assertTrue(manager.isTextFile(new File("report.docx")));

        // Негативные тесты
        assertFalse(manager.isTextFile(new File("image.png")));
        assertFalse(manager.isTextFile(new File("video.mp4")));
        assertFalse(manager.isTextFile(new File("archive.zip")));
        assertFalse(manager.isTextFile(new File("document"))); // Без расширения
    }

    @Test
    void testFindTextFiles() throws IOException {
        // Создайте временную директорию и файлы для тестирования
        File tempDir = new File("tempTestDir");
        tempDir.mkdir();

        // Создание тестовых файлов
        new File(tempDir, "file1.txt").createNewFile();
        new File(tempDir, "file2.md").createNewFile();
        new File(tempDir, "file3.jpg").createNewFile();
        new File(tempDir, "subDir").mkdir();
        new File(tempDir + "/subDir", "file4.xml").createNewFile();

        // Проверка, что найденные файлы соответствуют ожиданиям
        List<File> textFiles = manager.findTextFiles(tempDir);
        assertEquals(3, textFiles.size());
        assertTrue(textFiles.stream().anyMatch(file -> file.getName().equals("file1.txt")));
        assertTrue(textFiles.stream().anyMatch(file -> file.getName().equals("file2.md")));
        assertTrue(textFiles.stream().anyMatch(file -> file.getName().equals("file4.xml")));

        // Удаление временной директории
        for (File file : tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }
}
