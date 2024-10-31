import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class DependencyManager {
    private Map<File, List<File>> dependencyGraph = new HashMap<>();

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
        for (String line : Files.readAllLines(file.toPath())){
            if (line.startsWith("*require '")){
                String dePath = line.split("'")[1].split("'")[0];
                dependencies.add(new File(dePath));
            }
        }
        return dependencies;
    }
}
