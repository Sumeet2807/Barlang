package com.dev.mylang.barlang.stage3.ast;

public interface IListType extends IType {
	
	IType getElementType();
	void setElementType(IType type);

}
