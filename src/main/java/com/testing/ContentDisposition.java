package com.testing;

public class ContentDisposition {

	public static void main(String[] args) {
		String filePath = "form-data; name=\"file\"; filename=\"myfile.txt\"";
		String[] elements = filePath.split(";");
		for(String element:elements) {
		System.out.println(element);
		}
		for(String element:elements) {
			System.out.println(element.trim().startsWith("filename"));
			if(element.trim().startsWith("filename")) {
				System.out.println(element.substring(element.indexOf("=")+1).trim().replaceAll("\"",""));
			}
		}
	}

}
