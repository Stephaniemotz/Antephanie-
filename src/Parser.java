package src;

//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import src.Expr.Literal;
//import src.Expr.Assign;

import static src.TokenType.*;
//import static src.Expr.*;

class Parser {
	private final List<Token> tokens;
	private int current = 0;

	Parser(List<Token> tokens) {
		//Task 3.1 - Takes a List<Token> as an argument and 
		//initializes the tokens field with it.
		
		this.tokens = tokens;  
		this.current = 0;

	}
	
	/*
	 * * The entry point for parsing
	 */
	Expr parse() {
		if (peek().type == VAR) {
			return assignment();
		}
		return term();
	 }
	
	
	/* private Expr assignment() {

		
		consume(VAR, "Expect keyword Independent.");
		
		// Lab 5
		// TODO Task 3.1 – update the assignment() method, which 
		// can create an Expr.Assign Node (for variable declaration).
		Token name = consume(ID, "Expected indentifier after 'VAR'.");
		
		consume(EQUAL, "Expected '=' after the variable name.");


		Expr value = factor();
	    System.out.println("(" + name + " : " + ((Literal) value).value + ")");
	    return value;
		
	    
	} */

	private Expr assignment() {
		consume(VAR, "Expect keyword Independent.");
		Token name = consume(ID, "Expected identifier after 'VAR'.");
		TokenType operatorType = matchOperators(); 
		Expr value = factor();
		System.out.println("(" + operatorType + " " + ((Literal) value).value + ")");
		//return new Expr.Assign(name, operatorType, value);
		return value;
	}
	
	//Assignment Operators
	private TokenType matchOperators() {
		if (check(EQUAL)) {
			consume(EQUAL, "Expected '=' after the variable name.");
			return EQUAL;
		} else if (check(TIMESEQUAL)) {
			consume(TIMESEQUAL, "Expected '[Ts=]' after the variable name.");
			return TIMESEQUAL;
		} else if (check(DIVEQUAL)) {
			consume(DIVEQUAL, "Expected '[Dy=]' after the variable name.");
			return DIVEQUAL;
		} else if (check(PLUSEQUAL)) {
			consume(PLUSEQUAL, "Expected '[P=]' after the variable name.");
			return PLUSEQUAL;
		} else if (check(MINUSEQUAL)) {
			consume(MINUSEQUAL, "Expected '[-=]' after the variable name.");
			return MINUSEQUAL;
		} // Add more operators here as needed
		else {
			// Handle unexpected operator
			throw error(peek(), "Unexpected operator in assignment.");
		}
	}
	
	

	

	// Rule: term → factor ( ( "+" | "-" ) factor )*
	private Expr term() {
		Expr left = factor();

		while (match(MINUS, PLUS, GREATERTHAN, LESSTHAN, GREATEQ, LESSEQ, NOTEQUAL, EQUALTO, NOT, AND, OR)) {
			Token operator = peek();
			advance();
			Expr right = factor();

			// Task 3.7 - follow the grammar rule for a term and
			// return a Binary Operation AST nodes
			left = new Expr.Binary(left, operator, right);
			
		}

		return left;
	}

	// Rule: factor → unary ( ( "/" | "*" ) unary )* ;
	private Expr factor() {
		
		Expr left = unary();
		// Task 3.8 - follow the grammar rule for a factor and
		// return a Binary Operation AST nodes
		
		while (match(DIV, MULT, MOD)) {
			Token operator = peek();
			advance();
			Expr right = unary();
			
			left = new Expr.Binary(left,  operator, right);
			
			
		}
		return left;
		
	}

	// Rule: unary → "-" unary | primary ;
	private Expr unary() {
		
		// Task 3.9 - follow the grammar rule for a unary and
		// return a Unary Operation AST nodes
		if (match(MINUS)) {
			Token operator = peek();
			advance();
			Expr right = unary();
			return new Expr.Unary(operator, right);
			
		}

		return primary();

	}

	// Rule: primary → NUMBER | "(" expression ")" ;
	private Expr primary() {

		if (match(NUMBER)) {
			
			// TODO: Task 3.10 - return a Literal AST nodes
			advance();
			return new Expr.Literal(previous().value);
			

		}
	
		if (match(LEFT_PAREN)) {
			// Task 3.11 - follow the rule:
			// primary → "(" expression ")"
			// and return a Grouping AST nodes
			advance();
			Expr node = term();
			consume(RIGHT_PAREN, "Expect ')' after expression.");
			
			return new Expr.Grouping(node);
			

		}

		throw error(peek(), "Expect expression.");

	}
	


	/************** Utility Methods *****************/

	
	private boolean match(TokenType... types) {
		// Task 3.6 - check if the type of 
		// current token matches with 
		// one of the expected types
		for (TokenType type : types) {
			while (check(type) == true)  // i have no idea
				return true;
		}
		return false;
	}

	// checking if the current token is a specific type
	private boolean check(TokenType type) {
		if (isAtEnd())
			return false;
		return peek().type == type;
	}

	// consume - ensuring the syntax is correct
	private Token consume(TokenType type, String message) {
		if (check(type))
			return advance();

		throw error(peek(), message);
	}

	// return the token and advancing the token pointer
	private Token advance() {
		// Task 3.5 - return the token 
		// and advancing the token pointer
		if (!isAtEnd())
			current++;
		return this.tokens.get(current);
	}

	// check if current token is EOF
	/* private boolean isAtEnd() {
		// Task 3.4 - check if current token is EOF
		if (this.tokens.get(current).value == EOF) 
			return true;
		return false;

	}*/
	private boolean isAtEnd() {
	    return current >= tokens.size() || peek().type == EOF;
	}

	// get current token
	private Token peek() {
		// Task 3.3 - get current token
		return this.tokens.get(current);
	}

	// get previous token
	private Token previous() {
		// Task 3.2 - get previous token
		//if current > 0, then return current -1 	
		return this.tokens.get(current-1);
		
	}

	/************** Error Handling *****************/

	private static class ParseError extends RuntimeException {
	}

	private ParseError error(Token token, String message) {
		Antephanie.error(token, message);
		return new ParseError();
	}

}