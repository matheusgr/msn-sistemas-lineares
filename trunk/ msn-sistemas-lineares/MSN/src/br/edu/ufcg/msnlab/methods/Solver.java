/**
 * 
 */
package br.edu.ufcg.msnlab.methods;

import java.util.List;

import br.edu.ufcg.msnlab.util.Config;



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
	 * @param config Parâmetros de configuração para cada método.
	 * @return Result uma lista de matrizes, cada matriz representando uma iteracao.
	 */
	public Result<Object> solve(double[][] coeficientes, double[] estimativas, double[] termos, double aprox, int iteracoesMax, Config config);
	
	/**
	 * 
	 * @param coeficientes Matriz de coeficientes do sistema.	  
	 * @param termos Matriz de termos independentes do sistema linear. 
	 * @param aprox Aproximação desejada pela usuário.
	 * @param iteracoesMax Número de iterações máximo que o método vai executar.
	 * @param config Parâmetros de configuração para cada método.
	 * @return Result uma lista de matrizes, cada matriz representando uma iteracao.
	 */
	public Result<Object> solve(double[][] coeficientes, double[] termos, double aprox, int iteracoesMax, Config config);

}
