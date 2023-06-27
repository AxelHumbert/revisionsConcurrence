package fr.uge.exo3;

public class TurtleRace {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("On your mark!");
    Thread.sleep(30_000);
    System.out.println("Go!");
    int[] times = {25_000, 10_000, 20_000, 5_000, 50_000, 60_000};

    for (var i = 0; i < times.length; i++) {
      var tmp = i;
      Thread.ofPlatform().name("Turtle " + tmp).start(() -> {
        try {
          Thread.sleep(times[tmp]);
          System.out.println(Thread.currentThread().getName() + " has finished");
        } catch (InterruptedException e) {
          System.out.println("Issue with the sleep on the Thread " + Thread.currentThread().getName());
          throw new RuntimeException(e);
        }
      });
    }
  }
}
