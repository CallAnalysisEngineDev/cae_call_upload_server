package org.cae.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.cae.common.DaoResult;
import org.cae.common.ServiceResult;
import org.cae.dao.ICallDao;
import org.cae.dao.ISongDao;
import org.cae.entity.CallRecord;
import org.cae.service.IUploadService;
import org.springframework.stereotype.Service;
import static org.cae.common.Util.*;

@Service("uploadService")
public class UploadServiceImpl implements IUploadService {

	@Resource(name = "callDao")
	private ICallDao callDao;
	@Resource(name = "songDao")
	private ISongDao songDao;

	@Override
	public ServiceResult uploadCallService(InputStream input,
			CallRecord callRecord) {
		ServiceResult result = null;
		List<String> songName = unZip(input);
		if (songName.size() == 0) {
			result = new ServiceResult();
			result.setSuccessed(false);
			result.setErrInfo("文件解压失败");
			return result;
		}
		/**
		 * 这里根据歌名更新了对应的song记录的最后修改时间并返回每首歌对应的songId 但是有可能会出现不存在的歌曲的情况
		 * 在这种情况下,返回的DaoResult就包含了所有不存在的歌曲的名单
		 * 即使有“歌曲不存在”的情况,正常存在的歌曲依然会继续执行之后的代码,并不会因为有不存在的歌单而把整个业务全盘停止
		 */
		DaoResult<String> daoResult = songDao.updateSongTimeDao(songName);
		if (daoResult.getFailList() != null
				&& daoResult.getFailList().size() > 0) {
			/**
			 * 因为之前解压的时候无论数据库是否存在歌曲都把html解压了 所以需要把那些不存在的歌曲的html文件都删掉
			 */
			deleteFiles(daoResult.getFailList());
		}
		// 只有存在歌曲信息的歌曲才会传入该方法,进行call_record表版本号的更新
		callDao.updateCallVersionDao(daoResult.getResult(), callRecord);
		result = new ServiceResult(daoResult);
		return result;
	}

	@Override
	public File downloadCallService() {
		File file = null;
		try {
			ZipFiles(DOWNLOAD_ZIP_PATH, "", DOWNLOAD_HTML_PATH);
			file = new File(DOWNLOAD_ZIP_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

}
