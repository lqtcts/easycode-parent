package com.annotation;

import java.lang.annotation.Annotation;

public class TestPkgAnnotation {

	public static void main(String[] args) {
		Package pkg = Package.getPackage("com.annotation");
		Annotation[] annotations = pkg.getAnnotations();
		for (Annotation annotation : annotations) {
			System.out.println(annotation);
		}

		// ===========================友好类和包内访问常量==============
		new MyPackageMethod().myPackageMethod();
		System.out.println(MyPackageConst.PACKAGE_STRING);
	}
}
