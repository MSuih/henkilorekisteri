package tiedot;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JOptionPane;

 /* Tietokantavirhe toimii seuraavasti:
 - Kun yhteys-luokassa tulee virhe, älä palauta alkuperäistä virhettä vaan kääri se TietokantaVirhe:ksi
 - Lisää tähän luokkaan alkuperäinen virhe ja ilmoitus siitä miksi virhe tapahtui (esim. Salasana ei kelpaa)
 - Kun käyttöliittymä yrittää käyttää tietokantaa, pitää sen tarkistaa virheet
 - Jos virhe tapahtuu, käyttöliittymä näyttää sen näytäVirheIlmoitus()-metodilla
 - Virheilmoitukseen pitää lisätä komponentti jossa virhe tapahtui (tai null)
 - Lisäksi siihen tarvitaan teksti, jossa selostetaan mitä yritettiin tehdä (esim. Virhe tietokantaan kirjautuessa)
 */

/** Tietokantavirheiden yleisluokka. Jos tietojen hakemisessa tapahtuu jokin virhe, heitetään tämän luokan mukainen virhe. Virheen syitä on monia: Tietokannan rakenne ei vastaa ohjelmakoodia, tietoja ei löytynyt, yhteyttä ei voitu muodostaa, yhteys on katkennut tai suljettu, tietokantaa ei löytynyt.. */
public class TietokantaVirhe extends Exception {
    private final SQLException virhe;
    private final String syy;
    
    /** Luo uuden tietokantavirheen
     * @param virhe SQL-virhe tämän virheen taustalla
     * @param syy Yleinen syy,  miten virhe tapahtui. Käytetään vain mikäli syy on tuntematon.
     */
    public TietokantaVirhe(SQLException virhe, String syy) {
        this.virhe = virhe;
        this.syy = syy;
    }
    
    /** Näyttää käyttäjälle virheilmoituksen.
     * @param ruutu Komponentti, jonka päälle ilmoitus laitetaan. Voi olla null.
     * @param teksti Ilmoitus siitä, mitä yritettiin tehdä eli mikä ei onnistunut.
     */
    public void naytaVirheIlmoitus(Component ruutu, String teksti) {
        String s;
        //päätä mikä tarkempi virheteksti liitetään ilmoitukseen
        switch (virhe.getSQLState()) {
            case "42000":
                s = "Virhe tietokannan rakenteessa.";
                break;
            case "22001": 
                s = "Olet syöttänyt johonkin kenttään liian paljon tekstiä";
                break;
            case "28000":
                s = "Kirjautuminen ei kelvannut. Yritä kirjautua uudestaan.";
                break;
            default:
                s = syy;
                break;
        }
        //lisää perään tarkka SQL-virhe
        //tähän voisi lisätä jonkun if-ehdon
        s += "\n(sql: " + virhe.getSQLState()+ "/ " + virhe.getLocalizedMessage() + " )";
        JOptionPane.showMessageDialog(
                ruutu,
                teksti + ":\n" + s,
                "Virhe",
                JOptionPane.ERROR_MESSAGE
        );
    }
    /** Ilmoita kirjautumisvirheestä. 
     * @param ruutu Komponentti, jonka päälle virheilmoitus sijoitetaan. Voi olla null.
     */
    public void naytaKirjautumisVirhe(Component ruutu) {
        String s;
        switch (virhe.getSQLState()) {
            case "42000":
                s = "Tietokantaa ei löytynyt tai se on väärin asennettu.";
                break;
            case "28000":
                s = "Virheellinen käyttäjätunnus tai salasana.";
                break;
            default:
                s = syy + "(sql: " + virhe.getSQLState()+ ")";
                break;
        }
        JOptionPane.showMessageDialog(
                ruutu,
                "Kirjautuminen ei onnistunut:\n" + s,
                "Virhe",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
