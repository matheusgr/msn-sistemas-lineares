package br.edu.ufcg.msnlab.methods.seidel.precond;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.methods.choleskyqr.logic_methods.Matrix;
import br.edu.ufcg.msnlab.util.CheckerConditions;
import br.edu.ufcg.msnlab.util.Config;

/**
 * Class that solves systems of linear equations using the method of
 * Gauss-Seidel with Preconditioning for H-Matrices.
 * 
 * @author Renato Miceli (renatomiceli@gmail.com)
 * @author Vitor Amaral (vitorsamaral@gmail.com)
 */
public class GaussSeidelWithPreconditioningForHMatricesSolverImpl implements
		Solver {

	/**
	 * Resolve the system represented by the values passed.
	 * 
	 * @param coefficients
	 *            The matrix that contains the coefficients of the system.
	 * @param estimates
	 *            the estimates
	 * @throws MSNException
	 */
	@Override
	public Result solve(double[][] coeficients, double[] estimates,
			double[] terms, double approximation, int maximumNumberIterations,
			Config config) throws MSNException {
		return seidel(coeficients, estimates, terms, approximation,
				maximumNumberIterations, (Boolean) config.get(Config.dryrun));
	}

	/**
	 * Resolve the system represented by the values passed, with the default
	 * vector estimates
	 * 
	 * @param coefficients
	 *            The matrix that contains the coefficients of the system.
	 * @param estimates
	 *            the estimates
	 * @throws MSNException
	 */
	@Override
	public Result solve(double[][] coeficients, double[] terms,
			double approximation, int maximumNumberIterations, Config config)
			throws MSNException {

		return seidel(coeficients, createInitMatrix(coeficients.length), terms,
				approximation, maximumNumberIterations,
				(Boolean) config.get(Config.dryrun));
	}

	/**
	 * Implementation of the Seidel's method.
	 * 
	 * @param coefficients
	 *            The matrix that contains the coefficients of the system.
	 * @param estimates
	 * @param terms
	 *            The independent terms of the system.
	 * @param aproximation
	 *            The residue of the error in the system.
	 * @param maximumNumberIterations
	 *            number maximum of iterations.
	 * @param dryrun
	 *            Parameter to verify if an exception needed to be thrown.
	 * @return The solution of the system by the method of Seidel.
	 * @throws MSNException
	 */
	private Result seidel(double[][] coefficients, double[] estimates,
			double[] terms, double aproximation, int maximumNumberIterations,
			Boolean dryrun) throws MSNException {

		int numIteracoes = 1;
		Result results = new ResultMSN();
		CheckerConditions checker = new CheckerConditions();

		double[][] matrixTMP = increasedMatrix(coefficients, terms);

		try {
			matrixTMP = checker.organizeMatrix(matrixTMP);

			if (!checker.checkConditionConvergence(matrixTMP)) {
				matrixTMP = checker.searchMatrixConvergence(matrixTMP);
			}
			if (!checker.diagonalOK(matrixTMP)) {
				throw new MSNException("There is no convergence!!");
			}

			if (!checker.isHadamardMatrix(coefficients)) {
				throw new MSNException(
						"The coefficient matrix is not an H-Matrix!");
			}
		} catch (MSNException e) {
			if (dryrun) {
				throw e;
			}
		}
		coefficients = getCoefficients(matrixTMP);
		terms = getTerms(matrixTMP);

		// PRECOND INIT

		Matrix preconditioner = createPreconditioner(coefficients);
		coefficients = preconditioner.times(new Matrix(coefficients))
				.getArrayCopy();
		terms = preconditioner.times(new Matrix(terms, terms.length))
				.getColumnPackedCopy();

		// PRECOND END

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
				&& (verifiesConditionParade(matrixX, aproximation,
						matrixXanterior)));

		return results;
	}

	private Matrix createPreconditioner(double[][] coefficients) {

		double[][] S = new double[coefficients.length][coefficients[0].length];
		double[][] K = new double[coefficients.length][coefficients[0].length];
		Matrix P = Matrix.identity(coefficients.length, coefficients[0].length);

		for (int i = 0; i < coefficients.length - 1; i++) {
			double alpha = calcAlpha(coefficients, i);
			S[i][i + 1] = -alpha * coefficients[i][i + 1];
			double beta = calcBeta(coefficients, i);
			K[i + 1][i] = -beta * coefficients[i + 1][i];
		}

		return P.plus(new Matrix(S)).plus(new Matrix(K));
	}

	private double calcBeta(double[][] coefficients, int i) {
		double u = 1.0;
		double beta = u;
		for (int k = 0; k < i - 1; k++) {
			beta -= Math.abs(coefficients[i][k]) * u;
		}
		for (int k = i + 1; k < coefficients.length; k++) {
			beta -= Math.abs(coefficients[i][k]) * u;
		}
		beta += ((i - 1 < 0) ? 0 : Math.abs(coefficients[i][i - 1]) * u);
		double div = 0;
		for (int k = 0; k < coefficients.length && i - 1 >= 0; k++) {
			div += Math.abs(coefficients[i - 1][k]) * u;
		}
		beta /= (div == 0 ? 1 : 0);
		beta /= ((i - 1 < 0) ? 1 : Math.abs(coefficients[i][i - 1]) * u);

		return beta - 0.1;

	}

	private double calcAlpha(double[][] coefficients, int i) {
		double u = 1.0;
		double alpha = u;
		for (int k = 0; k < i; k++) {
			alpha -= Math.abs(coefficients[i][k]) * u;
		}
		for (int k = i + 2; k < coefficients.length; k++) {
			alpha -= Math.abs(coefficients[i][k]) * u;
		}
		alpha += ((i + 1 >= coefficients.length) ? 0 : Math
				.abs(coefficients[i][i + 1]) * u);
		double div = 0;
		for (int k = 0; k < coefficients.length && i + 1 < coefficients.length; k++) {
			div += Math.abs(coefficients[i + 1][k]) * u;
		}
		alpha /= (div == 0 ? 1 : div);
		alpha /= ((i + 1 >= coefficients.length) ? 1 : Math
				.abs(coefficients[i][i + 1]) * u);

		return alpha - 0.1;
	}

	/**
	 * Construct the increased matrix with the coefficients matrix and the
	 * independent terms matrix.
	 * 
	 * @param coeficients
	 *            The coefficients matrix.
	 * @param terms
	 *            The independent terms matrix.
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
	 * get the matrix of terms divided by the principal diagonal.
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
	 * Notes that the result of the system has been achieved condition stoppage
	 * of the method.
	 * 
	 * @param matrixX
	 * @param tolerance
	 * @param matrixPreviousIteration
	 * @return true if condition stop.
	 * @throws MSNException
	 */
	private boolean verifiesConditionParade(double[] matrixX, double tolerance,
			double[] matrixPreviousIteration) throws MSNException {
		double distanceBetweenTwoIterations = getIncreasedDistanceBetweenTwoIterations(
				matrixX, matrixPreviousIteration);
		double distantRelative = getDistantRelative(matrixX,
				distanceBetweenTwoIterations);
		if (distantRelative > tolerance) {
			return true;
		}
		return false;
	}

	/**
	 * Calculate the distance on
	 * 
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
	 * 
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
