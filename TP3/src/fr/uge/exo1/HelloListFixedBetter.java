package fr.uge.exo1;

public class HelloListFixedBetter {

  private static final int NB_THREAD = 4;
  private static final int NB_MAX = 5000;

  public static void main(String[] args) throws InterruptedException {
    var threads = new Thread[NB_THREAD];
    var threadSafeList = new ThreadSafeList(5_000 * NB_THREAD);

    for (var i = 0; i < NB_THREAD; i++) {
      var tmp = i;
      threads[tmp] = Thread.ofPlatform().start(() -> {
        for (var j = 0; j < NB_MAX; j++) {
          threadSafeList.add(j);
        }
      });
    }

    for (var i = 0; i < NB_THREAD; i++) {
      threads[i].join();
    }

    //System.out.println("Taille de la liste : " + threadSafeList.size());
    System.out.println(threadSafeList);
    System.out.println("Le programme est fini");
  }
}
