package fr.uge.exo1;

import java.util.Objects;

public class RendezVous<V> {
  private V value;
  private final Object lock = new Object();
  private boolean setted = false;

  public void set(V value) {
    synchronized (lock) {
      Objects.requireNonNull(value);
      this.value = value;
      this.setted = true;
      lock.notify();
    }
  }

  public V get() throws InterruptedException {
    synchronized (lock) {
      while (!setted) {
        lock.wait();
      }
    }
    synchronized (lock) {
      return value;
    }
  }

  public static void main(String[] args) throws InterruptedException {
    var rdv = new RendezVous<String>();
    Thread.ofPlatform().start(() -> {
      try {
        Thread.sleep(20_000);
        rdv.set("Message");
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    System.out.println(rdv.get());
  }
}
