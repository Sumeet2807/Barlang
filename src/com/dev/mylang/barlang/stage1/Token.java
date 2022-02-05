package com.dev.mylang.barlang.stage1;

public class Token implements IbarToken {
	
	Kind mykind;
	int myline;
	String mytext;
	int mycharpos;
	
	
	public Kind getKind(){
		return mykind;		
		
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return mytext;
	}

	@Override
	public int getLine() {
		// TODO Auto-generated method stub
		return myline;
	}

	@Override
	public int getCharPositionInLine() {
		// TODO Auto-generated method stub
		return mycharpos;
	}

	@Override
	public String getStringValue() {
		int index = 0;
		String new_string = "";
		while(index <= mytext.length() - 2)
		{
			if(index == 0) {
				index++;
				continue;
			}
			if("\\".indexOf(mytext.charAt(index)) != -1)
			{
				if("b".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\b';					
				}
				else if("t".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\t';					
				}
				else if("n".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\n';					
				}
				else if("r".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\r';					
				}
				else if("f".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\f';					
				}
				else if("\"".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '"';					
				}
				else if("'".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\'';					
				}
				else if("\\".indexOf(mytext.charAt(index+1)) != -1)
				{
					new_string = new_string + '\\';					
				}
				index = index + 2;
			
			}
			else
			{
				new_string = new_string + mytext.charAt(index);
				index++;
			}
		}
		

		// TODO Auto-generated method stub
		return new_string;
	}

	@Override
	public int getIntValue() {
		int ret_val = 0;
		if (mykind == barTokenKinds.Kind.INT_LITERAL)
		{
			ret_val = Integer.parseInt(mytext);		
		}
		return ret_val;
	}
}