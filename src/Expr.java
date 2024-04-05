package src;

//import java.util.List;
//import src.Expr.Assign;
//import src.Expr.Variable;
//import src.Expr.Visitor;


abstract class Expr {
	/**
	 * an abstract base class for different types of expression objects in an
	 * expression tree.
	 * 
	 **/

	// A generic interface within Expr that defines
	// a visitor with methods for visiting
	// different types of expressions.
	interface Visitor<R> {
		R visitBinaryExpr(Binary expr);

		R visitGroupingExpr(Grouping expr);

		R visitLiteralExpr(Literal expr);

		R visitUnaryExpr(Unary expr);
		
		R visitLogicalExpr(Logical expr); // New method for visiting logical expressions
		
		R visitAssignExpr(Assign expr);
		
		R visitVariableExpr(Variable expr);
	}

	// An abstract method must be implemented by subclasses.
	// It's part of the Visitor pattern, where it accepts
	// a visitor that will perform operations on the expression object.
	abstract <R> R accept(Visitor<R> visitor);

	/* * * * * * * * * * * * * 
	 * Expression Types: 
	 * - Literal 
	 * - Binary 
	 * - Unary 
	 * - Grouping
	 * 
	 * The subclasses of Expr represent different types of expressions. 
	 * Each subclass implements the accept method, calling 
	 * the appropriate visit method on the visitor, 
	 * passing this as an argument. 
	 * * * * * * * * * * *
	 */
	
	 public static class Assign extends Expr {
        public final Token name;
        public final TokenType operator;
        public final Expr value;

        public Assign(Token name, TokenType operator, Expr value) {
            this.name = name;
            this.operator = operator;
            this.value = value;
        }

		@Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }
	
	static class Variable extends Expr {
	    Variable(Token name) {
	      this.name = name;
	    }

	    @Override
	    <R> R accept(Visitor<R> visitor) {
	      return visitor.visitVariableExpr(this);
	    }

	    final Token name;
	  }
	
	

	/* Literal subclass */
	static class Literal extends Expr {
		Literal(Object value) {
			this.value = value;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLiteralExpr(this);
		}

		final Object value;
	}

	
	/* Binary Operation subclass */
	static class Binary extends Expr {
		Binary(Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitBinaryExpr(this);
		}

		final Expr left;
		final Token operator;
		final Expr right;
	}
	
	

	
	/* Unary Operation subclass */
	static class Unary extends Expr {
		// TODO: Task 2 - complete the Unary class, which represents 
		// a unary operation (e.g., negation) on a single expression.
		

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitUnaryExpr(this);
		}

		final Token operator;
		final Expr right;
		
		public Unary (Token operator, Expr right) {     //I DON'T KNOW IF THIS IS CORRECT
			this.operator = operator;
			this.right = right;
		}
	}

	/* Grouping subclass */
	static class Grouping extends Expr {
		Grouping(Expr expression) {
			this.expression = expression;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitGroupingExpr(this);
		}

		final Expr expression;
	}
	
	// Logical subclass for OR, AND, NOT
	static class Logical extends Expr {
        final Expr left;
        final Token operator;
        final Expr right;

        Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
            
      
        }
	}
}