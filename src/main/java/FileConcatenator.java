import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

//Конкатенация файлов на основе упорядоченного списка
public class FileConcatenator {
    public void concatenateFiles(List<File> sortedFiles, String outputFilePath) throws IOException{
        try (FileWriter writer = new FileWriter(outputFilePath)){
            for(File file : sortedFiles){
                writer.write(new String(Files.readAllBytes(file.toPath())));
                writer.write(System.lineSeparator());
            }
        }
    }
}
