package fr.uge.exo1;

import java.util.ArrayList;

public class ThreadSafeList {

  private final Object lock = new Object();
  private final ArrayList<Integer> list;

  public ThreadSafeList(int size) {
    if (size < 0) {
      throw new IllegalArgumentException();
    }
    list = new ArrayList<>(size);
  }

  public void add(Integer i) {
    synchronized (lock) {
      list.add(i);
    }
  }

  public int size() {
    synchronized (lock) {
      return list.size();
    }
  }

  @Override
  public String toString() {
    synchronized (lock) {
      return list.toString();
    }
  }
}
