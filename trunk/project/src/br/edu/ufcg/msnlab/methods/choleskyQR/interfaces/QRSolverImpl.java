package br.edu.ufcg.msnlab.methods.choleskyQR.interfaces;

import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.methods.choleskyQR.logic_methods.Matrix;
import br.edu.ufcg.msnlab.methods.choleskyQR.logic_methods.QR;
import br.edu.ufcg.msnlab.util.Config;

/**
 * Classe responsï¿½vel por solucionar equaï¿½oes lineares seguindo o procedimento do metodo QR
 * @author Gildo Jr e Diego Melo Gurjï¿½o
 *
 */
public class QRSolverImpl implements Solver{

	/**
	 * Numero de Interacoes default, utilizado caso o usuario entre com um numero de iteracoes nao perminito ou sem um valor definido
	 */
	private final int NUM_ITERACOES_DEFAULT = 50;
	/**
	 * A base de testes utilizado para comparar se a multiplicacao das matrizes ocorreu de forma perfeita
	 */
	private final double BASE_DE_TESTE_DEFAULT = 2.0;
	/**
	 * Exploente utilizando para calcular se a multiplicacao das matrizes ocorreu de forma correta
	 */
	private final double EXPOENTE_DE_TESTE_DEFAULT = -52.0;
	/**
	 * Criacao do objeto resultMSN que sera retornado para interface
	 */
	private ResultMSN resultMsn = new ResultMSN();
	
	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Calculo da solucao do sistema de equacoes atravez do metodo QR e refinamento do resultado
	 */
	@Override
	public Result solve(double[][] coeficientes, double[] termos, double aprox,
			int iteracoesMax, Config config) {

		  Matrix A = new Matrix(coeficientes);
		  Matrix B = transformaTermonsEmMatriz(termos);
	      QR QR = A.qr();
	      Matrix R = QR.getR();
	     
	      check(A,QR.getQ().times(R));
	     
	      Matrix X = QR.solve(B);
	      Matrix R1 = B.minus(A.times(X));
	      
	        
	    
	       
	     
	      if(iteracoesMax < 1){
	    	  iteracoesMax = NUM_ITERACOES_DEFAULT;
	      }

	      
	      double[][] xteste = X.getArray();
	      // retornando a matriz da iteração 1.
	      resultMsn.addResult(xteste);
	      
	      int iteracoesAtual =1;
	      while((checkCondicao(R1, aprox))&& (iteracoesAtual <= iteracoesMax)){
	    	  Matrix C = A.solve(R1);
	    	  Matrix Xlinha = X.plus(C);
	    	  R1 = B.minus(A.times(Xlinha));
	    	  xteste = Xlinha.getArray();
		      // retornando a matriz da iteração 1.
		      resultMsn.addResult(xteste);
		      iteracoesAtual++;
	      }
		return resultMsn;
	}
	
	/** Checa a diferença entre as Matrizes para não perfimitir grandes diferencas **/

	   private void check(Matrix X, Matrix Y) {
	      double eps = Math.pow(BASE_DE_TESTE_DEFAULT,EXPOENTE_DE_TESTE_DEFAULT);
	      if (X.norm1() == 0. & Y.norm1() < 10*eps) return;
	      if (Y.norm1() == 0. & X.norm1() < 10*eps) return;
	      if (X.minus(Y).norm1() > 1000*eps*Math.max(X.norm1(),Y.norm1())) {
	         throw new RuntimeException("The norm of (X-Y) is too large: " +  Double.toString(X.minus(Y).norm1()));
	      }
	   }
	   
	   /**
		 * Recebe um vetor de termos e retorna uma matriz
		 * @param termos um conjunto de termos
		 * @return uma matriz coluna
		 */
		private Matrix transformaTermonsEmMatriz(double[] termos) {
			
			double[] matrizTermos = termos;		
			Matrix B = new Matrix(matrizTermos, matrizTermos.length);
			return B;
		}
		
		   
		 /** Checa se ainda eh possivel continuar o refinamento **/

		   private boolean checkCondicao(Matrix R, double aprox) {
		      
			  double eps = aprox;
			  // if (X.norm1() == 0. & Y.norm1() < 10*eps) return;
			  // if (Y.norm1() == 0. & X.norm1() < 10*eps) return;
		      if (R.norm1() > eps) {
		         //throw new RuntimeException("The norm of (X-Y) is too large: " +  Double.toString(X.minus(Y).norm1()));
		    	  return true;
		      }
		      return false;
		   }

}
