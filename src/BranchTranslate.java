import java.util.ArrayList;
import java.util.List;

public class BranchTranslate {

   private String fileName;

   public BranchTranslate(String fileName) {
      this.fileName = fileName;
   }

   public List<String> translate(VmCommand vmCommand) {
      List<String> result = new ArrayList<>();
      switch (vmCommand.getOpCode()) {
         case label:
            result.add(String.format("(%s.%s)", fileName, vmCommand.getArg0()));
            break;
         case _goto:
            result.add(String.format("@%s.%s", fileName, vmCommand.getArg0()));
            result.add("0;JMP");
            break;
         case if_goto:
            result.add("@" + Translator.SP);
            result.add("AM=M-1");
            result.add("D=M");
            result.add(String.format("@%s.%s", fileName, vmCommand.getArg0()));
            result.add("D;JNE");
            break;
      }
      return result;
   }

}
