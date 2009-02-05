package br.edu.ufcg.msnlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import br.edu.ufcg.msnlab.facade.Facade;
import br.edu.ufcg.msnlab.methods.Methods;
import br.edu.ufcg.msnlab.methods.Result;
import br.edu.ufcg.msnlab.util.Checker;
import br.edu.ufcg.msnlab.util.Config;
import br.edu.ufcg.msnlab.util.SystemTypes;

public class MethodsAutomatedTest extends TestCase {

	private Facade facade;
	private PrintStream ps;

	public MethodsAutomatedTest() throws FileNotFoundException {
		this.ps = new PrintStream(new FileOutputStream(new File("docs/errors.txt")));		
	}
	
	public void setUp() {
		this.facade = new Facade();
	}

	public void test3Variables100OperationsLarge10Times() throws Exception {
		this.ps.println("3Variables100OperationsLarge10Times");
		for (int i = 0; i < 10; i++) {
			genericTestMethods(5, 10, 100);			
		}
	}
	
	public void test3Variables100Operations10Times() throws Exception {
		this.ps.println("3Variables100Operations10Times");
		for (int i = 0; i < 10; i++) {
			genericTestMethods(3, 1, 100);			
		}
	}
	
	public void test5Variables100Operations3Times() throws Exception {
		this.ps.println("5Variables100Operations10Times");
		for (int i = 0; i < 10; i++) {
			genericTestMethods(5, 1, 100);
		}
	}
	
	public void test1Variables10Operations10Times() throws Exception {
		this.ps.println("1Variables100Operations10Times");
		for (int i = 0; i < 10; i++) {
			genericTestMethods(1, 1, 10);
		}
	}
	
	public void test3Variables0Operations10Times() throws Exception {
		this.ps.println("3Variables0Operations10Times");
		for (int i = 0; i < 10; i++) {
			genericTestMethods(3, 1, 0);			
		}
	}
	
	public void testGaussAndJordan3Variables0Operations10Times() throws Exception {
		this.ps.println("GaussAndJordan3Variables0Operations10Times - Pivot and Triang Sup");
		int variables = 3;
		int factor = 1;
		int operations = 10;
		for (int i = 0; i < 10; i++) {
			SysEquations sys = createSystem(variables, factor, operations);			
			Config c = new Config();
			c.set(Config.pivoteamento, true);
			c.set(Config.triangularizacao, true);
			genericTestOneMethod(Methods.EliminacaoGauss, sys.clone(), variables, factor, operations, c);			
		}
		this.ps.println("GaussAndJordan3Variables0Operations10Times - Pivot and Triang Inf");
		for (int i = 0; i < 10; i++) {
			SysEquations sys = createSystem(variables, factor, operations);			
			Config c = new Config();
			c.set(Config.pivoteamento, true);
			c.set(Config.triangularizacao, false);
			genericTestOneMethod(Methods.EliminacaoGauss, sys.clone(), variables, factor, operations, c);			
		}
		this.ps.println("GaussAndJordan3Variables0Operations10Times - No Pivot and Triang Sup");
		for (int i = 0; i < 10; i++) {
			SysEquations sys = createSystem(variables, factor, operations);			
			Config c = new Config();
			c.set(Config.pivoteamento, false);
			c.set(Config.triangularizacao, true);
			genericTestOneMethod(Methods.EliminacaoGauss, sys.clone(), variables, factor, operations, c);			
		}
		this.ps.println("GaussAndJordan3Variables0Operations10Times - No Pivot and Triang Inf");
		for (int i = 0; i < 10; i++) {
			SysEquations sys = createSystem(variables, factor, operations);			
			Config c = new Config();
			c.set(Config.pivoteamento, false);
			c.set(Config.triangularizacao, false);
			genericTestOneMethod(Methods.EliminacaoGauss, sys.clone(), variables, factor, operations, c);			
		}
	}

	public void genericTestOneMethod(String method, SysEquations sys, int variables, int factor, int operations, Config c) throws Exception {
		try {
			ps.println("Testing... " + method);
			Result resolve = facade.resolve(sys.coef, sys.terms, 0.001, 1000, method, sys.estim, c);
			compare(method, resolve, sys, 0.001);
		} catch (Exception e) {
			ps.println("Exception - " + method);
			e.printStackTrace(ps);
		}
	}
	
	public void genericTestMethods(int variables, int factor, int operations) throws Exception {
		String[] methods = getMethods();
		
		SysEquations sys;
		SysEquations sys1 = createSystem(variables, factor, operations);
		
		Config c = new Config();
		
		for (int i = 0; i < methods.length; i++) {
			sys = sys1.clone();
			genericTestOneMethod(methods[i], sys, variables, factor, operations, c);
		}
	}

	private SysEquations createSystem(int variables, int factor, int operations) {
		Checker ch;
		SysEquations sys;
		do {
			sys = createEquation(variables, -10.0, 10.0, factor, operations);
			ch = new Checker(sys.coef, sys.terms);
		} while (! SystemTypes.POSSIVELDET.equals(ch.hasSolution()));
		return sys;
	}

	private void compare(String method, Result resolve, SysEquations sys, double est) {
		List<double[][]> values = resolve.getValues();
		double[][] m = values.get(values.size() - 1);
		boolean fail = false;
		for (int i = 0; i < sys.sol.length; i++) {
			if (Math.abs((sys.sol[i] - m[i][0])) > est) {
				fail = true;
			}
		}
		if (fail) {
			
			ps.println("Failure - " + method);
			ps.println("Coef:");
			for (int j = 0; j < m.length; j++) {
				ps.print(Arrays.toString(sys.coef[j]));
				ps.println(sys.terms[j]);
			}
			ps.println("Expected Solution:");
			ps.println(Arrays.toString(sys.sol));
			ps.println("Received Solution:");
			for (int j = 0; j < m.length; j++) {
				ps.println(Arrays.toString(m[j]));
			}
			ps.println();
		}
	}

	private class SysEquations {
		
		private double[][] coef;
		private double[] sol;
		private double[] terms;
		private double[] estim;

		public SysEquations(double[][] coef, double[] terms, double[] sol) {
			this.coef = coef;
			this.terms = terms;
			this.sol = sol;
			this.estim = new double[this.coef.length];
		}
		
		public SysEquations clone() {
			double[][] ccoef = new double[coef.length][coef[0].length];
			double[] csol = new double[sol.length];
			double[] cterms = new double[terms.length];
			double[] cestim = new double[estim.length];
			for (int i = 0; i < coef.length; i++) {
				System.arraycopy(coef[i], 0, ccoef[i], 0, coef[i].length);
			}
			System.arraycopy(sol, 0, csol, 0, csol.length);
			System.arraycopy(terms, 0, cterms, 0, cterms.length);
			System.arraycopy(estim, 0, cestim, 0, cestim.length);
			SysEquations clone = new SysEquations(ccoef, cterms, csol);
			clone.estim = cestim;
			return clone;
		}
		
	}
	
	
	private String[] getMethods() throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = Methods.class.getFields();
		String[] methods = new String[fields.length];
		for (int i = 0; i < methods.length; i++) {
			methods[i] = (String) fields[i].get(null);
		}
		return methods;
	}
	
	private SysEquations createEquation(int variables, double lower, double upper, double factor, int operations) {
		
		assert lower < upper;
		
		Random r = new Random();
		double[][] coef = new double[variables][variables];
		double[] terms = new double[variables];
		double[] solution = new double[variables];
		for (int i = 0; i < variables; i++) {
			double limit = upper - lower;
			coef[i][i] = 1;
			terms[i] = limit * r.nextDouble() + lower;
			solution[i] = terms[i];
		}
		for (int i = 0; i < operations; i++) {
			int line1 = r.nextInt(variables);
			switch (r.nextInt(3)) {
				case 0:
					double k = (factor * r.nextDouble());
					for (int j = 0; j < variables; j++) {
						coef[line1][j] *= k;
					}
					terms[line1] *= k;
					break;
				case 1:
					for (int j = 0; j < variables; j++) {
						coef[line1][j] *= -1;
					}
					terms[line1] *= -1;
					break;
				case 2:
					int line2 = r.nextInt(variables);
					for (int j = 0; j < variables; j++) {
						coef[line1][j] += coef[line2][j];
					}
					terms[line1] += terms[line2];
					break;
				default:
					throw new RuntimeException("Random should only cover 3 operations.");
			}
		}
		return new SysEquations(coef, terms, solution);
	}

	
}