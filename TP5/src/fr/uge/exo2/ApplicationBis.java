package fr.uge.exo2;

import java.util.List;

public class ApplicationBis {

  public static void main(String[] args) throws InterruptedException {
    var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");

    //var temperatures = new ArrayList<Integer>();
    var temperatures = new QueueBis<Integer>(rooms.size());

    for (String room : rooms) {
      Thread.ofPlatform().start(() -> {
        while (true) {
          int temperature = 0;
          try {
            temperature = Heat4J.retrieveTemperature(room);
            System.out.println("Temperature in room " + room + " : " + temperature);
            temperatures.add(temperature);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
    }

    while (true) {
      //System.out.println(temperatures.stream().mapToInt(Integer::intValue).average().getAsDouble());
      System.out.println(temperatures.average());
    }
  }
}
