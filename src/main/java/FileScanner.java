import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileScanner {
    public List<File> findTextFiles(File rootDir){
        List<File> textFiles = new ArrayList<>();
        scanDirectory(rootDir, textFiles);
        textFiles.sort(Comparator.comparing(File::getName));
        return textFiles;
    }

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
