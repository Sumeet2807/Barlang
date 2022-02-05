package com.dev.mylang.barlang.stage3.ast;

public interface INameDef extends IDeclaration {

	IIdentifier getIdent();
	IType getType();
	void setType(IType type);

}
