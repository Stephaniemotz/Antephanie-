package src;


 public class Token {

	final TokenType type; // token type
	final Object value; // token value

	/**
	 * Token Constructor
	 * 
	 * @param type  - the type of a token
	 * @param value - the value of a token
	 */
	Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**        
	 * @return the string representation of a token
	 */
	public String toString() {
		// task 1.2
		// update token_str to 
		// the string representation of the token
		if (value != null) {
			return type + ":" + value.toString();
		} else {
			return type.toString();
		}

	}
}