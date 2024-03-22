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
	private int line = 1;

	public Scanner(String source) {
		this.source = source;
	}
	

	List<Token> scanTokens() {
		while (!isAtEnd()) {

			start = current;
			scanToken();
		}

		// End of the script - add an EOF token
		addToken(EOF);
		return tokens;
	}


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

			//Period
			case 'P':
				if (match('e') && match('r') && match('i') && match('o') && match('d') && match('i') && match('c') ) {
                	addToken(PERIOD); 
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error(line, "Unexpected token, try again.");
            	}
            	break;

			//Start COMMENTS
			case '#':
				if (match('H') && match('2') && match('0') ) {
                	addToken(STARTCOMMENT); 
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error(line, "Unexpected token, try again.");
            	}
            	break;

			//End COMMENTS
			case 'H':
				if (match('2') && match('0') && match('#') ) {
                	addToken(ENDCOMMENT); 
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error(line, "Unexpected token, try again.");
            	}
            	break;

			//HANDLE ALL THE RIGHT BRACKETS
			/* case '-':
				if (match('-') && match('>')) {
                	addToken(RBRACKET); //should handle --> as right bracket
            	} else {
                	// Handle as an unknown token or report an error
                	Antephanie.error(line, "Unexpected token, try again.");
            	}
            	break;

			//HANDLE ALL THE LEFT BRACKETS
			case '<':
				if (match('-') && match('-')) {
					addToken(LBRACKET); //should handle <-- as left bracket 
				} else {
					// Handle as an unknown token or report an error
					Antephanie.error(line, "Unexpected token, try again.");
				}
				break;
			*/
				
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
					Antephanie.error(line, "Unexpected Chemical Reaction, try again.");
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
			Antephanie.error(line, "Unexpected Chemical Reaction, try again.");
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
			Antephanie.error(line, "Unexpected Chemical Reaction.");
		}
	
		// Consume the ']'
		next();
	}

/**
 * Scan a Number Token
 */
		private void number() {
			while (isDigit(currentChar())) {
				next();
			}
				
			// Look for a fractional part.
			if (currentChar() == '.' && isDigit(nextChar())) {
				// Consume the "."
				next();
	
				while (isDigit(currentChar()))
					next();
			}
			
			// Create a DOUBLE token
			String lexeme = source.substring(start, current);
			try {
				addToken(DOUBLE, Double.parseDouble(lexeme));
			} catch (NumberFormatException e) {
				// Handle the error for invalid number format
				//Antephanie.error("Invalid number format: " + lexeme);
				Antephanie.error(line, "Invalid number format!");
			
		}

	}

	private char currentChar() {
		if (isAtEnd())
			return '\0';
		
		return source.charAt(current);
	}
	
	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	private char nextChar() {
		
		if (isAtEnd())
			return '\0';
		
		return source.charAt(current++);
	}

	private boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		} else {
			return false;
		}
	}

	private char next() {
		char currentChar = currentChar();
		
		if (!isAtEnd()) {
			current++;
		}
		return currentChar;
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}


	private void addToken(TokenType type, Object value) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, value, text, line));
	}

}
		
	
