package com.dev.mylang.barlang.stage3.ast;

public interface IIfStatement extends IStatement {
	
	IExpression getGuardExpression();
	IBlock  getBlock();

}
