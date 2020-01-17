import java.io.IOException;
import java.util.List;

public class VMTranslator {

    public static final String COMMENT = "//";

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException("Source code file not passed.");
        }

        FileUtil fileUtil = new FileUtil(args[0]);

        List<String> sourceCode = fileUtil.readAllLines();

        Parser parser = new Parser(sourceCode);
        List<VmCommand> commands = parser.process();

        Translator translator = new Translator(fileUtil.getFileName(args[0]), commands);
        List<String> asmCode = translator.translate();

        fileUtil.saveAllLines(asmCode);

    }

}
