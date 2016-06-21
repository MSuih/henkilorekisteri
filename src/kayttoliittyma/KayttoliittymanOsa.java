package kayttoliittyma;

import javax.swing.JPanel;

/** Käyttöliittymän osien metodit. Näin päävalikosta voidaan eri osia avatessa päivittää tai tyhjentää näkymät. Tämän luokan metodit eivät tee itsessään mitään, vaan jokaisen ikkunan on korvattava ne mikäli tarvetta on. */
public abstract class KayttoliittymanOsa extends JPanel {
    
    /** Hakee tietokannasta uusimmat tiedot käyttöliittymään. */
    public void paivita() {
        
    }
    /** Tyhjentää käyttöliittymän tiedot. */
    public void tyhjenna() {
        
    }
}
