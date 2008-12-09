package br.edu.ufcg.msnlab.methods.jacobi;

import java.util.LinkedList;
import java.util.List;

import Jama.Matrix;
import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.util.Config;

public class JacobiSolverImpl implements JacobiSolver {
	
	

	/**
	 * Resolve the system represented by the values passed.
	 * @param coefficients The matrix that contains the coefficients of the system.
	 * @param estimates the estimates 
	 * @throws MSNException 
	 */
	public Result solve(double[][] coefficients, double[] estimates,
			double[] terms, double approximation, int maximumNumberIterations, Config config) throws MSNException {
		double[][] temp = increasedMatrix(coefficients,terms);
		temp = organizeMatrix(temp);
		coefficients = getCoefficients(temp);
		terms = getTerms(temp);
		List<Matrix> jacobi = jacobiMethod(coefficients, estimates, terms, approximation, maximumNumberIterations);
		return turns(jacobi); 
		
	}
	
	/**
	 * Resolve the system represented by the values passed.
	 * @param coefficients The matrix that contains the coefficients of the system.
	 * @param terms The independent terms of the system.
	 * @param aproximation The residue of the error in the system.
	 * @param maximumNumberIterations The maximum number of iterations.
	 * @param config The configuration for this method.
	 * @throws MSNException 
	 */
	public Result solve(double[][] coefficients, double[] terms, double aproximation,
			int maximumNumberIterations, Config config) throws MSNException {
		
		coefficients = organizeMatrix(coefficients);
		
		double[] estimates = initializeEstimates(coefficients.length-1);
		
		return solve(coefficients, estimates, terms, aproximation, maximumNumberIterations, config);
		
	}
	
	/**
	 * Calculate the coefficients of the matrix.
	 * @param matrix Matrix with coefficients and the terms.
	 * @return coefficients The coefficients.
	 */
	private double[][] getCoefficients(double[][] matrix) {
		int num_lines = matrix.length;
		double[][] coefficients = new double[num_lines][num_lines];
		for (int i = 0; i < coefficients.length; i++) {
			for (int j = 0; j < coefficients.length; j++) {
				coefficients[i][j] = matrix[i][j];
			}
		}
		return coefficients;
	}
	
	/**
	 * Calculate the terms of the matrix.
	 * @param matrix Matrix with coefficients and the terms. 
	 * @return terms The terms.
	 */
	private double[] getTerms(double[][] matrix) {
		int num_lines = matrix.length;
		double[] terms = new double[num_lines];
		for (int i = 0; i < terms.length; i++) {
			terms[i] = matrix[i][num_lines];
		}
		return terms;
	}
   
	/**
	 * Initialize the estimates.
	 * @param quantity Quantity the estimates. 
	 * @return The estimates initialized with zeros.
	 */
	private double[] initializeEstimates(int quantity) {
		double[] estimates = new double[quantity];
		for (int i = 0; i < estimates.length; i++) {
			estimates[i] = 0;
		}
		return estimates;
	}
	
	/**
	 * Implementation of the Jacobi's method. 
	 * @param coefficients The matrix that contains the coefficients of the system.
	 * @param estimates
	 * @param terms The independent terms of the system.
	 * @param aproximation The residue of the error in the system.
	 * @param maximumNumberIterations number maximum of iterations.
	 * @return The solution of the system by the method of Jacobi.
	 */
	private List<Matrix> jacobiMethod(double[][] coefficients, 
			double[] estimates, double[] terms, double aproximation,
			int maximumNumberIterations) {
		
		Matrix matrixX = initializeEstimates(estimates);
		Matrix matrixC = getMatrixCoefficients(coefficients, terms);
		Matrix matrixJ = getMatrixJ(coefficients);
		Matrix matrix_X_previousIteration = matrixX;
		int quantityIterations = 1;
		List<Matrix> result = new LinkedList<Matrix>();
		result.add(matrixX);
	
		do{
			Matrix matrixJC = matrixJ.times(matrixX);
			matrix_X_previousIteration = matrixX;
			matrixX = matrixJC.plus(matrixC);	
			result.add(matrixX);
			quantityIterations++;
			
		}while(verifiesConditionParade(matrixX, aproximation, matrix_X_previousIteration) 
				&& quantityIterations <= maximumNumberIterations);
		
		return result;
	}
	
	/**
	 * Construct the increased matrix with the coefficients matrix and the
	 * independent terms matrix.
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
		
		return mat;
	}
	
	/**
	 * Try to manipulate the matrix to avoid zeros in the main diagonal.
	 * @param matrix The increased matrix of the equation system.
	 * @throws MSNException Thrown if the matrix cannot be diagonalyzed.
	 */
	private double[][] organizeMatrix(double[][] matrix) throws MSNException {
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
		return matrix;
	}
	
	/**
	 * Swap two lines of the given matrix.
	 * @param line1 Line to be swapped.
	 * @param line2 Line to be swapped.
	 * @param matrix The matrix where will be happen the swap.
	 */
	private double[][] changeLines(int line1, int line2, double[][] matrix) {
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
	 * @param matrix The main matrix of the equation system.
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
	 * Calculate the result of each iteration
	 * @param listMatrix Collection of matrix
	 * @return results The result by each iteration
	 */
	@SuppressWarnings("unchecked")
	private ResultMSN turns(List<Matrix> listMatrix) {
		ResultMSN results = new ResultMSN();
		
		for (Matrix matrix : listMatrix) {
			results.addResult(matrix.getArrayCopy());
		}
		
		return  results;
	}
	
	/**
	 * Calculate the coefficients of the matrix.
	 * @param coefficients
	 * @param terms Independent terms
	 * @return matrixCte Matrix of the coefficients.
	 */
	private Matrix getMatrixCoefficients(double[][] coefficients,double[] terms) {
		Matrix matrixConstants = new Matrix(terms.length,1);
		
		for (int i = 0; i < terms.length; i++) {
			matrixConstants.set(i, 0, terms[i]/coefficients[i][i]);
		}
		return matrixConstants;
	}
	
	/**
	 * Notes that the result of the system has been achieved condition stoppage 
	 * of the method of Jacobi.
	 * @param matrixX 
	 * @param tolerance
	 * @param matrix_X_previousIteration
	 * @return true if condition stop. 
	 */
	private boolean verifiesConditionParade(Matrix matrixX, double tolerance, Matrix matrix_X_previousIteration) {
		double distanceBetweenTwoIterations = getIncreasedDistanceBetweenTwoIterations(matrixX, matrix_X_previousIteration);
		double distantRelative = getDistantRelative(matrixX,distanceBetweenTwoIterations);
		if (distantRelative > tolerance){
			return true;
		}
		return false;
	}
	
	/**
	 * Calculate the distance on
	 * @param matrixX
	 * @param distanceBetweenTwoIterations
	 * @return the distance between two iterations.
	 */
	private double getDistantRelative(Matrix matrixX, double distanceBetweenTwoIterations) {
		double largestElement = 0;
		double higherValue = 0;
		double distanteRelative = 0;
		for(int i = 0; i < matrixX.getRowDimension(); i++){
			for (int j = 0; j < matrixX.getColumnDimension(); j++) {
				higherValue = Math.abs(matrixX.get(i, j));
				if(higherValue > largestElement){
					largestElement = higherValue;
				}
			}
		}
		distanteRelative = distanceBetweenTwoIterations/largestElement;
		return distanteRelative;
	}
	
	/**
	 * Calculate the increased distance between two iterations.
	 * @param matrixX 
	 * @param matrix_X_previousIteration
	 * @return greaterDistance The best distance between two iterations.
	 */
	private double getIncreasedDistanceBetweenTwoIterations(Matrix matrixX, Matrix matrix_X_previousIteration) {
		double greaterDistance = 0;
		double distance = 0;
		for(int i = 0; i < matrixX.getRowDimension(); i++){
			for (int j = 0; j < matrixX.getColumnDimension(); j++) {
				 distance = Math.abs(matrixX.get(i,j) - matrix_X_previousIteration.get(i, j));
				if(distance > greaterDistance){					
					greaterDistance = distance;
				}
			}
		}
		return greaterDistance;
	}
	
	/**
	 * Calculate the Matrix J of method of Jacobi.
	 * @param coefficients the matrix of the coefficients. 
	 * @return Matrix J.
	 */
	public Matrix getMatrixJ(double[][] coefficients) {
         int line = coefficients.length;
         int column = line;
         
         Matrix matrixJ = new Matrix(coefficients);
         for (int i = 0; i < line; i++) {
        	 for (int j = 0; j < column; j++) {
            	  double temp;
                  if (j == column) 
                	  temp = coefficients[i][j]/coefficients[i][i];
                  else 
                	  temp = (-1)*coefficients[i][j]/coefficients[i][i];
                  if (i != j) 
                	  matrixJ.set(i,j,temp);
              }
              matrixJ.set(i,i,0.0);
         }
         return matrixJ;
	 }                        
	
	/**
	 * Initialize the estimates.
	 * @param estimates the estimates
	 * @return the matrix of the estimates inicialized.
	 */
	public Matrix initializeEstimates(double[] estimates) {
		Matrix matrixEstimators = new Matrix(estimates.length,1);
		for (int i = 0; i < estimates.length; i++) {
			matrixEstimators.set(i,0, estimates[i]);
		}
		return matrixEstimators;
	}
	
	private static void print(Matrix matrix) {
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) 
				System.out.print("["+ matrix.get(i, j));
			System.out.println("]");
		}
	}

}
