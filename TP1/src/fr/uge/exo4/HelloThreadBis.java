package fr.uge.exo4;

public class HelloThreadBis {

  private static final int NB_THREAD = 4;
  private static final int NB_MAX = 5000;

  public static void println(String s){
    for(var i = 0; i < s.length(); i++){
      System.out.print(s.charAt(i));
    }
    System.out.print("\n");
  }

  public static void main(String[] args) {
    for (var i = 0; i < NB_THREAD; i++) {
      var tmp = i;
      Thread.ofPlatform().start(() -> {
        for (var j = 0; j < NB_MAX; j++) {
          HelloThreadBis.println("hello " + tmp + " " + j);
        }
      });
    }
  }
}
