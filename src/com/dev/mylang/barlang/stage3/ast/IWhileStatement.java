package com.dev.mylang.barlang.stage3.ast;

public interface IWhileStatement extends IStatement {
	
	IExpression getGuardExpression();
	IBlock getBlock();

}
