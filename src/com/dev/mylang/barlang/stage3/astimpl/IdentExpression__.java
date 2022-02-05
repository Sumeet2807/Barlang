package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IIdentExpression;
import com.dev.mylang.barlang.stage3.ast.IIdentifier;

public class IdentExpression__ extends Expression__ implements IIdentExpression {
	
	IIdentifier name;

	public IdentExpression__(int line, int posInLine, String text, IIdentifier name) {
		super(line, posInLine, text);
		this.name = name;
	}
	
	@Override
	public IIdentifier getName() {
		return name;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIIdentExpression(this,arg);
	}

	@Override
	public String toString() {
		return "IdentExpression__ [name=" + name + "]";
	}

	
	
	

}
