package com.dev.mylang.barlang.stage1;

public interface IbarLexer {
	
	IbarToken nextToken() throws LexicalException;
}
