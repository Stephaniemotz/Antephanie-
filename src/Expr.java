package src;

import java.util.List;

/**
 * Represents an expression in the abstract syntax tree (AST). The Expr class
 * hierarchy is designed to represent various types of expressions encountered
 * in the language, utilizing the Visitor pattern for expression evaluation.
 */
abstract class Expr {

	/**
	 * Defines a generic visitor interface with methods to visit different types of
	 * expressions. This allows for the implementation of operations (like
	 * evaluation) to be defined externally to the class hierarchy.
	 */
	interface Visitor<R> {
		R visitIfElifElseExpr(IfElifElse expr);		/* Lab 7 */
		
		R visitLogicalExpr(Logical expr); 
		
		R visitLogicalNotExpr(LogicalNot logicalNot);

		R visitAssignExpr(Assign expr);

		R visitBinaryExpr(Binary expr);

		R visitGroupingExpr(Grouping expr);

		R visitLiteralExpr(Literal expr);

		R visitUnaryExpr(Unary expr);

		R visitVariableExpr(Variable expr);

		
	}

	/**
	 * Accepts a visitor that will perform some operations based on the type of the
	 * expression.
	 * 
	 * @param visitor - The visitor that will process this expression.
	 * @return The result of the operation performed by the visitor, of generic type
	 *         R.
	 */
	abstract <R> R accept(Visitor<R> visitor);
	
	/**
	 * 
	 * */
	static class CondExprPair {
        final Expr condition;
        final Expr expression;

        public CondExprPair(Expr condition, Expr expression) {
            this.condition = condition;
            this.expression = expression;
        }
        
        
    }
	
	static class IfElifElse extends Expr {
		
		IfElifElse(List<CondExprPair> cases, Expr elseCase) {
		      this.cases = cases;
		      this.elseCase = elseCase;
		    }
		

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitIfElifElseExpr(this);
		}

		final List<CondExprPair> cases;
		final Expr elseCase;
	}
	
	
	
	/**
	 * 
	 * */
	static class Logical extends Expr {
		Logical(Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLogicalExpr(this);
		}

		final Expr left;
		final Token operator;
		final Expr right;
	}
	
	static class LogicalNot extends Expr {
		LogicalNot(Token operator, Expr expr) {
			this.operator = operator;
			this.expr = expr;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLogicalNotExpr(this);
		}

		final Token operator;
		final Expr expr;
	}

	static class Assign extends Expr {
		Assign(Token name, Expr value) {
			this.name = name;
			this.value = value;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitAssignExpr(this);
		}

		final Token name;
		final Expr value;
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

	/**
	 * Represents a literal expression in the AST.
	 */
	static class Literal extends Expr {
		final Object value;

		Literal(Object value) {
			this.value = value;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitLiteralExpr(this);
		}
	}

	/**
	 * Represents a binary operation between two expressions.
	 */
	static class Binary extends Expr {
		final Expr left;
		final Token operator;
		final Expr right;

		Binary(Expr left, Token operator, Expr right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitBinaryExpr(this);
		}
	}

	/**
	 * Represents a unary operation on a single expression.
	 */
	static class Unary extends Expr {
		final Token operator;
		final Expr right;

		Unary(Token operator, Expr right) {
			this.operator = operator;
			this.right = right;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitUnaryExpr(this);
		}
	}

	/**
	 * Represents an expression enclosed in parentheses.
	 */
	static class Grouping extends Expr {
		final Expr expression;

		Grouping(Expr expression) {
			this.expression = expression;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitGroupingExpr(this);
		}
	}
}