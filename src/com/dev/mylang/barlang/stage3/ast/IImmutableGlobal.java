package com.dev.mylang.barlang.stage3.ast;

public interface IImmutableGlobal extends IDeclaration {

	
	INameDef getVarDef();
	IExpression getExpression();

}
