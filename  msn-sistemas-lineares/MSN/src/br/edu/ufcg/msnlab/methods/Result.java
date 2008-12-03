/**
 * 
 */
package br.edu.ufcg.msnlab.methods;
import java.util.List;


/** 
 * 
 * @author Hugo Marques
 * 
 */
public interface Result {
	
	/**
	 * TODO
	 * @return As soluções encontradas.
	 */
	public List getValues();
	
	/**
	 * Adiciona um resultado encontrado para uma incógnita.
	 * @param valor valor encontrado pelo método para a incógnita.
	 */
	public void addResult(Object valor);
	
	/**
	 * Retorna o valor encontrado pelo método para determinada incógnita.
	 * @param var Iteração que contém o resultado desejado. 
	 * @return valor Resultado para a iteração var.
	 */
	public Object getResult(int var);
	
}
