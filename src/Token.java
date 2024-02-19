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
	public String toString(int position) {
        if (value != null) {
            return type + ":" + value.toString() + " at position " + position;
        } else {
            return type.toString() + " at position " + position;
        }
    }
}