package br.edu.ufcg.msnlab;

import java.util.Arrays;

import br.edu.ufcg.msnlab.gui.util.ExpressionParser;
import br.edu.ufcg.msnlab.gui.util.ExpressionParser.ParserResult;
import junit.framework.TestCase;

public class ExpressionParserTest extends TestCase {

	private void genericTestExp(String exp) {
		ExpressionParser ep = new ExpressionParser();
		ParserResult parse = ep.parse(exp);
		System.out.println(parse.getVariables());
		System.out.println(parse.getErrors());
		
		double[][] coef = parse.getCoef();
		System.out.println("Coefs:");
		for (int i = 0; i < coef.length; i++) {
			System.out.println(Arrays.toString(coef[i]));			
		}
		System.out.println("Terms:");
		System.out.println(Arrays.toString(parse.getTerms()));
	}
	
	public void dontTestExp() {
		genericTestExp("1x = 2");
		System.out.println("-------");
		genericTestExp("1.0x = 2.2");
		System.out.println("-------");
		genericTestExp("1x = 2.0000");
		System.out.println("-------");
		genericTestExp("-1x = 2");
		System.out.println("-------");
	}
	
	public void testExp() {
		genericTestExp("+ 2y + x = 2.0\n2x + 2y = 0");
		System.out.println("-------");
	}
	
}
