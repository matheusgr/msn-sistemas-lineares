package br.edu.ufcg.msnlab;

import br.edu.ufcg.msnlab.exceptions.MSNException;
import br.edu.ufcg.msnlab.methods.ResultMSN;
import br.edu.ufcg.msnlab.util.Config;
import br.ufcg.edu.msnlab.methods.jacobi.JacobiSolver;
import br.ufcg.edu.msnlab.methods.jacobi.JacobiSolverImpl;
import junit.framework.TestCase;

public class TestJacobiImpl extends TestCase{
	
	private JacobiSolver jacobi;
	double [][] coefficients;
	double [] estimates;
	double[] terms;
	double aproximation; 
	int numberIterations;
	
	
	protected void setUp() throws Exception{
		super.setUp();
		jacobi =  new JacobiSolverImpl();
		coefficients = null;
		estimates = null;
		terms = null;
		aproximation = 0; 
		numberIterations = 0;
	}
	
	public void testSystem1() throws MSNException{
		double[][] expectedValue = new double[][] {{2.03125},{1.0625}};
		
		assertEquals(new double[][] {{2.,1.},{1.,2.}}, new double[] {0.0,0.0}, new double []{5.0,4.0}, 0.001, 5, null, expectedValue);
	}
	
	public void testSystem2() throws MSNException{
		double[][] expectedValue = new double[][] {{1.9647222222222225},{-0.02557870370370363},{0.9667777777777778}};
		
		assertEquals(new double[][] {{3.,1.,1.},{1.,4.,2.},{0.,2.,5.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,4.0,5.0}, 0.05, 6, null, expectedValue);
	}
	
	public void testSystem3() throws MSNException{
		double[][] expectedValue = new double[][] {{2.0567592592592594},{0.08305555555555544},{1.0227777777777778}};
		
		assertEquals(new double[][] {{3.,1.,1.},{1.,4.,2.},{0.,2.,5.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,4.0,5.0}, 0.05, 5, null, expectedValue);
	}
	
	public void testSystem4() throws MSNException{
		double[][] expectedValue = new double[][] {{2.3333333333333335},{1.0},{1.0}};
		
		assertEquals(new double[][] {{3.,1.,1.},{1.,4.,2.},{0.,2.,5.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,4.0,5.0}, 0.05, 1, null, expectedValue);
	}
	
	public void testSystem5() throws MSNException{
		double[][] expectedValue = new double[][] {{1.666666666666667},{-0.08333333333333348},{0.6000}};
		
		assertEquals(new double[][] {{3.,1.,1.},{1.,4.,2.},{0.,2.,5.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,4.0,5.0}, 0.05, 2, null, expectedValue);
	}
	
	public void testSystem6() throws MSNException{
		double[][] expectedValue = new double[][] {{0.7},{-1.6},{0.6}};
		
		assertEquals(new double[][] {{10.,2.,3.},{1.,5.,1.},{2.,3.,10.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,-8.0,6.0}, 0.05, 1, null, expectedValue);
	}
	
	public void testSystem7() throws MSNException{
		double[][] expectedValue = new double[][] {{0.8400000000000001},{-1.86},{0.94}};
		
		assertEquals(new double[][] {{10.,2.,3.},{1.,5.,1.},{2.,3.,10.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,-8.0,6.0}, 0.05, 2, null, expectedValue);
	}
	
	public void testSystem8() throws MSNException{
		double[][] expectedValue = new double[][] {{0.79},{-1.9560000000000002},{0.99}};
		
		assertEquals(new double[][] {{10.,2.,3.},{1.,5.,1.},{2.,3.,10.}}, new double[] {0.0,0.0,0.0}, new double []{7.0,-8.0,6.0}, 0.05, 3, null, expectedValue);
	}
	
	public void testSystem9() throws MSNException{
		double[][] expectedValue = new double[][] {{1.25},{1.25}};
		
		assertEquals(new double[][] {{2.,-1.},{1.,2.}}, new double[] {0.0,0.0}, new double []{1.0,3.0}, 0.01, 2, null, expectedValue);
	}
	
	public void testSystem10() throws MSNException{
		double[][] expectedValue = new double[][] {{0.5},{1.5}};
		
		assertEquals(new double[][] {{2.,-1.},{1.,2.}}, new double[] {0.0,0.0}, new double []{1.0,3.0}, 0.01, 1, null, expectedValue);
	}
	
	public void testSystem11() throws MSNException{
		double[][] expectedValue = new double[][] {{1.125},{0.875}};
		
    	assertEquals(new double[][] {{2.,-1.},{1.,2.}}, new double[] {0.0,0.0}, new double []{1.0,3.0}, 0.01, 3, null, expectedValue);
	}
	
	public void testSystem12() throws MSNException{
		double[][] expectedValue = new double[][] {{0.9375},{0.9375}};
		
		assertEquals(new double[][] {{2.,-1.},{1.,2.}}, new double[] {0.0,0.0}, new double []{1.0,3.0}, 0.01, 4, null, expectedValue);
	}
	
	public void testSystem13() throws MSNException{
		double[][] expectedValue = new double[][] {{3.0},{-1.0}};
		
		assertEquals(new double[][] {{0.,-1.},{1.,0.}}, new double[] {0.0,0.0}, new double []{1.0,3.0}, 0.01, 1, null, expectedValue);
	}
	
	private void assertEquals(double[][] coeficientes, 
			double[] estimativas, double [] termos, double aprox, int iteracoesMax, Config config, double [][] expectedValue) throws MSNException{
		
		ResultMSN values = (ResultMSN) jacobi.solve(coeficientes, estimativas, termos, aprox, iteracoesMax, null);
		double[][] lastValue = values.getResult(iteracoesMax); 
		
		for (int i = 0; i < lastValue.length; i++) {	
			assertEquals(expectedValue[i][0], lastValue[i][0]);
		}
	}	
}
