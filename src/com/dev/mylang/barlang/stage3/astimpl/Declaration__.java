package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.IDeclaration;

public abstract class Declaration__ extends ASTNode__ implements IDeclaration{

	public Declaration__(int line, int posInLine, String text) {
		super(line, posInLine, text);
	}



}
