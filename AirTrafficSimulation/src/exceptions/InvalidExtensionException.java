package exceptions;

public class InvalidExtensionException extends Exception {

	public InvalidExtensionException() {
		super("File must have .csv extension.");
	}
}
