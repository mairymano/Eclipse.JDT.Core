/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.model;

import java.util.Map;

import junit.framework.Test;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;

/**
 * Tests the Java search engine in Javadoc comment.
 *
 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=45518">bug 45518</a>
 * @see <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=46761">bug 46761</a>
 */
public class JavaSearchJavadocTests extends JavaSearchTests {

	Map originalOptions;

	/**
	 * @param name
	 */
	public JavaSearchJavadocTests(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.tests.model.SuiteOfTestCases#setUpSuite()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.originalOptions = JAVA_PROJECT.getOptions(true);
		JAVA_PROJECT.setOption(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
		this.resultCollector.showAccuracy = true;
		this.resultCollector.showInsideDoc = true;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.tests.model.SuiteOfTestCases#tearDownSuite()
	 */
	public void tearDown() throws Exception {
		JAVA_PROJECT.setOptions(originalOptions);
		super.tearDown();
	}
	private void setJavadocOptions() {
		JAVA_PROJECT.setOption(JavaCore.COMPILER_PB_INVALID_JAVADOC, JavaCore.WARNING);
		JAVA_PROJECT.setOption(JavaCore.COMPILER_PB_MISSING_JAVADOC_COMMENTS, JavaCore.ERROR);
	}
	private void disableJavadocOptions() {
		JAVA_PROJECT.setOption(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.DISABLED);
	}
	public static Test suite() {
//		return buildTestSuite(JavaSearchJavadocTests.class, "testJavadocMethod", null);
		return buildTestSuite(JavaSearchJavadocTests.class);
	}
	// Use this static initializer to specify subset for tests
	// All specified tests which do not belong to the class are skipped...
//	static {
//		TESTS_NAMES = new String[] { "testGenericFieldReferenceAC04" };
//		TESTS_NUMBERS = new int[] { 8 };
//		TESTS_RANGE = new int[] { -1, -1 };
//	}

	/*
	 * Test search of type declaration in javadoc comments
	 * ===================================================
	 */
	public void testJavadocTypeDeclaration() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		search(
				type, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java j1.JavadocSearched [JavadocSearched] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocTypeStringDeclaration() throws CoreException {
		search( 
				"JavadocSearched",
				TYPE,
				DECLARATIONS, 
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java j1.JavadocSearched [JavadocSearched] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocTypeDeclarationWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		search(
				type, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java j1.JavadocSearched [JavadocSearched] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}

	/*
	 * Test search of field declaration in javadoc comments
	 * ====================================================
	 */
	public void testJavadocFieldDeclaration() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IField field = type.getField("javadocSearchedVar");
		search(
				field, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java j1.JavadocSearched.javadocSearchedVar [javadocSearchedVar] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocFieldStringDeclaration() throws CoreException {
		search(
				"javadocSearchedVar", 
				FIELD,
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java j1.JavadocSearched.javadocSearchedVar [javadocSearchedVar] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocFieldDeclarationWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IField field = type.getField("javadocSearchedVar");
		setJavadocOptions();
		search(
				field, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java j1.JavadocSearched.javadocSearchedVar [javadocSearchedVar] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}

	/*
	 * Test search of method declarations in javadoc comments
	 * ======================================================
	 */
	public void testJavadocMethodDeclaration() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("javadocSearchedMethod", null);
		search(
				method, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java void j1.JavadocSearched.javadocSearchedMethod() [javadocSearchedMethod] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodArgDeclaration() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("javadocSearchedMethod", new String[] { "QString;" });
		search(
				method, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java void j1.JavadocSearched.javadocSearchedMethod(String) [javadocSearchedMethod] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodStringDeclaration() throws CoreException {
		search(
				"javadocSearchedMethod", 
				METHOD,
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java void j1.JavadocSearched.javadocSearchedMethod() [javadocSearchedMethod] EXACT_MATCH OUTSIDE_JAVADOC\n" + 
				"src/j1/JavadocSearched.java void j1.JavadocSearched.javadocSearchedMethod(String) [javadocSearchedMethod] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodDeclarationWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		IMethod method = type.getMethod("javadocSearchedMethod", null);
		search(
				method, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java void j1.JavadocSearched.javadocSearchedMethod() [javadocSearchedMethod] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodArgDeclarationWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		IMethod method = type.getMethod("javadocSearchedMethod", new String[] { "QString;" });
		search(
				method, 
				DECLARATIONS,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocSearched.java void j1.JavadocSearched.javadocSearchedMethod(String) [javadocSearchedMethod] EXACT_MATCH OUTSIDE_JAVADOC",
				this.resultCollector);
	}

	/*
	 * Test search of type references in javadoc comments
	 * ==================================================
	 */
	public void testJavadocTypeReference() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		search(
				type, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [j1.JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [j1.JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testJavadocTypeStringReference() throws CoreException {
		search(
				"JavadocSearched", 
				TYPE,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testJavadocTypeReferenceWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		search(
				type, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [j1.JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [j1.JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testJavadocTypeStringReferenceWithJavadoc() throws CoreException {
		setJavadocOptions();
		search(
				"JavadocSearched", 
				TYPE,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n"+
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testJavadocTypeStringReferenceWithJavadocDisabled() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		disableJavadocOptions();
		search(
				type, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults("", this.resultCollector);
	}

	/*
	 * Test search of field references in javadoc comments
	 * ===================================================
	 */
	public void testJavadocFieldReference() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IField field = type.getField("javadocSearchedVar");
		search(
				field, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedVar] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocFieldStringReference() throws CoreException {
		search(
				"javadocSearchedVar", 
				FIELD,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedVar] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocFieldReferenceWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IField field = type.getField("javadocSearchedVar");
		setJavadocOptions();
		search(
				field, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedVar] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocFieldStringReferenceWithJavadoc() throws CoreException {
		setJavadocOptions();
		search(
				"javadocSearchedVar", 
				FIELD,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedVar] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedVar] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocFieldStringReferenceWithJavadocDisabled() throws CoreException {
		disableJavadocOptions();
		search(
				"javadocSearchedVar", 
				FIELD,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults("", this.resultCollector);
	}

	/*
	 * Test search of method references in javadoc comments
	 * ====================================================
	 */
	public void testJavadocMethodReference() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("javadocSearchedMethod", null);
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedMethod] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodArgReference() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("javadocSearchedMethod", new String[] { "QString;" });
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodStringReference() throws CoreException {
		search(
				"javadocSearchedMethod", 
				METHOD,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodReferenceWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		IMethod method = type.getMethod("javadocSearchedMethod", null);
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedMethod] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodArgReferenceWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		IMethod method = type.getMethod("javadocSearchedMethod", new String[] { "QString;" });
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodStringReferenceWithJavadoc() throws CoreException {
		setJavadocOptions();
		search(
				"javadocSearchedMethod", 
				METHOD,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [javadocSearchedMethod] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocMethodArgReferenceWithJavadocDisabled() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("javadocSearchedMethod", new String[] { "QString;" });
		disableJavadocOptions();
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults("", this.resultCollector);
	}

	/*
	 * Test search of constructor references in javadoc comments
	 * ====================================================
	 */
	public void testJavadocConstructorReference() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("JavadocSearched", null);
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocConstructorArgReference() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("JavadocSearched", new String[] { "QString;" });
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocConstructorStringReference() throws CoreException {
		search(
				"JavadocSearched", 
				CONSTRUCTOR,
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocConstructorReferenceWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		IMethod method = type.getMethod("JavadocSearched", null);
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocConstructorArgReferenceWithJavadoc() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		setJavadocOptions();
		IMethod method = type.getMethod("JavadocSearched", new String[] { "QString;" });
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults(
				"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
				this.resultCollector);
	}
	public void testJavadocConstructorStringReferenceWithJavadoc() throws CoreException {
		setJavadocOptions();
		search(
			"JavadocSearched", 
			CONSTRUCTOR,
			REFERENCES,
			getJavaSearchScope(), 
			this.resultCollector);
		assertSearchResults(
			"src/j1/JavadocInvalidRef.java void j1.JavadocInvalidRef.invalid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j1/JavadocValidRef.java void j1.JavadocValidRef.valid() [JavadocSearched] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testJavadocConstructorReferenceWithJavadocDisabled() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j1", "JavadocSearched.java").getType("JavadocSearched");
		IMethod method = type.getMethod("JavadocSearched", null);
		disableJavadocOptions();
		search(
				method, 
				REFERENCES,
				getJavaSearchScope(), 
				this.resultCollector);
		assertSearchResults("", this.resultCollector);
	}
	public void testJavadocTypeParameterReferences01() throws CoreException {
		workingCopies = new ICompilationUnit[1];
		workingCopies[0] = getWorkingCopy("/JavaSearch15/src/b81190/Test.java",
			"package b81190;\n" + 
			"/**\n" + 
			" * @param <T1> First class type parameter\n" + 
			" * @param <T2> Second class type parameter\n" + 
			" * @param <T3> Last class type parameter\n" + 
			" */\n" + 
			"public class Test<T1, T2, T3> {\n" + 
			"	/**\n" + 
			"	 * @param <U> Method type parameter\n" + 
			"	 * @param x Method parameter\n" + 
			"	 */\n" + 
			"	<U> void generic(U x, T1 t) {\n" + 
			"		Object o = x;\n" + 
			"		o.toString();\n" + 
			"	}\n" + 
			"}\n"
			);
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(workingCopies);
		ITypeParameter typeParam = selectTypeParameter(workingCopies[0], "T1", 2);
		discard = false; // use same working copy for next test
		search(typeParam, REFERENCES, scope);
		assertSearchResults(
			"src/b81190/Test.java b81190.Test [T1] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/b81190/Test.java void b81190.Test.generic(U, T1) [T1] EXACT_MATCH OUTSIDE_JAVADOC"
		);
	}
	public void testJavadocTypeParameterReferences02() throws CoreException {
		assertNotNull("Problem in tests processing", workingCopies);
		assertEquals("Problem in tests processing", 1, workingCopies.length);
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(workingCopies);
		ITypeParameter typeParam = selectTypeParameter(workingCopies[0], "U", 2);
		discard = false; // use same working copy for next test
		search(typeParam, REFERENCES, scope);
		assertSearchResults(
			"src/b81190/Test.java void b81190.Test.generic(U, T1) [U] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/b81190/Test.java void b81190.Test.generic(U, T1) [U] EXACT_MATCH OUTSIDE_JAVADOC"
		);
	}
	// Local variables references in Javadoc have been fixed while implementing 81190
	public void testJavadocParameterReferences01() throws CoreException {
		assertNotNull("Problem in tests processing", workingCopies);
		assertEquals("Problem in tests processing", 1, workingCopies.length);
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(workingCopies);
		ILocalVariable local = selectLocalVariable(workingCopies[0], "x", 2);
		search(local, REFERENCES, scope);
		assertSearchResults(
			"src/b81190/Test.java void b81190.Test.generic(U, T1) [x] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/b81190/Test.java void b81190.Test.generic(U, T1) [x] EXACT_MATCH OUTSIDE_JAVADOC"
		);
	}

	/**
	 * Test fix for bug 47909.
	 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=47909">47909</a>
	 * @throws CoreException
	 */
	public void testBug47909() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j3", "Y.java").getType("Y");
		setJavadocOptions();
		IMethod method = type.getMethod("Y", new String[] { "I" });
		search(
			method, 
			REFERENCES,
			getJavaSearchScope(), 
			this.resultCollector);
		assertSearchResults(
			"test47909.jar void j3.X.bar() EXACT_MATCH OUTSIDE_JAVADOC",
			this.resultCollector);
	}
	
	/**
	 * Test fix for bug 47968.
	 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=47968">47968</a>
	 * @throws CoreException
	 */
	public void testBug47968type() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j2", "Bug47968.java").getType("Bug47968");
		setJavadocOptions();
		search(
			type, 
			REFERENCES,
			getJavaSearchScope(), 
			this.resultCollector);
		assertSearchResults(
			// These matches were not found before...
			"src/j2/Bug47968s.java j2.Bug47968s [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			// ...end
			"src/j2/Bug47968s.java j2.Bug47968s.y [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s.y [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s.y [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s.y [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [Bug47968] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug47968field() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j2", "Bug47968.java").getType("Bug47968");
		setJavadocOptions();
		IField field = type.getField("x");
		search(
			field, 
			REFERENCES,
			getJavaSearchScope(), 
			this.resultCollector);
		assertSearchResults(
			"src/j2/Bug47968s.java j2.Bug47968s [x] EXACT_MATCH INSIDE_JAVADOC\n" + // This match was not found before...
			"src/j2/Bug47968s.java j2.Bug47968s.y [x] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [x] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [x] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug47968method() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j2", "Bug47968.java").getType("Bug47968");
		setJavadocOptions();
		IMethod method = type.getMethod("foo", new String[] { "I" });
		search(
			method, 
			REFERENCES,
			getJavaSearchScope(), 
			this.resultCollector);
		assertSearchResults(
			"src/j2/Bug47968s.java j2.Bug47968s [foo] EXACT_MATCH INSIDE_JAVADOC\n" + // This match was not found before...
			"src/j2/Bug47968s.java j2.Bug47968s.y [foo] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [foo] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [foo] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug47968constructor() throws CoreException {
		IType type = getCompilationUnit("JavaSearch", "src", "j2", "Bug47968.java").getType("Bug47968");
		setJavadocOptions();
		IMethod method = type.getMethod("Bug47968", new String[] { "QString;" });
		search(
			method, 
			REFERENCES,
			getJavaSearchScope(), 
			this.resultCollector);
		assertSearchResults(
			"src/j2/Bug47968s.java j2.Bug47968s [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + // This match was not found before...
			"src/j2/Bug47968s.java j2.Bug47968s.y [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java j2.Bug47968s() [Bug47968] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j2/Bug47968s.java void j2.Bug47968s.bar() [Bug47968] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}

	/**
	 * Test fix for bug 47209.
	 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=47209">47209</a>
	 * @throws CoreException
	 */
	public void testBug47209type() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j4", "TT47209.java").getType("TT47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "TF47209.java").getType("TF47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "TC47209.java").getType("TC47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "TT47209.java").getType("TM47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/j4/TT47209.java j4.TT47209 [TT47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/TF47209.java j4.TF47209.f47209 [TF47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/TC47209.java j4.TC47209(String) [TC47209] EXACT_MATCH INSIDE_JAVADOC\n" +
				"src/j4/TM47209.java void j4.TM47209.m47209(int) [TM47209] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug47209field() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j4", "FT47209.java").getType("FT47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "FF47209.java").getType("FF47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "FC47209.java").getType("FC47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "FT47209.java").getType("FM47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/j4/FT47209.java j4.FT47209 [FT47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/FF47209.java j4.FF47209.f47209 [FF47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/FC47209.java j4.FC47209(String) [FC47209] EXACT_MATCH INSIDE_JAVADOC\n" +
				"src/j4/FM47209.java void j4.FM47209.m47209(int) [FM47209] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug47209method() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j4", "MT47209.java").getType("MT47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "MF47209.java").getType("MF47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "MC47209.java").getType("MC47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "MT47209.java").getType("MM47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/j4/MT47209.java j4.MT47209 [MT47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/MF47209.java j4.MF47209.f47209 [MF47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/MC47209.java j4.MC47209(String) [MC47209] EXACT_MATCH INSIDE_JAVADOC\n" +
				"src/j4/MM47209.java void j4.MM47209.m47209(int) [MM47209] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug47209constructor() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j4", "CT47209.java").getType("CT47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "CF47209.java").getType("CF47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "CC47209.java").getType("CC47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		type = getCompilationUnit("JavaSearch", "src", "j4", "CT47209.java").getType("CM47209");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/j4/CT47209.java j4.CT47209 [CT47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/CF47209.java j4.CF47209.f47209 [CF47209] EXACT_MATCH INSIDE_JAVADOC\n" + 
				"src/j4/CC47209.java j4.CC47209(String) [CC47209] EXACT_MATCH INSIDE_JAVADOC\n" +
				"src/j4/CM47209.java void j4.CM47209.m47209(int) [CM47209] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}

	/**
	 * Test fix for bug 49994.
	 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=49994">49994</a>
	 * @throws CoreException
	 */
	public void testBug49994() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j5", "Bug49994.java").getType("Bug49994");
		search(type,  REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults("", this.resultCollector);
	}
	public void testBug49994field() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j5", "Bug49994.java").getType("Bug49994");
		IField field = type.getField("field");
		search(field, REFERENCES, getJavaSearchScope(), this.resultCollector);
		assertSearchResults("src/j5/Bug49994.java void j5.Bug49994.foo() [field] EXACT_MATCH INSIDE_JAVADOC", this.resultCollector);
	}
	public void testBug49994method() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j5", "Bug49994.java").getType("Bug49994");
		IMethod method = type.getMethod("bar", new String[0]);
		search(method, REFERENCES, getJavaSearchScope(), this.resultCollector);
		assertSearchResults("src/j5/Bug49994.java void j5.Bug49994.foo() [bar] EXACT_MATCH INSIDE_JAVADOC", this.resultCollector);
	}
	public void testBug49994constructor() throws CoreException {
		setJavadocOptions();
		IType type = getCompilationUnit("JavaSearch", "src", "j5", "Bug49994.java").getType("Bug49994");
		IMethod method = type.getMethod("Bug49994", new String[] { "QString;" });
		search(method, REFERENCES, getJavaSearchScope(), this.resultCollector);
		assertSearchResults("src/j5/Bug49994.java void j5.Bug49994.foo() [Bug49994] EXACT_MATCH INSIDE_JAVADOC", this.resultCollector);
	}

	/**
	 * Test fix for bug 54962.
	 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=54962">54962</a>
	 * @throws CoreException
	 */
	public void testBug54962() throws CoreException {
		setJavadocOptions();
		this.resultCollector.showInsideDoc = true;
		IPackageDeclaration packDecl = getCompilationUnit("JavaSearch", "src", "j6", "Bug54962.java").getPackageDeclaration("j6");
		search(packDecl, REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/j6/Bug54962.java j6.Bug54962 [j6] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j6/Bug54962.java j6.Bug54962 [j6] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
			"src/j6/Bug54962.java j6.Bug54962 [j6] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j7/qua/li/fied/Bug54962a.java [j6] EXACT_MATCH OUTSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug54962qualified() throws CoreException {
		setJavadocOptions();
		this.resultCollector.showInsideDoc = true;
		IPackageDeclaration packDecl = getCompilationUnit("JavaSearch", "src", "j7.qua.li.fied", "Bug54962a.java").getPackageDeclaration("j7.qua.li.fied");
		search(packDecl, REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/j7/qua/li/fied/Bug54962a.java j7.qua.li.fied.Bug54962a [j7.qua.li.fied] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/j7/qua/li/fied/Bug54962a.java j7.qua.li.fied.Bug54962a [j7.qua.li.fied] POTENTIAL_MATCH INSIDE_JAVADOC\n" + 
			"src/j7/qua/li/fied/Bug54962a.java j7.qua.li.fied.Bug54962a [j7.qua.li.fied] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}

	/**
	 * Test fix for bug 71267: [Search][Javadoc] SearchMatch in class javadoc reported with element of type IImportDeclaration
	 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=71267">71267</a>
	 * @throws CoreException
	 */
	public void testBug71267() throws CoreException {
		setJavadocOptions();
		this.resultCollector.showInsideDoc = true;
		IPackageDeclaration packDecl = getCompilationUnit("JavaSearch", "src", "p71267", "Test.java").getPackageDeclaration("p71267");
		search(packDecl, REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/p71267/Test.java p71267.Test [p71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/Test.java p71267.Test [p71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/q71267/Test.java p71267.q71267.Test [p71267] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
	public void testBug71267qualified() throws CoreException {
		setJavadocOptions();
		this.resultCollector.showInsideDoc = true;
		IPackageDeclaration packDecl = getCompilationUnit("JavaSearch", "src", "p71267.q71267", "Test.java").getPackageDeclaration("p71267.q71267");
		search(packDecl, REFERENCES, getJavaSearchScope(),  this.resultCollector);
		assertSearchResults(
			"src/p71267/q71267/Test.java p71267.q71267.Test [p71267.q71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/q71267/Test.java p71267.q71267.Test [p71267.q71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/q71267/Test.java p71267.q71267.Test.field [p71267.q71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/q71267/Test.java p71267.q71267.Test.field [p71267.q71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/q71267/Test.java void p71267.q71267.Test.method() [p71267.q71267] EXACT_MATCH INSIDE_JAVADOC\n" + 
			"src/p71267/q71267/Test.java void p71267.q71267.Test.method() [p71267.q71267] EXACT_MATCH INSIDE_JAVADOC",
			this.resultCollector);
	}
}
