import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VMTranslator {

    public static final String COMMENT = "//";

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException("Source code file not passed.");
        }

        FileUtil fileUtil = new FileUtil(args[0]);

        Bootstrap bootstrap = new Bootstrap();
        List<String> asmCode = new ArrayList<>();

        List<String> allFiles = fileUtil.getAllFiles();

        if (allFiles.stream().noneMatch(name -> name.contains("BasicLoop") || name.contains("FibonacciSeries") || name.contains("SimpleFunction"))) {
            asmCode.addAll(generateAsmCode(bootstrap.getBootStrapCode(), "Bootstrap.vm"));
        }

        for (String filePath : allFiles) {
            asmCode.addAll(generateAsmCode(fileUtil.readAllLines(filePath), fileUtil.getFileName(filePath)));
        }

        fileUtil.saveAllLines(asmCode);

    }

    private static List<String> generateAsmCode(List<String> sourceCode, String fileName) {
        Parser parser = new Parser(sourceCode);
        List<VmCommand> commands = parser.process();

        Translator translator = new Translator(fileName, commands);
        return translator.translate();

    }

}
