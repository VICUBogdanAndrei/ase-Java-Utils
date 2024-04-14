public class Inscriere {

    private long cnp;
    private String nume;
    private double medie;
    private int codSpecializare;

    public Inscriere(long cnp, String nume, double medie, int codSpecializare) {
        this.cnp = cnp;
        this.nume = nume;
        this.medie = medie;
        this.codSpecializare = codSpecializare;
    }

    public Inscriere(String linie) {
        this.cnp = Long.parseLong(linie.split(",")[0]);
        this.nume = linie.split(",")[1];
        this.medie = Double.parseDouble(linie.split(",")[2]);
        this.codSpecializare = Integer.parseInt(linie.split(",")[3]);
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

    public double getMedie() {
        return medie;
    }

    public void setMedie(double medie) {
        this.medie = medie;
    }

    public int getCodSpecializare() {
        return codSpecializare;
    }

    public void setCodSpecializare(int codSpecializare) {
        this.codSpecializare = codSpecializare;
    }

    @Override
    public String toString() {
        return "Inscriere{" +
                "cnp=" + cnp +
                ", nume='" + nume + '\'' +
                ", medie=" + medie +
                ", codSpecializare=" + codSpecializare +
                '}';
    }
}
