package alpro2.bank;

public class AtmData {

  private String nomorKartu;
  private int pin;
  private double saldo;

  public AtmData(String nomorKartu, int pin, double saldo) {
    this.nomorKartu = nomorKartu;
    this.pin = pin;
    this.saldo = saldo;
  }

  public String getNomorKartu() {
    return this.nomorKartu;
  }

  public int getPin() {
    return this.pin;
  }

  public double getSaldo() {
    return this.saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }
}
