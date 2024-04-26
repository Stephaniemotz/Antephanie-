package src;

/*------------------ Note: This file is completed ----------------*/

/**
 * Token Types
 */
enum TokenType {
	// Single-character tokens.
	LEFT_PAREN, RIGHT_PAREN, // (  ,  )
	LBRACKET  , RBRACKET,    // {  ,  }
	LBRACE    , RBRACE,      // ~  ,  *
	COMMA     , PERIOD,      // ,  ,  . 
	SEMIC     , COLON,       // ;  ,  :
	
	//Arithmetic Operations
	MINUS, PLUS, DIV, MULT,    // - , [P], [Dy], [Ts]
	MOD, INCREMENT, DECREMENT, // [Mo] , [In], [Md] 
	
	//Assignment Operators
	DIVEQUAL, PLUSEQUAL, MINUSEQUAL, TIMESEQUAL,  //[Ts=], [Dy=], [P=], [-=]

	// Comparison Operations
	BANG_EQUAL,             //[No=]
	EQUAL, EQUAL_EQUAL,     // =, ==
	GREATER, GREATER_EQUAL, // >, >=
	LESS, LESS_EQUAL,       // <, <=
	
	//Logical Operations
	OR, AND, NOT,           //[O], [Am], [No]   
	
	//Decision Making 
	IF, THEN, ELIF, ELSE,   //Reactant, Product, Elif, Else
	SWITCH, CASE, BREAK,    //Experiment, Reaction, Spill
	
	//Looping
	FOR, WHILE, DO,        // For, While, Do 
	
	// Literals.
	NUMBER, 
	
	// Keywords.
	VAR, IDENTIFIER,       //Indepdendent, Identifier
	
	//Comments 
	StartComment, EndComment,  //#PERIODIC, PERIODIC#
	SINGLECOMMENT,             //%
	

	// EOF
	EOF
}