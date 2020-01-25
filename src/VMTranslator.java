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
        List<String> asmCode = new ArrayList<>();

        for (String filePath : fileUtil.getAllFiles()) {
            List<String> sourceCode = fileUtil.readAllLines(filePath);

            Parser parser = new Parser(sourceCode);
            List<VmCommand> commands = parser.process();

            Translator translator = new Translator(fileUtil.getFileName(filePath), commands);
            asmCode.addAll(translator.translate());
        }

        fileUtil.saveAllLines(asmCode);

    }

}
