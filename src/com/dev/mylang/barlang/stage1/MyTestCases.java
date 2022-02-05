package com.dev.mylang.barlang.stage1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MyTestCases implements barTokenKinds {

	IbarLexer getLexer(String input) {
		return CompilerComponentFactory.getLexer(input);
	}

	@Test
	public void test0() throws LexicalException {
		String input = """

				""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test1() throws LexicalException {
		String input = """
				abc
				  "def"
				     ghi

				""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "abc");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.STRING_LITERAL);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "\"def\"");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 3);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, "ghi");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}
	
	

	@Test
	public void test2() throws LexicalException {
		String input = """
				a123 /*123a*/
				""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "a123");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test3() throws LexicalException {
		String input = """
				
				9999999""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.INT_LITERAL);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "9999999");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}

	@Test
	public void test4() throws LexicalException {
		String input = """
								
				/*fsdfsd\n*/"
				""";
		IbarLexer lexer = getLexer(input);
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IbarToken token = lexer.nextToken();
		});
	}

	@Test
	public void test5() throws LexicalException {
		String input = """
				i=9;
				b=j+9;
				
				""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "i");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.ASSIGN);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 1);
			String text = token.getText();
			assertEquals(text, "=");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.INT_LITERAL);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "9");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.SEMI);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 3);
			String text = token.getText();
			assertEquals(text, ";");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "b");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.ASSIGN);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 1);
			String text = token.getText();
			assertEquals(text, "=");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "j");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.PLUS);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 3);
			String text = token.getText();
			assertEquals(text, "+");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.INT_LITERAL);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 4);
			String text = token.getText();
			assertEquals(text, "9");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.SEMI);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, ";");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}
	
	@Test
	public void test6() throws LexicalException {
		String input = """
				
				!=&&""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.NOT_EQUALS);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "!=");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.AND);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "&&");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EOF);
		}
	}
	
	@Test
	public void test7() throws LexicalException {
		String input = """
				'abc"def'

				""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			String test_string = token.getStringValue();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "abc");
		}
		
	}

}