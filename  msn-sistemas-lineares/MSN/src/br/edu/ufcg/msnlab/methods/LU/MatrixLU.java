package br.edu.ufcg.msnlab.methods.LU;

public class MatrixLU {

	private double[][] matrix;
	private int rows,columns;
	
	public MatrixLU(double[][] _matrix,int _rows,int _columns) {
		this.matrix = _matrix;
		this.rows = _rows;
		this.columns = _columns;
	}
	
	public MatrixLU(int _rows, int _columns) {
		this.matrix = new double[_rows][_columns];
		this.rows = _rows;
		this.columns = _columns;
	}
	
	public void printMatrix() {
		for(int i=0; i < rows; i++) {
			for(int j=0; j <columns; j++) {
				System.out.print(matrix[i][j] + "   ");
			}
			System.out.println("\n");
		}
		System.out.println();
	}
	
	public MatrixLU getMatrix(int _row, int _column) {
		int numRows = _row+1;
		int numColumns = _column+1;
		double[][] aux = new double[numRows][numColumns];
		for(int i=0;i<numRows;i++) {
			for(int j=0;j<numColumns;j++) {
				aux[i][j] = this.matrix[i][j];
			}
		}
		return new MatrixLU(aux,numRows,numColumns);
	}
	
	public double getElement(int _row, int _column) {
		return this.matrix[_row][_column];
	}
	
	public void setElement(int _row, int _column, double value) {
		this.matrix[_row][_column] = value;
	}
	
	public MatrixLU getColumn(int _column) {
		double[][] aux = new double[this.rows][1];
		for(int i=0; i < this.rows;i++) {
			aux[i][0]=this.matrix[i][_column];
		}
		return new MatrixLU(aux,this.rows,1);
	}
	
	public double[] getColumnToArray(int _column) {
		double[] aux = new double[this.rows];
		for(int i=0; i < this.rows;i++) {
			aux[i]=this.matrix[i][_column];
		}
		return aux;
	}
	
	public MatrixLU getRow(int _row) {
		double[][] aux = new double[1][this.columns];
		for(int i=0; i < this.columns;i++) {
			aux[0][i]=this.matrix[_row][i];
		}
		return new MatrixLU(aux,1,this.columns);
	}
	
	public double[] getRowToArray(int _row) {
		double[] aux = new double[this.columns];
		for(int i=0; i < this.columns;i++) {
			aux[i]=this.matrix[_row][i];
		}
		return aux;
	}
	
	public double[][] toArray() {
		return this.matrix;
	}
	
	public int getNumRows() {
		return this.rows;
	}
	
	public int getNumColumns() {
		return this.columns;
	}
	
	public String toString() {
		String matrixRepresentation = "A = {";
		for(int i=0; i < rows; i++) {
			for(int j=0; j <columns; j++) {
				matrixRepresentation += "[A"+i+j+":"+matrix[i][j] + "]";
			}
		}
		return matrixRepresentation +="}";
	}
	
	public MatrixLU clone() {
		return new MatrixLU(this.matrix,this.rows,this.columns);
	}
}
