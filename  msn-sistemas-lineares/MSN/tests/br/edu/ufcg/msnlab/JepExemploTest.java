package br.edu.ufcg.msnlab;

import junit.framework.TestCase;

import org.lsmp.djep.djep.DJep;
import org.lsmp.djep.sjep.Monomial;
import org.lsmp.djep.sjep.PConstant;
import org.lsmp.djep.sjep.PNodeI;
import org.lsmp.djep.sjep.Polynomial;
import org.lsmp.djep.sjep.PolynomialCreator;

public class JepExemploTest extends TestCase {

	public void testExemplo() throws Exception {
		DJep dj = new DJep();
		dj.addStandardConstants();
		dj.addStandardFunctions();
		dj.addComplex();
		dj.setAllowUndeclared(true);
		dj.setAllowAssignment(true);
		dj.setImplicitMul(true);
		dj.addStandardDiffRules();
		
		PolynomialCreator pc = new PolynomialCreator(dj);
		
		String cte = "2";
		PNodeI pCte = pc.createPoly(dj.parse(cte)).expand();
		assertEquals(PConstant.class, pCte.getClass());
		
		String monomial = "x + 2x";
		PNodeI pMono = pc.createPoly(dj.parse(monomial)).expand();
		assertEquals(Monomial.class, pMono.getClass());
		
		String polynomial = "x + 2x^2";
		PNodeI pPoly = pc.createPoly(dj.parse(polynomial)).expand();
		assertEquals(Polynomial.class, pPoly.getClass());
		
		String spolynomial = "sin(x) + 2x^2";
		PNodeI spPoly = pc.createPoly(dj.parse(spolynomial)).expand();
		assertEquals(Polynomial.class, spPoly.getClass());
		Polynomial p = ((Polynomial)spPoly);
		
		System.out.println(getTerms(p));
		
		//Object val = dj.evaluate(simp);
		//String s = dj.getPrintVisitor().formatValue(val);
		//System.out.println("Value:\t\t"+s);
		
	}
	
	public Object getTerms(Polynomial p) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		return Polynomial.class.getDeclaredField("terms").get(p);
	}
	
}
