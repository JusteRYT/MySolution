import java.io.File;
import java.util.List;

/**
 * Главный класс для запуска программы.
 */
public class Main {
    public static void main(String[] args) {
        String directoryPath = System.getProperty("user.dir");
        String outputFilePath = "output.txt";

        try {
            //Поиск всех файлов в данной директории
            DependencyManager manager = new DependencyManager();
            List<File> files = manager.findTextFiles(new File(directoryPath));

            //Анализ и сортировка
            manager.analyzeDependencies(files);
            List<File> sortedFiles = manager.sortFiles();

            //Конкатенация файлов
            FileConcatenator concatenator = new FileConcatenator();
            concatenator.concatenateFiles(sortedFiles, outputFilePath);

            System.out.println("Files concatenated successfully into" + outputFilePath);

        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
