import java.util.Map;

public class Liceu {

    private int codLiceu;
    private String denumire;
    private int nrSpecializari;
    private Map<Integer, Integer> nrLocuriPerSpec;

    public Liceu(int codLiceu, String denumire, int nrSpecializari, Map<Integer, Integer> nrLocuriPerSpec) {
        this.codLiceu = codLiceu;
        this.denumire = denumire;
        this.nrSpecializari = nrSpecializari;
        this.nrLocuriPerSpec = nrLocuriPerSpec;
    }

    public int getCodLiceu() {
        return codLiceu;
    }

    public void setCodLiceu(int codLiceu) {
        this.codLiceu = codLiceu;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNrSpecializari() {
        return nrSpecializari;
    }

    public void setNrSpecializari(int nrSpecializari) {
        this.nrSpecializari = nrSpecializari;
    }

    public Map<Integer, Integer> getNrLocuriPerSpec() {
        return nrLocuriPerSpec;
    }

    public void setNrLocuriPerSpec(Map<Integer, Integer> nrLocuriPerSpec) {
        this.nrLocuriPerSpec = nrLocuriPerSpec;
    }

    public int getNrTotalLocuri(){

        return nrLocuriPerSpec.values().stream().mapToInt(Integer::intValue).sum();

    }

    @Override
    public String toString() {
        return "Liceu{" +
                "codLiceu=" + codLiceu +
                ", denumire='" + denumire + '\'' +
                ", nrSpecializari=" + nrSpecializari +
                ", nrLocuriPerSpec=" + nrLocuriPerSpec +
                '}';
    }
}
