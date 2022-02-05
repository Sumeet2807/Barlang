package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.IUnaryExpression;

public class UnaryExpression__ extends Expression__ implements IUnaryExpression {

	IExpression expression;
	Kind op;
	
	public UnaryExpression__(int line, int posInLine, String text, IExpression expression, Kind op) {
		super(line, posInLine, text);
		this.expression = expression;
		this.op = op;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIUnaryExpression(this, arg);
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Kind getOp() {
		return op;
	}

	@Override
	public String toString() {
		return "UnaryExpression__ [expression=" + expression + ", op=" + op + "]";
	}
	
	

}
