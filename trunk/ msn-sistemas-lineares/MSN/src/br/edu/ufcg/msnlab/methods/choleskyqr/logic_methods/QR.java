package br.edu.ufcg.msnlab.methods.choleskyqr.logic_methods;
import Jama.util.Maths;


/** Decomposicao QR.
<P>
    Para uma matrix A de dimensoes mxn com m >= n, uma decomposicao QR eh uma
   matriz ortogonal m-por-n Q e uma matriz triangular superior n-by-n  R tal que
   A = Q*R.
<P>
   A decomposicao QR  sempre existe, mesmo que a matriz n�o tenha rank completo, de modo que o construtor nunca ir� falhar.
   O uso principal da decomposi��o QR est� em m�nimos quadrados solu��o,solu��es simult�nea de equa��es lineares. Isto ir� falhar se isFullRank ()
    retorna falso. 
*/

public class QR implements java.io.Serializable {



   /** Array de armazenamento interno.
   @serial array de armazenamento interno.
   */
   private double[][] QR;

   /** dimensoes de linhas e colunas.
   @serial dimensao de colunas.
   @serial dimensao de linhas.
   */
   private int m, n;

   /** Array de armazenamento da diagonal princial R.
   @serial diagonal de R.
   */
   private double[] Rdiag;



   /** Decomposicao QR, computada pela reflexao Householder.
   @param A    matriz Retangular
   @return     Estrutura para acessar R e vetores Householder computador em Q.
   */

   public QR (Matrix A) {
      // Initializa....
      QR = A.getArrayCopy();
      m = A.getRowDimension();
      n = A.getColumnDimension();
      Rdiag = new double[n];

      //loop.
      for (int k = 0; k < n; k++) {
         double nrm = 0;
         for (int i = k; i < m; i++) {
            nrm = Maths.hypot(nrm,QR[i][k]);
         }

         if (nrm != 0.0) {
            if (QR[k][k] < 0) {
               nrm = -nrm;
            }
            for (int i = k; i < m; i++) {
               QR[i][k] /= nrm;
            }
            QR[k][k] += 1.0;

            for (int j = k+1; j < n; j++) {
               double s = 0.0; 
               for (int i = k; i < m; i++) {
                  s += QR[i][k]*QR[i][j];
               }
               s = -s/QR[k][k];
               for (int i = k; i < m; i++) {
                  QR[i][j] += s*QR[i][k];
               }
            }
         }
         Rdiag[k] = -nrm;
      }
   }


   /** A matriz � full rank?
   @return     true se R, e A, forem full rank.
   */

   public boolean isFullRank () {
      for (int j = 0; j < n; j++) {
         if (Rdiag[j] == 0)
            return false;
      }
      return true;
   }

   /** Retorna o conjunto de vetores Householder
   @return     Parte inferior trapezoidal das columns define nas reflecoes
   */

   public Matrix getH () {
      Matrix X = new Matrix(m,n);
      double[][] H = X.getArray();
      for (int i = 0; i < m; i++) {
         for (int j = 0; j < n; j++) {
            if (i >= j) {
               H[i][j] = QR[i][j];
            } else {
               H[i][j] = 0.0;
            }
         }
      }
      return X;
   }

   /** Retorna o vator R matriz triangular superior
   @return     R
   */

   public Matrix getR () {
      Matrix X = new Matrix(n,n);
      double[][] R = X.getArray();
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            if (i < j) {
               R[i][j] = QR[i][j];
            } else if (i == j) {
               R[i][j] = Rdiag[i];
            } else {
               R[i][j] = 0.0;
            }
         }
      }
      return X;
   }

   /** Gera e retorna o tamano econ�mico- fator ortogonal
   @return     Q
   */

   public Matrix getQ () {
      Matrix X = new Matrix(m,n);
      double[][] Q = X.getArray();
      for (int k = n-1; k >= 0; k--) {
         for (int i = 0; i < m; i++) {
            Q[i][k] = 0.0;
         }
         Q[k][k] = 1.0;
         for (int j = k; j < n; j++) {
            if (QR[k][k] != 0) {
               double s = 0.0;
               for (int i = k; i < m; i++) {
                  s += QR[i][k]*Q[i][j];
               }
               s = -s/QR[k][k];
               for (int i = k; i < m; i++) {
                  Q[i][j] += s*QR[i][k];
               }
            }
         }
      }
      return X;
   }

   /** Realiza a operacao of A*X = B
   @param B    Uma Matriz com os numeros de linhas e colunas de A.
   @return     X que minimiza  Q*R*X-B.
   @exception  IllegalArgumentException  Matriz dimensoes das linhas devem concorcar.
   @exception  RuntimeException  Matrix � ranck embora deficiente.
   */

   public Matrix solve (Matrix B) {
      if (B.getRowDimension() != m) {
         throw new IllegalArgumentException("Matrix row dimensions must agree.");
      }
      if (!this.isFullRank()) {
         throw new RuntimeException("Matrix is rank deficient.");
      }
      
     
      int nx = B.getColumnDimension();
      double[][] X = B.getArrayCopy();

      // Computa Y = transpose(Q)*B
      for (int k = 0; k < n; k++) {
         for (int j = 0; j < nx; j++) {
            double s = 0.0; 
            for (int i = k; i < m; i++) {
               s += QR[i][k]*X[i][j];
            }
            s = -s/QR[k][k];
            for (int i = k; i < m; i++) {
               X[i][j] += s*QR[i][k];
            }
         }
      }
      // Solve R*X = Y;
      for (int k = n-1; k >= 0; k--) {
         for (int j = 0; j < nx; j++) {
            X[k][j] /= Rdiag[k];
         }
         for (int i = 0; i < k; i++) {
            for (int j = 0; j < nx; j++) {
               X[i][j] -= X[k][j]*QR[i][k];
            }
         }
      }
      return (new Matrix(X,n,nx).getMatrix(0,n-1,0,nx-1));
   }
}
