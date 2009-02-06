package br.edu.ufcg.msnlab.methods.choleskyqr.logic_methods;

/** Decomposicao Cholesky.
<P>
Para uma matriz sim�trica positiva, a decomposi��o Cholesky � uma matriz triangular inferior L, 
para que A = L * L '.
<P>
Se a matriz n�o � sim�trica positiva o construtor retorna uma decomposi��o parcial, e define 
um sinalizador interno que pode ser consultados mediante o metodo isSPD ().
 */


@SuppressWarnings("serial")
public class Cholesky implements java.io.Serializable {



   /** Array utilizando internamente na decomposicao.
   @serial valor interno de armazenamento.
   */
   private double[][] L;

   /** Linhas e colunas de dimensoes quadradas.
   @serial matriz de dimensoes.
   */
   private int n;

   /** Flag que indica se a matriz eh simetrica e positiva
   @serial a flag define se a matriz eh simetrica e positica.
   */
   private boolean isspd;



   /** O Algoritmo da decomposicao Cholesky para uma matriz simetrica e positiva.
   @param matriz simetrica e positiva.
   @return     Estrutura para acessar L e flag.
   */

   public Cholesky (Matrix Arg) {


     // Initializando....
      double[][] A = Arg.getArray();
      n = Arg.getRowDimension();
      L = new double[n][n];
      isspd = (Arg.getColumnDimension() == n);
      // Loop...
      for (int j = 0; j < n; j++) {
         double[] Lrowj = L[j];
         double d = 0.0;
         for (int k = 0; k < j; k++) {
            double[] Lrowk = L[k];
            double s = 0.0;
            for (int i = 0; i < k; i++) {
               s += Lrowk[i]*Lrowj[i];
            }
            Lrowj[k] = s = (A[j][k] - s)/L[k][k];
            d = d + s*s;
            isspd = isspd & (A[k][j] == A[j][k]); 
         }
         d = A[j][j] - d;
         isspd = isspd & (d > 0.0);
         L[j][j] = Math.sqrt(Math.max(d,0.0));
         for (int k = j+1; k < n; k++) {
            L[j][k] = 0.0;
         }
      }
   }


   /** A matriz eh simetrica e positiva?
   @return     true se A for simetrica e positiva.
   */

   public boolean isSPD () {
      return isspd;
   }

   /** Retorna um valor triangular.
   @return     L
   */

   public Matrix getL () {
      return new Matrix(L,n,n);
   }

   /** Resolve A*X = B
   @param  B   Uma Matriz solucoes.
   @return     X tal que L*L'*X = B
   @exception  IllegalArgumentException  Matriz dimensoes de linhas devem concordar.
   @exception  RuntimeException  Matriz nao eh simetrica-positiva.
   */

   public Matrix solve (Matrix B) {
      if (B.getRowDimension() != n) {
         throw new IllegalArgumentException("Matrix row dimensions must agree.");
      }
      if (!isspd) {
         throw new RuntimeException("Matrix is not symmetric positive definite.");
      }

      // Copy right hand side.
      double[][] X = B.getArrayCopy();
      int nx = B.getColumnDimension();

	      // Solve L*Y = B;
	      for (int k = 0; k < n; k++) {
	        for (int j = 0; j < nx; j++) {
	           for (int i = 0; i < k ; i++) {
	               X[k][j] -= X[i][j]*L[k][i];
	           }
	           X[k][j] /= L[k][k];
	        }
	      }
	
	      // Solve L'*X = Y;
	      for (int k = n-1; k >= 0; k--) {
	        for (int j = 0; j < nx; j++) {
	           for (int i = k+1; i < n ; i++) {
	               X[k][j] -= X[i][j]*L[i][k];
	           }
	           X[k][j] /= L[k][k];
	        }
	      }
      
      
      return new Matrix(X,n,nx);
   }
   
   public Matrix solve1 (Matrix B) {
	      if (B.getRowDimension() != n) {
	         throw new IllegalArgumentException("Matrix row dimensions must agree.");
	      }
	      if (!isspd) {
	         throw new RuntimeException("Matrix is not symmetric positive definite.");
	      }

	      // Copy right hand side.
	      double[][] X = B.getArrayCopy();
	      int nx = B.getColumnDimension();

		      // Solve L*Y = B;
		      for (int k = 0; k < n; k++) {
		        for (int j = 0; j < nx; j++) {
		           for (int i = 0; i < k ; i++) {
		               X[k][j] -= X[i][j]*L[k][i];
		           }
		           X[k][j] /= L[k][k];
		        }
		      }
		
		      // Solve L'*X = Y;
		      for (int k = n-1; k >= 0; k--) {
		        for (int j = 0; j < nx; j++) {
		           for (int i = k+1; i < n ; i++) {
		               X[k][j] -= X[i][j]*L[i][k];
		           }
		           X[k][j] /= L[k][k];
		        }
		      }
	      
	      
	      return new Matrix(X,n,nx);
	   }
}

