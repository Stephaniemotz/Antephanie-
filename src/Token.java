package src;

/*
 * Task 1 - the Token class
 */


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
		// Task 1.1
		// update the following lines for creating
		// token with the corresponding type and and value
		this.type = type;
		this.value = value;
	}

	/**
	 * override the stString method
	 * 
	 * examples: "INT:123" for integer 123
	 * 			 "PLUS" for a plus sign (+)
	 *         
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