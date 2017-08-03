package org.cae.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.cae.service.IUploadService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	private static Logger logger = Logger.getLogger(Util.class);
	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final Charset charset = Charset.forName("gbk");
	public static final byte[] buf = new byte[1024];

	public static String toJson(Object target) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(target);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String date2String(Date date) {
		return dateSdf.format(date);
	}

	public static String time2String(Date date) {
		return timeSdf.format(date);
	}

	public static String getNowDate() {
		return date2String(new Date());
	}

	public static String getNowTime() {
		return time2String(new Date());
	}

	public static String getBefore(long time) {
		return time2String(new Date(System.currentTimeMillis() - time));
	}

	public static String getCharId() {
		return getCharId(new String(), 10);
	}

	public static String getCharId(int size) {
		return getCharId(new String(), size);
	}

	public static String getCharId(String pre, int size) {
		StringBuffer theResult = new StringBuffer();
		theResult.append(pre);
		String a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < size - pre.length(); i++) {
			int rand = (int) (Math.random() * a.length());
			theResult.append(a.charAt(rand));
		}
		return theResult.toString();
	}

	public static short getRandom(int randomRange) {
		Random random = new Random();
		return (short) random.nextInt(randomRange);
	}

	public static boolean isNotNull(Object object) {
		boolean result = false;
		if (object == null)
			return result;
		if (object instanceof String) {
			String temp = (String) object;
			if (temp != null && !temp.equals(""))
				result = true;
			else
				result = false;
		}
		return result;
	}

	/**
	 * 解压文件
	 * 
	 * @param in文件输入流
	 * @return 解压的文件名集合
	 */
	public static List<String> unZip(InputStream in) {

		ZipInputStream zipInputStream = new ZipInputStream(in, charset);
		ZipEntry entry = null;
		List<String> songName = new ArrayList<>();
		try {
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String tempName = (IUploadService.DOWNLOAD_HTML_PATH
						+ File.separator + entry.getName());// 临时路径
				File file = new File(tempName);
				String fileName = file.getName().trim();// 文件名
				String filePath = IUploadService.DOWNLOAD_HTML_PATH
						+ File.separator + fileName;// 真正的文件路径
				if ((!tempName.endsWith(".html"))
						|| new File(tempName).isDirectory()) {
					continue;
				}
				songName.add(fileName.substring(0, fileName.lastIndexOf(".")));
				OutputStream out = new FileOutputStream(filePath);
				int len;
				while ((len = zipInputStream.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				out.close();
			}
			zipInputStream.close();
			return songName;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param zip
	 *            生成的Zip文件名
	 * @param base
	 *            压入Zip的目录，默认为根目录
	 * @param filePath
	 *            压缩的文件路径
	 * @throws IOException
	 */
	public static void ZipFiles(String zip, String base, String filePath)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
		File[] srcFiles = new File(filePath).listFiles();
		ZipFiles(out, base, srcFiles);
		out.close();
	}

	/**
	 * 压缩文件(原始方法)
	 * 
	 * @param out
	 *            压缩输出流
	 * @param base
	 *            压入Zip的目录，默认为根目录
	 * @param srcFiles
	 *            压缩的文件路径
	 */
	public static void ZipFiles(ZipOutputStream out, String base,
			File[] srcFiles) {
		base = base.replaceAll("\\*", "/");
		byte[] buf = new byte[1024];
		try {
			for (int i = 0; i < srcFiles.length; i++) {
				if (srcFiles[i].isDirectory()) {
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if (!srcPath.endsWith("/")) {
						srcPath += "/";
					}
					out.putNextEntry(new ZipEntry(base + srcPath));
					ZipFiles(out, base + srcPath, files);
				} else {
					FileInputStream in = new FileInputStream(srcFiles[i]);
					out.putNextEntry(new ZipEntry(base + srcFiles[i].getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteFiles(List<String> filenames) {
		File file;
		for (String filename : filenames) {
			file = new File(IUploadService.DOWNLOAD_HTML_PATH + "\\" + filename
					+ ".html");
			file.delete();
		}
	}

}
