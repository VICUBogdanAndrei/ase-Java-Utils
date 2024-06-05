public class PacinetPeSectie {
    private int cod_sectie;
    private double medie_varsta;

    public PacinetPeSectie(int cod_sectie, double medie_varsta) {
        this.cod_sectie = cod_sectie;
        this.medie_varsta = medie_varsta;
    }

    public int getCod_sectie() {
        return cod_sectie;
    }

    public void setCod_sectie(int cod_sectie) {
        this.cod_sectie = cod_sectie;
    }

    public double getMedie_varsta() {
        return medie_varsta;
    }

    public void setMedie_varsta(double medie_varsta) {
        this.medie_varsta = medie_varsta;
    }

    @Override
    public String toString() {
        return String.format("%21.2f", this.medie_varsta);
    }
}
