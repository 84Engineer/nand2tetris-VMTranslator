import java.io.IOException;
import java.util.List;

public class VMTranslator {

    public static final String COMMENT = "//";

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException("Source code file not passed.");
        }

        List<String> sourceCode = FileUtil.readAllLines(args[0]);

        Parser parser = new Parser(sourceCode);
        List<VmCommand> commands = parser.process();

        Translator translator = new Translator(commands);
        List<String> asmCode = translator.translate();


    }

}
