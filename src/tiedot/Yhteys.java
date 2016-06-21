package tiedot;

import yleista.Asetukset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** Yhteys tietokantaan ja tietojen hakeminen tietokannasta. Yhteydenpito tietokantaan tai tietokannasta hoituu tämän luokan avulla.
 * <p>Luokka toimii tällä hetkellä vain sql-pohjaisissa tietokannoissa, mutta muutoksilla se saadaan toimimaan lähes missä tahansa tietokannassa, esimerkiksi tekstitiedostoissa. Tietokannan tyyppi vaikuttaa vain luokan sisäisiin toimintoihin.</p>
 * <p>Luokka hakee yhteysasetukset Asetus-luokkaa käyttämällä silloin kun ensimmäistä yhteyttä muodostetaan. Yhteysosoitteet tai vastaavat täytyy siis päivittää siihen luokkaan ennen kun tätä luokkaa käytetään.</p>
 * <p>Lähes kaikki metodit voivat heittää TietokantaVirhe-virheen. Tämä voi johtua monesta erilaisista syistä, esimerkiksi tietokannan väärästä rakenteesta, yhteysvirheestä tai tietokantapalvelimen virheestä.</p>
 */
public class Yhteys {
    // Seuraavat kentät päivitetään static-kohdassa
    private static String ajuri; // Ajuri, jota käytetään kun tietokantaan otetaan yhteys
    private static String url; // osoite ja tietokanta johon otetaan yhteyttä
    
    /*  VALMIIT LAUSEET. 
    * Lisää lauseet tähän niin ne ei sotke ohjelmakoodia alla.
    * Muista merkitä lauteet private static final -muotoon.
    * Merkitse oikean otsikon alle niin koodi pysyy im ordnung.
    */
    //Työpaikat
    private static final String l_haeTyopaikat = "SELECT työpaikkaID, nimi FROM työpaikka";
    private static final String l_haeTyopaikanTiedot = "SELECT työpaikkaID, nimi, osoite, aukioloajat, arvioituvalmistumisaika FROM työpaikka WHERE työpaikkaID = ?";
    private static final String l_haeKaikkiTyopaikkaIDt = "SELECT työpaikkaID FROM työpaikka";
    private static final String l_lisaaTyopaikka = "INSERT INTO työpaikka (työpaikkaID, nimi, osoite, aukioloajat, arvioituvalmistumisaika) VALUES (?, ?, ?, ?, ?)";
    private static final String l_paivitaTyopaikanTiedot = "UPDATE työpaikka "
            + "SET nimi = ?, osoite = ?, aukioloajat = ?, arvioituvalmistumisaika = ? "
            + "WHERE työpaikkaID = ?" ;
    private static final String l_poistaTyopaikka = "DELETE FROM työpaikka WHERE työpaikkaID = ?";
    //Työntekijät
    private static final String l_haeTyopaikanTyontekijat = 
            "SELECT työntekijä.työntekijäID, etunimi, sukunimi "
            + "FROM työntekijä JOIN työrooli ON työntekijä.työntekijäID = työrooli.työntekijäID "
            + "JOIN työpaikka ON työrooli.työpaikkaID = työpaikka.työpaikkaID "
            + "WHERE työpaikka.työpaikkaID = ? "
            + "ORDER BY sukunimi ASC, etunimi ASC";
    private static final String l_haeKaikkiTyontekijat = "SELECT työntekijäID, etunimi, sukunimi FROM työntekijä ORDER BY sukunimi ASC, etunimi ASC";
    private static final String l_haeTyopaikanUlkopuolisetTyontekijat = 
            "SELECT työntekijä.työntekijäID, etunimi, sukunimi "
            + "FROM työntekijä JOIN työrooli "
            + "ON työntekijä.työntekijäID = työrooli.työntekijäID "
            + "WHERE työrooli.työpaikkaID <> ? "
            + "ORDER BY sukunimi ASC, etunimi ASC";
    private static final String l_haeTyontekijanTiedot = "SELECT työntekijäID, etunimi, sukunimi, osoite, puhelin, syntymäaika, koulutus, aloitusaika FROM työntekijä WHERE työntekijäID = ?";
    private static final String l_paivitaTyontekijanTiedot = 
            "UPDATE työntekijä "
            + "SET etunimi = ?, sukunimi = ?, osoite = ?, "
            + "puhelin = ?, syntymäaika = ?, koulutus = ?, "
            + "aloitusaika = ?"
            + "WHERE työntekijäID = ?";
    private static final String l_lisaaTyontekija = "INSERT INTO työntekijä (työntekijäID, etunimi, sukunimi, osoite, puhelin, syntymäaika, koulutus, aloitusaika) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String l_haeKaikkiTyontekijaIDt = "SELECT työntekijäID FROM työntekijä";
    //MahdollisetRoolit
    private static final String l_haeMahdollisetRoolit = "SELECT rooliID, nimi, kuvaus FROM mahdollisetroolit";
    private static final String l_haeKaikkiRooliIDt = "SELECT rooliID FROM mahdollisetroolit";
    private static final String l_lisaaMahdollinenRooli = "INSERT INTO mahdollisetroolit (rooliID, nimi, kuvaus) VALUES (?, ?, ?)";
    private static final String l_paivitaMahdollinenRooli = "UPDATE mahdollisetroolit "
            + "SET nimi = ?, kuvaus = ?"
            + "WHERE rooliID = ?";
    //Työroolit
    private static final String l_haeTyontekijanRooli = 
            "SELECT työrooli.rooliID, mahdollisetroolit.rooliID, mahdollisetroolit.nimi, työrooli.kuvaus "
            + "FROM työrooli JOIN mahdollisetroolit "
            + "ON mahdollisetroolit.rooliID = työrooli.rooliID "
            + "JOIN työntekijä ON työrooli.työntekijäID = työntekijä.työntekijäID "
            + "JOIN työpaikka ON työrooli.työpaikkaID = työpaikka.työpaikkaID "
            + "WHERE työntekijä.työntekijäID = ? AND työpaikka.työpaikkaID = ?";
    private static final String l_haeTyontekijanKaikkiRoolit = 
            "SELECT työrooli.rooliID, mahdollisetroolit.rooliID, mahdollisetroolit.nimi, työrooli.kuvaus, työpaikka.työpaikkaID, työpaikka.nimi "
            + "FROM työrooli JOIN mahdollisetroolit "
            + "ON työrooli.rooliID = mahdollisetroolit.rooliID "
            + "JOIN työntekijä ON työrooli.työntekijäID = työntekijä.työntekijäID "
            + "JOIN työpaikka ON työrooli.työpaikkaID = työpaikka.työpaikkaID "
            + "WHERE työntekijä.työntekijäID = ?";
    private static final String l_lisaaTyontekijanRooli = "INSERT INTO työrooli (työntekijäID, työpaikkaID, rooliID, kuvaus) VALUES (?, ?, ?, ?)";
    private static final String l_paivitaTyontekijanRooli = "UPDATE työrooli SET rooliID = ? WHERE työntekijäID = ? AND työpaikkaID = ?";
    private static final String l_poistaTyontekijanRooli = "DELETE FROM työrooli WHERE työntekijäID = ? AND työpaikkaID = ?";
    private static final String l_poistaKaikkiTyopaikanRoolit ="DELETE FROM työrooli WHERE työpaikkaID = ?";
    private static final String l_paivitaTyoroolinKuvaus = "UPDATE työrooli SET kuvaus = ? WHERE työntekijäID = ? AND työpaikkaID = ?";
    //Työajat
    private static final String l_haeTyoajat = "SELECT ma_alku, ma_loppu, ti_alku, ti_loppu, ke_alku, ke_loppu, to_alku, to_loppu, pe_alku, pe_loppu, la_alku, la_loppu, su_alku, su_loppu FROM työajat WHERE työntekijäID = ?";
    private static final String l_paivitaTyoajat = "REPLACE työajat SET "
            + "työntekijäID = ?, "
            + "ma_alku = ?, ma_loppu = ?, "
            + "ti_alku = ?, ti_loppu = ?, "
            + "ke_alku = ?, ke_loppu = ?, "
            + "to_alku = ?, to_loppu = ?, "
            + "pe_alku = ?, pe_loppu = ?, "
            + "la_alku = ?, la_loppu = ?, "
            + "su_alku = ?, su_loppu = ?";
    
    // yhteys, jonka kautta tietokantaa käsitellään.
    private final Connection yhteys;
    
    /** Luo uuden yhteyden. Jos virheitä ei tapahdu, yhteys on muodostettu ja se pysyy auki. Ohjelmaa suljettaessa pitää muistaa käyttää suljeYhteys-metodia yhteyden sulkemiseksi.
     * @param kayttaja Käyttäjätunnus, jolla yhdistetään
     * @param salasana Salasana, jota käytetään yhdistäessä
     * @throws tiedot.TietokantaVirhe Jos tietokantaan ei saatu yhteyttä
     * @throws java.lang.ClassNotFoundException Jos tietokanta-ajuria ei löydy tai sitä ei voitu alustaa (virheellinen asennus)
     */
    public Yhteys(String kayttaja, String salasana) throws TietokantaVirhe, ClassNotFoundException {
        ajuri = Asetukset.getAjuri();
        url = Asetukset.getOsoite();
        try {
            Class.forName(ajuri).newInstance();
            yhteys = DriverManager.getConnection(url, kayttaja, salasana);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e);
            throw new ClassNotFoundException("Virheellinen asennus: Tietokanta-ajuria ei löydy");
        }
        catch (SQLException e) {
            System.out.println(e);
            throw new TietokantaVirhe(e, "Virhe tietokantaan yhdistäessä\n(Virhekoodi: "+e.getSQLState()+")");
        }
    }
    /** Sulkee yhteyden. */
    public void suljeYhteys() {
        try {
            yhteys.close();
        } catch (SQLException ex) {
            // Jos yhteyttä ei voitu sulkea, ei tehdä mitään
        }
    }
    /** Hakee kaikki mahdolliset työpaikat tietokannasta
     * @return Lista työpaikoista
     * @throws tiedot.TietokantaVirhe Jos yhteyttä ei voitu muodostaa*/
    public List<Tyopaikka> getTyopaikat() throws TietokantaVirhe {
        List<Tyopaikka> l = new ArrayList<>();
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyopaikat)){
            ResultSet vastaus = lause.executeQuery();
            while (vastaus.next()) {
                l.add(new Tyopaikka(vastaus.getString("työpaikkaID"), vastaus.getString("nimi")));
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Tyopaikkalistaa ei voitu luoda\n(Virhekoodi: "+ex.getSQLState()+")");
        }
        return l;
    }
    /** Hakee yhden työpaikan tiedot.
     * @param t Työpaikka, jonka tiedot halutaan.
     * @return Työpaikan tiedot.
     * @throws tiedot.TietokantaVirhe Jos tietoja ei voitu hakea. */
    public TyopaikkaTiedoilla getTyopaikanTiedot(Tyopaikka t) throws TietokantaVirhe {
        TyopaikkaTiedoilla tiedot = null;
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyopaikanTiedot)) {
            lause.setString(1, t.id);
            ResultSet vastaus = lause.executeQuery();
            vastaus.next();
            tiedot = new TyopaikkaTiedoilla(
                    vastaus.getString("työpaikkaID"),
                    vastaus.getString("nimi"),
                    vastaus.getString("osoite"),
                    vastaus.getString("aukioloajat"),
                    vastaus.getString("arvioituvalmistumisaika")
            );
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Tyopaikan tietoja ei voitu hakea");
        }
        return tiedot;
    }
    /** Hakee seuraavan vapaan työpaikan ID:n.
     * @return Seuraava vapaa ID. Jos tietoja ei voitu hakea, palauttaa ty00.
     */
    public String getSeuraavaTyopaikkaID() {
        String pohja = "ty"; //ID:n aloitusmerkki
        String muoto = "%02d"; // numeroiden muoto eli nollien määrä. // numero, josta ID-etsiminen aloitetaan
        int lisays = 1; // ID alkaa normaalisti nollasta. Tällä voi aloittaa jostain paremmasta numerosta.
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeKaikkiTyopaikkaIDt)){
            ResultSet vastaus = lause.executeQuery();
            Set<String> list = new TreeSet<>(); // tee lista ja lisää siihen kaikki ID:t
            while (vastaus.next()) {
                list.add(vastaus.getString("työpaikkaID"));
            }
            int i = 0;
            for ( ; i < list.size() ; i++){
                String kID = pohja + String.format(muoto, i + lisays);// ID, jossa pohja ja kolme numeroa (t000, t001 jne)
                if (!list.contains(kID)) return kID; // Jos ID oli vapaa, palauta se
            }
            return pohja + String.format(muoto, i + lisays); // jos väleissä ei ollut tyhjää, palauta viimesin
        } catch (SQLException ex) {
            System.out.println(ex);
            return "ty00";
        }
    }
    /** Lisää uusi työpaikka tietokantaan.
     * @param t Työpaikka, joka lisätään.
     * @throws tiedot.TietokantaVirhe Jos lisäys ei onnistunut. */
    public void lisaaTyopaikka(TyopaikkaTiedoilla t) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_lisaaTyopaikka)) {
            lause.setString(1, t.id);
            lause.setString(2, t.nimi);
            lause.setString(3, t.osoite);
            lause.setString(4, t.tyoajat);
            lause.setString(5, t.valmistumis);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Tyopaikkaa ei voitu lisätä");
        }
    }
    /** Päivitä työpaikan tiedot tietokantaan.
     * @param t Työpaikan tiedot.
     * @throws tiedot.TietokantaVirhe Jos tietoja ei voitu päivittää. */
    public void setTyopaikanTiedot(TyopaikkaTiedoilla t) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_paivitaTyopaikanTiedot)) {
            lause.setString(1, t.nimi);
            lause.setString(2, t.osoite);
            lause.setString(3, t.tyoajat);
            lause.setString(4, t.valmistumis);
            lause.setString(5, t.id);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työpaikan tietoja ei voitu päivittää");
        }
    }
    /** Poistaa työpaikan ja kaikki siihen liittyvät työroolit.
     * @param t Työpaikka, joka poistetaan.
     * @throws TietokantaVirhe Jos työpaikkaa ei voitu poistaa.
     */
    public void poistaTyopaikka(Tyopaikka t) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_poistaKaikkiTyopaikanRoolit)) {
            lause.setString(1, t.id);
            lause.executeUpdate();
            try (PreparedStatement lause2 = yhteys.prepareStatement(l_poistaTyopaikka)) {
                lause2.setString(1, t.id);
                lause2.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työpaikkaa ei voitu poistaa");
        }
    }
    /** Hakee tietyn työpaikan työntekijät.
     * @param t Työpaikka, jonka työntekijät haetaan.
     * @return Lista henkilöistä
     * @throws tiedot.TietokantaVirhe Jos tietoja ei voitu hakea 
     */
    public List<Henkilo> getTyontekijat(Tyopaikka t) throws TietokantaVirhe {
        List<Henkilo> l = new ArrayList();
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyopaikanTyontekijat)) {
            lause.setString(1, t.id);
            ResultSet vastaus = lause.executeQuery();
            while (vastaus.next()) {
                l.add(new Henkilo(
                        vastaus.getString("työntekijäID"),
                        vastaus.getString("etunimi"),
                        vastaus.getString("sukunimi")
                ));
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työntekijälistaa ei voitu luoda");
        }
        return l;
    }
    /** Hakee kaikki tietokannassa olevat työntekijät
     * @return Lista, jossa työntekijät
     * @throws tiedot.TietokantaVirhe Jos tietoja ei voitu hakea   
     */
    public List<Henkilo> getKaikkiTyontekijat() throws TietokantaVirhe {
        List<Henkilo> l = new ArrayList();
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeKaikkiTyontekijat)) {
            ResultSet vastaus = lause.executeQuery();
            while (vastaus.next()) {
                l.add(new Henkilo(
                        vastaus.getString("työntekijäID"),
                        vastaus.getString("etunimi"),
                        vastaus.getString("sukunimi")
                ));
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työntekijälistaa ei voitu luoda\n(Virhekoodi: "+ex.getSQLState()+")");
        }
        return l;
    }
    /** Hakee kaikki työntekijät, jotka ei kuulu tiettyyn työpaikkaan.
     * @param t Työpaikka, johon kuulumattomia etsitään.
     * @return Lista henkilöistä.
     * @throws tiedot.TietokantaVirhe Mikäli tietoja ei voitu hakea.
     */
    public List<Henkilo> getTyopaikkaanKuulumattomat(Tyopaikka t) throws TietokantaVirhe{
        List<Henkilo> lista = new ArrayList<>();
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyopaikanUlkopuolisetTyontekijat)){
            lause.setString(1, t.id);
            ResultSet vastaus = lause.executeQuery();
            while (vastaus.next()) {
                lista.add(new Henkilo(
                    vastaus.getString("työntekijäID"),
                    vastaus.getString("etunimi"),
                    vastaus.getString("sukunimi")
                ));
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työntekijöiden hakemisessa tapahtui virhe");
        }
        return lista;
    }
    /** Hakee työntekijän tiedot.
     * @param h Henkilö, jonka tiedot haetaan
     * @return Työntekijän tiedot HenkiloTiedot-objektissa
     * @throws tiedot.TietokantaVirhe Jos tietoja ei voitu hakea */
    public HenkiloTiedot getTyontekijanTiedot(Henkilo h) throws TietokantaVirhe {
        HenkiloTiedot tiedot = null;
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyontekijanTiedot)){
            lause.setString(1, h.id);
            ResultSet vastaus = lause.executeQuery();
            vastaus.next();
            tiedot = new HenkiloTiedot(
                    vastaus.getString("työntekijäID"),
                    vastaus.getString("etunimi"),
                    vastaus.getString("sukunimi"),
                    vastaus.getString("osoite"),
                    vastaus.getString("puhelin"),
                    vastaus.getDate("syntymäaika"),
                    vastaus.getString("koulutus"),
                    vastaus.getDate("aloitusaika")
            );
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            throw new TietokantaVirhe(ex, "Tietoja ei löytynyt\n(Virhekoodi: "+ex.getSQLState()+")");
        }
        return tiedot;
    }
    /** Hae yksittäisen työntekijän työajat.
     * @param h Henkilo, jonka työajat halutaan
     * @return Työajat Tyoaika-luokan oliona
     * @throws tiedot.TietokantaVirhe 
     */
    public Tyoajat getTyoajat(Henkilo h) throws TietokantaVirhe{
        return getTyoajat(h.id);
    }
    /** Hae yksittäisen työntekijän työajat. Jos henkilön työaikoja ei ole tietokannassa, palautetaan työajat jossa kaikki työajat on null-arvoja.
     * @param s Henkilon ID, jonka työajat halutaan
     * @return Työajat Tyoaika-luokan oliona.
     * @throws tiedot.TietokantaVirhe 
     */
    public Tyoajat getTyoajat(String s) throws TietokantaVirhe {
        Tyoajat tyoajat = null;
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyoajat)) {
            lause.setString(1, s);
            ResultSet vastaus = lause.executeQuery();
            if (vastaus.next()){
                tyoajat = new Tyoajat(
                    vastaus.getTime("ma_alku"),
                    vastaus.getTime("ma_loppu"),
                    vastaus.getTime("ti_alku"),
                    vastaus.getTime("ti_loppu"),
                    vastaus.getTime("ke_alku"),
                    vastaus.getTime("ke_loppu"),
                    vastaus.getTime("to_alku"),
                    vastaus.getTime("to_loppu"),
                    vastaus.getTime("pe_alku"),
                    vastaus.getTime("pe_loppu"),
                    vastaus.getTime("la_alku"),
                    vastaus.getTime("la_loppu"),
                    vastaus.getTime("su_alku"),
                    vastaus.getTime("su_loppu")
                );
            }
            else tyoajat = new Tyoajat(new String[14]);
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työaikojen hakeminen ei onnistunut");
        }
        return tyoajat;
    }
    /** Päivitä yksittäisen työntekijän työajat. Jos työaikoja ei vielä ole tietokannassa, tehdään uudet tiedot.
     * @param h Henkilo, jonka työajat päivitetään.
     * @param t Uudet työajat.
     * @throws tiedot.TietokantaVirhe Mikäli työaikojen päivittäminen ei onnistu.
     */
    public void setTyoajat(Henkilo h, Tyoajat t) throws TietokantaVirhe{
        setTyoajat(h.id, t);
    }
    /** Päivitä yksittäisen työntekijän työajat. Jos työaikoja ei vielä ole tietokannassa, tehdään uudet tiedot.
     * @param s ID, jonka työajat päivitetään.
     * @param t Uudet työajat.
     * @throws tiedot.TietokantaVirhe Mikäli työaikojen päivittäminen ei onnistu.
     */
    public void setTyoajat(String s, Tyoajat t) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_paivitaTyoajat)) {
            int i = 1;
            lause.setString(i, s);
            for (Time aika : t.getAjat()) {
                i++;
                lause.setTime(i, aika);
            }
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työaikojen päivittäminen ei onnistunut");
        }
    }
    /** Muuttaa yhden työntekijän tietoja.
     * @param h Tiedot, jotka päivitetään tietokantaan. Korvaa edelliset samalla ID:llä olleet tiedot
     * @throws tiedot.TietokantaVirhe Jos tietoja ei voitu päivittää*/
    public void setTyontekijanTiedot(HenkiloTiedot h) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_paivitaTyontekijanTiedot)) {
            lause.setString(1, h.etunimi);
            lause.setString(2, h.sukunimi);
            lause.setString(3, h.osoite);
            lause.setString(4, h.puhelin);
            lause.setDate(5, h.syntyma);
            lause.setString(6, h.koulutus);
            lause.setDate(7, h.aloitusaika);
            lause.setString(8, h.id);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Virhe tietokantaan yhdistäessä");
        }
    }
    /** Lisää uusi henkilö tietokantaan. Jos aloitusaikaa on null, pistetään tämänhetkinen aika siihen ruutuun.
     * @param h Henkilö, joka lisätään tietokantaan.
     * @throws tiedot.TietokantaVirhe Mikäli henkilöä lisättäessä tapahtui virhe
     */
    public void lisaaTyontekija(HenkiloTiedot h) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_lisaaTyontekija)) {
            lause.setString(1, h.id);
            lause.setString(2, h.etunimi);
            lause.setString(3, h.sukunimi);
            lause.setString(4, h.osoite);
            lause.setString(5, h.puhelin);
            lause.setDate(6, h.syntyma);
            lause.setString(7, h.koulutus);
            if (h.aloitusaika == null) lause.setDate(8, new Date(System.currentTimeMillis()));
            else lause.setDate(8, h.aloitusaika);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Virhe henkilöä lisättäessä");
        }
    }
    /** Hakee seuraavan vapaan ID:n. Jos sellaista ei löytynyt tai yhteyttä tietokantaan ei voitu muodostaa, palautetaan t000.
     * @return Seuraava vapaa ID.
     */
    public String getSeuraavaTyontekijaID() {
        String pohja = "t"; //ID:n aloitusmerkki
        String muoto = "%03d"; // numeroiden muoto eli nollien määrä.
        int i = 0; // numero, josta ID-etsiminen aloitetaan
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeKaikkiTyontekijaIDt)){
            ResultSet vastaus = lause.executeQuery();
            Set<String> list = new TreeSet<>(); // tee lista ja lisää siihen kaikki ID:t
            while (vastaus.next()) {
                list.add(vastaus.getString("työntekijäID"));
            }
            for ( ; i < list.size() ; i++){
                String kID = pohja + String.format(muoto, i);// ID, jossa pohja ja kolme numeroa (t000, t001 jne)
                if (!list.contains(kID)) return kID; // Jos ID oli vapaa, palauta se
            }
            return pohja + String.format(muoto, i); // jos vapaita ei ollut, palauta oletus
        } catch (SQLException ex) {
            System.out.println(ex);
            return "t000";
        }
    }
    /** Hakee työntekijän työroolin.
     * @param h Henkilö, jonka roolia haetaan.
     * @param t Työpaikka, josta roolia haetaan.
     * @return Henkilön rooli työpaikalla.
     * @throws tiedot.TietokantaVirhe Jos tietoja ei ollut tai niitä ei voitu hakea
     */
    public HenkilonRooli getTyontekijanRooli(Henkilo h, Tyopaikka t) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyontekijanRooli)){
            lause.setString(1, h.id);
            lause.setString(2, t.id);
            ResultSet vastaus = lause.executeQuery();
            vastaus.next();
            return new HenkilonRooli(
                    vastaus.getString("työrooli.rooliID"), 
                        new MahdollinenRooli( 
                        vastaus.getString("mahdollisetroolit.rooliID"), 
                        vastaus.getString("nimi")), 
                    vastaus.getString("kuvaus"));
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Henkilön työroolia ei voitu hakea");
        }
    }

    /** Hakee yksittäisen työntekijän kaikki roolit kaikilla työpaikoilla.
     * @param h Henkilö, jonka tiedot haetaan.
     * @return Lista, jossa kaikki henkilön roolit
     * @throws TietokantaVirhe Jos rooleja ei voitu hakea.
     */
    public List<HenkilonRooliTyopaikalla> getTyontekijanKaikkiRoolit(Henkilo h) throws TietokantaVirhe{
        List<HenkilonRooliTyopaikalla> lista = new ArrayList<>();
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeTyontekijanKaikkiRoolit)){
            lause.setString(1, h.id);
            ResultSet vastaus = lause.executeQuery();
            while (vastaus.next()) {
                lista.add(new HenkilonRooliTyopaikalla(
                        vastaus.getString("työrooli.rooliID"),
                        new MahdollinenRooli(
                            vastaus.getString("mahdollisetroolit.rooliID"),
                            vastaus.getString("mahdollisetroolit.nimi")),
                        vastaus.getString("kuvaus"),
                        new Tyopaikka(
                                vastaus.getString("työpaikkaID"),
                                vastaus.getString("työpaikka.nimi"))
                ));
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työntekijän rooleja ei voitu hakea");
        }
        return lista;
    }
    /** Hakee kaikki mahdolliset roolit tietokannasta.
     * @return Lista, jossa jokainen rooli ja sen kuvaus.
     * @throws TietokantaVirhe Jos roolien hakeminen ei onnistu.
     */
    public List<MahdollinenRooliKuvauksella> getKaikkiMahdollisetRoolit() throws TietokantaVirhe {
        List<MahdollinenRooliKuvauksella> lista = new ArrayList<>();
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeMahdollisetRoolit)) {
            ResultSet vastaus = lause.executeQuery();
            while (vastaus.next()){
                lista.add(new MahdollinenRooliKuvauksella(
                        vastaus.getString("rooliID"),
                        vastaus.getString("nimi"),
                        vastaus.getString("kuvaus")
                ));
            }
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Roolien hakeminen ei onnistunut");
        }
        return lista;
    }
    /** Hakee seuraavan mahdollisen roolin ID:n. Jos rooleja ei voitu hakea, palauttaa tr00.
     * @return Seuraava vapaa roolin ID.
     */
    public String getSeuraavaRooliID() {
        String pohja = "tr"; //ID:n aloitusmerkki
        String muoto = "%02d"; // numeroiden muoto eli nollien määrä.
        int i = 1; // numero, josta ID-etsiminen aloitetaan
        try (PreparedStatement lause = yhteys.prepareStatement(l_haeKaikkiRooliIDt)){
            ResultSet vastaus = lause.executeQuery();
            Set<String> list = new TreeSet<>(); // tee lista ja lisää siihen kaikki ID:t
            while (vastaus.next()) {
                list.add(vastaus.getString("rooliID"));
            }
            for ( ; i < list.size() ; i++){
                String kID = pohja + String.format(muoto, i);// ID, jossa pohja ja kolme numeroa (t000, t001 jne)
                if (!list.contains(kID)) return kID; // Jos ID oli vapaa, palauta se
            }
            return pohja + String.format(muoto, i); // jos vapaita ei ollut, palauta oletus
        } catch (SQLException ex) {
            System.out.println(ex);
            return "tr00";
        }
    }
    /** Lisää uuden mahdollisen roolin tietokantaan.
     * @param k Rooli, joka lisätään.
     * @throws TietokantaVirhe Jos roolia ei voitu lisätä.
     */
    public void lisaaMahdollinenRooli(MahdollinenRooliKuvauksella k) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_lisaaMahdollinenRooli)) {
            lause.setString(1, k.id);
            lause.setString(2, k.nimi);
            lause.setString(3, k.kuvaus);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Uuden roolin lisäääminen ei onnistunut");
        }
    }
    /** Päivittää mahdollisen roolin nimen ja kuvaustekstin tietokantaan.
     * @param k Rooli, joka päivitetään.
     * @throws TietokantaVirhe Jos päivitys ei onnistunut.
     */
    public void setMahdollinenRooli(MahdollinenRooliKuvauksella k) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_paivitaMahdollinenRooli)) {
            lause.setString(1, k.nimi);
            lause.setString(2, k.kuvaus);
            lause.setString(3, k.id);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Roolin päivittäminen ei onnistunut");
        }
    }
    /** Lisää työntekijälle rooli tiettyn työpaikkaan liittyen.
     * @param h Henkilö, jolle rooli lisätään.
     * @param t Työpaikka, johon henkilön rooli kuuluu.
     * @param r Rooli, joka henkilölle lisätään.
     * @param kuvaus Tämän työtekijän tehtävien kuvausteksti.
     * @throws tiedot.TietokantaVirhe Mikäli lisääminen ei onnistunut.
     */
    public void lisaaTyontekijanRooli(Henkilo h, Tyopaikka t, MahdollinenRooli r, String kuvaus) throws TietokantaVirhe{
        try (PreparedStatement lause = yhteys.prepareStatement(l_lisaaTyontekijanRooli)){
            lause.setString(1, h.id);
            lause.setString(2, t.id);
            lause.setString(3, r.id);
            lause.setString(4, kuvaus);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Työpaikkaa ei voitu päivittää");
        }
    }
    /** Vaihda työntekijn työroolia tietyssä työpaikassa.
     * @param h Henkilö, jonka roolia muutetaan.
     * @param t Työpaikka, johon roolimuutos liittyy.
     * @param r Työntekijälle määritettävä uusi rooli.
     * @throws tiedot.TietokantaVirhe Mikäli roolin vaihtaminen ei onnistunut.
     */
    public void setTyontekijanRooli(Henkilo h, Tyopaikka t, MahdollinenRooli r) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_paivitaTyontekijanRooli)){
               lause.setString(1, r.id);
               lause.setString(2, h.id);
               lause.setString(3, t.id);
               lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Roolia ei voitu vaihtaa");
        }
    }
    /** Muuttaa olemassaolevan työroolin kuvaustekstiä.
     * @param h Henkilö, jonka kuvausta muutetaan.
     * @param t Työpaikka, johon liittyvän roolin kuvaustekstiä muutetaan.
     * @param kuvaus Uusi kuvausteksti.
     * @throws tiedot.TietokantaVirhe Jos tekstin muuttaminen ei onnistunut.
     */
    public void setTyontekijanRooliTeksti(Henkilo h, Tyopaikka t, String kuvaus) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_paivitaTyoroolinKuvaus)){
            lause.setString(1, kuvaus);
            lause.setString(2, h.id);
            lause.setString(3, t.id);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Kuvaustekstiä ei voitu päivittää");
        }
    }
    /** Poistaa työntekijän työroolin.
     * @param h Henkilö, jonka rooli poistetaan.
     * @param t Työpaikka, johon liittyvä rooli poistetaan.
     * @throws TietokantaVirhe Jos poistaminen ei onnistunut.
     */
    public void poistaTyontekijanRooli(Henkilo h, Tyopaikka t) throws TietokantaVirhe {
        try (PreparedStatement lause = yhteys.prepareStatement(l_poistaTyontekijanRooli)){
            lause.setString(1, h.id);
            lause.setString(2, t.id);
            lause.executeUpdate();
        } catch (SQLException ex) {
            throw new TietokantaVirhe(ex, "Roolia ei voitu poistaa");
        }
    }
}