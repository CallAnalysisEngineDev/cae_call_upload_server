package org.cae.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.cae.common.ServiceResult;
import org.cae.dao.ICallDao;
import org.cae.dao.ISongDao;
import org.cae.service.IUploadService;
import org.springframework.stereotype.Service;

import org.cae.common.Util;

@Service("uploadService")
public class UploadServiceImpl implements IUploadService{
	
	@Resource(name="callDao")
	private ICallDao callDao;
	@Resource(name="songDao")
	private ISongDao songDao;
	
	@Override
	public ServiceResult uploadCallService(InputStream input){
		ServiceResult result=null;
			List<String> songName=Util.unZip(input);
			if(songName.size()==0){
				result=new ServiceResult();
				result.setSuccessed(false);
				result.setErrInfo("文件解压失败");
			}
			if(songDao.updateSongTimeDao(songName).isResult()
					&&callDao.updateCallVersionDao(songName).isResult()){
					result=new ServiceResult();
					result.setSuccessed(true);
			}else{
				result=new ServiceResult();
				result.setSuccessed(false);
				result.setErrInfo("数据库更新失败");
			}
		return result;
	}

	@Override
	public File downloadCallService() {
		File file=null;
		try {
			Util.ZipFiles(DOWNLOAD_ZIP_PATH,"",DOWNLOAD_HTML_PATH);
			file=new File(DOWNLOAD_ZIP_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}


}
