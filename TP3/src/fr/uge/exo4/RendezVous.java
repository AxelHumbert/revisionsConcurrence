package fr.uge.exo4;

import java.util.Objects;

public class RendezVous<V> {
  private V value;
  private final Object lock = new Object();

  public void set(V value) {
    synchronized (lock) {
      Objects.requireNonNull(value);
      this.value = value;
    }
  }

  public V get() throws InterruptedException {
    while (true) {
      synchronized (lock) {
        if (value == null) {
          Thread.sleep(1);
        } else {
          break;
        }
      }
    }
    synchronized (lock) {
      return value;
    }
  }
}
