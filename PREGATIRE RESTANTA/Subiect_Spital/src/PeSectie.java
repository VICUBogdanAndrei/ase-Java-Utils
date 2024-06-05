public class PeSectie {
    private int codSectie;
    private double varstaMedie;

    public PeSectie(int codSectie, double varstaMedie) {
        this.codSectie = codSectie;
        this.varstaMedie = varstaMedie;
    }

    public int getCodSectie() {
        return codSectie;
    }

    public void setCodSectie(int codSectie) {
        this.codSectie = codSectie;
    }

    public double getVarstaMedie() {
        return varstaMedie;
    }

    public void setVarstaMedie(double varstaMedie) {
        this.varstaMedie = varstaMedie;
    }

    @Override
    public String toString() {
//        return "PeSectie{" +
//                "codSectie=" + codSectie +
//                ", varstaMedie=" + varstaMedie +
//                '}';
        return String.format("%20.2f", this.varstaMedie);
    }
}
