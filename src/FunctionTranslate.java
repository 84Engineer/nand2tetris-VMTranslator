import java.util.ArrayList;
import java.util.List;

public class FunctionTranslate {

   private final static String TEMP_REG_0 = "R13";
   private final static String TEMP_REG_1 = "R14";

   private String fileName;

   public FunctionTranslate(String fileName) {
      this.fileName = fileName;
   }

   public List<String> translate(VmCommand vmCommand) {
      List<String> result = new ArrayList<>();
      switch (vmCommand.getOpCode()) {
         case function:
            result.add(String.format("(%s.%s)", fileName, vmCommand.getArg0()));
            for (int i = 0; i < Integer.parseInt(vmCommand.getArg1()); i++) {
               result.add("@" + Translator.SP);
               result.add("A=M");
               result.add("M=0");
               result.add("@" + Translator.SP);
               result.add("M=M+1");
            }
            break;
         case _return:
            result.add("@" + Translator.LCL);
            break;
         case call:
            break;
      }
      return result;
   }



}
