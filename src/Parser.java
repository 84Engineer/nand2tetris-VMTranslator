import java.util.ArrayList;
import java.util.List;

public class Parser {

   private static final String CMD_SEPARATOR = " ";

   private List<String> sourceCode;

   public Parser(List<String> sourceCode) {
      this.sourceCode = sourceCode;
   }

   public List<VmCommand> process() {
      List<VmCommand> commands = new ArrayList<>();
      for (String codeLine : sourceCode) {
         if (isEmptyLine(codeLine)) {
            continue;
         }
         codeLine = cleanLine(codeLine);

         VmCommand cmd = new VmCommand();
         cmd.setVmCommand(codeLine);

         String[] cmdParts = codeLine.split(CMD_SEPARATOR);

         switch (cmdParts.length) {
            case 3:
               cmd.setArg1(cmdParts[2]);
            case 2:
               cmd.setArg0(cmdParts[1]);
            case 1:
               String opCode = cmdParts[0];
               opCode = opCode.contains("-") ? opCode.replace("-", "_") : opCode;
               opCode = opCode.equals("goto") || opCode.equals("return") ? "_" + opCode : opCode;
               cmd.setOpCode(VmCommand.OpCode.valueOf(opCode));
               break;
            default:
               throw new IllegalStateException("Unexpected command: " + codeLine);
         }
         commands.add(cmd);
      }
      return commands;
   }

   private boolean isEmptyLine(String codeLine) {
      codeLine = codeLine.trim();
      return codeLine.equals("") || codeLine.startsWith(VMTranslator.COMMENT);
   }

   private String cleanLine(String codeLine) {
      int offset = codeLine.indexOf(VMTranslator.COMMENT);
      if (offset != -1) {
         codeLine = codeLine.substring(0, offset);
      }
      return codeLine.trim();
   }

}
