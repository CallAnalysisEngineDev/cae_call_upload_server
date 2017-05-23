package org.cae.dao;

import java.util.List;

import org.cae.common.DaoResult;
import org.cae.entity.Song;

public interface ISongDao {

	DaoResult<String> updateSongTimeDao(List<String> songNames);
	
	List<Song> getSongNameById(List<String> songIds);
}
