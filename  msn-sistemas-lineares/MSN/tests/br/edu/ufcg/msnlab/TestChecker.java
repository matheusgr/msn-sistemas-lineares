package br.edu.ufcg.msnlab;

import br.edu.ufcg.msnlab.util.Checker;
import br.edu.ufcg.msnlab.util.SystemTypes;
import junit.framework.TestCase;

public class TestChecker extends TestCase {
	

	public void testHasSolution() {
		double[][] s = new double[2][2];
		s[0][0]=1;
		s[0][1]=2;
		s[1][0]=2;
		s[1][1]=-1;
		double[] t = new double[] {-1,8};
		Checker c = new Checker(s,t);
		try {
			assertEquals(c.hasSolution(),SystemTypes.POSSIVELDET);
		} catch (Exception e) {	}
		s[0][0]=4;
		s[0][1]=2;
		s[1][0]=8;
		s[1][1]=4;
		t = new double[] {100,200};
		c = new Checker(s,t);
		try {
			assertEquals(c.hasSolution(),SystemTypes.POSSIVELINDET);
		} catch (Exception e) {	}
		s[0][0]=1;
		s[0][1]=3;
		s[1][0]=1;
		s[1][1]=3;
		t = new double[] {4,5};
		c = new Checker(s,t);
		try {
			assertEquals(c.hasSolution(),SystemTypes.IMPOSSIVEL);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		double[][] s2 = new double[1][2];
		s[0][0]=1;
		s[0][1]=3;
		t = new double[]{4};
		c = new Checker(s2,t);
		try {
			assertEquals(c.hasSolution(), SystemTypes.HETEROGENEO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
