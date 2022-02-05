package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.IASTNode;

public abstract class ASTNode__ implements IASTNode {
	
	final int line;
	final int posInLine;
	final String text;
	
	
	public ASTNode__(int line, int posInLine, String text) {
		super();
		this.line = line;
		this.posInLine = posInLine;
		this.text = text;
	}


	public int getLine() {
		return line;
	}


	public int getPosInLine() {
		return posInLine;
	}


	public String getText() {
		return text;
	}
	
	

}
