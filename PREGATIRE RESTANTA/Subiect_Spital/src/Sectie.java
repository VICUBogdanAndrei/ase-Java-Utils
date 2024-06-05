public class Sectie {
    private int cod_sectie;
    private String denumire;
    private int nrLocuri;

    public Sectie(int cod_sectie, String denumire, int nrLocuri) {
        this.cod_sectie = cod_sectie;
        this.denumire = denumire;
        this.nrLocuri = nrLocuri;
    }

    public int getCod_sectie() {
        return cod_sectie;
    }

    public void setCod_sectie(int cod_sectie) {
        this.cod_sectie = cod_sectie;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "Sectie{" +
                "cod_sectie=" + cod_sectie +
                ", denumire='" + denumire + '\'' +
                ", nrLocuri=" + nrLocuri +
                '}';
    }
}
