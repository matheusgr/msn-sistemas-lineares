package br.edu.ufcg.msnlab.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.gui.input.FrameInputInterface;

public class MenuFile extends JMenu {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private MSNLab msnLab;

    private JMenuItem newInstance;
    private JMenuItem exit;

    public MenuFile(MSNLab msnLab) {
        super("File");
        this.msnLab = msnLab;
        initialize();
        actions();
        makeMenu();
    }

    private void initialize() {
        exit = new JMenuItem("Exit");
        newInstance = new JMenuItem("Start new instance");
    }

    private void actions() {
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        newInstance.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		MenuFile.this.msnLab.openComponent(new FrameInputInterface(MenuFile.this.msnLab));
        	}
        });

    }

    private void makeMenu() {
        add(newInstance);
        add(exit);
    }

}
