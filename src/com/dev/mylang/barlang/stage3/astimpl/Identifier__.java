package com.dev.mylang.barlang.stage3.astimpl;

import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IDeclaration;
import com.dev.mylang.barlang.stage3.ast.IIdentifier;

public class Identifier__ extends ASTNode__ implements IIdentifier {

	String name;

	public Identifier__(int line, int posInLine, String text, String name) {
		super(line, posInLine, text);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIIdentifier(this, arg);
	}

	@Override
	public String toString() {
		return "Identifier__ [name=" + name + ", dec=" + dec + ", slot=" + slot + "]";
	}
	
	
    IDeclaration dec;

	public IDeclaration getDec() {
		return dec;
	}

	public void setDec(IDeclaration dec) {
		this.dec = dec;
	}
   
	int slot = -1;

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}
	
}
