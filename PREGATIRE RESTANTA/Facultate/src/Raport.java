public class Raport {
    public int id;
    public String denumire;
    public int nrinscrieri;
    public double medie;

    public Raport(int id, String denumire, int nrinscrieri, double medie) {
        this.id = id;
        this.denumire = denumire;
        this.nrinscrieri = nrinscrieri;
        this.medie = medie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNrinscrieri() {
        return nrinscrieri;
    }

    public void setNrinscrieri(int nrinscrieri) {
        this.nrinscrieri = nrinscrieri;
    }

    public double getMedie() {
        return medie;
    }

    public void setMedie(double medie) {
        this.medie = medie;
    }

    @Override
    public String toString() {
        return "Raport{" +
                "id=" + id +
                ", denumire='" + denumire + '\'' +
                ", nrinscrieri=" + nrinscrieri +
                ", medie=" + medie +
                '}';
    }
}
