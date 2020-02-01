import java.util.ArrayList;
import java.util.List;

public class FunctionTranslate {

   private final static String TEMP_REG_0 = "R13";
   private final static String TEMP_REG_1 = "R14";
   private final static String TEMP_REG_2 = "R15";

   private static long retCount = 0;

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
            result.add("A=D");
            result.add("D=M");
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
            result.add("@" + TEMP_REG_1);
            result.add("A=M");
            result.add("0;JMP");
            break;
         case call:
            // pushing return address
            result.add("@" + fileName + ".ret" + retCount);
            result.add("D=A");
            result.add("@" + Translator.SP);
            result.add("A=M");
            result.add("M=D"); //???
            result.add("@" + Translator.SP);
            result.add("AM=M+1");
            // pushing memory segments
            result.addAll(pushMemorySegment(Translator.LCL));
            result.addAll(pushMemorySegment(Translator.ARG));
            result.addAll(pushMemorySegment(Translator.THIS));
            result.addAll(pushMemorySegment(Translator.THAT));
            // ARG = SP - 5 - nArgs
            result.add("@" + Translator.SP);
            result.add("D=M");
            result.add("@5");
            result.add("D=D-A");
            result.add("@" + vmCommand.getArg1());
            result.add("D=D-A");
            result.add("@" + Translator.ARG);
            result.add("M=D");
            // LCL = SP
            result.add("@" + Translator.SP);
            result.add("D=M");
            result.add("@" + Translator.LCL);
            result.add("M=D");
            // goto function
            result.add("@" + fileName + "." + vmCommand.getArg0());
            result.add("0;JMP");
            // declare return address
            result.add("(" + fileName + ".ret" + retCount++ + ")");
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

   private List<String> pushMemorySegment(String memSegment) {
      List<String> result = new ArrayList<>();
      result.add("@" + memSegment);
      result.add("D=M");
      result.add("@" + Translator.SP);
      result.add("A=M");
      result.add("M=D");
      result.add("@" + Translator.SP);
      result.add("AM=M+1");
      return result;
   }


}
