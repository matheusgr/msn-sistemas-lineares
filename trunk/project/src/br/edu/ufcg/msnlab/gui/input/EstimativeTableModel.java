package br.edu.ufcg.msnlab.gui.input;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

public class EstimativeTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private Set<String> variables;

	public EstimativeTableModel(Set<String> variables) {
		this.variables = variables;
	}
	
	@Override
	public int getColumnCount() {
		return variables.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return variables.toArray(new String[] {})[columnIndex];
	}
	
	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}

}
