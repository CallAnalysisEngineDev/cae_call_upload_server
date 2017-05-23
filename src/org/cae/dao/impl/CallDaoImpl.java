package org.cae.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;
import org.cae.common.DaoResult;
import org.cae.common.Util;
import org.cae.dao.ICallDao;
import org.cae.entity.CallRecord;
import org.cae.entity.Song;

@Repository("callDao")
public class CallDaoImpl implements ICallDao{
	
	private Logger logger=Logger.getLogger(getClass());
	
	//call_record表中call_source字段的水团call表固定前缀
	private final static String CALL_SOURCE_PREFIX = "/aqours/";
	
	@Autowired
	private JdbcTemplate template;
	
	@Override
	public DaoResult<CallRecord> updateCallVersionDao(final List<String> songIds, final CallRecord callRecord){
		try {
			String sql="UPDATE call_record "
						+ "SET call_version = ? "
						+ "WHERE song_id = ?";
			int[] result=template.batchUpdate(sql, new BatchPreparedStatementSetter(){
				@Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {  
					ps.setShort(1, callRecord.getCallVersion());
					ps.setString(2, songIds.get(i).trim());
                }

				@Override
				public int getBatchSize() {
					return songIds.size();
				}   
			});
			List<String> failList=new ArrayList<String>();
			for(int i=0;i<result.length;i++){
				if(result[i]==0){
					failList.add(songIds.get(i));
				}
			}
			if(failList.size()>0){
				String warnInfo="有一些歌曲只有歌曲信息没有具体的call表信息,名单如下:[";
				List<Song> songs=getSongById(failList);
				for(int i=0;i<songs.size();i++){
					if(i==songs.size()-1){
						warnInfo+=songs.get(i).getSongName()+"]";
					}
					else{
						warnInfo+=songs.get(i).getSongName()+",";
					}
				}
				logger.warn(warnInfo);
				logger.warn("即将为上述名单的歌曲添加call表记录");
				saveCallDao(songs,callRecord);
				logger.warn("添加完毕");
			}
			return new DaoResult<CallRecord>(true,null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private List<Song> getSongById(final List<String> songIds){
		String sql="SELECT song_name "
				+ "FROM song "
				+ "WHERE song_id IN(";
		for(int i=0;i<songIds.size();i++){
			if(i==songIds.size()-1){
				sql+="?)";
			}
			else{
				sql+="?,";
			}
		}
		//对songIds进行排序,以便与rowmapper中的ResultSet中的歌名一一对应
		Collections.sort(songIds);
		return template.query(sql, songIds.toArray(), new RowMapper<Song>(){

			@Override
			public Song mapRow(ResultSet rs, int row) throws SQLException {
				Song song=new Song(songIds.get(row));
				song.setSongName(rs.getString("song_name"));
				return song;
			}
			
		});
	}
	
	private void saveCallDao(final List<Song> songs, final CallRecord callRecord){
		String sql="INSERT INTO call_record(call_id,song_id,call_source,call_version) "
				+ "VALUES(?,?,?,?)";
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int count) throws SQLException {
				Song song=songs.get(count);
				ps.setString(1, Util.getCharId("CR-", 10));
				ps.setString(2, song.getSongId());
				ps.setString(3, CALL_SOURCE_PREFIX+song.getSongName()+".html");
				ps.setShort(4, callRecord.getCallVersion());
			}
			
			@Override
			public int getBatchSize() {
				return songs.size();
			}
		});
	}
}
