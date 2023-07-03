package fr.uge.exo2;

import java.util.ArrayList;
import java.util.Objects;

public class BoundedSafeQueue<V> {
  private final ArrayList<V> queue;
  private final Object lock = new Object();
  private final int capacity;

  public BoundedSafeQueue(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException();
    }
    queue = new ArrayList<>(capacity);
    this.capacity = capacity;
  }

  public void put(V value) throws InterruptedException {
    synchronized (lock) {
      Objects.requireNonNull(value);
      while (queue.size() == capacity) {
        lock.wait();
      }
      queue.add(value);
      lock.notify();
    }
  }

  public V take() throws InterruptedException {
    synchronized (lock) {
      while (queue.isEmpty()) {
        lock.wait();
      }
      lock.notify();
      return queue.remove(0);
    }
  }

  public static void main(String[] args) throws InterruptedException {

    var nbThread = 100;
    var capacity = 100;
    var list = new BoundedSafeQueue<String>(capacity);

    for (var i = 0; i < nbThread; i++) {
      Thread.ofPlatform().start(() -> {
        while (true) {
          try {
            Thread.sleep(2_000);
            list.put(Thread.currentThread().getName());
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
