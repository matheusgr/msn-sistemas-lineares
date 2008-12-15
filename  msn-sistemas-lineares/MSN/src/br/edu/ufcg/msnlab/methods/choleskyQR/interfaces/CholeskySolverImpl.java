package br.edu.ufcg.msnlab.methods.choleskyQR.interfaces;

import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.methods.choleskyQR.logic_methods.Cholesky;
import br.edu.ufcg.msnlab.methods.choleskyQR.logic_methods.Matrix;
import br.edu.ufcg.msnlab.util.Config;
/**
 * Esta classe tem como objetivo solucionar 
 * equa��es matriciais pelo m�todo de Cholesky
 * 
 * @author Diego Gurj�o e Gildo Junior
 *
 */
public class CholeskySolverImpl implements Solver{

	@SuppressWarnings("unused")
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

	 /** Checa a diferen�a entre as Matrizes para n�o perfimitir grandes diferencas **/

	   private void check(Matrix X, Matrix Y) {
	      double eps = Math.pow(BASE_DE_TESTE_DEFAULT,EXPOENTE_DE_TESTE_DEFAULT);
	      if (X.norm1() == 0. & Y.norm1() < 10*eps) return;
	      if (Y.norm1() == 0. & X.norm1() < 10*eps) return;
	      if (X.minus(Y).norm1() > 1000*eps*Math.max(X.norm1(),Y.norm1())) {
	         throw new RuntimeException("A diferen�a entre as matrizes eh muito grande: " +  Double.toString(X.minus(Y).norm1()));
	      }
	   }
	   
		 /** Checa se ainda eh possivel continuar o refinamento **/

	   private boolean checkCondicao(Matrix R, double aprox) {
	       System.out.println("Checando condicoes");
		  double eps = aprox;
		  
		  // if (X.norm1() == 0. & Y.norm1() < 10*eps) return;
		  // if (Y.norm1() == 0. & X.norm1() < 10*eps) return;
	      if (R.norm1() > eps) {
	         //throw new RuntimeException("The norm of (X-Y) is too large: " +  Double.toString(X.minus(Y).norm1()));
	    	  return true;
	      }
	      return false;
	   }

	   /**
	    * Teste para verificar se o m�todo est� sendo aplicado corretamente.
	    * @param args argumento do main
	    */
	   public static void main(String args[]){
		   double[][] xvals = {{1.,1.,0.},{1.,2.,-1.},{0.,-1.,3.}};
		   double[] bvals = {2.,1.,5.};
		    
		    Matrix A = new Matrix(xvals);
		    System.out.println(A.norm2()+" maioOOOOOr valor A");

		  
		    CholeskySolverImpl ch = new CholeskySolverImpl();
		    ch.solve(xvals, bvals, 0.000000000000000000000005, 50000, null);
		   
		    
		    /*double[][] xvals1 = {{4.,2.,-4.},{2.,10.,4.},{-4.,4.,9.}};
		    double[] bvals1 = {2.,16.,9.};
		    
		    Matrix A1 = new Matrix(xvals1);
		    System.out.println();
		    System.out.println("Segundo teste");

		  
		    CholeskySolverImpl ch1 = new CholeskySolverImpl();
		    ch1.solve(xvals1, bvals1, 0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005, 50000, null);*/
		   
		   
	   }

	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Calculo da solucao do sistema de equacoes atravez do metodo de Cholesky e refinamento do resultado
	 */
	@Override
	public Result solve(double[][] coeficientes, double[] termos, double aprox,
			int iteracoesMax, Config config) {
	
	      Matrix A = new Matrix(coeficientes);
	      Matrix B = transformaTermonsEmMatriz(termos);
	      Cholesky Chol = A.chol(); 
	      Matrix L = Chol.getL();
	      check(A,L.times(L.transpose()));
	      Matrix X = Chol.solve(B);
	      Matrix R = B.minus(A.times(X));
	      
	      if(iteracoesMax < 1){
	    	  iteracoesMax = NUM_ITERACOES_DEFAULT;
	      }
    	 
	      double[][] xteste = X.getArray();
	      // retornando a matriz da itera��o 1.
	      resultMsn.addResult(xteste);
	      R.print(3, 50);
	      int iteracoesAtual =1;
	      while((checkCondicao(R, aprox))&& (iteracoesAtual <= iteracoesMax)){
	    	  System.out.println("Itera��o "+iteracoesAtual);
	    	  System.out.println("aproxima��o "+aprox);
	    	  Matrix C = A.solve(R);
	    	  Matrix Xlinha = X.plus(C);
	    	  R = B.minus(A.times(Xlinha));
	    	  R.print(3, 50);
	    	  xteste = Xlinha.getArray();
		      // retornando a matriz da itera��o 1.
		      resultMsn.addResult(xteste);
		      iteracoesAtual++;
	      }
	      
	      System.out.println("Resultado desejado =");
	      X.print(3, 5);
		
		return resultMsn;
	}
}
