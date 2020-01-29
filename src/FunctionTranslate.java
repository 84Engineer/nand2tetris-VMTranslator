import java.util.ArrayList;
import java.util.List;

public class FunctionTranslate {

   private final static String TEMP_REG_0 = "R13";
   private final static String TEMP_REG_1 = "R14";
   private final static String TEMP_REG_2 = "R15";

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
            // Saving end frame
            result.add("@" + Translator.LCL);
            result.add("D=M");
            result.add("@" + TEMP_REG_0);
            result.add("M=D");
            // Saving return address
            result.add("@5");
            result.add("D=D-A");
            result.add("@" + TEMP_REG_1);
            result.add("M=D");
            // Passing return value
            result.add("@" + Translator.SP);
            result.add("A=M-1");
            result.add("D=M");
            result.add("@" + TEMP_REG_2);
            result.add("M=D");
            result.add("@" + Translator.ARG);
            result.add("A=M");
            result.add("M=D");
            // Setting SP
            result.add("@" + Translator.ARG);
            result.add("D=M+1");
            result.add("@" + Translator.SP);
            result.add("M=D");
            // Restoring memory segments
            result.addAll(restoreMemorySegment(Translator.THAT));
            result.addAll(restoreMemorySegment(Translator.THIS));
            result.addAll(restoreMemorySegment(Translator.ARG));
            result.addAll(restoreMemorySegment(Translator.LCL));
            // Goto return address
//            result.add("@" + TEMP_REG_1);
//            result.add("0;JMP");
            break;
         case call:
            break;
      }
      return result;
   }

   private List<String> restoreMemorySegment(String memSegment) {
      List<String> result = new ArrayList<>();
      result.add("@" + TEMP_REG_0);
      result.add("AM=M-1");
      result.add("D=M");
      result.add("@" + memSegment);
      result.add("M=D");
      return result;
   }



}
