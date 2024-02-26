package src;


import java.util.ArrayList;
import java.util.List;

import static src.TokenType.*; // [static-import]

public class Scanner {

	private final String source; 	// the Antephanie source code
	private final List<Token> tokens = new ArrayList<>();	// the list of tokens

	// current scan location
	private int start = 0;		// staring position of current token
	private int current = 0;	// position of current character

	/**
	 * Scanner Constructor
	 * @param source - the Antephanie source code
	 * */
	public Scanner(String source) {
		this.source = source;
	}
	
	/**
	 * @return current character or '\0'
	 * */
	private char currentChar() {
		if (isAtEnd())
			return '\0';
		
		return source.charAt(current);
	}
	
	
	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	/**
	 * @return next Character or '\0'. This method does NOT update the position of the character 
	 * */
	private char nextChar() {
		
		if (isAtEnd())
			return '\0';
		
		return source.charAt(current++);
	}
	
	
	
	/** 
	 * Check if a Character is a Digit
	 * @param c - a character
	 * @return true if c is a digit, false otherwise
	 **/
	private boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * Advance to next Character. This method updates the position of the character 
	 * @return current character, Increase the index of current character
	 * 
	 */
	private char next() {
		char currentChar = currentChar();
		
		if (!isAtEnd()) {
			current++;
		}
		return currentChar;
	}

	
	/**
	 * Add a Token to the tokens list
	 * 
	 * @param type - token type note that token value is null
	 */
	private void addToken(TokenType type) {
		addToken(type, null);
	}

	
	/**
	 * Add a Token to the tokens list
	 * 
	 * @param type  - token type
	 * @param value - token value
	 */
	private void addToken(TokenType type, Object value) {
		tokens.add(new Token(type, value));	
		
	}


	/**
	 * Generate a list of tokens
	 * @return a list of tokens
	 * */
	List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}
		

		// End of the script - add an EOF token
		addToken(EOF);
		return tokens;
	}

	/**
	 * Scan a Token.
	 * Use the addToken() method to 
	 * add a token to the tokens list
	 */

	 private void scanToken() {
		char c = next();

		switch (c) {
			//Delimeters
			case '(':
				addToken(LPAREN);
				break;
			case ')':
				addToken(RPAREN);
				break;
			case '{':
				addToken(LBRACE);
				break;
			case '}':
				addToken(RBRACE);
				break;
			case ',':
				addToken(COMMA);
				break;
			case ';':
				addToken(SEMI);
				break;
			case ':':
				addToken(COLON);
				break;
			//case '.':
				//addToken(PERIOD); //Periodic???
				//.break;

			//Period
			case 'P':
				if (match('e') && match('r') && match('i') && match('o') && match('d') && match('i') && match('c') ) {
                	addToken(PERIOD); 
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error("Unexpected Token. Try Again.");
            	}
            	break;

			//Start COMMENTS
			case '#':
				if (match('H') && match('2') && match('0') ) {
                	addToken(STARTCOMMENT); 
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error("Unexpected Token. Try Again.");
            	}
            	break;

			//End COMMENTS
			case 'H':
				if (match('2') && match('0') && match('#') ) {
                	addToken(ENDCOMMENT); 
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error("Unexpected Token. Try Again.");
            	}
            	break;

			//HANDLE ALL THE RIGHT BRACKETS
			case '-':
				if (match('-') && match('>')) {
                	addToken(RBRACKET); //should handle --> as right bracket
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error("Unexpected Token. Try Again.");
            	}
            	break;

			//HANDLE ALL THE LEFT BRACKETS
			case '<':
				if (match('-') && match('-')) {
					addToken(LBRACKET); //should handle <-- as left bracket 
				} else {
					// Handle as an unknown token or report an error
					Antephanie.error("Unexpected Token. Try Again.-");
				}
				break;
				
			//Time to handle all the square brackets (yay)
			case '[':
				SquareBracketContent();
				break;
			
			case ' ': //Empty 
			case '\r':
			case '\t':
				break; // Ignore ' ', '\r', and '\t'
			default:
				// digit-start
				if (isDigit(c)) {
					number();
				} else {
					// Handle as an unknown token or report an error
					Antephanie.error("Unexpected Chemical Reaction. Try Again.");
				}
				break;
		}
	}

	//match and peek methods used for brackets 
	private char peek() {
		if (current < source.length()) {
			return source.charAt(current);
		}
		return '\0';
	}
	private boolean match(char expected) {
		if (peek() == expected) {
			current++;
			return true;
		}
		return false;
	}
	
	private void SquareBracketContent() {
		// Save the starting position of the content inside '['
		int startBracketContent = current;
	
		// Consume characters until a ']' is encountered
		while (!isAtEnd() && currentChar() != ']') {
			next();
		}
	
		// Check if ']' was found
		if (isAtEnd()) {
			Antephanie.error("Unexpected Chemical Reaction. Try Again.");
			return;
		}
	
		// Get the content inside '[' and ']'
		String content = source.substring(startBracketContent, current);
	
		// Process the content and add the appropriate token
		processSquareBracket(content);
	}
	
	private void processSquareBracket(String content) {
		// Example: Check for specific content inside '[' and ']'

		//Operators
		if (content.equals("P")) {
			addToken(PLUS);
		} else if (content.equals("S")) {
			addToken(MINUS);
		} else if (content.equals("Ts")) {
			addToken(MULT);
		} else if (content.equals("Dy")) {
			addToken(DIV);
		} else if (content.equals("Mo")) {
			addToken(MOD);
		} else if (content.equals("O")) {
			addToken(OR);
		} else if (content.equals("Am")) {
			addToken(AND);
		} else if (content.equals("No")) {
			addToken(NOT);
		} else if (content.equals("Lr")) {
			addToken(LOR);
		} else if (content.equals("Al")) {
			addToken(LAND);
		} else if (content.equals("Lu")) {
			addToken(LNOT);
		} else if (content.equals("La")) {
			addToken(LESSTHAN);
		} else if (content.equals("Ga")) {
			addToken(GREATERTHAN);
		} else if (content.equals("Li")) {
			addToken(LESSEQ);
		} else if (content.equals("Ge")) {
			addToken(GREATEQ);
		} else if (content.equals("Eu")) {
			addToken(EQUAL);
		} else if (content.equals("Ne")) {
			addToken(NOTEQUAL);

		//Assigment Operators
		} else if (content.equals("Te")) {
			addToken(TIMESEQUAL);
		} else if (content.equals("Ds")) {
			addToken(DIVEQUAL);
		} else if (content.equals("Pa")) {
			addToken(PLUSEQUAL);
		} else if (content.equals("Mn")) {
			addToken(MINUSEQUAL);

		//Increment/Decrement 
		} else if (content.equals("In")) {
			addToken(INCREMENT);
		} else if (content.equals("Md")) {
			addToken(DECREMENT);

		}else {
			// Handle as an unknown token or report an error
			Antephanie.error("Unexpected Chemical Reaction: " + content);
		}
	
		// Consume the ']'
		next();
	}

	/**
	 * Scan a Number Token
	 */
		private void number() {
		    boolean hasDot = false;

		    while (isDigit(currentChar())) {
		        next();
		    }

		    // Look for a fractional part.
		    if (currentChar() == '.') {
		        // Consume the dot
		        hasDot = true;
		        next();

		        // Consume the digits after the dot
		        while (isDigit(currentChar())) {
		            next();
		        }
		    }

		    // Create a NUMBER token
    		String lexeme = source.substring(start, current);
    			try {
       				addToken(DOUBLE, Double.parseDouble(lexeme));
    			} catch (NumberFormatException e) {
        			// Handle the error for invalid number format
        			Antephanie.error("Invalid number format: " + lexeme);
    		}
		}
	}
		
	
