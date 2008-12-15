package br.edu.ufcg.msnlab.methods.choleskyQR.interfaces;

import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.methods.choleskyQR.logic_methods.Matrix;
import br.edu.ufcg.msnlab.methods.choleskyQR.logic_methods.QR;
import br.edu.ufcg.msnlab.util.Config;

/**
 * Classe respons�vel por solucionar equa�oes lineares seguindo o procedimento do metodo QR
 * @author Gildo Jr e Diego Melo Gurj�o
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
	      System.out.println("Sem res�duo");
	      X.print(3, 50);
	      Matrix R1 = B.minus(A.times(X));
	      
	        
	    
	       
	     
	      if(iteracoesMax < 1){
	    	  iteracoesMax = NUM_ITERACOES_DEFAULT;
	      }

	      
	      double[][] xteste = R.getArray();
	      // retornando a matriz da itera��o 1.
	      resultMsn.addResult(xteste);
	      
	      int iteracoesAtual =1;
	      while((checkCondicao(R1, aprox))&& (iteracoesAtual <= iteracoesMax)){
	    	  System.out.println("Itera��o "+iteracoesAtual);
	    	  System.out.println("aproxima��o "+aprox);
	    	  Matrix C = A.solve(R1);
	    	  Matrix Xlinha = X.plus(C);
	    	  R1 = B.minus(A.times(Xlinha));
	    	  R1.print(3, 50);
	    	  xteste = Xlinha.getArray();
		      // retornando a matriz da itera��o 1.
		      resultMsn.addResult(xteste);
		      iteracoesAtual++;
	      }
	      
	      System.out.println("Resultado desejado =");
	      R1.print(3, 5);
		
		return resultMsn;
	}
	
	/** Checa a diferen�a entre as Matrizes para n�o perfimitir grandes diferencas **/

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

		   public static void main(String args[]){
			   double[][] xvals = {{1.,1.,0.},{1.,2.,-1.},{0.,-1.,3.}};
			    double[] bvals = {2.,1.,5.};
			    
			    Matrix A = new Matrix(xvals);
			    System.out.println(A.norm2()+" maioOOOOr valor A");

			  
			    QRSolverImpl qr = new QRSolverImpl();
			    qr.solve(xvals, bvals,  0.00000000000000000000000000000000000000000000000000000000000001, 50000, null);
			   
			    
			    /*double[][] xvals1 = {{4.,2.,-4.},{2.,10.,4.},{-4.,4.,9.}};
			    double[] bvals1 = {2.,16.,9.};
			    
			    Matrix A1 = new Matrix(xvals1);
			    System.out.println();
			    System.out.println("Segundo teste");

			  
			    CholeskySolverImpl ch1 = new CholeskySolverImpl();
			    ch1.solve(xvals1, bvals1, 0.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005, 50000, null);*/
			   
			   
		   }

}
