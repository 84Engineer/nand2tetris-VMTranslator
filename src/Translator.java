import java.util.ArrayList;
import java.util.List;

public class Translator {

   public enum MemSegment {
      local, argument, _this, that, constant, _static, pointer, temp
   }

   private static final String SP = "R0";
   private static final String LCL = "R1";
   private static final String ARG = "R2";
   private static final String THIS = "R3";
   private static final String THAT = "R4";

   private static final long STATIC_START = 16L;
   private static final long STATIC_END = 255L;

   private static final long TEMP_START = 5L;
   private static final long TEMP_END = 12L;

   private List<VmCommand> vmCommands;

   public Translator(List<VmCommand> vmCommands) {
      this.vmCommands = vmCommands;
   }

   public List<String> translate() {
      List<String> asmCode = new ArrayList<>();
      for (VmCommand vmCommand : vmCommands) {
         asmCode.add(VMTranslator.COMMENT + " " + vmCommand.getVmCommand());
         switch (vmCommand.getOpCode()) {
            case push:
               asmCode.addAll(push(vmCommand));
               break;
            case pop:
               break;
            case add:
               break;
            case sub:
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
      }
      return asmCode;
   }

   private List<String> push(VmCommand vmCommand) {
      String arg0 = vmCommand.getArg0();
      String segmentStr = arg0.equals("this") || arg0.equals("static") ? "_" + arg0 : arg0;
      MemSegment memSegment = MemSegment.valueOf(segmentStr);

      List<String> result = new ArrayList<>();

      switch (memSegment) {
         case local:
            result.addAll(push(LCL, vmCommand));
            break;
         case argument:
            result.addAll(push(ARG, vmCommand));
            break;
         case _this:
            result.addAll(push(THIS, vmCommand));
            break;
         case that:
            result.addAll(push(THAT, vmCommand));
            break;
         case constant:
            result.add("@" + vmCommand.getArg1());
            result.add("D=A");
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
      return result;
   }

   private List<String> push() {
      List<String> result = new ArrayList<>();
      result.add("@" + SP);
      result.add("A=M");
      result.add("M=D");
      result.add("@" + SP);
      result.add("M=M+1");
      return result;
   }

}
