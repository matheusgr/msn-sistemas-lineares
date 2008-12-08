package br.edu.ufcg.msnlab.exceptions;

public class MSNException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MSNException() {
		super();
	}
	
	
	public MSNException(String errorMessage){
		super(errorMessage);
	}
	
}
