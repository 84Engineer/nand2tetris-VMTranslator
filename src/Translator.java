import java.util.ArrayList;
import java.util.List;

public class Translator {

   public enum MemSegment {
      local, argument, _this, that, constant, _static, pointer, temp
   }

   private static final long SP = 0L;
   private static final long LCL = 1L;
   private static final long ARG = 2L;
   private static final long THIS = 3L;
   private static final long THAT = 4L;

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

      switch (memSegment) {
         case local:
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
      return null;
   }

}
