package exceptions;

public class FilenameEmptyException extends Exception {
	
	public FilenameEmptyException() {
		super("File name missing.");
	}
}
