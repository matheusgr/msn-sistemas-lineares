package br.edu.ufcg.msnlab.methods.GaussJordan;

import java.util.LinkedList;
import java.util.List;

import org.lsmp.djep.vectorJep.function.Length;

import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.util.Config;

/**
 * Essa classe contém métodos que resolvem um sistema de equações linear por meio do método Gauss-Jordan.
 * @author Rafael Dantas, Alan de Farias
 * 
 */

public class GaussJordan implements Solver {
	
	private double[][] matriz; //matriz usada para os calculos
	private List<double[][]> resultList; //lista usada pra retornar as matrizes passo-a-passo
	
	public GaussJordan() {
		resultList = new LinkedList<double[][]>();
	}
	
	/**
	 * Método que efetua a troca de uma linha por outra.
	 * Ex.: linha1	(trocando 1 por 3) =>	linha3
	 * 		linha2							linha2
	 * 		linha3							linha1
	 * @param coeficientes A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @param linha1 O número de uma das linhas que será permutada.
	 * @param linha2 O número da outra linha que será permutada com a linha passada anterior.
	 * @param coluna O número da coluna a partir da qual os valores das linhas serão trocados.
	 * 				 É útil quando deseja-se trocar apenas parte dos valores das duas linhas. Por padrão, o valor é 0.
	 */
	private void trocaLinhas(double[][] coeficientes, int linha1, int linha2, int coluna) {
		int numCoeficientes = coeficientes[0].length - 1; //nro de coeficientes da matriz (nao usa o indice 0)
		double aux;
		for (int i = coluna; i <= numCoeficientes; i++) {
			aux = coeficientes[linha1][i];
			coeficientes[linha1][i] = coeficientes[linha2][i];
			coeficientes[linha2][i] = aux;
		}
	}

	/**
	 * Método que divide os elementos de uma linha por um dos valores da linha,
	 * com a finalidade de igualar esse valor a 1.
	 * Antes de chamar esse método, todos os valores anteriores ao valor da coluna
	 * devem ser iguais a zero.
	 * Ex.: 0 0 0 0 4 8 2	(antes)
	 * 		0 0 0 0 1 2 0.5	(depois)
	 * @param coeficientes A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @param linha
	 * @param coluna
	 */
	private void divideLinhaPorValor(double[][] coeficientes, int linha, int coluna){
		int numCoeficientes = coeficientes[0].length - 1; //nro de coeficientes da matriz (nao usa o indice 0)
		for(int i = coluna + 1; i <= numCoeficientes; i++) {
			coeficientes[linha][i] /= coeficientes[linha][coluna];
		}
		coeficientes[linha][coluna] = 1;
	}

	/**
	 * Método que zera todos os elementos não nulos de uma dada 
	 * coluna subtraindo-se cada linha diferente da atual de um 
	 * múltiplo apropriado da linha em questão. Ao encontrar a
	 * matriz formalizada com zeros, na parte inferior da diagonal
	 * principal, esses valores são substituídoss para zeraram a parte 
	 * superior da diagonal.
	 * 
	 * @param coeficientes A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @param linha O número da linha atual.
	 * @param coluna O número da coluna da qual deseja-se zerar os valores.
	 *   
	 */
	private void zeraElemento(double[][] coeficientes, int linha, int coluna) {
		int numLinhas = coeficientes.length - 1;
		int numColunas = coeficientes[0].length - 1;
		for(int p = 1; p <= numLinhas; p++) {
			if( p != linha && coeficientes[p][coluna] != 0 ) {
				for(int q = coluna + 1; q <= numColunas; q++) {
					coeficientes[p][q] -= coeficientes[p][coluna] * coeficientes[linha][q];
				}
				coeficientes[p][coluna] = 0;
			}
		}
	}
	
	/**
	 * Método que encontra a linha em uma coluna onde se encontra o maior elemento.
	 * @param i Número da linha a partir da qual (até a última linha) a busca será feita.
	 * @param j Número da coluna onde a busca será feita.
	 * @return Número da linha onde está o maior elemento da coluna.
	 */
	private int encontraMaiorElementoDaColuna(int i,int j) {
		double max = matriz[i][i];
		//matriz[1][1]; 
//			Double.MIN_VALUE;
		int pos = i;
		int tamanhoLinha = matriz.length;
		for (int k = i; k < tamanhoLinha; k++) {
			double elementMat = matriz[k][j];
			if(elementMat > max){
				max = matriz[k][j];
				pos = k;
			}
		}
		return pos;
		
	}

	/**
	 * Método que constrói uma matriz onde os índices zeros [0][0] são desconsiderados.
	 * O intuito disso é facilitar o manuseio da matriz.
	 * @param coeficientes A matriz 'tradicional'.
	 * @return A matriz aumentada em uma linha e uma coluna, onde a 1a linha e a 1a coluna têm valores nulos.
	 */
	private double[][] constroiMatriz(double[][] coeficientes, double[] termos) {
		int numLinhas = coeficientes.length;
		int numColunas = coeficientes[0].length;
		matriz = new double[numLinhas + 1][numColunas + 2]; //desconsidera os indices 0
		
		for (int i = 1; i <= numLinhas; i++) {
			for (int j = 1; j <= numColunas; j++) {
				matriz[i][j] = coeficientes[i - 1][j - 1];
			}
			matriz[i][numColunas + 1] = termos[i - 1];
		}
		
		return matriz;
	}
	
	/**
	 * Método que imprime os valores atuias da matriz.
	 * @param matriz A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 */
	private double[][] imprimeMatriz(double[][] matriz){
		int numLinhas = matriz.length - 1; //subtrai 1 pra eliminar indice 0
		int numColunas = matriz[0].length - 1; //subtrai 1 pra eliminar indice 0
		double[][] matrizAux = new double[numLinhas][numColunas];
		                                
		for(int i = 1; i <= numLinhas; i++){
			for(int j = 1; j <= numColunas; j++){
				matrizAux[i-1][j-1]=  matriz[i][j];
			}
		}
		return matrizAux;
	}

	/**
	 * Resolve o sistema de equações linear através do método de Gauss-Jordan com pivoteamento.
	 * @param matriz A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @return rank da matriz
	 */
	private int gaussJordanComPivo(double[][] matriz) {
		int i, j, k, numLinhas, numColunas;
		numLinhas = matriz.length - 1; //desconsidera os indices 0
		numColunas = matriz[0].length - 1; //desconsidera os indices 0
		i = 1;
		j = 1;
		while( i <= numLinhas && j <= numColunas ){

			//procura na coluna j um valor nao nulo nas linhas abaixo da linha i
			k = i;
			while( k <= numLinhas && matriz[k][j] == 0 ) {
				k++;
			}

			//valor nao nulo achado na linha k
			if( k <= numLinhas ){

				//troca a linha que contem o valor nao nulo com a linha k
				if( k != i ) {
					trocaLinhas(matriz, i, k, j);
					resultList.add(imprimeMatriz(matriz));
	            }

				//valor da diagonal principal diferente de 1, entao tenta colocar 1 
	            if( matriz[i][j] != 1 ){
	            	divideLinhaPorValor(matriz, i, j);
	            	resultList.add(imprimeMatriz(matriz));
	            }

	            //zera todos os outros valores da coluna
	            zeraElemento(matriz, i, j);
	            resultList.add(imprimeMatriz(matriz));
	            i++;
	        }
	        j++;
	    }	        

		return i;
	}
	
	/**
	 * Resolve o sistema de equações linear através do método de Gauss-Jordan sem pivoteamento.
	 * @param matriz A matriz com todos os coeficientes (inclusive os independentes) do sistema.
	 * @return rank da matriz
	 */
	private int gaussJodanSemPivo(double[][] matriz) {
		int i, j, k, numLinhas, numColunas;
		numLinhas = matriz.length - 1; //desconsidera os indices 0
		numColunas = matriz[0].length - 1; //desconsidera os indices 0
		i = 1;
		j = 1;
		while( i <= numLinhas && j <= numColunas ){

			//procura na coluna j um valor nao nulo nas linhas abaixo da linha i
			k = i;
			//encontra onde se encontra o maior elemento da coluna e
			//troca a linha atual por essa linha
			int posMaiorElementoColuna = encontraMaiorElementoDaColuna(k, j);
			trocaLinhas(matriz, i, posMaiorElementoColuna, j);
			
			while( k <= numLinhas && matriz[k][j] == 0 ) {
				k++;
			}

			//valor nao nulo achado na linha k
			if( k <= numLinhas ){

				//troca a linha que contem o valor nao nulo com a linha k
				if( k != i ) {
					trocaLinhas(matriz, i, k, j);
					resultList.add(imprimeMatriz(matriz));
	            }

				//valor da diagonal principal diferente de 1, entao tenta colocar 1 
	            if( matriz[i][j] != 1 ){
	            	divideLinhaPorValor(matriz, i, j);
	            	resultList.add(imprimeMatriz(matriz));
	            }

	            //zera todos os outros valores da coluna
	            zeraElemento(matriz, i, j);
	            resultList.add(imprimeMatriz(matriz));
	            i++;
	        }
	        j++;
	    }
		return i;
	}
	
	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config) {
		
		return solve(coeficientes, termos, aprox, iteracoesMax, config);
	}

	@Override
	public Result solve(double[][] coeficientes, double[] termos, double aprox,
			int iteracoesMax, Config config) {
		matriz = constroiMatriz(coeficientes, termos);
		
		boolean comPivo = (Boolean) config.get(Config.pivoteamento);
		
		if (comPivo) {
			gaussJordanComPivo(matriz);
		} else {
			gaussJodanSemPivo(matriz);
		}
		double[][] lastResp = resultList.get(resultList.size()-1);
		resultList.add(parseSolution(lastResp));
		ResultMSN resp = new ResultMSN(this.resultList);
		
		return new ResultMSN(this.resultList);
	}

	private double[][] parseSolution(double[][] matriz) {
		double[][] sol = new double[matriz.length][1];
		double elemento =0;
		for (int i = 0; i < matriz.length; i++) {
			int coluna = matriz.length;
			elemento = matriz[i][coluna];
			sol[i][0] = elemento;
		}
		
		return sol;
		
	}

	public List<double[][]> getResultList() {
		return resultList;
	}
	
}
