package org.cae.service.impl;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.cae.common.ServiceResult;
import org.cae.dao.ICallDao;
import org.cae.dao.ISongDao;
import org.cae.service.IUploadService;
import org.springframework.stereotype.Service;

import org.cae.common.Util;
@Service("uploadService")
public class UploadServiceImpl implements IUploadService{
	public static final String destPath="D:/nginx-1.12.0/html/aqours";
	public static final Charset charset=Charset.forName("gbk");
	private Logger logger=Logger.getLogger(this.getClass().getName());
	
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


}
