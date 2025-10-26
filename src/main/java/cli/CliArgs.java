
package cli; import java.util.*;
public record CliArgs(String game,int players,int width,int height,int size,int wallsPerPlayer,boolean random){
  public static CliArgs from(String[] a){
    Map<String,String> m=new HashMap<>(); for(int i=0;i<a.length;i+=2){ if(i+1<a.length && a[i].startsWith("--")) m.put(a[i],a[i+1]); }
    String g=m.getOrDefault("--game","quoridor");
    int p=parseInt(m.get("--players"),2);
    int w=parseInt(m.get("--width"),3), h=parseInt(m.get("--height"),3);
    int sz=parseInt(m.get("--size"),9); int wp=parseInt(m.get("--wallsPerPlayer"),10);
    boolean rand=m.containsKey("--random");
    return new CliArgs(g,p,w,h,sz,wp,rand);
  }
  private static int parseInt(String s,int d){ try{ return s==null?d:Integer.parseInt(s);}catch(Exception e){ return d; } }
}
