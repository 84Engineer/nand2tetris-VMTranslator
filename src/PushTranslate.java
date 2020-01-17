import java.util.ArrayList;
import java.util.List;

public class PushTranslate {

    private String fileName;

    public PushTranslate(String fileName) {
        this.fileName = fileName;
    }

    public List<String> translate(VmCommand vmCommand) {
        String arg0 = vmCommand.getArg0();
        String segmentStr = arg0.equals("this") || arg0.equals("static") ? "_" + arg0 : arg0;
        Translator.MemSegment memSegment = Translator.MemSegment.valueOf(segmentStr);

        List<String> result = new ArrayList<>();

        switch (memSegment) {
            case local:
                result.addAll(push(Translator.LCL, vmCommand));
                break;
            case argument:
                result.addAll(push(Translator.ARG, vmCommand));
                break;
            case _this:
                result.addAll(push(Translator.THIS, vmCommand));
                break;
            case that:
                result.addAll(push(Translator.THAT, vmCommand));
                break;
            case constant:
                result.addAll(pushConstant(vmCommand));
                break;
            case _static:
                result.addAll(pushStatic(vmCommand));
                break;
            case pointer:
                result.addAll(pushPointer(vmCommand));
                break;
            case temp:
                result.addAll(pushTemp(vmCommand));
                break;
            default:
                throw new IllegalStateException("Unsupported memory segment: " + memSegment);
        }
        return result;
    }

    private List<String> pushConstant(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + vmCommand.getArg1());
        result.add("D=A");
        result.addAll(push());
        return result;
    }

    private List<String> pushPointer(VmCommand vmCommand) {
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
        result.add("D=M");
        result.addAll(push());
        return result;
    }

    private List<String> pushStatic(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + fileName + "." + vmCommand.getArg1());
        result.add("D=M");
        result.addAll(push());
        return result;
    }

    private List<String> pushTemp(VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + (Translator.TEMP_START + Long.parseLong(vmCommand.getArg1())));
        result.add("D=M");
        result.addAll(push());
        return result;
    }

    private List<String> push(String memSegment, VmCommand vmCommand) {
        List<String> result = new ArrayList<>();
        result.add("@" + memSegment);
        result.add("D=M");
        result.add("@" + vmCommand.getArg1());
        result.add("D=D+A");
        result.add("A=D");
        result.add("D=M");
        result.addAll(push());
        return result;
    }

    private List<String> push() {
        List<String> result = new ArrayList<>();
        result.add("@" + Translator.SP);
        result.add("A=M");
        result.add("M=D");
        result.add("@" + Translator.SP);
        result.add("M=M+1");
        return result;
    }


}
