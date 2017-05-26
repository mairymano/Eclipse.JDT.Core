/*******************************************************************************
 * Copyright (c) 2016, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This is an implementation of an early-draft specification developed under the Java
 * Community Process (JCP) and is made available for testing and evaluation purposes
 * only. The code is not compatible with any specification of the JCP.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.dom;

import junit.framework.Test;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.JrtPackageFragmentRoot;
import org.eclipse.jdt.internal.core.SourceModule;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

@SuppressWarnings({"rawtypes"})
public class ASTConverter9Test extends ConverterTestSetup {

	ICompilationUnit workingCopy;
	private static boolean isJRE9 = false;

	public void setUpSuite() throws Exception {
		super.setUpSuite();
		this.ast = AST.newAST(AST_INTERNAL_JLS9);
	}

	public ASTConverter9Test(String name) {
		super(name);
	}

	static {
//		TESTS_NUMBERS = new int[] { 19 };
//		TESTS_RANGE = new int[] { 1, -1 };
//		TESTS_NAMES = new String[] {"testBug515875_003"};
	}
	public static Test suite() {
		String javaVersion = System.getProperty("java.version");
		if (javaVersion.length() > 3) {
			javaVersion = javaVersion.substring(0, 3);
		}
		long jdkLevel = CompilerOptions.versionToJdkLevel(javaVersion);
		if (jdkLevel >= ClassFileConstants.JDK9) {
			isJRE9 = true;
		}
		return buildModelTestSuite(ASTConverter9Test.class);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if (this.workingCopy != null) {
			this.workingCopy.discardWorkingCopy();
			this.workingCopy = null;
		}
	}

	public void testBug497719_0001() throws JavaModelException {
		ICompilationUnit sourceUnit = getCompilationUnit("Converter9" , "src", "testBug497719_001", "X.java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		ASTNode result = runConversion(this.ast.apiLevel(), sourceUnit, true, true);
		char[] source = sourceUnit.getSource().toCharArray();
		assertTrue("Not a compilation unit", result.getNodeType() == ASTNode.COMPILATION_UNIT);
		CompilationUnit compilationUnit = (CompilationUnit) result;
		assertProblemsSize(compilationUnit, 0);
		ASTNode node = getASTNode(compilationUnit, 0, 0);
		assertEquals("Not a compilation unit", ASTNode.METHOD_DECLARATION, node.getNodeType());
		MethodDeclaration methodDeclaration = (MethodDeclaration) node;
		TryStatement tryStatement = (TryStatement) methodDeclaration.getBody().statements().get(1);
		List list = tryStatement.resources();
		VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression) list.get(0);
		checkSourceRange(variableDeclarationExpression, "final Y y = new Y()", source);
		SimpleName simpleName = (SimpleName) list.get(1);
		checkSourceRange(simpleName, "y1", source);
		variableDeclarationExpression = (VariableDeclarationExpression) list.get(2);
		checkSourceRange(variableDeclarationExpression, "final Y y2 = new Y()", source);
		
	}
	
	public void testBug497719_0002() throws JavaModelException {
		String contents =
				"import java.io.IOException;\n" +
				"\n" +
				"class Z {\n" +
				"	 final Y yz = new Y();\n" +
				"}\n" +
				"public class X extends Z {\n" +
				"	final  Y y2 = new Y();\n" +
				"	\n" +
				"	 Y bar() {\n" +
				"		 return new Y();\n" +
				"	 }\n" +
				"	public void foo() {\n" +
				"		Y y3 = new Y();\n" +
				"		int a[];\n" +
				"		try (y3; y3;super.yz;super.yz;this.y2;Y y4 = new Y())  {  \n" +
				"			System.out.println(\"In Try\");\n" +
				"		} catch (IOException e) {			  \n" +
				"		} \n" +
				"	}\n" +
				"	public static void main(String[] args) {\n" +
				"		new X().foo();\n" +
				"	}\n" +
				"}\n" +
				"class Y implements AutoCloseable {\n" +
				"	@Override\n" +
				"	public void close() throws IOException {\n" +
				"		System.out.println(\"Closed\");\n" +
				"	}  \n" +
				"}";
			this.workingCopy = getWorkingCopy("/Converter9/src/X.java", true/*resolve*/);
			ASTNode node = buildAST(contents, this.workingCopy, false);
			assertEquals("Not a compilation unit", ASTNode.COMPILATION_UNIT, node.getNodeType());
			node = getASTNode((CompilationUnit)node, 1, 2);
			MethodDeclaration methodDeclaration = (MethodDeclaration) node;
			TryStatement tryStatement = (TryStatement)methodDeclaration.getBody().statements().get(2);
			List<Expression> resources = tryStatement.resources();
			Expression expr = resources.get(0);
			SimpleName simpleName = (SimpleName) expr;
			checkSourceRange(simpleName, "y3", contents);
			expr = resources.get(1);
			simpleName = (SimpleName) expr;
			checkSourceRange(expr, "y3", contents);
			expr = resources.get(2);
			SuperFieldAccess superFieldAccess = (SuperFieldAccess) expr;
			checkSourceRange(superFieldAccess, "super.yz", contents);
			expr = resources.get(3);
			superFieldAccess = (SuperFieldAccess) expr;
			checkSourceRange(superFieldAccess, "super.yz", contents);
			expr = resources.get(4);
			FieldAccess fieldAccess = (FieldAccess) expr;
			checkSourceRange(fieldAccess, "this.y2", contents);
			expr = resources.get(5);
			VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression) expr;
			checkSourceRange(variableDeclarationExpression, "Y y4 = new Y()", contents);
	}
	public void testBug496123_0001() throws JavaModelException {
		this.workingCopies = new ICompilationUnit[1];
		String content =  "module first {"
				+ "  requires second;\n"
				+ "  exports pack11 to third, fourth;\n"
				+ "  uses NewType;\n"
				+ "  provides pack22.I22 with pack11.packinternal.Z11;\n"
				+ "}";
		this.workingCopies[0] = getWorkingCopy(
				"/Converter9/src/module-info.java", content);

		CompilationUnit unit = (CompilationUnit) runConversion(AST_INTERNAL_JLS9, this.workingCopies[0], false/*no bindings*/);
		ModuleDeclaration moduleDecl = unit.getModule();

		assertFalse(moduleDecl.isOpen());
		checkSourceRange(moduleDecl, content, content);
		List<ModuleStatement> stmts = moduleDecl.moduleStatements();
		assertTrue(stmts.size() > 0);

		RequiresStatement req = (RequiresStatement) stmts.get(0);
		checkSourceRange(req, "requires second;", content);

		ExportsStatement exp = (ExportsStatement) stmts.get(1);
		checkSourceRange(exp, "exports pack11 to third, fourth;", content);
		checkSourceRange(exp.getName(), "pack11", content);
		List<Name> modules = exp.modules();
		assertTrue(modules.size() == 2);
		checkSourceRange(modules.get(0), "third", content);
		checkSourceRange(modules.get(1), "fourth", content);
		
		UsesStatement u = (UsesStatement) stmts.get(2);
		checkSourceRange(u, "uses NewType;", content);
		Type type = u.getType();
		checkSourceRange(type, "NewType", content);
		
		ProvidesStatement p = (ProvidesStatement) stmts.get(3);
		checkSourceRange(p, "provides pack22.I22 with pack11.packinternal.Z11;", content);
		type = p.getType();
		checkSourceRange(type, "pack22.I22", content);
		List<Type> impls = p.implementations();
		assertTrue(impls.size() > 0);
		type = impls.get(0);
		checkSourceRange(type, "pack11.packinternal.Z11", content);		
	}

	public void testBug512023_0001() throws Exception {
		try {
			IJavaProject project1 = createJavaProject("ConverterTests9", new String[] {"src"}, new String[] {"JCL18_LIB"}, "bin", "9");
			project1.open(null);
			addClasspathEntry(project1, JavaCore.newContainerEntry(new Path("org.eclipse.jdt.MODULE_PATH")));
			String content = 
				"module first {\n" +
				"    requires second.third;\n" +
				"    exports pack1.X11 to org.eclipse.jdt;\n" +
				"}";
			createFile("/ConverterTests9/src/module-info.java",	content);
			createFolder("/ConverterTests9/src/pack1");
			createFile("/ConverterTests9/src/pack1/X11.java",
					"package pack1;\n" +
					"public class X11 {}\n");
			this.workingCopy = getWorkingCopy("/ConverterTests9/src/module-info.java", false);
			ASTNode node = buildAST(content, this.workingCopy, false);
			assertEquals("Not a compilation unit", ASTNode.COMPILATION_UNIT, node.getNodeType());
			CompilationUnit unit = (CompilationUnit) node;
			ModuleDeclaration moduleDecl = unit.getModule();
			assertFalse(moduleDecl.isOpen());
			checkSourceRange(moduleDecl, content, content);
			List<ModuleStatement> stmts = moduleDecl.moduleStatements();
			assertTrue(stmts.size() > 0);

			QualifiedName qName;
			RequiresStatement req = (RequiresStatement) stmts.get(0);
			qName = (QualifiedName) req.getName();
			checkSourceRange(qName, "second.third", content);
			checkSourceRange(qName.getName(), "third", content);
			checkSourceRange(qName.getQualifier(), "second", content);

			ExportsStatement exp = (ExportsStatement) stmts.get(1);
			checkSourceRange(exp, "exports pack1.X11 to org.eclipse.jdt;", content);
			qName = (QualifiedName) exp.getName();
			checkSourceRange(qName, "pack1.X11", content);
			checkSourceRange(qName.getName(), "X11", content);
			checkSourceRange(qName.getQualifier(), "pack1", content);

			List<Name> modules = exp.modules();
			qName = (QualifiedName) modules.get(0);
			checkSourceRange(qName, "org.eclipse.jdt", content);
			checkSourceRange(qName.getName(), "jdt", content);
			checkSourceRange(qName.getQualifier(), "org.eclipse", content);
		} finally {
			deleteProject("ConverterTests9");
		}
	}

	public void testBug514417() throws CoreException {
		if (!isJRE9) return;
		try {
			createJava9Project("Bug514417", new String[]{"src"});
			createFolder("/Bug514417/src/pack1");
			String content =  "package pack1;\n" +
					"import java.lang.String;\n" +
					"public class X { \n" +
					"	java.lang.String str = null;\n" +
					"}\n";
			createFile("/Bug514417/src/pack1/X.java", content);
			ICompilationUnit sourceUnit = getCompilationUnit("Bug514417" , "src", "pack1", "X.java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ASTNode unit = runConversion(AST_INTERNAL_JLS9, sourceUnit, true);
			assertEquals("Not a compilation unit", ASTNode.COMPILATION_UNIT, unit.getNodeType());
			List imps = ((CompilationUnit) unit).imports();
			assertEquals("import missing", 1, imps.size());
			ImportDeclaration impo = (ImportDeclaration) imps.get(0);
			IBinding bind = impo.resolveBinding();
			assertNotNull("binding null", bind);
			IJavaElement element = bind.getJavaElement();
			assertNotNull(element);
			assertEquals("Incorrect element type", IJavaElement.TYPE, element.getElementType());
			IType type = (IType) element;
			assertEquals("Incorrect name", "java.lang.String", type.getFullyQualifiedName());
			element = element.getParent();
			assertNotNull(element);
			assertEquals("Incorrect element type", IJavaElement.CLASS_FILE, element.getElementType());
			element = element.getParent();
			assertNotNull(element);
			assertEquals("Incorrect element type", IJavaElement.PACKAGE_FRAGMENT, element.getElementType());
			element = element.getParent();
			assertNotNull(element);
			assertEquals("Incorrect element type", IJavaElement.PACKAGE_FRAGMENT_ROOT, element.getElementType());
			assertTrue("incorrect root type", (element instanceof JrtPackageFragmentRoot));
			JrtPackageFragmentRoot root = (JrtPackageFragmentRoot) element;
			assertEquals("incorrect module name", "java.base", root.getElementName());
		} finally {
			deleteProject("Bug514417");
		}
	}
	public void testBug516785_0001_since_9() throws Exception {
		this.workingCopies = new ICompilationUnit[1];
		String content =  "open module first {"
				+ "  requires one;\n"
				+ "  requires static two;\n"
				+ "  requires transitive three;\n"
				+ "  requires static transitive four;\n"
				+ "  requires transitive static five;\n"
				+ "}";
		this.workingCopies[0] = getWorkingCopy(
				"/Converter9/src/module-info.java", content);
		
		CompilationUnit unit = (CompilationUnit) runConversion(AST_INTERNAL_JLS9, this.workingCopies[0], false/*no bindings*/);
		ModuleDeclaration moduleDecl = unit.getModule();
		
		assertTrue(moduleDecl.isOpen());
		checkSourceRange(moduleDecl, content, content);
		List<ModuleStatement> stmts = moduleDecl.moduleStatements();
		assertTrue(stmts.size() > 0);
		
		int count = 0;
		RequiresStatement req = (RequiresStatement) stmts.get(count++);
		checkSourceRange(req, "requires one;", content);

		req = (RequiresStatement) stmts.get(count++);
		checkSourceRange(req, "requires static two;", content);
		checkSourceRange((ASTNode) req.modifiers().get(0), "static", content);

		req = (RequiresStatement) stmts.get(count++);
		checkSourceRange(req, "requires transitive three;", content);
		checkSourceRange((ASTNode) req.modifiers().get(0), "transitive", content);

		req = (RequiresStatement) stmts.get(count++);
		checkSourceRange(req, "requires static transitive four;", content);
		checkSourceRange((ASTNode) req.modifiers().get(0), "static", content);
		checkSourceRange((ASTNode) req.modifiers().get(1), "transitive", content);

		req = (RequiresStatement) stmts.get(count++);
		checkSourceRange(req, "requires transitive static five;", content);
		checkSourceRange((ASTNode) req.modifiers().get(0), "transitive", content);
		checkSourceRange((ASTNode) req.modifiers().get(1), "static", content);
	}

	public void testBug515875_001() throws Exception {
		try {
			IJavaProject project1 = createJavaProject("ConverterTests9", new String[] {"src"}, new String[] {"JCL18_LIB"}, "bin", "9");
			project1.open(null);
			addClasspathEntry(project1, JavaCore.newContainerEntry(new Path("org.eclipse.jdt.MODULE_PATH")));
			String content = 
				"module first {\n" +
				"    requires transitive static second.third;\n" +
				"    exports pack1.X11 to org.eclipse.jdt;\n" +
				"}";
			createFile("/ConverterTests9/src/module-info.java",	content);
			createFolder("/ConverterTests9/src/pack1");
			createFile("/ConverterTests9/src/pack1/X11.java",
					"package pack1;\n" +
					"public class X11 {}\n");
			this.workingCopy = getWorkingCopy("/ConverterTests9/src/module-info.java", true);
			ASTNode node = buildAST(content, this.workingCopy, false);
			assertEquals("Not a compilation unit", ASTNode.COMPILATION_UNIT, node.getNodeType());
			CompilationUnit unit = (CompilationUnit) node;
			ModuleDeclaration moduleDecl = unit.getModule();
			checkSourceRange(moduleDecl, content, content);

			ModuleBinding moduleBinding = (ModuleBinding) moduleDecl.resolveBinding();
			assertTrue("Module Binding null", moduleBinding != null);
			String name = moduleBinding.getName();
			assertTrue("Module Name null", name != null);
			assertTrue("Wrong Module Name", name.equals("first"));

			IJavaElement element = moduleBinding.getJavaElement();
			assertNotNull("Module Java Element Null", element);
			assertTrue(element instanceof SourceModule);
			SourceModule sModule = (SourceModule) element;
			assertTrue("Source module name incorrect", sModule.getElementName().equals("first"));

			String key = moduleBinding.getKey();
			assertTrue("Unique Key incorrecct", key.equals("first"));

		} finally {
			deleteProject("ConverterTests9");
		}
	}

	public void testBug515875_002() throws Exception {
		try {

			IJavaProject project1 = createJavaProject("ConverterTests9", new String[] {"src"}, new String[] {"JCL18_LIB"}, "bin", "9");
			project1.open(null);
			addClasspathEntry(project1, JavaCore.newContainerEntry(new Path("org.eclipse.jdt.MODULE_PATH")));
			String fileContent = 
				"module first {\n" +
				"    requires second;\n" +
				"    provides pack22.I22 with pack1.X11;\n" +
				"}";
			createFile("/ConverterTests9/src/module-info.java",	fileContent);
			createFolder("/ConverterTests9/src/pack1");
			createFile("/ConverterTests9/src/pack1/X11.java",
					"package pack1;\n" +
					"public class X11 implements pack22.I22{}\n");

			IJavaProject project2 = createJavaProject("second", new String[] {"src"}, new String[] {"JCL18_LIB"}, "bin", "9");
			project2.open(null);
			addClasspathEntry(project2, JavaCore.newContainerEntry(new Path("org.eclipse.jdt.MODULE_PATH")));
			String secondFile = 
					"module second {\n" +
					"    exports pack22 to first;\n" +
					"}";
			createFile("/second/src/module-info.java",	secondFile);
			createFolder("/second/src/pack22");
			createFile("/second/src/pack22/I22.java",
					"package pack22;\n" +
					"public interface I22 {}\n");

			addClasspathEntry(project1, JavaCore.newProjectEntry(project2.getPath()));
			project1.close(); // sync
			project2.close();
			project2.open(null);
			project1.open(null);

			ICompilationUnit sourceUnit1 = getCompilationUnit("ConverterTests9" , "src", "", "module-info.java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ASTNode unit1 = runConversion(AST_INTERNAL_JLS9, sourceUnit1, true);
			assertEquals("Not a compilation unit", ASTNode.COMPILATION_UNIT, unit1.getNodeType());
			ModuleDeclaration moduleDecl1 = ((CompilationUnit) unit1).getModule();
			checkSourceRange(moduleDecl1, fileContent, fileContent);

			ModuleBinding moduleBinding = (ModuleBinding) moduleDecl1.resolveBinding();
			assertTrue("Module Binding null", moduleBinding != null);
			String name = moduleBinding.getName();
			assertTrue("Module Name null", name != null);
			assertTrue("Wrong Module Name", name.equals("first"));
			
			Name modName1 = moduleDecl1.getName();
			IBinding binding = modName1.resolveBinding();
			assertTrue("binding not a module binding", binding instanceof ModuleBinding);
			moduleBinding = (ModuleBinding) binding;
			assertTrue("Module Binding null", moduleBinding != null);
			name = moduleBinding.getName();
			assertTrue("Module Name null", name != null);
			assertTrue("Wrong Module Name", name.equals("first"));

			IModuleBinding[] reqs = moduleBinding.getRequiredModules();
			assertTrue("Null requires", reqs != null);
			assertTrue("incorrect number of requires modules", reqs.length == 1);
			ModuleBinding req11 = (ModuleBinding) reqs[0];
			assertTrue("incorrect name for requires modules", req11.getName().equals("second"));

			IPackageBinding[] secPacks = req11.getExportedPackages();
			assertTrue("Packages Exported in second module null", secPacks != null);
			assertTrue("Incorrect number of exported packages in second module", secPacks.length == 1);
			IPackageBinding pack22 = secPacks[0];
			assertTrue("Incorrect Package", pack22.getName().equals("pack22"));

/*			ITypeBinding[] services = moduleBinding.getServices();
			assertTrue("services null", services != null);
			assertTrue("Incorrect number of services", services.length == 1);
			assertTrue("Incorrect service", services[0].getName().equals("pack22.I22"));

			ITypeBinding[] implementations = moduleBinding.getImplementations();
			assertTrue("implementations null", implementations != null);
			assertTrue("Incorrect number of implementations", implementations.length == 1);
			assertTrue("Incorrect implementation", implementations[0].getName().equals("pack1.X11"));
*/		}
		finally {
			deleteProject("ConverterTests9");
			deleteProject("second");
		}
	}

	public void testBug515875_003() throws Exception {
		try {

			IJavaProject project1 = createJavaProject("ConverterTests9", new String[] {"src"}, new String[] {"JCL18_LIB"}, "bin", "9");
			project1.open(null);
			addClasspathEntry(project1, JavaCore.newContainerEntry(new Path("org.eclipse.jdt.MODULE_PATH")));
			String fileContent = 
				"module first {\n" +
				"    requires second;\n" +
				"    provides pack22.I22 with pack1.X11;\n" +
				"}";
			createFile("/ConverterTests9/src/module-info.java",	fileContent);
			createFolder("/ConverterTests9/src/pack1");
			createFile("/ConverterTests9/src/pack1/X11.java",
					"package pack1;\n" +
					"public class X11 implements pack22.I22{}\n");

			IJavaProject project2 = createJavaProject("second", new String[] {"src"}, new String[] {"JCL18_LIB"}, "bin", "9");
			project2.open(null);
			addClasspathEntry(project2, JavaCore.newContainerEntry(new Path("org.eclipse.jdt.MODULE_PATH")));
			String secondFile = 
					"module second {\n" +
					"    exports pack22 to first;\n" +
					"}";
			createFile("/second/src/module-info.java",	secondFile);
			createFolder("/second/src/pack22");
			createFile("/second/src/pack22/I22.java",
					"package pack22;\n" +
					"public interface I22 {}\n");

			addClasspathEntry(project1, JavaCore.newProjectEntry(project2.getPath()));
			project1.close(); // sync
			project2.close();
			project2.open(null);
			project1.open(null);

			ICompilationUnit sourceUnit1 = getCompilationUnit("ConverterTests9" , "src", "", "module-info.java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ASTNode unit1 = runConversion(AST_INTERNAL_JLS9, sourceUnit1, true);
			assertEquals("Not a compilation unit", ASTNode.COMPILATION_UNIT, unit1.getNodeType());
			ModuleDeclaration moduleDecl1 = ((CompilationUnit) unit1).getModule();
			checkSourceRange(moduleDecl1, fileContent, fileContent);

			RequiresStatement req = (RequiresStatement) moduleDecl1.moduleStatements().get(0);
			Name reqModule = req.getName();
			ModuleBinding moduleBinding = (ModuleBinding) reqModule.resolveBinding();
			assertTrue("Module Binding null", moduleBinding != null);
			String name = moduleBinding.getName();
			assertTrue("Module Name null", name != null);
			assertTrue("Wrong Module Name", name.equals("second"));
		}
		finally {
			deleteProject("ConverterTests9");
			deleteProject("second");
		}
	}


// Add new tests here 
}
