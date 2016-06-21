package kayttoliittyma;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.zip.DataFormatException;
import javax.swing.*;
import yleista.AlustaDerbyTietokanta;
import yleista.Asetukset;

/** Asetusten muutosikkuna. */
public class AsetusIkkuna extends JFrame {
    private final JPanel pohja = new JPanel();
    //yläreuna
    private final JPanel ylaosa = new JPanel();
    private final ButtonGroup v_ryhma = new ButtonGroup();
    private final JRadioButton v_sql = new JRadioButton("SQL-tietokanta");
    private final JRadioButton v_derby = new JRadioButton("Derby-tietokanta");
    //keskiosa
    private final JPanel keskiosa = new JPanel();
    private final CardLayout kortit = new CardLayout();
    private final JPanel keskiosa_derby = new JPanel();
    private final JButton alustaTietokanta = new JButton("Alusta tietokanta");
    private final JLabel kayttaja = new JLabel("Käyttäjätunnus: tunnus", SwingConstants.CENTER);
    private final JLabel sala = new JLabel("Salasana: sala", SwingConstants.CENTER);
    private final JPanel keskiosa_sql = new JPanel();
    private final JLabel t_osoite = new JLabel("Tietokannan osoite");
    private final JTextField k_osoite = new JTextField();
    
    private final JPanel alaosa = new JPanel();
    private final JButton tallenna = new JButton("Tallenna");
    
    /** Avaa uusi asetusikkuna. */
    public AsetusIkkuna() {
        //yläosan painikkeet
        ylaosa.add(v_sql);
        v_ryhma.add(v_sql);
        ylaosa.add(v_derby);
        v_ryhma.add(v_derby);
        v_sql.setSelected(true);
        //keskiosa
        keskiosa_derby.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        keskiosa_derby.add(alustaTietokanta, c);
        c.gridy = 1;
        keskiosa_derby.add(kayttaja, c);
        c.gridy = 2;
        keskiosa_derby.add(sala, c);
        keskiosa_sql.setLayout(new BorderLayout());
        keskiosa_sql.add(t_osoite, BorderLayout.CENTER);
        keskiosa_sql.add(k_osoite, BorderLayout.PAGE_END);
        k_osoite.setText(Asetukset.getOsoiteTekstikentalle());
        keskiosa.setLayout(kortit);
        keskiosa.add(keskiosa_derby, "derby");
        keskiosa.add(keskiosa_sql, "sql");
        if (Asetukset.getOnkoSQL()) {
            v_sql.setSelected(true);
            kortit.show(keskiosa, "sql");
        }
        else v_derby.setSelected(true);
        
        alaosa.add(tallenna);
        //pohja
        pohja.setLayout(new BorderLayout());
        pohja.add(ylaosa, BorderLayout.PAGE_START);
        pohja.add(keskiosa, BorderLayout.CENTER);
        pohja.add(alaosa, BorderLayout.PAGE_END);
        pohja.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(pohja);
        
        this.setResizable(false);
        this.setTitle("Asetukset");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        
        v_sql.addActionListener(new Tapahtumat());
        v_derby.addActionListener(new Tapahtumat());
        tallenna.addActionListener(new Tapahtumat());
        alustaTietokanta.addActionListener(new Tapahtumat());
    }
    private class Tapahtumat implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Käytttäjä haluaa alustaa derby-tietokannan
            if (e.getSource() == alustaTietokanta) {
                try {
                    AlustaDerbyTietokanta.alusta();
                } catch (DataFormatException ex) {
                    JOptionPane.showMessageDialog(pohja, "Tietokantaa ei voitu alustaa.");
                }
            }
            //käyttäjä haluaa tallentaa asetukset
            else if (e.getSource() == tallenna) {
                Asetukset.setAjuri(v_sql.isSelected());
                if (v_sql.isSelected()) Asetukset.setOsoiteTekstikentasta(k_osoite.getText());
                Asetukset.tallenna();
                dispose();
            }
            //käyttäjä haluaa vaihtaa välilehtien eli sql- tai derby-tietokantojen välillä
            else {
                if (v_sql.isSelected()) kortit.show(keskiosa, "sql");
                else kortit.show(keskiosa, "derby");
            }
        }
    }
}