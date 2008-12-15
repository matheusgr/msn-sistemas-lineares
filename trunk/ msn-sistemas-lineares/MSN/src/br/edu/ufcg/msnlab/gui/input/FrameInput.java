package br.edu.ufcg.msnlab.gui.input;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.facade.Facade;
import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.gui.output.FrameOutput;
import br.edu.ufcg.msnlab.gui.util.ExpressionParser;
import br.edu.ufcg.msnlab.gui.util.JDecimalField;
import br.edu.ufcg.msnlab.gui.util.JNumberField;
import br.edu.ufcg.msnlab.gui.util.ExpressionParser.ParserError;
import br.edu.ufcg.msnlab.gui.util.ExpressionParser.ParserResult;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.util.Config;
import br.edu.ufcg.msnlab.util.SystemTypes;

public class FrameInput extends JInternalFrame {

	private static final long serialVersionUID = -4714939028473804954L;
	private JPanel panelButtons;
	private JPanel panelFields;

	private Container screen;

	private JLabel textExpression;
	private JLabel textMethods;
	private JLabel textTolerance;
	private JLabel textIterations;

	private JDecimalField fieldTolerance;

	private JNumberField fieldIteration;

	private JTextArea fieldExpression;

	private JScrollPane scrollExpression;

	private JComboBox comboMethods;

	private JButton buttonSolve;
	private MSNLab msnlab;

	public FrameInput(MSNLab msnlab) {
		super("Solve Equation");
		this.msnlab = msnlab;
		initialize();
		positions();
		actions();
		makeScreen();
	}

	private void initialize() {
		setClosable(false);
		setSize(335, 230);
		screen = getContentPane();
		screen.setLayout(null);

		panelButtons = new JPanel();
		panelButtons.setLayout(null);
		panelButtons.setBorder(new EtchedBorder());

		panelFields = new JPanel();
		panelFields.setLayout(null);
		panelFields.setBorder(new EtchedBorder());

		textExpression = new JLabel("System: ");
		textIterations = new JLabel("Max. Iterations: ");
		textMethods = new JLabel("Method: ");
		textTolerance = new JLabel("Tolerance: ");

		fieldIteration = new JNumberField();
		fieldTolerance = new JDecimalField();
		fieldExpression = new JTextArea();

		scrollExpression = new JScrollPane(fieldExpression);

		comboMethods = makeComboMethods();

		buttonSolve = new JButton("Solve");

		buttonSolve.setFocusable(false);

		screen.setBackground(panelButtons.getBackground());

	}

	private static String[] getMethods() {
		Field[] fields = Methods.class.getFields();
		String[] methods = new String[fields.length];
		for (int i = 0; i < methods.length; i++) {
			try {
				methods[i] = (String) fields[i].get(null);
			} catch (Exception e) {
				throw new RuntimeException("Could not get methods.");
			}
		}
		return methods;
	}

	private JComboBox makeComboMethods() {
		JComboBox combo = new JComboBox();
		String[] methods = getMethods();
		List<String> asList = new ArrayList<String>(Arrays.asList(methods));
		asList.add(Methods.EliminacaoGauss + "_WITH_PIVOT");
		asList.add(Methods.EliminacaoGaussJordan + "_WITH_PIVOT");
		Collections.sort(asList);
		for (String meth : asList) {
			combo.addItem(meth);
		}
		return combo;
	}

	private void positions() {
		panelFields.setBounds(7, 7, 310, 145);

		textExpression.setBounds(0, 7, 80, 20);
		scrollExpression.setBounds(80, 7, 220, 80);
		textMethods.setBounds(0, 92, 80, 20);
		comboMethods.setBounds(80, 92, 220, 20);

		textTolerance.setBounds(0, 117, 80, 20);
		fieldTolerance.setBounds(80, 117, 60, 20);

		textIterations.setBounds(140, 117, 100, 20);
		fieldIteration.setBounds(240, 117, 60, 20);

		panelButtons.setBounds(7, 155, 310, 35);
		buttonSolve.setBounds(92, 7, 105, 20);

		textExpression.setHorizontalAlignment(JLabel.RIGHT);
		textMethods.setHorizontalAlignment(JLabel.RIGHT);
		textTolerance.setHorizontalAlignment(JLabel.RIGHT);
		textIterations.setHorizontalAlignment(JLabel.RIGHT);
	}

	private void actions() {
		comboMethods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String method = (String) comboMethods.getSelectedItem();
				if (Methods.isDirect(method)) {
					textIterations.setVisible(true);
					textTolerance.setVisible(true);
					fieldIteration.setVisible(true);
					fieldTolerance.setVisible(true);
				} else if (Methods.isIteractive(method)) {
					textIterations.setVisible(true);
					textTolerance.setVisible(true);
					fieldIteration.setVisible(true);
					fieldTolerance.setVisible(true);
				}
			}
		});
		buttonSolve.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				solver();
			}

		});

	}

	protected void solver() {
		String method = (String) comboMethods.getSelectedItem();
		boolean pivot = false;
		if (method.endsWith("_WITH_PIVOT")) {
			method = method.substring(0, method.length() - "_WITH_PIVOT".length());
			System.out.println(method);
			pivot = true;
		}
		ExpressionParser ep = new ExpressionParser();
		ParserResult parserResult = ep.parse(fieldExpression.getText());
		ArrayList<ParserError> errors = parserResult.getErrors();
		if (errors.size() > 0) {
			String message = "";
			for (ParserError parserError : errors) {
				message += parserError.toString() + "\n";
			}
			msnlab.showError(message);
		} else {
			Facade f = new Facade();
			// SystemTypes
			String systemType = f.hasSolution(parserResult.getCoef(),
					parserResult.getTerms());
			if (SystemTypes.HETEROGENEO.equals(systemType)) {
				msnlab.showError("This system isn't homogeneous.");
				return;
			} else if (SystemTypes.IMPOSSIVEL.equals(systemType)) {
				msnlab.showError("This system doesn't have a solution.");
				return;
			} else if (SystemTypes.POSSIVELINDET.equals(systemType)) {
				if (Methods.isDirect(method)) {
					msnlab
							.showError("This system has more than one solution. Use an iterative method (Seidel or Jacobi).");
					return;
				}
			}
			double tolerance;
			try {
				tolerance = Double.parseDouble(fieldTolerance.getText());
			} catch (NumberFormatException nfe) {
				msnlab
				.showError("Invalid tolerance value: " + nfe.getMessage());
				return;
			}
			int iter;
			try {
				iter = Integer.parseInt(fieldIteration.getText());
			} catch (NumberFormatException nfe) {
				msnlab
				.showError("Invalid iterations value: " + nfe.getMessage());
				return;
			}
			try {
				Config config = new Config();
				config.set(Config.pivoteamento, pivot);
				Result result = f.resolve(parserResult.getCoef(), parserResult
						.getTerms(), tolerance, iter, method, parserResult
						.getTerms(), config);
				FrameOutput fo = new FrameOutput(this.msnlab);
				fo.setSolution(method, result);
                fo.setLocation(new Point(0, 250));
                msnlab.openComponent(fo);
			} catch (Exception e) {
				e.printStackTrace();
				msnlab.showError("Exception solving this system: "
						+ e.getMessage());
			}
		}
	}

	private void makeScreen() {
		panelFields.add(textExpression);
		panelFields.add(textIterations);
		panelFields.add(textMethods);
		panelFields.add(textTolerance);
		panelFields.add(fieldIteration);
		panelFields.add(fieldTolerance);
		panelFields.add(scrollExpression);
		panelFields.add(comboMethods);

		panelButtons.add(buttonSolve);

		screen.add(panelButtons);
		screen.add(panelFields);
	}

}
