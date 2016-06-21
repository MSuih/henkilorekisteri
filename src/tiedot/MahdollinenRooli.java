package tiedot;

/** Mahdollinen henkilölle annettava rooli. */
public class MahdollinenRooli {
    /** Roolin ID. */
    public final String id;
    /** Roolin nimi. */
    public final String nimi;
    
    /** Luo uusi Mahdollinenrooli-olio.
     * @param id Roolin ID.
     * @param nimi Roolin nimi.
     */
    public MahdollinenRooli(String id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    /** Palauttaa tämän roolin tekstimuodossa.
     * @return Teksti muodossa [nimi] ([ID])
     */
    @Override
    public String toString() {
        return nimi + " (" + id + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MahdollinenRooli)) return false;
        MahdollinenRooli r = (MahdollinenRooli) o;
        return this.id.equals(r.id) && this.nimi.equals(r.nimi);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id.hashCode();
        hash = 37 * hash + this.nimi.hashCode();
        return hash;
    }
}