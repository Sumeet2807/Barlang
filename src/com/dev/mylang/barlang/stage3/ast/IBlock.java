package com.dev.mylang.barlang.stage3.ast;

import java.util.List;

public interface IBlock extends IASTNode {

	List<IStatement> getStatements();
	
}
