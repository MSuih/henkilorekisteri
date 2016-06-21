package kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tiedot.Yhteys;
import javax.swing.*;
import tiedot.MahdollinenRooliKuvauksella;
import tiedot.TietokantaVirhe;

/** Roolien muokkausvalikko. Tästä muokataan mahdollisia rooleja joita käytetään työroolien pohjana. */
public class RoolienMuokkaus extends KayttoliittymanOsa {
    private final Yhteys yhteys; 
    
    private final JComboBox<MahdollinenRooliKuvauksella> roolit = new JComboBox<>();
    private final JLabel t_nimi = new JLabel("Nimi");
    private final JTextField k_nimi = new JTextField();
    private final JLabel t_id = new JLabel("ID");
    private final JTextField k_id = new JTextField();
    private final JLabel t_kuvaus = new JLabel("Roolin kuvaus");
    private final JTextArea k_kuvaus = new JTextArea(0, 2);
    private final JScrollPane kuvausscroll = new JScrollPane(k_kuvaus);
    
    private final JButton uusi = new JButton("Uusi");
    private final JButton tallenna = new JButton("Tallenna muutokset");
    private final JButton peruuta = new JButton("Peruuta");
    
    //luodaanko uutta roolia vaiko muokataanko olemassaolevaa
    private boolean luonti_kaytossa = false;
    
    /** Luo uusi muokkausvalikko.
     * @param y Yhteys tietokantaan.
     */
    public RoolienMuokkaus(Yhteys y) {
        yhteys = y;
        k_kuvaus.setWrapStyleWord(true);
        k_kuvaus.setLineWrap(true);
        k_id.setEditable(false);
        
        GroupLayout asettelu = new GroupLayout(this);
        asettelu.setAutoCreateGaps(true);
        asettelu.setHorizontalGroup(
            asettelu.createParallelGroup()
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(roolit)
                .addComponent(uusi)
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
            .addComponent(t_kuvaus)
            .addComponent(kuvausscroll)
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(tallenna)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(peruuta)
            )
        );
        asettelu.setVerticalGroup(
            asettelu.createSequentialGroup()
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(roolit, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(uusi)
            )
            .addGroup(
                asettelu.createParallelGroup()
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(t_nimi)
                    .addComponent(k_nimi, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(t_id)
                    .addComponent(k_id)
                )
            )
            .addComponent(t_kuvaus)
            .addComponent(kuvausscroll)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(tallenna)
                .addComponent(peruuta)
            )
        );
        asettelu.linkSize(SwingConstants.VERTICAL, k_nimi, k_id);
        this.setLayout(asettelu);
        
        roolit.addActionListener(new Tapahtumat());
        uusi.addActionListener(new Tapahtumat());
        peruuta.addActionListener(new Tapahtumat());
    }
    @Override
    public void paivita(){
        roolit.removeAllItems();
        try {
            for (MahdollinenRooliKuvauksella r : yhteys.getKaikkiMahdollisetRoolit()) {
                roolit.addItem(r);
            }
        } catch (TietokantaVirhe ex) {
            ex.naytaVirheIlmoitus(this, "Rooleja ei voitu näyttää");
        }
    }
    @Override
    public void tyhjenna(){
        k_nimi.setText("");
        k_id.setText("");
        k_kuvaus.setText("");
    }
    
    private class Tapahtumat implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MahdollinenRooliKuvauksella valitturooli;
            if (roolit.getSelectedItem() == null && (e.getSource() != uusi && e.getSource() != peruuta) ) return;
            else valitturooli = (MahdollinenRooliKuvauksella) roolit.getSelectedItem();
            //Roolilistasta valittiin jotain
            if (e.getSource() == roolit) {
                k_nimi.setText(valitturooli.nimi);
                k_id.setText(valitturooli.id);
                k_kuvaus.setText(valitturooli.kuvaus);
            }
            // Käyttäjä haluaa luoda uuden roolin
            else if (e.getSource() == uusi) {
                luonti_kaytossa = true;
                k_id.setEditable(true);
                roolit.setEnabled(false);
                roolit.setSelectedIndex(-1);
                uusi.setEnabled(false);
                tyhjenna();
                k_id.setText(yhteys.getSeuraavaRooliID());
                tallenna.setText("Luo uusi rooli");
            }
            //Käyttäjä peruuttaa uuden luomisen
            else if (e.getSource() == peruuta && luonti_kaytossa) {
                luonti_kaytossa = false;
                k_id.setEditable(false);
                roolit.setEnabled(true);
                uusi.setEnabled(true);
                roolit.setSelectedIndex(0);
                tallenna.setText("Tallenna muutokset");
            }
            //käyttäjä peruuttaa nykyiseen tehdyt muutokset
            else if (e.getSource() == peruuta) {
                roolit.setSelectedIndex(-1);
                roolit.setSelectedItem(valitturooli);
            }
            //käyttäjä luo uuden roolin
            else if (e.getSource() == tallenna && luonti_kaytossa) {
                MahdollinenRooliKuvauksella uusirooli = new MahdollinenRooliKuvauksella(
                        k_id.getText(),
                        k_nimi.getText(),
                        k_kuvaus.getText()
                );
                try {
                    yhteys.lisaaMahdollinenRooli(uusirooli);
                    JOptionPane.showMessageDialog(null, "Uusi rooli lisätty", TOOL_TIP_TEXT_KEY, WIDTH);
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Uuden roolin luominen ei onnistunut");
                }
                luonti_kaytossa = false;
                k_id.setEditable(false);
                roolit.setEnabled(true);
                uusi.setEnabled(true);
                paivita();
                roolit.setSelectedItem(uusirooli);
                tallenna.setText("Tallenna muutokset");
            }
            //käyttäjä tallentaa muutokset nykyiseen rooliin
            else if (e.getSource() == tallenna) {
                try {
                    MahdollinenRooliKuvauksella uusirooli = new MahdollinenRooliKuvauksella(
                            k_id.getText(),
                            k_nimi.getText(),
                            k_kuvaus.getText()
                    );
                    yhteys.setMahdollinenRooli(uusirooli);
                    JOptionPane.showMessageDialog(null, "Roolin tiedot on päivitetty", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Roolin päivittäminen ei onnistunut");
                }
            }
        }
    }
}