package tiedot;

/** Yksittäisen työpaikan luokka. Tässä on vain työpaikkojen tunnistamiseen riittävät tiedot. */
public class Tyopaikka {
    /** Työpaikan ID. */
    public final String id;
    /** Työpaikan nimi. */
    public final String nimi;
    
    /** Luo uusi työpaikka.
     * @param id Työpaikan ID.
     * @param nimi 
     */
    public Tyopaikka (String id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tyopaikka)) return false;
        Tyopaikka t = (Tyopaikka) o;
        return this.id.equals(t.id) && this.nimi.equals(t.nimi);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id.hashCode();
        hash = 71 * hash + this.nimi.hashCode();
        return hash;
    }
    /** Palauttaa työpaikan tiedot tekstimuodossa.
     * @return Tekstinpätkä muodossa "Nimi (Tunnus)"
     */
    @Override
    public String toString() {
        return nimi + " (" + id + ")";
    }
}
