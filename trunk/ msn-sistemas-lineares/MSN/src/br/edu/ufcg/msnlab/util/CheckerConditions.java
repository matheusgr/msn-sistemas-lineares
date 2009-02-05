package br.edu.ufcg.msnlab.util;

import br.edu.ufcg.msnlab.exceptions.MSNException;

public class CheckerConditions {


	
	public CheckerConditions(){
		
	}
	
	
	public boolean checkConditionConvergence(double[][] matrix) {
		return checkConditionOfLines(matrix) || checkCriterionSassenfeld(matrix);
	}
	
	/**
	 * Check condition of convergence of the lines
	 * of the method of Gauss-Jacobi
	 * @param matrix
	 * @return true if converge and false if not 
	 */
	private boolean checkConditionOfLines(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			double sumLine = calculateSumLine(matrix[i]);
			double factor = Math.abs(sumLine-Math.abs(matrix[i][i]));
			if (Math.abs(matrix[i][i]) <= factor)
				return false;
		}
		return true;
	}
	
	/**
	 * Check the condition of Sassenfeld
	 * @param matrix - Matrix to be verified by the method of Sassenfeld
	 * @return true if the criterion of Sassenfeld go true, false otherwise.
	 */
	private boolean checkCriterionSassenfeld(double[][] matrix) {
		double[] beta = getValuesBeta(matrix);
		for (int i = 0; i < beta.length; i++) {
			if (beta[i] >= 1)
				return false;
		}
		return true;
	}
	
	/**
	 * Calculate the values of the betas needful to the method of Sassenfeld.
	 * @param matrix - Matrix to be calculated the values of beta.
	 * @return - The values of beta.
	 */
	private double[] getValuesBeta(double[][] matrix) {
		double[] beta = new double[matrix.length];
		beta[0] = (1/Math.abs(matrix[0][0]))*Math.abs(calculateSumLine(matrix[0])-Math.abs(matrix[0][0]));
		for (int i = 1; i < matrix.length; i++) {
			for (int j = 0; j < beta.length; j++) {
				if (i != j && beta[j] != 0.) 
					beta[i] += Math.abs(beta[j])*Math.abs(matrix[i][j]);
				else if (i != j) beta[i] += Math.abs(matrix[i][j]); 
			}
			beta[i] = beta[i]/(Math.abs(matrix[i][i]));
		}
		return beta;
	}

	
	/**
	 * Sum the line of a matrix
	 * @param line
	 * @return sum
	 */
	public  double calculateSumLine(double[] line) {
		double sum = 0.0;
		for (int i = 0; i < line.length-1; i++) {
			sum += line[i];
		}
		return sum;
	}
	
	/**
	 * Change the matrix to find a new matrix that can converge
	 * @param matrix
	 * @return convergent matrix
	 * @throws MSNException
	 */
	public double[][] searchMatrixConvergence(double[][] matrix) throws MSNException {
		boolean isPossible = false;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i != j) {
					matrix = changeLines(i, j, matrix); 
					isPossible = checkConditionConvergence(matrix);
					if (isPossible) {
						return matrix;
					}
				}
			}
		}
		if (!isPossible) throw new MSNException("The system can not converge!");
		return matrix;
	}
	
	/**
	 * Swap two lines of the given matrix.
	 * @param line1 Line to be swapped.
	 * @param line2 Line to be swapped.
	 * @param matrix The matrix where will be happen the swap.
	 */
	private  double[][] changeLines(int line1, int line2, double[][] matrix) {
		double[] aux = new double[matrix.length + 1];
		for (int i = 0; i < matrix.length + 1; i++) {
			aux[i] = matrix[line2][i];
			matrix[line2][i] = matrix[line1][i];
		}
		for (int i = 0; i < matrix.length + 1; i++) {
			matrix[line1][i] = aux[i];
		}
		return matrix;
	}

	
	/**
	 * Try to manipulate the matrix to avoid zeros in the main diagonal.
	 * @param matrix The increased matrix of the equation system.
	 * @throws MSNException Thrown if the matrix cannot be diagonalyzed.
	 */
	public  double[][] organizeMatrix(double[][] matrix) throws MSNException {
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
			throw new MSNException("Impossível diagonalizar a matriz!!");
		return matrix;
	}
	
	/**
	 * Verify if in the main diagonal there isn't zeros.
	 * @param matrix The main matrix of the equation system.
	 * @return A boolean value indicating if the there is any zero in the main
	 *         diagonal of the given matrix.
	 */
	public  boolean diagonalOK(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i][i] == 0)
				return false;
		}
		return true;
	}
	
	
	/**
	 * Search for a line that can be swapped.
	 * @param line Line when there is a zero on the main diagonal.
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
	 * @param line The line to be analyzed.
	 * @param matrix The matrix in question.
	 * @return The number of zeros in the given line.
	 */
	private  int countZerosLine(int line, double[][] matrix) {
		int zeros = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[line][i] == 0)
				zeros++;
		}
		return zeros;
	}
	
}
