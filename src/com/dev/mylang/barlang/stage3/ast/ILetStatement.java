package com.dev.mylang.barlang.stage3.ast;

public interface ILetStatement extends IStatement {
	
	IBlock getBlock();
	IExpression getExpression();
	INameDef getLocalDef();

}
