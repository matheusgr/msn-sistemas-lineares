package br.edu.ufcg.msnlab.gui.output;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.gui.util.ExpressionParser.ParserResult;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;

public class FrameOutput extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected MSNLab msnlab;

    private JTextArea textArea;
    private JScrollPane scroll;

    private Container screen;

	private JPanel panelButtons;

	private JPanel panelFields;

	private JButton buttonPrevious;

	private JTextPane buttonHowMany;

	private JButton buttonNext;

	private Result result;

	private int currentResult;

	private ParserResult parserResult;

	private String method;

    public FrameOutput(MSNLab msnLab) {
        this.msnlab = msnLab;
        setTitle("Output");
        initialize();
        positions();
        actions();
        makeScreen();
    }

    private void actions() {
		buttonNext.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
		    	currentResult++;
		    	setCurrentResult(currentResult);
			}

		});
		
		buttonPrevious.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
		    	currentResult--;
		    	setCurrentResult(currentResult);
			}

		});
		
	}

	private void initialize() {
        setLayout(null);
        screen = getContentPane();
        setSize(330, 230);
        setClosable(true);
        
        textArea = new JTextArea();
        scroll = new JScrollPane(textArea);
        
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
		panelFields.setBounds(7, 7, 310, 145);
        scroll.setBounds(7, 7, 295, 130);
        panelButtons.setBounds(7, 155, 310, 35);
        buttonHowMany.setBounds(7, 7, 50, 20);
        buttonPrevious.setBounds(60, 7, 80, 20);
        buttonNext.setBounds(150, 7, 80, 20);
        
        buttonHowMany.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
        
    }
    
    public void setSolution(String method, Result result, ParserResult parserResult) {
    	this.method = method;
    	this.result = result;
    	this.parserResult = parserResult;
    	this.currentResult = 1;
    	setCurrentResult(currentResult);
    }

    private void setCurrentResult(int res) {
    	if (res == 1) {
    		buttonPrevious.setEnabled(false);
    	} else {
    		buttonPrevious.setEnabled(true);
    	}
    	
    	if (res == result.getValues().size()) {
    		buttonNext.setEnabled(false);
    	} else {
    		buttonNext.setEnabled(true);
    	}
    	
    	buttonHowMany.setText(res + "/" + result.getValues().size());
    	double[][] resultArray = (double[][]) result.getValues().get(currentResult - 1);

    	String resStr = "";

    	if (Methods.DecomposicaoSVD.equals(this.method)) {
    		if (currentResult == result.getValues().size()) {
				resStr = "Solution:\n";
			} else {
				resStr = "Refining solution:\n";
			}
    		int i = 0;
    		for (String var : parserResult.getVariables()) {
				resStr += var + " = " + resultArray[i++][0] + "\n";
			}
		} else {
			if (resultArray[0].length == 1) {
				if (currentResult == result.getValues().size()) {
					resStr = "Solution:\n";
				} else {
					resStr = "Refining solution:\n";
				}
	    		int i = 0;
	    		for (String var : parserResult.getVariables()) {
					resStr += var + " = " + resultArray[i++][0] + "\n";
				}
			} else {
				for (int i = 0; i < resultArray.length; i++) {
					int j = 0;
					boolean first = true;
					for (String var : parserResult.getVariables()) {
						double coef = resultArray[i][j];
						j++;
						if (coef != 0) {
							if (!first && coef > 0) {
								resStr += "+ ";
							}
							if (coef == 1.0) {
								resStr += var + " ";
							} else {
								resStr += coef + var + " ";
							}
						}
						first = false;
					}
					resStr += "= " + resultArray[i][j] + "\n";
				}
			}
    	}
    	textArea.setText(resStr);
    }
    
    private void makeScreen() {
    	panelFields.add(scroll);
    	
    	panelButtons.add(buttonPrevious);
    	panelButtons.add(buttonHowMany);
    	panelButtons.add(buttonNext);
    	
        screen.add(panelFields);
        screen.add(panelButtons);
    }

}
