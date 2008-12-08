package br.edu.ufcg.msnlab.util;

public class ParsedSystem {
	
	private double[][] coeficientes;
	private double[] termos;
	private String[] incognitas;
	
	public ParsedSystem(double[][] c, double[] t, String[] i) {
		this.coeficientes = c;
		this.termos = t;
		this.incognitas = i;
	}
	
	public double[][] getCoeficientes() {
		return coeficientes;
	}
	public void setCoeficientes(double[][] coeficientes) {
		this.coeficientes = coeficientes;
	}
	public double[] getTermos() {
		return termos;
	}
	public void setTermos(double[] termos) {
		this.termos = termos;
	}
	public String[] getIncognitas() {
		return incognitas;
	}
	public void setIncognitas(String[] incognitas) {
		this.incognitas = incognitas;
	}
}
