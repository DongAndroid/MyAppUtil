package com.example.myapputil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.provider.ContactsContract;

/**
 * 作者：刘海东<P/>
 * 
 * 方法：<P/>
 * <P/>
 * 获取通讯录        getAllCallRecords(Context context)<P/><P/>
 * 
 * 
 *
 */
public class MyAppUtil {
	MyAppUtil my;
	//私有化构造方法
	private MyAppUtil() {
		super();
	}


	//线程同步，确保线程安全
	public synchronized MyAppUtil getinterface(){
		if(my==null){
			my=new MyAppUtil();
		}
		return my;

	}
	/**
	 *
	 * 获取通讯录，返回map<String,String>
	 * 
	 * 
	 */
	//获取通讯录联系人map
	private  Map<String,String> getAllCallRecords(Context context) { 
		Map<String,String> temp = new HashMap<String, String>(); 
		Cursor c = context.getContentResolver().query( 
				ContactsContract.Contacts.CONTENT_URI, 
				null, 
				null, 
				null, 
				ContactsContract.Contacts.DISPLAY_NAME 
				+ " COLLATE LOCALIZED ASC"); 
		if (c.moveToFirst()) { 
			do { 
				// 获得联系人的ID号 
				String contactId = c.getString(c 
						.getColumnIndex(ContactsContract.Contacts._ID)); 
				// 获得联系人姓名 
				String name = c 
						.getString(c 
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
				// 查看该联系人有多少个电话号码。如果没有这返回值为0 
				int phoneCount = c 
						.getInt(c 
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
				String number=null; 
				if (phoneCount > 0) { 
					// 获得联系人的电话号码 
					Cursor phones = context.getContentResolver().query( 
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID 
							+ " = " + contactId, null, null); 
					if (phones.moveToFirst()) { 
						number = phones 
								.getString(phones 
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
					} 
					phones.close(); 
				} 
				temp.put(name, number); 
			} while (c.moveToNext()); 
		} 
		c.close(); 
		return temp; 
	}

	/**
	 *
	 * 获取星期几，返回String星期
	 * 
	 * 
	 */

	//获取星期几
	private String getWeek(String castTime){
		String week="";
		Calendar c=Calendar.getInstance();
		SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
		try{
			try {
				c.setTime(s.parse(castTime));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(ParseException e){
			e.printStackTrace();
		}
		if(c.get(Calendar.DAY_OF_WEEK)==1){week="周日";}
		if(c.get(Calendar.DAY_OF_WEEK)==2){week="周一";}
		if(c.get(Calendar.DAY_OF_WEEK)==3){week="周二";}
		if(c.get(Calendar.DAY_OF_WEEK)==4){week="周三";}
		if(c.get(Calendar.DAY_OF_WEEK)==5){week="周四";}
		if(c.get(Calendar.DAY_OF_WEEK)==6){week="周五";}
		if(c.get(Calendar.DAY_OF_WEEK)==7){week="周六";}
		return week;
	}




	/**
	 *
	 * 删除文件夹和文件夹里面的文件
	 * 
	 * 
	 */

	//删除文件夹和文件夹里面的文件
	public  void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(file.getPath()); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}



	/** 
	 * 删除应用数据库文件夹
	 *  
	 * @param directory 
	 */ 

	public  void cleanDatabases(Context context) {  
		deleteFilesByDirectory(new File("/data/data/"  
				+ context.getPackageName() + "/databases"));  
	}  
	/** 
	 * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 
	 *  
	 * @param File 
	 */  
	private  void deleteFilesByDirectory(File directory) {  
		if (directory != null && directory.exists() && directory.isDirectory()) {  
			for (File item : directory.listFiles()) {  
				item.delete();  
			}  
		}  
	}  
	/** 
	 * 统计目录下文件大小，返回单位k
	 *  
	 * @param String路径
	 */ 
	//统计缓存大小
	public  long cachesize(String path) {
		long size=0;
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return 0;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				size+=file.length(); // 删除所有文件
				else if (file.isDirectory())
					cachesize(file.getPath()); // 递规的方式删除文件夹
		}

		return size/1000;
	}


















}
