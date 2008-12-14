package br.edu.ufcg.msnlab.methods.LU;


import java.math.BigDecimal;
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
	private int size,iter;
	private List<Double> coefficientList;
	private double[] b,y,x;
	private double[][] matrix,u,l,a2;
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
		this.size = this.matrix.length;
		this.u = copyMatrix(matrix);
		this.l = new double[this.size][this.size];
		this.a2 = copyMatrix(matrix);

		this.x = new double[this.b.length];
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
		double[] firstRow = new double[this.u.length];
		double[] secondRow = new double[this.u.length];
		for(int i=0;i<this.u.length;i++) {
			firstRow[i] = this.u[_firstRow][i];
			secondRow[i] = this.u[_secondRow][i];
		}

		double[] result = new double[firstRow.length];
		double coefficient = secondRow[_column]/firstRow[_column];
		for(int i = 0;i < result.length;i++) {
			result[i] = secondRow[i] - (coefficient*firstRow[i]);
			this.u[_secondRow][i] = result[i];
			this.a2[_secondRow][i] = result[i];
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
				this.a2[row][column] = coefficientList.get(index);
				this.l[row][column] = coefficientList.get(index);
				index++;
			}
		}
	}

	/**
	 * Puts the number 1 to the main diagonal of matrix L
	 */
	private void diagonalizeL() {
		for(int i=0;i<this.size;i++) {
			this.l[i][i] = 1;
		}
	}

	/**
	 * Calculates the matrix Y used to solve the linear equation 
	 */
	private void calculateY() {
		for(int row = 0;row < this.y.length;row++) {
			this.y[row] = this.b[row];
			for(int i=0;i<row;i++) {
				this.y[row] -= this.l[row][i]*this.y[i];
			}
		}
	}

	/**
	 * Calculates the solution of the linear systems
	 */
	private void calculateX() {
		for(int row = this.x.length-1;row >= 0;row--) {
			this.x[row] = this.y[row];
			for(int i=row;i<this.x.length-1;i++) {
				this.x[row] -= (this.u[row][i+1]*this.x[i+1]);
			}
			this.x[row] /= this.u[row][row];
		}
	}

	/**
	 * Gets the matrix X, the result with error
	 * @return
	 */
	public double[] getResult() {
		return this.x;
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

	private double[] copyArray(double[] array) {
		double[] copy = new double[array.length];
		for(int i=0;i<array.length;i++) {
			copy[i] = array[i];
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
	private double[] fitResidue(double[][] matrix, double[] vet,double residue) {
		BigDecimal[] vetResidue = new BigDecimal[vet.length];
		boolean hasResidue = false;
		for (int i = 0; i < matrix.length; i++) {
			BigDecimal currentResult = new BigDecimal("0");
			for (int j = 0; j < matrix.length; j++) {
				currentResult = (new BigDecimal(matrix[i][j]).multiply(new BigDecimal(vet[j])).add(currentResult));
			}
			vetResidue[i] = new BigDecimal(b[i]).abs().subtract(currentResult.abs());
			if(vetResidue[i].abs().compareTo(new BigDecimal(residue)) > 0) {
				hasResidue = true;
			}
		}
		if(hasResidue) {
			double[] oldB = copyArray(this.b);
			double[] oldResult = copyArray(this.x);
			for(int i=0;i<this.b.length;i++) {
				this.b[i] = vetResidue[i].doubleValue();
			}
			double[] delta = new double[this.x.length];
			matrixDecomposition();
			this.calculateY();
			this.calculateX();
			delta = getResult();
			this.b = copyArray(oldB);
			this.x = copyArray(oldResult);
			double[] newb = new double[this.b.length];
			for(int i=0;i<delta.length;i++) {
				newb[i] = this.x[i]+delta[i];
			}
			this.x = copyArray(newb);
		}
		return vet;
	}

	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config)
			throws MSNException {
		// TODO Auto-generated method stub
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
		this.matrix = coeficientes;
		this.b = termos;
		this.iter = iteracoesMax;
		setUp();

		matrixDecomposition();

		this.calculateY();

		this.calculateX();

		double[][] resultMatrix = new double[this.x.length][1];
		for(int i=0;i<resultMatrix.length;i++) {
			resultMatrix[i][0] = this.x[i];
		}
		
		this.results.add(resultMatrix);
		
		double[] mat = fitResidue(this.matrix,this.x,this.aprox);
		double[][] finalResultMatrix = new double[this.x.length][1];
		for(int i=0;i<finalResultMatrix.length;i++) {
			finalResultMatrix[i][0] = mat[i];
		}
		
		this.results.add(finalResultMatrix);
		
		return new ResultMSN(this.results);
	}
}