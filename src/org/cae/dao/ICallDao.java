package org.cae.dao;

import java.util.List;

import org.cae.common.DaoResult;

public interface ICallDao {

	DaoResult updateCallVersionDao(List<String> songNames);
}
