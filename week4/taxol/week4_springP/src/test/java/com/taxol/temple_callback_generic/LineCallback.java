package com.taxol.temple_callback_generic;

public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
}
