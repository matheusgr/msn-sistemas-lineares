package br.edu.ufcg.msnlab.methods.seidel;


import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.util.CheckerConditions;
import br.edu.ufcg.msnlab.util.Config;

/**
 * Class that solves systems of linear equations using the method of Gauss-Seidel.
 * 
 * @author Anderson Pablo (andersonpablo@gmail.com)
 * @author José Wilson (wilsonufcg@gmail.com)
 */
public class GaussSeidelSolverImpl implements GaussSeidelSolver {

	/**
	 * Resolve the system represented by the values passed.
	 * @param coefficients The matrix that contains the coefficients of the system.
	 * @param estimates the estimates 
	 * @throws MSNException 
	 */
	@Override
	public Result solve(double[][] coeficients, double[] estimates,
			double[] terms, double approximation, int maximumNumberIterations, Config config)
			throws MSNException {
		return seidel(coeficients, estimates, terms, approximation, maximumNumberIterations);
	}

	/**
	 * Resolve the system represented by the values passed, with the default vector estimates
	 * @param coefficients The matrix that contains the coefficients of the system.
	 * @param estimates the estimates 
	 * @throws MSNException 
	 */
	@Override
	public Result solve(double[][] coeficients, double[] terms, double approximation,
			int maximumNumberIterations, Config config) throws MSNException {
		
		return seidel(coeficients, createInitMatrix(coeficients.length),
				terms, approximation, maximumNumberIterations);
	}

	/**
	 * Implementation of the Seidel's method. 
	 * @param coefficients The matrix that contains the coefficients of the system.
	 * @param estimates
	 * @param terms The independent terms of the system.
	 * @param aproximation The residue of the error in the system.
	 * @param maximumNumberIterations number maximum of iterations.
	 * @return The solution of the system by the method of Seidel.
	 * @throws MSNException 
	 */
	private Result seidel(double[][] coefficients, double[] estimates,
			double[] terms, double aproximation, int maximumNumberIterations)
			throws MSNException {

		int numIteracoes = 1;
		Result results = new ResultMSN();
		CheckerConditions checker = new CheckerConditions();

		double[][] matrixTMP = increasedMatrix(coefficients, terms);
		matrixTMP = checker.organizeMatrix(matrixTMP);

		if (!checker.checkConditionConvergence(matrixTMP)) {
			matrixTMP = checker.searchMatrixConvergence(matrixTMP);
		}
		if (!checker.diagonalOK(matrixTMP))
			throw new MSNException("There is no convergence!!");

		coefficients = getCoefficients(matrixTMP);
		terms = getTerms(matrixTMP);

		double[] matrixX = estimates;
		
		double[] matrixC = getMatrixC(coefficients, terms); 
		
		double[][] matrixS = getMatrixS(coefficients); 
		
		double[] matrixXanterior = matrixX.clone(); 
		
		do {
			matrixXanterior = matrixX.clone();
			matrixX = calculateNewEstimate(matrixX, matrixC, matrixS);
			addResult(results, matrixX);
			numIteracoes++;
		} while (numIteracoes <= maximumNumberIterations
				&& (verifiesConditionParade(matrixX, aproximation, matrixXanterior)));

		return results;
	}

	/**
	 * Construct the increased matrix with the coefficients matrix and the
	 * independent terms matrix.
	 * @param coeficients The coefficients matrix.
	 * @param terms The independent terms matrix.
	 * @return The increased matrix.
	 */
	private double[][] increasedMatrix(double[][] coeficientes, double[] termos) {
		double[][] mat = new double[coeficientes.length][coeficientes.length + 1];
		for (int i = 0; i < coeficientes.length; i++) {
			for (int j = 0; j < coeficientes.length; j++) {
				mat[i][j] = coeficientes[i][j];
			}
		}
		for (int i = 0; i < termos.length; i++) {
			mat[i][coeficientes.length] = termos[i];
		}

		return mat;
	}

	/**
	 * Construct the matrix of coefficients
	 * 
	 * @param matrix
	 *            the matrix to extract the coefficients.
	 * @return coefficients the matrix of coefficients.
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
	 * Construct the matrix of independent terms
	 * 
	 * @param matrix
	 *            the matrix to extract the terms.
	 * @return the independent terms.
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
	 * Return the initial matrix of estimates
	 */
	private double[] createInitMatrix(int lenth) {
		double[] matrixInic = new double[lenth];
		for (int i = 0; i < lenth; i++) {
			matrixInic[i] = 0.0;
		}
		return matrixInic;
	}

	/**
	 * Add the matrix n x 1 in the Result set.
	 */
	private void addResult(Result results, double[] matrixX) {
		int length = matrixX.length;
		double[][] result = new double[length][1];
		for (int i = 0; i < length; i++) {
			result[i][0] = matrixX[i];
		}
		results.addResult(result);

	}

	/**
	 * Calculate the values of estimate in the iteration.
	 */
	private double[] calculateNewEstimate(double[] matrixX, double[] matrixC,
			double[][] matrixS) {
		int lin = matrixS.length;
		int col = lin;
		double[] matrixXnew = matrixX.clone();

		for (int i = 0; i < lin; i++) {
			double temp = 0.0;
			for (int j = 0; j < col; j++) {
				temp = temp + ((matrixS[i][j] * matrixXnew[j]));
			}
			temp = temp + +matrixC[i];
			matrixXnew[i] = temp;
		}

		return matrixXnew;
	}

	/**
	 * get the matrix of independents terms divided by the principal diagonal.
	 */
	private double[] getMatrixC(double[][] coeficients, double[] terms) {
		double[] matrix = new double[terms.length];
		for (int i = 0; i < terms.length; i++) {
			matrix[i] = terms[i] / coeficients[i][i];
		}

		return matrix;
	}

	/**
	 *get the matrix of therms divided by the principal diagonal.
	 */
	public double[][] getMatrixS(double[][] coeficients) {
		int lin = coeficients.length;
		int col = lin;

		double[][] matrixS = new double[lin][col];
		for (int i = 0; i < lin; i++) {
			for (int j = 0; j < col; j++) {
				double temp = (-1) * coeficients[i][j] / coeficients[i][i];
				if (i != j)
					matrixS[i][j] = temp;
			}
			matrixS[i][i] = 0.0;
		}
		return matrixS;
	}
	
	/**
	 * Notes that the result of the system has been achieved condition stoppage of the method.
	 * @param matrixX 
	 * @param tolerance
	 * @param matrixPreviousIteration
	 * @return true if condition stop. 
	 * @throws MSNException 
	 */
	private boolean verifiesConditionParade(double[] matrixX, double tolerance, double[] matrixPreviousIteration) throws MSNException {
		double distanceBetweenTwoIterations = getIncreasedDistanceBetweenTwoIterations(matrixX, matrixPreviousIteration);
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
	 * @throws MSNException 
	 */
	private double getDistantRelative(double[] matrixX,
			double distanceBetweenTwoIterations) {

		double maxElement = 0;
		double maxValue = 0;
		double distantRelative = 0;
		int length = matrixX.length;

		for (int i = 0; i < length; i++) {
			maxValue = Math.abs(matrixX[i]);
			if (maxValue > maxElement) {
				maxElement = maxValue;
			}

		}
		distantRelative = distanceBetweenTwoIterations / maxElement;
		return distantRelative;
	}

	/**
	 * Calculate the increased distance between two iterations.
	 * @param matrixX 
	 * @param matrixPreviousIteration
	 * @return greaterDistance The best distance between two iterations.
	 */
	private double getIncreasedDistanceBetweenTwoIterations(double[] matrixX,
			double[] matrixPreviousIteration) {
		double maxDistant = 0.0;
		double distant = 0.0;
		int length = matrixX.length;
		for (int i = 0; i < length; i++) {
			distant = Math.abs(matrixX[i] - matrixPreviousIteration[i]);
			if (distant > maxDistant) {
				maxDistant = distant;
			}
		}
		return maxDistant;
	}

}
