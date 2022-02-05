
package com.dev.mylang.barlang.stage3.ast;



public interface IMutableGlobal extends IDeclaration
{
  INameDef getVarDef();
  IExpression getExpression();

} 
