package br.edu.ufcg.msnlab.methods.LU;


import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.util.Config;

/**
 * This class implements the LU decomposition using Doolittle method.
 * @author Clerton Ribeiro
 * @author Adauto Trigueiro
 *
 */

public class LU implements Solver{
	
	// Attributes of a LU Method
	private MatrixLU matrix,u,l,a2;
	private int size;
	private List<Double> coefficientList;
	private double[] b,y;
	private double[][] extendedMatrix,x;
	private double aprox;
	
	// private ResultMSN results;
	private List<double[][]> results;
	
	
	/**
	 * Constructor of a LUMethod class
	 */
	public LU() {
		this.coefficientList = new ArrayList<Double>();
		this.results = new ArrayList<double[][]>();
		this.size = 0;
		this.u = null;
		this.l = null;
		this.a2 = null;
		this.b = null;
		this.x = null;
		this.y = null;
	}
	
	/**
	 * Sets variables of LU method
	 */
	private void setUp() {
		this.size = this.matrix.getNumRows();
		this.u = this.matrix.getMatrix(this.size-1, this.size-1);
		this.l = new MatrixLU(new double[this.size][this.size],this.size,this.size);
		this.a2 = this.matrix.getMatrix(this.size-1, this.size-1);
		
		this.b = this.matrix.getColumnToArray(this.size);
		this.x = new double[this.b.length][1];
		this.y = new double[this.b.length];
		
	}
	
	/**
	 * Returns a linear combination of a row
	 * @param _firstRow the base row to do the linear combination .
	 * @param _secondRow the row to do the linear combination with the first one.
	 * @param _column the column of the element of second row that will be nulled
	 * @return a linear combination of two rows
	 */
	private double[] linearCombination(int _firstRow, int _secondRow, int _column) {
		double[] firstRow = this.u.getRowToArray(_firstRow);
		double[] secondRow = this.u.getRowToArray(_secondRow);
		double[] result = new double[firstRow.length];
		double coefficient = secondRow[_column]/firstRow[_column];
		for(int i = 0;i < result.length;i++) {
			result[i] = secondRow[i] - (coefficient*firstRow[i]);
			this.u.setElement(_secondRow, i, result[i]);
			this.a2.setElement(_secondRow, i, result[i]);
		}
		this.coefficientList.add(coefficient);
		return result;
	}
	
	/**
	 * Decompose the main matrix to two matrices L and U
	 */
	private void matrixDecomposition() {
		for(int column = 0; column < this.size; column++) {
			for(int row = column+1; row < this.size; row++) {
				linearCombination(column, row, column);
			}
		}
		insertCoefficients();
		diagonalizeL();
	}
	
	/**
	 * Constructs matrices L and the new matrix A
	 */
	private void insertCoefficients() {
		int index = 0;
		for(int column = 0; column < this.size; column++) {
			for(int row = column+1; row < this.size; row++) {
				this.a2.setElement(row, column, coefficientList.get(index));
				this.l.setElement(row, column, coefficientList.get(index));
				index++;
			}
		}
	}
	
	/**
	 * Puts the number 1 to the main diagonal of matrix L
	 */
	private void diagonalizeL() {
		for(int i=0;i<this.size;i++) {
			this.l.setElement(i, i, 1);
		}
	}

	/**
	 * Calculates the matrix Y used to solve the linear equation 
	 */
	private void calculateY() {
		for(int row = 0;row < this.y.length;row++) {
			this.y[row] = this.b[row];
			for(int i=0;i<row;i++) {
				this.y[row] -= this.l.getElement(row, i)*this.y[i];
			}
		}
	}
	
	/**
	 * Calculates the solution of the linear systems
	 */
	private void calculateX() {
		for(int row = this.x.length-1;row >= 0;row--) {
			this.x[row][0] = this.y[row];
			for(int i=row;i<this.x.length-1;i++) {
				this.x[row][0] -= (this.u.getElement(row,i+1)*this.x[i+1][0]);
			}
			this.x[row][0] /= this.u.getElement(row, row);
		}
	}
	
	/**
	 * Gets the matrix X, the result with error
	 * @return
	 */
	public double[][] getResult() {
		return this.x;
	}
	
	/**
	 * Construct the increased matrix with the coefficients matrix and the
	 * independent terms matrix.
	 * 
	 * @param coeficients The coefficients matrix.
	 * @param terms The independent terms matrix.
	 * @return The increased matrix.
	 */
	private double[][] increasedMatrix(double[][] coeficients, double[] terms) {
		double[][] mat = new double[coeficients.length][coeficients.length + 1];
		for (int i = 0; i < coeficients.length; i++) {
			for (int j = 0; j < coeficients.length; j++) {
				mat[i][j] = coeficients[i][j];
			}
		}
		for (int i = 0; i < terms.length; i++) {
			mat[i][coeficients.length] = terms[i];
		}
		this.results.add(copyMatrix(mat));
		return mat;
	}
	
	/**
	 * Copy all the terms of one matrix in another one.
	 * @param matrix The matrix to be copied.
	 * @return The matrix copy.
	 */
	private double[][] copyMatrix(double[][] matrix) {
		double[][] copy = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				copy[i][j] = matrix[i][j];
			}
		}
		return copy;
	}

	/**
	 * Calculate the residue of the answer calculated by this method.
	 * 
	 * @param matrix
	 *            The main matrix of the equation system.
	 * @param vet
	 *            The vector-answer.
	 * @param residue
	 *            The residue to be compared.
	 * @return The final vector-answer, adjusted with the residue.
	 */
	private double[][] fitResidue(double[][] matrix, double[][] vet,
			double residue) {
		double[] vetResults = new double[vet.length];
		for (int i = 0; i < matrix.length; i++) {
			double currentResult = 0;
			for (int j = 0; j < matrix.length; j++) {
				currentResult += matrix[i][j] * vet[j][0];
			}
			vetResults[i] = matrix[i][matrix.length] - currentResult;
		}

		verifyResidue(matrix, vetResults, residue, vet);
		return vet;
	}
	
	/**
	 * Verify if the results are ok with the given residue.
	 * 
	 * @param matrix
	 *            The main matrix of the equation system.
	 * @param vetResults
	 *            The vector of the residues of each variable.
	 * @param residue
	 *            The residue to be compared.
	 * @param vet
	 *            The vector-answer.
	 */
	private void verifyResidue(double[][] matrix, double[] vetResults,
			double residue, double[][] vet) {
		boolean changed = false;
		for (int j = 0; j < vetResults.length; j++) {
			if (Math.abs(vetResults[j]) > residue) {
				vet[j][0] += vetResults[j];
				changed = true;
			}
		}
		if (changed) {
			this.results.add(vet);
			fitResidue(matrix, vet, residue);
		}

	}

	/**
	 * Don't used
	 */
	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config)
			throws MSNException {
		// This class won't use this method, because is a direct method and
		// don't need of a initial estimative.
		return null;
	}

	/**
	 * Resolve the system represented by the values passed.
	 * @param coeficients The matrix that contains the coefficients of the system.
	 * @param terms The independent terms of the system.
	 * @param aprox The residue of the error in the system.
	 * @param iteracoesMax The maximum number of iterations.
	 * @param config The configuration for this method.
	 */
	@Override
	public Result solve(double[][] coeficientes, double[] termos, double aprox,
			int iteracoesMax, Config config) throws MSNException {
		
		this.aprox = aprox;
		this.extendedMatrix = increasedMatrix(coeficientes,termos);
		this.matrix = new MatrixLU(this.extendedMatrix,this.extendedMatrix.length,this.extendedMatrix.length);
		
		setUp();
		
		matrixDecomposition();
		
		this.calculateY();
		
		this.calculateX();

		double[][] mat = fitResidue(this.matrix.toArray(), getResult(), this.aprox);
		this.results.add(copyMatrix(mat));
		return new ResultMSN(this.results);
	}
}