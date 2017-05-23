package org.cae.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.apache.log4j.Logger;
import org.cae.common.DaoResult;
import org.cae.common.Util;
import org.cae.dao.ICallDao;
import org.cae.dao.ISongDao;
import org.cae.entity.CallRecord;
import org.cae.entity.Song;

@Repository("callDao")
public class CallDaoImpl implements ICallDao{
	
	@Autowired
	private ISongDao songDao;
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
			//result数组代表每条sql的影响条数,如果更新成功的话对应位置的值应该是1
			//如果是0的话就说明0行被影响,这里就可以等同于“有歌曲信息但是之前没有录入call_record记录”
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
			//将“有歌曲信息但是之前没有录入call_record记录”的名单记录下来,然后添加到call_record表中
			List<String> failList=new ArrayList<String>();
			for(int i=0;i<result.length;i++){
				if(result[i]==0){
					failList.add(songIds.get(i));
				}
			}
			if(failList.size()>0){
				String warnInfo="有一些歌曲只有歌曲信息没有具体的call表信息,名单如下:[";
				List<Song> songs=songDao.getSongNameById(failList);
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
