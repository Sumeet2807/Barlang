package com.dev.mylang.barlang.stage3.astimpl;

import java.util.List;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IBlock;
import com.dev.mylang.barlang.stage3.ast.IStatement;

public class Block__ extends ASTNode__ implements IBlock {
	

	public Block__(int line, int posInLine, String text, List<IStatement> statements) {
		super(line, posInLine, text);
		this.statements = statements;
	}

	final List<IStatement> statements;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIBlock(this, arg);
	}

	@Override
	public List<IStatement> getStatements() {
		return statements;
	}

	@Override
	public String toString() {
		return "Block__ [statements=" + statements + "]";
	}
	
	

}
