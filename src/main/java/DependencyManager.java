import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Класс для управления зависимостями файлов и их сортировкой.
 */
public class DependencyManager {
    private final Map<File, List<File>> dependencyGraph = new HashMap<>();
    //Список поддерживаемых расширений для текстовых файлов
    private final List<String> textFileExtensions = Arrays.asList(
            "txt", "md", "log", "xml", "doc", "docx", "odt", "rtf", "csv"
    );

    /**
     * Анализирует зависимости файлов.
     *
     * @param files Список файлов для анализа.
     * @throws IOException Если возникает ошибка при чтении файлов.
     */
    public void analyzeDependencies(List<File> files) throws IOException{
        for(File file : files){
            List<File> dependencies = parseDependencies(file);
            dependencyGraph.put(file, dependencies);
        }
    }

    /**
     * Сортирует файлы в порядке их зависимостей.
     *
     * @return Отсортированный список файлов.
     * @throws Exception Если обнаружена циклическая зависимость.
     */
    public List<File> sortFiles() throws Exception{
        List<File> sortedFiles =  new ArrayList<>();
        Set<File> visited = new HashSet<>();
        Set<File> recStack = new HashSet<>();
        for(File file : dependencyGraph.keySet()){
            if(!visited.contains(file) && !topologicalSort(file, visited, recStack, sortedFiles)){
                throw new Exception("Circular dependency detected");
            }
        }
        return sortedFiles;
    }

    /**
     * Находит текстовые файлы в указанной директории.
     *
     * @param directory Директория для поиска.
     * @return Список найденных текстовых файлов.
     */
    public List<File> findTextFiles(File directory){
        List<File> textFiles = new ArrayList<>();
        if (directory.isDirectory()){
            for (File file : Objects.requireNonNull(directory.listFiles())){
                if(file.isDirectory()){
                    textFiles.addAll(findTextFiles(file)); //Рекурсивный поиск
                } else if (isTextFile(file)){
                    textFiles.add(file);
                }
            }
        }
        return textFiles;
    }
    /**
     * Проверяет, является ли файл текстовым.
     *
     * @param file Файл для проверки.
     * @return true, если файл текстовый; иначе false.
     */
    boolean isTextFile(File file){
        String name = file.getName().toLowerCase();
        String extension = getFileExtension(name);
        return textFileExtensions.contains(extension);
    }

    /**
     * Получает расширение файла.
     *
     * @param fileName Имя файла.
     * @return Расширение файла или пустая строка, если расширение отсутствует.
     */
    private String getFileExtension(String fileName){
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1){
            return ""; //Если нет расширений
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    /**
     * Выполняет топологическую сортировку файлов.
     *
     * @param file Текущий файл для сортировки.
     * @param visited Множество посещенных файлов.
     * @param recStack Множество файлов в текущем стеке.
     * @param sortedFiles Список для хранения отсортированных файлов.
     * @return true, если сортировка выполнена успешно; иначе false.
     */
    private boolean topologicalSort(File file, Set<File> visited, Set<File> recStack, List<File> sortedFiles) {
        visited.add(file);
        recStack.add(file);

        for (File dep : dependencyGraph.getOrDefault(file, Collections.emptyList())){
            if (recStack.contains(dep) || (!visited.contains(dep) && !topologicalSort(dep, visited, recStack, sortedFiles))){
                return false;
            }
        }

        recStack.remove(file);
        sortedFiles.add(file);
        return true;
    }

    /**
     * Парсит зависимости из файла.
     *
     * @param file Файл для анализа.
     * @return Список зависимостей.
     * @throws IOException Если возникает ошибка при чтении файла.
     */
    private List<File> parseDependencies(File file) throws IOException {
        List<File> dependencies = new ArrayList<>();
        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines){
            if (line.startsWith("*require '")){
                String requiredFileName = line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"));
                File dependecnyFile = new File(file.getParentFile(), requiredFileName);
                dependencies.add(dependecnyFile);
            }
        }
        return dependencies;
    }
}
