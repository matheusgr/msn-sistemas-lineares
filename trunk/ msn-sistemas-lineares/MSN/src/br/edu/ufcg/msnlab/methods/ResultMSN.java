package br.edu.ufcg.msnlab.methods;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Hugo Marques e Théo Alves
 *
 */
public class ResultMSN implements Result<double[][]> {
	
	/**
	 * Lista de resultados das iterações dos métodos.
	 */
	private List<double[][]> resultados;
	
	/**
	 * Construtor de um novo result sem lista prévia de resultados.
	 */
	public ResultMSN() {
		this.resultados = new ArrayList<double[][]>();
	}
	
	/**
	 * Construtor de um result a partir de uma lista de resultados já existente.
	 * @param r Lista de resultados existente.
	 */
	public ResultMSN(List<double[][]> r) {
		this.resultados = r;
	}
		
	@Override
	public double[][] getResult(int var) {
		return this.resultados.get(var);
	}

	@Override
	public List<double[][]> getValues() {		
		return this.resultados;
	}

	@Override
	public void addResult(Object valor) {
		this.resultados.add((double[][])valor);
		
	}

}
