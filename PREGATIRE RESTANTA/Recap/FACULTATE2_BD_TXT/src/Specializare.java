public class Specializare {
    private int cod;
    private String denumire;
    private int nr_locuri;

    public Specializare(int cod, String denumire, int nr_locuri) {
        this.cod = cod;
        this.denumire = denumire;
        this.nr_locuri = nr_locuri;
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

    public int getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(int nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    @Override
    public String toString() {
        return "Specializare{" +
                "cod=" + cod +
                ", denumire='" + denumire + '\'' +
                ", nr_locuri=" + nr_locuri +
                '}';
    }
}
