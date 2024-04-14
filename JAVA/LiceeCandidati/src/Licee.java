import java.util.Map;

public class Licee {
    private int codLiceu;
    private String denumireLiceu;
    private int nrSpecializari;
    private Map<Integer, Integer> nrLocuriPerSpec;

    public Licee(int codLiceu, String denumireLiceu, int nrSpecializari, Map<Integer, Integer> nrLocuriPerSpec) {
        this.codLiceu = codLiceu;
        this.denumireLiceu = denumireLiceu;
        this.nrSpecializari = nrSpecializari;
        this.nrLocuriPerSpec = nrLocuriPerSpec;
    }

    @Override
    public String toString() {
        return "Licee{" +
                "codLiceu=" + codLiceu +
                ", denumireLiceu='" + denumireLiceu + '\'' +
                ", nrSpecializari=" + nrSpecializari +
                ", nrLocuriPerSpec=" + nrLocuriPerSpec +
                '}';
    }

    public int getCodLiceu() {
        return codLiceu;
    }

    public void setCodLiceu(int codLiceu) {
        this.codLiceu = codLiceu;
    }

    public String getDenumireLiceu() {
        return denumireLiceu;
    }

    public void setDenumireLiceu(String denumireLiceu) {
        this.denumireLiceu = denumireLiceu;
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

    public int getNrTotalLocuri() {
        return nrLocuriPerSpec.values().stream().mapToInt(Integer::intValue).sum();
    }

    public String cerinta2() {
        return this.codLiceu+ " " + this.denumireLiceu + " " + this.getNrTotalLocuri();
    }
}
