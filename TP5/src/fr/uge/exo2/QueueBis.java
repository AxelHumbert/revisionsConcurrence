package fr.uge.exo2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class QueueBis<T> {
  private final Object lock = new Object();
  private final ArrayList<T> list;
  private final int capacity;
  private boolean averaged = true;
  private int compteur = 0; //

  public QueueBis(int size) {
    synchronized (lock) {
      if (size < 0) {
        throw new IllegalArgumentException();
      }
      capacity = size;
      list = new ArrayList<>(capacity);
    }
  }

  public void add(T value) throws InterruptedException {
    synchronized (lock) {
      Objects.requireNonNull(value);
      while (!averaged) {
        lock.wait();
      }
      list.add(value);
      while (list.size() < capacity) {
        lock.wait();
      }
      compteur++; //
      averaged = false;
      lock.notifyAll();
    }
  }

  public Stream<T> stream() throws InterruptedException {
    synchronized (lock) {
      while (list.size() < capacity || compteur != capacity) {
        lock.wait();
      }
      var result = list.stream();
      list.clear();
      averaged = true;
      compteur = 0;
      lock.notify();
      return result;
    }
  }

  public double average() throws InterruptedException {
    synchronized (lock) {
      while (list.size() < capacity || compteur != capacity) {
        lock.wait();
      }
      var result = list.stream().mapToInt(e -> Integer.parseInt(e.toString())).average().getAsDouble();
      list.clear();
      averaged = true;
      compteur = 0;
      lock.notify();
      return result;
    }
  }
}
