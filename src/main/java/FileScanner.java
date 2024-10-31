import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс для сканирования директории и поиска текстовых файлов.
 */
public class FileScanner {
    /**
     * Находит текстовые файлы в корневой директории.
     *
     * @param rootDir Корневая директория для поиска.
     * @return Список найденных текстовых файлов.
     */
    public List<File> findTextFiles(File rootDir){
        List<File> textFiles = new ArrayList<>();
        scanDirectory(rootDir, textFiles);
        textFiles.sort(Comparator.comparing(File::getName));
        return textFiles;
    }

    /**
     * Рекурсивно сканирует директорию на наличие текстовых файлов.
     *
     * @param rootDir Корневая директория для сканирования.
     * @param textFiles Список для хранения найденных текстовых файлов.
     */
    private void scanDirectory(File rootDir, List<File> textFiles) {
        File[] files = rootDir.listFiles();
        if(files != null){
            for(File file : files){
                if(file.isDirectory()){
                    scanDirectory(file, textFiles);
                } else if (file.getName().endsWith(".txt")) {
                    textFiles.add(file);
                }
            }
        }
    }
}
