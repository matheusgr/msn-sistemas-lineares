package br.edu.ufcg.msnlab.methods.LU;

/**
 * This class implements a matrix.
 * @author Clerton Ribeiro
 * @author Adauto Trigueiro
 *
 */

public class MatrixLU {

	//Attributes of a matrix
	private double[][] matrix;
	private int rows,columns;
	
	/**
	 * Constructor of a Matrix class
	 * @param _matrix An array representing the matrix
	 * @param _rows Number of rows
	 * @param _columns Number of columns
	 */
	public MatrixLU(double[][] _matrix,int _rows,int _columns) {
		this.matrix = _matrix;
		this.rows = _rows;
		this.columns = _columns;
	}
	
	/**
	 * Constructor of a Matrix class
	 * @param _rows Number of rows
	 * @param _columns Number of columns
	 */
	public MatrixLU(int _rows, int _columns) {
		this.matrix = new double[_rows][_columns];
		this.rows = _rows;
		this.columns = _columns;
	}
	
	/**
	 * Debug: prints a matrix on console
	 */
	public void printMatrix() {
		for(int i=0; i < rows; i++) {
			for(int j=0; j <columns; j++) {
				System.out.print(matrix[i][j] + "   ");
			}
			System.out.println("\n");
		}
		System.out.println();
	}
	
	/**
	 * Gets a submatrix inside a matrix
	 * @param _row Initial row
	 * @param _column Final column
	 * @return A submatrix of a given row and column
	 */
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
	
	/**
	 * Gets a element of a matrix
	 * @param _row Row number of an element
	 * @param _column Column number of an element
	 * @return An element of a matrix
	 */
	public double getElement(int _row, int _column) {
		return this.matrix[_row][_column];
	}
	
	/**
	 * Sets a new value for an element in the matrix
	 * @param _row Row number of an element
	 * @param _column Column number of an element
	 * @param value The new element value
	 */
	public void setElement(int _row, int _column, double value) {
		this.matrix[_row][_column] = value;
	}
	
	/**
	 * Gets a column of the matrix
	 * @param _column Column number
	 * @return A new matrix just with the desired column number
	 */
	public MatrixLU getColumn(int _column) {
		double[][] aux = new double[this.rows][1];
		for(int i=0; i < this.rows;i++) {
			aux[i][0]=this.matrix[i][_column];
		}
		return new MatrixLU(aux,this.rows,1);
	}
	
	/**
	 * Gets a column of the matrix
	 * @param _column Column number
	 * @return A array just with the desired column number
	 */
	public double[] getColumnToArray(int _column) {
		double[] aux = new double[this.rows];
		for(int i=0; i < this.rows;i++) {
			aux[i]=this.matrix[i][_column];
		}
		return aux;
	}
	
	/**
	 * Gets a row of the matrix
	 * @param _row Row number
	 * @return A new matrix just with desired row number
	 */
	public MatrixLU getRow(int _row) {
		double[][] aux = new double[1][this.columns];
		for(int i=0; i < this.columns;i++) {
			aux[0][i]=this.matrix[_row][i];
		}
		return new MatrixLU(aux,1,this.columns);
	}
	
	/**
	 * Gets a row of the matrix
	 * @param _row row number
	 * @return A array just with desired row number
	 */
	public double[] getRowToArray(int _row) {
		double[] aux = new double[this.columns];
		for(int i=0; i < this.columns;i++) {
			aux[i]=this.matrix[_row][i];
		}
		return aux;
	}
	
	/**
	 * Gives a 2-D array representation of a matrix
	 * @return A 2-D array representation of a matrix
	 */
	public double[][] toArray() {
		return this.matrix;
	}
	
	/**
	 * Gets the number of rows
	 * @return Rows number of the matrix
	 */
	public int getNumRows() {
		return this.rows;
	}
	
	/**
	 * Gets the number of columns
	 * @return Columns number of the matrix
	 */
	public int getNumColumns() {
		return this.columns;
	}
	
	/**
	 * Returns a String representation for the matrix
	 */
	public String toString() {
		String matrixRepresentation = "A = {";
		for(int i=0; i < rows; i++) {
			for(int j=0; j <columns; j++) {
				matrixRepresentation += "[A"+i+j+":"+matrix[i][j] + "]";
			}
		}
		return matrixRepresentation +="}";
	}
}
