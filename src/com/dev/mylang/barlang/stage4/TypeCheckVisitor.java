package com.dev.mylang.barlang.stage4;

import java.util.List;

import com.dev.mylang.barlang.stage1.barTokenKinds.Kind;
import com.dev.mylang.barlang.stage3.ast.ASTVisitor;
import com.dev.mylang.barlang.stage3.ast.IASTNode;
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
import com.dev.mylang.barlang.stage3.ast.IType.TypeKind;
import com.dev.mylang.barlang.stage3.ast.IUnaryExpression;
import com.dev.mylang.barlang.stage3.ast.IWhileStatement;
import com.dev.mylang.barlang.stage3.astimpl.ListType__;
import com.dev.mylang.barlang.stage3.astimpl.PrimitiveType__;
import com.dev.mylang.barlang.stage3.astimpl.Type__;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String m) {
			super(m);
		}
	}

	SymbolTable symtab = new SymbolTable();

	private void check(boolean b, IASTNode n, String message) throws TypeCheckException {
		if (!b) {
			if (n.getLine() > 0) {
				message = n.getLine() + ":" + n.getPosInLine() + " " + n.getText() + "\n" + message;
			}
			throw new TypeCheckException(message);
		}
	}

	@Override
	public Object visitIBinaryExpression(IBinaryExpression n, Object arg) throws Exception {
		
		IExpression l = n.getLeft();
		IExpression r = n.getRight();
		IType lType = (IType) l.visit(this, arg);
		IType rType = (IType) r.visit(this, arg);
		Kind op = n.getOp();
		boolean list_same = false;
		
		if (lType.isList() && rType.isList())
		{
			IListType left_list_type = (IListType) lType;
			IListType right_list_type = (IListType) rType;
			
			if(left_list_type.getElementType().equals(right_list_type.getElementType()))
			{
				list_same = true;
			}
			
		}
		if(lType.equals(rType) || list_same) {
			
			if((op == Kind.EQUALS) || (op == Kind.NOT_EQUALS) || (op == Kind.GT) || (op == Kind.LT))
			{
				n.setType(PrimitiveType__.booleanType);
			}
			else if(op == Kind.PLUS && (lType.isList() || lType.isInt() || lType.isString())) 
			{
				n.setType(rType);	
			}
			else if(lType.isInt() && (op == Kind.MINUS || op == Kind.TIMES || op == Kind.DIV ))
			{
				n.setType(PrimitiveType__.intType);
			}
			else if(lType.isBoolean() && (op == Kind.AND || op == Kind.OR))
			{
				n.setType(PrimitiveType__.booleanType);
			}
			else
			{
				check(false, n, "Illegal operator or expression type in Binary expression");
			}
			
		}
		else {
			check(false, n, "Type mismatch in binary expression");			
		}
		return n.getType();
		//TODO
//		throw new UnsupportedOperationException("IMPLEMENT ME!");
		
	}

	/**
	 * arg is enclosing function declaration
	 */
	@Override
	public Object visitIBlock(IBlock n, Object arg) throws Exception {
		List<IStatement> statements = n.getStatements();
		for (IStatement statement : statements) {
			statement.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitIBooleanLiteralExpression(IBooleanLiteralExpression n, Object arg) throws Exception {
		IType type = PrimitiveType__.booleanType;
		n.setType(type);
		return type;
		//TODO
//		throw new UnsupportedOperationException("IMPLEMENT ME!");
	}

	@Override
	public Object visitIFunctionDeclaration(IFunctionDeclaration n, Object arg) throws Exception {
		String name = n.getName().getName();
		IType resultType = n.getResultType();
		if (resultType != null) {
			resultType.visit(this, null);
			check(fullyTyped(resultType), n, "result type cannot be partially defined");
		} else {
			n.setType(Type__.voidType);
		}
		check(symtab.insert(name, n), n, name + " already declared in scope");
		symtab.enterScope();
		for (INameDef argument : n.getArgs()) {
			IType argType = (IType) argument.visit(this, argument);
			check(fullyTyped(argType), argument, "Type of argument must be defined");
		}
		n.getBlock().visit(this, n);
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitIFunctionCallExpression(IFunctionCallExpression n, Object arg) throws Exception {
		IIdentifier name = n.getName();
		IDeclaration dec = (IDeclaration) name.visit(this, null);
		check(dec instanceof IFunctionDeclaration, n, name.getName() + " is not declared or is not a function");
		IFunctionDeclaration fdec = (IFunctionDeclaration) dec;
		List<INameDef> formalArgDecs = fdec.getArgs();
		List<IExpression> actualArgs = n.getArgs();
		check(formalArgDecs.size() == actualArgs.size(), n, "formal and actual parameter lists have different lengths");
		for (int i = 0; i < actualArgs.size(); i++) {
			IType actualType = (IType) actualArgs.get(i).visit(this, arg);
			IType formalType = formalArgDecs.get(i).getType();
			check(compatiblestageTypes(formalType, actualType), actualArgs.get(i),
					"types of actual and formal parameter " + formalArgDecs.get(i).getIdent().getName()
							+ "are inconsistent");
		}
		IType resultType = fdec.getResultType();
		n.setType(resultType);
		return resultType;
	}

	@Override
	public Object visitIIdentExpression(IIdentExpression n, Object arg) throws Exception {
		IIdentifier name = n.getName();
		IDeclaration dec = (IDeclaration) name.visit(this, null);
		IType type = getType(dec);
		check(type != Type__.undefinedType, n, "Identifier " + name + " does not have defined type");
		n.setType(type);
		return type;
	}

	@Override
	public Object visitIIfStatement(IIfStatement n, Object arg) throws Exception {
		//TODO
		IExpression guard = n.getGuardExpression();
		IType guardType = (IType) guard.visit(this, arg);
		check(guardType.isBoolean(), n, "Guard expression type not boolean");
		IBlock block = n.getBlock();
		block.visit(this, arg);
		return arg;		
		
		
//		throw new UnsupportedOperationException("IMPLEMENT ME!");
	}

	@Override
	public Object visitIImmutableGlobal(IImmutableGlobal n, Object arg) throws Exception {
		IExpression expression = n.getExpression(); // IIMutableGlobal must have initalizing expression
		IType expressionType = (IType) expression.visit(this, arg);
		INameDef nameDef = n.getVarDef();
		IType declaredType = (IType) nameDef.visit(this, n);
		IType inferredType = unifyAndCheck(declaredType, expressionType, n);
		nameDef.setType(inferredType);
		return null;
	}

	@Override
	public Object visitIIntLiteralExpression(IIntLiteralExpression n, Object arg) throws Exception {
		IType type = PrimitiveType__.intType;
		n.setType(type);
		return type;
	}

	/**
	 * arg is enclosing Function declaration
	 */
	@Override
	public Object visitILetStatement(ILetStatement n, Object arg) throws Exception {
		IExpression expression = n.getExpression();	
		IType expressionType = expression != null ? (IType) expression.visit(this, arg) : Type__.undefinedType;
		IBlock b = n.getBlock();
		INameDef def = n.getLocalDef();
		
		symtab.enterScope();
		IType declaredType = (IType) def.visit(this, def);
		IType inferredType = unifyAndCheck(declaredType, expressionType, n);
		def.setType(inferredType);
		b.visit(this, arg);
		symtab.leaveScope();
		return arg;
		
		//TODO
		
//		throw new UnsupportedOperationException("IMPLEMENT ME!");
	}

	@Override
	public Object visitIListSelectorExpression(IListSelectorExpression n, Object arg) throws Exception {
		IDeclaration dec = (IDeclaration) n.getName().visit(this, arg);
		IType type = getType(dec);
		check(type.isList(), n, "invalid or missing type for list ");
		IListType ltype = (IListType) type;
		IType indexType = (IType) n.getIndex().visit(this, null);
		check(indexType.isInt(), n, "index selector must be int");
		IType elementType = ltype.getElementType();
		n.setType(elementType);
		return elementType;
	}

	IType getType(IDeclaration dec) {
		if (dec == null) {
			return null;
		}
		if (dec instanceof IMutableGlobal mg) {
			return mg.getVarDef().getType();
		}
		if (dec instanceof IImmutableGlobal mg) {
			return mg.getVarDef().getType();
		}
		if (dec instanceof INameDef nd) {
			return nd.getType();
		}
		if (dec instanceof IFunctionDeclaration fd) {
			return fd.getResultType();
		}
		return null;
	}

	@Override
	public Object visitIListType(IListType n, Object arg) throws Exception {
		IType elementType = n.getElementType();
		IType inferredElemType = elementType != null ? (IType) elementType.visit(this, null) : Type__.undefinedType;
		return new ListType__(n.getLine(), n.getPosInLine(), "", inferredElemType);
	}

	/**
	 * The enclosing declaration should be passed in as argument. If this is an
	 * IMutableGlobal or IImmutableGlobal, the declaration object is is inserted
	 * into the symbol table as the declaration. If the enclosing declaration is a
	 * Function declaration (and this is a formal parameter) or a Let statement (and
	 * this is a local variable declaration), then this NameDef object is the
	 * Declaration in the symbol table.
	 */
	@Override
	public Object visitINameDef(INameDef n, Object arg) throws Exception {
		String name = n.getIdent().getName();
		IType type = (IType) n.getType();
		IType varType = type != null ? (IType) type.visit(this, null) : Type__.undefinedType;
		if (arg instanceof IMutableGlobal || arg instanceof IImmutableGlobal) {
			check(symtab.insert(name, (IDeclaration) arg), n, "Variable " + name + "already declared in this scope");
		} else {
			check(symtab.insert(name, n), n, "Variable " + name + "already declared in this scope");
		}
		return varType;
	}

	@Override
	public Object visitINilConstantExpression(INilConstantExpression n, Object arg) throws Exception {
		n.setType(Type__.nilType);
		return n.getType();
	}

	@Override
	public Object visitIProgram(IProgram n, Object arg) throws Exception {
		List<IDeclaration> decs = n.getDeclarations();
		for (IDeclaration dec : decs) {
			dec.visit(this, symtab);
		}
		return n;
	}

	/**
	 * arg is enclosing function definition
	 */
	@Override
	public Object visitIReturnStatement(IReturnStatement n, Object arg) throws Exception {
		
		IExpression e = n.getExpression();
		IType eType = (IType) e.visit(this, arg);
		IType fType = ((IFunctionDeclaration) arg).getResultType();
		
		check(compatiblestageTypes(fType, eType), n, "incompatible types in Return Statement");
		
		//TODO
//		throw new UnsupportedOperationException("IMPLEMENT ME!");
		
		return arg;
	}

	@Override
	public Object visitIStringLiteralExpression(IStringLiteralExpression n, Object arg) throws Exception {
		IType type = PrimitiveType__.stringType;
		n.setType(type);
		return type;
	}

	boolean compatiblestageTypes(IType declared, IType actual) {
		if (declared instanceof IListType && actual instanceof INilConstantExpression) {
			return true;
		}
		if (declared instanceof IListType && actual instanceof IListType) {
			return compatiblestageTypes(((IListType) declared).getElementType(),
					((IListType) actual).getElementType());
		}
		return declared.equals(actual);
	}

	boolean isConstantExpression(IExpression expression) {
		if (expression instanceof IIdentExpression e) {
			String name = ((IIdentExpression) expression).getName().getName();
			IDeclaration dec = symtab.lookupDec(name);
			return ! isMutable(dec);
		} else
			return (expression instanceof IBooleanLiteralExpression) || (expression instanceof IIntLiteralExpression)
					|| (expression instanceof IStringLiteralExpression)
					|| (expression instanceof INilConstantExpression);
	}

	/**
	 * arg is enclosing function definition
	 */
	@Override
	public Object visitISwitchStatement(ISwitchStatement n, Object arg) throws Exception {
		IExpression switchExpression = n.getSwitchExpression();
		List<IExpression> branchExpressions = n.getBranchExpressions();
		List<IBlock> blocks = n.getBlocks();
		IBlock defaultBlock = n.getDefaultBlock();
		IType bType = null;
		IType sType = (IType) switchExpression.visit(this, arg);
		if(sType.isInt() ||  sType.isBoolean() ||  sType.isString()) {			
		}
		else {
			check(false, n, "Illegal type in Switch expression guard expression");
		}	
		
		for (int i = 0; i < branchExpressions.size(); i++) {
			bType = (IType) branchExpressions.get(i).visit(this, arg);
			check((compatiblestageTypes(sType, bType) && isConstantExpression(branchExpressions.get(i))), n, "incompatible types in Switch");
			blocks.get(i).visit(this, arg);
		}
		defaultBlock.visit(this, arg);
		
		//TODO
//		throw new UnsupportedOperationException("IMPLEMENT ME!");
		return arg;
	}

	@Override
	public Object visitIUnaryExpression(IUnaryExpression n, Object arg) throws Exception {
		IExpression e = n.getExpression();
		IType eType = (IType) e.visit(this, arg);
		Kind op = n.getOp();
		if (op == Kind.MINUS && eType.isInt()) {
			n.setType(PrimitiveType__.intType);
		} else if (op == Kind.MINUS && eType.isList()) {
			// tail of list, result is same type of list as argument
			n.setType(eType);
		} else if (op == Kind.BANG && eType.isBoolean()) {
			n.setType(PrimitiveType__.booleanType);
		} else if (op == Kind.BANG && eType.isList()) {
			IListType listType = (IListType) eType;
			IType elementType = listType.getElementType();
			n.setType(elementType);
		} else {
			// not a legal case
			check(false, n, "Illegal operator or expression type in unary expression");
		}
		return n.getType();

	}

	/**
	 * arg is enclosing function declaration
	 */
	@Override
	public Object visitIWhileStatement(IWhileStatement n, Object arg) throws Exception {
		IExpression guard = n.getGuardExpression();
		IType guardType = (IType) guard.visit(this, arg);
		check(guardType.isBoolean(), n, "Guard expression type not boolean");
		IBlock block = n.getBlock();
		block.visit(this, arg);
		return arg;
	}

	@Override
	public Object visitIMutableGlobal(IMutableGlobal n, Object arg) throws Exception {
		IExpression expression = n.getExpression();
		IType expressionType = expression != null ? (IType) expression.visit(this, arg) : Type__.undefinedType;
		INameDef def = n.getVarDef();
		IType declaredType = (IType) def.visit(this, n);
		IType inferredType = unifyAndCheck(declaredType, expressionType, n);
		def.setType(inferredType);
		return null;
	}

	IType unifyAndCheck(IType declaredType, IType expressionType, IASTNode n) throws TypeCheckException {
		boolean fullyDefinedExpr = fullyTyped(expressionType);
		boolean fullyDefinedDec = fullyTyped(declaredType);
		IType inferredType;
		check(fullyDefinedDec || fullyDefinedExpr, n, "type must be inferrable from declaration or initializer");
		if (fullyDefinedDec && fullyDefinedExpr) {
			check(compatiblestageTypes(declaredType, expressionType), n, "incompatible types in declaration");
			inferredType = declaredType;
		} else if (expressionType.equals(Type__.undefinedType)) {
			inferredType = declaredType;
		} else if (expressionType.equals(Type__.nilType)) {
			check(declaredType.isList(), n, "NIL is not compatible with declared type");
			inferredType = declaredType;
		} else if (declaredType.equals(Type__.undefinedType)) {
			inferredType = expressionType;
		} else {
			check(declaredType.isList(), n, "invalid type");
			check(expressionType.isList(), expressionType, "incompatible expression type");
			IListType d = (IListType) declaredType;
			IType e = expressionType;
			while (! d.getElementType().equals(Type__.undefinedType)) {
				d = (IListType) d.getElementType();
				check(e.isList(), expressionType, "incompatible expression type");
				e = ((IListType) e).getElementType();
			}
			inferredType = e;
		}
		return inferredType;
	}

	boolean fullyTyped(IType type) {
		if (type.equals(Type__.undefinedType) || type.equals(Type__.nilType)) {
			return false;
		}
		if (type.isList()) {
			IType t = type;
			while (t instanceof IListType) {
				t = ((IListType) t).getElementType();
				if (t.equals(Type__.undefinedType)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public Object visitIPrimitiveType(IPrimitiveType n, Object arg) throws Exception {
		return n;
	}

	@Override
	public Object visitIstageStatement(IstageStatement n, Object arg) throws Exception {
		IExpression leftExpression = n.getLeft();
		IType leftType = (IType) leftExpression.visit(this, arg);
		IExpression rightExpression = n.getRight();
		if (leftExpression instanceof IFunctionCallExpression) {
			check(rightExpression == null, n, "cannot assign to function");
		} else {
			check(isMutable(leftExpression), n, "attempting to assign to immutable variable");
		}
		if (rightExpression != null) {
			IType rightType = (IType) rightExpression.visit(this, arg);
			check(compatiblestageTypes(leftType, rightType), n, "incompatible types in stage statement");
		}
		return arg;
	}

	private boolean isMutable(IExpression expression) {
		if (expression instanceof IIdentExpression e) {
			String name = e.getName().getName();
			IDeclaration dec = symtab.lookupDec(name);
			return isMutable(dec);
		}
		if (expression instanceof IListSelectorExpression e) {
			String name = e.getName().getName();
			IDeclaration dec = symtab.lookupDec(name);
			return isMutable(dec);
		}
		return false;
	}

	boolean isMutable(IDeclaration dec) {
		return (dec instanceof IMutableGlobal || dec instanceof INameDef);
	}

	@Override
	public Object visitIIdentifier(IIdentifier n, Object arg) throws Exception {
		String name = n.getName();
		IDeclaration dec = symtab.lookupDec(name);
		check(dec != null, n, "identifier not declared");
		n.setDec(dec);
		return dec;
	}

	@Override
	public Object visitIExpressionStatement(IExpressionStatement n, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
