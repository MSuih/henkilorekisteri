package tiedot;

import java.sql.Time;
import java.util.Arrays;

/** Työaikoja sisällään pitävä luokka. Tämä luokka sisältää koko viikon aloitus- ja lopetusajat*/
public class Tyoajat {
    // Viikonpäivät. Maanantai, tiistai jne. 
    private static final String[] paivat = {"Maanantai", "Tiistai", "Keskiviikko", "Torstai", "Perjantai", "Lauantai", "Sunnuntai"};
    /** Maanantai. */
    public static final byte MA = 0;
    /** Tiistai. */
    public static final byte TI = 1;
    /** Keskiviikko. */
    public static final byte KE = 2;
    /** Torstai. */
    public static final byte TO = 3;
    /** Perjantai. */
    public static final byte PE = 4;
    /** Lauantai. */
    public static final byte LA = 5;
    /** Sunnnutai. */
    public static final byte SU = 6;
    /** Työpäivän aloitusaika. */
    public static final byte ALKU = 0;
    /** Työpäivän lopetusaika. */
    public static final byte LOPPU = 1;
    
    /* Sekuntien poistamiseen / lisäämiseen tarvittavat muuttujat. String-muotoiset työajat on _aina_ ilman sekunteja ja sekunnit lisätään/poistetaan kun ajat muutetaan Timeksi/Timestä. */
    private static final int aika_alku = 0;
    private static final int aika_loppu = 5;
    private static final String aika_paate = ":00";
    // Työaikojen määrä. Pitäisi olla (Viikonpäivien määrä * 2), eli 10 tai 14
    private static final int aika_maara = 14;
    
    // [7] [2] taulukko
    private final Time[][] ajat;
    
    /** Tekee uuden Työajat-objektin annetuilla tiedoilla.
     * @param ma_a Maanantain työpäivän aloitusaika.
     * @param ma_l Maanantain työpäivän lopetusaika.
     * @param ti_a Tiistain työpäivän aloitusaika.
     * @param ti_l Tiistain työpäivän lopetusaika.
     * @param ke_a Keskiviikon työpäivän aloitusaika.
     * @param ke_l Keskiviikon työpäivän lopetusaika.
     * @param to_a Torstain työpäivän aloitusaika.
     * @param to_l Torstain työpäivän lopetusaika.
     * @param pe_a Perjantain työpäivän aloitusaika.
     * @param pe_l Perjantain työpäivän lopetusaika.
     * @param la_a Lauantain työpäivän aloitusaika.
     * @param la_l Lauantain työpäivän lopetusaika.
     * @param su_a Sunnuntain työpäivän aloitusaika.
     * @param su_l Sunnuntain työpäivän lopetusaika.
     */
    public Tyoajat(Time ma_a, Time ma_l, Time ti_a, Time ti_l, Time ke_a, Time ke_l, Time to_a, Time to_l, Time pe_a, Time pe_l, Time la_a, Time la_l, Time su_a, Time su_l) {
        ajat = new Time[][]{ 
            {ma_a, ma_l}, 
            {ti_a, ti_l}, 
            {ke_a, ke_l},
            {to_a, to_l},
            {pe_a, pe_l},
            {la_a, la_l},
            {su_a, su_l}
        };
    }
    /** Tekee uuden työajat-objektin tekstikenttien syötteiden tai vastaavien perusteella
     * @param syote Työajat muodossa 00:00. Järjestyksessä maanantain aloitus, maanantain lopetus, tiistain aloitus [...]
     * @throws IllegalArgumentException Mikäli päiviä ei ole oikea määrä
     */
    public Tyoajat(String[] syote) throws IllegalArgumentException {
        if (syote.length != aika_maara) throw new IllegalArgumentException("Aikojen pituus ei täsmää! "
                + "Odotettu = " + aika_maara 
                + ", saatu = " + syote.length);
        int i = 0;
        int p = 0;
        ajat = new Time[7][2];
        for (String s : syote) {
            if (s == null) ajat[p][i] = null;
            else if (s.equals("")) ajat[p][i] = null;
            else ajat[p][i] = Time.valueOf(s + aika_paate);
            if (i > 0) {
                i = 0;
                p++;
            }
            else i++;
        }
    }

    /** Pyydä tietyn viikonpäivän aloitus- tai lopetusaika tekstimuotona.
     * @param paiva Sen viikonpäivän muuttuja, jonka tiedot halutaan. (MA, TI, KE jne.)
     * @param tyyppi ALKU tai LOPPU muuttuja.
     * @return Aika tekstimuodossa, ilman sekunteja. Esim. 17:00
     */
    public String getAika(byte paiva, byte tyyppi){
        if (ajat[paiva][tyyppi] == null) return "";
        return ajat[paiva][tyyppi].toString().substring(aika_alku, aika_loppu);
    }
    /** Pyydä työpäivän aloitus- ja lopetusajat tekstinä.
     * @param paiva Sen viikonpäivän muuttuja, jonka tiedot halutaan. (MA, TI, KE jne.)
     * @return Aloitus- ja lopetusaika ko. päivälle. String[0] = aloitus ja String[1] = lopetus.
     */
    public String[] getPaivanAjat(byte paiva) {
        return new String[]{ajat[paiva][0].toString().substring(aika_alku, aika_loppu), ajat[paiva][1].toString().substring(aika_alku, aika_loppu)};
    }
    /** Näyttää työajat tekstinä. Esim:<br>
     * <p>
     *  Maanantai: 11:00 - 17:00<br>
     *  Tiistai: 11:00 - 17:00<br>
     *  Keskiviikko: 11:00 - 17:00<br>
     *  Torstai: 11:00 - 17:00<br>
     *  Perjantai: 11:00 - 17:00<br>
     *  Lauantai: Ei töitä<br>
     *  Sunnuntai: Ei töitä<br>
     * </p>
     * @return Tekstimuotoinen esitys työajoista.
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < 5; i++) {
            if (ajat[i][0] == null) s += paivat[i] + ": ei töitä";
            else s += paivat[i] + ": " + ajat[i][0].toString().substring(aika_alku, aika_loppu) +" - "+ ajat[i][1].toString().substring(aika_alku, aika_loppu);
            if (i < 4) s += "\n";
        }
        return s;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tyoajat)) return false;
        Tyoajat t = (Tyoajat) o;
        return Arrays.deepEquals(this.ajat, t.ajat);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Arrays.deepHashCode(this.ajat);
        return hash;
    }
    /** Palauttaa kaikki työajat Time[] -taulukkona. 
     * @return Ajat taulukkona. Järjestyksessä: maanantain aloitus, maanantain lopetus, tiistain aloitus...
     */
    public Time[] getAjat() {
        Time[] lista = new Time[14];
        int i = 0;
        int p = 0;
        int t = 0;
        while (p < ajat.length){
            lista[i] = ajat[p][t];
            i++;
            if (t > 0){
                t = 0;
                p++;
            }
            else t++;
        }
        return lista;
    }
}