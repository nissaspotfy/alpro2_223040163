package alpro2.bank;

public class AtmView {

  public void menu() {
    System.out.println("===== MENU ATM =====");
    System.out.println("1. Cek Saldo");
    System.out.println("2. Transfer");
    System.out.println("3. Tarik Tunai");
    System.out.println("4. Setor Tunai");
    System.out.println("5. Pembayaran");
    System.out.println("6. Cek History Bank");
    System.out.println("7. Keluar");
    System.out.print("Pilih menu (1-7): ");
  }

  public void menuPembayaran() {
    System.out.println("===== MENU PEMBAYARAN =====");
    System.out.println("1. Topup Pulsa");
    System.out.println("2. Pembayaran Listrik");
    System.out.println("3. Pembayaran PDAM");
    System.out.println("4. Kembali");
    System.out.print("Pilih menu (1-4): ");
  }

  public void menuTopUp() {
    System.out.println("===== MENU TOPUP PULSA =====");
    System.out.println("1. Rp. 5.000");
    System.out.println("2. Rp. 10.000");
    System.out.println("3. Rp. 25.000");
    System.out.println("4. Rp. 50.000");
    System.out.println("5. Isi Sendiri");
    System.out.println("6. Cancel");
    System.out.print("Pilih menu (1-6): ");
  }

  public void menuPembayaranListrik() {
    System.out.println("===== MENU PEMBAYARAN LISTRIK =====");
    System.out.println("1. Rp. 50.000");
    System.out.println("2. Rp. 100.000");
    System.out.println("3. Rp. 250.000");
    System.out.println("4. Rp. 500.000");
    System.out.println("5. Cancel");
    System.out.print("Pilih menu (1-5): ");
  }

  public void menuPembayaranPDAM() {
    System.out.println("===== MENU PEMBAYARAN PDAM =====");
    System.out.println("1. Rp. 50.000");
    System.out.println("2. Rp. 100.000");
    System.out.println("3. Rp. 250.000");
    System.out.println("4. Rp. 500.000");
    System.out.println("5. Cancel");
    System.out.print("Pilih menu (1-5): ");
  }
}
