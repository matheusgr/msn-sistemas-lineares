/**
 * 
 */
package br.edu.ufcg.msnlab.methods;

import java.util.Map;

/**
 * TODO
 * 
 * @author Hugo Marques

 */
public interface Result<K,T> {
	
	/**
	 * TODO
	 * @return A variável e o valor da mesma de acordo com a solução encontrada.
	 */
	public Map<K,T> getValues();
	
}
