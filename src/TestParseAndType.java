import java.io.*;
import java.util.*;

import edu.polytechnique.mjava.ast.topdecl.*;
import edu.polytechnique.mjava.parser.*;
import edu.polytechnique.mjava.toplevel.*;
import edu.polytechnique.mjava.typing.exn.*;

public final class TestParseAndType {
  public final static String PATH = "example.wil";

  public static void main(String[] args) throws IOException {
    try {
      MJavaTop.parseAndTypeProgramFromFile(PATH);
    } catch (MJavaParseError | MJavaTypingError e) {
      System.err.println(e.getMessage());
    }
  }
}