package kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import tiedot.Henkilo;
import tiedot.HenkiloTiedot;
import tiedot.HenkilonRooliTyopaikalla;
import tiedot.TietokantaVirhe;
import tiedot.Tyoajat;
import tiedot.Tyopaikka;
import tiedot.Yhteys;

/** Luettelo yrityksen henkilöstöstä, sekä kokonaisuudessaan että työpaikkakohtaisesti. */
public class HenkilostoLuettelo extends KayttoliittymanOsa {
    //vasemman reunan toiminnot (toimipaikan valinta ja työntekijälista)
    private final JPanel vasenPuoli = new JPanel();
    private final JComboBox paikkaLista = new JComboBox();
    private final String kaikki = "Kaikki työntekijät";
    private final DefaultListModel<Henkilo> tyontekijanimet = new DefaultListModel();
    private final JList<Henkilo> tyontekijat = new JList<>(tyontekijanimet);
    private final JScrollPane nimiLista= new JScrollPane(tyontekijat);
    
    //oiken reunan toiminnot (työntekijän tiedot)
    private final JPanel oikeaPuoli = new JPanel();
    private final JLabel t_nimi = new JLabel("Nimi");
    private final JTextField k_etunimi = new JTextField(8);
    private final JTextField k_sukunimi = new JTextField(12);
    private final JLabel t_puhnro = new JLabel("Puhelinnumero"); 
    private final JTextField k_puhnro = new JTextField(20);
    private final JLabel t_osoite = new JLabel("Osoite");
    private final JTextField k_katuos = new JTextField(20);
    //postinro ja kaupunki on tilapäisesti otettu pois
    /*private final JTextField k_postnro = new JTextField(5);
    private final JTextField k_postkau = new JTextField(15);*/
    private final JLabel t_koulut = new JLabel("Koulutus");
    private final JTextField k_koulut = new JTextField(20);
    private final JLabel t_synt = new JLabel("Syntymäaika");
    private final JTextField k_syntp = new JTextField(2);
    private final JLabel t_syntp1 = new JLabel(".");
    private final JTextField k_syntk = new JTextField(2);
    private final JLabel t_syntp2 = new JLabel(".");
    private final JTextField k_syntv = new JTextField(4);
    private final JLabel t_aloi = new JLabel("Aloitusaika");
    private final JTextField k_aloip = new JTextField(2);
    private final JLabel t_aloip1 = new JLabel(".");
    private final JTextField k_aloik = new JTextField(2);
    private final JLabel t_aloip2 = new JLabel(".");
    private final JTextField k_aloiv = new JTextField(4);
    private final JLabel t_roolit = new JLabel("Roolit");
    private final DefaultListModel<String> roolit = new DefaultListModel();
    private final JList roolilista = new JList(roolit);
    private final JScrollPane k_roolit = new JScrollPane(roolilista, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    //painikkeet
    private final JButton peruuta = new JButton("Peruuta");
    private final JButton muokkaa = new JButton("Muokkaa");
    private final JButton tyoajat = new JButton("Työajat");
    private boolean muokkausKaytossa = false;
    
    //yhteys
    private final Yhteys yhteys;
    
    /** Avaa uusi henkilöstöluettelo.
     * @param y Yhteys tietokantaan.
     */
    public HenkilostoLuettelo(Yhteys y) {
        this.yhteys = y;
        
        //vasen puoli
        vasenPuoli.add(paikkaLista);
        vasenPuoli.add(nimiLista);
        tyontekijat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GroupLayout l_vasen = new GroupLayout(vasenPuoli);
        l_vasen.setAutoCreateGaps(true);
        l_vasen.setHorizontalGroup(
            l_vasen.createParallelGroup()
            .addComponent(paikkaLista)
            .addComponent(nimiLista)
        );
        l_vasen.setVerticalGroup(
            l_vasen.createSequentialGroup()
            .addComponent(paikkaLista, 0,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
            .addComponent(nimiLista)
        );
        vasenPuoli.setLayout(l_vasen);
        
        //oikea puoli
        oikeaPuoli.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        k_etunimi.setEditable(false);
        k_sukunimi.setEditable(false);
        k_puhnro.setEditable(false);
        k_katuos.setEditable(false);
        /*k_postnro.setEditable(false);
        k_postkau.setEditable(false);*/
        k_koulut.setEditable(false);
        k_syntp.setEditable(false);
        k_syntk.setEditable(false);
        k_syntv.setEditable(false);
        k_aloip.setEditable(false);
        k_aloik.setEditable(false);
        k_aloiv.setEditable(false);
        roolilista.setVisibleRowCount(3);
        roolilista.setSelectionModel(new DefaultListSelectionModel() {
            //valintamodel, joka ei anna valita mitään. Listaa painamalla ei siis tapahdu mitään muutosta mihinkään
            @Override
            public void setSelectionInterval(int index0, int index1) {
                super.setSelectionInterval(-1, -1);
            }
        });
        peruuta.setVisible(false);
        GroupLayout l_oikea = new GroupLayout(oikeaPuoli);
        l_oikea.setAutoCreateGaps(true);
        l_oikea.setHorizontalGroup(
            l_oikea.createParallelGroup()
            .addComponent(t_nimi)
            .addGroup(
                l_oikea.createSequentialGroup()
                .addComponent(k_etunimi)
                .addComponent(k_sukunimi)
            )
            .addComponent(t_puhnro)
            .addComponent(k_puhnro)
            .addComponent(t_osoite)
            .addComponent(k_katuos)
                    /*.addGroup(
                    l_oikea.createSequentialGroup()
                    .addComponent(k_postnro)
                    .addComponent(k_postkau)
                    )*/
            .addComponent(t_koulut)
            .addComponent(k_koulut)
            .addComponent(t_synt)
            .addGroup(
                l_oikea.createSequentialGroup()
                .addComponent(k_syntp)
                .addComponent(t_syntp1)
                .addComponent(k_syntk)
                .addComponent(t_syntp2)
                .addComponent(k_syntv)
            )
            .addComponent(t_aloi)
            .addGroup(
                l_oikea.createSequentialGroup()
                .addComponent(k_aloip)
                .addComponent(t_aloip1)
                .addComponent(k_aloik)
                .addComponent(t_aloip2)
                .addComponent(k_aloiv)
            )
            .addGroup(
                l_oikea.createSequentialGroup()
                .addComponent(t_roolit)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,0,Short.MAX_VALUE)
                .addComponent(tyoajat)
            )
            .addComponent(k_roolit)
            .addGroup(
                l_oikea.createSequentialGroup()
                .addComponent(muokkaa)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,0,Short.MAX_VALUE)
                .addComponent(peruuta)
            )
        );
        l_oikea.setVerticalGroup(
            l_oikea.createSequentialGroup()
            .addComponent(t_nimi)
            .addGroup(
                l_oikea.createParallelGroup()
                .addComponent(k_etunimi)
                .addComponent(k_sukunimi)
            )
            .addComponent(t_puhnro)
            .addComponent(k_puhnro)
            .addComponent(t_osoite)
            .addComponent(k_katuos)
                    /*.addGroup(
                    l_oikea.createParallelGroup()
                    .addComponent(k_postnro)
                    .addComponent(k_postkau)
                    )*/
            .addComponent(t_koulut)
            .addComponent(k_koulut)
            .addComponent(t_synt)
            .addGroup(
                l_oikea.createParallelGroup()
                .addComponent(k_syntp)
                .addComponent(t_syntp1)
                .addComponent(k_syntk)
                .addComponent(t_syntp2)
                .addComponent(k_syntv)
            )
            .addComponent(t_aloi)
            .addGroup(
                l_oikea.createParallelGroup()
                .addComponent(k_aloip)
                .addComponent(t_aloip1)
                .addComponent(k_aloik)
                .addComponent(t_aloip2)
                .addComponent(k_aloiv)
            )
            .addGroup(
                l_oikea.createParallelGroup()
                .addComponent(t_roolit)
                .addComponent(tyoajat)
            )
            .addComponent(k_roolit)
            .addGroup(
                l_oikea.createParallelGroup()
                .addComponent(muokkaa)
                .addComponent(peruuta)
            )
        );
        oikeaPuoli.setLayout(l_oikea);
        
        this.add(vasenPuoli);
        this.add(oikeaPuoli);
        
        GroupLayout l_pohja = new GroupLayout(this);
        l_pohja.setHorizontalGroup(
            l_pohja.createSequentialGroup()
            .addComponent(vasenPuoli,100,220,Short.MAX_VALUE)
            .addComponent(oikeaPuoli)
        );
        l_pohja.setVerticalGroup(
            l_pohja.createParallelGroup()
            .addComponent(vasenPuoli)
            .addComponent(oikeaPuoli)
        );
        this.setLayout(l_pohja);
        
        // Toiminnot
        muokkaa.addActionListener(new NappiToiminto());
        peruuta.addActionListener(new NappiToiminto());
        tyoajat.addActionListener(new MuutaTyoaikoja());
        paikkaLista.addActionListener(new TyopaikkaListaToiminto());
        tyontekijat.addListSelectionListener(new TyontekijaListaToiminto());
    }
    private void paivitaMuokkausTila() {
        //muista ensin vaihtaa muokkausKäytössä -arvon tilaa jos haluat ottaa muokkauksen päälle/pois
        //tämä vain lukitsee/vapauttaa kentät
        k_etunimi.setEditable(muokkausKaytossa);
        k_sukunimi.setEditable(muokkausKaytossa);
        k_puhnro.setEditable(muokkausKaytossa);
        k_katuos.setEditable(muokkausKaytossa);
        /*k_postnro.setEditable(muokkausKaytossa);
        k_postkau.setEditable(muokkausKaytossa);*/
        k_koulut.setEditable(muokkausKaytossa);
        k_syntp.setEditable(muokkausKaytossa);
        k_syntk.setEditable(muokkausKaytossa);
        k_syntv.setEditable(muokkausKaytossa);
        k_aloip.setEditable(muokkausKaytossa);
        k_aloik.setEditable(muokkausKaytossa);
        k_aloiv.setEditable(muokkausKaytossa);
        muokkaa.setText((muokkausKaytossa?"Tallenna":"Muokkaa"));
        peruuta.setVisible(muokkausKaytossa);
        paikkaLista.setEnabled(!muokkausKaytossa);
        tyontekijat.setEnabled(!muokkausKaytossa);
    }
    
    @Override
    public void paivita() {
        if (muokkausKaytossa) {
            muokkausKaytossa = false;
            paivitaMuokkausTila();
        }
        try {
            paikkaLista.removeAllItems();
            paikkaLista.addItem(kaikki);
            for (Tyopaikka t : yhteys.getTyopaikat()) {
                paikkaLista.addItem(t);
            }
            paikkaLista.setSelectedIndex(1);
            
        } catch (TietokantaVirhe ex) {
            ex.naytaVirheIlmoitus(this, "Tietojen hakeminen ei onnistunut!");
        }
    }
    private void haeTiedot() {
        Henkilo h = tyontekijat.getSelectedValue();
        if (h == null) return;
        try {
            HenkiloTiedot t = yhteys.getTyontekijanTiedot(h);
            k_etunimi.setText(t.etunimi);
            k_sukunimi.setText(t.sukunimi);
            k_katuos.setText(t.osoite);
            k_puhnro.setText(t.puhelin);
            k_koulut.setText(t.koulutus);
            Calendar c = Calendar.getInstance();
            c.setTime(t.aloitusaika);
            k_aloip.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
            k_aloik.setText(String.valueOf(c.get(Calendar.MONTH)+1));
            k_aloiv.setText(String.valueOf(c.get(Calendar.YEAR)));
            c.setTime(t.syntyma);
            k_syntp.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
            k_syntk.setText(String.valueOf(c.get(Calendar.MONTH)+1));
            k_syntv.setText(String.valueOf(c.get(Calendar.YEAR)));
            roolit.removeAllElements();
            for (HenkilonRooliTyopaikalla r : yhteys.getTyontekijanKaikkiRoolit(h)) {
                roolit.addElement(r.toString());
            }
        } catch (TietokantaVirhe ex) {
            ex.naytaVirheIlmoitus(this, "Työntekijän tietojen haku ei onnistunut");
        }
    }
    /** Työaikojen muokkaus. */
    private class MuutaTyoaikoja implements ActionListener {
        private final JPanel pohja = new JPanel();
        //maanantai
        private final JLabel t_ma = new JLabel("Maanantai");
        private final JTextField k_maA = new JTextField(5);
        private final JLabel vma = new JLabel("-");
        private final JTextField k_maL = new JTextField(5);
        //tiistai
        private final JLabel t_ti = new JLabel("Tiistai");
        private final JTextField k_tiA = new JTextField(5);
        private final JLabel vti = new JLabel("-");
        private final JTextField k_tiL = new JTextField(5);
        //keskiviikko
        private final JLabel t_ke = new JLabel("Keskiviikko");
        private final JTextField k_keA = new JTextField(5);
        private final JLabel vke = new JLabel("-");
        private final JTextField k_keL = new JTextField(5);
        //torstai
        private final JLabel t_to = new JLabel("Torstai");
        private final JTextField k_toA = new JTextField(5);
        private final JLabel vto = new JLabel("-");
        private final JTextField k_toL = new JTextField(5);
        //perjantai
        private final JLabel t_pe = new JLabel("Perjantai");
        private final JTextField k_peA = new JTextField(5);
        private final JLabel vpe = new JLabel("-");
        private final JTextField k_peL = new JTextField(5);
        //lauantai
        private final JLabel t_la = new JLabel("Lauantai");
        private final JTextField k_laA = new JTextField(5);
        private final JLabel vla = new JLabel("-");
        private final JTextField k_laL = new JTextField(5);
        //sunnuntai
        private final JLabel t_su = new JLabel("Sunnuntai");
        private final JTextField k_suA = new JTextField(5);
        private final JLabel vsu = new JLabel("-");
        private final JTextField k_suL = new JTextField(5);
        
        private final JLabel huom = new JLabel("<html>Jätä tyhjäksi ne ruudut, jolloin <br> henkilö ei tule töihin </html>", SwingConstants.CENTER);
        {
            GroupLayout asettelu = new GroupLayout(pohja);
            asettelu.setAutoCreateContainerGaps(true);
            asettelu.setAutoCreateGaps(true);
            asettelu.setHorizontalGroup(
                asettelu.createParallelGroup()
                .addComponent(t_ma)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_maA)
                    .addComponent(vma)
                    .addComponent(k_maL)
                )
                .addComponent(t_ti)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_tiA)
                    .addComponent(vti)
                    .addComponent(k_tiL)
                )
                .addComponent(t_ke)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_keA)
                    .addComponent(vke)
                    .addComponent(k_keL)
                )
                .addComponent(t_to)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_toA)
                    .addComponent(vto)
                    .addComponent(k_toL)
                )
                .addComponent(t_pe)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_peA)
                    .addComponent(vpe)
                    .addComponent(k_peL)
                )
                .addComponent(t_la)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_laA)
                    .addComponent(vla)
                    .addComponent(k_laL)
                )
                .addComponent(t_su)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_suA)
                    .addComponent(vsu)
                    .addComponent(k_suL)
                )
                .addComponent(huom)
            );
            asettelu.setVerticalGroup(
                asettelu.createSequentialGroup()
                .addComponent(t_ma)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_maA)
                    .addComponent(vma)
                    .addComponent(k_maL)
                )
                .addComponent(t_ti)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_tiA)
                    .addComponent(vti)
                    .addComponent(k_tiL)
                )
                .addComponent(t_ke)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_keA)
                    .addComponent(vke)
                    .addComponent(k_keL)
                )
                .addComponent(t_to)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_toA)
                    .addComponent(vto)
                    .addComponent(k_toL)
                )
                .addComponent(t_pe)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_peA)
                    .addComponent(vpe)
                    .addComponent(k_peL)
                )
                .addComponent(t_la)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_laA)
                    .addComponent(vla)
                    .addComponent(k_laL)
                )
                .addComponent(t_su)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_suA)
                    .addComponent(vsu)
                    .addComponent(k_suL)
                )
                .addComponent(huom)
            );
            pohja.setLayout(asettelu);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            //Tyhjennä ruudut
            k_maA.setText("");
            k_maL.setText("");
            k_tiA.setText("");
            k_tiL.setText("");
            k_keA.setText("");
            k_keL.setText("");
            k_toA.setText("");
            k_toL.setText("");
            k_peA.setText("");
            k_peL.setText("");
            k_laA.setText("");
            k_laL.setText("");
            k_laA.setText("");
            k_laL.setText("");
            //Aseta nykyiset työajat ruutuihin
            Henkilo h = tyontekijat.getSelectedValue();
            if (h == null) return;
            try {
                Tyoajat t = yhteys.getTyoajat(h);
                k_maA.setText(t.getAika(Tyoajat.MA, Tyoajat.ALKU));
                k_maL.setText(t.getAika(Tyoajat.MA, Tyoajat.LOPPU));
                k_tiA.setText(t.getAika(Tyoajat.TI, Tyoajat.ALKU));
                k_tiL.setText(t.getAika(Tyoajat.TI, Tyoajat.LOPPU));
                k_keA.setText(t.getAika(Tyoajat.KE, Tyoajat.ALKU));
                k_keL.setText(t.getAika(Tyoajat.KE, Tyoajat.LOPPU));
                k_toA.setText(t.getAika(Tyoajat.TO, Tyoajat.ALKU));
                k_toL.setText(t.getAika(Tyoajat.TO, Tyoajat.LOPPU));
                k_peA.setText(t.getAika(Tyoajat.PE, Tyoajat.ALKU));
                k_peL.setText(t.getAika(Tyoajat.PE, Tyoajat.LOPPU));
                k_laA.setText(t.getAika(Tyoajat.LA, Tyoajat.ALKU));
                k_laL.setText(t.getAika(Tyoajat.LA, Tyoajat.LOPPU));
                k_laA.setText(t.getAika(Tyoajat.SU, Tyoajat.ALKU));
                k_laL.setText(t.getAika(Tyoajat.SU, Tyoajat.LOPPU));
            } catch (TietokantaVirhe ex) {
                //jos työaikoja ei ollut, ei tehdä mitään
                ex.naytaVirheIlmoitus(peruuta, "Tyoaikoja ei voitu näyttää");
                return;
            }
            //pyöritetään niin kauan kunnes kaikki tiedot on oikein tai käyttäjä ei halua tallentaa muutoksia
            //tämä siksi, ettei joku pieni pilkkuvirhe poista kaikkia tietoja. Kenttiä kun on paljon.
            boolean b = true;
            while (b) {
                //Näytä ilmoitus
                int i = JOptionPane.showOptionDialog(vasenPuoli, pohja, "Työajat", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Tallenna", "Hylkää"}, null);
                //Jos muutoksia, tallenna ne
                if (i == JOptionPane.YES_OPTION) {
                    String[] s = new String[]{
                        k_maA.getText(), k_maL.getText(),
                        k_tiA.getText(), k_tiL.getText(),
                        k_keA.getText(), k_keL.getText(),
                        k_toA.getText(), k_toL.getText(),
                        k_peA.getText(), k_peL.getText(),
                        k_laA.getText(), k_laL.getText(),
                        k_suA.getText(), k_suL.getText()};
                    try {
                        Tyoajat uudet = new Tyoajat(s);
                        yhteys.setTyoajat(h, uudet);
                        b = false;
                        JOptionPane.showMessageDialog(vasenPuoli, "Työajat päivitetty", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(vasenPuoli, "Tarkista, että kirjoitit työajat muotoon \"00:00\". \n\nJätä tyhjiksi ne ruudut jolloin henkilö ei tule töihin.", "Virhe", JOptionPane.ERROR_MESSAGE);
                    } catch (TietokantaVirhe ex) {
                        ex.naytaVirheIlmoitus(peruuta, "Työaikojen päivittäminen ei onnistunut");
                    }
                }
                else b = false;
            }
            
        }
    
    }
    private class NappiToiminto implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Muokkaus- tai tallennus-painikkeen toiminto
            if (e.getSource() == muokkaa) {
                if (tyontekijat.isSelectionEmpty()) return;
                //jos tallennetaan
                if (muokkausKaytossa){
                    try {
                        HenkiloTiedot h = new HenkiloTiedot(
                                tyontekijat.getSelectedValue().id, 
                                k_etunimi.getText(), k_sukunimi.getText(), 
                                k_katuos.getText(), 
                                k_puhnro.getText(), 
                                Date.valueOf(k_syntv.getText() + "-"+ k_syntk.getText() + "-" + k_syntp.getText()),
                                k_koulut.getText(), 
                                Date.valueOf(k_aloiv.getText() + "-" + k_aloik.getText() + "-" + k_aloip.getText())
                        );
                        yhteys.setTyontekijanTiedot(h);
                        muokkausKaytossa = false;
                        paivitaMuokkausTila();
                        final int i = paikkaLista.getSelectedIndex();
                        paivita();
                        paikkaLista.setSelectedIndex(i);
                        if (tyontekijanimet.contains(h)) tyontekijat.setSelectedIndex(tyontekijanimet.indexOf(h));
                    } catch (TietokantaVirhe ex) {
                        ex.naytaVirheIlmoitus(null, "Tietojen lisäämisessä tietokantaan tapahtui virhe");
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(vasenPuoli, "Syntymä- tai aloituspäivä ei kelpaa. \nTarkista että molemmat ovat oikein kirjoitettuja", "Virhe", JOptionPane.ERROR_MESSAGE);
                    }
                }
                //jos muokkaus aloitetaan
                else {
                    muokkausKaytossa = true;
                    paivitaMuokkausTila();
                }
            }
            //jos käyttäjä peruu tietojen muokkaamisen
            else if (e.getSource() == peruuta) {
                muokkausKaytossa = false;
                haeTiedot();
                paivitaMuokkausTila();
            }
        }
    }
    private class TyontekijaListaToiminto implements ListSelectionListener {
        //Työntekijälistalta valittiin joku -> Päivitä hänen tiedot ruutuihin
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) return;
            Henkilo h = tyontekijat.getSelectedValue();
            haeTiedot();
        }
    
    }
    private class TyopaikkaListaToiminto implements ActionListener {
        //työpaikkalistalta valittiin joku kohde, päivitä työntekijöiden lista
        @Override
        public void actionPerformed(ActionEvent e) {
            Object o = paikkaLista.getSelectedItem();
            if (o == null) return;
            //jos valittiin "kaikki työntekijät"
            if (o == kaikki) {
                try {
                    tyontekijanimet.clear();
                    for (Henkilo hlo : yhteys.getKaikkiTyontekijat()) {
                        tyontekijanimet.addElement(hlo);
                    }
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Työntekijöiden hakeminen ei onnistunut");
                }
            }
            //jos valittiin yksittäinen työpaikka
            else if (o instanceof Tyopaikka) {
                try {
                    Tyopaikka t = (Tyopaikka) o;
                    tyontekijanimet.clear();
                    for (Henkilo hlo : yhteys.getTyontekijat(t)) {
                        tyontekijanimet.addElement(hlo);
                    }
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Tämän työpaikan työntekijöiden haku ei onnistunut");
                }
            }
        }
    }
}