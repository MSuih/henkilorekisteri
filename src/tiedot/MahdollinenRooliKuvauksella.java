package tiedot;

/** MahdollinenRooli, jossa on yksi kenttä varattu roolin kuvaukselle. */
public class MahdollinenRooliKuvauksella extends MahdollinenRooli{
    /** Tämän roolin kuvaus. Tieto siitä minkälainen rooli tämä on. */
    public final String kuvaus;
    
    /** Tekee uuden MahdollinenRooliKuvauksella-olion.
     * @param id Roolin ID.
     * @param nimi Roolin nimi.
     * @param kuvaus Roolin kuvausteksti.
     */
    public MahdollinenRooliKuvauksella(String id, String nimi, String kuvaus) {
        super(id, nimi);
        this.kuvaus = kuvaus;
    }
    
}
