Hello Chemists, and Welcome to Antephanie₂!

This is a repository for the Final Project in CS441. Antephanie is a specialized programming language designed primarily as a lexical analyzer for Java. 

Users will input an equation that will return the equation described in Antephanie language. The language facilitates the parsing of mathematical equations 
and integrates periodic table elements that correspond to different aspects of the equations.

The tools we are using is the periodic table, Visual Studio Code (preferred IDE), JFlex (Lexer Generator for Java), and ChatGPT (AI used for language processing).

To use Antephanie, simply input an equation and the lexer will return the equation described in Antephanie language syntax. Examples include:

Lexer Example:

      Input:5 [P] 4
      Output: 9

Parser Example: 

      Input: experiments (groups) {​
                 reaction 1:​
                   element = "metals";​
                   spill;​
                 reaction 2:​
                    element = "nonmetals"; ​
                    spill;​
                 reaction 3:​
                    element = "noble gases";​
                    spill;​
              }  

Interpreter Example:

      Input:  VAR element = 14 (Silicon)
              REACTANT element > 1 [Am] element < 20 PRODUCT element-6
      Output: 14 (Silicon)
              8 (Oxygen)
              



