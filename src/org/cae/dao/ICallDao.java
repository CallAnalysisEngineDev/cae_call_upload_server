package org.cae.dao;

import java.util.List;

import org.cae.common.DaoResult;
import org.cae.entity.CallRecord;

public interface ICallDao {

	DaoResult<CallRecord> updateCallVersionDao(List<String> songIds,
			CallRecord callRecord);
}
