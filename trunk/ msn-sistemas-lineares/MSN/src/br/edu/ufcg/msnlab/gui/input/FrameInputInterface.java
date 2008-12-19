package br.edu.ufcg.msnlab.gui.input;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.border.EtchedBorder;

import br.edu.ufcg.msnlab.facade.Facade;
import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.gui.controls.CtrMatrixSelect;
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

public class FrameInputInterface extends JInternalFrame {

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

	private CtrMatrixSelect fieldExpression;

	private JComboBox comboMethods;

	private JButton buttonSolve;
	private MSNLab msnlab;

	public FrameInputInterface(MSNLab msnlab) {
		super("Solve System");
		this.msnlab = msnlab;
		initialize();
		positions();
		resizeComponents();
		actions();
		makeScreen();
	}

	private void initialize() {
		setClosable(true);
		setResizable(true);
		setSize(640, 480);
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
		//fieldExpression = new JTextArea();
		fieldExpression = new CtrMatrixSelect();

		//scrollExpression = new JScrollPane(fieldExpression);

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
		//scrollExpression.setBounds(80, 7, 220, 80);
		fieldExpression.setBounds( 80, 7, 220, 80 );
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
	
	private void resizeComponents(){

		
		panelButtons.setBounds( 7, getHeight()-panelButtons.getHeight()-35, getWidth()-panelFields.getLocation().x*2-8, panelButtons.getHeight() );
		
		panelFields.setSize( getWidth()-panelFields.getLocation().x*2-8, panelButtons.getLocation().y-panelFields.getLocation().y*2 );
		
		int width = panelFields.getWidth() - fieldExpression.getLocation().x-7;
		int height = panelFields.getHeight() - fieldExpression.getLocation().y-7 -50;
		fieldExpression.setSize( width, height );
		
		textMethods.setLocation( 0, panelFields.getHeight() - 50 );
		comboMethods.setLocation( 80, panelFields.getHeight() - 50 );

		textTolerance.setLocation( 0, panelFields.getHeight() - 25 );
		fieldTolerance.setLocation( 80, panelFields.getHeight() - 25 );

		textIterations.setLocation( 140, panelFields.getHeight() - 25 );
		fieldIteration.setLocation( 240, panelFields.getHeight() - 25 );
}

	private void actions() {
		
		addComponentListener( new ComponentAdapter(){

			public void componentResized(ComponentEvent arg0) {
				resizeComponents();				
			}

		});
		
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
		ParserResult parserResult = ExpressionParser.parse(fieldExpression.getCurrentMatrixControl().getMatrix());
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
				fo.setSolution(method, result, parserResult);
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
		panelFields.add(fieldExpression);
		panelFields.add(comboMethods);

		panelButtons.add(buttonSolve);

		screen.add(panelButtons);
		screen.add(panelFields);
	}

}
