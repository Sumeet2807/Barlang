package com.dev.mylang.barlang.stage3.ast;

import com.dev.mylang.barlang.stage1.barTokenKinds;

public interface IBinaryExpression extends IExpression, barTokenKinds {
	
	IExpression getLeft();
	IExpression getRight();
	Kind getOp();

}
