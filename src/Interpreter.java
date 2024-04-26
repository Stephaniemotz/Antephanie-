package src;

//import java.util.HashMap;
//import java.util.Map;

import src.Expr.IfElifElse;
//import src.Expr.Logical;
//import src.Expr.LogicalNot;
import src.Expr.Variable;

/**
 * Interpreter for expressions
 */
class Interpreter implements Expr.Visitor<Object>  {
	final Environment globals = new Environment();
	private Environment environment = globals;

	/**
	 * Interprets the given expression and prints its result.
	 */
	void interpret(Expr expression) {
		try {
			Object value = evaluate(expression);
			System.out.println(stringify(value));
		} catch (RuntimeError error) {
			Antephanie.runtimeError(error);
		}
	}


	/**
	 * Evaluates the given expression.
	 * 
	 * @param expr - The expression to evaluate.
	 * @return The result of the evaluation, as an Object. Use the expression's
	 *         accept method.
	 */
	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}
	
	@Override
	public Object visitIfElifElseExpr(IfElifElse expr) {

	    for (Expr.CondExprPair casePair : expr.cases) {

	        Object conditionResult = evaluate(casePair.condition);
	        
	        if (isTruthy(conditionResult)) {
	            return evaluate(casePair.expression);
	        }
	    }
	    if (expr.elseCase != null) {
	        return evaluate(expr.elseCase);
	    }

	    return null;
	}

	

	
	@Override
	public Object visitLogicalExpr(Expr.Logical expr) {
		Object left = evaluate(expr.left);

	    if (expr.operator.type == TokenType.OR) {
	      if (isTruthy(left)) return left;
	    } else {
	      if (!isTruthy(left)) return left;
	    }

	    return evaluate(expr.right);
	}
	
	@Override
	public Object visitLogicalNotExpr(Expr.LogicalNot expr) {
		Object value = evaluate(expr.expr);

	    return !(isTruthy(value));
	}
	
	
	@Override
	public Object visitAssignExpr(Expr.Assign expr) {
		Object value = evaluate(expr.value);
		
		environment.define(expr.name.lexeme, value);
		
		return value;
	}


	@Override
	public Object visitVariableExpr(Variable expr) {
		return environment.get(expr.name);
	}


	/**
	 * Visits a Grouping expression.
	 * 
	 * @param expr - The Grouping expression.
	 * @return The evaluated result of the inner expression.
	 */
	@Override
	public Object visitGroupingExpr(Expr.Grouping expr) {
		return evaluate(expr.expression);
	}

	/**
	 * Visits a Literal expression and returns its value.
	 * 
	 * @param expr - The Literal expression.
	 * @return The literal value.
	 */
	@Override
	public Object visitLiteralExpr(Expr.Literal expr) {
		return expr.value;
	}

	/**
	 * Visits a Binary expression.
	 * 
	 * @param expr The Binary expression.
	 * @return The result of applying the binary operation. Throws RuntimeError if
	 *         the operands do not match the expected types for the operation.
	 */
	@Override
	public Object visitBinaryExpr(Expr.Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);

		switch (expr.operator.type) {
		/* Lab 6 */
		// binary-equality
		case BANG_EQUAL: return !isEqual(left, right);
	    case EQUAL_EQUAL: return isEqual(left, right);
	    
	    // binary-comparison
	    case GREATER:
	    	// greater-operand
	        checkNumberOperands(expr.operator, left, right);
	        return (double)left > (double)right;
	    case GREATER_EQUAL:
	    	// greater-equal-operand
	    	checkNumberOperands(expr.operator, left, right);
	    	return (double)left >= (double)right;
		case LESS:
			// less-operand
			checkNumberOperands(expr.operator, left, right);
			return (double)left < (double)right;
		case LESS_EQUAL:
			// less-equal-operand
			checkNumberOperands(expr.operator, left, right);
			return (double)left <= (double)right;
			
			
		case PLUS:
			if (left instanceof Double && right instanceof Double) {
				return (double) left + (double) right;
			}
		case MINUS:
			checkNumberOperands(expr.operator, left, right);
			return (double) left - (double) right;
		case MULT:
			checkNumberOperands(expr.operator, left, right);
			return (double) left * (double) right;
		case DIV:
			checkNumberOperands(expr.operator, left, right);
			return (double) left / (double) right;
		default:
			break;
		}

		return null;
	}

	/**
	 * Visits a Unary expression.
	 * 
	 * @param expr - The Unary expression.
	 * @return The result of applying the unary operation. Throws RuntimeError if
	 *         the operand is not a number when expected.
	 */
	@Override
	public Object visitUnaryExpr(Expr.Unary expr) {
		Object right = evaluate(expr.right);

		switch (expr.operator.type) {
		case MINUS:
			checkNumberOperand(expr.operator, right);
			return -(double) right;
		default:
			break;
		}

		return null;
	}

	/**
	 * check-operand Checks if the operand is a number, throwing a RuntimeError if
	 * not.
	 * 
	 * @param operator - The operator, used for error reporting.
	 * @param operand  - The operand to check. Throws {@link RuntimeError} if the
	 *                 operand is not a number.
	 */
	private void checkNumberOperand(Token operator, Object operand) {
		if (operand instanceof Double)
			return;
		throw new RuntimeError(operator, "Operand must be a number.");
	}

	/**
	 * check-operands Checks if both operands are numbers, throwing a RuntimeError
	 * if not.
	 * 
	 * @param operator - The operator, used for error reporting.
	 * @param left     - The left operand.
	 * @param right    - The right operand. Throws {@link RuntimeError} if either
	 *                 operand is not a number.
	 */
	private void checkNumberOperands(Token operator, Object left, Object right) {
		if (left instanceof Double && right instanceof Double)
			return;
		throw new RuntimeError(operator, "Operands must be numbers.");
	}

	/**
	 * stringify
	 */
	private String stringify(Object object) {
		if (object == null)
			return "nil";

		if (object instanceof Double) {
			String text = object.toString();
			if (text.endsWith(".0")) {
				text = text.substring(0, text.length() - 2);
			}
			return text;
		}

		return object.toString();
	}

	/**
	 * 
	 * */
	private boolean isTruthy(Object object) {
	    if (object == null) return false;
	    if (object instanceof Boolean) return (boolean)object;
	    return true;
	}
	
	
	private boolean isEqual(Object a, Object b) {
	    if (a == null && b == null) return true;
	    if (a == null) return false;

	    return a.equals(b);
	  }


	


}
