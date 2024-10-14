package alpro2.bank;

import java.io.*;
import java.util.Arrays;

public class AtmFileController {

  private static final String FILE_ATM = "src/data_atm.txt";
  private static final String FILE_LOG_ATM = "src/data_log.txt";
  private final AtmData[] dataATMArray;
  private AtmLogData[] dataATMLogArray;

  AtmFileController() {
    dataATMArray = readDataATM();
    dataATMLogArray = readDataLogATM();
  }

  public void writeDataATM() {
    StringBuilder data = new StringBuilder();
    for (AtmData atmData : dataATMArray) {
      data
        .append("Nomor Kartu: ")
        .append(atmData.getNomorKartu())
        .append(", PIN: ")
        .append(atmData.getPin())
        .append(", Saldo: ")
        .append(atmData.getSaldo())
        .append("\n");
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_ATM))) {
      writer.write(data.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeDataLogATM() {
    StringBuilder data = new StringBuilder();
    for (AtmLogData logData : dataATMLogArray) {
      data
        .append("Nomor Kartu: ")
        .append(logData.getNomorKartu())
        .append(", Log: ");
      for (int i = 0; i < logData.getLog().length; i++) {
        data
          .append(logData.getLog()[i])
          .append(": ")
          .append(logData.getSum()[i])
          .append(",Date: ")
          .append(logData.getDate()[i])
          .append("],");
      }
      data.append("]\n");
    }
    try (
      BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_LOG_ATM))
    ) {
      writer.write(data.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void createNewLog(String nomorKartu) {
    this.dataATMLogArray =
      Arrays.copyOf(dataATMLogArray, dataATMLogArray.length + 1);
    this.dataATMLogArray[dataATMLogArray.length - 1] =
      new AtmLogData(nomorKartu, 1);
  }

  public AtmLogData[] readDataLogATM() {
    try (BufferedReader br = new BufferedReader(new FileReader(FILE_LOG_ATM))) {
      String line;
      int jumlahData = 0;
      while ((line = br.readLine()) != null) {
        if (!line.isEmpty()) {
          jumlahData++;
        }
      }

      AtmLogData[] dataLogATMArray = new AtmLogData[jumlahData];
      br.close();

      try (BufferedReader newBr = new BufferedReader(
        new FileReader("src/data_log.txt")
      )) {
        int index = 0;

        while ((line = newBr.readLine()) != null) {
          if (line.isEmpty()) {
            continue;
          }

          String[] data = line.split(", ");
          String[] dataLog1 = data[1].split("Log: ");
          String[] dataLog2 = dataLog1[1].split("],");
          String[] dataEachLog = Arrays.copyOf(dataLog2, dataLog2.length - 1);

          String nomorKartu = data[0].split(": ")[1];
          int jumlahLog = dataEachLog.length;

          dataLogATMArray[index] = new AtmLogData(nomorKartu, jumlahLog);
          String[][] dataEachLog2 = new String[dataEachLog.length][0];
          for (int i = 0; i < dataEachLog.length; i++) {
            dataEachLog2[i] = dataEachLog[i].split(",Da");
          }

          String[][][] dataSmall = new String[dataEachLog2.length][dataEachLog2[0].length][0];
          for (int u = 0; u < dataEachLog2.length; u++) {
            for (int i = 0; i < dataEachLog2[u].length; i++) {
              dataSmall[u][i] = dataEachLog2[u][i].split(": ");
            }
          }

          for (int i = 0; i < dataSmall.length; i++) {
            for (int u = 0; u < dataSmall[i].length; u++) {
              for (int p = 0; p < dataSmall[i][u].length; p++) {
                //                            log Check
                if (dataLogATMArray[index].getLog()[i] == null) {
                  if (!dataSmall[i][u][0].equalsIgnoreCase("te")) {
                    dataLogATMArray[index].setLog(dataSmall[i][u][0], i);
                  }
                }
                //                            sum check
                if (dataLogATMArray[index].getSum()[i] == null) {
                  if (!dataSmall[i][u][0].equalsIgnoreCase("te")) {
                    dataLogATMArray[index].setSum(dataSmall[i][u][1], i);
                  }
                }
                //                            date check
                if (dataLogATMArray[index].getDate()[i] == null) {
                  if (dataSmall[i][u][0].equalsIgnoreCase("te")) {
                    dataLogATMArray[index].setDate(dataSmall[i][u][1], i);
                  }
                }
              }
            }
          }
          index++;
        }
      }
      return dataLogATMArray;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AtmData[] readDataATM() {
    try (BufferedReader br = new BufferedReader(new FileReader(FILE_ATM))) {
      String line;
      int jumlahData = 0;

      //            count sum of data for length array
      while ((line = br.readLine()) != null) {
        if (!line.isEmpty()) {
          jumlahData++;
        }
      }

      AtmData[] dataATMArray = new AtmData[jumlahData];
      br.close();

      //            read real data
      BufferedReader newBr = new BufferedReader(new FileReader(FILE_ATM)); // Open a new BufferedReader
      int index = 0;

      while ((line = newBr.readLine()) != null) {
        if (line.isEmpty()) {
          continue;
        }

        String[] data = line.split(", ");

        //                 Extract values from the split data
        String nomorKartu = data[0].split(": ")[1];
        int tempPin = Integer.parseInt(data[1].split(": ")[1]);
        double saldo = Double.parseDouble(data[2].split(": ")[1]);

        //                Insert into AtmData
        int pin = Integer.parseInt(String.valueOf(tempPin));
        dataATMArray[index] = new AtmData(nomorKartu, pin, saldo);
        index++;
      }

      newBr.close();

      return dataATMArray;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AtmData findDataATM(String nomorKartu) {
    for (AtmData atmData : dataATMArray) {
      if (atmData != null && atmData.getNomorKartu().equals(nomorKartu)) {
        return atmData;
      }
    }
    return null;
  }

  public AtmLogData findDataLogATM(String nomorKartu) {
    for (AtmLogData atmLogData : dataATMLogArray) {
      if (atmLogData != null && atmLogData.getNomorKartu().equals(nomorKartu)) {
        return atmLogData;
      }
    }
    return null;
  }

  public AtmData[] getDataATMArray() {
    return this.dataATMArray;
  }
}
