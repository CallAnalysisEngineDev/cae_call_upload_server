package org.cae.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.cae.common.DaoResult;
import org.cae.common.Util;
import org.cae.dao.ISongDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("songDao")
public class SongDaoImpl implements ISongDao{
	@Autowired
	private JdbcTemplate template;
	@Override
	public DaoResult updateSongTimeDao(List<String> songNames){
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
			return new DaoResult(true, null);
		} catch (Exception e) {
			return null;
		}
	}
}
