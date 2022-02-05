package com.dev.mylang.barlang.stage3.ast;

import java.util.List;

public interface IProgram extends IASTNode {
	
	List<IDeclaration> getDeclarations();

}
