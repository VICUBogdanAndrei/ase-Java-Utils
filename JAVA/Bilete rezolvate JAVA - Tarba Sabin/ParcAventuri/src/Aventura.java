public class Aventura {

    private int cod;
    private String denumire;
    private double tarif;
    private int nrLocuriDisponibile;

    public Aventura(int cod, String denumire, double tarif, int nrLocuriLibere) {
        this.cod = cod;
        this.denumire = denumire;
        this.tarif = tarif;
        this.nrLocuriDisponibile = nrLocuriLibere;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
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

    public int getNrLocuriDisponibile() {
        return nrLocuriDisponibile;
    }

    public void setNrLocuriDisponibile(int nrLocuriLibere) {
        this.nrLocuriDisponibile = nrLocuriLibere;
    }

    @Override
    public String toString() {
        return "Aventura{" +
                "cod=" + cod +
                ", denumire='" + denumire + '\'' +
                ", tarif=" + tarif +
                ", nrLocuriDisponibile=" + nrLocuriDisponibile +
                '}';
    }
}
