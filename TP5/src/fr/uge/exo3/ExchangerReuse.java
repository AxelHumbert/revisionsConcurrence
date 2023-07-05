package fr.uge.exo3;

public class ExchangerReuse<T> {

  private enum State {
    NOTHING, FIRST_VALUE, SECOND_VALUE;
  }

  private final Object lock = new Object();
  private T valueToExchange1;
  private T valueToExchange2;
  private State state = State.NOTHING;

  public T exchange(T val) throws InterruptedException {
    synchronized (lock) {
      if (state == State.NOTHING) {
        valueToExchange1 = val;
        state = State.FIRST_VALUE;
        lock.wait();
        while (state != State.SECOND_VALUE) {
          lock.wait();
        }
        state = State.NOTHING;
        return valueToExchange2;
      } else {
        valueToExchange2 = val;
        state = State.SECOND_VALUE;
        lock.notify();
        return valueToExchange1;
      }
    }
  }
}
