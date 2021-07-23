package com.taxol.temple_callback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	/*
	public Integer calcSum(String filepath) throws IOException {
		//템플릿 콜백을 적용
		BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException {
				Integer sum = 0;
				String line = null;
				while((line = br.readLine()) != null) {
					sum += Integer.valueOf(line);		// Integer 반환
				}
				return sum;
			}
		};
		
		return fileReadTemplate(filepath, sumCallback);
	}
	

	public Object calcMultiply(String filepath) {
		//템플릿 콜백을 적용
		BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException {
				Integer multiply = 1;
				String line = null;
				while((line = br.readLine()) != null) {
					multiply *= Integer.valueOf(line);		// Integer 반환
				}
				return multiply;
			}
		};
		return fileReadTemplate(filepath, multiplyCallback);
	}
	*/
	
	public Integer calcSum(String filepath) throws IOException {
		//템플릿 콜백을 적용
		LineCallback sumCallback = new LineCallback() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value + Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, sumCallback, 0);
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		//템플릿 콜백을 적용
		LineCallback multiplyCallback = new LineCallback() {
			
			@Override
			public Integer doSomethingWithLine(String line, Integer value) {
				return value * Integer.valueOf(line);
			}
		};
		return lineReadTemplate(filepath, multiplyCallback, 1);
	}
	
	public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			Integer res = initVal;
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
		return -1;
	}
	
	// BufferedReaderCallback을 사용하는 템플릿 메서드
	public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			return callback.doSomethingWithReader(br);
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
		return -1;
	}

}
