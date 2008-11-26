/**
 * 
 */
package br.edu.ufcg.msnlab.methods;

import java.util.Map;

/** 
 * 
 * @author Hugo Marques
 * 
 */
public interface Result<K,T> {
	
	/**
	 * TODO
	 * @return A variável e o valor da mesma de acordo com a solução encontrada.
	 */
	public Map<K,T> getValues();
	
	/**
	 * Adiciona um resultado encontrado para uma incógnita.
	 * @param variavel incógnita que teve seu valor encontrado.
	 * @param valor valor double encontrado pelo método para a incógnita.
	 */
	public void addResult(String variavel, double valor);
	
	/**
	 * Retorna o valor encontrado pelo método para determinada incógnita.
	 * @param variavel Incógnita que contém o valor desejado.
	 * @return valor double da incógnita.
	 */
	public double getResult(String variavel);
	
}
