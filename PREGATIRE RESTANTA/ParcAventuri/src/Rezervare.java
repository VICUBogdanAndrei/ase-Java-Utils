public class Rezervare {
    private int id_rezervare;
    private int cod_aventura;
    private int numar_locuri_rezervare;

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

    public int getNumar_locuri_rezervare() {
        return numar_locuri_rezervare;
    }

    public void setNumar_locuri_rezervare(int numar_locuri_rezervare) {
        this.numar_locuri_rezervare = numar_locuri_rezervare;
    }

    public Rezervare(int id_rezervare, int cod_aventura, int numar_locuri_rezervare) {
        this.id_rezervare = id_rezervare;
        this.cod_aventura = cod_aventura;
        this.numar_locuri_rezervare = numar_locuri_rezervare;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "id_rezervare=" + id_rezervare +
                ", cod_aventura=" + cod_aventura +
                ", numar_locuri_rezervare=" + numar_locuri_rezervare +
                '}';
    }
}
