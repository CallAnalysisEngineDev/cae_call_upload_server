package org.cae.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	private static SimpleDateFormat dateSdf=new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeSdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String destPath="D:\\nginx-1.12.0\\html\\aqours";
	public static final Charset charset=Charset.forName("gbk");
	
	public static String toJson(Object target){
		ObjectMapper mapper=new ObjectMapper();
		try{
			return mapper.writeValueAsString(target);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String date2String(Date date){
		return dateSdf.format(date);
	}
	
	public static String time2String(Date date){
		return timeSdf.format(date);
	}

	public static String getNowDate(){
		return date2String(new Date());
	}
	
	public static String getNowTime(){
		return time2String(new Date());
	}
	
	public static String getBefore(long time){
		return time2String(new Date(System.currentTimeMillis()-time));
	}
	
	public static String getCharId(){
		return getCharId(new String(), 10);
	}
	
	public static String getCharId(int size){
		return getCharId(new String(),size);
	}
	
	public static String getCharId(String pre,int size){
		StringBuffer theResult=new StringBuffer();
		theResult.append(pre);
		String a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int i=0;i<size-pre.length();i++){
			int rand =(int)(Math.random() * a.length());
			theResult.append(a.charAt(rand));
		}
		return theResult.toString();
	}
	
	public static short getRandom(int randomRange){
		Random random=new Random();
		return (short) random.nextInt(randomRange);
	}
	
	public static boolean isNotNull(Object object){
		boolean result=false;
		if(object==null)
			return result;
		if(object instanceof String){
			String temp=(String) object;
			if(temp!=null&&!temp.equals(""))
				result=true;
			else
				result=false;
		}
		return result;
	}
	
	public static void logStackTrace(Logger logger,StackTraceElement[] stackTrace){
		String stackInfo="";
		for(StackTraceElement element:stackTrace){
			stackInfo+=element+"\n";
		}
		logger.error(stackInfo);
	}
	
	public  static List<String> unZip(InputStream in){

		ZipInputStream zipInputStream = new ZipInputStream(in,charset);
		ZipEntry entry=null;
		List<String> songName =new ArrayList<>();
	try{
		while((entry=zipInputStream.getNextEntry()) != null){
			String tempName = (destPath+File.separator+entry.getName());//临时路径
			File file = new File(tempName);
			String fileName=file.getName().trim();//文件名
			String filePath=destPath+File.separator+fileName;//真正的文件路径
			if((!tempName.endsWith(".html"))||new File(tempName).isDirectory()){
            	continue;
            }
			songName.add(fileName.substring(0, fileName.lastIndexOf(".")));
            OutputStream out = new FileOutputStream(filePath);
            byte[] buf1 = new byte[1024];
            int len;
            while((len=zipInputStream.read(buf1))>0){
                out.write(buf1,0,len);
            }
            
            out.close();
		}
		zipInputStream.close();
		return songName;
	}catch (Exception e) {
		e.printStackTrace();
		return new ArrayList<>();
	}
	}
}
