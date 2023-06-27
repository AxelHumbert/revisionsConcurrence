package fr.uge.exo1;

public class HelloThread {

  private static final int NB_THREAD = 4;
  private static final int NB_MAX = 5000;

  public static void main(String[] args) {
    for (var i = 0; i < NB_THREAD; i++) {
      var tmp = i;
      Thread.ofPlatform().start(() -> {
        for (var j = 0; j < NB_MAX; j++) {
          System.out.println("hello " + tmp + " " + j);
        }
      });
    }
  }
}
