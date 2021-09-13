package com.taxol.domain.sqlservice;

//SQL에 대한 키 값을 전달하면 그에 해당하는 SQL을 돌려주면 된다.
public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
