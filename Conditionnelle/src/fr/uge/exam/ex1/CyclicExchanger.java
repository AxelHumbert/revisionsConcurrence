package fr.uge.exam.ex1;

import java.util.Objects;

public class CyclicExchanger<T> {

  private final int nbParticipants;
  private final Object[] tab;
  private final Object lock = new Object();
  private int nbAsk = 0;

  public CyclicExchanger (int nbParticipants) {
    if (nbParticipants < 1) {
      throw new IllegalArgumentException();
    }
    this.nbParticipants = nbParticipants;
    tab = new Object[nbParticipants];
  }

  public T exchange(T value) throws InterruptedException {
    Objects.requireNonNull(value);
    synchronized (lock) {
      var index = 0;
      var idThread = Integer.parseInt(Thread.currentThread().getName());
      tab[idThread] = value;
      nbAsk++;
      while (nbAsk < nbParticipants) {
        lock.wait();
      }
      lock.notifyAll();
      if (idThread != nbParticipants - 1) {
        index = idThread + 1;
      }
      return (T) tab[index];
    }
  }

  public static void main(String[] args) {
    var nbParticipants = 3;
    var cyclicExchanger = new CyclicExchanger<Integer>(nbParticipants);
    for (var i = 0; i < nbParticipants; i++) {
      var tmp = i;
      Thread.ofPlatform().name(String.valueOf(tmp)).start(() -> {
        try {
          System.out.println("Ich bin " + Thread.currentThread().getName() + " und Ich habe " + cyclicExchanger.exchange(tmp));
        } catch (InterruptedException e) {
          return;
        }
      });
    }
  }
}
