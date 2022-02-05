package com.dev.mylang.barlang.stage5;


import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.dev.mylang.barlang.stage1.barTokenKinds.Kind;
import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IstageStatement;
import com.dev.mylang.barlang.stage3.ast.IBinaryExpression;
import com.dev.mylang.barlang.stage3.ast.IBlock;
import com.dev.mylang.barlang.stage3.ast.IBooleanLiteralExpression;
import com.dev.mylang.barlang.stage3.ast.IDeclaration;
import com.dev.mylang.barlang.stage3.ast.IExpression;
import com.dev.mylang.barlang.stage3.ast.IExpressionStatement;
import com.dev.mylang.barlang.stage3.ast.IFunctionCallExpression;
import com.dev.mylang.barlang.stage3.ast.IFunctionDeclaration;
import com.dev.mylang.barlang.stage3.ast.IIdentExpression;
import com.dev.mylang.barlang.stage3.ast.IIdentifier;
import com.dev.mylang.barlang.stage3.ast.IIfStatement;
import com.dev.mylang.barlang.stage3.ast.IImmutableGlobal;
import com.dev.mylang.barlang.stage3.ast.IIntLiteralExpression;
import com.dev.mylang.barlang.stage3.ast.ILetStatement;
import com.dev.mylang.barlang.stage3.ast.IListSelectorExpression;
import com.dev.mylang.barlang.stage3.ast.IListType;
import com.dev.mylang.barlang.stage3.ast.IMutableGlobal;
import com.dev.mylang.barlang.stage3.ast.INameDef;
import com.dev.mylang.barlang.stage3.ast.INilConstantExpression;
import com.dev.mylang.barlang.stage3.ast.IPrimitiveType;
import com.dev.mylang.barlang.stage3.ast.IProgram;
import com.dev.mylang.barlang.stage3.ast.IReturnStatement;
import com.dev.mylang.barlang.stage3.ast.IStatement;
import com.dev.mylang.barlang.stage3.ast.IStringLiteralExpression;
import com.dev.mylang.barlang.stage3.ast.ISwitchStatement;
import com.dev.mylang.barlang.stage3.ast.IType;
import com.dev.mylang.barlang.stage3.ast.IUnaryExpression;
import com.dev.mylang.barlang.stage3.ast.IWhileStatement;
import com.dev.mylang.barlang.stage3.astimpl.Type__;


public class StarterCodeGenVisitor implements ASTVisitor, Opcodes {
	
	public StarterCodeGenVisitor(String className, String packageName, String sourceFileName){
		this.className = className;
		this.packageName = packageName;	
		this.sourceFileName = sourceFileName;
	}
	

	ClassWriter cw;
	String className;
	String packageName;
	String classDesc;
	String sourceFileName; //



	public static final String stringClass = "java/lang/String";
	public static final String stringDesc = "Ljava/lang/String;";
	public static final String listClass = "java/util/ArrayList";
	public static final String listDesc = "Ljava/util/ArrayList;";
	public static final String runtimeClass = "com/dev/mylang/barlang/stage5/Runtime";
	
	
	
	/* Records for information passed to children, namely the methodVisitor and information about current methods Local Variables */
	record LocalVarInfo(String name, String typeDesc, Label start, Label end) {}
	record MethodVisitorLocalVarTable(MethodVisitor mv, List<LocalVarInfo> localVars) {};	

	/*  Adds local variables to a method
	 *  The information about local variables to add has been collected in the localVars table.  
	 *  This method should be invoked after all instructions for the method have been generated, immediately before invoking mv.visitMaxs.
	 */
	private void addLocals(MethodVisitorLocalVarTable arg, Label start, Label end) {
		MethodVisitor mv = arg.mv;
		List<LocalVarInfo> localVars = arg.localVars();
		for (int slot = 0; slot < localVars.size(); slot++) {
			LocalVarInfo varInfo = localVars.get(slot);
			String varName = varInfo.name;
			String localVarDesc = varInfo.typeDesc;
			Label range0 = varInfo.start == null ? start : varInfo.start;
		    Label range1 = varInfo.end == null ? end : varInfo.end;
		    mv.visitLocalVariable(varName, localVarDesc, null, range0, range1, slot);
		}
	}

	@Override
	public Object visitIBinaryExpression(IBinaryExpression n, Object arg) throws Exception {
		//get method visitor from arg
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		//generate code to leave value of expression on top of stack
		n.getLeft().visit(this, arg);
		n.getRight().visit(this, arg);
		//get the operator and types of operand and result
		Kind op = n.getOp();
		IType resultType = n.getType();
		IType operandType = n.getRight().getType();
		switch(op) {
		case PLUS -> {
			// TODO - add operand check ?
			if(operandType.isInt()) {
				
				mv.visitInsn(IADD);
			}
			if(operandType.isString()) {
				
//				mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//				mv.visitInsn(DUP);
//				mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
				mv.visitMethodInsn(INVOKEVIRTUAL, stringClass, "concat", "(Ljava/lang/String;)Ljava/lang/String;",false);
			}
		}
		case MINUS -> {
			if(operandType.isInt()) {
				mv.visitInsn(ISUB);
			}
		}
		case TIMES -> {
			if(operandType.isInt()) {
				mv.visitInsn(IMUL);
			}
		}
		case DIV -> {
			if(operandType.isInt()) {
				mv.visitInsn(IDIV);
			}
		}
		case AND -> {
			if(operandType.isBoolean()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "and", "(ZZ)Z",false);
			}
		}
		case OR -> {
			if(operandType.isBoolean()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "or", "(ZZ)Z",false);
			}
		}
		case EQUALS -> {
			if(operandType.isInt()) {	
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "int_equals", "(II)Z",false);
			}
			if(operandType.isBoolean()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "bool_equals", "(ZZ)Z",false);
			}
			if(operandType.isString()) {
				mv.visitMethodInsn(INVOKEVIRTUAL, stringClass, "equals", "(Ljava/lang/Object;)Z", false);
			}
		}

		case NOT_EQUALS -> {
			if(operandType.isInt()) {	
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "int_not_equals", "(II)Z",false);
			}
			if(operandType.isBoolean()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "bool_equals", "(ZZ)Z",false);
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "not", "(Z)Z",false);
			}
			if(operandType.isString()) {
				mv.visitMethodInsn(INVOKEVIRTUAL, stringClass, "equals", "(Ljava/lang/Object;)Z", false);
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "not", "(Z)Z",false);				
			}
		}
		case LT -> {
			if(operandType.isInt()) {	
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "int_lt", "(II)Z",false);
			}
			if(operandType.isBoolean()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "bool_lt", "(ZZ)Z",false);
			}
			if(operandType.isString()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "string_lt", "(Ljava/lang/String;Ljava/lang/String;)Z",false);		
			}
		}
		case GT -> {
			if(operandType.isInt()) {	
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "int_gt", "(II)Z",false);
			}
			if(operandType.isBoolean()) {
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "bool_gt", "(ZZ)Z",false);
			}
			if(operandType.isString()) {
				mv.visitMethodInsn(INVOKEVIRTUAL, stringClass, "startsWith", "(Ljava/lang/String;)Z", false);
			}
		}
		default -> throw new UnsupportedOperationException("compiler error");
		}
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}


	@Override
	public Object visitIBlock(IBlock n, Object arg) throws Exception {
		List<IStatement> statements = n.getStatements();
		for(IStatement statement: statements) {
			statement.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitIBooleanLiteralExpression(IBooleanLiteralExpression n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		mv.visitLdcInsn(n.getValue());
		return null;
	}


	
	@Override
	public Object visitIFunctionDeclaration(IFunctionDeclaration n, Object arg) throws Exception {
		String name = n.getName().getName();

		//Local var table
		List<LocalVarInfo> localVars = new ArrayList<LocalVarInfo>();
		//Add args to local var table while constructing type desc.
		List<INameDef> args = n.getArgs();

		//Iterate over the parameter list and build the function descriptor
		//Also assign and store slot numbers for parameters
		StringBuilder sb = new StringBuilder();	
		sb.append("(");
		for( INameDef def: args) {
			String desc = def.getType().getDesc();
			sb.append(desc);
			def.getIdent().setSlot(localVars.size());
			localVars.add(new LocalVarInfo(def.getIdent().getName(), desc, null, null));
		}
		sb.append(")");
		sb.append(n.getResultType().getDesc());
		String desc = sb.toString();
		
		// get method visitor
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, name, desc, null, null);
		// initialize
		mv.visitCode();
		// mark beginning of instructions for method
		Label funcStart = new Label();
		mv.visitLabel(funcStart);
		MethodVisitorLocalVarTable context = new MethodVisitorLocalVarTable(mv, localVars);
		//visit block to generate code for statements
		n.getBlock().visit(this, context);
		
		//add return instruction if Void return type
		if(n.getResultType().equals(Type__.voidType)) {
			mv.visitInsn(RETURN);
		}
		
		//add label after last instruction
		Label funcEnd = new Label();
		mv.visitLabel(funcEnd);
		
		addLocals(context, funcStart, funcEnd);

		mv.visitMaxs(0, 0);
		
		//terminate construction of method
		mv.visitEnd();
		return null;

	}




	@Override
	public Object visitIFunctionCallExpression(IFunctionCallExpression n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		String name = n.getName().getName();
		StringBuilder sb = new StringBuilder();	
		sb.append("(");
		for(IExpression e : n.getArgs()) {			
			e.visit(this, arg);
			String desc = e.getType().getDesc();
			sb.append(desc);
		}		
		sb.append(")");
		IFunctionDeclaration declr = ((IFunctionDeclaration)(n.getName().getDec()));
		sb.append(declr.getResultType().getDesc());
		String desc = sb.toString();
		mv.visitMethodInsn(INVOKESTATIC, className, name, desc, false);
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public Object visitIIdentExpression(IIdentExpression n, Object arg) throws Exception {
		//TODO complete the static load
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		IIdentifier ident = n.getName();
		String varName = ident.getName();
		String typeDesc = "";
		IDeclaration dec = ident.getDec();
		int slot = (int)ident.visit(this, arg);
		if(slot > -1)
		{
			if (((INameDef)dec).getType().isInt() || ((INameDef)dec).getType().isBoolean()){
				mv.visitVarInsn(ILOAD, slot);				
			}
			else {
				mv.visitVarInsn(ALOAD, slot);				
			}
			
		}
		else
		{
			if(dec instanceof IMutableGlobal)
			{
				typeDesc = ((IMutableGlobal)dec).getVarDef().getType().getDesc();
			}
			if(dec instanceof IImmutableGlobal)
			{
				typeDesc = ((IImmutableGlobal)dec).getVarDef().getType().getDesc();
			}			
//			String typeDesc = ident.getDec().getType().getDesc();
			mv.visitFieldInsn(GETSTATIC, className, varName, typeDesc);	
		}
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public Object visitIIdentifier(IIdentifier n, Object arg) throws Exception {
		IDeclaration dec = n.getDec();
		int retval = -1;

		if(dec instanceof INameDef)
		{
			retval = ((INameDef)n.getDec()).getIdent().getSlot();
		}
		return retval;
		
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public Object visitIIfStatement(IIfStatement n, Object arg) throws Exception {
		
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;	
		IExpression guard = n.getGuardExpression();
		guard.visit(this, arg);
		Label l0 = new Label();
		mv.visitJumpInsn(IFEQ, l0);
		IBlock block = n.getBlock();
		block.visit(this, arg);
		mv.visitLabel(l0);
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public Object visitIImmutableGlobal(IImmutableGlobal n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;				
		INameDef nameDef = n.getVarDef();
		String varName = nameDef.getIdent().getName();
		String typeDesc = nameDef.getType().getDesc();
		FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, varName, typeDesc, null, null);
		fieldVisitor.visitEnd();
		//generate code to initialize field.  
		IExpression e = n.getExpression();
		e.visit(this, arg);  //generate code to leave value of expression on top of stack
		mv.visitFieldInsn(PUTSTATIC, className, varName, typeDesc);	
		return null;
	}

	@Override
	public Object visitIIntLiteralExpression(IIntLiteralExpression n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		mv.visitLdcInsn(n.getValue());
		return null;
	}

	@Override
	public Object visitILetStatement(ILetStatement n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		INameDef dec = n.getLocalDef();
		dec.visit(this, arg);
		if(n.getExpression() != null) {
			n.getExpression().visit(this, arg);	
			if (dec.getType().isInt() || dec.getType().isBoolean()){
				mv.visitVarInsn(ISTORE, dec.getIdent().getSlot());				
			}
			else {
				mv.visitVarInsn(ASTORE, dec.getIdent().getSlot());				
			}			
		}
		n.getBlock().visit(this, arg);
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}
		


	@Override
	public Object visitIListSelectorExpression(IListSelectorExpression n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS");
	}

	@Override
	public Object visitIListType(IListType n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS!!");
	}

	@Override
	public Object visitINameDef(INameDef n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		List<LocalVarInfo> localVars = ((MethodVisitorLocalVarTable)arg).localVars;
		String desc = n.getType().getDesc();
		String varName = n.getIdent().getName();
		IType varType = n.getType();
		n.getIdent().setSlot(localVars.size());
		localVars.add(new LocalVarInfo(n.getIdent().getName(), desc, null, null));
		return null;
//		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitINilConstantExpression(INilConstantExpression n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS");
	}

	@Override
	public Object visitIProgram(IProgram n, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		/*
		 * If the call to mv.visitMaxs(1, 1) crashes, it is sometime helpful to temporarily try it without COMPUTE_FRAMES. You won't get a runnable class file
		 * but you can at least see the bytecode that is being generated. 
		 */
//	    cw = new ClassWriter(0); 
		classDesc = "L" + className + ";";
		cw.visit(V16, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		if (sourceFileName != null) cw.visitSource(sourceFileName, null);
		
		// create MethodVisitor for <clinit>  
		//  This method is the static initializer for the class and contains code to initialize global variables.
		// get a MethodVisitor
		MethodVisitor clmv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "<clinit>", "()V", null, null);
		// visit the code first
		clmv.visitCode();
		//mark the beginning of the code
		Label clinitStart = new Label();
		clmv.visitLabel(clinitStart);
		//create a list to hold local var info.  This will remain empty for <clinit> but is shown for completeness.  Methods with local variable need this.
		List<LocalVarInfo> initializerLocalVars = new ArrayList<LocalVarInfo>();
		//pair the local var infor and method visitor to pass into visit routines
		MethodVisitorLocalVarTable clinitArg = new MethodVisitorLocalVarTable(clmv,initializerLocalVars);
		//visit all the declarations. 
		List<IDeclaration> decs = n.getDeclarations();
		for (IDeclaration dec : decs) {
			dec.visit(this, clinitArg);  //argument contains local variable info and the method visitor.  
		}
		//add a return method
		clmv.visitInsn(RETURN);
		//mark the end of the bytecode for <clinit>
		Label clinitEnd = new Label();
		clmv.visitLabel(clinitEnd);
		//add the locals to the class
		addLocals(clinitArg, clinitStart, clinitEnd);  //shown for completeness.  There shouldn't be any local variables in clinit.
		//required call of visitMaxs.  Since we created the ClassWriter with  COMPUTE_FRAMES, the parameter values don't matter. 
		clmv.visitMaxs(0, 0);
		//finish the method
		clmv.visitEnd();
	
		//finish the clas
		cw.visitEnd();

		//generate classfile as byte array and return
		return cw.toByteArray();
	}

	@Override
	public Object visitIReturnStatement(IReturnStatement n, Object arg) throws Exception {
		//get the method visitor from the arg
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		IExpression e = n.getExpression();
		if (e != null) {  //the return statement has an expression
			e.visit(this, arg);  //generate code to leave value of expression on top of stack.
			//use type of expression to determine which return instruction to use
			IType type = e.getType();
			if (type.isInt() || type.isBoolean()) {mv.visitInsn(IRETURN);}
			else  {mv.visitInsn(ARETURN);}
		}
		else { //there is no argument, (and we have verified duirng type checking that function has void return type) so use this return statement.  
			mv.visitInsn(RETURN);
		}
		return null;
	}

	@Override
	public Object visitIStringLiteralExpression(IStringLiteralExpression n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		mv.visitLdcInsn(n.getValue());
		return null;
		//throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public Object visitISwitchStatement(ISwitchStatement n, Object arg) throws Exception {
		throw new UnsupportedOperationException("SKIP THIS");

	}

	@Override
	public Object visitIUnaryExpression(IUnaryExpression n, Object arg) throws Exception {
		//get method visitor from arg
		MethodVisitor mv = ((MethodVisitorLocalVarTable) arg).mv();
		//generate code to leave value of expression on top of stack
		n.getExpression().visit(this, arg);
		//get the operator and types of operand and result
		Kind op = n.getOp();
		IType resultType = n.getType();
		IType operandType = n.getExpression().getType();
		switch(op) {
		case MINUS -> {
			// TODO - add operand check ?
			mv.visitInsn(INEG);
			//throw new UnsupportedOperationException("IMPLEMENT unary minus");
		}
		case BANG -> {
			if (operandType.isBoolean()) {
				//this is complicated.  Use a Java method instead
//				Label brLabel = new Label();
//				Label after = new Label();
//				mv.visitJumpInsn(IFEQ,brLabel);
//				mv.visitLdcInsn(0);
//				mv.visitJumpInsn(GOTO,after);
//				mv.visitLabel(brLabel);
//				mv.visitLdcInsn(1);
//				mv.visitLabel(after);
				mv.visitMethodInsn(INVOKESTATIC, runtimeClass, "not", "(Z)Z",false);
			}
			else { //argument is List
				throw new UnsupportedOperationException("SKIP THIS");
		}
		}
		default -> throw new UnsupportedOperationException("compiler error");
		}
		return null;
	}

	@Override
	public Object visitIWhileStatement(IWhileStatement n, Object arg) throws Exception {
		
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		IBlock b = n.getBlock();
		IExpression e = n.getGuardExpression();
		
		Label l0 = new Label();
		mv.visitLabel(l0);
		Label l1 = new Label();
		//add code
		e.visit(this, arg);
		mv.visitJumpInsn(IFEQ, l1);
		b.visit(this, arg);
		mv.visitJumpInsn(GOTO, l0);
		mv.visitLabel(l1);
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}


	@Override
	public Object visitIMutableGlobal(IMutableGlobal n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;				
		INameDef nameDef = n.getVarDef();
		String varName = nameDef.getIdent().getName();
		String typeDesc = nameDef.getType().getDesc();
		FieldVisitor fieldVisitor = cw.visitField(ACC_PUBLIC | ACC_STATIC, varName, typeDesc, null, null);
		fieldVisitor.visitEnd();
		//generate code to initialize field.  
		IExpression e = n.getExpression();
		if(e != null){
			e.visit(this, arg);  //generate code to leave value of expression on top of stack
			mv.visitFieldInsn(PUTSTATIC, className, varName, typeDesc);	
		}		
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
	}

	@Override
	public Object visitIPrimitiveType(IPrimitiveType n, Object arg) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}


	@Override
	public Object visitIstageStatement(IstageStatement n, Object arg) throws Exception {
		MethodVisitor mv = ((MethodVisitorLocalVarTable)arg).mv;
		IExpression leftExpression = n.getLeft();
		IExpression rightExpression = n.getRight();
		if (leftExpression instanceof IFunctionCallExpression) {
			leftExpression.visit(this, arg);
		} else {
			rightExpression.visit(this,arg);
			IIdentifier ident = ((IIdentExpression)leftExpression).getName();
			String varName = ident.getName();
			String typeDesc = "";
			IDeclaration dec = ident.getDec();
			int slot = (int)ident.visit(this, arg);
			if(slot > -1)
			{
				if (((INameDef)dec).getType().isInt() || ((INameDef)dec).getType().isBoolean()){
					mv.visitVarInsn(ISTORE, slot);				
				}
				else {
					mv.visitVarInsn(ASTORE, slot);				
				}
//				mv.visitVarInsn(ISTORE, slot);
			}
			else
			{
				if(dec instanceof IMutableGlobal)
				{
					typeDesc = ((IMutableGlobal)dec).getVarDef().getType().getDesc();
				}
				if(dec instanceof IImmutableGlobal)
				{
					typeDesc = ((IImmutableGlobal)dec).getVarDef().getType().getDesc();
				}			
//				String typeDesc = ident.getDec().getType().getDesc();
				mv.visitFieldInsn(PUTSTATIC, className, varName, typeDesc);	
		    }
		}
		return null;
//		throw new UnsupportedOperationException("TO IMPLEMENT");
//		return

	}

	@Override
	public Object visitIExpressionStatement(IExpressionStatement n, Object arg) throws Exception {
		throw new UnsupportedOperationException("TO IMPLEMENT");
	}
}
