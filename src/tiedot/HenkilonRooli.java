package tiedot;

/** Yksi työntekijän työrooli. */
public class HenkilonRooli {
    /** Työntekijän roolin ID. */
    public final String id;
    /** Roolin nimi. */
    public final MahdollinenRooli rooli;
    /** Roolin kuvaus/kommentit. */
    public final String kuvaus;
    
    /** Luo uusi työntekijän rooli.
     * @param id Työntekijän roolin ID.
     * @param rooli MahdollinenRooli, johon tämä rooli perustuu.
     * @param kuvaus Roolin kuvaus.
     */
    public HenkilonRooli(String id, MahdollinenRooli rooli, String kuvaus) {
        this.id = id;
        this.rooli = rooli;
        this.kuvaus = kuvaus;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HenkilonRooli)) return false;
        HenkilonRooli r = (HenkilonRooli) o;
        return this.id.equals(r.id) && this.rooli.equals(r.rooli);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id.hashCode();
        hash = 37 * hash + this.rooli.hashCode();
        return hash;
    }
}