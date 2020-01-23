public class VmCommand {

   enum OpCode {
      push, pop, add, sub, neg, eq, gt, lt, and, or, not, label, _goto, if_goto;
   }

   private String vmCommand;
   private OpCode opCode;
   private String arg0;
   private String arg1;

   public VmCommand() {
   }

   public VmCommand(String vmCommand, OpCode opCode, String arg0, String arg1) {
      this.vmCommand = vmCommand;
      this.opCode = opCode;
      this.arg0 = arg0;
      this.arg1 = arg1;
   }

   public String getVmCommand() {
      return vmCommand;
   }

   public void setVmCommand(String vmCommand) {
      this.vmCommand = vmCommand;
   }

   public OpCode getOpCode() {
      return opCode;
   }

   public void setOpCode(OpCode opCode) {
      this.opCode = opCode;
   }

   public String getArg0() {
      return arg0;
   }

   public void setArg0(String arg0) {
      this.arg0 = arg0;
   }

   public String getArg1() {
      return arg1;
   }

   public void setArg1(String arg1) {
      this.arg1 = arg1;
   }
}
