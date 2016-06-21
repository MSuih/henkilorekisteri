package tiedot;

import java.sql.Date;

/** Henkilön kaikki tiedot yhtenä oliona. Lisää Henkilo-olioon lisäkenttiä jotta kaikki tiedot voidaan siirtää tietokannasta tai tietokantaan. */
public class HenkiloTiedot extends Henkilo {
    /** Henkilön osoite. */
    public final String osoite;
    /** Henkilön puhelinnumero. */
    public final String puhelin;
    /** Henkilön syntymäaika. */
    public final Date syntyma;
    /** Henkilön koulutus. */
    public final String koulutus;
    /** Henkilön työskentelyn aloituspäivä. */
    public final Date aloitusaika;
    
    /** Luo uuden HenkilöTiedot-olion annetuista tiedoista.
     * @param id Henkilön ID.
     * @param etunimi Henkilön etunimi.
     * @param sukunimi Henkilön sukunimi.
     * @param osoite Henkilön osoite.
     * @param puhelin Henkilön puhelinnumero.
     * @param syntyma Henkilön syntymäaika.
     * @param koulutus Henkilön koulutus.
     * @param aloitusaika Henkilön työskentelyn aloituspäivä.
     */
    public HenkiloTiedot(String id, String etunimi, String sukunimi, String osoite, String puhelin, Date syntyma, String koulutus, Date aloitusaika) {
        super(id, etunimi, sukunimi);
        this.osoite = osoite;
        this.puhelin = puhelin;
        this.syntyma = syntyma;
        this.koulutus = koulutus;
        this.aloitusaika = aloitusaika;
    }
}