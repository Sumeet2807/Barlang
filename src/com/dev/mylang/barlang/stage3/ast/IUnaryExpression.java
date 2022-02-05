package com.dev.mylang.barlang.stage3.ast;

import com.dev.mylang.barlang.stage1.barTokenKinds;

public interface IUnaryExpression extends IExpression, barTokenKinds{
	
	IExpression getExpression();
	Kind getOp();
}
