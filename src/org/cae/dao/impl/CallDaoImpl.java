package org.cae.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.cae.common.DaoResult;
import org.cae.dao.ICallDao;
@Repository("callDao")
public class CallDaoImpl implements ICallDao{
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public DaoResult updateCallVersionDao(List<String> songNames){
		try {
			 String max_sql="SELECT MAX(call_version) AS max_version "
			 			+ "FROM call_record ";
				List<Map<String,Object>> max =template.queryForList(max_sql);
				Integer max_version =(Integer) max.get(0).get("max_version")+1;
			String sql="UPDATE call_record "
					 + "LEFT JOIN song "
					 + "USING(song_id) "
					 + "SET call_version = "+max_version+" "
					 + "WHERE song_name IN(?) ";
			List<Object[]> batchArgs=new ArrayList<Object[]>();
			for(int i=0;i<songNames.size();i++){
				Object[] objectArray=new Object[1];
				objectArray[0]=songNames.get(i);
				batchArgs.add(objectArray);
			}
			template.batchUpdate(sql, batchArgs);
			return new DaoResult(true,null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
