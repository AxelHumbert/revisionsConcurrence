package fr.uge.exo2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Queue<T> {

  private final Object lock = new Object();
  private final ArrayList<T> list = new ArrayList<>();
  private final int capacity;

  public Queue(int capacity) {
    synchronized (lock) {
      if (capacity < 0) {
        throw new IllegalArgumentException();
      }
      this.capacity = capacity;
    }
  }

  public void add(T value) {
    synchronized (lock) {
      Objects.requireNonNull(value);
      list.add(value);
      if (list.size() == capacity) {
        lock.notify();
      }
    }
  }

  public Stream<T> stream() throws InterruptedException {
    synchronized (lock) {
      while (list.size() != capacity) {
        lock.wait();
      }
    }
    return list.stream();
  }
}
