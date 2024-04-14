public class Persoana {

    private int cod;
    private String cnp;
    private String nume;


    public Persoana(int cod, String cnp, String nume) {
        this.cod = cod;
        this.cnp = cnp;
        this.nume = nume;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Persoana{" +
                "cod=" + cod +
                ", cnp='" + cnp + '\'' +
                ", nume='" + nume + '\'' +
                '}';
    }
}
