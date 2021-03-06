package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IBooleanLiteralExpression;

public class BooleanLiteralExpression__ extends Expression__ implements IBooleanLiteralExpression {
	
	final boolean value;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIBooleanLiteralExpression(this,arg);
	}

	@Override
	public boolean getValue() {
		return value;
	}

	public BooleanLiteralExpression__(int line, int posInLine, String text, boolean value) {
		super(line, posInLine, text);
		this.value = value;
	}

	@Override
	public String toString() {
		return "BooleanLiteralExpression__ [value=" + value + "]";
	}

	
}
