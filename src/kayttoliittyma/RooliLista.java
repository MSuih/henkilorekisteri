package kayttoliittyma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import tiedot.Henkilo;
import tiedot.HenkilonRooli;
import tiedot.MahdollinenRooli;
import tiedot.MahdollinenRooliKuvauksella;
import tiedot.TietokantaVirhe;
import tiedot.Tyopaikka;
import tiedot.Yhteys;

/** Työmaan työroolien muokkaus. Valikosta valitaan ensin työmaa, jonka jälkeen tulee näkyviin työntekijät ja heidän työroolinsa. Työmaahan voi myös lisätä uusia työntekijöitä. */
public class RooliLista extends KayttoliittymanOsa {
    private final Yhteys yhteys;
    
    private final JComboBox<Tyopaikka> tyopaikat = new JComboBox<>();
    private final DefaultListModel<Henkilo> listamodel = new DefaultListModel<>(); 
    private final JList<Henkilo> henkilolista = new JList<>(listamodel);
    private final JScrollPane henkiloscroll = new JScrollPane(henkilolista);
    
    private final JLabel t_nimi = new JLabel("Henkilö:");
    private final JTextField k_nimi = new JTextField();
    private final JLabel t_rooli = new JLabel("Työtehtävä");
    private final JTextField k_rooli = new JTextField();
    private final JLabel t_komm = new JLabel("Kuvaus tehtävistä");
    private final JTextArea k_komm = new JTextArea(3, 20);
    private final JScrollPane kommscroll = new JScrollPane(k_komm) ;

    private final JButton tallenna = new JButton("Tallenna kuvaus");
    private final JButton poista = new JButton("Poista");
    private final JButton lisaa = new JButton("Lisää henkilö tälle työpaikalle");
    private final JButton rooli_muok = new JButton("Muuta");
    
    private HenkilonRooli henk_rooli = null;
    
    /** Luo uusi työroolilista.
     * @param y Yhteys tietokantaan.
     */
    public RooliLista(Yhteys y){
        yhteys = y;
        k_nimi.setEditable(false);
        k_rooli.setEditable(false);
        k_komm.setLineWrap(true);
        k_komm.setWrapStyleWord(true);
        henkilolista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        GroupLayout asettelu = new GroupLayout(this);
        this.setLayout(asettelu);
        asettelu.setAutoCreateGaps(true);
        asettelu.setHorizontalGroup(
            asettelu.createSequentialGroup()
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(tyopaikat, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(henkiloscroll)
                .addComponent(lisaa)
            )
            .addGap(5, 5, 5)
            .addGroup(
                asettelu.createParallelGroup()
                .addComponent(t_nimi)
                .addComponent(k_nimi)
                .addComponent(t_rooli)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(k_rooli)
                    .addComponent(rooli_muok)
                )
                .addComponent(k_rooli)
                .addComponent(t_komm)
                .addComponent(kommscroll)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(
                    asettelu.createSequentialGroup()
                    .addComponent(tallenna)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(poista)
                )
            )
        );
        asettelu.setVerticalGroup(
            asettelu.createParallelGroup()
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(tyopaikat, 0, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(henkiloscroll)
                .addComponent(lisaa)
            )
            .addGroup(
                asettelu.createSequentialGroup()
                .addComponent(t_nimi)
                .addComponent(k_nimi)
                .addComponent(t_rooli)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(k_rooli)
                    .addComponent(rooli_muok)
                )
                .addComponent(t_komm)
                .addComponent(kommscroll)
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(
                    asettelu.createParallelGroup()
                    .addComponent(tallenna)
                    .addComponent(poista)
                )
            )
        );
        asettelu.linkSize(SwingConstants.VERTICAL, t_nimi, k_nimi, k_rooli);
        asettelu.linkSize(SwingConstants.HORIZONTAL, tyopaikat, henkiloscroll, lisaa);
        
        tyopaikat.addActionListener(new Tapahtumat());
        henkilolista.addListSelectionListener(new TyontekijaListaToiminto());
        poista.addActionListener(new Tapahtumat());
        lisaa.addActionListener(new Tapahtumat());
        tallenna.addActionListener(new Tapahtumat());
        rooli_muok.addActionListener(new Tapahtumat());
    }
    
    @Override
    public void paivita() {
        henkilolista.setSelectedIndex(0);
        k_nimi.setText("");
        k_rooli.setText("");
        k_komm.setText("");
        tyopaikat.removeAllItems();
        try {
            for (Tyopaikka t : yhteys.getTyopaikat()) {
                tyopaikat.addItem(t);
            }
        } catch (TietokantaVirhe ex) {
            ex.naytaVirheIlmoitus(this, "Listoja ei voitu päivittää");
        }
    }
    
    private class Tapahtumat implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Työpaikka-listasta valittiin jotain
            if (e.getSource() == tyopaikat) {
                Object o = tyopaikat.getSelectedItem();
                if (o == null) return;
                Tyopaikka t = (Tyopaikka) o;
                //tyhjennä työntekijälista ja lisää siihen kys. työpaikan tiedot
                listamodel.removeAllElements();
                try {
                    for (Henkilo h : yhteys.getTyontekijat(t)){
                        listamodel.addElement(h);
                    }
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Työntekijöitä hakiessa tapahtui virhe");
                }
            }
            //käyttäjä haluaa tallentaa kuvaustekstin muutokset
            else if (e.getSource() == tallenna) {
                try {
                    yhteys.setTyontekijanRooliTeksti(henkilolista.getSelectedValue(), (Tyopaikka) tyopaikat.getSelectedItem(), k_komm.getText());
                    JOptionPane.showMessageDialog(null, "Kuvausteksti on päivitetty", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Roolitekstin päivittäminen ei onnistunut");
                }
            }
            //Käyttäjä haluaa poistaa henkilön
            else if (e.getSource() == poista) {
                Henkilo h = henkilolista.getSelectedValue();
                if (h == null) return;
                //Kysytään onko hän varma tästä
                int i =JOptionPane.showConfirmDialog(null, "Haluatko varmasti poistaa henkilön\n" + h.toString() + "\ntästä työpaikasta?", "Vahvista", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (i == JOptionPane.YES_OPTION) {
                    try {
                        //Yritetään poistaa ja ilmoitetaan mitä tapahtui
                        yhteys.poistaTyontekijanRooli(h, (Tyopaikka) tyopaikat.getSelectedItem());
                        JOptionPane.showMessageDialog(null, "Henkilö poistettu", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                        paivita();
                    } catch (TietokantaVirhe ex) {
                        ex.naytaVirheIlmoitus(null, "Henkilöä ei voitu poistaa");
                    }
                }
            }
            //käyttäjä haluaa lisätä uuden henkilön
            else if (e.getSource() == lisaa) {
                Object o =  tyopaikat.getSelectedItem();
                if (!(o instanceof Tyopaikka)) return;
                Tyopaikka t = (Tyopaikka) o;
                try {
                    //luodaan pohja ja siihen kentät lisäämistä varten
                    final JLabel t_tyopaikka = new JLabel("Työpaikka");
                    final JTextField k_tyopaikka = new JTextField(t.toString());
                    final JLabel t_henkilo = new JLabel("Työntekijä");
                    final JComboBox<Henkilo> henkilolista = new JComboBox<>();
                    final JLabel t_rooli = new JLabel("Työrooli");
                    final JComboBox<MahdollinenRooli> roolilista = new JComboBox<>();
                    final JLabel t_kuvaus = new JLabel("Kuvaus");
                    final JTextArea k_kuvaus = new JTextArea(3,0);
                    final JScrollPane kuvausscroll = new JScrollPane(k_kuvaus);
                    JPanel pohja = new JPanel() {
                        {
                            for (Henkilo h : yhteys.getTyopaikkaanKuulumattomat(t)) {
                                henkilolista.addItem(h);
                            }
                            for (MahdollinenRooli r : yhteys.getKaikkiMahdollisetRoolit()) {
                                roolilista.addItem(r);
                            }
                            k_tyopaikka.setText(t.toString());
                            k_tyopaikka.setEditable(false);
                            k_kuvaus.setLineWrap(true);
                            k_kuvaus.setWrapStyleWord(true);
                            
                            GroupLayout asettelu = new GroupLayout(this);
                            this.setLayout(asettelu);
                            asettelu.setAutoCreateContainerGaps(true);
                            asettelu.setAutoCreateGaps(true);
                            asettelu.setHorizontalGroup(
                                asettelu.createParallelGroup()
                                .addComponent(t_tyopaikka)
                                .addComponent(k_tyopaikka)
                                .addComponent(t_henkilo)
                                .addComponent(henkilolista)
                                .addComponent(t_rooli)
                                .addComponent(roolilista)
                                .addComponent(t_kuvaus)
                                .addComponent(kuvausscroll)
                            );
                            asettelu.setVerticalGroup(
                                asettelu.createSequentialGroup()
                                .addComponent(t_tyopaikka)
                                .addComponent(k_tyopaikka)
                                .addComponent(t_henkilo)
                                .addComponent(henkilolista)
                                .addComponent(t_rooli)
                                .addComponent(roolilista)
                                .addComponent(t_kuvaus)
                                .addComponent(kuvausscroll)
                            );
                        }
                    };
                    //Näytetään kentät
                    int i = JOptionPane.showOptionDialog(null, pohja, "Lisää henkilö", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Lisää","Peruuta"}, null);
                    if (i != JOptionPane.YES_OPTION) return;
                    try {
                        //Yritetään lisätä ja kerrotaan miten siinä kävi.
                        yhteys.lisaaTyontekijanRooli(
                            henkilolista.getItemAt(henkilolista.getSelectedIndex()),
                            t,
                            roolilista.getItemAt(henkilolista.getSelectedIndex()),
                            k_kuvaus.getText());
                        JOptionPane.showMessageDialog(null, "Henkilö lisätty", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                        paivita();
                    }
                    catch (TietokantaVirhe ex) {
                        ex.naytaVirheIlmoitus(null, "Henkilöä ei voitu lisätä");
                    }
                }
                catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Henkilöjä ei voi lisätä");
                }
            }
            //Henkilö haluaa muokata jonkun roolia
            else if (e.getSource() == rooli_muok) {
                Henkilo h = henkilolista.getSelectedValue();
                if (h == null ) return;
                try {
                    final JComboBox<MahdollinenRooliKuvauksella> k_rooli = new JComboBox<>();
                    final JLabel t_rooli = new JLabel("Rooli");
                    final JLabel t_kuvaus = new JLabel("Kuvaus");
                    final JTextArea k_kuvaus = new JTextArea(3, 0);
                    final JScrollPane kuvausscroll = new JScrollPane(k_kuvaus, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    //luo kentät roolin muokkaamiseen
                    JPanel pohja = new JPanel(){
                        {   
                            k_kuvaus.setLineWrap(true);
                            k_kuvaus.setWrapStyleWord(true);
                            k_rooli.addActionListener((ActionEvent e) -> {
                                k_kuvaus.setText(k_rooli.getItemAt(k_rooli.getSelectedIndex()).kuvaus);
                                k_kuvaus.setCaretPosition(0);
                            });
                            for (MahdollinenRooliKuvauksella l : yhteys.getKaikkiMahdollisetRoolit()) {
                                k_rooli.addItem(l);
                                if (l.id.equals(henk_rooli.id)) k_rooli.setSelectedItem(l);
                            }
                            
                            k_kuvaus.setEditable(false);
                            GroupLayout asettelu = new GroupLayout(this);
                            this.setLayout(asettelu);
                            asettelu.setAutoCreateContainerGaps(true);
                            asettelu.setAutoCreateGaps(true);
                            asettelu.setHorizontalGroup(
                                asettelu.createParallelGroup()
                                .addComponent(t_rooli)
                                .addComponent(k_rooli)
                                .addComponent(t_kuvaus)
                                .addComponent(kuvausscroll)
                            );
                            asettelu.setVerticalGroup(
                                asettelu.createSequentialGroup()
                                .addComponent(t_rooli)
                                .addComponent(k_rooli)
                                .addComponent(t_kuvaus)
                                .addComponent(kuvausscroll)
                            );
                        }
                    };
                    //näytetään ikkuna jossa hienot kentät
                    int i = JOptionPane.showOptionDialog(null, pohja, "Muuta työroolia", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Muuta","Peruuta"}, null);
                    if (i != JOptionPane.YES_OPTION) return;
                    //Jos käyttäjä haluaa, yritetään muokata ja kerrotaan miten kävi
                    try {
                        yhteys.setTyontekijanRooli(h, tyopaikat.getItemAt(tyopaikat.getSelectedIndex()), k_rooli.getItemAt(k_rooli.getSelectedIndex()));
                        JOptionPane.showMessageDialog(null, "Työrooli päivitetty", "Ilmoitus", JOptionPane.INFORMATION_MESSAGE);
                        paivita();
                    }
                    catch (TietokantaVirhe ex){
                        ex.naytaVirheIlmoitus(null, "Työroolia ei voitu päivittää");
                    }
                    
                } catch (TietokantaVirhe ex) {
                    ex.naytaVirheIlmoitus(null, "Rooleja ei voitu muokata: ");
                }
            }
        }
    }
    
    private class TyontekijaListaToiminto implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            //Työntekijä-listasta valittiin joku henkilö, päivitä ruudut
            if (e.getValueIsAdjusting()) return;
            Henkilo h = henkilolista.getSelectedValue();
            if (h == null) return;
            k_nimi.setText(h.toString());
            try {
                henk_rooli = yhteys.getTyontekijanRooli(h, tyopaikat.getItemAt(tyopaikat.getSelectedIndex()));
                k_rooli.setText(henk_rooli.rooli.nimi);
                k_komm.setText(henk_rooli.kuvaus);
            } catch (TietokantaVirhe ex) {
                ex.naytaVirheIlmoitus(k_komm, "Rooleja hakiessa tapahtui virhe");
            }
        }
    }
}