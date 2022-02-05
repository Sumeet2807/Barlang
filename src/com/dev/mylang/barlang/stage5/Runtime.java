package com.dev.mylang.barlang.stage5;

public class Runtime {

	public static boolean not(boolean arg) {
		return !arg;
	}
	
	public static boolean and(boolean arg1,boolean arg2) {
		return(arg1 && arg2);
	}
	
	public static boolean or(boolean arg1,boolean arg2) {
		return(arg1 || arg2);
	}
	
	public static boolean int_equals(int arg1,int arg2) {
		return(arg1 == arg2);
	}
	
	public static boolean bool_equals(boolean arg1,boolean arg2) {
		return(arg1 == arg2);
	}
	
	public static boolean int_gt(int arg1,int arg2) {
		return(arg1 > arg2);
	}
	public static boolean bool_gt(boolean arg1,boolean arg2) {
		if((arg1 == true) && (arg2 == false))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean int_lt(int arg1,int arg2) {
		return(arg1 < arg2);
	}

	public static boolean bool_lt(boolean arg1,boolean arg2) {
		if((arg1 == false) && (arg2 == true))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean int_not_equals(int arg1,int arg2) {
		return(arg1 != arg2);
	}
	
	public static boolean string_lt(String arg1,String arg2) {
		return(arg2.startsWith(arg1));
	}
	
	public static boolean string_equals(String arg1,String arg2) {
		return(arg1.equals(arg2));
	}


	
}
