package kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import tiedot.TietokantaVirhe;
import tiedot.Tyopaikka;
import tiedot.TyopaikkaTiedoilla;
import tiedot.Yhteys;

/** Työpaikkojen muokkausikkuna. */
public class TyopaikkaMuokkaus extends KayttoliittymanOsa {
    private final Yhteys yhteys;
    
    private boolean muokkaus_kaytossa = false;
    private boolean luonti_kaytossa = false;
    
    private final DefaultComboBoxModel<Tyopaikka> tyopaikkalista = new DefaultComboBoxModel<>();
    private final JComboBox<Tyopaikka> tyopaikat = new JComboBox<>(tyopaikkalista);
    private final JButton luo = new JButton("Uusi");
    
    private final JLabel t_nimi = new JLabel("Nimi");
    private final JTextField k_nimi = new JTextField(15);
    private final JLabel t_id = new JLabel("ID");
    private final JTextField k_id = new JTextField(5);
    private final JLabel t_osoite = new JLabel("Osoite");
    private final JTextField k_osoite = new JTextField(20);
    private final JLabel t_aukiolo = new JLabel("Aukioloajat");
    private final JTextField k_aukiolo = new JTextField(20);
    private final JLabel t_arvio = new JLabel("Arvioitu valmistumisaika");
    private final JTextField k_arvio = new JTextField(20);
    
    private final JButton muokkaa = new JButton("Muokkaa");
    private final JButton peruuta = new JButton("Poista");
    
    /** Luo uusi muokkausikkuna
     * @param y Yhteys tietokantaan. 
     */
    public TyopaikkaMuokkaus(Yhteys y) {
        yhteys = y;
        k_nimi.setEditable(false);
        k_id.setEditable(false);
        k_osoite.setEditable(false);
        k_aukiolo.setEditable(false);
        k_arvio.setEditable(false);
        //peruuta.setVisible(false);
        
        GroupLayout asettelu = new GroupLayout(this);
        asettelu.setAutoCreateGaps(true);
        asettelu.setHorizontalGroup(
            asettelu.createParallelGroup()
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(tyopaikat)
                .addComponent(luo)
            )
            .addGroup(
                asettelu.createSequentialGroup()
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(t_nimi)
                    .addComponent(k_nimi)
                )
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(t_id)
                    .addComponent(k_id)
                )
            )
            .addComponent(t_osoite)
            .addComponent(k_osoite)
            .addComponent(t_aukiolo)
            .addComponent(k_aukiolo)
            .addComponent(t_arvio)
            .addComponent(k_arvio)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(muokkaa)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(peruuta)
            )
        );
        asettelu.setVerticalGroup(
            asettelu.createSequentialGroup()
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(tyopaikat, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(luo)
            )
            .addGroup(
                asettelu.createParallelGroup()
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(t_nimi)
                    .addComponent(k_nimi)
                )
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(t_id)
                    .addComponent(k_id)
                )
            )
            .addComponent(t_osoite)
            .addComponent(k_osoite)
            .addComponent(t_aukiolo)
            .addComponent(k_aukiolo)
            .addComponent(t_arvio)
            .addComponent(k_arvio)
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(muokkaa)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(peruuta)
            )
        );
        asettelu.linkSize(SwingConstants.VERTICAL, t_nimi, t_id, k_nimi, k_osoite, k_aukiolo, k_arvio, k_id);
        this.setLayout(asettelu);
        luo.addActionListener(new Toiminnot());
        peruuta.addActionListener(new Toiminnot());
        muokkaa.addActionListener(new Toiminnot());
        tyopaikat.addActionListener(new ListaToiminto());
    }
    
    @Override
    public void paivita() {
        tyopaikkalista.removeAllElements();
        try {
            for (Tyopaikka t : yhteys.getTyopaikat()) {
                tyopaikkalista.addElement(t);
            }
        } catch (TietokantaVirhe ex) {
            ex.naytaVirheIlmoitus(this, "Työpaikkalistan päivittäminen epäonnistui");
        }
    }
    private void tyhjennaKentat() {
        k_nimi.setText(null);
        k_id.setText(null);
        k_osoite.setText(null);
        k_aukiolo.setText(null);
        k_arvio.setText(null);
    }
    private boolean kentatTaytetty() {
        if (k_nimi.getText().equals("")) return false;
        if (k_id.getText().equals("")) return false;
        if (k_osoite.getText().equals("")) return false;
        if (k_aukiolo.getText().equals("")) return false;
        return !k_arvio.getText().equals("");
    }
    private void avaaKentat(boolean b) {
        k_nimi.setEditable(b);
        k_osoite.setEditable(b);
        k_aukiolo.setEditable(b);
        k_arvio.setEditable(b);
        tyopaikat.setEnabled(!b);
        luo.setEnabled(!b);
    }
    
    private class Toiminnot implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // kun käyttäjä haluaa luoda uuden työpaikan
            if (e.getSource() == luo ) {
                tyhjennaKentat();
                k_id.setText(yhteys.getSeuraavaTyopaikkaID());
                luonti_kaytossa = true;
                muokkaus_kaytossa = true;
                k_id.setEditable(true);
                peruuta.setText("Peruuta");
                muokkaa.setText("Luo");
                avaaKentat(true);
            }
            // kun käyttäjä haluaa muokata nykyistä valintaa
            else if (e.getSource() == muokkaa && !muokkaus_kaytossa) {
                muokkaus_kaytossa = true;
                avaaKentat(true);
                muokkaa.setText("Tallenna");
                peruuta.setText("Peruuta");
            }
            // käyttäjä luo uuden työpaikan annetuilla tiedoilla
            else if (e.getSource() == muokkaa && luonti_kaytossa) {
                if (!kentatTaytetty()) {
                    JOptionPane.showMessageDialog(null, "Täytä kentät ensin", "Virhe", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                TyopaikkaTiedoilla t = new TyopaikkaTiedoilla(k_id.getText(), k_nimi.getText(), k_osoite.getText(), k_aukiolo.getText(), k_arvio.getText());
                //yritä lähettää muutokset
                try {
                    yhteys.lisaaTyopaikka(t);
                    //jos onnistuu, poistu muokkaustilasta
                    peruuta.setText("Poista");
                    muokkaa.setText("Muokkaa");
                    k_id.setEditable(false);
                    avaaKentat(false);
                    muokkaus_kaytossa = false;
                    luonti_kaytossa = false;
                    //päivitä lista ja valitse juuri lisätty
                    paivita();
                    for (int i = 0; i < tyopaikkalista.getSize(); i++) 
                    if (tyopaikkalista.getElementAt(i).id.equals(t.id)){
                        tyopaikat.setSelectedIndex(i);
                        break;
                    }
                } catch (TietokantaVirhe ex) {
                    //jos ei, ilmoita
                    ex.naytaVirheIlmoitus(null, "Virhe työpaikkaa lisättäessä");
                }
            }
            // käyttäjä päivittää työpaikan tiedot
            else if (e.getSource() == muokkaa) {
                if (!kentatTaytetty()) {
                    JOptionPane.showMessageDialog(null, "Täytä kentät ensin", "Virhe", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //yritä lähettää muutokset
                try {
                    TyopaikkaTiedoilla t = new TyopaikkaTiedoilla(k_id.getText(), k_nimi.getText(), k_osoite.getText(), k_aukiolo.getText(), k_arvio.getText());
                    yhteys.setTyopaikanTiedot(t);
                    //jos onnistuu, poistu muokkaustilasta
                    peruuta.setText("Poista");
                    muokkaa.setText("Muokkaa");
                    avaaKentat(false);
                    muokkaus_kaytossa = false;
                } catch (TietokantaVirhe ex) {
                    //jos ei, ilmoita
                    ex.naytaVirheIlmoitus(null, "Virhe työpaikan tietoja päivittäessä");
                }
            }
            // käyttäjä peruuttaa muokkaamisen
            else if (e.getSource() == peruuta && muokkaus_kaytossa) {
                tyhjennaKentat();
                muokkaa.setText("Muokkaa");
                peruuta.setText("Poista");
                if (luonti_kaytossa) k_id.setEditable(false);
                tyopaikat.setEnabled(true);
                luo.setEnabled(true);
                k_id.setEditable(false);
                avaaKentat(false);
                // hae uudestaan valittu työpaikka
                int i = tyopaikat.getSelectedIndex();
                tyopaikat.setSelectedIndex(-1);
                tyopaikat.setSelectedIndex(i);
                luonti_kaytossa = false;
                muokkaus_kaytossa = false;
            }
            //käyttäjä haluaa poistaa työpaikan
            else if (e.getSource() == peruuta) {
                //tarkista onko mitään valittu
                Tyopaikka t = (Tyopaikka) tyopaikat.getSelectedItem();
                if (t == null) return;
                //jos on, kysy onko käyttäjä varma
                int i = JOptionPane.showConfirmDialog(null, 
                        "Oletko varma että haluat poistaa tämän työpaikan?\n" 
                        + "Kaikki tähän työpaikkaan littyvät työroolit poistuvat samalla.\n" 
                        + "Poistamista ei voi perua eikä menetettyjä tietoja saa takaisin.", 
                        "Varoitus", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.WARNING_MESSAGE);
                if (i != JOptionPane.YES_OPTION) return;
                //yritä poistaa ja ilmoita
                try {
                    yhteys.poistaTyopaikka(t);
                    JOptionPane.showMessageDialog(null, "Työpaikka on poistettu", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                    paivita();
                    
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Työpaikkaa ei voitu poistaa");
                }
            }
        }
    }
    private class ListaToiminto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Tyopaikka t = (Tyopaikka) tyopaikat.getSelectedItem();
            if (t == null) return;
            try {
                TyopaikkaTiedoilla tyo = yhteys.getTyopaikanTiedot(t);
                k_id.setText(tyo.id);
                k_nimi.setText(tyo.nimi);
                k_osoite.setText(tyo.osoite);
                k_aukiolo.setText(tyo.tyoajat);
                k_arvio.setText(tyo.valmistumis);
            } catch (TietokantaVirhe ex) {
                ex.naytaVirheIlmoitus(null, "Työpaikan tietoja ei voitu hakea");
            }
        }
    
    }
}
