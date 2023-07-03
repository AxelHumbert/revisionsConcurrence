package fr.uge.exo2;

import java.util.ArrayList;
import java.util.Objects;

public class UnboundedSafeQueue<V> {
  private final ArrayList<V> queue = new ArrayList<>();
  private final Object lock = new Object();

  public void add(V value) {
    synchronized (lock) {
      Objects.requireNonNull(value);
      queue.add(value);
      lock.notify();
    }
  }

  public V take() throws InterruptedException {
    synchronized (lock) {
      while (queue.isEmpty()) {
        lock.wait();
      }
      return queue.remove(0);
    }
  }

  public static void main(String[] args) throws InterruptedException {

    var nbThread = 3;
    var list = new UnboundedSafeQueue<String>();

    for (var i = 0; i < nbThread; i++) {
      Thread.ofPlatform().start(() -> {
        while (true) {
          try {
            Thread.sleep(2_000);
            list.add(Thread.currentThread().getName());
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
    }

    while (true) {
      System.out.println(list.take());
    }
  }
}
