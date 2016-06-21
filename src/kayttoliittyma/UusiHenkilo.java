package kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Arrays;
import javax.swing.*;
import tiedot.HenkiloTiedot;
import tiedot.TietokantaVirhe;
import tiedot.Yhteys;

/** Uusien työntekijöiden lisäysikkuna. */
public class UusiHenkilo extends KayttoliittymanOsa {
    //Yhteys tietokantaan
    private final Yhteys yhteys;
    
    // Kentät ja niiden selitteet
    // t_ = teksti / tunniste
    // k_ = kenttä
    private final JLabel t_nimi = new JLabel("Nimi");
    private final JLabel t_id = new JLabel("ID:");
    private final JTextField k_id = new JTextField(3);
    private final JTextField k_etunimi = new JTextField(10);
    private final JTextField k_sukunimi = new JTextField(10);
    private final JLabel t_puhnro = new JLabel("Puhelinnumero");
    private final JTextField k_puhnro = new JTextField(20);
    private final JLabel t_osoi = new JLabel("Osoite");
    private final JTextField k_osoi = new JTextField(20);
    private final JLabel t_koulut = new JLabel("Koulutus");
    private final JTextField k_koulut = new JTextField(20);
    private final JLabel t_synt = new JLabel("Syntymäaika (pp.kk.vvvv)");
    private final JTextField k_syntp = new JTextField(3);
    private final JLabel t_pist1 = new JLabel(".");
    private final JTextField k_syntk = new JTextField(3);
    private final JLabel t_pist2 = new JLabel(".");
    private final JTextField k_syntv = new JTextField(5);
    
    //lopun toimintonäppäimet
    private final JButton lisaa = new JButton("Lisää henkilö");
    private final JButton tyhj = new JButton("Tyhjennä");
    
    /** Luo uusi lisäysikkuna
     * @param y Yhteys tietokantaan.
     */
    public UusiHenkilo(Yhteys y) {
        yhteys = y;
        
        GroupLayout asettelu = new GroupLayout(this);
        asettelu.setAutoCreateGaps(true);
        asettelu.setHorizontalGroup(
            asettelu.createParallelGroup()
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(t_nimi)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 0, Short.MAX_VALUE)
                .addComponent(t_id)
                .addComponent(k_id)
            )
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(k_etunimi)
                .addComponent(k_sukunimi)
            )
            .addComponent(t_puhnro)
            .addComponent(k_puhnro)
            .addComponent(t_osoi)
            .addComponent(k_osoi)
            .addComponent(t_koulut)
            .addComponent(k_koulut)
            .addComponent(t_synt)
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(k_syntp)
                .addComponent(t_pist1)
                .addComponent(k_syntk)
                .addComponent(t_pist2)
                .addComponent(k_syntv)
            )
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(lisaa)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 0, Short.MAX_VALUE)
                .addComponent(tyhj)
            )
        );
        asettelu.setVerticalGroup(
            asettelu.createSequentialGroup()
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(t_nimi)
                .addComponent(t_id)
                .addComponent(k_id)
            )
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(k_etunimi)
                .addComponent(k_sukunimi)
            )
            .addComponent(t_puhnro)
            .addComponent(k_puhnro)
            .addComponent(t_osoi)
            .addComponent(k_osoi)
            .addComponent(t_koulut)
            .addComponent(k_koulut)
            .addComponent(t_synt)
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(k_syntp)
                .addComponent(t_pist1)
                .addComponent(k_syntk)
                .addComponent(t_pist2)
                .addComponent(k_syntv)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 0, Short.MAX_VALUE)
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(lisaa)
                .addComponent(tyhj)
            )
        );
        // Varmistetaan ettei kenttien kokoa venytetä vaan tyhjiä rakoja niiden välistä
        asettelu.linkSize(
                SwingConstants.VERTICAL,
                t_nimi,
                k_etunimi,
                k_sukunimi,
                t_puhnro,
                k_puhnro,
                k_osoi,
                k_koulut,
                k_syntp,
                k_syntv,
                k_syntk,
                k_id
        );
        this.setBorder(BorderFactory.createEmptyBorder(0, 30, 5, 30));
        this.setLayout(asettelu);
        tyhj.addActionListener(new Nappaimet());
        lisaa.addActionListener(new Nappaimet());
    }
    
    @Override
    public void tyhjenna() {
        k_etunimi.setText("");
        k_sukunimi.setText("");
        k_puhnro.setText("");
        k_osoi.setText("");
        k_koulut.setText("");
        k_syntp.setText("");
        k_syntk.setText("");
        k_syntv.setText("");
        k_id.setText(yhteys.getSeuraavaTyontekijaID());
    }
    
    private class Nappaimet implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            //tyhjennysnäppäin
            if (e.getSource() == tyhj) tyhjenna();
            //Henkilöiden lisääminen
            else if (e.getSource() == lisaa) {
                // Onko kaikki kentät täytetty?
                if (k_etunimi.getText().equals("") ||
                    k_sukunimi.getText().equals("") ||
                    k_puhnro.getText().equals("") ||
                    k_osoi.getText().equals("") ||
                    k_koulut.getText().equals("") ||
                    k_syntp.getText().equals("") ||
                    k_syntk.getText().equals("") ||
                    k_syntv.getText().equals("") ||
                    k_id.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Täytä ensin kaikki kentät", "Virhe", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //todo: lisää syntymä, aloitus, työajat
                
                try {
                    HenkiloTiedot h = new HenkiloTiedot(
                            k_id.getText(),
                            k_etunimi.getText(),
                            k_sukunimi.getText(),
                            k_osoi.getText(),
                            k_puhnro.getText(), 
                            Date.valueOf(k_syntv.getText() + "-" + k_syntk.getText() + "-" + k_syntp.getText()),
                            k_koulut.getText(),
                            null);
                    // yritä lisätä henkilö
                    yhteys.lisaaTyontekija(h);
                    JOptionPane.showMessageDialog(null, "Henkilö: " + h.toString() + "\non lisätty tietokantaan", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                    tyhjenna();
                } catch (TietokantaVirhe ex) {
                    //lisäys ei onnistunut, ilmoita
                    ex.naytaVirheIlmoitus(null, "Työntekijän lisääminen ei onnistunut");
                }
                catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Syntymäaika on väärää muotoa.\nTarkista että se on muodossa päivä-kuukausi-vuosi", "Virhe", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    
    }
}
