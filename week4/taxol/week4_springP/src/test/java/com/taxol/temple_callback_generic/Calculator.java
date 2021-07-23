package com.taxol.temple_callback_generic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public Integer calcSum(String filepath) throws IOException {
		//템플릿 콜백을 적용
		LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, sumCallback, 0);
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		//템플릿 콜백을 적용
		LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value * Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, multiplyCallback, 1);
	}
	
	public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			T res = initVal;
			String line = null;
			while((line = br.readLine()) != null) {
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			if(br != null) {
				try { br.close(); }
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return initVal;
	}
	
	
	public String concatenate(String filepath) throws IOException {
		LineCallback<String> concatenateCallback = new LineCallback<String>() {
			
			@Override
			public String doSomethingWithLine(String line, String value) {
				return value + line;
			}
		};
		
		return lineReadTemplate(filepath, concatenateCallback, "");
	}
	
}
