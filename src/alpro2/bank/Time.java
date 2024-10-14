package alpro2.bank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

  public String formatDateTime() {
    // Get the current date and time
    LocalDateTime currentDateTime = LocalDateTime.now();

    // If you want to format the date and time, you can use a DateTimeFormatter
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "yyyy/MM/dd HH-mm-ss"
    );

    return currentDateTime.format(formatter);
  }
}
