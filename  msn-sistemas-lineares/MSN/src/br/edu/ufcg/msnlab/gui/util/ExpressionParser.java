package br.edu.ufcg.msnlab.gui.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ExpressionParser {

	public static class ParserError {
		
		private int line;
		
		private String message;

		public ParserError(int line, String message) {
			this.line = line;
			this.message = message;
		}
		
		public String toString() {
			return this.line + ": " + this.message;
		}
		
	}
	
	public static class ParserResult {
		private ArrayList<ParserError> errors;
		private double[][] coef;
		private double[] terms;
		private Set<String> variables;

		public ParserResult() {
			this.errors = new ArrayList<ParserError>();
		}

		public ArrayList<ParserError> getErrors() {
			return errors;
		}

		public double[][] getCoef() {
			return coef;
		}

		public double[] getTerms() {
			return terms;
		}

		public Set<String> getVariables() {
			return variables;
		}

	}
	
	public static ParserResult parse(String exp) {
		LineNumberReader lnr = new LineNumberReader(new StringReader(exp));
		TreeSet<String> variables = new TreeSet<String>();
		ArrayList<Map<String, Double>> equations = new ArrayList<Map<String, Double>>();
		ParserResult pr = new ParserResult();
		try {
			String readLine;
			while ((readLine = lnr.readLine()) != null) {
				System.out.println(readLine);
				char[] eq = readLine.toCharArray();
				HashMap<String, Double> curEquation = new HashMap<String, Double>();
				equations.add(curEquation);
				String curValue = "";
				for (int i = 0; i < eq.length; i++) {
					i = consumeWhiteSpace(eq, i);
					if (i == eq.length) {
						pr.errors.add(new ParserError(lnr.getLineNumber(), "Unexpected end of line."));
						continue;
					}
					i = readingTerm(eq, i, variables, equations, pr, lnr.getLineNumber());
					i = consumeWhiteSpace(eq, i);
					if (i == eq.length) {
						pr.errors.add(new ParserError(lnr.getLineNumber(), "Unexpected end of line."));
						continue;
					}
					if (Character.isLetterOrDigit(eq[i])) {
						pr.errors.add(new ParserError(lnr.getLineNumber(), "Expecting a new coef or equals symbol."));
						continue;
					}
					if (eq[i] == '=') {
						i++;
						i = consumeWhiteSpace(eq, i);
						for (; i < eq.length; i++) {
							curValue += eq[i];
						}
						try {
							curEquation.put("term", Double.parseDouble(curValue));
						} catch (NumberFormatException ne) {
							pr.errors.add(new ParserError(lnr.getLineNumber(), "Invalid form of term."));
						}
					}
				}
				if (!curEquation.containsKey("term")) {
					pr.errors.add(new ParserError(lnr.getLineNumber(), "Missing term."));
					continue;
				}
				if (curEquation.size() == 1) {
					pr.errors.add(new ParserError(lnr.getLineNumber(), "Expecting at least one variable."));
					continue;
				}
			}
		} catch (IOException e) {
			pr.errors.add(new ParserError(-1, "Unexpected parser error."));
		}
		double[][] coef = new double[equations.size()][variables.size()];
		double[] terms = new double[equations.size()];
		int i = 0;
		for (Map<String, Double> equation : equations) {
			int j = 0;
			for (String var : variables) {
				if (!(var == "term")) {
					if (equation.containsKey(var)) {
						coef[i][j] = equation.get(var);
					}
					j++;					
				}
			}
			if (equation.containsKey("term")) {
				terms[i] = equation.get("term");
			}
			i++;
		}
		pr.coef = coef;
		pr.terms = terms;
		pr.variables = variables;
		return pr;
	}

	private static int readingTerm(char[] eq, int i, Set<String> variables,
			ArrayList<Map<String, Double>> equations, ParserResult pr, int line) {
		String curValue = "";
		Map<String, Double> curEquation = equations.get(equations.size() - 1);
		if (eq[i] == '-') {
			curValue += '-';
		}
		if (eq[i] == '-' || eq[i] == '+') {
			i++;
		}
		
		i = consumeWhiteSpace(eq, i);
		if (i == eq.length) {
			pr.errors.add(new ParserError(line, "Unexpected end of line."));
			return i;
		}
		
		if (!Character.isLetterOrDigit(eq[i])) {
			pr.errors.add(new ParserError(line, "Expecting a digit or a variable."));
		}

		if (Character.isLetter(eq[i])) {
			curValue += "1";
		}
		while (i < eq.length && Character.isDigit(eq[i])) {
			curValue += eq[i++];
		}
		if (i == eq.length) {
			pr.errors.add(new ParserError(line, "Unexpected end of line."));
			return i;
		}
		if (eq[i] == '.') {
			i++;
			curValue += '.';
			if (Character.isDigit(eq[i])) {
				while (Character.isDigit(eq[i])) {
					curValue += eq[i++];
				}
			}
		}
		
		if (!Character.isLetter(eq[i])) {
			pr.errors.add(new ParserError(line, "Expecting a variable."));
		} else {
			String curVar = Character.toString(eq[i++]);
			variables.add(curVar);
			if (curEquation.containsKey(curVar)) {
				pr.errors.add(new ParserError(line, "Variable already used in another term."));
			} else {
				try {
					double value = Double.parseDouble(curValue);
					curEquation.put(curVar, value);
				} catch (NumberFormatException nfe) {
					pr.errors.add(new ParserError(line, "Invalid coef."));
				}
			}
		}
		
		return i;

	}

	private static int consumeWhiteSpace(char[] eq, int i) {
		while (i < eq.length && eq[i] == ' ') {
			i++;
		}
		return i;
	}
	
}
