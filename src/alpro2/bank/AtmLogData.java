package alpro2.bank;

public class AtmLogData {

  private String nomorKartu;
  private String[] log, sum, date;

  AtmLogData(String nomorKartu, int jumlahLog) {
    this.nomorKartu = nomorKartu;
    this.log = new String[jumlahLog];
    this.sum = new String[jumlahLog];
    this.date = new String[jumlahLog];
  }

  public void printLog() {
    for (int i = 0; i < this.log.length; i++) System.out.println(
      this.log[i] + ": " + this.sum[i] + " On Date: " + this.date[i]
    );
  }

  public void setNewLog(String newLog, double newSum, String newDate) {
    int length = this.getLog().length;

    // Buat array baru langsung
    String[] tempLog = new String[length + 1];
    String[] tempSum = new String[length + 1];
    String[] tempDate = new String[length + 1];

    // Salin elemen yang sudah ada (jika ada)
    System.arraycopy(this.getLog(), 0, tempLog, 0, length);
    System.arraycopy(this.getSum(), 0, tempSum, 0, length);
    System.arraycopy(this.getDate(), 0, tempDate, 0, length);

    // Tambahkan elemen baru ke array
    tempLog[length] = newLog;
    tempSum[length] = String.valueOf(newSum);
    tempDate[length] = newDate;

    // Perbarui array yang ada dengan array baru
    this.log = tempLog;
    this.sum = tempSum;
    this.date = tempDate;
  }

  public void setDate(String date, int i) {
    this.date[i] = date;
  }

  public void setLog(String log, int i) {
    this.log[i] = log;
  }

  public void setSum(String sum, int i) {
    this.sum[i] = sum;
  }

  public String getNomorKartu() {
    return this.nomorKartu;
  }

  public String[] getLog() {
    return this.log;
  }

  public String[] getSum() {
    return this.sum;
  }

  public String[] getDate() {
    return this.date;
  }
}
