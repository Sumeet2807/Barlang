package com.dev.mylang.barlang.stage3.ast;

public interface IstageStatement extends IStatement {
	
	IExpression getLeft();
	IExpression getRight();

}
