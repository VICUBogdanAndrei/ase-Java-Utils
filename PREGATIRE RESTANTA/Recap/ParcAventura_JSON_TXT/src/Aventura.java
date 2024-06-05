public class Aventura {
    private int cod_aventura;
    private String denumire;
    private double tarif;
    private int locuri_disponibile;

    public Aventura(int cod_aventura, String denumire, double tarif, int locuri_disponibile) {
        this.cod_aventura = cod_aventura;
        this.denumire = denumire;
        this.tarif = tarif;
        this.locuri_disponibile = locuri_disponibile;
    }

    public int getCod_aventura() {
        return cod_aventura;
    }

    public void setCod_aventura(int cod_aventura) {
        this.cod_aventura = cod_aventura;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public int getLocuri_disponibile() {
        return locuri_disponibile;
    }

    public void setLocuri_disponibile(int locuri_disponibile) {
        this.locuri_disponibile = locuri_disponibile;
    }

    @Override
    public String toString() {
        return "Aventura{" +
                "cod_aventura=" + cod_aventura +
                ", denumire='" + denumire + '\'' +
                ", tarif=" + tarif +
                ", locuri_disponibile=" + locuri_disponibile +
                '}';
    }
}
