package br.edu.ufcg.msnlab.gui.output;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.gui.util.ExpressionParser.ParserResult;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;

public class FrameOutput extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected MSNLab msnlab;

    private JScrollPane scroll;

    private Container screen;

	private JPanel panelButtons;

	private JPanel panelFields;

	private JLabel label;

	private JButton buttonPrevious;
	
	private JTextPane buttonHowMany;

	private JButton buttonNext;

	private Result result;

	private int currentResult;

	private ParserResult parserResult;

	private String method;

	private JTable tableResult;

    public FrameOutput(MSNLab msnLab) {
        this.msnlab = msnLab;
        initialize();
        positions();
        actions();
        makeScreen();
    }

    private void actions() {
		buttonNext.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
		    	currentResult++;
		    	setCurrentResult();
			}

		});
		
		buttonPrevious.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
		    	currentResult--;
		    	setCurrentResult();
			}

		});
		
	}

	private void initialize() {
        setLayout(null);
        screen = getContentPane();
        setSize(330, 250);
        setClosable(true);

        label = new JLabel("Method");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVisible(true);
        
        tableResult = new JTable();
        scroll = new JScrollPane(tableResult);
        
        panelButtons = new JPanel();
        panelButtons.setLayout(null);
        panelButtons.setBorder(new EtchedBorder());

        panelFields = new JPanel();
        panelFields.setLayout(null);
        panelFields.setBorder(new EtchedBorder());
        
        buttonHowMany = new JTextPane();
		buttonPrevious = new JButton("<-");
		buttonNext = new JButton("->");
        
    }

    private void positions() {
    	label.setBounds(7, 7, 310, 20);
		panelFields.setBounds(7, 7, 310, 165);
        scroll.setBounds(7, 27, 295, 130);
        panelButtons.setBounds(7, 175, 310, 35);
        buttonHowMany.setBounds(7, 7, 50, 20);
        buttonPrevious.setBounds(60, 7, 80, 20);
        buttonNext.setBounds(150, 7, 80, 20);
        
        buttonHowMany.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        
    }
    
    public void setSolution(String method, Result result, ParserResult parserResult) {
    	this.setTitle("OUTPUT - " + method);
    	this.method = method;
    	this.result = result;
    	this.parserResult = parserResult;
    	this.currentResult = 1;
    	setCurrentResult();
    }

    private void setCurrentResult() {
        
    	SolutionTableModel solutionTableModel = new SolutionTableModel();
    	SystemTableModel systemTableModel = new SystemTableModel();

    	if (currentResult == 1) {
    		buttonPrevious.setEnabled(false);
    	} else {
    		buttonPrevious.setEnabled(true);
    	}
    	
    	if (currentResult == result.getValues().size()) {
    		buttonNext.setEnabled(false);
    	} else {
    		buttonNext.setEnabled(true);
    	}
    	buttonHowMany.setText(currentResult + "/" + result.getValues().size());

    	double[][] resultArray = (double[][]) result.getValues().get(currentResult - 1);

    	if (Methods.DecomposicaoSVD.equals(this.method)) {
    		solutionTableModel.setCurrentResult(result, this.currentResult, parserResult.getVariables());
        	tableResult.setModel(solutionTableModel);
        	label.setText("Refining Solution");
		} else {
			if (resultArray[0].length == 1) {
				solutionTableModel.setCurrentResult(result, this.currentResult, parserResult.getVariables());
		    	tableResult.setModel(solutionTableModel);
		    	label.setText("Refining Solution");
			} else {
				systemTableModel.setCurrentResult(result, currentResult, parserResult.getVariables());
				tableResult.setModel(systemTableModel);
				label.setText("Changing Matrix");
			}
    	}
    }
    
    private void makeScreen() {
    	panelFields.add(label);
    	panelFields.add(scroll);

    	panelButtons.add(buttonPrevious);
    	panelButtons.add(buttonHowMany);
    	panelButtons.add(buttonNext);
    	
        screen.add(panelFields);
        screen.add(panelButtons);
    }

}
