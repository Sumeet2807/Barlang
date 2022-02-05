package com.dev.mylang.barlang.stage3.ast;

import com.dev.mylang.barlang.stage3.ast.IType.TypeKind;

public interface IType extends IASTNode {

	public static enum TypeKind {
		INT,
		BOOLEAN,
		STRING,
		LIST
	}
	
	boolean isInt();
	boolean isBoolean();
	boolean isString();
	boolean isList();
	boolean isKind(TypeKind kind);
	
	String getDesc() throws Exception;
	String getClassName() throws Exception;
	
}
