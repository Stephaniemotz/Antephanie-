package src;

/**
 * Token Types
 */
enum TokenType {

	//Delimeters
	LPAREN, RPAREN, LBRACKET, RBRACKET, LBRACE, RBRACE, COMMA, PERIOD, SEMI, COLON, 
	// (, ), {, }, -->, <--, ',' , . , ; , : 

	// Operators.
	DOT, MINUS, PLUS, DIV, MULT, MOD, OR, AND, NOT, XOR, LOR, LAND, LNOT, LESSTHAN, GREATERTHAN, LESSEQ, GREATEQ, EQUAL, NOTEQUAL, 
	//DOT not included, [P], [S], [Ts], [Dy], [Mo], [O], [Am], [No], [Lr], [Al], [Lu], [La], [Ga], [Li], [Ge], [Eu], [Ne],

	//Assignment Operators
	TIMESEQUAL, DIVEQUAL, MODEQUAL, PLUSEQUAL, MINUSEQUAL, 
	//[Te], [Ds], [Pa], [Mn],

	//Increment/Decrement
	INCREMENT, DECREMENT,
	//[In], [Md]

	// Literals. (This will be changed to only take in float numbers, to represent atomic weight)
	INT, DOUBLE, IDENTIFIER,

	//Comments.
	COMMENTS,
	//H20

	// EOF
	EOF
}
