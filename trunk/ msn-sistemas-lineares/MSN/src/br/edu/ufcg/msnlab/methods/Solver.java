/**
 * 
 */
package br.edu.ufcg.msnlab.methods;

import java.util.List;



/**
 * TODO
 * 
 * @author Hugo Marques
 *
 */
public interface Solver<T> {
	
	/**
	 *	 
	 * @param coeficientes Matriz de coeficientes do sistema.
	 * @param estimativas Matriz de estimativas determinadas pelo usuário (opcional)
	 * @param termos Matriz de termos independentes do sistema linear. 
	 * @param aprox Aproximação desejada pela usuário.
	 * @param iteracoesMax Número de iterações máximo que o método vai executar.
	 * @return Result contendo um mapa de valor(es) (incógnita, valor).
	 */
	public List<Result> solve(double[][] coeficientes, double[] estimativas, double[] termos, double aprox, int iteracoesMax);
	
	/**
	 * 
	 * @param coeficientes Matriz de coeficientes do sistema.	  
	 * @param termos Matriz de termos independentes do sistema linear. 
	 * @param aprox Aproximação desejada pela usuário.
	 * @param iteracoesMax Número de iterações máximo que o método vai executar.
	 * @return Result contendo um mapa de valor(es) (incógnita, valor).
	 */
	public List<Result> solve(double[][] coeficientes, double[] termos, double aprox, int iteracoesMax);

}
