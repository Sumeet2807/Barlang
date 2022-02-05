package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.IReturnStatement;

public class ReturnStatement__ extends Statement__ implements IReturnStatement {
	
	IExpression expression;

	public ReturnStatement__(int line, int posInLine, String text, IExpression returnExpression) {
		super(line, posInLine, text);
		this.expression = returnExpression;
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIReturnStatement(this, arg);
	}

	@Override
	public String toString() {
		return "ReturnStatement__ [expression=" + expression + "]";
	}
	
	

}
