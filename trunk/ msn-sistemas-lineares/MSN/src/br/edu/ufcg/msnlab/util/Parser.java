package br.edu.ufcg.msnlab.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ufcg.msnlab.exceptions.MSNException;

/**
 * Classe de transforma uma String descrevendo um sistema de equações em várias linhas
 * em um objeto {@link ParsedSystem} que é uma abstração de coeficientes variaveis e 
 * termos útil para manipulação e cálculo do sistema linear.
 * 
 * Cada linha do sistema deve ter a forma: 
 * (('+'|'-')? real ('*')? (alpha)+) (('+'|'-') real ('*')? )* '=' ('+'|'-')? real
 * onde real: d+(sep d+)?
 * alpha: [a-zA-Z]
 * d: [0-9]
 * sep: o separador de casas decimais do sistema (iealmente '.' ou ',')
 * @author Hugo Marques e Théo Alves
 *
 */
public class Parser {
	
	/**
	 * Método de suporte que cria um mapa chaveado pela variável do sistema de euqações
	 * e a posição (coluna) que os coeficientes dela terão no {@link ParsedSystem} gerado.
	 * @param vars O conjunto de variáveis processados no sistema de equações
	 * @return Um mapa com as variaveis do sistema como chave e a coluna ocupada na matriz de
	 * coeficientes do {@link ParsedSystem}.
	 */
	private static Map<String, Integer> varPosMap(Set<String> vars) {
		//criando um mapa de posições das variaveis em cada linha
		Map<String, Integer> pos = new HashMap<String, Integer>();
		int i = 0;
		for(String k : vars) {
			pos.put(k, i++);
		}
		return pos; 
	}
	
	/**
	 * Método que trata uma lista de Strings para a formação de {@link ParsedSystem}
	 * @param equations a lista de Strings onde cada elemento representa uma linha do sistema 
	 * linear sob avaliação.
	 * conforme tratado pelo método de parser.
	 * @param decimalFormater o caractere de separação da descrição de casas decimais da equação
	 * @return Retorna um {@link ParsedSystem} com descrição dos coeficientes, variaveis e termos
	 * @throws MSNException Caso alguma {@link String} nao case com o padrão de formação de uma linha 
	 * do sistema Será lançada uma exceção.
	 */
	public static ParsedSystem parse(List<String> equations, char decimalFormater) throws MSNException {
		List<ParsedLine> lines = new ArrayList<ParsedLine>();
		Set<String> vars = new HashSet<String>();
		for(String line : equations) {
			ParsedLine eq = new ParsedLine(line);
			vars.addAll(eq.variaveis());
			lines.add(eq);
		}
		//TODO Iniciar essa matriz?
		double[][] coeficientes = new double[lines.size()][vars.size()];
		
		double[] termos = new double[lines.size()];
		
		String[] variaveis = vars.toArray(new String[vars.size()]);
		Map<String, Integer> pos = varPosMap(vars);
		
		for(int i = 0; i < lines.size(); i++) {
			termos[i] = lines.get(i).getTermo();
			Map<String, Double> tmp = lines.get(i).getIncognitas();
			for(String k : tmp.keySet()) {
				coeficientes[i][pos.get(k)] = tmp.get(k);
			}
		}
		System.out.println(vars);
		ParsedSystem out = new ParsedSystem(coeficientes, termos, variaveis);
		return out;
	}
	
	/**
	 * Método que transforma uma lista de {@link String} num {@link ParsedSystem}
	 * @param equations A lista de Strings onde cada elemento representa uma linha do sistema 
	 * linear sob avaliação.
	 * @return Retorna um {@link ParsedSystem} com descrição dos coeficientes, variaveis e termos
	 * conforme tratado pelo método de parser.
	 * @throws MSNException Caso alguma {@link String} nao case com o padrão de formação de uma linha 
	 * do sistema Será lançada uma exceção.
	 */
	public static ParsedSystem parse(List<String> equations) throws MSNException {
		return parse(equations, '.');
	}
	
	/**
	 * Método que transforma uma {@link String} de várias linhas num {@link ParsedSystem}
	 * @param equations A lista de Strings onde cada elemento representa uma linha do sistema 
	 * linear sob avaliação.
	 * @return Retorna um {@link ParsedSystem} com descrição dos coeficientes, variaveis e termos
	 * conforme tratado pelo método de parser.
	 * @throws MSNException Caso alguma {@link String} nao case com o padrão de formação de uma linha 
	 * do sistema Será lançada uma exceção.
	 */
	public static ParsedSystem parse(String equations) throws MSNException {
		Scanner sc = new Scanner(equations);
		List<String> lines = new ArrayList<String>(); 
		while(sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		return parse(lines);
	}
	
	//TODO acescentar um toString ao ParsedSystem
	
	@Deprecated
	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringBuffer buffer = new StringBuffer();
		String line;
		try {
			while((line = in.readLine()) != null) {
				if (line.trim().equals("#")) {
					ParsedSystem ps = Parser.parse(buffer.toString());
					buffer = new StringBuffer();
				} else {
					buffer.append(line+"\n");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MSNException m) {
			m.printStackTrace();
		}
		
	}
	
}

/**
 * Classe utilitária que faz o parser de uma linha de um sistema de equações lineares.
 * 
 * Cada String passada deve ter a forma descrita em {@link Parser}.
 * @author Hugo Marques e Théo Alves
 *
 */
class ParsedLine implements Comparable<ParsedLine>{
	
	/**
	 * constantes que descrevem a expressão regular de formação da linha do sistema. 
	 */
	public final String sep;// = "\\" + new DecimalFormatSymbols().getDecimalSeparator();
	public final String sinal;// = "(\\+|-)";
	public final String real;// = "\\d+("+sep+"\\d+)?";
	public final String head;// = sinal+"?" + "("+real+")?" + "(\\p{Alpha})+";
	public final String rest;// = sinal +"(" + real +")?" + "(\\p{Alpha})+";
	public final String exp;// = head + "(" + rest + ")*=" + sinal+"?"+real;
	
	/**
	 * Objetos que modelam a linha do sistema.
	 */
	private double termo;
	private Map<String, Double> incognitas;
	private Set<String> variaveis;
	
	/**
	 * Construtor privado que faz a inicialização das constantes baseado no separador 
	 * de casas decimais definido
	 * @param decimalSeparator O separador de casas decimais (idelamente '.' ou ','). 
	 */
	private ParsedLine(char decimalSeparator) {
		//TODO Na verdade ele devia receber todo um locale para 
		//tratar da localização (separados de milhar, separador decimal, etc...)
		//para tratar algo como: ((+|-)? real (\\p{Alpha})+) ((+|-) real (\\p{Alpha})+)* = (+|-)? real
		this.sep = "\\" + decimalSeparator;
		this.sinal = "(\\+|-)";
		this.real = "\\d+("+sep+"\\d+)?";
		this.head = sinal+"?" + "("+real+")?" + "(\\p{Alpha})+";
		this.rest = sinal +"(" + real +")?" + "(\\p{Alpha})+";
		this.exp = head + "(" + rest + ")*=" + sinal+"?"+real;
	}
	
	/**
	 * Construtor 
	 * @param line A {@link String} que descreve a linha do sistema.
	 * @param decimalSeparator O caractere separador de casas decimais (idelamente '.' ou ',').
	 * @throws MSNException em caso de expressao mal descrita. Vide {@link Parser}.
	 */
	public ParsedLine(String line, char decimalSeparator) throws MSNException {
		//TODO Na verdade ele devia receber todo um locale para 
		//tratar da localização (separados de milhar, separador decimal, etc...)
		this(decimalSeparator);
		if(Pattern.matches(this.exp, line)) {
			throw new MSNException("expressao mal formada: " + line);
		}
		this.incognitas = new HashMap<String, Double>();
		this.parse(line);
	}
	
	/**
	 * Cosntutor utilitário que utiliza '.' como separador padrão.
	 * @param line A {@link String} que descreve a linha do sistema.
	 * @throws MSNException em caso de expressao mal descrita. Vide {@link Parser}.
	 */
	public ParsedLine(String line) throws MSNException {
		this(line, '.'); //TODO puxar pelo separador adequado ao Locale...
	}
	
	/**
	 * Método que extrai da {@link String} as caracteristicas da linha do sistema de equações.
	 * @param line A {@link String} que descreve a linah do sistema.
	 */
	private void parse(String line) {
		//eliminando os espaços
		String tmp = line.trim().replaceAll("\\s", "");
		//quebrando em torno da igualdade 
		String[] slices = tmp.split("=");
		//selecionando o termo daquela linha
		this.termo = Double.parseDouble(slices[1]);
		
		//quebrando os tokens (o sinal deve ser mantido)
		Pattern p = Pattern.compile(this.head);//("[\\+|\\-|=]?\\p{Alpha}*");
		Matcher m = p.matcher(tmp);
		while(m.find()) {
			String t = m.group();
			String fator = t.replaceAll("\\p{Alpha}", "");
			boolean isOne = fator.equals("+") || fator.equals("-") || fator.equals("");
			String normal =  isOne ? t.replaceAll("\\p{Alpha}", "")  + "1" + 
					t.replaceAll("\\p{Punct}", ""): t;
			String incognita = t.replaceAll(this.sinal+"?"+"("+this.real+")?", "");
			double coef = Double.parseDouble(normal.replaceAll("\\p{Alpha}", ""));
			
			//TODO permitindo essa soma de elementos na mesma linha ele pode zerar o que ferra a linha... 
			double lastCoef = this.incognitas.get(incognita) == null ? 0 : this.incognitas.get(incognita);
			this.incognitas.put(incognita, coef + lastCoef);
		}
		//retirando os coeficientes zerados
		for(String key: new HashSet<String>(this.incognitas.keySet())) {
			if(this.incognitas.get(key) == 0) {
				this.incognitas.remove(key);
			}
		}
		this.variaveis = this.incognitas.keySet();
	}
	
	public double getTermo() {
		return termo;
	}
	
	public Map<String, Double> getIncognitas() {
		return incognitas;
	}
	
	public Set<String> variaveis() {
		return this.variaveis;
	}
	
	/**
	 * Quantidade de variáveis corretamente descritas na linha
	 * @return A quantidade de variáveis que ao processar a {@link String} não teve
	 * fator zerado e por isso se manteve como variável da linha.
	 */
	public int num_variaveis() {
		return this.incognitas.size();
	}

	@Override
	public int compareTo(ParsedLine o) {
		return this.num_variaveis() - o.num_variaveis();
	}
	
}