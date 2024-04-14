import java.util.List;
import java.util.Map;

public class Candidati {
    private int cod;
    private String nume;
    private double medie;
    private Map<Integer, List<Integer>> optiuni;

    public Candidati(int cod, String nume, double medie, Map<Integer, List<Integer>> optiuni) {
        this.cod = cod;
        this.nume = nume;
        this.medie = medie;
        this.optiuni = optiuni;
    }

    @Override
    public String toString() {
        return "Candidati{" +
                "cod=" + cod +
                ", nume='" + nume + '\'' +
                ", medie=" + medie  +
                ", optiuni=" + optiuni +
                '}';
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
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

    public Map<Integer, List<Integer>> getOptiuni() {
        return optiuni;
    }

    public void setOptiuni(Map<Integer, List<Integer>> optiuni) {
        this.optiuni = optiuni;
    }

    public int getNrOptiuni() {
        int nrOptiuni = 0;
        for( var optiune : optiuni.entrySet())
            nrOptiuni+=optiune.getValue().size();
        return nrOptiuni;
    }
}
