package org.cae.dao;

import java.util.List;

import org.cae.common.DaoResult;

public interface ISongDao {

	DaoResult<String> updateSongTimeDao(List<String> songNames);
}
