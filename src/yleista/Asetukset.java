package yleista;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Ohjelman käyttämät asetukset. Ohjelman käynnistyessä asetukset haetaan asetustiedostosta, mikäli mahdollista. Mikäli asetustiedostoa ei löytynyt, käytetään oletusasetuksia. */
public class Asetukset {
    private static final String ajuri = "ajuri";
    private static final String ajuri_sql = "com.mysql.jdbc.Driver";
    private static final String ajuri_derby = "org.apache.derby.jdbc.EmbeddedDriver";
    
    private static final String osoite = "osoite";
    private static final String osoite_alku = "jdbc:mysql://";
    private static final String osoite_sql = "jdbc:mysql://localhost/henkilotietokanta";
    private static final String osoite_derby = "jdbc:derby:henkilotietokanta";
    
    private static final String tiedosto = "asetukse.t";
    
    private Asetukset(){}
    private static final Map<String, String> map;
    
    static {
        File f = new File(tiedosto);
        Map temp;
        try (ObjectInputStream r = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))){
            temp = (Map<String, String>) r.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            temp = new HashMap<>();
        }
        map = (Map<String, String>) Collections.synchronizedMap(temp);
    }
    /** Aseta käytettäväksi ajuriksi joko sql- tai derby-ajuri.
     * @param sql Tosi = SQL, Epätosi = Derby.
     */
    public static void setAjuri(boolean sql) {
        if (sql) map.put(ajuri, ajuri_sql);
        else map.put(ajuri, ajuri_derby);
    }
    /** Palauttaa käytetyn ajurin.
     * @return Ajurin luokan nimi.
     */
    public static String getAjuri(){
        return map.getOrDefault(ajuri, ajuri_sql);
    }
    /** Palauttaa käytetäänkö SQL- vai Derby-tietokantaa.
     * @return Tosi = SQL, Epätosi = Derby.
     */
    public static boolean getOnkoSQL() {
        return (map.get(ajuri) == null ? true : map.get(ajuri).equals(ajuri_sql));
    }
    /** Aseta tietokannan osoite kokonaisuudessaan. Muodossa [ajurin etuliite]:[osoite]/[tietokanta].
     * @param uusiosoite Uusi osoite.
     */
    public static void setOsoite(String uusiosoite) {
        map.put(osoite, uusiosoite);
    }
    /** Aseta tietokannan osoite. Muodossa [osoite]/[tietokanta].
     * @param uusiosoite Uusi osoite.
     */
    public static void setOsoiteTekstikentasta(String uusiosoite){
        map.put(osoite, osoite_alku + uusiosoite);
    }
    /** Palauttaa tietokannan osoitteen. Muodossa [ajurin etuliite]:[osoite]/[tietokanta].
     * @return Tietokannan osoite.
     */
    public static String getOsoite(){
        if (map.get(ajuri) != null) if (map.get(ajuri).equals(ajuri_derby)) return osoite_derby;
        return map.getOrDefault(osoite, osoite_sql);
    }
    /** Palauttaa tietokannan osoitteen. Muodossa [osoite]/[tietokanta].
     * @return Tietokannan osoite.
     */
    public static String getOsoiteTekstikentalle(){
        return map.getOrDefault(osoite, osoite_sql).substring(13);
    }
    /** Tallentaa muutokset kovalevylle asetustiedostoon.
     * @return Onnistuiko tallentaminen.
     */
    public static boolean tallenna() {
        File f = new File(tiedosto);
        
        try (ObjectOutputStream r = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)))){
            r.writeObject(map);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}