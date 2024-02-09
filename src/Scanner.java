package src;


import java.util.ArrayList;
import java.util.List;

import static src.TokenType.*; // [static-import]


/*
 * Task 2 - the Scanner class
 */


public class Scanner {

	private final String source; 	// the Jay source code
	private final List<Token> tokens = new ArrayList<>();	// the list of tokens

	// current scan location
	private int start = 0;		// staring position of current token
	private int current = 0;	// position of current character

	/**
	 * Scanner Constructor
	 * @param source - the Jay source code
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
		
		// Task 2.1
		// update next line to return current character
		return source.charAt(current);
	}
	
	
	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	/**
	 * @return next Character or '\0'. This method does NOT update the position of the character 
	 * */
	private char nextChar() {
		// Task 2.2
		// update this method to return next Character or '\0'
		
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
		//Task 2.4
		// update this method
		//return current character, and then increase position by 1
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
		// Task 2.5
		// Add a Token to the tokens list
		tokens.add(new Token(type, value));	
		
	}


	/**
	 * Generate a list of tokens
	 * @return a list of tokens
	 * */
	List<Token> scanTokens() {
		// Task 2.6
		// invoke the scanToken method to
		// scan all the tokens
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
		// Task 2.7
		// update the switch for adding a token
		switch (c) {
		
		case '(':
			addToken(LEFT_PAREN);
			break;
		case ')':
			addToken(RIGHT_PAREN);
			break;
		case '-':
			addToken(MINUS);
			break;
		case '+':
			addToken(PLUS);
			break;
		case '*':
			addToken(MULT);
			break;
		case '/':
			addToken(DIV);
			break;
		case ' ':
		case '\r':
		case '\t':
			break; // Ignore ' ', '\r', and '\t'
		default:
			// digit-start
			if (isDigit(c)) {
				number();
			} else {
				// char-error
				Jay.error("Unexpected character.");
			}

			break;
		}
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
		    if (hasDot) {
		        addToken(DOUBLE, Double.parseDouble(lexeme));
		    } else {
		        addToken(INT, Integer.parseInt(lexeme));
		    }
		}
		
	
}
