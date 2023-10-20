package fr.uge.exo1;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookExchange {

  private final ExecutorService executors;
  private final boolean[] booleans;

  public BookExchange(int nbReaders) {
    if (nbReaders < 0) {
      throw new IllegalArgumentException();
    }
    executors = Executors.newFixedThreadPool(nbReaders);
    booleans = new boolean[nbReaders];
  }

  public boolean registerInterruptibly() {
    return false;
  }

  public void addBook(String book) {
    Objects.requireNonNull(book);
  }

  public Optional<String> seeBookIfAvailable() {
    return Optional.empty();
  }

  public String getBook() {
    return null;
  }

  public Set<Thread> readersWaiting() {
    return null;
  }

  public List<String> close() {
    return null;
  }
}
