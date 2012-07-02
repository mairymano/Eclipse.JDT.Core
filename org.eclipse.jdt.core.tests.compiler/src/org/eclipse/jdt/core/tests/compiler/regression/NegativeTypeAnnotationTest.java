/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
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
package org.eclipse.jdt.core.tests.compiler.regression;

import junit.framework.Test;

public class NegativeTypeAnnotationTest extends AbstractRegressionTest {

	static { 
//		TESTS_NUMBERS = new int [] { 35 };
	}
	public static Class testClass() {
		return NegativeTypeAnnotationTest.class;
	}
	public static Test suite() {
		return buildMinimalComplianceTestSuite(testClass(), F_1_8);
	}
	public NegativeTypeAnnotationTest(String testName){
		super(testName);
	}
	public void test001() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"public class X extends @Marker2 Object {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X extends @Marker2 Object {}\n" + 
				"	                        ^^^^^^^\n" + 
				"Marker2 cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void test002() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"import java.io.Serializable;\n" +
					"public class X implements @Marker2 Serializable {\n" +
					"	private static final long serialVersionUID = 1L;\n" +
					"}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 2)\n" + 
				"	public class X implements @Marker2 Serializable {\n" + 
				"	                           ^^^^^^^\n" + 
				"Marker2 cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void test003() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"public class X extends @Marker Object {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X extends @Marker Object {}\n" + 
				"	                        ^^^^^^\n" + 
				"Marker cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void test004() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"public class X<@Marker T> {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X<@Marker T> {}\n" + 
				"	                ^^^^^^\n" + 
				"Marker cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void test005() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"public class X<@Marker T> {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X<@Marker T> {}\n" + 
				"	                ^^^^^^\n" + 
				"Marker cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void test006() throws Exception {
		this.runNegativeTest(
			new String[] {
				"Y.java",
				"class Y {}\n",
				"X.java",
				"public class X extends @A(id=\"Hello, World!\") @B @C('(') Y {\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X extends @A(id=\"Hello, World!\") @B @C(\'(\') Y {\n" + 
		"	                        ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X extends @A(id=\"Hello, World!\") @B @C(\'(\') Y {\n" + 
		"	                                               ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 1)\n" + 
		"	public class X extends @A(id=\"Hello, World!\") @B @C(\'(\') Y {\n" + 
		"	                                                  ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n");
	}
	public void test007() throws Exception {
		this.runNegativeTest(
			new String[] {
				"I.java",
				"interface I {}\n",
				"J.java",
				"interface J {}\n",
				"X.java",
				"public class X implements @A(id=\"Hello, World!\") I, @B @C('(') J {}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X implements @A(id=\"Hello, World!\") I, @B @C(\'(\') J {}\n" + 
		"	                           ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X implements @A(id=\"Hello, World!\") I, @B @C(\'(\') J {}\n" + 
		"	                                                     ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 1)\n" + 
		"	public class X implements @A(id=\"Hello, World!\") I, @B @C(\'(\') J {}\n" + 
		"	                                                        ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n");
	}
	public void test010() throws Exception {
		this.runNegativeTest(
			new String[] {
				"Y.java",
				"class Y<T> {}\n",
				"X.java",
				"public class X extends @A(\"Hello, World!\") Y<@B @C('(') String> {\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X extends @A(\"Hello, World!\") Y<@B @C(\'(\') String> {\n" + 
		"	                        ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X extends @A(\"Hello, World!\") Y<@B @C(\'(\') String> {\n" + 
		"	                                              ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 1)\n" + 
		"	public class X extends @A(\"Hello, World!\") Y<@B @C(\'(\') String> {\n" + 
		"	                                                 ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n");
	}
	public void test011() throws Exception {
		this.runNegativeTest(
			new String[] {
				"I.java",
				"interface I<T> {}\n",
				"J.java",
				"interface J<T> {}\n",
				"X.java",
				"public class X implements I<@A(\"Hello, World!\") String>,  @B J<@C('(') Integer> {}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X implements I<@A(\"Hello, World!\") String>,  @B J<@C(\'(\') Integer> {}\n" + 
		"	                             ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X implements I<@A(\"Hello, World!\") String>,  @B J<@C(\'(\') Integer> {}\n" + 
		"	                                                           ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 1)\n" + 
		"	public class X implements I<@A(\"Hello, World!\") String>,  @B J<@C(\'(\') Integer> {}\n" + 
		"	                                                                ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n");
	}
	// throws
	public void test012() throws Exception {
		this.runNegativeTest(
			new String[] {
				"E.java",
				"class E extends RuntimeException {\n" +
				"	private static final long serialVersionUID = 1L;\n" +
				"}\n",
				"E1.java",
				"class E1 extends RuntimeException {\n" +
				"	private static final long serialVersionUID = 1L;\n" +
				"}\n",
				"E2.java",
				"class E2 extends RuntimeException {\n" +
				"	private static final long serialVersionUID = 1L;\n" +
				"}\n",
				"X.java",
				"public class X {\n" +
				"	void foo() throws @A(\"Hello, World!\") E, E1, @B @C('(') E2 {}\n" +
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	void foo() throws @A(\"Hello, World!\") E, E1, @B @C(\'(\') E2 {}\n" + 
		"	                   ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 2)\n" + 
		"	void foo() throws @A(\"Hello, World!\") E, E1, @B @C(\'(\') E2 {}\n" + 
		"	                                              ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 2)\n" + 
		"	void foo() throws @A(\"Hello, World!\") E, E1, @B @C(\'(\') E2 {}\n" + 
		"	                                                 ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n");
	}
	// method receiver
	public void test013() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	void foo(@B(3) X this) {}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	void foo(@B(3) X this) {}\n" + 
		"	          ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// method return type
	public void test014() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	@B(3) int foo() {\n" +
				"		return 1;\n" +
				"	}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	@B(3) int foo() {\n" + 
		"	 ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// field type
	public void test015() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	@B(3) int field;\n" +
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	@B(3) int field;\n" + 
		"	 ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// method parameter
	public void test016() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	int foo(@B(3) String s) {\n" +
				"		return s.length();\n" +
				"	}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	int foo(@B(3) String s) {\n" + 
		"	         ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// method parameter generic or array
	public void test017() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	int foo(String @B(3) [] s) {\n" +
				"		return s.length;\n" +
				"	}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	int foo(String @B(3) [] s) {\n" + 
		"	                ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// field type generic or array
	public void test018() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	int @B(3) [] field;\n" +
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	int @B(3) [] field;\n" + 
		"	     ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// class type parameter
	public void test019() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X<@A @B(3) T> {}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X<@A @B(3) T> {}\n" + 
		"	                ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X<@A @B(3) T> {}\n" + 
		"	                   ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// method type parameter
	public void test020() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	<@A @B(3) T> void foo(T t) {}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	<@A @B(3) T> void foo(T t) {}\n" + 
		"	  ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 2)\n" + 
		"	<@A @B(3) T> void foo(T t) {}\n" + 
		"	     ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// class type parameter bound
	public void test021() throws Exception {
		this.runNegativeTest(
			new String[] {
				"Z.java",
				"public class Z {}",
				"X.java",
				"public class X<T extends @A Z & @B(3) Cloneable> {}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X<T extends @A Z & @B(3) Cloneable> {}\n" + 
		"	                          ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X<T extends @A Z & @B(3) Cloneable> {}\n" + 
		"	                                 ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// class type parameter bound generic or array
	public void test022() throws Exception {
		this.runNegativeTest(
			new String[] {
				"Y.java",
				"public class Y<T> {}",
				"X.java",
				"public class X<T extends Y<@A String @C[][]@B[]> & @B(3) Cloneable> {}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 1)\n" + 
		"	public class X<T extends Y<@A String @C[][]@B[]> & @B(3) Cloneable> {}\n" + 
		"	                            ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 1)\n" + 
		"	public class X<T extends Y<@A String @C[][]@B[]> & @B(3) Cloneable> {}\n" + 
		"	                                      ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 1)\n" + 
		"	public class X<T extends Y<@A String @C[][]@B[]> & @B(3) Cloneable> {}\n" + 
		"	                                            ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"4. ERROR in X.java (at line 1)\n" + 
		"	public class X<T extends Y<@A String @C[][]@B[]> & @B(3) Cloneable> {}\n" + 
		"	                                                    ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// method type parameter bound
	public void test023() throws Exception {
		this.runNegativeTest(
			new String[] {
				"Z.java",
				"public class Z {}",
				"X.java",
				"public class X {\n" +
				"	<T extends @A Z & @B(3) Cloneable> void foo(T t) {}\n" +
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	<T extends @A Z & @B(3) Cloneable> void foo(T t) {}\n" + 
		"	            ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 2)\n" + 
		"	<T extends @A Z & @B(3) Cloneable> void foo(T t) {}\n" + 
		"	                   ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// class type parameter bound generic or array
	public void test024() throws Exception {
		this.runNegativeTest(
			new String[] {
				"Z.java",
				"public class Z {}",
				"Y.java",
				"public class Y<T> {}",
				"X.java",
				"public class X {\n" +
				"	<T extends Y<@A Z @C[][]@B[]> & @B(3) Cloneable> void foo(T t) {}\n" +
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 2)\n" + 
		"	<T extends Y<@A Z @C[][]@B[]> & @B(3) Cloneable> void foo(T t) {}\n" + 
		"	              ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 2)\n" + 
		"	<T extends Y<@A Z @C[][]@B[]> & @B(3) Cloneable> void foo(T t) {}\n" + 
		"	                   ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 2)\n" + 
		"	<T extends Y<@A Z @C[][]@B[]> & @B(3) Cloneable> void foo(T t) {}\n" + 
		"	                         ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"4. ERROR in X.java (at line 2)\n" + 
		"	<T extends Y<@A Z @C[][]@B[]> & @B(3) Cloneable> void foo(T t) {}\n" + 
		"	                                 ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// local variable + generic or array
	public void test025() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	void foo(String s) {\n" + 
				"		@C int i;\n" + 
				"		@A String [] @B(3)[] tab = new String[][] {};\n" + 
				"		if (tab != null) {\n" + 
				"			i = 0;\n" + 
				"			System.out.println(i + tab.length);\n" + 
				"		} else {\n" + 
				"			System.out.println(tab.length);\n" + 
				"		}\n" + 
				"		i = 4;\n" + 
				"		System.out.println(-i + tab.length);\n" + 
				"	}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	@C int i;\n" + 
		"	 ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 4)\n" + 
		"	@A String [] @B(3)[] tab = new String[][] {};\n" + 
		"	 ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 4)\n" + 
		"	@A String [] @B(3)[] tab = new String[][] {};\n" + 
		"	              ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// type argument constructor call
	public void test026() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	<T> X(T t) {\n" + 
				"	}\n" + 
				"	public Object foo() {\n" + 
				"		X x = new <@A @B(1) String>X(null);\n" + 
				"		return x;\n" + 
				"	}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	X x = new <@A @B(1) String>X(null);\n" + 
		"	            ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 5)\n" + 
		"	X x = new <@A @B(1) String>X(null);\n" + 
		"	               ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// type argument constructor call generic or array
	public void test027() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" + 
				"	<T> X(T t) {\n" + 
				"	}\n" + 
				"	public Object foo() {\n" + 
				"		X x = new <@A @B(1) String>X(null);\n" + 
				"		return x;\n" + 
				"	}\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 5)\n" + 
		"	X x = new <@A @B(1) String>X(null);\n" + 
		"	            ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 5)\n" + 
		"	X x = new <@A @B(1) String>X(null);\n" + 
		"	               ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n");
	}
	// type argument method call and generic or array
	public void test028() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" +
				"\n" +
				"	static <T, U> T foo(T t, U u) {\n" +
				"		return t;\n" +
				"	}\n" +
				"	public static void main(String[] args) {\n" +
				"		System.out.println(X.<@A @B(1) String[], @C('-') X>foo(new String[]{\"SUCCESS\"}, null)[0]);\n" +
				"	}\n" +
				"}\n",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 7)\n" + 
		"	System.out.println(X.<@A @B(1) String[], @C(\'-\') X>foo(new String[]{\"SUCCESS\"}, null)[0]);\n" + 
		"	                       ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 7)\n" + 
		"	System.out.println(X.<@A @B(1) String[], @C(\'-\') X>foo(new String[]{\"SUCCESS\"}, null)[0]);\n" + 
		"	                          ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 7)\n" + 
		"	System.out.println(X.<@A @B(1) String[], @C(\'-\') X>foo(new String[]{\"SUCCESS\"}, null)[0]);\n" + 
		"	                                          ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n");
	}
	public void test029() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"public class X extends @Marker2 Object {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X extends @Marker2 Object {}\n" + 
				"	                        ^^^^^^^\n" + 
				"Marker2 cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void test030() throws Exception {
		this.runNegativeTest(
				new String[] {
					"X.java",
					"import java.io.Serializable;\n" +
					"public class X implements @Marker2 Serializable {\n" +
					"	private static final long serialVersionUID = 1L;\n" +
					"}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 2)\n" + 
				"	public class X implements @Marker2 Serializable {\n" + 
				"	                           ^^^^^^^\n" + 
				"Marker2 cannot be resolved to a type\n" + 
				"----------\n");
	}
	public void _test031() throws Exception {
		this.runNegativeTest(
				new String[] {
					"Marker.java",
					"import java.lang.annotation.Target;\n" + 
					"import static java.lang.annotation.ElementType.*;\n" + 
					"@Target(TYPE_USE)\n" + 
					"@interface Marker {}",
					"X.java",
					"public class X<@Marker T> {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X<@Marker T> {}\n" + 
				"	               ^^^^^^^\n" + 
				"The annotation @Marker is disallowed for this location\n" + 
				"----------\n");
	}
	public void _test032() throws Exception {
		this.runNegativeTest(
				new String[] {
					"Marker.java",
					"@interface Marker {}",
					"X.java",
					"public class X<@Marker T> {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X<@Marker T> {}\n" + 
				"	               ^^^^^^^\n" + 
				"The annotation @Marker is disallowed for this location\n" + 
				"----------\n");
	}
	public void _test033() throws Exception {
		this.runNegativeTest(
				new String[] {
					"Marker.java",
					"@interface Marker {}",
					"Y.java",
					"public class Y {}",
					"X.java",
					"public class X extends @Marker Y {}",
				},
				"----------\n" + 
				"1. ERROR in X.java (at line 1)\n" + 
				"	public class X extends @Marker Y {}\n" + 
				"	                       ^^^^^^^\n" + 
				"The annotation @Marker is disallowed for this location\n" + 
				"----------\n");
	}
	// check locations
	public void test034() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"import java.util.Map;\n" +
				"import java.util.List;\n" +
				"public class X {\n" + 
				"	@H String @E[] @F[] @G[] field;\n" + 
				"	@A Map<@B String, @C List<@D Object>> field2;\n" + 
				"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	@H String @E[] @F[] @G[] field;\n" + 
		"	 ^\n" + 
		"H cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 4)\n" + 
		"	@H String @E[] @F[] @G[] field;\n" + 
		"	           ^\n" + 
		"E cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 4)\n" + 
		"	@H String @E[] @F[] @G[] field;\n" + 
		"	                ^\n" + 
		"F cannot be resolved to a type\n" + 
		"----------\n" + 
		"4. ERROR in X.java (at line 4)\n" + 
		"	@H String @E[] @F[] @G[] field;\n" + 
		"	                     ^\n" + 
		"G cannot be resolved to a type\n" + 
		"----------\n" + 
		"5. ERROR in X.java (at line 5)\n" + 
		"	@A Map<@B String, @C List<@D Object>> field2;\n" + 
		"	 ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"6. ERROR in X.java (at line 5)\n" + 
		"	@A Map<@B String, @C List<@D Object>> field2;\n" + 
		"	        ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"7. ERROR in X.java (at line 5)\n" + 
		"	@A Map<@B String, @C List<@D Object>> field2;\n" + 
		"	                   ^\n" + 
		"C cannot be resolved to a type\n" + 
		"----------\n" + 
		"8. ERROR in X.java (at line 5)\n" + 
		"	@A Map<@B String, @C List<@D Object>> field2;\n" + 
		"	                           ^\n" + 
		"D cannot be resolved to a type\n" + 
		"----------\n" + 
		"9. ERROR in X.java (at line 6)\n" + 
		"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
		"	 ^\n" + 
		"A cannot be resolved to a type\n" + 
		"----------\n" + 
		"10. ERROR in X.java (at line 6)\n" + 
		"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
		"	        ^\n" + 
		"B cannot be resolved to a type\n" + 
		"----------\n" + 
		"11. ERROR in X.java (at line 6)\n" + 
		"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
		"	                   ^\n" + 
		"H cannot be resolved to a type\n" + 
		"----------\n" + 
		"12. ERROR in X.java (at line 6)\n" + 
		"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
		"	                             ^\n" + 
		"E cannot be resolved to a type\n" + 
		"----------\n" + 
		"13. ERROR in X.java (at line 6)\n" + 
		"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
		"	                                  ^\n" + 
		"F cannot be resolved to a type\n" + 
		"----------\n" + 
		"14. ERROR in X.java (at line 6)\n" + 
		"	@A Map<@B String, @H String @E[] @F[] @G[]> field3;\n" + 
		"	                                       ^\n" + 
		"G cannot be resolved to a type\n" + 
		"----------\n");
	}
	// check locations
	public void test035() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"import java.util.Map;\n" +
				"import java.util.List;\n" +
				"public class X {\n" + 
				"	@H java.lang.String @E[] @F[] @G[] field;\n" + 
				"}",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 4)\n" + 
		"	@H java.lang.String @E[] @F[] @G[] field;\n" + 
		"	 ^\n" + 
		"H cannot be resolved to a type\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 4)\n" + 
		"	@H java.lang.String @E[] @F[] @G[] field;\n" + 
		"	                     ^\n" + 
		"E cannot be resolved to a type\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 4)\n" + 
		"	@H java.lang.String @E[] @F[] @G[] field;\n" + 
		"	                          ^\n" + 
		"F cannot be resolved to a type\n" + 
		"----------\n" + 
		"4. ERROR in X.java (at line 4)\n" + 
		"	@H java.lang.String @E[] @F[] @G[] field;\n" + 
		"	                               ^\n" + 
		"G cannot be resolved to a type\n" + 
		"----------\n");
	}
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=383884 -- Compiler tolerates illegal dimension annotation in class literal expressions
	public void test036() throws Exception {
		this.runNegativeTest(
			new String[] {
				"X.java",
				"public class X {\n" +
				"  public static void main(String[] args) {\n" +
				"    System.out.println(int @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" +
				"    System.out.println(X @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" +
				"    System.out.println(int [] [] [] [] [].class);\n" +
				"    System.out.println(X [] [] [] [] [].class);\n" +
				"  }\n" +
				"}\n" +
				"@interface Empty {\n" +
				"}\n" +
				"@interface NonEmpty {\n" +
				"}\n",
		},
		"----------\n" + 
		"1. ERROR in X.java (at line 3)\n" + 
		"	System.out.println(int @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" + 
		"	                       ^^^^^^^^^\n" + 
		"Syntax error, type annotations are illegal here\n" + 
		"----------\n" + 
		"2. ERROR in X.java (at line 3)\n" + 
		"	System.out.println(int @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" + 
		"	                                       ^^^^^^^^^^^^^^^^\n" + 
		"Syntax error, type annotations are illegal here\n" + 
		"----------\n" + 
		"3. ERROR in X.java (at line 3)\n" + 
		"	System.out.println(int @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" + 
		"	                                                              ^^^^^^^^^\n" + 
		"Syntax error, type annotations are illegal here\n" + 
		"----------\n" + 
		"4. ERROR in X.java (at line 4)\n" + 
		"	System.out.println(X @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" + 
		"	                     ^^^^^^^^^\n" + 
		"Syntax error, type annotations are illegal here\n" + 
		"----------\n" + 
		"5. ERROR in X.java (at line 4)\n" + 
		"	System.out.println(X @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" + 
		"	                                     ^^^^^^^^^^^^^^^^\n" + 
		"Syntax error, type annotations are illegal here\n" + 
		"----------\n" + 
		"6. ERROR in X.java (at line 4)\n" + 
		"	System.out.println(X @NonEmpty [] [] @NonEmpty @Empty [] [] @NonEmpty[].class); // illegal!\n" + 
		"	                                                            ^^^^^^^^^\n" + 
		"Syntax error, type annotations are illegal here\n" + 
		"----------\n");
	}
}