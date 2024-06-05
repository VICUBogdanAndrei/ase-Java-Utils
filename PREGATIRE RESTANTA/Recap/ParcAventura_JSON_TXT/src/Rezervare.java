public class Rezervare {
    private int id_rezervare;
    private int cod_aventura;
    private int nr_locuri_rezervate;

    public Rezervare(int id_rezervare, int cod_aventura, int nr_locuri_rezervate) {
        this.id_rezervare = id_rezervare;
        this.cod_aventura = cod_aventura;
        this.nr_locuri_rezervate = nr_locuri_rezervate;
    }

    public int getId_rezervare() {
        return id_rezervare;
    }

    public void setId_rezervare(int id_rezervare) {
        this.id_rezervare = id_rezervare;
    }

    public int getCod_aventura() {
        return cod_aventura;
    }

    public void setCod_aventura(int cod_aventura) {
        this.cod_aventura = cod_aventura;
    }

    public int getNr_locuri_rezervate() {
        return nr_locuri_rezervate;
    }

    public void setNr_locuri_rezervate(int nr_locuri_rezervate) {
        this.nr_locuri_rezervate = nr_locuri_rezervate;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "id_rezervare=" + id_rezervare +
                ", cod_aventura=" + cod_aventura +
                ", nr_locuri_rezervate=" + nr_locuri_rezervate +
                '}';
    }
}
