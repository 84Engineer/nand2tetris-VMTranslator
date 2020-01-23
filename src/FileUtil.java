import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUtil {

   private String filePath;

   public FileUtil(String filePath) {
      this.filePath = filePath;
   }

   public List<String> readAllLines() throws IOException {
      return Files.readAllLines(Paths.get(filePath));
   }

   public void saveAllLines(List<String> lines) throws IOException {
      Path sourceFile = Paths.get(filePath);
      Path parent = sourceFile.getParent();

      String outputFileName = getFileName(sourceFile.getFileName().toString()) + ".asm";

      Path outputFile = parent != null ? parent.resolve(outputFileName) : sourceFile.resolveSibling(outputFileName);

      Files.write(outputFile, lines);
   }

   public String getFileName(String filePath) {
      String separator = File.separator.equals("\\") ? "\\\\" : File.separator;
      String[] fileParts = filePath.split(separator);
      return fileParts[fileParts.length - 1].split("\\.")[0];
   }

}
