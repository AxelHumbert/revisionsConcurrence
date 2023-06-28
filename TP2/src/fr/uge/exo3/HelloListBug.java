package fr.uge.exo3;

import java.util.ArrayList;

public class HelloListBug {

  private static final int NB_THREAD = 4;
  private static final int NB_MAX = 5000;

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[NB_THREAD];
    var list = new ArrayList<Integer>(5000 * NB_THREAD);
    for (var i = 0; i < NB_THREAD; i++) {
      var tmp = i;
      threads[tmp] = Thread.ofPlatform().start(() -> {
        for (var j = 0; j < NB_MAX; j++) {
          //System.out.println("hello " + tmp + " " + j);
          list.add(tmp);
        }
      });
    }

    for (var i = 0; i < NB_THREAD; i++) {
      threads[i].join();
    }

    System.out.println("Taille de la liste : " + list.size());
    System.out.println("le programme est fini");
  }
}
