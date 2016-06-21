package tiedot;

/** Työroolin ja työpaikan sisältävä luokka. */
public class HenkilonRooliTyopaikalla extends HenkilonRooli {
    /** Työpaikka, johon työrooli liittyy. */
    public final Tyopaikka tyopaikka;
    
    /** Luo uusi työpaikan sisältämä työrooli.
     * @param id Henkilön roolin ID.
     * @param rooli Rooli, johon tämä työrooli perustuu.
     * @param kuvaus Roolin kuvaus.
     * @param tyopaikka Työpaikka, johon rooli kuuluu.
     */
    public HenkilonRooliTyopaikalla(String id, MahdollinenRooli rooli, String kuvaus, Tyopaikka tyopaikka) {
        super(id, rooli, kuvaus);
        this.tyopaikka = tyopaikka;
    }
    
    /** Tulostaa tämä roolin tekstimuodossa
     * @return Työrooli muodossa [Työpaikka] / [Roolin nimi]
     */
    @Override
    public String toString() {
        return tyopaikka.nimi + " / " + rooli.nimi;
    }
}