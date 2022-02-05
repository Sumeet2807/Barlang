package com.dev.mylang.barlang.stage3.ast;

public interface IListSelectorExpression extends IExpression {
	
	IIdentifier getName();
	IExpression getIndex();

}
