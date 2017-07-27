package org.cae.service;

import java.io.File;
import java.io.InputStream;

import org.cae.common.ServiceResult;
import org.cae.entity.CallRecord;

public interface IUploadService {

	// 下载时call表压缩后的输出路径
	final static String DOWNLOAD_ZIP_PATH = "result.zip";

	// call表在nginx下的文件夹路径
	final static String DOWNLOAD_HTML_PATH = "D:\\develop\\nginx-1.12.1\\cae\\resource\\html\\aqours";

	// final static String DOWNLOAD_HTML_PATH =
	// "C:\\Program Files\\nginx\\cae\\resource\\html\\aqours";

	ServiceResult uploadCallService(InputStream input, CallRecord callRecord);

	File downloadCallService();
}
