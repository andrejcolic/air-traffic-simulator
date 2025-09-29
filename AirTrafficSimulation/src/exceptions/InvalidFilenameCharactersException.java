package exceptions;

public class InvalidFilenameCharactersException extends Exception {
	
	public InvalidFilenameCharactersException() {
		super("A File name can not contain any of these characters: * ? \" < > |.");
	}

}
