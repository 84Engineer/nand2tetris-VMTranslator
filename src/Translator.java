import java.util.ArrayList;
import java.util.List;

public class Translator {

   public enum MemSegment {
      local, argument, _this, that, constant, _static, pointer, temp
   }

   public static final String SP = "R0";
   public static final String LCL = "R1";
   public static final String ARG = "R2";
   public static final String THIS = "R3";
   public static final String THAT = "R4";

//   private static final long STATIC_START = 16L;
//   private static final long STATIC_END = 255L;

   public static final long TEMP_START = 5L;
//   private static final long TEMP_END = 12L;

   private List<VmCommand> vmCommands;
   private PushTranslate pushTranslate;
   private PopTranslate popTranslate;
   private OperationTranslate operationTranslate;

   public Translator(String fileName, List<VmCommand> vmCommands) {
      this.vmCommands = vmCommands;
      this.pushTranslate = new PushTranslate(fileName);
      this.popTranslate = new PopTranslate(fileName);
      this.operationTranslate = new OperationTranslate();
   }

   public List<String> translate() {
      List<String> asmCode = new ArrayList<>();
      for (VmCommand vmCommand : vmCommands) {
         asmCode.add(VMTranslator.COMMENT + " " + vmCommand.getVmCommand());
         switch (vmCommand.getOpCode()) {
            case push:
               asmCode.addAll(pushTranslate.translate(vmCommand));
               break;
            case pop:
               asmCode.addAll(popTranslate.translate(vmCommand));
               break;
            default:
               asmCode.addAll(operationTranslate.translate(vmCommand));
         }
      }
      return asmCode;
   }

}
