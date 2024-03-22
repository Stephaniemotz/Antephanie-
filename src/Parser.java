package src;

//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import static src.TokenType.*;
//import static Jay.Expr.*;

class Parser {
	private final List<Token> tokens;
	private int current = 0;

	Parser(List<Token> tokens) {
		//Task 3.1 - Takes a List<Token> as an argument and 
		//initializes the tokens field with it.
		
		this.tokens = tokens;  //Check this? I'm not sure
		this.current = 0;

	}

	/*
	 * * The entry point for parsing
	 */
	Expr parse() {
		// Task 3.12 - starts the parsing process and
		// returns the resulting AST.
		Expr res = this.term();
		return res;
		

	}

	// Rule: term → factor ( ( "+" | "-" ) factor )*
	private Expr term() {
		Expr left = factor();

		while (match(MINUS, PLUS)) {
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
		
		while (match(DIV, MULT)) {
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

		if (match(DOUBLE)) {
			
			advance();
			return new Expr.Literal(previous().value);
			

		}

		if (match(LPAREN)) {
			// Task 3.11 - follow the rule:
			// primary → "(" expression ")"
			// and return a Grouping AST nodes
			advance();
			Expr node = term();
			consume(RPAREN, "Expect ')' after expression.");
			
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
	private boolean isAtEnd() {
		// Task 3.4 - check if current token is EOF
		if (this.tokens.get(current).value == EOF) 
			return true;
		return false;

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
