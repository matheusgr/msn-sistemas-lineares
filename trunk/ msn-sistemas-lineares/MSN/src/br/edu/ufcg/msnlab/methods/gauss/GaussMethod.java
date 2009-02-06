package br.edu.ufcg.msnlab.methods.gauss;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.util.Config;


/**
 * This class implements the Gauss' elimination with and without pivoting.
 * @author João Felipe
 * @author Everton Leandro
 *
 */
public class GaussMethod implements Solver {

	// Attributes of a Gauss Method
	private double[][] coefficientsMatrix;
	private double[] independentTerms;
	private double[][] matrix;

	// private ResultMSN results;
	private List<double[][]> results;
	private double[][] origMatrix;

	/**
	 * Constructor of a GaussMethod class
	 */
	public GaussMethod() {
		this.coefficientsMatrix = null;
		this.independentTerms = null;
		this.matrix = null;
		this.results = new ArrayList<double[][]>();
	}

	/**
	 * Don't used
	 */
	public ResultMSN solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config) {
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
	public ResultMSN solve(double[][] coeficients, double[] terms,
			double aprox, int iteracoesMax, Config config) throws MSNException {
		this.coefficientsMatrix = coeficients;
		this.independentTerms = terms;
		this.matrix = increasedMatrix(this.coefficientsMatrix,
				this.independentTerms);
		this.origMatrix = copyMatrix(matrix);
		organizeMatrix(this.matrix);
		boolean triangSup = (Boolean) config.get(Config.triangularizacao);
		boolean pivoting = (Boolean) config.get(Config.pivoteamento);
		if (pivoting) {
			diagonalyzeMatrixWithPivoting(this.matrix, triangSup);
		} else {
			diagonalyzeMatrixWithoutPivoting(this.matrix, triangSup);
		}
		double[][] vet = backReplacement(this.matrix, triangSup);
		int iterations = 0;
		boolean resultFit = fitResidue(this.origMatrix, vet, aprox);
		while(iterations < iteracoesMax && !resultFit){
			resultFit = fitResidue(this.origMatrix, vet, aprox);
		}
		this.results.add(copyMatrix(vet));
		return new ResultMSN(this.results);
	}

	// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

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
	 * Diagonalyze the given increased matrix of the system with the pivoting
	 * technique.
	 * 
	 * @param matrix The increased matrix of the system.
	 * @param triangSup A boolean value indicating if the superior triangular or
	 * inferior triangular will be considered.
	 */
	private void diagonalyzeMatrixWithPivoting(double[][] matrix,
			boolean triangSup) {
		if (triangSup) {
			for (int i = 0; i < matrix.length - 1; i++) {
				pivotMatrixUpDown(matrix, i);
				for (int j = i + 1; j < matrix.length; j++) {
					double multiplier = matrix[j][i] / matrix[i][i];
					for (int k = i; k < matrix[0].length; k++) {
						matrix[j][k] = matrix[j][k]
								- (multiplier * matrix[i][k]);
					}
				}
				this.results.add(copyMatrix(matrix));
			}
		} else {
			for (int i = matrix.length - 1; i > 0; i--) {
				pivotMatrixDownUp(matrix, i);
				for (int j = i - 1; j >= 0; j--) {
					double multiplier = matrix[j][i] / matrix[i][i];
					for (int k = 0; k < matrix[0].length; k++) {
						matrix[j][k] = matrix[j][k]
								- (multiplier * matrix[i][k]);
					}
				}
				this.results.add(copyMatrix(matrix));
			}
		}
	}

	/**
	 * Diagonalyze the given increased matrix of the system without the pivoting
	 * technique.
	 * 
	 * @param matrix The increased matrix of the system.
	 * @param triangSup A boolean value indicating if the superior triangular or
	 * inferior triangular will be considered.
	 */
	private void diagonalyzeMatrixWithoutPivoting(double[][] matrix,
			boolean triangSup) {
		if (triangSup) {
			for (int i = 0; i < matrix.length - 1; i++) {
				for (int j = i + 1; j < matrix.length; j++) {
					double multiplier = matrix[j][i] / matrix[i][i];
					for (int k = i; k < matrix[0].length; k++) {
						matrix[j][k] = matrix[j][k]
								- (multiplier * matrix[i][k]);
					}
				}
				this.results.add(copyMatrix(matrix));
			}
		} else {
			for (int i = matrix.length - 1; i > 0; i--) {
				for (int j = i - 1; j >= 0; j--) {
					double multiplier = matrix[j][i] / matrix[i][i];
					for (int k = 0; k < matrix[0].length; k++) {
						matrix[j][k] = matrix[j][k]
								- (multiplier * matrix[i][k]);
					}
				}
				this.results.add(copyMatrix(matrix));
			}
		}
	}

	/**
	 * Decide which the pivot of the matrix, given the index of element of the
	 * main diagonal.
	 * 
	 * @param matrix The increased matrix of the system to analyze.
	 * @param index The index matrix[i][i] to guide the pivoting technique.
	 */
	private void pivotMatrixUpDown(double[][] matrix, int index) {
		if (index < matrix.length) {
			double maxValue = Math.abs(matrix[index][index]);
			int selectedLine = -1;
			int i;
			for (i = index + 1; i < matrix.length; i++) {
				if (Math.abs(matrix[i][index]) > maxValue) {
					maxValue = Math.abs(matrix[i][index]);
					selectedLine = i;
				}
			}
			if (selectedLine != -1) {
				changeLines(index, selectedLine, matrix);
			}
		}
	}

	/**
	 * Decide which the pivot of the matrix, given the index of element of the
	 * main diagonal.
	 * 
	 * @param matrix The increased matrix of the system to analyze.
	 * @param index The index matrix[i][i] to guide the pivoting technique.
	 */
	private void pivotMatrixDownUp(double[][] matrix, int index) {
		if (index < matrix.length) {
			double maxValue = Math.abs(matrix[index][index]);
			int selectedLine = -1;
			int i;
			for (i = index - 1; i >= 0; i--) {
				if (Math.abs(matrix[i][index]) > maxValue) {
					maxValue = Math.abs(matrix[i][index]);
					selectedLine = i;
				}
			}
			if (selectedLine != -1) {
				changeLines(index, selectedLine, matrix);
			}
		}
	}

	/**
	 * Realize the back replacement in the matrix.
	 * 
	 * @param matrix The increased matrix of the equation system to be
	 * back-replaced.
	 * @param triangSup A boolean value indicating if this matrix will be considered 
	 * the superior half. 
	 * @return The results vector.
	 */
	private double[][] backReplacement(double[][] matrix, boolean triangSup){
		double[][] sol = new double[matrix.length][1];
		if (triangSup){
			sol[matrix.length-1][0] = matrix[matrix.length-1][matrix.length]/
								   matrix[matrix.length-1][matrix.length-1]; 
			for (int i = matrix.length-2; i >= 0 ; i--) {
				double sum = 0;
				for (int j = i+1; j <= matrix.length-1; j++) {
					sum += matrix[i][j] * sol[j][0];
				}
				sol[i][0] = (matrix[i][matrix.length] - sum)/ matrix[i][i] ;
			}
		}else{
			sol[0][0] = matrix[0][matrix.length]/matrix[0][0]; 
			for (int i = 1; i <= matrix.length-1 ; i++) {
				double sum = 0;
				for (int j = 0; j < i; j++) {
					sum += matrix[i][j] * sol[j][0];
				}
				sol[i][0] = (matrix[i][matrix.length] - sum)/ matrix[i][i] ;
			}
		}
		this.results.add(sol);
		return sol;
	}

	/**
	 * Try to manipulate the matrix to avoid zeros in the main diagonal.
	 * 
	 * @param matrix The increased matrix of the equation system.
	 * @throws MSNException Thrown if the matrix cannot be diagonalyzed.
	 */
	private void organizeMatrix(double[][] matrix) throws MSNException {
		int limit = matrix.length * matrix.length;
		int j = 0;
		while (!diagonalOK(matrix) && j < limit) {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][i] == 0)
					changeLines(i, findLineToChange(i, matrix), matrix);
			}
			j++;
		}
		if (j == limit)
			throw new MSNException("Impossivel Diagonalizar a matriz!!");
	}

	/**
	 * Swap two lines of the given matrix.
	 * 
	 * @param line1
	 *            Line to be swapped.
	 * @param line2
	 *            Line to be swapped.
	 * @param matrix
	 *            The matrix where will be happen the swap.
	 */
	private void changeLines(int line1, int line2, double[][] matrix) {
		double[] aux = new double[matrix.length + 1];
		for (int i = 0; i < matrix.length + 1; i++) {
			aux[i] = matrix[line2][i];
			matrix[line2][i] = matrix[line1][i];
		}
		for (int i = 0; i < matrix.length + 1; i++) {
			matrix[line1][i] = aux[i];
		}
		this.results.add(matrix);
	}

	/**
	 * Search for a line that can be swapped.
	 * 
	 * @param line
	 *            Line when there is a zero on the main diagonal.
	 * @param matrix
	 *            The main matrix of the equation system.
	 * @return The line found.
	 */
	private int findLineToChange(int line, double[][] matrix) {
		int numMaxZeros = -1;
		int lineToChange = -1;
		for (int i = 0; i < matrix.length; i++) {
			if (i != line) {
				if (matrix[i][line] != 0) {
					int zeros = countZerosLine(i, matrix);
					if (zeros > numMaxZeros) {
						numMaxZeros = zeros;
						lineToChange = i;
					}
				}
			}
		}
		return lineToChange;
	}

	/**
	 * Count the number of zeros in the main diagonal.
	 * 
	 * @param line
	 *            The line to be analyzed.
	 * @param matrix
	 *            The matrix in question.
	 * @return The number of zeros in the given line.
	 */
	private int countZerosLine(int line, double[][] matrix) {
		int zeros = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[line][i] == 0)
				zeros++;
		}
		return zeros;
	}

	/**
	 * Verify if in the main diagonal there isn't zeros.
	 * 
	 * @param matrix
	 *            The main matrix of the equation system.
	 * @return A boolean value indicating if the there is any zero in the main
	 *         diagonal of the given matrix.
	 */
	private boolean diagonalOK(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i][i] == 0)
				return false;
		}
		return true;
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
	private boolean fitResidue(double[][] matrix, double[][] vet,
			double residue) {
		double[] vetResults = new double[vet.length];
		for (int i = 0; i < matrix.length; i++) {
			double currentResult = 0;
			for (int j = 0; j < matrix.length; j++) {
				currentResult += matrix[i][j] * vet[j][0];
			}
			vetResults[i] = matrix[i][matrix.length] - currentResult;
		}
		boolean changed = false;
		for (int j = 0; j < vetResults.length; j++) {
			if (Math.abs(vetResults[j]) > residue) {
				vet[j][1] += vetResults[j];
				changed = true;
			}
		}
		if (changed) {
			this.results.add(this.copyMatrix(vet));
		}
		return !changed;
	}
}
