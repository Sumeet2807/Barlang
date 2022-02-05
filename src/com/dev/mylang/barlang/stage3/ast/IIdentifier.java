package com.dev.mylang.barlang.stage3.ast;

public interface IIdentifier extends IASTNode {
	
	String getName();
	IDeclaration getDec();
	void setDec(IDeclaration dec);
	void setSlot(int i);
	int getSlot();
	
}
