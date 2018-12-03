import java.util.stream.*;

import edu.polytechnique.mjava.parser.*;
import edu.polytechnique.mjava.toplevel.*;
import edu.polytechnique.mjava.toplevel.MJavaTop.*;
import edu.polytechnique.mjava.typing.*;
import edu.polytechnique.mjava.typing.exn.*;
import edu.polytechnique.xvm.asm.opcodes.*;

public class TestExprCodeGen {
  // Modify this expression at will
  public final static String DEFAULT =
    "< x = 2; y = 7; z = 5; >" + // Local variables declaration
    "x + 2 * y - z";
  
  public static void main(String[] args)
      throws MJavaParseError, MJavaTypingError
  {
    // Get input expression
    String input = args.length == 0 ? DEFAULT : String.join(" ", args);
    
    // Parse & type input expression
    ExprWithCtxt ce = MJavaTop.parseAndTypeExpr(input);

    // Create code generator
    CodeGen cg = new CodeGen();

    // Push local variables initialization
    // (in order of declaration from left-to-right)
    for (EVarInit vinit : ce.getCtxt())
      cg.pushInstruction(new PUSH(vinit.getValue()));

    // Create expression code generator using local variables `vars'
    ExprCodeGen ecg = new ExprCodeGen(cg,
        IntStream
          .range(0, ce.getCtxt().size())
          .mapToObj(i -> new Integer(i))
          .collect(Collectors.toMap(
                x -> ce.getVInit(x).getName(),
                x -> x)));

    // Generate code for expression `e'
    ce.getExpr().accept(ecg);

    // Display generated XVM code
    cg.pushInstruction(new STOP());
    System.out.print(cg.toString());
  }
}
