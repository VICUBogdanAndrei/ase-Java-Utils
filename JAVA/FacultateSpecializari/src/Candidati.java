import java.lang.Comparable;

public class Candidati implements Comparable<Candidati>{
    private long cnp;
    private String nume;
    private double notaBac;
    private int codSpecializare;

    public Candidati(long cnp, String nume, double notaBac, int codSpecializare) {
        this.cnp = cnp;
        this.nume = nume;
        this.notaBac = notaBac;
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

    public double getNotaBac() {
        return notaBac;
    }

    public void setNotaBac(double notaBac) {
        this.notaBac = notaBac;
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
                ", notaBac=" + notaBac +
                ", codSpecializare=" + codSpecializare +
                '}';
    }

    @Override
    public int compareTo(Candidati o) {
        return Double.compare(this.notaBac, o.getNotaBac());
    }
}
