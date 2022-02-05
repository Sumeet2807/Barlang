package com.dev.mylang.barlang.stage1;

import com.dev.mylang.barlang.stage2.IbarParser;
import com.dev.mylang.barlang.stage2.Parser;
import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage4.TypeCheckVisitor;
//import com.dev.mylang.barlang.stage4.TypeCheckVisitor;
import com.dev.mylang.barlang.stage5.StarterCodeGenVisitor;


public class CompilerComponentFactory {


	public static IbarLexer getLexer(String input) {
		//Replace with whatever is needed for your lexer.
		IbarLexer lexer = new Lexer(input);
		return lexer;
	}
	
	public static IbarParser getParser(String input) {
		//Replace this with whatever is needed for your parser.
		IbarLexer lexer = new Lexer(input);
		IbarParser parser = new Parser(lexer);
		return parser;
	}

	public static ASTVisitor getTypeCheckVisitor() {
		// Replace this with whatever is needed for your compiler
		return new TypeCheckVisitor();
	}
	
	
	public static ASTVisitor getCodeGenVisitor(String className, String packageName, String sourceFileName) {
		//Replace this with whatever is needed for your compiler
		return new StarterCodeGenVisitor(className,packageName, sourceFileName);
	}

}
