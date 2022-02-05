package com.dev.mylang.barlang.stage1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ExampleLexerTests implements barTokenKinds {

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
				  def
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
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 2);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "def");
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
				a123 123a
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
			assertEquals(kind, Kind.INT_LITERAL);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, "123");
			int val = token.getIntValue();
			assertEquals(val, 123);
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.IDENTIFIER);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 8);
			String text = token.getText();
			assertEquals(text, "a");
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
				= == ===
				""";
		IbarLexer lexer = getLexer(input);
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.ASSIGN);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 0);
			String text = token.getText();
			assertEquals(text, "=");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EQUALS);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 2);
			String text = token.getText();
			assertEquals(text, "==");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.EQUALS);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 5);
			String text = token.getText();
			assertEquals(text, "==");
		}
		{
			IbarToken token = lexer.nextToken();
			Kind kind = token.getKind();
			assertEquals(kind, Kind.ASSIGN);
			int line = token.getLine();
			assertEquals(line, 1);
			int charPositionInLine = token.getCharPositionInLine();
			assertEquals(charPositionInLine, 7);
			String text = token.getText();
			assertEquals(text, "=");
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
				a %
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
			assertEquals(text, "a");
		}
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IbarToken token = lexer.nextToken();
		});
	}

	@Test
	public void test5() throws LexicalException {
		String input = """
				99999999999999999999999999999999999999999999999999999999999999999999999
				""";
		IbarLexer lexer = getLexer(input);
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IbarToken token = lexer.nextToken();
		});
	}

}