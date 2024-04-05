package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/*------------------ Note: This file is completed ----------------*/
public class Antephanie {

	// had-error
	static boolean hadError = false;

	
	/**
	 * RUN Jay Script using Prompt
	 */
	public static void main(String[] args) throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		for (;;) {
			System.out.print("Antephanie Parser --> ");
			String line = reader.readLine();
			if (line == null)
				break;
			run(line);
		}

	}

	/**
	 * use the Lexer and Parser, then
	 * print the generated AST
	 */
	private static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		
		Parser parser = new Parser(tokens);
		Expr expression = parser.parse();

		// Stop if there was a syntax error.
	    if (hadError) return;

	    System.out.println(new AstPrinter().print(expression));
		
		
	}

	/**
	 * Handle the Error
	 */
	static void error(int line, String message) {
		report(line, "", message);
	}

	
	
	//> Parsing Expressions token-error
	static void error(Token token, String message) {
	    if (token.type == TokenType.EOF) {
	        report(token.line, " at end", message);
	    } else {
	        report(token.line, " at '" + token.lexeme + "'", message);
	    }
	}

	private static void report(int line, String where, String message) {
	    System.err.println(
	            "[line " + line + "] Error" + where + ": " + message);
	    hadError = true;
	}
	//< Parsing Expressions token-error

}