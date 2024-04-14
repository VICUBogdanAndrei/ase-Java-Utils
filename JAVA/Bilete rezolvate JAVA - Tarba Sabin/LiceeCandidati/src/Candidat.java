import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Candidat {

    private int codCandidat;
    private String nume;
    private double medie;
    private Map<Integer, List<Integer>> optiuni; // key = cod liceu, value = cod optiune

    public Candidat(int codCandidat, String nume, double medie, Map<Integer, List<Integer>> optiuni) {
        this.codCandidat = codCandidat;
        this.nume = nume;
        this.medie = medie;
        this.optiuni = optiuni;
    }

    public int getCodCandidat() {
        return codCandidat;
    }

    public void setCodCandidat(int codCandidat) {
        this.codCandidat = codCandidat;
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

    public int getNrOptiuni(){

        int nrOptiuni = 0;

        for(var optiune : optiuni.entrySet())
            nrOptiuni += optiune.getValue().size();

        return nrOptiuni;

    }

    @Override
    public String toString() {
        return "Candidat{" +
                "codCandidat=" + codCandidat +
                ", nume='" + nume + '\'' +
                ", medie=" + medie +
                ", optiuni=" + optiuni +
                '}';
    }
}
