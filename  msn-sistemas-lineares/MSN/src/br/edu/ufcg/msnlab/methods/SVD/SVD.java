package br.edu.ufcg.msnlab.methods.SVD;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.methods.LU.MatrixLU;
import br.edu.ufcg.msnlab.util.Config;

/**
 * This class implements the SVD decomposition using Golub-Reinsch method.
 * @author Clerton Ribeiro
 * @author Adauto Trigueiro
 *
 */

public class SVD implements Solver{

	private MatrixLU Smatrix,Umatrix,Vmatrix,matrix;
	private double[][] extendedMatrix;
	// private ResultMSN results;
	private List<double[][]> results;
	
	
	public SVD() {
		
		this.results = new ArrayList<double[][]>();
				
	}

	/**
	 * Don't used
	 */
	
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
		this.extendedMatrix = increasedMatrix(coeficientes,termos);
		this.matrix = new MatrixLU(this.extendedMatrix,this.extendedMatrix.length,this.extendedMatrix.length);
		
		SVDImpl svd = new SVDImpl(matrix);
		MatrixLU Smatrix = svd.getS();
		MatrixLU Vmatrix = svd.getV();
		MatrixLU Umatrix = svd.getU();
		
		this.results.add(Smatrix.toArray());
		this.results.add(Vmatrix.toArray());
		this.results.add(Umatrix.toArray());
		double[][] Uinverse = new Jama.Matrix(Umatrix.toArray()).inverse().getArray();
		double[][] Vinverse = new Jama.Matrix(Vmatrix.toArray()).inverse().getArray();
		MatrixLU Uinv = new MatrixLU(Uinverse,Umatrix.getNumRows(),Umatrix.getNumColumns());
		MatrixLU Vinv = new MatrixLU(Vinverse,Vmatrix.getNumRows(),Vmatrix.getNumColumns());
		MatrixLU UV = new MatrixLU(new double[Umatrix.getNumRows()][Vmatrix.getNumColumns()],Umatrix.getNumRows(),Vmatrix.getNumColumns());
		double[][] UVinverse = MatrixLU.multiplicarMatriz(Uinv, Vinv,UV);

		
		MatrixLU UVinv = new MatrixLU(UVinverse,Umatrix.getNumRows(),Umatrix.getNumColumns());
		double[][] Sinverse = new Jama.Matrix(Smatrix.toArray()).inverse().getArray();
		MatrixLU Sinv = new MatrixLU(Sinverse,Smatrix.getNumRows(),Smatrix.getNumColumns());
		MatrixLU UVS = new MatrixLU(new double[Umatrix.getNumRows()][Vmatrix.getNumColumns()],Umatrix.getNumRows(),Vmatrix.getNumColumns());
		double[][] UVSinverse = MatrixLU.multiplicarMatriz(UVinv,Sinv,UVS);
		
		MatrixLU UVSinv = new MatrixLU(UVSinverse,Umatrix.getNumRows(),Umatrix.getNumColumns());
		double[][] X = MatrixLU.multiplicarMatriz(UVSinv, matrix.getColumn(matrix.getNumColumns()-1), new MatrixLU(new double[Umatrix.getNumRows()][1],Umatrix.getNumRows(),1));

		results.add(X);
		return new ResultMSN(this.results);
	}
	
	/**
	 * Construct the increased matrix with the coefficients matrix and the
	 * independent terms matrix.
	 * 
	 * @param coeficients The coefficients matrix.
	 * @param terms The independent terms matrix.
	 * @return The increased matrix.
	 */
	private double[][] increasedMatrix(double[][] coeficients, double[] terms) {
		double[][] mat = new double[coeficients.length][coeficients.length + 1];
		for (int i = 0; i < coeficients.length; i++) {
			for (int j = 0; j < coeficients.length; j++) {
				mat[i][j] = coeficients[i][j];
			}
		}
		for (int i = 0; i < terms.length; i++) {
			mat[i][coeficients.length] = terms[i];
		}
		this.results.add(copyMatrix(mat));
		return mat;
	}
	
	/**
	 * Copy all the terms of one matrix in another one.
	 * @param matrix The matrix to be copied.
	 * @return The matrix copy.
	 */
	private double[][] copyMatrix(double[][] matrix) {
		double[][] copy = new double[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				copy[i][j] = matrix[i][j];
			}
		}
		return copy;
	}

}
