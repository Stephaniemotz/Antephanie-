package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static src.TokenType.*; // [static-import]

public class Scanner {

	private static final Map<String, TokenType> keywords;
	
	static {
		keywords = new HashMap<>();
		keywords.put("Independent", VAR);
	}

	
	private final String source; //
	private final List<Token> tokens = new ArrayList<>();

	// current scan location
	private int start = 0;
	private int current = 0;
	private int line = 1;

	public Scanner(String source) {
		this.source = source;
	}

	List<Token> scanTokens() {
		while (!isAtEnd()) {
			// We are at the beginning of the next token lexeme
			start = current;
			scanToken();
		}

		addToken(EOF);
		return tokens;
	}

	/**
	 * Scan Token
	 */
	private void scanToken() {
		char c = next();
		switch (c) {
		case '(':
			addToken(LEFT_PAREN);
			break;
		case ')':
			addToken(RIGHT_PAREN);
			break;
		case '{':
			addToken(LBRACKET);
			break;
		case '}':
			addToken(RBRACKET);
			break;
		case '~':
			addToken(LBRACE);
			break;
		case '*':
			addToken(RBRACE);
			break;
		case ',':
			addToken(COMMA);
			break;
		case '.':
			addToken(PERIOD);
			break;
		case ';':
			addToken(SEMI);
			break;
		case ':':
			addToken(COLON);
			break;
		case '-':
			addToken(MINUS);
			break;
		case '=':
			addToken(EQUAL);
			break;
		case '>':
			addToken(GREATERTHAN);
			break;
		case '<':
			addToken(LESSTHAN);
			break;
			
		//Handle Square Brackets
		case '[':
			SquareBracketContent();
			break;
			
		//Single Line Comments
		case '%':
			// Handle single-line comment: skip characters until newline
            while (currentChar() != '\n' && !isAtEnd()) {
                next();
            }
            break;
            
        case '#':
        	if (match('P') && match('E') && match('R') &&match('I') && match('O') 
					&& match('D') && match('I') && match('C') ) {
                // Handle block comment start
                skipBlockComment();
            } else {
                // Treat '#' as a regular token
                addToken(TokenType.SINGLECOMMENT);
            }
            break;
         
		
				
		case ' ':
		case '\r':
		case '\t':
			break; // Ignore ' ', '\r', and '\t'
		case '\n':
			line++;
			break;
		default:

			// digit-start
			if (isDigit(c)) {
				// Number token
				number();
			} 
			else if (isAlpha(c)){
				identifier();
			}
			else {
				// char-error
				Antephanie.error(line, "Unexpected character.");
			}
			break;
		}
	}
	
	private void skipBlockComment() {
	    // Skip characters until '#' is encountered
	    while (currentChar() != '#' && !isAtEnd()) {
	        next();
	    }
	    // Skip the closing '#'
	    next();
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
			
			//Arithmatic  
			if (content.equals("P")) {
				addToken(PLUS);
			} else if (content.equals("Ts")) {
				addToken(MULT);
			} else if (content.equals("Dy")) {
				addToken(DIV);
			} else if (content.equals("Mo")) {
				addToken(MOD);
			} else if (content.equals("In")) {
				addToken(INCREMENT);
			} else if (content.equals("Md")) {
				addToken(DECREMENT);
				
				//Logical 
			} else if (content.equals("O")) {
				addToken(OR);
			} else if (content.equals("Am")) {
				addToken(AND);
			} else if (content.equals("No")) {
				addToken(NOT);
				
		

			//Comparison Operations
			} else if (content.equals("==")) {
				addToken(EQUALTO);
			} else if (content.equals("No=")) {
				addToken(NOTEQUAL);
			} else if (content.equals(">=")) {
				addToken(GREATEQ);
			} else if (content.equals("<=")) {
				addToken(LESSEQ);


				
			//Assignment 
			} else if (content.equals("Ts=")) {
				addToken(TIMESEQUAL);
			} else if (content.equals("Dy=")) {
				addToken(DIVEQUAL);
			} else if (content.equals("P=")) {
				addToken(PLUSEQUAL);
			} else if (content.equals("-=")) {
				addToken(MINUSEQUAL);

				
			}else {
				Antephanie.error(line, "Unexpected Chemical Reaction.");
			}
		
			next();
		}
		
	/* Scan a identifier/keyword Token */
	private void identifier() {
	    while ( isAlphaNumeric(currentChar()) ) {
	    	next();
	    }
	    
	    String text = source.substring(start, current);
	    TokenType type = keywords.get(text);
	    
	    // TODO 2.2: finish this method to handle 
	    // the IDENTIFIER and keyword token (VAR)
	    
	    if (type != null) { 
	    	addToken(TokenType.VAR);
	    } else {
	    	addToken(TokenType.ID);  
	    
	    }
	    
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
		
		String lexeme = source.substring(start, current);
		addToken(NUMBER, Double.parseDouble(lexeme));
		
	}


	/**
	 * 
	 * 
	 * */
	private char currentChar() {
		if (isAtEnd())
			return '\0';
		return source.charAt(current);
	}
	
	
	/**
	 * 
	 * 
	 * */
	private char nextChar() {
		if (current + 1 >= source.length())
			return '\0';
		return source.charAt(current + 1);
	}
	
	

	/**
	 * Check if a Character is a Digit
	 * 
	 **/
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	/**
	 * Check if a Character is an alphabet
	 **/
	private boolean isAlpha(char c) {
	    return (c >= 'a' && c <= 'z') ||
	           (c >= 'A' && c <= 'Z') ||
	            c == '_';
	}
	
	/**
	 * Check if a Character is a alphabet or digit
	 **/
	private boolean isAlphaNumeric(char c) {
	    return isAlpha(c) || isDigit(c);
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}

	
	/**
	 * Next Character increase the index of current character, and return current
	 * character
	 * 
	 * @return current character
	 */
	private char next() {
		return source.charAt(current++);
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
		String text = source.substring(start, current);
		tokens.add(new Token(type, value, text, line));
	}

	
}