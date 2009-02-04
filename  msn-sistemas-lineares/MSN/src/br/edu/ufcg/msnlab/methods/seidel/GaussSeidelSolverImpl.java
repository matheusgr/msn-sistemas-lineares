package br.edu.ufcg.msnlab.methods.seidel;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.util.CheckerConditions;
import br.edu.ufcg.msnlab.util.Config;

/**
 * Classe que soluciona sistemas de equações através do método de GaussSeidel.
 * 
 * @author Anderson Pablo (andersonpablo@gmail.com)
 * @author José Wilson (wilsonufcg@gmail.com)
 */
public class GaussSeidelSolverImpl implements GaussSeidelSolver {

	/**
	 * Soluciona o sistema, representado pelas variáveis passadas, usando o
	 * método de Gauss-Seidel, com vetor de estimativas passado pelo usuário.
	 * 
	 * @param coeficientes
	 *            Matriz de coeficientes do sistema.
	 * @param estimativas
	 *            Matriz de estimativas determinadas pelo usuário (opcional).
	 * @param termos
	 *            Matriz de termos independentes do sistema linear.
	 * @param aprox
	 *            Aproximação desejada pela usuário.
	 * @param iteracoesMax
	 *            Número de iterações máximo que o método vai executar.
	 * @param config
	 *            Parâmetros de configuração para cada método.
	 * @return Result, uma lista de matrizes (de dimensões n x 1), cada matriz
	 *         representando uma iteração da solução.
	 * @throws MSNException
	 */
	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config)
			throws MSNException {
		return seidel(coeficientes, estimativas, termos, aprox, iteracoesMax);
	}

	/**
	 * Soluciona o sistema, representado pelas variáveis passadas, usando o
	 * método de Gauss-Seidel, com um vetor de estimativas default.
	 * 
	 * @param coeficientes
	 *            Matriz de coeficientes do sistema.
	 * @param termos
	 *            Matriz de termos independentes do sistema linear.
	 * @param aprox
	 *            Aproximação desejada pela usuário.
	 * @param iteracoesMax
	 *            Número de iterações máximo que o método vai executar.
	 * @param config
	 *            Parâmetros de configuração para cada método.
	 * @return Result, uma lista de matrizes, cada matriz representando uma
	 *         iteracao.
	 * @throws MSNException
	 */
	@Override
	public Result solve(double[][] coeficientes, double[] termos, double aprox,
			int iteracoesMax, Config config) throws MSNException {
		// Caso seja chamado o metodo sem estimativas iniciais, elas serao
		// geradas com valores nulos
		return seidel(coeficientes, criaMatrixEestInic(coeficientes.length),
				termos, aprox, iteracoesMax);
	}

	/**
	 * Método privado que efetivamente soluciona o sistema linear usando o
	 * método de Gauss-Seidel.
	 * 
	 * @throws MSNException
	 */
	private Result seidel(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax)
			throws MSNException {

		int numIteracoes = 1;
		Result results = new ResultMSN();
		CheckerConditions checker = new CheckerConditions();

		double[][] matrixTMP = increasedMatrix(coeficientes, termos);
		matrixTMP = checker.organizeMatrix(matrixTMP);

		if (!checker.checkConditionOfConvergence(matrixTMP)) {
			matrixTMP = checker.searchMatrixConvergence(matrixTMP);
		}
		if (!checker.diagonalOK(matrixTMP))
			throw new MSNException("There is no convergence!!");

		coeficientes = getCoefficients(matrixTMP);
		termos = getTerms(matrixTMP);

		double[] matrixX = estimativas; // Matriz que contem a melhor
										// aproximacao do resultado até o
										// momento
		double[] matrixC = getMatrixC(coeficientes, termos); // Matriz dos
																// termos
																// independentes
																// divididos
																// pela diagonal
		double[][] matrixS = getMatrixS(coeficientes); // Matriz contendo todos
														// os termos dividos
														// pela diagonal, e com
														// a diagonal nula
		double[] matrixXanterior = matrixX.clone(); // Matriz de resultados da
													// iteração anterior
													// adicionaResult(results,
													// matrixX);
		do {
			matrixXanterior = matrixX.clone();
			matrixX = calculaNovaEstimativa(matrixX, matrixC, matrixS);
			adicionaResult(results, matrixX);
			numIteracoes++;
		} while (numIteracoes <= iteracoesMax
				&& (verificaCondicaoParada(matrixX, aprox, matrixXanterior)));

		return results;
	}

	/**
	 * Constrói uma matriz com a matriz de coeficientes e a matriz de termos
	 * independentes.
	 * 
	 * @param coeficientes
	 *            A matriz de coeficientes.
	 * @param termos
	 *            A matriz de termos independentes.
	 * @return A matriz resultante.
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
	 * Recupera a matriz de coeficientes a partir da matriz de coeficientes mais
	 * termos independentes.
	 * 
	 * @param matrix
	 *            A matriz de coeficientes mais termos independentes.
	 * @return coefficients A matriz de coeficientes.
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
	 * Recupera a matriz de termos independentes a partir da matriz de
	 * coeficientes mais termos independentes.
	 * 
	 * @param matrix
	 *            A matriz de coeficientes mais termos independentes.
	 * @return terms Os termos independentes.
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
	 * Cria o vetor de estimativas inicial com zeros.
	 */
	private double[] criaMatrixEestInic(int tamanho) {
		double[] matrixInic = new double[tamanho];
		for (int i = 0; i < tamanho; i++) {
			matrixInic[i] = 0.0;
		}
		return matrixInic;
	}

	/**
	 * Adiciona a matriz n x 1 de uma iteração no Result.
	 */
	private void adicionaResult(Result results, double[] matrixX) {
		int tamanho = matrixX.length;
		double[][] result = new double[tamanho][1];
		for (int i = 0; i < tamanho; i++) {
			result[i][0] = matrixX[i];
		}
		results.addResult(result);

	}

	/**
	 * Calcula os valores de estimativa da iteração atual.
	 */
	private double[] calculaNovaEstimativa(double[] matrixX, double[] matrixC,
			double[][] matrixS) {
		int lin = matrixS.length;
		int col = lin;
		double[] matrixXnova = matrixX.clone();

		for (int i = 0; i < lin; i++) {
			double temp = 0.0;
			for (int j = 0; j < col; j++) {
				temp = temp + ((matrixS[i][j] * matrixXnova[j]));
			}
			temp = temp + +matrixC[i];
			matrixXnova[i] = temp;
		}

		return matrixXnova;
	}

	/**
	 * Cria e retorna matriz dos termos independentes divididos pela diagonal.
	 */
	private double[] getMatrixC(double[][] coeficientes, double[] termos) {
		double[] matrix = new double[termos.length];
		for (int i = 0; i < termos.length; i++) {
			matrix[i] = termos[i] / coeficientes[i][i];
		}

		return matrix;
	}

	/**
	 * Cria e retorna a matriz contendo todos os termos dividos pela diagonal, e
	 * com a diagonal nula.
	 */
	public double[][] getMatrixS(double[][] coeficientes) {
		int lin = coeficientes.length;
		int col = lin;

		double[][] matrixS = new double[lin][col];
		for (int i = 0; i < lin; i++) {
			for (int j = 0; j < col; j++) {
				double temp = (-1) * coeficientes[i][j] / coeficientes[i][i];
				if (i != j)
					matrixS[i][j] = temp;
			}
			matrixS[i][i] = 0.0;
		}
		return matrixS;
	}

	/**
	 * Verifica condição de paradas para as iterações.
	 */
	private boolean verificaCondicaoParada(double[] matrixX, double tolerancia,
			double[] matrizXanterior) {
		double distanciaEntreDuasIteracoes = getMaiorDistanciaEntreDuasIteracoes(
				matrixX, matrizXanterior);
		double distanciaRelativa = getDistanciaRelativa(matrixX,
				distanciaEntreDuasIteracoes);
		if (distanciaRelativa > tolerancia) {
			return true;
		}
		return false;
	}

	/**
	 * Retorna a distância relativa entre duas iterações.
	 */
	private double getDistanciaRelativa(double[] matrixX,
			double distanciaEntreDuasIteracoes) {

		double maiorElemento = 0;
		double maiorValor = 0;
		double distanciaRelativa = 0;
		int tamanho = matrixX.length;

		for (int i = 0; i < tamanho; i++) {
			maiorValor = Math.abs(matrixX[i]);
			if (maiorValor > maiorElemento) {
				maiorElemento = maiorValor;
			}

		}
		distanciaRelativa = distanciaEntreDuasIteracoes / maiorElemento;
		return distanciaRelativa;
	}

	/**
	 * Retorna a maior distância entre duas iterações.
	 */
	private double getMaiorDistanciaEntreDuasIteracoes(double[] matrixX,
			double[] matrizXanterior) {
		double maiorDistancia = 0.0;
		double distancia = 0.0;
		int tamanho = matrixX.length;
		for (int i = 0; i < tamanho; i++) {
			distancia = Math.abs(matrixX[i] - matrizXanterior[i]);
			if (distancia > maiorDistancia) {
				maiorDistancia = distancia;
			}
		}
		return maiorDistancia;
	}

}
