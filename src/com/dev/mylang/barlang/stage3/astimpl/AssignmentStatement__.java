package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IstageStatement;
import com.dev.mylang.barlang.stage3.ast.IExpression;

public class stageStatement__ extends Statement__ implements IstageStatement {
	
	IExpression left;
	IExpression right;
	public stageStatement__(int line, int posInLine, String text, IExpression left, IExpression right) {
		super(line, posInLine, text);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIstageStatement(this,arg);
	}

	@Override
	public IExpression getLeft() {
		return left;
	}

	@Override
	public IExpression getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "stageStatement__ [left=" + left + ", right=" + right + "]";
	}


	

}
