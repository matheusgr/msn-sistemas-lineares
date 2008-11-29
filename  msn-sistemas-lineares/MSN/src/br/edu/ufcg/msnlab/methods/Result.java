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
public interface Result<T> {
	
	/**
	 * TODO
	 * @return O valor das soluções encontradas.
	 */
	public List<T> getValues();
	
	/**
	 * Adiciona um resultado encontrado para uma incógnita.
	 * @param valor valor double encontrado pelo método para a incógnita.
	 */
	public void addResult(double valor);
	
	/**
	 * Retorna o valor encontrado pelo método para determinada incógnita.
	 * @param var Posição da variável desejada.
	 * @return valor double da incógnita.
	 */
	public double getResult(int var);
	
}
