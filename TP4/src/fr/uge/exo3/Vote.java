package fr.uge.exo3;

import java.util.HashMap;
import java.util.Objects;

public class Vote {

  private final int maxVote;
  private final HashMap<String, Integer> votes;
  private final Object lock = new Object();
  private int nbVoteCurrent = 0;
  private String winner;

  public Vote(int i) {
    if (i < 0) {
      throw new IllegalArgumentException();
    }
    maxVote = i;
    votes = new HashMap<>(i);
  }

  public String vote(String vote) throws InterruptedException {
    synchronized (lock) {
      Objects.requireNonNull(vote);
      if (maxVote == nbVoteCurrent) {
        return winner;
      }
      votes.merge(vote, 1, Integer::sum);
      nbVoteCurrent++;

      if (nbVoteCurrent == maxVote) {
        winner = computeWinner();
        lock.notifyAll();
      }
      while (nbVoteCurrent != maxVote) {
        lock.wait();
      }
      return winner;
    }
  }

  private String computeWinner() {
    var score = -1;
    String winner = null;
    for (var e : votes.entrySet()) {
      var key = e.getKey();
      var value = e.getValue();
      if (value > score || (value == score && key.compareTo(Objects.requireNonNull(winner)) < 0)) {
        winner = key;
        score = value;
      }
    }
    return winner;
  }

  public static void main(String[] args) throws InterruptedException {
    var vote = new Vote(4);
    Thread.ofPlatform().start(() -> {
      try {
        Thread.sleep(2_000);
        System.out.println("The winner is " + vote.vote("un"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    Thread.ofPlatform().start(() -> {
      try {
        Thread.sleep(1_500);
        System.out.println("The winner is " + vote.vote("zero"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    Thread.ofPlatform().start(() -> {
      try {
        Thread.sleep(1_000);
        System.out.println("The winner is " + vote.vote("un"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    System.out.println("The winner is " + vote.vote("zero"));
  }
}
