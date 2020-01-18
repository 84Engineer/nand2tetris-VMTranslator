import java.util.ArrayList;
import java.util.List;

public class ArithmeticTranslate {

    private static final String EQ_LABEL = "EQ_LABEL_";
    private static final String GT_LABEL = "GT_LABEL_";
    private static final String LT_LABEL = "LT_LABEL_";

    private static long lblId = 0;

    public List<String> translate(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        switch (vmCommand.getOpCode()) {
            case add:
                result.addAll(twoArgsOperation("D=D+M"));
                break;
            case sub:
                result.addAll(twoArgsOperation("D=M-D"));
                break;
            case neg:
                result.add("@" + Translator.SP);
                result.add("A=M-1");
                result.add("M=-M");
                break;
            case eq:
                result.addAll(twoArgsOperation("D=M-D"));
                result.add("@" + Translator.SP);
                result.add("A=M");
                result.add("A=A-1");
                result.add("M=-1");
                String eqLbl = EQ_LABEL + lblId++;
                result.add("@" + eqLbl);
                result.add("D;JEQ");
                result.add("@" + Translator.SP);
                result.add("A=M-1");
                result.add("M=0");
                result.add("(" + eqLbl + ")");
                break;
            case gt:
                result.addAll(twoArgsOperation("D=M-D"));
                result.add("@" + Translator.SP);
                result.add("A=M");
                result.add("A=A-1");
                result.add("M=-1");
                String gtLbl = GT_LABEL + lblId++;
                result.add("@" + gtLbl);
                result.add("D;JGT");
                result.add("@" + Translator.SP);
                result.add("A=M-1");
                result.add("M=0");
                result.add("(" + gtLbl + ")");
                break;
            case lt:
                result.addAll(twoArgsOperation("D=M-D"));
                result.add("@" + Translator.SP);
                result.add("A=M");
                result.add("A=A-1");
                result.add("M=-1");
                String ltLbl = LT_LABEL + lblId++;
                result.add("@" + ltLbl);
                result.add("D;JLT");
                result.add("@" + Translator.SP);
                result.add("A=M-1");
                result.add("M=0");
                result.add("(" + ltLbl + ")");
                break;
            case and:
                result.addAll(twoArgsOperation("D=D&M"));
                break;
            case or:
                result.addAll(twoArgsOperation("D=D|M"));
                break;
            case not:
                result.add("@" + Translator.SP);
                result.add("A=M-1");
                result.add("M=!M");
                break;
            default:
                throw new IllegalStateException("Unsupported command: " + vmCommand.getOpCode());
        }
        return result;
    }

    public List<String> twoArgsOperation(String operation) {
        List<String> result = new ArrayList<>();
//        result.add("@" + Translator.SP);
//        result.add("AM=M-1");
//        result.add("D=M");
//        result.add("A=A-1");
//        result.add(operation);
        result.add("@" + Translator.SP);
        result.add("M=M-1");
        result.add("A=M");
        result.add("D=M");
        result.add("@" + Translator.SP);
        result.add("M=M-1");
        result.add("A=M");
        result.add(operation);
        result.add("@" + Translator.SP);
        result.add("A=M");
        result.add("M=D");
        result.add("@" + Translator.SP);
        result.add("M=M+1");
        return result;
    }

}
