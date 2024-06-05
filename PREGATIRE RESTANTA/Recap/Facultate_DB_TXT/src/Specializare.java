public class Specializare {
    private int cod;
    private String denumire;
    private int nrLocuri;

    public Specializare(int cod, String denumire, int nrLocuri) {
        this.cod = cod;
        this.denumire = denumire;
        this.nrLocuri = nrLocuri;
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

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "Specializare{" +
                "cod=" + cod +
                ", denumire='" + denumire + '\'' +
                ", nrLocuri=" + nrLocuri +
                '}';
    }
}
