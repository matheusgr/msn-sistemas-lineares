package br.edu.ufcg.msnlab.util;

import Jama.Matrix;
import br.edu.ufcg.msnlab.exceptions.MSNException;

public class Checker {
	
	private double[][] coeficientes;
	private double[] termos;
	private static double PRECISION = 0.0001;

	public Checker(double[][] coef, double[] t) {
		this.coeficientes = coef;
		this.termos = t;		
	}
	
	public static void setPrecision(double p){
		Checker.PRECISION = p;
	}
	
	public String hasSolution() {
		if(!this.isHomogeneous()) {
			return SystemTypes.HETEROGENEO;
		} else {
			if (Math.abs(calcDet(this.coeficientes)) <= Checker.PRECISION) {				
				for (int i=0;i<coeficientes.length;i++) {
					double[][] matriz = trocaCoef(i);
					if (Math.abs(calcDet(matriz)) >= Checker.PRECISION) {
						return SystemTypes.IMPOSSIVEL;
					}
				}
			} else {
				return SystemTypes.POSSIVELDET;
			}
		}
		return SystemTypes.POSSIVELINDET;
	}
	
	private double calcDet(double[][] mat) {
		Matrix m = new Matrix(mat);
		double det = m.det(); 
		return det;
	}

	private boolean isHomogeneous() {
		return coeficientes[0].length == coeficientes.length;			
	}

	private double[][] trocaCoef(int col) {		
		for(int i=0;i<coeficientes.length;i++) {
			coeficientes[i][col] = termos[i];
		}
		return coeficientes;
	}
	
//	public static void main(String[] args) {
//		trocaCoef(1);
//	}
	
}
