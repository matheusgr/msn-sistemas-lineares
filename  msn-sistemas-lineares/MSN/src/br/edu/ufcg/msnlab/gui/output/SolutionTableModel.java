package br.edu.ufcg.msnlab.gui.output;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import br.edu.ufcg.msnlab.methods.Result;

public class SolutionTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Result result;
	
	private int curIter;

	private String[] variables;

	private JLabel label;

	public void setCurrentResult(JLabel label, Result result, int curIter, String[] strings) {
		this.label = label;
		this.result = result;
		this.curIter = curIter;
		this.variables = strings;
		fireTableStructureChanged();
	}

	@Override
	public int getColumnCount() {
		if (result.getValues().size() - curIter > 0) {
			return 3;
		} else {
			return 2;
		}
	}

	@Override
	public int getRowCount() {
		return variables.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return this.variables[rowIndex];			
		case 1:
			return ((double[][]) result.getValues().get(this.curIter - 1))[rowIndex][0];				
		case 2:
			return ((double[][]) result.getValues().get(this.curIter))[rowIndex][0];
		default:
			throw new RuntimeException("Unexpect column index");
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "Variables";
		}
		if (getColumnCount() == 2) {
			label.setText("Solution");
			return "Values";
		} else {
			label.setText("Finding solution");
			switch (columnIndex) {
			case 1:
				return "Iteration " + (curIter);
			case 2:
				if (result.getValues().size() == curIter + 1) {
					return "Solution";
				} else {
					return "Iteration " + (curIter + 1);
				}
			}
			throw new RuntimeException("Unexpect column index");
		}
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		default:
			return Double.class;
		}
	}

}
