public class Apartament {
    private int numarApartament;
    private String nume;
    private int suprafata;
    private int numarPersoane;

    public Apartament(int numarApartament, String nume, int suprafata, int numarPersoane) {
        this.numarApartament = numarApartament;
        this.nume = nume;
        this.suprafata = suprafata;
        this.numarPersoane = numarPersoane;
    }

    public int getNumarApartament() {
        return numarApartament;
    }

    public void setNumarApartament(int numarApartament) {
        this.numarApartament = numarApartament;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getSuprafata() {
        return suprafata;
    }

    public void setSuprafata(int suprafata) {
        this.suprafata = suprafata;
    }

    public int getNumarPersoane() {
        return numarPersoane;
    }

    public void setNumarPersoane(int numarPersoane) {
        this.numarPersoane = numarPersoane;
    }

    @Override
    public String toString() {
        return "Apartament{" +
                "numarApartament=" + numarApartament +
                ", nume='" + nume + '\'' +
                ", suprafata=" + suprafata +
                ", numarPersoane=" + numarPersoane +
                '}';
    }
}
