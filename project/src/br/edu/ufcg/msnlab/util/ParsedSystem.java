package br.edu.ufcg.msnlab.util;

import java.util.Arrays;

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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < this.getCoeficientes()[0].length; i++) {
			buffer.append(this.getIncognitas()[i]);
			buffer.append(":");
			for(int j = 0; j < this.getCoeficientes().length; j++) {
				buffer.append(this.getCoeficientes()[j][i]);
				buffer.append(", ");
			}
			buffer.append(System.getProperty("line.separator"));
		}
		buffer.append("termos: ");
		buffer.append(Arrays.toString(this.termos));
		return buffer.toString();
	}
}
