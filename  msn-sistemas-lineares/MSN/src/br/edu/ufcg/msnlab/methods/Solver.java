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
	 * 
	 * @param variaveis Variaveis que compoem o sistema linear
	 * @param coeficientes Matriz de coeficientes do sistema.
	 * @param estimativas Matriz de estimativas determinadas pelo usuário (opcional)
	 * @param termos Matriz de termos independentes do sistema linear. 
	 * @param aprox Aproximação desejada pela usuário.
	 * @param iteracoesMax Número de iterações máximo que o método vai executar.
	 * @return Result contendo um mapa de valor(es) (incógnita, valor).
	 */
	public Result solve(String[] variaveis, double[][] coeficientes, double[] estimativas, List<T> termos, double aprox, int iteracoesMax);

}
