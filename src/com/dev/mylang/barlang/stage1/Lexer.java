package com.dev.mylang.barlang.stage1;
import java.util.HashMap;



public class Lexer implements IbarLexer {
	
	public enum states {
		START,
		SYM,
		SYM_EQ,
		SYM_AND,
		SYM_BANG,
		SYM_OR,
		SYM_DIV,
		COMMENT,
		NUM,
		STR_D,
		STR_S,
		IDENT
		
	}
	HashMap<String, barTokenKinds.Kind> string_to_kind = new HashMap<String, barTokenKinds.Kind>();
	HashMap<String, String> keywords = new HashMap<String, String>();
	String ident_val;
	String myinput;	
	int exception_occurred = 0;
	int curr_char_pos=0;
	int new_token_pos=0;
	int curr_line_pos=1;
	int new_token_line=0;
	int curr_pos_inline=0;
	int new_token_pos_inline=0;
	char curr_char;
	Token token_to_return;
	states curr_state = states.START;
	
	
	public Lexer(String input) {
		myinput = input;
//		string_to_kind.put("", barTokenKinds.Kind.INT_LITERAL);
//		string_to_kind.put("", barTokenKinds.Kind.STRING_LITERAL);
//		string_to_kind.put("", barTokenKinds.Kind.IDENTIFIER);
		string_to_kind.put("FUN", barTokenKinds.Kind.KW_FUN);
		string_to_kind.put("(", barTokenKinds.Kind.LPAREN);
		string_to_kind.put(":", barTokenKinds.Kind.COLON);
		string_to_kind.put(",", barTokenKinds.Kind.COMMA);
		string_to_kind.put(")", barTokenKinds.Kind.RPAREN);
		string_to_kind.put("DO", barTokenKinds.Kind.KW_DO);
		string_to_kind.put("END", barTokenKinds.Kind.KW_END);
		string_to_kind.put("LET", barTokenKinds.Kind.KW_LET);
		string_to_kind.put("=", barTokenKinds.Kind.ASSIGN);
		string_to_kind.put(";", barTokenKinds.Kind.SEMI);
		string_to_kind.put("SWITCH", barTokenKinds.Kind.KW_SWITCH);
		string_to_kind.put("CASE", barTokenKinds.Kind.KW_CASE);
		string_to_kind.put("DEFAULT", barTokenKinds.Kind.KW_DEFAULT);
		string_to_kind.put("IF", barTokenKinds.Kind.KW_IF);
		string_to_kind.put("ELSE", barTokenKinds.Kind.KW_ELSE);
		string_to_kind.put("WHILE", barTokenKinds.Kind.KW_WHILE);
		string_to_kind.put("RETURN", barTokenKinds.Kind.KW_RETURN);
		string_to_kind.put("LIST", barTokenKinds.Kind.KW_LIST);
		string_to_kind.put("VAR", barTokenKinds.Kind.KW_VAR);
		string_to_kind.put("VAL", barTokenKinds.Kind.KW_VAL);
		string_to_kind.put("&&", barTokenKinds.Kind.AND);
		string_to_kind.put("||", barTokenKinds.Kind.OR);
		string_to_kind.put("<", barTokenKinds.Kind.LT);
		string_to_kind.put(">", barTokenKinds.Kind.GT);
		string_to_kind.put("==", barTokenKinds.Kind.EQUALS);
		string_to_kind.put("!=", barTokenKinds.Kind.NOT_EQUALS);
		string_to_kind.put("+", barTokenKinds.Kind.PLUS);
		string_to_kind.put("-", barTokenKinds.Kind.MINUS);
		string_to_kind.put("*", barTokenKinds.Kind.TIMES);
		string_to_kind.put("/", barTokenKinds.Kind.DIV);
		string_to_kind.put("!", barTokenKinds.Kind.BANG);
		string_to_kind.put("NIL", barTokenKinds.Kind.KW_NIL);
		string_to_kind.put("TRUE", barTokenKinds.Kind.KW_TRUE);
		string_to_kind.put("FALSE", barTokenKinds.Kind.KW_FALSE);
		string_to_kind.put("[", barTokenKinds.Kind.LSQUARE);
		string_to_kind.put("]", barTokenKinds.Kind.RSQUARE);
		string_to_kind.put("INT", barTokenKinds.Kind.KW_INT);
		string_to_kind.put("STRING", barTokenKinds.Kind.KW_STRING);
		string_to_kind.put("FLOAT", barTokenKinds.Kind.KW_FLOAT);
		string_to_kind.put("BOOLEAN", barTokenKinds.Kind.KW_BOOLEAN);		
//		string_to_kind.put("", barTokenKinds.Kind.EOF);
//		string_to_kind.put("", barTokenKinds.Kind.ERROR);
		
		
		keywords.put("BOOLEAN", "OK");
		keywords.put("VAR", "OK");
		keywords.put("VAL", "OK");
		keywords.put("FUN", "OK");
		keywords.put("DO", "OK");
		keywords.put("END", "OK");
		keywords.put("LET", "OK");
		keywords.put("SWITCH", "OK");
		keywords.put("CASE", "OK");
		keywords.put("DEFAULT", "OK");
		keywords.put("IF", "OK");
		keywords.put("WHILE", "OK");
		keywords.put("RETURN", "OK");
		keywords.put("NIL", "OK");
		keywords.put("TRUE", "OK");
		keywords.put("FALSE", "OK");
		keywords.put("INT", "OK");
		keywords.put("STRING", "OK");
		keywords.put("LIST", "OK");
//		keywords.put("BOOLEAN", "OK");
		
		
	}
	
	public Token generateToken(barTokenKinds.Kind kind,int char_pos_inline, int line_pos) {
		
		String text;
		Token new_token = new Token();
		new_token.mykind = kind;
		new_token.mycharpos = char_pos_inline;
		new_token.myline = line_pos;
		text = myinput.substring(new_token_pos, curr_char_pos);
		new_token.mytext = text;		
		
		return(new_token);
	}
	
	public IbarToken nextToken() throws LexicalException{
		
		barTokenKinds.Kind temp_kind;	
		String ident_val;

		int token_found = 0;
		
		if(exception_occurred == 1)
		{
			throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
		}
		
		while((token_found != 1) && (curr_char_pos <= myinput.length() - 1))
		{
			curr_char = myinput.charAt(curr_char_pos);			

			
			switch(curr_state) {
			case START:
				new_token_pos = curr_char_pos;
				new_token_line = curr_line_pos;
				new_token_pos_inline = curr_pos_inline;
				
				if(",;:()[]<>+-*".indexOf(curr_char) != -1)
				{
					curr_state = states.SYM;		
				}
				else if("=".indexOf(curr_char) != -1)
				{
					curr_state = states.SYM_EQ;
				}
				else if("&".indexOf(curr_char) != -1)
				{
					curr_state = states.SYM_AND;
				}
				else if("/".indexOf(curr_char) != -1)
				{
					curr_state = states.SYM_DIV;
				}
				else if("|".indexOf(curr_char) != -1)
				{
					curr_state = states.SYM_OR;
				}
				else if("!".indexOf(curr_char) != -1)
				{
					curr_state = states.SYM_BANG;
				}
				else if("0123456789".indexOf(curr_char) != -1)
				{
					curr_state = states.NUM;
				}
				else if((Character.isLetter(curr_char)) || ("_$".indexOf(curr_char) != -1))
				{
					curr_state = states.IDENT;
				}
				else if("\"".indexOf(curr_char) != -1)
				{
					curr_state = states.STR_D;
				}
				else if("'".indexOf(curr_char) != -1)
				{
					curr_state = states.STR_S;
				}
				else if(" \n\t\r".indexOf(curr_char) != -1)
				{
					if("\n\r".indexOf(curr_char) != -1)
					{
						curr_line_pos++;
						curr_pos_inline = -1;
					}
				}
				else
				{
					throw new LexicalException("Illegal character!",new_token_line,new_token_pos_inline);
				}

				curr_char_pos++;
				curr_pos_inline++;
				break;
				
				
			case SYM_EQ:
				curr_state = states.START;
				if("=".indexOf(curr_char) != -1)
				{				
					curr_char_pos++;
					curr_pos_inline++;
					token_to_return = generateToken(barTokenKinds.Kind.EQUALS,new_token_pos_inline,new_token_line);
					token_found = 1;
					
				}
				else
				{
					token_to_return = generateToken(barTokenKinds.Kind.ASSIGN,new_token_pos_inline,new_token_line);
					token_found = 1;					
					
				}
				break;
				
			case SYM_DIV:
				if("*".indexOf(curr_char) != -1)
				{				
					curr_char_pos++;
					curr_pos_inline++;
					curr_state = states.COMMENT;
					
				}
				else
				{
					curr_state = states.START;
					token_to_return = generateToken(barTokenKinds.Kind.DIV,new_token_pos_inline,new_token_line);
					token_found = 1;					
					
				}
				break;

			case SYM_BANG:
				curr_state = states.START;
				if("=".indexOf(curr_char) != -1)
				{				
					curr_char_pos++;
					curr_pos_inline++;
					token_to_return = generateToken(barTokenKinds.Kind.NOT_EQUALS,new_token_pos_inline,new_token_line);
					token_found = 1;
					
				}
				else
				{
					token_to_return = generateToken(barTokenKinds.Kind.BANG,new_token_pos_inline,new_token_line);
					token_found = 1;					
					
				}
				break;
				
			case SYM_AND:
				curr_state = states.START;
				if("&".indexOf(curr_char) != -1)
				{
					curr_char_pos++;
					curr_pos_inline++;
					token_to_return = generateToken(barTokenKinds.Kind.AND,new_token_pos_inline,new_token_line);
					token_found = 1;
					
				}
				else
				{
					exception_occurred = 1;
					throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
				}
				break;
				
			case SYM_OR:
				curr_state = states.START;
				if("|".indexOf(curr_char) != -1)
				{
					curr_char_pos++;
					curr_pos_inline++;
					token_to_return = generateToken(barTokenKinds.Kind.OR,new_token_pos_inline,new_token_line);
					token_found = 1;
					
				}
				else
				{
					exception_occurred = 1;
					throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
				}
				break;
				
			case NUM:
				if(Character.isDigit(curr_char))
				{
					curr_char_pos++;
					curr_pos_inline++;
				}
				else
				{
					try
					{
						Integer.parseInt(myinput.substring(new_token_pos, curr_char_pos));
					}
					catch (Exception e)
					{
						exception_occurred = 1;
						throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);						
					}
					token_to_return = generateToken(barTokenKinds.Kind.INT_LITERAL,new_token_pos_inline,new_token_line);
					token_found = 1;
					curr_state = states.START;
				}
				break;
				
				
			case IDENT:
				if(("_$".indexOf(curr_char) != -1)||(Character.isDigit(curr_char))||(Character.isLetter(curr_char)))
				{
					curr_char_pos++;
					curr_pos_inline++;
				}
				else
				{
					ident_val = myinput.substring(new_token_pos, curr_char_pos);
					if(keywords.containsKey(ident_val))
					{
						temp_kind = string_to_kind.get(ident_val);
						token_to_return = generateToken(temp_kind,new_token_pos_inline,new_token_line);
						token_found = 1;
						curr_state = states.START;
					}
					else
					{
						token_to_return = generateToken(barTokenKinds.Kind.IDENTIFIER,new_token_pos_inline,new_token_line);
						token_found = 1;
						curr_state = states.START;
						
					}
				}
				break;
				
			case STR_D:
				if("\"".indexOf(curr_char) != -1)
				{
					curr_char_pos++;
					curr_pos_inline++;
					token_to_return = generateToken(barTokenKinds.Kind.STRING_LITERAL,new_token_pos_inline,new_token_line);
					token_found = 1;
					curr_state = states.START;
				}
				else if("\\".indexOf(curr_char) != -1)
				{
					if((curr_char_pos + 1) >=  myinput.length())
					{
						exception_occurred = 1;
						throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
					}
					if("btnrf\"'\\".indexOf(myinput.charAt(curr_char_pos + 1)) != -1) 
					{
						curr_char_pos = curr_char_pos + 2;
						curr_pos_inline = curr_pos_inline + 2;
					}
					else
					{
						exception_occurred = 1;
						throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
						
					}
				}
				else
				{
					curr_char_pos++;
					curr_pos_inline++;
				}
				break;
				
			case STR_S:
				if("'".indexOf(curr_char) != -1)
				{
					curr_char_pos++;
					curr_pos_inline++;
					token_to_return = generateToken(barTokenKinds.Kind.STRING_LITERAL,new_token_pos_inline,new_token_line);
					token_found = 1;
					curr_state = states.START;
				}
				else if("\\".indexOf(curr_char) != -1)
				{
					if((curr_char_pos + 1) >=  myinput.length())
					{
						exception_occurred = 1;
						throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
					}
					if("btnrf\"'\\".indexOf(myinput.charAt(curr_char_pos + 1)) != -1) 
					{
						curr_char_pos = curr_char_pos + 2;
						curr_pos_inline = curr_pos_inline + 2;
					}
					else
					{
						exception_occurred = 1;
						throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
						
					}
				}
				else
				{
					curr_char_pos++;
					curr_pos_inline++;
				}
				break;
				
			case SYM:
//				if(",;:()[]<>+-*".indexOf(curr_char) != -1)
//				{
				curr_state = states.START;
				temp_kind = string_to_kind.get(myinput.substring(new_token_pos, curr_char_pos));
				token_to_return = generateToken(temp_kind,new_token_pos_inline,new_token_line);
				token_found = 1;
//				}
				break;
				
			case COMMENT:
				if("*".indexOf(curr_char) != -1)
				{
					if((curr_char_pos + 1) >=  myinput.length())
					{
						exception_occurred = 1;
						throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
					}
					if("/".indexOf(myinput.charAt(curr_char_pos + 1)) != -1)
					{
						curr_state = states.START;						
					}
					curr_char_pos = curr_char_pos + 2;
					curr_pos_inline = curr_pos_inline + 2;
				}
				else
				{
					curr_char_pos++ ;
					curr_pos_inline++ ;
				}
				break;
		
			default:
				break;
				
				
			}
		}
		

		
		
		if((curr_char_pos >= myinput.length()) && (token_found != 1))
		{
			switch(curr_state)
			{
			case START:
				token_to_return = new Token();
				token_to_return.mykind = barTokenKinds.Kind.EOF;				
				break;
				
			case SYM_DIV:
				token_to_return = generateToken(barTokenKinds.Kind.DIV,new_token_pos_inline,new_token_line);
				curr_state = states.START;
				break;
				
			case SYM_AND:
				token_to_return = generateToken(barTokenKinds.Kind.AND,new_token_pos_inline,new_token_line);
				curr_state = states.START;
				break;
				
			case SYM_EQ:
				token_to_return = generateToken(barTokenKinds.Kind.ASSIGN,new_token_pos_inline,new_token_line);
				curr_state = states.START;
				break;
				
			case SYM_OR:
				token_to_return = generateToken(barTokenKinds.Kind.OR,new_token_pos_inline,new_token_line);
				curr_state = states.START;
				break;
				
			case SYM_BANG:
				token_to_return = generateToken(barTokenKinds.Kind.BANG,new_token_pos_inline,new_token_line);
				curr_state = states.START;
				break;
			
			case NUM:
				token_to_return = generateToken(barTokenKinds.Kind.INT_LITERAL,new_token_pos_inline,new_token_line);
				curr_state = states.START;
				break;

			case IDENT:
				ident_val = myinput.substring(new_token_pos, curr_char_pos);
				if(keywords.containsKey(ident_val))
				{
					temp_kind = string_to_kind.get(ident_val);
					token_to_return = generateToken(temp_kind,new_token_pos_inline,new_token_line);
				}
				else
				{
					token_to_return = generateToken(barTokenKinds.Kind.IDENTIFIER,new_token_pos_inline,new_token_line);
				}
				curr_state = states.START;
				break;
				
			case STR_D:
			case STR_S:
			case COMMENT:
				exception_occurred = 1;
				throw new LexicalException("something wrong",new_token_line,new_token_pos_inline);
				
			default:
				break;
			
			
			}
		}
		
		return token_to_return;		
			
		
	}
	

}
