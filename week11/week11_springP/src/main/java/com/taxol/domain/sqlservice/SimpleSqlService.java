package com.taxol.domain.sqlservice;

import java.util.Map;

public class SimpleSqlService implements SqlService {
	private Map<String, String> sqlMap; // sql정보는 이 프로퍼티에 <map>을 이용해 등록.
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException {
		String sql = sqlMap.get(key);
		if(sql == null)
			throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
		else
			return sql;
	}

}
