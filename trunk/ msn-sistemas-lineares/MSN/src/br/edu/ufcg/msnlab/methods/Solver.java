/**
 * 
 */
package br.edu.ufcg.msnlab.methods;

import java.util.List;

import br.edu.ufcg.msnlab.util.Coeficiente;



/**
 * TODO
 * 
 * @author Hugo Marques
 *
 */
public interface Solver<T> {
	
	/**
	 * TODO
	 * @param funcion
	 * @param tolerance
	 * @return
	 */
	public List<T> solve(double[][] coeficientes,double[] estimativas, List<T> termos, double aprox, int iteracoesMax);

}
