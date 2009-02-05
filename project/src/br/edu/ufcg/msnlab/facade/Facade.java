package br.edu.ufcg.msnlab.facade;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.util.Config;
import br.edu.ufcg.msnlab.util.ParsedSystem;

/**
 * 
 * @author hugods e theoam
 *
 */
public class Facade {
	
	private Controller controlador;
	/**
	 * Construtor
	 */
	public Facade() {
		 controlador = new Controller();
	}
	
	
	/**
	 * Método que executa o processamento do sistema de equacoes lineares de acordo 	 
	 * @param coef Matriz de coeficientes do sistema linear.
	 * @param termos matriz de termos independentes do sistema linear.
	 * @param aprox Aproximacao desejada para o resultado.
	 * @param interacoes Numero máximo de interações para se atingir uma solução.
	 * @param metodo Metodo desejado pelo usuário para solução do sistema.
	 * @param estimativas Matriz de valores de estimativa usado pelos métodos iterativos.
	 * @param config Configurações para execuções dos métodos.
	 * @return Result contendo todas as iteracoes que levam ao resultado final.
	 */
	public Result resolve(double[][] coef, double[] termos, double aprox, int interacoes, 
			String metodo, double[] estimativas, Config config) throws MSNException{
		return controlador.resolve(coef, termos, estimativas, aprox, interacoes, metodo, config);
	}
	
	/**
	 * Método que verifica se o sistema possui solução pelos métodos apresentados.
	 * @param coef Matriz de coeficientes do sistema linear.
	 * @param termos matriz de termos independentes do sistema linear.
	 * @return String que apresenta se o sistema é heterogêneo, possível e determinada, possível e indeterminado ou sistema impossível.
	 */
	public String hasSolution(double[][] coef, double[] termos) {
		return controlador.hasSolution(coef, termos);
	}
	
	/**
	 * Método que dado um sistema em String converte esse sistema para um objeto intermediário que contém
	 * as matrizes de coeficientes, de termos e as incógnitas do sistema.
	 * @param sistemaEq Sistemas de equações em string.
	 * @return Objeto intermediário que contém o sistema em formato de matrizes.(para detalhes ver br.edu.ufcg.msnlab.util.SystemTypes).
	 */
	public ParsedSystem parse(String sistemaEq) throws MSNException {
		return controlador.parse(sistemaEq);
	}

}
