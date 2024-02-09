package src;

/**
 * Token Types
 */
enum TokenType {
	// Single-character tokens.
	LEFT_PAREN, RIGHT_PAREN, DOT, MINUS, PLUS, DIV, MULT,

	// Literals.
	INT, DOUBLE, IDENTIFIER,

	// EOF
	EOF
}
