public class Pacient {

    private long cnp;
    private String nume;
    private int varsta;
    private int codSectie;

    public Pacient(long cnp, String nume, int varsta, int codSectie) {
        this.cnp = cnp;
        this.nume = nume;
        this.varsta = varsta;
        this.codSectie = codSectie;
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

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public int getCodSectie() {
        return codSectie;
    }

    public void setCodSectie(int codSectie) {
        this.codSectie = codSectie;
    }

    @Override
    public String toString() {
        return "Pacient{" +
                "cnp=" + cnp +
                ", nume='" + nume + '\'' +
                ", varsta=" + varsta +
                ", codSectie=" + codSectie +
                '}';
    }
}
