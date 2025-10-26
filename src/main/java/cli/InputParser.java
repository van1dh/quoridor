
package cli; import java.util.*;
public final class InputParser {
  private InputParser(){}
  public static Optional<Integer> intOpt(String s){
    try{ return Optional.of(Integer.parseInt(s)); }catch(Exception e){ return Optional.empty(); }
  }
  public static String[] split(String line){
    return line.trim().split("\s+");
  }
}
