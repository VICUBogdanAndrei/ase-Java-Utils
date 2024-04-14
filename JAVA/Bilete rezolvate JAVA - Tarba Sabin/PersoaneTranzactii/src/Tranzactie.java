import java.util.Locale;

enum Tip{

    VANZARE(1),
    CUMPARARE(-1);

    private int semn;

    Tip(String tip){
        if(tip.equalsIgnoreCase("VANZARE"))
            semn = -1;
        else semn = 1;
    }

    Tip(int semn){

        this.semn = semn;
    }

    public int getSemn(){
        return this.semn;
    }

}


public class Tranzactie {

    private int cod;
    private String simbol;
    private Tip tip;
    private int cantitate;
    private float pret;

    public Tranzactie(int cod, String simbol, Tip tip, int cantitate, float pret) {
        this.cod = cod;
        this.simbol = simbol;
        this.tip = tip;
        this.cantitate = cantitate;
        this.pret = pret;
    }

    public Tranzactie(String linie) {

        this.cod = Integer.parseInt(linie.split(",")[0]);
        this.simbol = linie.split(",")[1];
        this.tip = Tip.valueOf(linie.split(",")[2].toUpperCase());
        this.cantitate = Integer.parseInt(linie.split(",")[3]);
        this.pret = Float.parseFloat(linie.split(",")[4]);
    }


    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getSimbol() {
        return simbol;
    }

    public void setSimbol(String simbol) {
        this.simbol = simbol;
    }

    public Tip getTip() {
        return tip;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public float getPret() {
        return pret;
    }

    public void setPret(float pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "cod=" + cod +
                ", simbol='" + simbol + '\'' +
                ", tip=" + tip +
                ", cantitate=" + cantitate +
                ", pret=" + pret +
                '}';
    }
}
