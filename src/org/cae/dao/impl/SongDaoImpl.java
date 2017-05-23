package org.cae.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cae.common.DaoResult;
import org.cae.common.Util;
import org.cae.dao.ISongDao;
import org.cae.entity.Song;
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
	public DaoResult<String> updateSongTimeDao(final List<String> songNames){
		DaoResult<String> theResult=null;
		try {
			String sql="UPDATE song "
					+ "SET song_last_modify_time = ? "
					+ "WHERE song_name = ? ";
			//result数组代表每条sql的影响条数,如果更新成功的话对应位置的值应该是1
			//如果是0的话就说明0行被影响,这里就可以等同于“没有找到这样一条song记录”
			int[] result=template.batchUpdate(sql, new BatchPreparedStatementSetter(){
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
			List<String> failList=new ArrayList<String>();
			for(int i=0;i<result.length;i++){
				//将在数据库不存在的歌曲加入failList中
				if(result[i]==0){
					failList.add(songNames.get(i));
				}
			}
			//上面是计算出不存在歌曲信息的歌曲名单,下面是查询所有songId
			//这两步不能反过来,因为需要把不存在歌曲信息的歌曲名单返回给客户端,并不是在这里简单地过滤掉就行
			//查询所有的songId以便之后更新版本号
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
			List<String> list=new ArrayList<String>();
			list=template.query(sql, songNames.toArray(), new RowMapper<String>(){

				@Override
				public String mapRow(ResultSet rs, int row)
						throws SQLException {
					return rs.getString("song_id");
				}
				
			});
			if(failList.size()==0){
				theResult=new DaoResult<String>(true, list);
			}
			else{
				theResult=new DaoResult<String>(false, list, failList);
				theResult.setErrInfo("没有找到部分歌曲");
			}
			return theResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Song> getSongNameById(final List<String> songIds) {
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
}
