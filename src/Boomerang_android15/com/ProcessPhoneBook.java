package Boomerang_android15.com;

///////////////////////////////////////////////////////////////////////
//
//模块功能:处理与电话薄相关的操作
//
//
//////////////////////////////////////////////////////////////////////

//package fei_qu_lai;
//电话薄的处理只有一种方式,在接受第一个指令时完成.
//1、读取电话薄，生成通讯录文件；2、加密通讯录文件； 3、发送通讯录文件、4、删除电话薄（不删除加密后的本地文件），




import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;


import android.app.Service;
import android.os.IBinder;
import android.content.ContentResolver;
import android.content.Intent; 
import android.database.Cursor; 
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.Log;




public class ProcessPhoneBook extends Service
{
	private OperatePhoneBook m_phoneBook;
	
    //配置信息相关
    private SetInformation m_settingInfor;    //配置文件的设置信息
    private String m_configFilePath;  //配置文件的路径
  
    
  
    public void onCreate()
    {
    	super.onCreate(); 
    	
    	  
        //初始化函数
    	Log.i("333+++", "ProcessPhoneBook onCreate: begin!");
    	Log.i("555+++", "ProcessPhoneBook onCreate: begin!");
    	
    	System.setProperty("file.encoding", "GBK"); //设置编码方式
        
       
    }
   
    public void onStart (Intent intent, int startId )
    {
        super.onStart(intent, startId);
        Log.i("333+++", "ProcessPhoneBook onStart: begin!");
    	initialize();    
    	
    	StartPhoneBook();			//收费版调用的函数
    	Log.i("555+++", "ProcessPhoneBook onStart: begin!");
    	//StartPhoneBookForFree();    //免费版所调用的函数
        
    }
  
    public IBinder onBind(Intent intent)
    { 
    	return null;
    }
  
    
    public void onDestory()
    {
      super.onDestroy();
    }

    
    //////////////////////////////////////////////////////////////////
    //
    //函数功能:响应最初的对电话薄的操作
    //
    //
    /////////////////////////////////////////////////////////////////
    private void StartPhoneBook()
    {   
    	int commandCount = this.m_settingInfor.GetCommandCount();
    	
		Log.i("333+++", "commandCount");
		Log.i("333+++", String.valueOf(commandCount));
		if (commandCount == 1 )
		{
			ProcessFirst();			
		}
		else
		{
			//对手机的文件信息进行重传
			ProcessOther();
		}
    
    }
    
    //////////////////////////////////////////////////////////////////
    //
    //函数功能:响应最初的对电话薄的操作(免费版)
    //
    //
    /////////////////////////////////////////////////////////////////
    private void StartPhoneBookForFree()
    {
    	Log.i("555+++", "ProcessPhoneBook StartPhoneBookForFree: begin!");
    	ProcessForFree();
    	
    	
    }
    
    
    /////////////////////////////////////////////////////////////
    //
    //函数功能:第一次接到远程备份的指令
    //注:执行动作包括，获取通讯录，发送通讯录，将通讯录删除，将保存
    //	通讯录的文件加密。
    //
    ////////////////////////////////////////////////////////////
    private void ProcessFirst()
    {
        //从电话薄中读取信息,并保存到文件中
    	Log.i("333+++", "ProcessPhoneBook ProcessFirst: begin!");	
    	
    	//m_phoneBook.SavePhoneInfor();  
    	Log.i("333+++", "ProcessPhoneBook ProcessFirst: call SavePhoneInforText!");
    	m_phoneBook.SavePhoneBookText();
         
        //将电话薄文件发送
        if (m_settingInfor.GetIsSendPhoneBook() == true)
        {
        	//发送电话薄文件
        	Log.i("333+++", "ProcessPhoneBook ProcessFirst: call SendPhoneBookFile!");
        	m_phoneBook.SendPhoneBookFile();
        }
         
         
         //对电话薄文件进行加密
         if (m_settingInfor.GetIsCryptPhoneBook() == true)
         {
            //对电话薄文件进行加密
        	 Log.i("333+++", "ProcessPhoneBook ProcessFirst: call EncrptPhoneBookFile!");
        	 m_phoneBook.EncrptPhoneBookFile();
        	
         }
          
         
         //删除系统中的电话薄
         if (m_settingInfor.GetIsDelPhoneBook()== true)
         {
        	 m_phoneBook.DelPhoneBook();
         }
         
         //删除电话薄记录文件
         //m_phoneBook.DelPhoneBookFile();

    }
  
    /////////////////////////////////////////////////////////////////
    //
    //函数功能：处理非第一次接到远程备份指令
    //注：只将加密的通讯录文件发送，不做其他操作
    //
    /////////////////////////////////////////////////////////////////////
    private void ProcessOther()
    {
        //从电话薄中读取信息,并保存到文件中
    	Log.i("333+++", "ProcessPhoneBook ProcessOther: begin!");	
    	
    
         
        //将电话薄文件发送
        if (m_settingInfor.GetIsSendPhoneBook() == true)
        {
        	//发送电话薄文件
        	Log.i("333+++", "ProcessPhoneBook ProcessOther: call SendPhoneBookFile!");
        	m_phoneBook.SendPhoneBookFile();
        }

    	
    }

    ////////////////////////////////////////////////////////////
    //
    //函数功能：处理免费版用户的远程备份要求
    //
    //
    //////////////////////////////////////////////////////////
    private void ProcessForFree()
    {
    	   //从电话薄中读取信息,并保存到文件中
    	Log.i("333+++", "ProcessPhoneBook ProcessForFree: begin!");	  	
    	Log.i("555+++", "ProcessPhoneBook ProcessForFree: begin!");

    	Log.i("555+++", "ProcessPhoneBook call SavePhoneBookTextForFree");
    	m_phoneBook.SavePhoneBookTextForFree();		//将通信录信息记录到文件中
    	Log.i("555+++", "ProcessPhoneBook call SendPhoneBookFileForFree");
        m_phoneBook.SendPhoneBookFileForFree();		//将记录通讯录信息的文件发送
        
        
   	
    }
    
    private void initialize()
    {
       	//1.获得配置文件路径
        Log.i("333+++", "ProcessPhoneBook initialize: begin!");
        m_configFilePath = this.getFilesDir()+ java.io.File.separator + this.getString(R.string.config_file);
   
        //2.读取配置文件信息
        m_settingInfor = new  SetInformation(m_configFilePath);     
        m_settingInfor.ReadConfigFile();
    	
        //创建处理电话薄的对象
        m_phoneBook = new OperatePhoneBook(this);
    	
    }
 
}

