public class Sectie {

    private int codSectie;
    private String denumireSectie;
    private int nrLocuri;

    public Sectie(int codSectie, String denumireSectie, int nrLocuri) {
        this.codSectie = codSectie;
        this.denumireSectie = denumireSectie;
        this.nrLocuri = nrLocuri;
    }

    public int getCodSectie() {
        return codSectie;
    }

    public void setCodSectie(int codSectie) {
        this.codSectie = codSectie;
    }

    public String getDenumireSectie() {
        return denumireSectie;
    }

    public void setDenumireSectie(String denumireSectie) {
        this.denumireSectie = denumireSectie;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "Sectie{" +
                "codSectie=" + codSectie +
                ", denumireSectie='" + denumireSectie + '\'' +
                ", nrLocuri=" + nrLocuri +
                '}';
    }
}
