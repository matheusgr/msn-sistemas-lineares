package br.edu.ufcg.msnlab.methods.jacobi;

import java.util.List;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.util.Config;

public interface JacobiSolver extends Solver {

	/**
	 * 
	 * @param coeficientes Matriz de coeficientes do sistema.
	 * @param estimativas Matriz de estimativas determinadas pelo usuário (opcional)
	 * @param termos Matriz de termos independentes do sistema linear. 
	 * @param aprox Aproximação desejada pela usuário.
	 * @param iteracoesMax Número de iterações máximo que o método vai executar.
	 * @param config Parâmetros de configuração para cada método.
	 * @return Result uma lista de matrizes, cada matriz representando uma iteracao.
	 * @throws MSNException 
	 */
	public Result solve(double[][] coeficientes, double[] estimativas, double[] termos, double aprox, int iteracoesMax, Config config) throws MSNException;
	
	/**
	 * 
	 * @param coeficientes Matriz de coeficientes do sistema.	  
	 * @param termos Matriz de termos independentes do sistema linear. 
	 * @param aprox Aproximação desejada pela usuário.
	 * @param iteracoesMax Número de iterações máximo que o método vai executar.
	 * @param config Parâmetros de configuração para cada método.
	 * @return Result uma lista de matrizes, cada matriz representando uma iteracao.
	 * @throws MSNException 
	 */
	public Result solve(double[][] coeficientes, double[] termos, double aprox, int iteracoesMax, Config config) throws MSNException;
}
