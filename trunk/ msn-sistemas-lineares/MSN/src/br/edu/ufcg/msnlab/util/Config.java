package br.edu.ufcg.msnlab.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável pelos parâmetros de configuração dos métodos
 * @author Hugo Marques e Théo Alves
 *
 */
public class Config {
	
	private Map<String,Object> param;
	public static final String pivoteamento = "PIVOTEAMENTO";
	public static final String triangularizacao = "TRIANGULARIZACAO";
	
	/**
	 * Criação do config com seus valores default.
	 */
	public Config() {
		 this.param = new HashMap<String, Object>();
		 this.param.put(pivoteamento,new Boolean(false));
		 this.param.put(triangularizacao,new Boolean(false));		 
	}
	
	/**
	 * Método que altera uma configuração existente ou cria uma nova configuração para os métodos.
	 * @param param Nome do parâmetro de configuração.
	 * @param valor Valor da nova configuração.
	 */
	public void set(String param, Object valor) {
		this.param.put(param, valor);
	}
	
	/**
	 * Método que retorna um parâmetro existente nas configurações dos métodos.
	 * @param param Nome do parâmetro que se deseja obter o valor.
	 * @return valor do parâmetro.
	 */
	public Object get(String param) {
		return this.param.get(param);
	}

}
