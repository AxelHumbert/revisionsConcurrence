package fr.uge.exo2;

public class HonorBoard {
  private String firstName;
  private String lastName;
  private final Object lock = new Object();
  
  public void set(String firstName, String lastName) {
    synchronized (lock) {
      this.firstName = firstName;
      this.lastName = lastName;
    }
  }
  
  @Override
  public String toString() {

    synchronized (lock) {
      return firstName + ' ' + lastName;
    }
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }
  
  public static void main(String[] args) {
    var board = new HonorBoard();
    Thread.ofPlatform().start(() -> {
      for(;;) {
        board.set("Mickey", "Mouse");
      }
    });
    
    Thread.ofPlatform().start(() -> {
      for(;;) {
        board.set("Donald", "Duck");
      }
    });
    
    Thread.ofPlatform().start(() -> {
      for(;;) {
        System.out.println(board);
        //System.out.println(board.firstName() + ' ' + board.lastName()); // -> Non fonctionnel comme ça
      }
    });
  }
}