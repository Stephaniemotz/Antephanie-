package src;


 public class Token {

	final TokenType type; 	// token type
	final Object value; 	// token value
	final String lexeme;
	final int line;

	/**
	 * Token Constructor
	 * @param type	- the type of a token
	 * @param value	- the value of a token
	 * @param lexeme- token value as a String
	 * @param line	- token location
	 * */
	Token(TokenType type, Object value, String lexeme, int line) {
		this.type = type; 		// token type
		this.value = value; 	// token value
		this.lexeme = lexeme;
		this.line = line;
	}

	/**        
	 * @return the string representation of a token
	 */
	public String toString() {

		if (value != null) {
			return type + ":" + value.toString();
		} else {
			return type.toString();
		}

	}
}