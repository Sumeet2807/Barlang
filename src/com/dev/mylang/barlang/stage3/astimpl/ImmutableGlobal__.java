package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.IImmutableGlobal;
import com.dev.mylang.barlang.stage3.ast.INameDef;
import com.dev.mylang.barlang.stage3.ast.IType;

public class ImmutableGlobal__ extends Declaration__ implements IImmutableGlobal {
	
	INameDef nameDef;
	IExpression expression;
	
	public ImmutableGlobal__(int line, int posInLine, String text, INameDef nameDef, IExpression expression) {
		super(line, posInLine, text);
		this.nameDef = nameDef;
		this.expression = expression;
	}

	@Override
	public INameDef getVarDef() {
		return nameDef;
	}

	@Override
	public IExpression getExpression() {
		return expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIImmutableGlobal(this,arg);
	}

	@Override
	public String toString() {
		return "ImmutableGlobal__ [nameDef=" + nameDef + ", expression=" + expression + "]";
	}

	@Override
	public void setType(IType type) {
		nameDef.setType(type);
	}
	
	
	

}
