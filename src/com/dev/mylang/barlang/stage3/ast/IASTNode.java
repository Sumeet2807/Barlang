package com.dev.mylang.barlang.stage3.ast;

public interface IASTNode {
	
	Object visit(ASTVisitor v, Object arg) throws Exception;
	
	int getLine();
	int getPosInLine();
	String getText();

}
