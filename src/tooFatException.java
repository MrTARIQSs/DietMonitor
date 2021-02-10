
public class tooFatException extends Exception {
	public tooFatException() {
		super("Stop! this will make you super fat!");
	}

	public tooFatException(String message) {
		super(message);
	}
}
