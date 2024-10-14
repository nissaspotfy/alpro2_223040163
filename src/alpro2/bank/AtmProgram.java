package alpro2.bank;

import java.util.Scanner;

public class AtmProgram {

  private final Scanner scanner = new Scanner(System.in);
  private final AtmView view = new AtmView();
  private final AtmFileController fileController = new AtmFileController();
  private final Time time = new Time();

  public AtmProgram() {
    if (fileController.getDataATMArray() != null) {
      AtmData tempDataAtm = login();
      memilihMenuUtama(tempDataAtm);
    } else {
      System.out.println(
        "Gagal membaca data ATM dari file. Program akan keluar."
      );
    }
  }

  private void memilihMenuUtama(AtmData tempDataAtm) {
    while (true) {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      view.menu();
      int pilihan = scanner.nextInt();

      switch (pilihan) {
        case 1 -> cekSaldo(tempDataAtm);
        case 2 -> transfer(tempDataAtm);
        case 3 -> tarikTunai(tempDataAtm);
        case 4 -> setorTunai(tempDataAtm);
        case 5 -> memilihMenuPembayaran(tempDataAtm);
        case 6 -> historyAtm(tempDataAtm);
        case 7 -> {
          fileController.writeDataATM();
          fileController.writeDataLogATM();
          System.out.println(
            "Terima kasih telah menggunakan layanan ATM. Sampai jumpa!"
          );
          System.exit(0);
        }
        default -> System.out.println(
          "Pilihan tidak valid. Silakan pilih kembali."
        );
      }
    }
  }

  private void memilihMenuPembayaran(AtmData tempDataAtm) {
    boolean filled;
    do {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      view.menuPembayaran();
      int pilihan = scanner.nextInt();

      filled = false;
      switch (pilihan) {
        case (1) -> topupPulsa(tempDataAtm);
        case (2) -> pembayaranListrik(tempDataAtm);
        case (3) -> pembayaranPDAM(tempDataAtm);
        case (4) -> {
          filled = true;
        }
        default -> System.out.println("Pilihan Tidak Valid!");
      }
    } while (!filled);
  }

  private void cekSaldo(AtmData dataATM) {
    System.out.println("Saldo saat ini: Rp." + dataATM.getSaldo());
  }

  private void transfer(AtmData dataATM) {
    AtmData targetNoKartu;
    scanner.nextLine();
    do {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.print("Masukan nomor yang dituju: ");
      String inputTargetNoKartu = scanner.nextLine();
      targetNoKartu = fileController.findDataATM(inputTargetNoKartu);
      if (targetNoKartu == null) {
        System.out.println("Nomor yang dituju tidak terdaftar");
      }
    } while (targetNoKartu == null);
    double transfer;
    do {
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.print("Masukan nominal yang akan di Transfer: ");
      transfer = scanner.nextDouble();
      if (transfer > dataATM.getSaldo()) {
        System.out.println("Nominal yang anda masukan tidak cukup");
      } else {
        dataATM.setSaldo(dataATM.getSaldo() - transfer);
        targetNoKartu.setSaldo(targetNoKartu.getSaldo() + transfer);
        AtmLogData log = fileController.findDataLogATM(dataATM.getNomorKartu());
        if (log == null) {
          fileController.createNewLog(dataATM.getNomorKartu());
          log = fileController.findDataLogATM(dataATM.getNomorKartu());
          log.setLog("[Transfer", 0);
          log.setSum(String.valueOf(transfer), 0);
          log.setDate(time.formatDateTime(), 0);
        } else {
          log.setNewLog("[Transfer", transfer, time.formatDateTime());
        }
        System.out.println(
          "Rp. " +
          transfer +
          " berhasil di transfer \n" +
          "Sisa saldo anda sebesar Rp. " +
          dataATM.getSaldo()
        );
      }
    } while (transfer > dataATM.getSaldo());
  }

  private void tarikTunai(AtmData dataATM) {
    System.out.print("Masukan nominal untuk menarik tunai: ");
    double nominal = scanner.nextDouble();
    double tempSaldo = dataATM.getSaldo();
    if (nominal > tempSaldo) {
      System.out.println("Maaf Saldo anda tidak mencukupi!");
    } else {
      dataATM.setSaldo(tempSaldo - nominal);
      AtmLogData log = fileController.findDataLogATM(dataATM.getNomorKartu());
      if (log == null) {
        fileController.createNewLog(dataATM.getNomorKartu());
        log = fileController.findDataLogATM(dataATM.getNomorKartu());
        log.setLog("[Tarik Tunai", 0);
        log.setSum(String.valueOf(nominal), 0);
        log.setDate(time.formatDateTime(), 0);
      } else {
        log.setNewLog("[Transfer", nominal, time.formatDateTime());
      }
      System.out.println("Sisa saldo anda Rp. " + dataATM.getSaldo());
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void setorTunai(AtmData dataATM) {
    boolean loop = true;
    while (loop) {
      System.out.println("Hanya Menerima Saldo Kelipatan 50.000 dan 100.000");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.print("Silahkan masukan uang yang akan di setorkan: ");
      double setor = scanner.nextDouble();
      if (setor % 50000 == 0) {
        dataATM.setSaldo(dataATM.getSaldo() + setor);
        AtmLogData log = fileController.findDataLogATM(dataATM.getNomorKartu());
        if (log == null) {
          fileController.createNewLog(dataATM.getNomorKartu());
          log = fileController.findDataLogATM(dataATM.getNomorKartu());
          log.setLog("[Setor Tunai", 0);
          log.setSum(String.valueOf(setor), 0);
          log.setDate(time.formatDateTime(), 0);
        } else {
          log.setNewLog("[Setor Tunai", setor, time.formatDateTime());
        }
        loop = false;
      }
    }
  }

  private void historyAtm(AtmData noKartu) {
    AtmLogData log = fileController.findDataLogATM(noKartu.getNomorKartu());
    if (log == null) {
      System.out.println("Anda Belum Melakukan Transaksi Apapun");
    } else {
      log.printLog();
    }
  }

  private void topupPulsa(AtmData dataATM) {
    System.out.print("Isikan Nomor Tujuan Top Up");
    scanner.next();
    int number;
    double saldoIni = dataATM.getSaldo();
    boolean filled = false;
    double jumlah = 0;
    do {
      view.menuTopUp();
      number = scanner.nextInt();
      switch (number) {
        case (1) -> {
          if (saldoIni > 5500) {
            jumlah = 5500;
            dataATM.setSaldo(saldoIni - jumlah);
            filled = true;
          }
        }
        case (2) -> {
          if (saldoIni > 11000) {
            jumlah = 11000;
            dataATM.setSaldo(saldoIni - jumlah);
            filled = true;
          }
        }
        case (3) -> {
          if (saldoIni > 27500) {
            jumlah = 27500;
            dataATM.setSaldo(saldoIni - jumlah);
            filled = true;
          }
        }
        case (4) -> {
          if (saldoIni > 55000) {
            jumlah = 55000;
            dataATM.setSaldo(saldoIni - jumlah);
            filled = true;
          }
        }
        case (5) -> {
          System.out.print("Jumlah yang ingin di top up Rp. ");
          jumlah = scanner.nextDouble();
          if (saldoIni > jumlah) {
            dataATM.setSaldo(saldoIni - jumlah);
            filled = true;
          }
        }
        case (6) -> {
          filled = true;
        }
        default -> System.out.println("Pilihan Tidak Valid");
      }
      if (number >= 1 && number <= 5 && !filled) {
        System.out.println("Saldo anda tidak mencukupi");
      }
    } while (!filled);
    if (number != 6) {
      AtmLogData log = fileController.findDataLogATM(dataATM.getNomorKartu());
      if (log == null) {
        fileController.createNewLog(dataATM.getNomorKartu());
        log = fileController.findDataLogATM(dataATM.getNomorKartu());
        log.setLog("[Topup Pulsa", 0);
        log.setSum(String.valueOf(jumlah), 0);
        log.setDate(time.formatDateTime(), 0);
      } else {
        log.setNewLog("[Topup Pulsa", jumlah, time.formatDateTime());
      }
      System.out.println("Top Up Berhasil");
    }
  }

  private void pembayaranListrik(AtmData dataATM) {
    double saldo = dataATM.getSaldo();
    view.menuPembayaranListrik();
    int number = scanner.nextInt();
    boolean filled = false;
    double jumlah = 0;
    do {
      switch (number) {
        case (1) -> {
          if (saldo > 55000) {
            jumlah = 55000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (2) -> {
          if (saldo > 110000) {
            jumlah = 110000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (3) -> {
          if (saldo > 275000) {
            jumlah = 275000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (4) -> {
          if (saldo > 550000) {
            jumlah = 550000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (5) -> {
          filled = true;
        }
        default -> System.out.println("Pilihan tidak valid");
      }
    } while (!filled);
    if (number != 5) {
      AtmLogData log = fileController.findDataLogATM(dataATM.getNomorKartu());
      if (log == null) {
        fileController.createNewLog(dataATM.getNomorKartu());
        log = fileController.findDataLogATM(dataATM.getNomorKartu());
        log.setLog("[Pembayaran Listrik", 0);
        log.setSum(String.valueOf(jumlah), 0);
        log.setDate(time.formatDateTime(), 0);
      } else {
        log.setNewLog("[Pembayaran Listrik", jumlah, time.formatDateTime());
      }
      System.out.println(
        "Anda berhasil Membayar listrik! \n" +
        "Sisa Saldo anda sebesar: Rp." +
        dataATM.getSaldo()
      );
    }
  }

  private void pembayaranPDAM(AtmData dataATM) {
    double saldo = dataATM.getSaldo();
    view.menuPembayaranPDAM();
    int number = scanner.nextInt();
    boolean filled = false;
    double jumlah = 0;
    do {
      switch (number) {
        case (1) -> {
          if (saldo > 55000) {
            jumlah = 55000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (2) -> {
          if (saldo > 110000) {
            jumlah = 110000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (3) -> {
          if (saldo > 275000) {
            jumlah = 275000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (4) -> {
          if (saldo > 550000) {
            jumlah = 550000;
            dataATM.setSaldo(saldo - jumlah);
            filled = true;
          }
        }
        case (5) -> {
          filled = true;
        }
        default -> System.out.println("Pilihan tidak valid");
      }
    } while (!filled);
    if (number != 5) {
      AtmLogData log = fileController.findDataLogATM(dataATM.getNomorKartu());
      if (log == null) {
        fileController.createNewLog(dataATM.getNomorKartu());
        log = fileController.findDataLogATM(dataATM.getNomorKartu());
        log.setLog("[PDAM", 0);
        log.setSum(String.valueOf(jumlah), 0);
        log.setDate(time.formatDateTime(), 0);
      } else {
        log.setNewLog("[PDAM", jumlah, time.formatDateTime());
      }
      System.out.println(
        "Anda berhasil Membayar PDAM! \n" +
        "Sisa Saldo anda sebesar: Rp." +
        dataATM.getSaldo()
      );
    }
  }

  private AtmData login() {
    AtmData tempDataAtm;
    boolean found = false;
    do {
      System.out.print("Masukan nomor kartu ATM: ");
      String tempNoKartu = scanner.nextLine();
      tempDataAtm = fileController.findDataATM(tempNoKartu);
      if (tempDataAtm != null) {
        found = true;
      } else {
        System.out.println("Nomor kartu yang dimasukan tidak ada!");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } while (!found);
    boolean found2 = false;
    do {
      System.out.print("Masukan PIN Anda: ");
      int tempPin = scanner.nextInt();
      if (tempDataAtm.getPin() == tempPin) {
        found2 = true;
      } else {
        System.out.println("Pin yang anda masukan salah!");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } while (!found2);
    return tempDataAtm;
  }
}
