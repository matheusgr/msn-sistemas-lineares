package br.edu.ufcg.msnlab.methods.gaussjordan;

import java.util.LinkedList;
import java.util.List;

import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.util.Config;

/**
 * This class contaisn all the methods used for solving an linear system
 * using the Gauss Jordan method
 * Essa classe contém métodos que resolvem um sistema de equações linear por meio do método Gauss-Jordan.
 * @author Rafael Dantas, Alan de Farias
 * 
 */

public class GaussJordan implements Solver {
	
	private double[][] matrix; //matriz usada para os calculos
	private List<double[][]> resultList; //lista usada pra retornar as matrizes passo-a-passo
	
	public GaussJordan() {
		resultList = new LinkedList<double[][]>();
	}
	
	/**
	 * Método que efetua a troca de uma linha por outra.
	 * Ex.: linha1	(trocando 1 por 3) =>	linha3
	 * 		linha2							linha2
	 * 		linha3							linha1
	 * @param coefficients A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @param line1 O número de uma das linhas que será permutada.
	 * @param line2 O número da outra linha que será permutada com a linha passada anterior.
	 * @param collumn O número da coluna a partir da qual os valores das linhas serão trocados.
	 * 				 É útil quando deseja-se trocar apenas parte dos valores das duas linhas. Por padrão, o valor é 0.
	 *
	 * Swap two lines from the given matrix.
	 * Ex.: line1	(swapping 1 for 3) =>	line3
	 * 		line2							line2
	 * 		line3							line1
	 * @param coefficients The matrix containing all the coefficients (including the independent) of the system.
	 * @param line1 The line number that will be swapped.
	 * @param line2 The other line that will be swapped with the previous one
	 * @param collumn The number of the collum. That contains which values from the lines will be swapped.
	 * 				 Useful when it need to change only parts of the values from the two lines. Default value is 0.
	 *
	 */
	private void changeLines(double[][] coefficients, int line1, int line2, int collumn) {
		int numCoeficientes = coefficients[0].length - 1; //nro de coeficientes da matriz (nao usa o indice 0)
		double aux;
		for (int i = collumn; i <= numCoeficientes; i++) {
			aux = coefficients[line1][i];
			coefficients[line1][i] = coefficients[line2][i];
			coefficients[line2][i] = aux;
		}
	}

	/**
	 * Método que divide os elementos de uma linha por um dos valores da linha,
	 * com a finalidade de igualar esse valor a 1.
	 * Antes de chamar esse método, todos os valores anteriores ao valor da coluna
	 * devem ser iguais a zero.
	 * Ex.: 0 0 0 0 4 8 2	(antes)
	 * 		0 0 0 0 1 2 0.5	(depois)
	 * @param coefficients A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @param line
	 * @param collumn
	 * 
	 * Divide elementes from a given line using one of its own value,
	 * and making this value equals to 1.
	 * Before calling this method, all the previous values from the collumn
	 * must be equals to 0.
	 * Ex.: 0 0 0 0 4 8 2	(before)
	 * 		0 0 0 0 1 2 0.5	(after)
	 * @param coefficients The matrix containing all the coefficients (including the independent) of the system.
	 * @param line The actual line.
	 * @param collumn The actual collumn. 
	 */
	private void divideLinePerValue(double[][] coefficients, int line, int collumn){
		int numTerms = coefficients[0].length - 1; //nro de coeficientes da matriz (nao usa o indice 0)
		for(int i = collumn + 1; i <= numTerms; i++) {
			coefficients[line][i] /= coefficients[line][collumn];
		}
		coefficients[line][collumn] = 1;
	}

	/**
	 * Método que zera todos os elementos não nulos de uma dada 
	 * coluna subtraindo-se cada linha diferente da atual de um 
	 * múltiplo apropriado da linha em questão. Ao encontrar a
	 * matriz formalizada com zeros, na parte inferior da diagonal
	 * principal, esses valores são substituídoss para zeraram a parte 
	 * superior da diagonal.
	 * 
	 * Makes all elements non-null equals to zero, from a given
	 * collumn, subtracting each line differrent from the actual
	 * of a given multiple from the given line. Once with the matrix
	 * is formalized with zeros,in the lower part of the main diagonal,
	 * these values are replaced to make the upper part equals to zero.
	 * 
     * @param coefficients The matrix containing all the coefficients (including the independent) of the system.
     * @param line The actual line.
	 * @param collumn The actual collumn. 
	 *   
	 */
	private void zeroTerm(double[][] coefficients, int line, int collumn) {
		int numLines = coefficients.length - 1;
		int numCollumns = coefficients[0].length - 1;
		for(int p = 1; p <= numLines; p++) {
			if( p != line && coefficients[p][collumn] != 0 ) {
				for(int q = collumn + 1; q <= numCollumns; q++) {
					coefficients[p][q] -= coefficients[p][collumn] * coefficients[line][q];
				}
				coefficients[p][collumn] = 0;
			}
		}
	}
	
	/**
	 * Método que encontra a linha em uma coluna onde se encontra o maior elemento.
	 * Finds the line in a given collumn containing the biggest ellement.
	 * @param i Número da linha a partir da qual (até a última linha) a busca será feita.
	 * @param j Número da coluna onde a busca será feita.
	 * @return Número da linha onde está o maior elemento da coluna.
	 * 
	 * @param i Line number which will be searched until the last line.
	 * @param j Collumn target of the search.
	 * @return Line number containing the biggest element of the collumn.
	 * 
	 */
	private int findBiggestElementInCollumn(int i,int j) {
		double max = matrix[i][i];
		//matriz[1][1]; 
//			Double.MIN_VALUE;
		int pos = i;
		int lineSize = matrix.length;
		for (int k = i; k < lineSize; k++) {
			double elementMat = matrix[k][j];
			if(elementMat > max){
				max = matrix[k][j];
				pos = k;
			}
		}
		return pos;
		
	}

	/**
	 * Método que constrói uma matriz onde os índices zeros [0][0] são desconsiderados.
	 * O intuito disso é facilitar o manuseio da matriz.
	 * @param coefficients A matriz 'tradicional'.
	 * @return A matriz aumentada em uma linha e uma coluna, onde a 1a linha e a 1a coluna têm valores nulos.

	 * Build the matrix where all the zeros are discarted.
	 * Making it easy to handle the matrix.
	 * @param coefficients The main matrix
	 * @return The matrix in echelon form where all the values from the first line and first 
	 * collum are equals to zero.
	 */
	private double[][] buildMatrix(double[][] coefficients, double[] terms) {
		int numLines = coefficients.length;
		int numCollumn = coefficients[0].length;
		matrix = new double[numLines + 1][numCollumn + 2]; //desconsidera os indices 0
		
		for (int i = 1; i <= numLines; i++) {
			for (int j = 1; j <= numCollumn; j++) {
				matrix[i][j] = coefficients[i - 1][j - 1];
			}
			matrix[i][numCollumn + 1] = terms[i - 1];
		}
		
		return matrix;
	}
	
	/**
	 * Método que imprime os valores atuias da matriz.
	 * @param matrix A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * Formalize the given matrix
	 * @param matrix The matrix containing all the coefficients (including the independent) of the system.
	 */
	private double[][] formalizeMatrix(double[][] matrix){ // antes era o imprimeMatriz
		int numLinhas = matrix.length - 1; //subtrai 1 pra eliminar indice 0
		int numColunas = matrix[0].length - 1; //subtrai 1 pra eliminar indice 0
		double[][] matrizAux = new double[numLinhas][numColunas];
		                                
		for(int i = 1; i <= numLinhas; i++){
			for(int j = 1; j <= numColunas; j++){
				matrizAux[i-1][j-1]=  matrix[i][j];
			}
		}
		return matrizAux;
	}

	/**
	 * Resolve o sistema de equações linear através do método de Gauss-Jordan com pivoteamento.
	 * @param matrix A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @return rank da matriz
	 * 
	 * Solve the system using the Gauss-jordan method with pivoting.
	 * @param matrix The matrix containing all the coefficients (including the independent) of the system.
	 * @return rank of the matrix 
	 */
	private int gaussJordanPivoting(double[][] matrix) {
		int i, j, k, numLines, numCollumn;
		numLines = matrix.length - 1; //desconsidera os indices 0
		numCollumn = matrix[0].length - 1; //desconsidera os indices 0
		i = 1;
		j = 1;
		while( i <= numLines && j <= numCollumn ){

			//procura na coluna j um valor nao nulo nas linhas abaixo da linha i
			k = i;
			while( k <= numLines && matrix[k][j] == 0 ) {
				k++;
			}

			//valor nao nulo achado na linha k
			if( k <= numLines ){

				//troca a linha que contem o valor nao nulo com a linha k
				if( k != i ) {
					changeLines(matrix, i, k, j);
					resultList.add(formalizeMatrix(matrix));
	            }

				//valor da diagonal principal diferente de 1, entao tenta colocar 1 
	            if( matrix[i][j] != 1 ){
	            	divideLinePerValue(matrix, i, j);
	            	resultList.add(formalizeMatrix(matrix));
	            }

	            //zera todos os outros valores da coluna
	            zeroTerm(matrix, i, j);
	            resultList.add(formalizeMatrix(matrix));
	            i++;
	        }
	        j++;
	    }	        

		return i;
	}
	
	/**
	 * Resolve o sistema de equações linear através do método de Gauss-Jordan sem pivoteamento.
	 * @param matrix A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @return rank da matriz
	 * 
	 * Solve the system using the Gauss-jordan method without pivoting.
	 * @param matrix The matrix containing all the coefficients (including the independent) of the system.
	 * @return rank of the matrix 
	 */
	private int gaussJodanNoPivoting(double[][] matrix) {
		int i, j, k, numLines, numCollumns;
		numLines = matrix.length - 1; //desconsidera os indices 0
		numCollumns = matrix[0].length - 1; //desconsidera os indices 0
		i = 1;
		j = 1;
		while( i <= numLines && j <= numCollumns ){

			//procura na coluna j um valor nao nulo nas linhas abaixo da linha i
			k = i;
			//encontra onde se encontra o maior elemento da coluna e
			//troca a linha atual por essa linha
			int biggestElementFromCollumn = findBiggestElementInCollumn(k, j);
			changeLines(matrix, i, biggestElementFromCollumn, j);
			
			while( k <= numLines && matrix[k][j] == 0 ) {
				k++;
			}

			//valor nao nulo achado na linha k
			if( k <= numLines ){

				//troca a linha que contem o valor nao nulo com a linha k
				if( k != i ) {
					changeLines(matrix, i, k, j);
					resultList.add(formalizeMatrix(matrix));
	            }

				//valor da diagonal principal diferente de 1, entao tenta colocar 1 
	            if( matrix[i][j] != 1 ){
	            	divideLinePerValue(matrix, i, j);
	            	resultList.add(formalizeMatrix(matrix));
	            }

	            //zera todos os outros valores da coluna
	            zeroTerm(matrix, i, j);
	            resultList.add(formalizeMatrix(matrix));
	            i++;
	        }
	        j++;
	    }
		return i;
	}
	
	@Override //parece que nao é usado.
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config) {
		
		return solve(coeficientes, termos, aprox, iteracoesMax, config);
	}

	
	/**
	 * Solve the system using the given parameters from the GUI.
	 * 
	 */
	public Result solve(double[][] coefficients, double[] terms, double aprox,
			int maxIterations, Config config) {
		matrix = buildMatrix(coefficients, terms);
		
		boolean withPivoting = (Boolean) config.get(Config.pivoteamento);
		
		if (withPivoting) {
			gaussJordanPivoting(matrix);
		} else {
			gaussJodanNoPivoting(matrix);
		}
		double[][] lastResp = resultList.get(resultList.size()-1);
		resultList.add(parseSolution(lastResp));
		ResultMSN resp = new ResultMSN(this.resultList);
		
		return new ResultMSN(this.resultList);
	}

	/**
	 * Parse the actual matrix to the default model solution
	 * @param matrix The actual matrix.
	 * @return The matrix in the correct form.
	 */
	private double[][] parseSolution(double[][] matrix) {
		double[][] sol = new double[matrix.length][1];
		double element =0;
		for (int i = 0; i < matrix.length; i++) {
			int coluna = matrix.length;
			element = matrix[i][coluna];
			sol[i][0] = element;
		}
		
		return sol;
		
	}
	
	/**
	 * 
	 * @return The list containing the results of each iteration.
	 */
	public List<double[][]> getResultList() {
		return resultList;
	}
	
}
