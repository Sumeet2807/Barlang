package com.dev.mylang.barlang.stage2;



import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.dev.mylang.barlang.stage1.CompilerComponentFactory;


class ExampleParserTests {
	
	static boolean VERBOSE = true;
	
	void noErrorParse(String input)  {
		IbarParser parser = CompilerComponentFactory.getParser(input);
		try {
			parser.parse();
		} catch (Throwable e) {
			throw new RuntimeException(e); 
		}
	}
	

	private void syntaxErrorParse(String input, int line, int column) {
		IbarParser parser = CompilerComponentFactory.getParser(input);
		assertThrows(SyntaxException.class, () -> {
			try {
			parser.parse();
			}
			catch(SyntaxException e) {
				if (VERBOSE) System.out.println(input + '\n' + e.getMessage());
				Assertions.assertEquals(line, e.line);
				Assertions.assertEquals(column, e.column);
				throw e;
			}
		});
		
	}

	

	@Test public void test0() {
		String input = """

		""";
		noErrorParse(input);
	}
	

		@Test public void test1() {
		String input = """
		VAL a: INT = 0;
		""";
		noErrorParse(input);
		}


		@Test public void test2() {
		String input = """
		VAL a: STRING = "hello";
		""";
		noErrorParse(input);
		}


		@Test public void test3() {
		String input = """
		VAL b: BOOLEAN = TRUE;
		""";
		noErrorParse(input);
		}


		@Test public void test4() {
		String input = """
		VAR b: LIST[];
		""";
		noErrorParse(input);
		}

       //This input has a syntax error at line 2, position 19.
		@Test public void test5()  {
		String input = """
		FUN func() DO
		WHILE x>0 DO x=x-1 END
		END
		""";
		syntaxErrorParse(input,2,19);
		}

		@Test public void test6()  {
		String input = """
		WHILE
		""";
		syntaxErrorParse(input,1,0);
		}
		
		@Test public void test7()  {
		String input = """
		THIS IS COMPLETE GIBBERISH
		""";
		syntaxErrorParse(input,1,0);
		}		

		@Test public void test8()  {
		String input = """
		FUN func() DO
		WHILE x>0 DO x=x-1; END
		END
		""";
		noErrorParse(input);
		
		}
		
		@Test public void test9()  {
		String input = """
		VAL a: INT = 0;
		VAL a: STRING = "hello";
		VAL b: BOOLEAN = TRUE;
		VAR b: LIST[];
		FUN func() DO
		WHILE x>0 DO x=x-1; END
		END
		""";
		noErrorParse(input);
		
		}
		
		@Test public void test10()  {
		String input = """
		FUN soln(a:LIST[INT],d:LIST[INT]) DO
		SWITCH aa==bb
		CASE exam: LET ans=a*d DO END
		DEFAULT RETURN a/d;
		END
		END
		""";
		noErrorParse(input);
		
		}
		
		@Test public void test11()  {
		String input = """
		VAR A:LIST[LIST[INT]];
		""";
		noErrorParse(input);
		
		}
		
		@Test public void test12()  {
		String input = """
		VAR B:LIST[LIST[LIST[BOOLEAN]]];
		""";
		noErrorParse(input);
		
		}
		
		@Test public void test13()  {
		String input = """
		VAR a:LIST[LIST[]];
		""";
		noErrorParse(input);
		
		}
}