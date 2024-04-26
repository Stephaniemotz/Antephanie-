package src;

import java.util.List;

import src.Expr.Assign;
import src.Expr.Logical;
import src.Expr.LogicalNot;
import src.Expr.Variable;

class AstPrinter implements Expr.Visitor<String> {

	String print(Expr expr) {
		return expr.accept(this);
	}
	@Override
	public String visitIfElifElseExpr(Expr.IfElifElse expr) {
		return parenthesize3("if", expr.cases, expr.elseCase);
	}

	@Override
	public String visitLogicalExpr(Logical expr) {
		return parenthesize(expr.operator.lexeme, expr.left, expr.right);
	}
	
	@Override
	public String visitLogicalNotExpr(LogicalNot expr) {
		return parenthesize(expr.operator.lexeme, expr.expr);
	}
	
	@Override
	public String visitBinaryExpr(Expr.Binary expr) {
		return parenthesize(expr.operator.lexeme, expr.left, expr.right);
	}

	@Override
	public String visitGroupingExpr(Expr.Grouping expr) {
		return parenthesize("group", expr.expression);
	}

	@Override
	public String visitLiteralExpr(Expr.Literal expr) {
		if (expr.value == null)
			return "nil";
		return expr.value.toString();
	}

	@Override
	public String visitUnaryExpr(Expr.Unary expr) {
		return parenthesize(expr.operator.lexeme, expr.right);
	}

	

	@Override
	public String visitAssignExpr(Assign expr) {
		return parenthesize2("=", expr.name.lexeme, expr.value);
	}

	@Override
	public String visitVariableExpr(Variable expr) {
		return expr.name.lexeme;
	}

	private String parenthesize(String name, Expr... exprs) {
		StringBuilder builder = new StringBuilder();

		builder.append("(").append(name);
		for (Expr expr : exprs) {
			builder.append(" ");
			builder.append(expr.accept(this));
		}
		builder.append(")");

		return builder.toString();
	}
	
	private String parenthesize2(String name, Object... parts) {
		StringBuilder builder = new StringBuilder();

		builder.append("(").append(name);
		transform(builder, parts);
		builder.append(")");

		return builder.toString();
	}
	
	private String parenthesize3(String name, List<Expr.CondExprPair> pairs, Expr elseCase) {
		StringBuilder builder = new StringBuilder();
		builder.append("(").append(name);
		
		for(Expr.CondExprPair pair: pairs) {
			transform(builder, pair.condition, pair.expression);
		}
		
		if( elseCase != null ) {
			transform(builder, elseCase);
		}
		
		builder.append(")");

		return builder.toString();
	}
	
	

	private void transform(StringBuilder builder, Object... parts) {
		for (Object part : parts) {
			builder.append(" ");
			if (part instanceof Expr) {
				builder.append(((Expr) part).accept(this));
			} else if (part instanceof Token) {
				builder.append(((Token) part).lexeme);
			} else if (part instanceof List) {
				transform(builder, ((List) part).toArray());
			} else {
				builder.append(part);
			}
		}
	}	

}