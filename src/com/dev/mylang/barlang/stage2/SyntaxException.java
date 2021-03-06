package com.dev.mylang.barlang.stage2;

@SuppressWarnings("serial")
public class SyntaxException extends Exception {
	
	int line;
	int column;

	public SyntaxException(String message, int line, int charPositionInLine) {
		super( "Syntax Error " + line + ":" + charPositionInLine + "  " + message);
		this.line = line;
		this.column = charPositionInLine;
	}

}
