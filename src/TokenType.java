package src;


/**
 * Token Types
 */
enum TokenType {
	
	//Delimeters 
	LEFT_PAREN, RIGHT_PAREN, LBRACKET, RBRACKET, LBRACE, RBRACE, COMMA, PERIOD  , SEMI, COLON,
	//  (      ,      )    ,    {    ,    }    ,    ~  ,   *   ,  ',' ,    .    ,  ;  ,  :
	  
	//Arithmetic Operations
	MINUS, PLUS, DIV, MULT, MOD, INCREMENT, DECREMENT, 
	// - , [P] , [Dy], [Ts], [Mo], [In],      [Md],
	
	//Assignment Operators
	EQUAL, TIMESEQUAL, DIVEQUAL, PLUSEQUAL, MINUSEQUAL, 
	//=,      [Ts=],    [Dy=],     [P=],      [-=],
	
	//Comparison Operations
	EQUALTO, NOTEQUAL, GREATERTHAN, LESSTHAN, GREATEQ, LESSEQ,
	// ==,  [No=], >, <, >=, <=,
	
	//Logical Operations
	OR, AND, NOT, 
	//[O],[Am], [No],
	
	//Decision Making (Else if??)
	IF, ELSE, SWITCH, CASE, BREAK,
	//Reactant, Product, Experiment, Reaction, Spill
	
	//Looping 
	FOR, WHILE, DO,
		
	// Literals.
	NUMBER, 
	
	//Keywords
	VAR, ID,
	//Independent
	
	//Block Comments
	SCOMMENT, ECOMMENT,
	// #PERIODIC ,  PERIODIC# 
	
	//Single Line Comment 
	SINGLECOMMENT,
	//%
	
	// EOF
	EOF
}

//TODO!!
//EQUALTO ==
//>=
//<=


//Period are now just . (it broke everything)
//Block Comments are now #periodic and periodic#
//Single line comments are %
// Independent (variable) *the variable being changed or controlled by a scientist*
