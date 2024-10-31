import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class DependencyManager {
    private Map<File, List<File>> dependencyGraph = new HashMap<>();
    //Список поддерживаемых расширений для текстовых файлов
    private final List<String> textFileExtensions = Arrays.asList(
            "txt", "md", "log", "xml", "doc", "docx", "odt", "rtf", "csv"
    );

    public void analyzeDependencies(List<File> files) throws IOException{
        for(File file : files){
            List<File> dependencies = parseDependencies(file);
            dependencyGraph.put(file, dependencies);
        }
    }

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

    //Метод для поиска текстовых файлов
    public List<File> findTextFiles(File directory){
        List<File> textFiles = new ArrayList<>();
        if (directory.isDirectory()){
            for (File file : directory.listFiles()){
                if(file.isDirectory()){
                    textFiles.addAll(findTextFiles(file)); //Рекурсивный поиск
                } else if (isTextFile(file)){
                    textFiles.add(file);
                }
            }
        }
        return textFiles;
    }
    //Проверка, является ли файл текстовым
    private boolean isTextFile(File file){
        String name = file.getName().toLowerCase();
        String extension = getFileExtension(name);
        return textFileExtensions.contains(extension);
    }

    //Метод для получение расширения файл
    private String getFileExtension(String fileName){
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1){
            return ""; //Если нет расширений
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

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
