package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IBlock;
import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.ILetStatement;
import com.dev.mylang.barlang.stage3.ast.INameDef;

public class LetStatement__ extends Statement__ implements ILetStatement {
	
	IBlock block;
	IExpression expression;
	INameDef localDef;
	
	public LetStatement__(int line, int posInLine, String text, IBlock block, IExpression expression,
			INameDef localDef) {
		super(line, posInLine, text);
		this.block = block;
		this.expression = expression;
		this.localDef = localDef;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitILetStatement(this, arg);
	}

	@Override
	public IBlock getBlock() {
		return block;
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public INameDef getLocalDef() {
		return localDef;
	}

	@Override
	public String toString() {
		return "LetStatement__ [block=" + block + ", expression=" + expression + ", localDef=" + localDef + "]";
	}
	
	

}
