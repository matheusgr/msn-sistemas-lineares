package br.edu.ufcg.msnlab.facade;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.methods.Solver;
import br.edu.ufcg.msnlab.methods.GaussJordan.GaussJordan;
import br.edu.ufcg.msnlab.methods.GaussMethod.GaussMethod;
import br.edu.ufcg.msnlab.methods.GaussSeidel.GaussSeidelSolverImpl;
import br.edu.ufcg.msnlab.methods.choleskyQR.interfaces.CholeskySolverImpl;
import br.edu.ufcg.msnlab.methods.choleskyQR.interfaces.QRSolverImpl;
import br.edu.ufcg.msnlab.methods.jacobi.JacobiSolverImpl;
import br.edu.ufcg.msnlab.methods.lu.LU.LU.LU;
import br.edu.ufcg.msnlab.methods.svd.SVD;
import br.edu.ufcg.msnlab.util.Checker;
import br.edu.ufcg.msnlab.util.Config;
import br.edu.ufcg.msnlab.util.ParsedSystem;
import br.edu.ufcg.msnlab.util.Parser;

public class Controller {

	public String hasSolution(double[][] coef, double[] termos) {
		Checker c = new Checker(coef, termos);
		return c.hasSolution();
	}	

	public Result resolve(double[][] coeficientes, double[] termos,
			double[] estimativas, double aprox, int iteracoesMax,
			String metodo, Config config) throws MSNException{
		//TODO FALTA O PESSOAL ME PASSAR AS CLASSES DOS METODOS PARA FAZER A INSTANCIA E USAR O SOLVER.
		Solver method = null;
		Result r = null;
		if (metodo.equals(Methods.EliminacaoGauss)) {
			method = new GaussMethod();
			r = method.solve(coeficientes, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.EliminacaoGaussJordan)) {
			method = new GaussJordan();
			r = method.solve(coeficientes, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.DecomposicaoCholesky)) {
			method = new CholeskySolverImpl();
			r = method.solve(coeficientes, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.DecomposicaoLU)) {
			method = new LU();
			r = method.solve(coeficientes, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.DecomposicaoQR)) {
			method = new QRSolverImpl();
			r = method.solve(coeficientes, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.DecomposicaoSVD)) {
			method = new SVD();
			r = method.solve(coeficientes, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.GaussJacobi)) {
			method = new JacobiSolverImpl();
			r = method.solve(coeficientes, estimativas, termos, aprox, iteracoesMax, config);
		} else if (metodo.equals(Methods.GaussSeidel)) {
			method = new GaussSeidelSolverImpl();
			r = method.solve(coeficientes, estimativas, termos, aprox, iteracoesMax, config);
		} 
		return r;
	}

	public ParsedSystem parse(String sistemaEq) throws MSNException {
		return Parser.parse(sistemaEq);		
	}	

}
