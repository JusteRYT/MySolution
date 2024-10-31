import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Класс FileConcatenator предназначен для объединения содержимого нескольких файлов
 * в один файл в порядке, определённом переданным списком.
 */
public class FileConcatenator {

    /**
     * Объединяет содержимое файлов из списка sortedFiles в один файл по указанному пути.
     *
     * @param sortedFiles список файлов, содержимое которых нужно объединить.
     * @param outputFilePath путь к файлу, в который будет записан результат конкатенации.
     * @throws IOException если возникает ошибка при записи в файл.
     */
    public void concatenateFiles(List<File> sortedFiles, String outputFilePath) throws IOException{
        try (FileWriter writer = new FileWriter(outputFilePath)){
            for(File file : sortedFiles){
                writer.write(new String(Files.readAllBytes(file.toPath())));
                writer.write(System.lineSeparator());
            }
        }
    }
}
