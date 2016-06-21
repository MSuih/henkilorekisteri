package kayttoliittyma;

import yleista.VirheidenHallinta;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import tiedot.TietokantaVirhe;
import tiedot.Yhteys;

/** Ikkuna, jolla kirjaudutaan sisään ohjelmaan. */
public class KirjautumisIkkuna extends JFrame{
    //pohja, joka sisältää kaiken muun
    private final JPanel pohja = new JPanel();
    //yrityksen logon sisältävä paneeli
    private final JPanel kuva = new JPanel();
    
    //ruutujen tekstit
    private final JLabel t_tunnus = new JLabel("Käyttäjätunnus:");
    private final JLabel t_sala = new JLabel("Salasana:");
    
    //kirjoitusruudut
    private final JTextField k_tunnus = new JTextField(10);
    private final JPasswordField k_sala = new JPasswordField(10);
    
    //näppäimet
    private final JButton kirjaudu = new JButton("Kirjaudu");
    private final JButton asetukset = new JButton("Asetukset");
    
    /** Avaa uusi kirjautumisikkuna. */
    public KirjautumisIkkuna() {
        
        kirjaudu.addActionListener(new Kirjaudu());
        asetukset.addActionListener(new Asetukset());
        
        Image i;
        try {
            //tarkista missä tiedostossa logo on, ja lataa se
            File f = new File("logo.png");
            if (!f.exists()) f = new File("logo.jpg");
            if (!f.exists()) f = new File("logo.gif");
            i = ImageIO.read(f);
        } catch (IOException ex) {
            i = null;
        }
        if (i != null) {
            //lisätään kuva labeliin ja se kuvapaneeliin
            JLabel l = new JLabel(new ImageIcon(i));
            kuva.add(l);
        }
        else {
            //kuvaa ei ole -> ei näytetä mitään
            kuva.setPreferredSize(new Dimension(0, 0));
        }
        
        GroupLayout asettelu = new GroupLayout(pohja);
        asettelu.setAutoCreateContainerGaps(true);
        asettelu.setAutoCreateGaps(true);
        
        asettelu.setHorizontalGroup(
            asettelu.createParallelGroup()
            // Logo
            .addComponent(kuva)
            //käyttäjätunnus
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(t_tunnus)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 0, Short.MAX_VALUE)
                .addComponent(k_tunnus)
            )
            //salasana
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(t_sala)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 0, Short.MAX_VALUE)
                .addComponent(k_sala)
            )
            //näppäimet
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(asetukset)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 0, Short.MAX_VALUE)
                .addComponent(kirjaudu)
            )
        );
        asettelu.setVerticalGroup(
            asettelu.createSequentialGroup()
            //logo
            .addComponent(kuva)
            //muu sisältö
            .addGroup(
                asettelu.createParallelGroup()
                .addGroup(
                    asettelu.createSequentialGroup()
                    //tekstit
                    .addComponent(t_tunnus)
                    .addComponent(t_sala)
                )
                .addGroup(
                    asettelu.createSequentialGroup()
                    //kirjoitusruudut
                    .addComponent(k_tunnus)
                    .addComponent(k_sala)
                )
            )
            //näppäimet
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(asetukset)
                .addComponent(kirjaudu)
            )
        );
        asettelu.linkSize(SwingConstants.VERTICAL, t_tunnus,k_tunnus,t_sala,k_sala);
        asettelu.linkSize(k_tunnus,k_sala);
        pohja.setLayout(asettelu);
        this.add(pohja);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Kirjaudu sisään - Henkilörekisteri");
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }
    /** Käynnistää koko ohjelman.
     * @param args Ei käytössä. 
     */
    public static void main(String[] args) {
        //asetetaan oletuksena toimiva virheidenhallinta
        Thread.setDefaultUncaughtExceptionHandler(new VirheidenHallinta());
        SwingUtilities.invokeLater(() -> {
            new KirjautumisIkkuna().setVisible(true);
        });
    }
    private class Kirjaudu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Tarkista että molemmissa ruuduissa on tekstiä
            if (k_tunnus.getText().isEmpty() || k_sala.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Unohdit syöttää käyttäjätunnuksen tai salasanan", "Virhe", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Luo Yhteysolio
            Yhteys y;
            String sala = new String(k_sala.getPassword());
            try {
                y = new Yhteys(k_tunnus.getText(), sala);
            }
            catch (TietokantaVirhe ex) {
                ex.naytaKirjautumisVirhe(null);
                return;
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(rootPane, "Tietokanta-ajuria ei löydy tai sitä ei voitu käyttää.\nAsenna ohjelma uudestaan", "Vakava virhe", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Avaa ikkuna
            new Paaikkuna(y).setVisible(true);
            dispose();
        }
    }
    
    private class Asetukset implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            new AsetusIkkuna().setVisible(true);
        }
    }
}
