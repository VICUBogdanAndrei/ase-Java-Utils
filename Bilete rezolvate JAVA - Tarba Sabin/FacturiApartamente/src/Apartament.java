public class Apartament {

    private int nrApartament;
    private int suprafata;
    private int nrPersoane;


    public Apartament(int nrApartament, int suprafata, int nrPersoane) {
        this.nrApartament = nrApartament;
        this.suprafata = suprafata;
        this.nrPersoane = nrPersoane;
    }

    public Apartament(String linie) {
        this.nrApartament = Integer.parseInt(linie.split(",")[0]);
        this.suprafata = Integer.parseInt(linie.split(",")[1]);
        this.nrPersoane = Integer.parseInt(linie.split(",")[2]);
    }

    public int getNrApartament() {
        return nrApartament;
    }

    public void setNrApartament(int nrApartament) {
        this.nrApartament = nrApartament;
    }

    public int getSuprafata() {
        return suprafata;
    }

    public void setSuprafata(int suprafata) {
        this.suprafata = suprafata;
    }

    public int getNrPersoane() {
        return nrPersoane;
    }

    public void setNrPersoane(int nrPersoane) {
        this.nrPersoane = nrPersoane;
    }

    @Override
    public String toString() {
        return "Apartament{" +
                "nrApartament=" + nrApartament +
                ", suprafata=" + suprafata +
                ", nrPersoane=" + nrPersoane +
                '}';
    }
}
