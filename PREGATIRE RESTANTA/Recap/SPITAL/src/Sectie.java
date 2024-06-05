public class Sectie {
    private int cod_sectie;
    private String denumire;
    private int nr_locuri;

    public Sectie(int cod_sectie, String denumire, int nr_locuri) {
        this.cod_sectie = cod_sectie;
        this.denumire = denumire;
        this.nr_locuri = nr_locuri;
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

    public int getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(int nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    @Override
    public String toString() {
        return "Sectie{" +
                "cod_sectie=" + cod_sectie +
                ", denumire='" + denumire + '\'' +
                ", nr_locuri=" + nr_locuri +
                '}';
    }
}

