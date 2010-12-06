package br.edu.ufcg.msnlab.methods;

import java.lang.reflect.Field;

/**
 * @author Hugo Marques.
 * @author Renato Miceli
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
	public static final String GaussSeidelWPreconditioning = "GAUSS_SEIDEL_WITH_PRECOND";

	public static boolean isDirect(String method) {
		return EliminacaoGauss.equals(method)
				|| EliminacaoGaussJordan.equals(method)
				|| DecomposicaoCholesky.equals(method)
				|| DecomposicaoLU.equals(method)
				|| DecomposicaoSVD.equals(method)
				|| DecomposicaoQR.equals(method);
	}

	public static boolean isIteractive(String method) {
		return GaussJacobi.equals(method) || GaussSeidel.equals(method)
				|| GaussSeidelWPreconditioning.equals(method);
	}

	public static String[] getMethods() {
		Field[] fields = Methods.class.getFields();
		String[] methods = new String[fields.length];
		for (int i = 0; i < methods.length; i++) {
			try {
				methods[i] = (String) fields[i].get(null);
			} catch (Exception e) {
				throw new RuntimeException("Could not get methods.");
			}
		}
		return methods;
	}

}
