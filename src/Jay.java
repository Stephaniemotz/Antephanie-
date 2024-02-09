package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


/* ------------------------------------  
 * Note: This file is completed.
 * ---------------------------------- */
public class Jay {

	// had-error
	static boolean hadError = false;

	
	/**
	 * RUN Jay Script using Prompt
	 */
	public static void main(String[] args) throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		for (;;) {
			System.out.print("jay > ");
			String line = reader.readLine();
			if (line == null)
				break;
			run(line);
		}

	}

	/**
	 * use the Lexer/Scanner and 
	 * print the tokens
	 */
	private static void run(String source) {
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();

		if(!hadError) {
			// For now, just print the tokens.
			int i = 0;
			System.out.print("[");
			for (Token token : tokens) {
				System.out.print(token);
				if(++i < tokens.size()) System.out.print("; ");
			}
			System.out.println("]");
		}
		
		hadError = false;
	}

	/**
	 * Handle the Error
	 */
	static void error(String message) {
		report(message);
	}

	private static void report(String message) {
		System.err.println("Error: " + message);
		System.out.print("");
		hadError = true;
	}

}