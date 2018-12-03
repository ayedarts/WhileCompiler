import edu.polytechnique.xvm.asm.opcodes.*;

public class TestCodeGen {
  public static void main(String[] args) {
    CodeGen cg = new CodeGen();

    cg.pushInstruction(new PUSH(0));
    cg.pushLabel("loop");
    cg.pushInstruction(new PUSH(1));
    cg.pushInstruction(new ADD());
    cg.pushInstruction(new GTO("loop"));

    System.out.print(cg);
  }
}
