import java.util.ArrayList;
import java.util.List;

public class OperationTranslate {

    public List<String> translate(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        switch (vmCommand.getOpCode()) {
            case add:
                result.add("@" + Translator.SP);
                result.add("M=M-1");
                result.add("A=M");
                result.add("D=M");
                result.add("@" + Translator.SP);
                result.add("M=M-1");
                result.add("A=M");
                result.add("D=D+M");
                result.add("@" + Translator.SP);
                result.add("A=M");
                result.add("M=D");
                break;
            case sub:
                result.add("@" + Translator.SP);
                result.add("M=M-1");
                result.add("A=M");
                result.add("D=M");
                result.add("@" + Translator.SP);
                result.add("M=M-1");
                result.add("A=M");
                result.add("D=M-D");
                result.add("@" + Translator.SP);
                result.add("A=M");
                result.add("M=D");
                break;
            case neg:
                break;
            case eq:
                break;
            case gt:
                break;
            case lt:
                break;
            case and:
                break;
            case or:
                break;
            case not:
                break;
            default:
                throw new IllegalStateException("Unsupported command: " + vmCommand.getOpCode());
        }
        return result;
    }

}
