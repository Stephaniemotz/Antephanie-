package src;

public class Token {

	final TokenType type; 	// token type
	final Object value; 	// token value
	final String lexeme; 	// token value as a String
	final int line;			// token location

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
	 * 
	 * example: for a token with type INT and value 123,
	 * 			the corresponding token_str is INT:123 
	 * */
	public String toString() {
		String token_str = "";
		
		if(this.value != null) 
			token_str += type + ":" + value;
		else 
			token_str += type;
		
		return token_str;
	}

}

