package br.edu.ufcg.msnlab.gui.about;


import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import br.edu.ufcg.msnlab.gui.MSNLab;

public class FrameAbout extends JInternalFrame {

    private static final long serialVersionUID = -5319776206787609374L;

    private JPanel panelAbout;

    private JLabel textInterface;
    private JLabel textMethods;

    private JList listInterface;
    private JList listMethoods;

    @SuppressWarnings("unused")
    private MSNLab msnlab;

    public FrameAbout(MSNLab msnlab) {
        super("About");
        this.msnlab = msnlab;

        initialize();
        positions();
        actions();
        makeScreen();
    }

    private void initialize() {
        setClosable(true);
        setSize(400, 480);

        panelAbout = new JPanel();
        panelAbout.setLayout(null);
        panelAbout.setBorder(new EtchedBorder());

        textInterface = new JLabel("Interface");
        textMethods = new JLabel("Methods");
        listInterface = new JList();
        listMethoods = new JList();

        Vector<String> vetorInterface = new Vector<String>();
        vetorInterface.add("Dayane Gaudencio");
        vetorInterface.add("Hugo Marques");
        vetorInterface.add("Jackson Porciuncula");
        vetorInterface.add("Matheus Gaudencio");
        vetorInterface.add("Ricardo Araújo");
        vetorInterface.add("Roberta Guedes");
        vetorInterface.add("Theo Alves");
        listInterface.setListData(vetorInterface);

        Vector<String> vetorMethods = new Vector<String>();
        vetorMethods.add("Adauto Trigueiro");
        vetorMethods.add("Alan Farias");
        vetorMethods.add("Anderson Pablo");
        vetorMethods.add("Diego Melo Gurjão");
        vetorMethods.add("Everton Leandro");
        vetorMethods.add("João Felipe Ouriques");
        vetorMethods.add("José Wilson");
        vetorMethods.add("José Gildo");
        vetorMethods.add("Leonardo Ribeiro Mendes");
        vetorMethods.add("Rafael Dantas");
        vetorMethods.add("Rodrigo Pinheiro");
        listMethoods.setListData(vetorMethods);
    }

    private void positions() {
        panelAbout.setBounds(7, 7, 390, 460);
        textInterface.setBounds(7, 7, 80, 20);
        listInterface.setBounds(7, 27, 375, 150);
        
        textMethods.setBounds(7, 187, 80, 20);
        listMethoods.setBounds(7, 207, 375, 230);
    }

    private void actions() {

    }

    private void makeScreen() {
        panelAbout.add(textInterface);
        panelAbout.add(listInterface);
        panelAbout.add(textMethods);
        panelAbout.add(listMethoods);

        add(panelAbout);

    }

}
