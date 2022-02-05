package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.INilConstantExpression;

public class NilConstantExpression__ extends Expression__ implements INilConstantExpression {

	public NilConstantExpression__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitINilConstantExpression(this, arg);
	}

	@Override
	public String toString() {
		return "NilConstantExpression__ []";
	}
	
	

}
