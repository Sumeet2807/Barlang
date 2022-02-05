package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.IMutableGlobal;
import com.dev.mylang.barlang.stage3.ast.INameDef;
import com.dev.mylang.barlang.stage3.ast.IType;

public class MutableGlobal__ extends Declaration__ implements IMutableGlobal {

	INameDef varDef;
	IExpression expression;
	
	public MutableGlobal__(int line, int posInLine, String text, INameDef varDef, IExpression expr) {
		super(line, posInLine, text);
		this.varDef = varDef;
		this.expression = expr;
	}

	@Override
	public INameDef getVarDef() {
		return varDef;
	}
	
	@Override
	public IExpression getExpression() {
		return expression;
	}


	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIMutableGlobal(this, arg);
		
	}

	@Override
	public String toString() {
		return "MutableGlobal__ [varDef=" + varDef + ", expression=" + expression + "]";
	}


	@Override
	public void setType(IType type) {
		varDef.setType(type);		
	}
	
}
