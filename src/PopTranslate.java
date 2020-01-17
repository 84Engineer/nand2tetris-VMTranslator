import java.util.ArrayList;
import java.util.List;

public class PopTranslate {

    public static String TEMP_REG = "R13";

    private String fileName;

    public PopTranslate(String fileName) {
        this.fileName = fileName;
    }

    public List<String> translate(VmCommand vmCommand) {
        String arg0 = vmCommand.getArg0();
        String segmentStr = arg0.equals("this") || arg0.equals("static") ? "_" + arg0 : arg0;
        Translator.MemSegment memSegment = Translator.MemSegment.valueOf(segmentStr);

        List<String> result = new ArrayList<>();
        switch (memSegment) {
            case local:
                result.addAll(pop(Translator.LCL, vmCommand));
                break;
            case argument:
                result.addAll(pop(Translator.ARG, vmCommand));
                break;
            case _this:
                result.addAll(pop(Translator.THIS, vmCommand));
                break;
            case that:
                result.addAll(pop(Translator.THAT, vmCommand));
                break;
            case _static:
                result.addAll(popStatic(vmCommand));
                break;
            case pointer:
                result.addAll(popPointer(vmCommand));
                break;
            case temp:
                result.addAll(popTemp(vmCommand));
                break;
            default:
                throw new IllegalStateException("Unsupported memory segment: " + memSegment);
        }
        return result;
    }

    private List<String> popPointer(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        switch (Integer.parseInt(vmCommand.getArg1())) {
            case 0:
                result.add("@" + Translator.THIS);
                break;
            case 1:
                result.add("@" + Translator.THAT);
                break;
            default:
                throw new IllegalArgumentException("Cannot push pointer " + vmCommand.getArg1());
        }
        result.add("D=A");
        result.addAll(pop());
        return result;
    }

    private List<String> popTemp(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + (Translator.TEMP_START + Long.parseLong(vmCommand.getArg1())));
        result.add("D=A");
        result.addAll(pop());
        return result;
    }

    private List<String> popStatic(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + fileName + "." + vmCommand.getArg1());
        result.add("D=A");
        result.addAll(pop());
        return result;
    }

    private List<String> pop(String memSegment, VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + memSegment);
        result.add("D=M");
        result.add("@" + vmCommand.getArg1());
        result.add("D=D+A");
        result.addAll(pop());
        return result;
    }

    private List<String> pop() {
        List<String> result = new ArrayList<>();
        // D should contain memory segment address
        result.add("@" + TEMP_REG); // saving mem segment address to temp register
        result.add("M=D");
        result.add("@" + Translator.SP);
        result.add("M=M-1");
        result.add("A=M");
        result.add("D=M"); // taking value from the stack
        result.add("@" + TEMP_REG);
        result.add("A=M");
        result.add("M=D"); // saving value to mem segment
        return result;
    }


}
