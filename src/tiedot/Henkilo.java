package tiedot;

/** Yksittäinen henkilö. Sisältää vain henkilön tunnistamiseen tarvittavat tiedot.*/
public class Henkilo {
    /** Henkilön ID-tunnus. */
    public final String id;
    /** Henkilön etunimi. */
    public final String etunimi;
    /** Henkilön sukunimi. */
    public final String sukunimi;
    
    /** Luo uuden Henkilo-olion annetuilla tiedoilla.
     * @param id Henkilön ID.
     * @param etunimi Henkilön etunimi.
     * @param sukunimi Henkilön sukunimi.
     */
    public Henkilo(String id, String etunimi, String sukunimi) {
        this.id = id;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
    }
    /** Palauttaa henkilön tiedot tekstinä.
     * @return Teksti muodossa "Etunimi Sukunimi (ID)".
     */
    @Override
    public String toString() {
        return etunimi + " " + sukunimi + " (" + id + ")";
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Henkilo)) return false;
        Henkilo h = (Henkilo) o;
        return h.id.equals(this.id) && h.etunimi.equals(this.etunimi) && h.sukunimi.equals(this.sukunimi);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.id.hashCode();
        hash = 61 * hash + this.etunimi.hashCode();
        hash = 61 * hash + this.sukunimi.hashCode();
        return hash;
    }
}