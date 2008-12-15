package br.edu.ufcg.msnlab.methods;

/**
 * @author Hugo Marques.
 */
public class Methods {
	
	public static final String EliminacaoGauss = "ELIMINACAO_GAUSS";	
	public static final String EliminacaoGaussJordan = "GAUSS_JORDAN";	
	public static final String DecomposicaoLU = "DECOMPOSICAO_LU";
	public static final String DecomposicaoSVD = "DECOMPOSICAO_SVD";
	public static final String DecomposicaoCholesky = "DECOMPOSICAO_CHOLESKY";
	public static final String DecomposicaoQR = "DECOMPOSICAO_QR";
	public static final String GaussJacobi = "GAUSS_JACOBI";
	public static final String GaussSeidel = "GAUSS_SEIDEL";
	
	public static boolean isDirect(String method) {
		return EliminacaoGauss.equals(method) ||
			EliminacaoGaussJordan.equals(method) ||
			DecomposicaoCholesky.equals(method) ||
			DecomposicaoLU.equals(method) ||
			DecomposicaoSVD.equals(method) ||
			DecomposicaoQR.equals(method);
	}

	public static boolean isIteractive(String method) {
		return GaussJacobi.equals(method) || GaussSeidel.equals(method);
	}

}
