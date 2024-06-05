public class Candidat implements Comparable<Candidat>{


    private long cnp;
    private String nume;
    private double notaBAC;
    private int codSpecializare;

    public Candidat(long cnp, String nume, double notaBAC, int codSpecializare) {
        this.cnp = cnp;
        this.nume = nume;
        this.notaBAC = notaBAC;
        this.codSpecializare = codSpecializare;
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

    public int getCodSpecializare() {
        return codSpecializare;
    }

    public void setCodSpecializare(int codSpecializare) {
        this.codSpecializare = codSpecializare;
    }

    @Override
    public String toString() {
        return "Candidati{" +
                "cnp=" + cnp +
                ", nume='" + nume + '\'' +
                ", notaBAC=" + notaBAC +
                ", codSpecializare=" + codSpecializare +
                '}';
    }

    @Override
    public int compareTo(Candidat o) {
        return Double.compare(this.notaBAC,o.getNotaBAC());
    }
}


