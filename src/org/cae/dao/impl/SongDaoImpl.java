package org.cae.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.cae.common.Util;
import org.cae.dao.ISongDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("songDao")
public class SongDaoImpl implements ISongDao{
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public List<String> updateSongTimeDao(final List<String> songNames){
		try {
			String sql="UPDATE song "
					+ "SET song_last_modify_time = ? "
					+ "WHERE song_name = ? ";
			template.batchUpdate(sql, new BatchPreparedStatementSetter(){
				@Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {  
					ps.setString(1, Util.getNowDate());
					ps.setString(2, songNames.get(i).trim());
                }

				@Override
				public int getBatchSize() {
					return songNames.size();
				}   
			});
			sql="SELECT song_id "
				+ "FROM song "
				+ "WHERE song_name IN(";
			for(int i=0;i<songNames.size();i++){
				if(i==songNames.size()-1){
					sql+="?)";
				}
				else{
					sql+="?,";
				}
			}
			List<String> theResult=new ArrayList<String>();
			theResult=template.query(sql, songNames.toArray(), new RowMapper<String>(){

				@Override
				public String mapRow(ResultSet rs, int row)
						throws SQLException {
					return rs.getString("song_id");
				}
				
			});
			return theResult;
		} catch (Exception e) {
			return null;
		}
	}
}
