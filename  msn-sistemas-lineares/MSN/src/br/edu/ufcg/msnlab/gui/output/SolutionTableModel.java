package br.edu.ufcg.msnlab.gui.output;

import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import br.edu.ufcg.msnlab.methods.Result;

public class SolutionTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Result result;
	
	private int curIter;

	private Collection<String> variables;

	public void setCurrentResult(Result result, int curIter, Collection<String> variables) {		
		this.result = result;
		this.curIter = curIter;
		this.variables = variables;
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
		return variables.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return this.variables.toArray(new String[] {})[rowIndex];			
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
			return "Solution";
		} else {
			if (result.getValues().size() == curIter) {
				return "Solution";
			}
			switch (columnIndex) {
			case 1:
				return "Iteration " + (curIter - 1);				
			case 2:
				return "Iteration " + (curIter);
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
