package br.edu.ufcg.msnlab.util;

import br.edu.ufcg.msnlab.exceptions.MSNException;

public class CheckerConditions {


	
	public CheckerConditions(){
		
	}
	
	
	/**
	 * Check the conditions for convergence 
	 * of the method
	 * @param matrix
	 * @return true if converge and false if not 
	 */
	public  boolean checkConditionOfConvergence(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			double sumLine = calculateSumLine(matrix[i]);
			double factor = Math.abs(sumLine-matrix[i][i])/Math.abs(matrix[i][i]);
			
			if (factor >= 1)
				return false;
		}
		return true;
	}
	
	/**
	 * Sum the line of a matrix
	 * @param line
	 * @return sum
	 */
	public double calculateSumLine(double[] line) {
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
	public  double[][] searchMatrixConvergence(double[][] matrix) throws MSNException {
		boolean isPossible = false;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i != j) {
					matrix = changeLines(i, j, matrix);
					isPossible = checkConditionOfConvergence(matrix); 
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
