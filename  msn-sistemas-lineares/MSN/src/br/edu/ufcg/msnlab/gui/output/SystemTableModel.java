package br.edu.ufcg.msnlab.gui.output;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

import br.edu.ufcg.msnlab.methods.Result;

public class SystemTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Result result;
	
	private int curIter;

	private String[] variables;

	public void setCurrentResult(JLabel label, Result result, int curIter, String[] strings) {
		label.setText("Changing Matrix");
		this.result = result;
		this.curIter = curIter;
		this.variables = strings;
		fireTableStructureChanged();
	}

	@Override
	public int getColumnCount() {
    	double[][] resultArray = (double[][]) result.getValues().get(curIter - 1);
		return resultArray[0].length;
	}

	@Override
	public int getRowCount() {
		double[][] resultArray = (double[][]) result.getValues().get(curIter - 1);
		return resultArray.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return ((double[][]) result.getValues().get(curIter - 1))[rowIndex][columnIndex];
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == getColumnCount() - 1) {
			return "Terms";
		}
		return this.variables[columnIndex];					
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Double.class;
	}

}
