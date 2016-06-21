package kayttoliittyma;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import tiedot.Yhteys;

/** Ohjelman pääikkuna, johon muut osat liitetään. */
public class Paaikkuna extends JFrame {
    //pohja, jossa muut elementit ovat
    private final JPanel pohja = new JPanel();
    private final Dimension ikkunakoko_paa = new Dimension(215,240);
    private final Dimension ikkunakoko_muu = new Dimension(300,350);
    private final Dimension ikkunakoko_lis = new Dimension(300,400);
    
    //ikkunan pääosa, jossa "kortit" eli eri näkymät/panelit sijaitsevat
    private final JPanel korttipohja = new JPanel();
    private final CardLayout korttiasettelu = new CardLayout();
    private final KayttoliittymanOsa valikko_paa;
    private final KayttoliittymanOsa valikko_hlo;
    private final KayttoliittymanOsa valikko_lisaahlo;
    private final KayttoliittymanOsa valikko_tyopaikat;
    private final KayttoliittymanOsa valikko_roolit;
    private final KayttoliittymanOsa valikko_roolimuok;
    
    //ikkunan alaosa
    private final JPanel alapohja = new JPanel();
    private final JButton ulos = new JButton("Kirjaudu ulos");
    private final JButton sulje = new JButton("Sulje");
    
    //Yhteys
    private final Yhteys yhteys;
    
    /** Luo uusi pääikkuna.
     * @param y Yhteys tietokantaan.
     */
    public Paaikkuna(Yhteys y) {
        this.valikko_hlo = new HenkilostoLuettelo(y);
        this.valikko_paa = new Paavalikko(y);
        this.valikko_lisaahlo = new UusiHenkilo(y);
        this.valikko_tyopaikat = new TyopaikkaMuokkaus(y);
        this.valikko_roolit = new RooliLista(y);
        this.valikko_roolimuok = new RoolienMuokkaus(y);
        //lisää aloitusvalikko pääosaan
        korttipohja.setLayout(korttiasettelu);
        korttipohja.add(valikko_paa, "paa");
        korttipohja.add(valikko_hlo, "hlo");
        korttipohja.add(valikko_lisaahlo, "lishlo");
        korttipohja.add(valikko_tyopaikat, "tyopaikat");
        korttipohja.add(valikko_roolit, "roolit");
        korttipohja.add(valikko_roolimuok, "roolimuok");
        korttiasettelu.show(korttipohja, "paa");
        //lisää ikkunan alaosa
        alapohja.setLayout(new FlowLayout(FlowLayout.TRAILING));
        alapohja.add(ulos);
        alapohja.add(sulje);
        sulje.addActionListener(new Tapahtumat());
        ulos.addActionListener(new Tapahtumat());
        //lisää molemmat osat pohjaan ja pohja ikkunaan
        pohja.setLayout(new BorderLayout());
        pohja.add(korttipohja, BorderLayout.CENTER);
        pohja.add(alapohja, BorderLayout.PAGE_END);
        pohja.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        this.add(pohja);
        
        //alusta ikkuna
        this.setTitle("Henkilöstörekisteri");
        this.setResizable(false);
        //pack();
        this.setSize(ikkunakoko_paa);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new IkkunanSulkeminen());
        this.yhteys = y;
    }
    /** Valikko, jonka kautta pääsee ohjelman muihin osiin. Näytetään ensimmäisenä kun pääikkuna avataan. */
    private class Paavalikko extends KayttoliittymanOsa {
        private final JButton henkilosto = new JButton("Henkilöstöluettelo");
        private final JButton lisaahlo = new JButton("Lisää uusi henkilö");
        private final JButton tyopaikat = new JButton("Muokkaa työpaikkoja");
        private final JButton roolit = new JButton("Työroolit");
        private final JButton roolimuok = new JButton("Muokkaa mahdollisia rooleja");
        public Paavalikko(Yhteys y) {
            GroupLayout asettelu = new GroupLayout(this);
            asettelu.setAutoCreateGaps(true);
            asettelu.setHorizontalGroup(
                asettelu.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(henkilosto)
                .addComponent(lisaahlo)
                .addComponent(tyopaikat)
                .addComponent(roolit)
                .addComponent(roolimuok)
            );
            asettelu.setVerticalGroup(
                asettelu.createSequentialGroup()
                .addComponent(henkilosto)
                .addComponent(lisaahlo)
                .addComponent(tyopaikat)
                .addComponent(roolit)
                .addComponent(roolimuok)
            );
            asettelu.linkSize(SwingConstants.HORIZONTAL, lisaahlo, henkilosto, tyopaikat, roolit, roolimuok);
            this.setLayout(asettelu);
            
            henkilosto.setActionCommand("henkiloluettelo");
            henkilosto.addActionListener(new Tapahtumat());
            lisaahlo.setActionCommand("lisaahenkilo");
            lisaahlo.addActionListener(new Tapahtumat());
            tyopaikat.setActionCommand("tyopaikat");
            tyopaikat.addActionListener(new Tapahtumat());
            roolit.setActionCommand("roolit");
            roolit.addActionListener(new Tapahtumat());
            roolimuok.setActionCommand("roolimuok");
            roolimuok.addActionListener(new Tapahtumat());
        }
    }
    private class Tapahtumat implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Paavalikon näppäimet
            switch (e.getActionCommand()) {
                case "henkiloluettelo":
                    korttiasettelu.show(korttipohja, "hlo");
                    valikko_hlo.paivita();
                    sulje.setText("Takaisin");
                    break;
                case "lisaahenkilo":
                    korttiasettelu.show(korttipohja, "lishlo");
                    valikko_lisaahlo.tyhjenna();
                    sulje.setText("Takaisin");
                    break;
                case "tyopaikat":
                    korttiasettelu.show(korttipohja, "tyopaikat");
                    sulje.setText("Takaisin");
                    valikko_tyopaikat.paivita();
                    break;
                case "roolit":
                    korttiasettelu.show(korttipohja, "roolit");
                    valikko_roolit.paivita();
                    sulje.setText("Takaisin");
                    break;
                case "roolimuok":
                    korttiasettelu.show(korttipohja, "roolimuok");
                    valikko_roolimuok.paivita();
                    sulje.setText("Takaisin");
                    break;
            }
            //takaisin-näppäin, jos joku näkymä on auki
            if (e.getSource() == sulje && !valikko_paa.isVisible()) {
                korttiasettelu.show(korttipohja, "paa");
                sulje.setText("Sulje");
            }
            //takaisin-näppäin päävalikossa eli sulje-painike
            else if (e.getSource() == sulje) {
                yhteys.suljeYhteys();
                System.exit(0);
            }
            //kirjaudu ulos-painike
            else if (e.getSource() == ulos) {
                yhteys.suljeYhteys();
                dispose();
                new KirjautumisIkkuna().setVisible(true);
                return;
            }
            //Määritä ikkunalle oikea koko.
            //CardLayout on aina saman kokoinen, joten täytyy olla tällainen systeemi tai osa ikkunoista on täynä tyhjää tilaa
            if (valikko_paa.isVisible()) setSize(ikkunakoko_paa);
            else if (valikko_lisaahlo.isVisible()) setSize(ikkunakoko_lis);
            else if (valikko_hlo.isVisible() || valikko_roolit.isVisible()) pack();
            else setSize(ikkunakoko_muu);
            setLocationRelativeTo(null);
        }
    }
    private class IkkunanSulkeminen extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (!valikko_paa.isVisible()){
                //Jos joku ohjelman osista on auki, kysytään onko käyttäjä varma
                //Muuten joku tärkeä tieto saattaa hävitä vahinkopainalluksen takia
                int i = JOptionPane.showConfirmDialog(null, "Haluatko varmasti sulkea ohjelman?\nMenetät kaikki tallentamattomat tiedot.", "Varoitus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (i != JOptionPane.OK_OPTION) return;
            }
            yhteys.suljeYhteys();
            System.exit(0);
        }
    }
}