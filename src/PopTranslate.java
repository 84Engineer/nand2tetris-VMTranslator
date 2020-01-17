import java.util.ArrayList;
import java.util.List;

public class PopTranslate {

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
                break;
            case _this:
                break;
            case that:
                break;
            case constant:
                break;
            case _static:
                break;
            case pointer:
                break;
            case temp:
                break;
            default:
                throw new IllegalStateException("Unsupported memory segment: " + memSegment);
        }
        return result;
    }

//    private List<String> pop(String memSegment, VmCommand vmCommand) {
//        List<String> result = new ArrayList<>();
//        result.add("@" + vmCommand.getArg1());
//        result.add("D=A");
//
//        return result;
//    }

    private List<String> pop(String memSegment, VmCommand vmCommand) {
        //
        List<String> result = new ArrayList<>();
//        result.add("@" + Translator.SP);
//        result.add("D=M");

        result.add("@" + memSegment);
        result.add("D=M");
        result.add("@" + vmCommand.getArg1());
        result.add("D=D+A");
        result.add("A=D");




        result.add("@" + Translator.SP);
        result.add("M=M-1");
        return result;
    }


}
