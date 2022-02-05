package com.dev.mylang.barlang.stage2;
import com.dev.mylang.barlang.stage1.LexicalException;
import com.dev.mylang.barlang.stage1.barTokenKinds;
import com.dev.mylang.barlang.stage1.IbarLexer;
import com.dev.mylang.barlang.stage1.IbarToken;
import com.dev.mylang.barlang.stage3.astimpl.*;
import com.dev.mylang.barlang.stage3.ast.*;
import java.util.*;




public class Parser implements IbarParser {
	
	IbarLexer mylexer;
	IbarToken curr_token;
	barTokenKinds.Kind curr_kind; 
	
	public Parser(IbarLexer lexer)
	{
		mylexer = lexer;
	}
	
	public IASTNode parse() throws LexicalException, SyntaxException {

		curr_token = mylexer.nextToken();
		curr_kind = curr_token.getKind();
		return(program());
	}
	
	public IProgram program() throws LexicalException, SyntaxException {
		
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();	
		List<IDeclaration> declarations_list = new ArrayList<IDeclaration>();	
		IProgram retnode = null;
		int kleene = 1;
		
		while(kleene == 1)
		{
			if((curr_kind == barTokenKinds.Kind.KW_FUN) ||
					(curr_kind == barTokenKinds.Kind.KW_VAR) ||
					(curr_kind == barTokenKinds.Kind.KW_VAL)) {
				
				declarations_list.add(declaration());				
				
			}
			else {
				kleene = 0;
			}
		}			
		match(barTokenKinds.Kind.EOF);
		retnode = new Program__(Line, CharPositionInLine, Text, declarations_list);
		return(retnode);	
	}
	
	public IDeclaration declaration() throws LexicalException, SyntaxException
	{
		
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();	
		IDeclaration retnode = null;
		INameDef namedefnode = null;
		IExpression expnode = null;
		
		switch(curr_kind) {
		case KW_FUN:
			retnode = function();
			break;
		
		case KW_VAR:
			match(barTokenKinds.Kind.KW_VAR);
			namedefnode = namedef();
			if(curr_kind == barTokenKinds.Kind.ASSIGN) {
				match(barTokenKinds.Kind.ASSIGN);
				expnode = expression();				
			}
			match(barTokenKinds.Kind.SEMI);
			retnode = new MutableGlobal__(Line, CharPositionInLine, Text, namedefnode, expnode);
			break;
			
		case KW_VAL:
			match(barTokenKinds.Kind.KW_VAL);
			namedefnode = namedef();
			match(barTokenKinds.Kind.ASSIGN);
			expnode = expression();
			match(barTokenKinds.Kind.SEMI);
			retnode = new ImmutableGlobal__(Line, CharPositionInLine, Text, namedefnode, expnode);
			break;
		default:
			throw new SyntaxException("Syntax Error!",
					curr_token.getLine(),
					curr_token.getCharPositionInLine());
					
		}			
		
		return(retnode);
	}
	
	public IFunctionDeclaration function() throws LexicalException, SyntaxException {
		
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();		
		int kleene = 1;
		IFunctionDeclaration retnode = null;
		List<INameDef> args_list = new ArrayList<INameDef>();
		IType typenode = null;
		IBlock blocknode = null;
		
				
		match(barTokenKinds.Kind.KW_FUN);
		IIdentifier identnode = new Identifier__(curr_token.getLine(), curr_token.getCharPositionInLine(),
				curr_token.getText(),curr_token.getText()); 
		match(barTokenKinds.Kind.IDENTIFIER);
		match(barTokenKinds.Kind.LPAREN);
		if(curr_kind == barTokenKinds.Kind.IDENTIFIER) {
			args_list.add(namedef());
			while(kleene == 1) {
				if(curr_kind == barTokenKinds.Kind.COMMA) {
					match(barTokenKinds.Kind.COMMA);
					args_list.add(namedef());
				}
				else {
					kleene = 0;
				}
			}
		}
		match(barTokenKinds.Kind.RPAREN);
		if(curr_kind==barTokenKinds.Kind.COLON) {
			match(barTokenKinds.Kind.COLON);
			typenode = type();
		}
		match(barTokenKinds.Kind.KW_DO);
		blocknode = block();
		match(barTokenKinds.Kind.KW_END);		
		
		retnode = new FunctionDeclaration___(Line ,CharPositionInLine,Text, identnode, args_list, typenode, blocknode);
		
		return(retnode);
	}
	
	public IBlock block() throws LexicalException, SyntaxException {
		
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		int kleene = 1;
		IBlock retnode = null;
		List<IStatement> statement_list = new ArrayList<IStatement>();
		
		while(kleene == 1) {
			
			if((curr_kind == barTokenKinds.Kind.KW_LET)||
					(curr_kind == barTokenKinds.Kind.KW_SWITCH)||
					(curr_kind == barTokenKinds.Kind.KW_IF)||
					(curr_kind == barTokenKinds.Kind.KW_WHILE)||
					(curr_kind == barTokenKinds.Kind.KW_RETURN)||
					(curr_kind == barTokenKinds.Kind.BANG)||
					(curr_kind == barTokenKinds.Kind.MINUS)||
					(curr_kind == barTokenKinds.Kind.KW_NIL)||
					(curr_kind == barTokenKinds.Kind.KW_TRUE)||
					(curr_kind == barTokenKinds.Kind.KW_FALSE)||
					(curr_kind == barTokenKinds.Kind.INT_LITERAL)||
					(curr_kind == barTokenKinds.Kind.STRING_LITERAL)||
					(curr_kind == barTokenKinds.Kind.LPAREN)||
					(curr_kind == barTokenKinds.Kind.IDENTIFIER))
			{
				statement_list.add(statement());
			}
			else {
				kleene = 0;
			}
			
		}
		retnode = new Block__(Line ,CharPositionInLine,Text, statement_list);
		return(retnode);
	}
	
	public INameDef namedef() throws LexicalException, SyntaxException {

		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		INameDef retnode = null;
		IType typenode = null;
		
		IIdentifier identnode = new Identifier__(Line, CharPositionInLine,Text,Text); 
		match(barTokenKinds.Kind.IDENTIFIER);
		
		if(curr_kind == barTokenKinds.Kind.COLON) {
			match(barTokenKinds.Kind.COLON);
			typenode = type();			
		}
		
		retnode = new NameDef__(Line ,CharPositionInLine,Text, identnode, typenode);
		return(retnode);
	}
		
	public IStatement statement() throws LexicalException, SyntaxException {
		
		int kleene = 1;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		IStatement retnode = null;
		IExpression expnode = null;
		IExpression expnode_r = null;
		IBlock blocknode = null;
		List<IExpression> exp_list = new ArrayList<IExpression>();
		List<IBlock> block_list = new ArrayList<IBlock>();
		
		switch(curr_kind) {
		case KW_LET:
			
			match(barTokenKinds.Kind.KW_LET);
			INameDef namedefnode= namedef();
			if(curr_kind == barTokenKinds.Kind.ASSIGN) {
				match(barTokenKinds.Kind.ASSIGN);				
				expnode = expression();				
			}
//			match(barTokenKinds.Kind.SEMI);
			match(barTokenKinds.Kind.KW_DO);
			blocknode = block();
			match(barTokenKinds.Kind.KW_END);
			retnode = new LetStatement__(Line ,CharPositionInLine, Text, blocknode , expnode,namedefnode); 
			break;
			
		case KW_SWITCH:
			
			
			match(barTokenKinds.Kind.KW_SWITCH);
			expnode = expression();
			kleene = 1;
			while(kleene == 1) {
				if(curr_kind == barTokenKinds.Kind.KW_CASE) {
					match(barTokenKinds.Kind.KW_CASE);
					exp_list.add(expression());
					match(barTokenKinds.Kind.COLON);
					block_list.add(block());
				}
				else {
					kleene = 0;
				}
			}
			match(barTokenKinds.Kind.KW_DEFAULT);
			blocknode = block();
			match(barTokenKinds.Kind.KW_END);
			retnode = new SwitchStatement__(Line ,CharPositionInLine, Text, expnode, exp_list, block_list, blocknode);
			break;
			
		case KW_IF:
			match(barTokenKinds.Kind.KW_IF);
			expnode = expression();
			match(barTokenKinds.Kind.KW_DO);
			blocknode = block();
			match(barTokenKinds.Kind.KW_END);
			retnode = new IfStatement__(Line ,CharPositionInLine, Text, expnode, blocknode);
			break;
			
		case KW_WHILE:
			match(barTokenKinds.Kind.KW_WHILE);
			expnode = expression();
			match(barTokenKinds.Kind.KW_DO);
			blocknode = block();
			match(barTokenKinds.Kind.KW_END);
			retnode = new WhileStatement__(Line ,CharPositionInLine, Text, expnode, blocknode);
			break;
			
		case KW_RETURN:
			match(barTokenKinds.Kind.KW_RETURN);
			expnode = expression();
			match(barTokenKinds.Kind.SEMI);
			retnode = new ReturnStatement__(Line ,CharPositionInLine, Text, expnode);
			break;
			
		case MINUS:
		case BANG:
		case KW_NIL:
		case KW_TRUE:
		case KW_FALSE:
		case INT_LITERAL:
		case STRING_LITERAL:
		case LPAREN:
		case IDENTIFIER:
			expnode = expression();
			if(curr_kind == barTokenKinds.Kind.ASSIGN) {
				match(barTokenKinds.Kind.ASSIGN);
				expnode_r = expression();
			}
			match(barTokenKinds.Kind.SEMI);
			retnode = new stageStatement__(Line ,CharPositionInLine, Text, expnode, expnode_r);
			break;
			
		default:
			throw new SyntaxException("Syntax Error!",
					curr_token.getLine(),
					curr_token.getCharPositionInLine());
		
		}
		
		return(retnode);
		
	}
	
	public IExpression expression() throws LexicalException, SyntaxException {
		return(logicalexpression());
	}
	
	public IExpression logicalexpression() throws LexicalException, SyntaxException {
		
		IExpression retnode = null;
		int kleene = 1;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		retnode = comparison_expression();		
		
		while(kleene == 1) {
			if(curr_kind == barTokenKinds.Kind.AND) {
			
				match(barTokenKinds.Kind.AND);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, comparison_expression(),barTokenKinds.Kind.AND);
				
			}
			else if(curr_kind == barTokenKinds.Kind.OR) {
				
				match(barTokenKinds.Kind.OR);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, comparison_expression(),barTokenKinds.Kind.OR);
			}
			else {				
				kleene = 0;
			}
				
		}
		return(retnode);
	 }
	
	public IExpression comparison_expression() throws LexicalException, SyntaxException {
		
		IExpression retnode = null;
		int kleene = 1;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		retnode = additive_expression();
		
		while(kleene == 1) {
			if(curr_kind == barTokenKinds.Kind.GT) {
			
				match(barTokenKinds.Kind.GT);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, additive_expression(),barTokenKinds.Kind.GT);
				
			}
			else if(curr_kind == barTokenKinds.Kind.LT) {
				
				match(barTokenKinds.Kind.LT);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, additive_expression(),barTokenKinds.Kind.LT);
			}
			else if(curr_kind == barTokenKinds.Kind.EQUALS) {
				
				match(barTokenKinds.Kind.EQUALS);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, additive_expression(),barTokenKinds.Kind.EQUALS);
			}
			else if(curr_kind == barTokenKinds.Kind.NOT_EQUALS) {
				
				match(barTokenKinds.Kind.NOT_EQUALS);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, additive_expression(),barTokenKinds.Kind.NOT_EQUALS);
			}
			else {				
				kleene = 0;
			}
				
		}
		return(retnode);
	}
		
	
	public IExpression additive_expression() throws LexicalException, SyntaxException {
		
		IExpression retnode = null;
		int kleene = 1;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		retnode = multiplicative_expression();
		
		
		while(kleene == 1) {
			if(curr_kind == barTokenKinds.Kind.PLUS) {
			
				match(barTokenKinds.Kind.PLUS);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, multiplicative_expression(),barTokenKinds.Kind.PLUS);
				
			}
			else if(curr_kind == barTokenKinds.Kind.MINUS) {
				
				match(barTokenKinds.Kind.MINUS);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, multiplicative_expression(),barTokenKinds.Kind.MINUS);
			}
			else {				
				kleene = 0;
			}
				
		}	
		return(retnode);
		
	}
		
	public IExpression multiplicative_expression() throws LexicalException, SyntaxException {
		
		IExpression retnode = null;
		int kleene = 1;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		retnode = unary_expression();
		while(kleene == 1) {
			if(curr_kind == barTokenKinds.Kind.TIMES) {
			
				match(barTokenKinds.Kind.TIMES);				
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, unary_expression(),barTokenKinds.Kind.TIMES);
				
			}
			else if(curr_kind == barTokenKinds.Kind.DIV) {
				
				match(barTokenKinds.Kind.DIV);
				retnode = new BinaryExpression__(Line ,CharPositionInLine, Text, retnode, unary_expression(),barTokenKinds.Kind.DIV);
			}
			else {				
				kleene = 0;
			}
				
		}
		return(retnode);
	}
		
	public IExpression unary_expression() throws LexicalException, SyntaxException {
		
		IExpression retnode = null;
		barTokenKinds.Kind kind = null;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		

		if(curr_kind == barTokenKinds.Kind.BANG) {	
			match(barTokenKinds.Kind.BANG);	
			kind = barTokenKinds.Kind.BANG;
		}
		else if(curr_kind == barTokenKinds.Kind.MINUS) {
			match(barTokenKinds.Kind.MINUS);
			kind = barTokenKinds.Kind.MINUS;
		}
		
		retnode = primary_expression();
		
		if(kind != null) {
			retnode = new UnaryExpression__(Line ,CharPositionInLine, Text, retnode, kind );
		}
		
		return(retnode);
				
	}
	
	public IExpression primary_expression() throws LexicalException, SyntaxException {
		
		
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		int kleene = 1;
		IExpression retnode = null;
		List<IExpression> args = new ArrayList<IExpression>();
		IIdentifier ident_node = null;
		IExpression expnode = null;
		
		switch(curr_kind) {
		case KW_NIL:
			
			retnode = new NilConstantExpression__(curr_token.getLine(), curr_token.getCharPositionInLine(), 
					curr_token.getText());
			match(barTokenKinds.Kind.KW_NIL);
			break;
		case KW_TRUE:
			
			retnode = new BooleanLiteralExpression__(curr_token.getLine(), curr_token.getCharPositionInLine(), 
					curr_token.getText(),true);
			match(barTokenKinds.Kind.KW_TRUE);
			break;
		case KW_FALSE:
			
			retnode = new BooleanLiteralExpression__(curr_token.getLine(), curr_token.getCharPositionInLine(), 
					curr_token.getText(),false);
			match(barTokenKinds.Kind.KW_FALSE);
			break;
		case INT_LITERAL:
			
			retnode = new IntLiteralExpression__(curr_token.getLine(), curr_token.getCharPositionInLine(), 
					curr_token.getText(),curr_token.getIntValue());
			match(barTokenKinds.Kind.INT_LITERAL);
			break;
		case STRING_LITERAL:
			
			retnode = new StringLiteralExpression__(curr_token.getLine(), curr_token.getCharPositionInLine(), 
					curr_token.getText(),curr_token.getStringValue());
			match(barTokenKinds.Kind.STRING_LITERAL);
			break;
		case LPAREN:
			match(barTokenKinds.Kind.LPAREN);			
			retnode = expression();
			match(barTokenKinds.Kind.RPAREN);
			break;
		case IDENTIFIER:
			ident_node = new Identifier__(Line, CharPositionInLine,Text,Text); 
			retnode = new IdentExpression__(Line, CharPositionInLine,Text,ident_node);
			match(barTokenKinds.Kind.IDENTIFIER);			
			if(curr_kind == barTokenKinds.Kind.LPAREN) {
				
				match(barTokenKinds.Kind.LPAREN);
				if((curr_kind == barTokenKinds.Kind.BANG) ||
						(curr_kind == barTokenKinds.Kind.MINUS)||
						(curr_kind == barTokenKinds.Kind.KW_NIL)||
						(curr_kind == barTokenKinds.Kind.KW_TRUE)||
						(curr_kind == barTokenKinds.Kind.KW_FALSE)||
						(curr_kind == barTokenKinds.Kind.INT_LITERAL)||
						(curr_kind == barTokenKinds.Kind.STRING_LITERAL)||
						(curr_kind == barTokenKinds.Kind.LPAREN)||
						(curr_kind == barTokenKinds.Kind.IDENTIFIER)) {
					
					args.add(expression());
					kleene = 1;
					while(kleene == 1) {
						
						if(curr_kind == barTokenKinds.Kind.COMMA){
							match(barTokenKinds.Kind.COMMA);
							args.add(expression());
						}
						else {
							kleene = 0;
						}
						
					}
				}
				match(barTokenKinds.Kind.RPAREN);
				retnode = new FunctionCallExpression__(Line, CharPositionInLine,Text,ident_node,args);
			}
			else if(curr_kind == barTokenKinds.Kind.LSQUARE){
				
				match(barTokenKinds.Kind.LSQUARE);
				expnode = expression();
				match(barTokenKinds.Kind.RSQUARE);
				retnode = new ListSelectorExpression__(Line, CharPositionInLine, Text, ident_node,expnode);
			}
			
			break;
		default:
			throw new SyntaxException("Syntax Error!",
					curr_token.getLine(),
					curr_token.getCharPositionInLine());
		}
		return(retnode);
		
	}
	
	public IType type() throws LexicalException, SyntaxException {
		
		IType retnode = null;
		int Line = curr_token.getLine();
		int CharPositionInLine = curr_token.getCharPositionInLine();
		String Text = curr_token.getText();
		IType typenode = null;
		
		switch(curr_kind) {
		case KW_INT:
			match(barTokenKinds.Kind.KW_INT);
			retnode = new PrimitiveType__(Line, CharPositionInLine, Text, IType.TypeKind.INT);
			break;
		case KW_STRING:
			match(barTokenKinds.Kind.KW_STRING);
			retnode = new PrimitiveType__(Line, CharPositionInLine, Text, IType.TypeKind.STRING);
			break;
		case KW_BOOLEAN:			
			match(barTokenKinds.Kind.KW_BOOLEAN);
			retnode = new PrimitiveType__(Line, CharPositionInLine, Text, IType.TypeKind.BOOLEAN);
			break;
		case KW_LIST:
			match(barTokenKinds.Kind.KW_LIST);
			match(barTokenKinds.Kind.LSQUARE);
			if((curr_kind == barTokenKinds.Kind.KW_INT)||
					(curr_kind == barTokenKinds.Kind.KW_STRING)||
					(curr_kind == barTokenKinds.Kind.KW_BOOLEAN)||
					(curr_kind == barTokenKinds.Kind.KW_LIST))
			{
				Line = curr_token.getLine();
				CharPositionInLine = curr_token.getCharPositionInLine();
				Text = curr_token.getText();
				typenode = type();
				
			}
			match(barTokenKinds.Kind.RSQUARE);
			retnode = new ListType__(Line, CharPositionInLine, Text, typenode);
			break;
		default:
			throw new SyntaxException("Syntax Error!",
					curr_token.getLine(),
					curr_token.getCharPositionInLine());
		}
		
		return(retnode);		
				
	}
		
	public void match(barTokenKinds.Kind kind) throws LexicalException, SyntaxException{
		if(curr_kind == kind) {	
			curr_token = mylexer.nextToken();
			curr_kind = curr_token.getKind();
				
			}			
		else {		
					
			throw new SyntaxException("Syntax Error!",
					curr_token.getLine(),
					curr_token.getCharPositionInLine());
		}
	}
	
	
}

// check identifier