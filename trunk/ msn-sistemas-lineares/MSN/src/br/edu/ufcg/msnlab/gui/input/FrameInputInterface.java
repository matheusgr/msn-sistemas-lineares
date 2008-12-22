package br.edu.ufcg.msnlab.gui.input;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.facade.Facade;
import br.edu.ufcg.msnlab.gui.MSNLab;
import br.edu.ufcg.msnlab.gui.controls.CtrMatrixSelect;
import br.edu.ufcg.msnlab.gui.output.FrameOutput;
import br.edu.ufcg.msnlab.gui.util.JDecimalField;
import br.edu.ufcg.msnlab.gui.util.JNumberField;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.util.Config;
import br.edu.ufcg.msnlab.util.ParsedSystem;
import br.edu.ufcg.msnlab.util.Parser;
import br.edu.ufcg.msnlab.util.SystemTypes;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class FrameInputInterface extends JInternalFrame {

	private static final long serialVersionUID = -4714939028473804954L;
	private JPanel panelButtons;
	private JPanel panelFields;

	private Container screen;

	private JLabel textMethods;
	private JLabel textIterations;

	private JNumberField fieldIteration;

	private CtrMatrixSelect fieldExpression;

	private JComboBox comboMethods;

	private JButton buttonSolve;
	private MSNLab msnlab;
	private JButton estimativeButton;
	private JDecimalField residualEdit;
	private JComboBox triangComboBox;
	private JCheckBox pivotBox;
	private JLabel residualLabel;
	private JLabel estimativeLabel;
	private JTable estimativeTable;
	private JScrollPane tableEstimativePanel;
	private JPanel optionsPanel;
	private TableModel estimativeTableModel;

	public FrameInputInterface(MSNLab msnlab) {
		super("Solve System");
		this.msnlab = msnlab;
		initialize();
		positions();
		makeScreen();
		actions();
	}

	private void initialize() {
		setClosable(true);
		setResizable(false);
		setSize(640, 480);
		this.setPreferredSize(new java.awt.Dimension(540, 480));
		this.setBounds(0, 0, 540, 480);
		screen = getContentPane();
		screen.setLayout(null);
		screen.setPreferredSize(new java.awt.Dimension(520, 456));

		panelButtons = new JPanel();
		BorderLayout panelButtonsLayout = new BorderLayout();
		panelButtons.setLayout(panelButtonsLayout);
		panelButtons.setBorder(new EtchedBorder());
		{
			buttonSolve = new JButton("Solve");
			panelButtons.add(buttonSolve, BorderLayout.CENTER);
			buttonSolve.setFocusable(false);
			buttonSolve.setPreferredSize(new java.awt.Dimension(517, 26));
			buttonSolve.setEnabled(false);
		}
		
		panelFields = new JPanel();
		panelFields.setLayout(null);
		panelFields.setBorder(new EtchedBorder());

		fieldExpression = new CtrMatrixSelect();

		screen.setBackground(panelButtons.getBackground());

	}



	private JComboBox makeComboMethods() {
		String[] methods = Methods.getMethods();
		List<String> asList = new ArrayList<String>(Arrays.asList(methods));
		Collections.sort(asList);
		for (String meth : asList) {
			comboMethods.addItem(meth);
		}
		return comboMethods;
	}

	private void positions() {
		panelFields.setBounds(4, 6, 522, 287);

		fieldExpression.setBounds(2, 2, 518, 283);

		panelButtons.setBounds(4, 412, 522, 30);

	}
	
	private void actions() {
		
		comboMethods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String method = (String) comboMethods.getSelectedItem();
				if (Methods.EliminacaoGauss.equals(method) || Methods.EliminacaoGaussJordan.equals(method)) {
					pivotBox.setEnabled(true);
				} else {
					pivotBox.setEnabled(false);
				}
				if (Methods.EliminacaoGauss.equals(method)) {
					triangComboBox.setEnabled(true);
				} else {
					triangComboBox.setEnabled(false);
				}
				if (Methods.isDirect(method)) {
					textIterations.setVisible(true);
					fieldIteration.setVisible(true);
					estimativeTable.setEnabled(false);
					estimativeButton.setEnabled(false);
					estimativeTableModel = new DefaultTableModel();
					estimativeTable.setModel(estimativeTableModel);
					validate();
				} else if (Methods.isIteractive(method)) {
					textIterations.setVisible(true);
					fieldIteration.setVisible(true);
					estimativeTable.setEnabled(true);
					estimativeButton.setEnabled(true);
				}
				buttonSolve.setEnabled(true);
			}
		});
		buttonSolve.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				solver();
			}

		});
		estimativeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				ParsedSystem parse = null;
				try {
					parse = Parser.parse(fieldExpression.getCurrentMatrixControl().getMatrix());
				} catch (MSNException e) {
					msnlab.showError("Could not parse matrix: " + e);
					return;
				}
				estimativeTableModel = new DefaultTableModel(parse.getIncognitas(), 1);
				for (int i = 0; i < estimativeTableModel.getColumnCount(); i++) {
					estimativeTableModel.setValueAt("0", 0, i);
				}
				estimativeTable.setModel(estimativeTableModel);
				validate();
			}

		});
	}

	protected void solver() {
		String method = (String) comboMethods.getSelectedItem();
		boolean pivot = pivotBox.isSelected();
		ParsedSystem parse = null;
		try {
			parse = Parser.parse(fieldExpression.getCurrentMatrixControl().getMatrix());
		} catch (MSNException e) {
			msnlab.showError("Could not parse matrix: " + e);
			return;
		}
		Facade f = new Facade();
		// SystemTypes
		String systemType = f.hasSolution(parse.getCoeficientes(),
				parse.getTermos());
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
			tolerance = Double.parseDouble(residualEdit.getText());
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
		double[] estimatives = null;
		if (Methods.isIteractive(method)) {
			if (estimativeTableModel.getRowCount() != 1) {
				msnlab
				.showError("Please, set estimatives first.");
				return;
			}
			Map<String, Integer> varMap = new HashMap<String, Integer>();
			int j = 0;
			for (String v : parse.getIncognitas()) {
				varMap.put(v, j++);
			}
			estimatives = new double[j];
			for (int i = 0; i < estimativeTableModel.getColumnCount(); i++) {
				String var = estimativeTableModel.getColumnName(i);
				try {
					estimatives[varMap.get(var)] = Double.parseDouble((String) estimativeTableModel.getValueAt(0, varMap.get(var)));
				} catch (NumberFormatException nfe) {
					msnlab
					.showError("Error setting estimatives: " + nfe.getMessage());
					return;
				}
			}
		}
		try {
			Config config = new Config();
			config.set(Config.pivoteamento, pivot);
			config.set(Config.triangularizacao, triangComboBox.getSelectedItem().toString() == "Triang Sup");
			Result result = f.resolve(parse.getCoeficientes(), parse.getTermos(), tolerance, iter, method, estimatives, config);
			FrameOutput fo = new FrameOutput(this.msnlab);
			fo.setSolution(method, result, parse);
            fo.setLocation(new Point(0, 250));
            msnlab.openComponent(fo);
		} catch (Exception e) {
			e.printStackTrace();
			msnlab.showError("Exception solving this system: "
					+ e.getMessage());
		}
	}

	private void makeScreen() {
		panelFields.add(fieldExpression);

		screen.add(panelButtons);
		screen.add(panelFields);
		{
			optionsPanel = new JPanel();
			screen.add(optionsPanel);
			optionsPanel.setBounds(4, 299, 522, 107);
			optionsPanel.setLayout(null);
			optionsPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			{
				textMethods = new JLabel("Methods");
				optionsPanel.add(textMethods);
				textMethods.setBounds(7, 14, 71, 15);
			}
			{
				comboMethods = new JComboBox();
				makeComboMethods();
				comboMethods.setSelectedIndex(-1);
				optionsPanel.add(comboMethods);
				comboMethods.setBounds(90, 10, 212, 22);
			}
			{
				textIterations = new JLabel("Max. Iterations");
				optionsPanel.add(textIterations);
				textIterations.setBounds(314, 14, 122, 15);
			}
			{
				fieldIteration = new JNumberField();
				optionsPanel.add(fieldIteration);
				fieldIteration.setBounds(417, 12, 91, 19);
			}
			{
				tableEstimativePanel = new JScrollPane();
				optionsPanel.add(tableEstimativePanel);
				tableEstimativePanel.setBounds(90, 63, 210, 38);
				estimativeTableModel = new DefaultTableModel();
				estimativeTable = new JTable();
				tableEstimativePanel.setViewportView(estimativeTable);
				estimativeTable.setModel(estimativeTableModel);
			}
			{
				estimativeLabel = new JLabel();
				optionsPanel.add(estimativeLabel);
				estimativeLabel.setText("Estimatives");
				estimativeLabel.setBounds(7, 63, 83, 15);
			}
			{
				residualLabel = new JLabel();
				optionsPanel.add(residualLabel);
				residualLabel.setText("Tolerance");
				residualLabel.setBounds(312, 41, 71, 15);
			}
			{
				pivotBox = new JCheckBox();
				optionsPanel.add(pivotBox);
				pivotBox.setText("Pivot");
				pivotBox.setBounds(90, 39, 61, 19);
				pivotBox.setEnabled(false);
			}
			{
				ComboBoxModel jComboBox1Model = 
					new DefaultComboBoxModel(
							new String[] { "Triang Sup", "Triang Inf" });
				triangComboBox = new JComboBox();
				optionsPanel.add(triangComboBox);
				triangComboBox.setModel(jComboBox1Model);
				triangComboBox.setBounds(156, 37, 144, 22);
				triangComboBox.setEnabled(false);
			}
			{
				residualEdit = new JDecimalField();
				optionsPanel.add(residualEdit);
				residualEdit.setBounds(417, 39, 91, 19);
			}
			{
				estimativeButton = new JButton();
				optionsPanel.add(estimativeButton);
				estimativeButton.setText("Get Estimative Variables");
				estimativeButton.setBounds(312, 70, 197, 22);
				estimativeButton.setEnabled(false);
			}
		}
	}
}
