package org.cae.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.cae.common.DaoResult;
import org.cae.dao.ICallDao;
import org.cae.entity.CallRecord;

@Repository("callDao")
public class CallDaoImpl implements ICallDao{
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public DaoResult updateCallVersionDao(final List<String> songIds, final CallRecord callRecord){
		try {
			String sql="UPDATE call_record "
						+ "SET call_version = ? "
						+ "WHERE song_id IN (";
			for(int i=0;i<songIds.size();i++){
				if(i==songIds.size()-1){
					sql+="?)";
				}
				else{
					sql+="?,";
				}
			}
			template.update(sql, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setShort(1, callRecord.getCallVersion());
					for(int i=2;i<=songIds.size()+1;i++){
						ps.setString(i, songIds.get(i-2));
					}
				}
			});
			return new DaoResult(true,null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
