public class Inscriere {
    private long cnp;
    private String nume;
    private double notaBAC;
    private int cod_specializare;

    public Inscriere(long cnp, String nume, double notaBAC, int cod_specializare) {
        this.cnp = cnp;
        this.nume = nume;
        this.notaBAC = notaBAC;
        this.cod_specializare = cod_specializare;
    }

    public long getCnp() {
        return cnp;
    }

    public void setCnp(long cnp) {
        this.cnp = cnp;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getNotaBAC() {
        return notaBAC;
    }

    public void setNotaBAC(double notaBAC) {
        this.notaBAC = notaBAC;
    }

    public int getCod_specializare() {
        return cod_specializare;
    }

    public void setCod_specializare(int cod_specializare) {
        this.cod_specializare = cod_specializare;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "cnp=" + cnp +
                ", nume='" + nume + '\'' +
                ", notaBAC=" + notaBAC +
                ", cod_specializare=" + cod_specializare +
                '}';
    }
}
