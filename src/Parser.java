package src;

import java.util.ArrayList;
import java.util.List;


import static src.TokenType.*;
import static src.Expr.*;

class Parser {
	// parse-error
	private static class ParseError extends RuntimeException {
	}

	private ParseError error(Token token, String message) {
		Antephanie.error(token, message);
		return new ParseError();
	}

	// tokens
	private final List<Token> tokens;
	private int current = 0;

	Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	Expr parse() {
		return assignment();
	}

	private Expr assignment() {
		if (peek().type == VAR) {
			consume(VAR, "Expect keyword VAR.");

			Token name = consume(IDENTIFIER, "Expect variable name.");

			Expr expr = null;
			if (match(EQUAL)) {
				expr = term();
			}

			return new Expr.Assign(name, expr);
		}
		return or();
	}
	
	
	private Expr or() {
	    Expr expr = and();

	    while (match(OR)) {
	      Token operator = previous();
	      Expr right = and();
	      expr = new Expr.Logical(expr, operator, right);
	    }

	    return expr;
	  }

	
	private Expr and() {
	    Expr expr = not();
	
	    while (match(AND)) {
	      Token operator = previous();
	      Expr right = not();
	      expr = new Expr.Logical(expr, operator, right);
	    }
	
	    return expr;
	}
	
	private Expr not() {
		if (peek().type == NOT) {
			Token operator = consume(NOT, "Expect keyword NOT");
			Expr node = not();
			
			return new Expr.LogicalNot(operator, node);
		}
		else {
			return equality();
		}
		
	}

	  
	  private Expr equality() {
	    Expr expr = comparison();

	    while (match(BANG_EQUAL, EQUAL_EQUAL)) {
	      Token operator = previous();
	      Expr right = comparison();
	      expr = new Expr.Binary(expr, operator, right);
	    }

	    return expr;
	  }
	

	  
	  private Expr comparison() {
	    Expr expr = term();

	    while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
	      Token operator = previous();
	      Expr right = term();
	      expr = new Expr.Binary(expr, operator, right);
	    }

	    return expr;
	  }
	  
	  

	// Rule: term → factor ( ( "+" | "-" ) factor )*
	private Expr term() {
		Expr left = factor();

		while (match(MINUS, PLUS)) {
			Token operator = previous();
			Expr right = factor();
			left = new Expr.Binary(left, operator, right);
		}

		return left;
	}

	// Rule: factor → unary ( ( "/" | "*" ) unary )* ;
	private Expr factor() {
		Expr left = unary();

		while (match(DIV, MULT)) {
			Token operator = previous();
			Expr right = unary();
			left = new Expr.Binary(left, operator, right);
		}

		return left;
	}

	// Rule: unary → "-" unary | primary ;
	private Expr unary() {
		if (match(MINUS)) {
			Token operator = previous();
			Expr right = unary();
			return new Expr.Unary(operator, right);
		}

		return primary();

	}

	// Rule: primary → NUMBER | "(" expression ")"
	//       | if_elif_else;
	
	private Expr primary() {

		if (match(NUMBER)) {
			return new Expr.Literal(previous().value);
		}

		if (match(IDENTIFIER)) {
			return new Expr.Variable(previous());
		}

		if (match(LEFT_PAREN)) {
			Expr expr = term();
			consume(RIGHT_PAREN, "Expect ')' after expression.");
			return new Expr.Grouping(expr);
		}
		
		/* Lab 7 */
		// Task 2.1 -
		// handle the rule:  primary  →  if_elif_else
		if (peek().type == IF) {
			return IfElifElse();
		}

		throw error(peek(), "Expect expression.");

	}

	
	/* Lab 7 */
	private Expr IfElifElse() {
		// handle the rule:  
		// if_elif_else   →	 "IF" expression THEN expression
		//        			 ("ELIF" expression THEN expression)*
		//        			 ("ELSE" expression)?
		
		List<CondExprPair> cases = new ArrayList<CondExprPair>();
		Expr elseCase = null;
		
		
		// part 1 - "IF" expression THEN expression
		consume(IF, "Expected 'IF'. ");						// IF
		
		Expr condition = parse();							// expression
		
		consume(THEN, "Expected 'THEN' after condition. ");	// ELSE
		
		Expr expression = parse();							// expression
		
		cases.add(new Expr.CondExprPair(condition, expression));
		
		
		// Task 2.2 - 
		// part 2 - ("ELIF" expression THEN expression)*
		while (match(ELIF)) {
	        Expr elifCondition = parse(); 
	        
	        consume(THEN, "Expected 'THEN' after 'elif' condition.");
	        
	        Expr elifExpression = parse(); 
	        
	        cases.add(new CondExprPair(elifCondition, elifExpression)); 	    
	    
		}
	
		
		// Task 2.3 - 
		// part 3 - ("ELSE" expression)?
		if (match(ELSE)) {
			
	        elseCase = parse(); 
	        
	    }
		
		return new Expr.IfElifElse(cases, elseCase);
		
	}
	
	

	// > match token types
	private boolean match(TokenType... types) {
		for (TokenType type : types) {
			if (check(type)) {
				advance();
				return true;
			}
		}

		return false;
	}

	// > check
	private boolean check(TokenType type) {
		if (isAtEnd())
			return false;
		return peek().type == type;
	}

	// > consume
	private Token consume(TokenType type, String message) {
		if (check(type))
			return advance();

		throw error(peek(), message);
	}

	// > advance
	private Token advance() {
		if (!isAtEnd())
			current++;
		return previous();
	}

	// > utils
	private boolean isAtEnd() {
		return peek().type == EOF;
	}

	private Token peek() {
		return tokens.get(current);
	}

	private Token previous() {
		return tokens.get(current - 1);
	}
	// < utils

}
