import java.util.*;

import edu.polytechnique.mjava.ast.*;
import edu.polytechnique.mjava.ast.expr.*;
import edu.polytechnique.mjava.ast.visitor.*;
import edu.polytechnique.xvm.asm.interfaces.*;
import edu.polytechnique.xvm.asm.opcodes.*;

public class ExprCodeGen extends DefaultExprVisitor {
  protected final CodeGen              codegen;
  protected final Map<String, Integer> offsets;

  public void push(AsmInstruction asm) {
    this.codegen.pushInstruction(asm);
  }

  public void push(String label) {
    this.codegen.pushLabel(label);
  }

  @Override
  public void visit(EBool ebool) {
    final boolean value = ebool.getValue(); // Literal value
    if (value) {
    	this.codegen.pushInstruction(new PUSH(1));
    }
    else {
    	this.codegen.pushInstruction(new PUSH(0));
    }
  }

  @Override
  public void visit(EInt eint) {
    final int value = eint.getValue(); // Literal value
    this.codegen.pushInstruction(new PUSH(value));
  }

  @Override
  public void visit(EVar evar) {
    final String name = evar.getName(); // Variable name
    int i = offsets.get(name);
    this.codegen.pushInstruction(new RFR(i));
  }

  @Override
  public void visit(EBinOp ebin) {
    final Expr eleft = ebin.getLeft(); // Left operand
    final Expr eright = ebin.getRight(); // Right operand
    final EBinOp.Op op = ebin.getOp(); // Operator
    switch (op) {
    case ADD:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new ADD());
    	break;
    
    case AND:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new MULT());
    	break;
    	
    case DIV:
    	this.visit(eright);
    	this.visit(eleft);
    	this.push(new DIV());
    	break;
    	
    case EQ:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new EQ());
    	break;
    
    case LT:
    	this.visit(eright);
    	this.visit(eleft);
    	this.push(new LT());
    	break;
    	
    case GT:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new LT());
    	break;
    
    case LE:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new LT());
    	this.push(new NOT());
    	break;
    
    case GE:
    	this.visit(eright);
    	this.visit(eleft);
    	this.push(new LT());
    	this.push(new NOT());
    	break;
    
    case MUL:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new MULT());
    	break;
    	
    case NEQ:
    	this.visit(eleft);
    	this.visit(eright);
    	this.push(new EQ());
    	this.push(new NOT());
    	break;
    	
    case OR:
    	this.visit(eleft);
    	this.push(new NOT());
    	this.visit(eright);
    	this.push(new NOT());
    	this.push(new MULT());
    	this.push(new NOT());
    	break;
    	
    case SUB:
    	this.visit(eright);
    	this.visit(eleft);
    	this.push(new SUB());
    	break;
    }
  }

  @Override
  public void visit(EUniOp euni) {
    final Expr esub = euni.getExpr(); // Operand
    final EUniOp.Op op = euni.getOp(); // Operator

    switch (op) {
    case NOT:
      this.visit(esub);
      this.push(new NOT());
      break;

    case NEG:
      this.push(new PUSH(0));
      this.visit(esub);
      this.push(new SUB());
      break;
    }
  }

  public ExprCodeGen(CodeGen codegen, Map<String, Integer> offsets) {
    this.codegen = codegen;
    this.offsets = offsets;
  }
}