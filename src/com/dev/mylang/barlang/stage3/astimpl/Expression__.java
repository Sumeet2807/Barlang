package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.IType;

public abstract class Expression__ extends ASTNode__ implements IExpression{

	public Expression__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}
	
	IType type;
	
	
	@Override
	public 
	IType getType() {
		return type;
	}
	

	@Override
	public
	void setType(IType type) {
		this.type = type;
	}

	


}
