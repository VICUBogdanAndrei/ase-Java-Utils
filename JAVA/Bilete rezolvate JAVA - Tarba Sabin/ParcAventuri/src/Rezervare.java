public class Rezervare {

    private int codRezervare;
    private int codAventura;
    private int nrLocuriCerute;

    public Rezervare(int codRezervare, int codAventura, int nrLocuriCerute) {
        this.codRezervare = codRezervare;
        this.codAventura = codAventura;
        this.nrLocuriCerute = nrLocuriCerute;
    }

    public Rezervare(String linie) {
        this.codRezervare = Integer.parseInt(linie.split(",")[0]);
        this.codAventura = Integer.parseInt(linie.split(",")[1]);
        this.nrLocuriCerute = Integer.parseInt(linie.split(",")[2]);

    }



    public int getCodRezervare() {
        return codRezervare;
    }

    public void setCodRezervare(int codRezervare) {
        this.codRezervare = codRezervare;
    }

    public int getCodAventura() {
        return codAventura;
    }

    public void setCodAventura(int codAventura) {
        this.codAventura = codAventura;
    }

    public int getNrLocuriCerute() {
        return nrLocuriCerute;
    }

    public void setNrLocuriCerute(int nrLocuriCerute) {
        this.nrLocuriCerute = nrLocuriCerute;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "codRezervare=" + codRezervare +
                ", codAventura=" + codAventura +
                ", nrLocuriCerute=" + nrLocuriCerute +
                '}';
    }
}
