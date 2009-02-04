package br.edu.ufcg.msnlab.methods;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.util.Config;

/**
 * This class implements the SVD decomposition using Golub-Reinsch method.
 * @author Clerton Ribeiro
 * @author Adauto Trigueiro
 *
 */

public class SVD implements Solver{

	private double[][] matrix;
	// private ResultMSN results;
	private List<double[][]> results;
	
	
	public SVD() {
		
		this.results = new ArrayList<double[][]>();
				
	}
	
	
	/**
	 * Multiplies two matrices
	 * @param A The first matrix
	 * @param B The second matrix
	 * @param C The result
	 * @return A array of C matrix
	 */
	public static double[][] matrixMultiply (double[][] A, double[][] B, double[][] C) {

		  int m = A.length;
		  int n = A[0].length;
		  int p = B[0].length;
		  
			for (int j = p; --j >= 0; ) {
				for (int i = m; --i >= 0; ) {
					double s = 0;
					for (int k = n; --k >= 0; ) {
						s += A[i][k] * B[k][j];
					}
					C[i][j]= s + C[i][j];
				}
			}
			return C;  
	 }
	
	/**
	 * Copy all the terms of one matrix in another one.
	 * @param matrix The matrix to be copied.
	 * @return The matrix copy.
	 */
	public static double[][] copyMatrix(double[][] matrix) {
		double[][] copy = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				copy[i][j] = matrix[i][j];
			}
		}
		return copy;
	}
	
	/** Matrix transpose.
	   @return    A'
	   */

	   public double[][] transpose (double[][] A) {
	      double[][] C = new double[A.length][A[0].length];
	      for (int i = 0; i < A[0].length; i++) {
	         for (int j = 0; j < A.length; j++) {
	            C[j][i] = A[i][j];
	         }
	      }
	      return C;
	   }

	@Override
	public Result solve(double[][] coeficientes, double[] estimativas,
			double[] termos, double aprox, int iteracoesMax, Config config)
			throws MSNException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Resolve the system represented by the values passed.
	 * @param coeficients The matrix that contains the coefficients of the system.
	 * @param terms The independent terms of the system.
	 * @param aprox The residue of the error in the system.
	 * @param iteracoesMax The maximum number of iterations.
	 * @param config The configuration for this method.
	 */
	
	@Override
	public Result solve(double[][] coeficientes, double[] termos, double aprox,
			int iteracoesMax, Config config) throws MSNException {
		this.matrix = coeficientes;		
		SVDImpl svd = new SVDImpl(this.matrix);
		double[][] s = svd.getS();
		double[][] v = svd.getV();
		double[][] u = svd.getU();
		double[][] si = new Matrix(s).inverse().getArray();
		double[][] ut = transpose(u);
		
		double[][] vsi = matrixMultiply(v, si, new double[v.length][si[0].length]);
		double[][] stvut = matrixMultiply(vsi, ut, new double[vsi.length][ut[0].length]);
		double[][] terms = new double[termos.length][1];
		for(int i=0;i<terms.length;i++) {
			terms[i][0] = termos[i];
		}
		double[][] stvutb = matrixMultiply(stvut, terms, new double[si.length][v[0].length]);
		results.add(stvutb);
		return new ResultMSN(this.results);
	}
}
