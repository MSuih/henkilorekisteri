package tiedot;

/** Työpaikan tiedot yhtenä oliona. */
public class TyopaikkaTiedoilla extends Tyopaikka {
    /** Työpaikan osoite. */
    public final String osoite;
    /** Arvioitu valmistumisaika. */
    public final String valmistumis;
    /** Työpaikan aukiolo/työajat .*/
    public final String tyoajat;
    
    /** Luo uuden työpaikan annetuilla tiedoilla.
     * @param id Työpaikan ID.
     * @param nimi Työpaikan nimi.
     * @param osoite Työpaikan osoite.
     * @param tyoajat Työpaikan työajat.
     * @param valmistumis Työpaikan valmistumisaika.
     */
    public TyopaikkaTiedoilla(String id, String nimi, String osoite, String tyoajat, String valmistumis) {
        super(id, nimi);
        this.osoite = osoite;
        this.valmistumis = valmistumis;
        this.tyoajat = tyoajat;
    }
    
}
