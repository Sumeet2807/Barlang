package com.dev.mylang.barlang.stage3.ast;

import com.dev.mylang.barlang.stage3.ast.IType;

public interface IExpression extends IASTNode {

	IType getType();
	void setType(IType type);

}
