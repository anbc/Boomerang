package Boomerang_android15.com;

///////////////////////////////////////////////////////////////////////
//
//模块功能:处理接收到的短信指令
//注:包括判断真实性,识别指令内容,启动相关模块
//
//////////////////////////////////////////////////////////////////////




import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent; 
//import android.os.Bundle;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;
import android.util.Log;

//import android.util.Log;


public class LaunchService extends Service
{	
	SetInformation m_settingInfor;		//配置文件的设置信息
	String m_configFilePath;	//配置文件的路径
	String m_messageText;		//收到的短息内容
	
	//!!!!!!!!需要传入发送短信的手机号
	String m_messageAddress = "";	//收到短息的手机号, 传过来.
	
	
	String [] elementStr;
			
	public void onCreate()
	{
		super.onCreate();	
		
		System.setProperty("file.encoding", "GBK"); //设置编码方式
		Log.i("333+++", "LaunchService onCreate begin ");
		//获得配置文件的路径
		m_configFilePath = this.getFilesDir()+ java.io.File.separator + this.getString(R.string.config_file);
		
		//获取配置文件信息
		m_settingInfor = new  SetInformation(m_configFilePath);     
		m_settingInfor.ReadConfigFile();
		Log.i("333+++", m_configFilePath);
		
		
		
	}
	 
	public void onStart (Intent intent, int startId )
	{
		super.onStart(intent, startId);
		
		//接收FqlSMSReceiver服务传来的参数,其中参数包括,控制短信内容,和指令发送者的手机号
		Log.i("333+++", "LaunchService onCreate begin ");
		Bundle bundle = intent.getExtras();
		m_messageText = bundle.getString("MESSAGE");
		m_messageAddress = bundle.getString("PHONE_NUM");
		elementStr = m_messageText.split(" ");
					
		if (elementStr.length <3)
		{
			Log.i("333+++", "LaunchService onStart: lenght is too short ");
			return;
		}

		//判断指令中是否包含关键字
		if(0 != elementStr[0].compareTo("fql"))
		{
			//不符合关键字直接返回
			Log.i("333+++", "LaunchService onStart: can't find fql in messaging ");
			return;
		}

		if (elementStr[2].compareTo(m_settingInfor.GetPassWord())!= 0)
		{
			//密码不符
			Log.i("333+++", "LaunchService onStart: password wrong! ");
			Log.i("333+++", m_settingInfor.GetPassWord());		
			return;
		}
		

		if(elementStr.length == 3)
		{
			//不存在任何命令使用默认命令,启动手机自我保护机制
			Log.i("333+++", "LaunchService onStart: call  DoDefaultAct function!");
			DoDefaultAct();  //其中手机保护机制
			
		}
		
		if (elementStr[2].compareTo("command") == 0)
		{
			//处理其他命令
		}


	}

	public IBinder onBind(Intent intent)
	{

		return null;
	}

	
	public void onDestory()
	{
		
		Log.i("333+++", "LaunchService onDestory: call the Server finished");
		/*
		int commandCount = m_settingInfor.GetCommandCount();
		commandCount++;
		m_settingInfor.SetCommandCount(commandCount);
		m_settingInfor.WriteConfigFile();
		*/
		super.onDestroy();
	}

	//函数功能:发送反馈短信息
	private void SendAckMessage()
	{
		SmsManager smsManager = SmsManager.getDefault();
		
				
		String ackMessageText= new String();
		ackMessageText = "接收远程备份命令成功！正在回传备份信息。如有疑问请联系：fql_helper@sina.com.cn";
		
		Log.i("333+++", "send address:");
		Log.i("333+++", m_messageAddress);
		
		Log.i("333+++", "ackMessageText:");
		Log.i("333+++", ackMessageText);
		
		//发送反馈短信
		try
		{
			Log.i("333+++", "LaunchService SendAckMessage: in try!");
			PendingIntent mPI = PendingIntent.getBroadcast(LaunchService.this, 0, new Intent(), 0);
			smsManager.sendTextMessage(this.m_messageAddress, null, ackMessageText, mPI, null);
		}
		catch(Exception e)
		{
			Log.i("333+++", "LaunchService SendAckMessage: in catch!");
			
			e.printStackTrace();
			
		}
		
	}

	//////////////////////////////////////////////////////////////
	//
	//函数功能:其中手机自我保护机制
	//包括:	通信录保护
	//		文件保护
	//		短信保护(未完成)
	//		敏感信息外送,敏感信息销毁
	//
	//////////////////////////////////////////////////////////////
	private void DoDefaultAct()
	{
		Log.i("555+++", "LaunchService DoDefaultAct: begin!");
		
		//发送反馈短信息;		
		SendAckMessage();
		
		Log.i("333+++", "LaunchService DoDefaultAct: begin start ProcessPhoneBook!");
		
		//删除特殊短信短信
		//OperateSMS sms= new OperateSMS(this);
		//sms.deleteCommandSMS();
		Log.i("333+++", "LaunchService DoDefaultAct: end deleteCommandSMS!");
		
		//对接收指令进行计数
		Log.i("333+++", "LaunchService DoDefaultAct: count command!");
		int commandCount = m_settingInfor.GetCommandCount();
		Log.i("333+++", String.valueOf(commandCount));
		commandCount++;
		m_settingInfor.SetCommandCount(commandCount);
		m_settingInfor.WriteConfigFile();
		m_settingInfor.ReadConfigFile();
		commandCount = m_settingInfor.GetCommandCount();
		Log.i("333+++", String.valueOf(commandCount));
		
		
		//启动电话薄保护
		Log.i("555+++", "LaunchService start Phone book");
		Intent intentPhone =new Intent(LaunchService.this, ProcessPhoneBook.class);
		Bundle bundlePhone = new Bundle();
		bundlePhone.putString("command", "all");
		intentPhone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intentPhone);
		
        Sleep(1000);
        
		//启动获取手机的自身信息
        Log.i("555+++", "LaunchService start Phone infor");
		Intent intentPhoneInfor = new Intent(LaunchService.this, ProcessPhoneInfor.class);		                                                        
		Bundle bundlePhoneInfor = new Bundle();
		bundlePhoneInfor.putString("command", "all");
		intentPhoneInfor.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intentPhoneInfor);

		Sleep(1000);

		//启动文件保护
		Log.i("555+++", "LaunchService start Phone file");
		Intent intentFile =new Intent(LaunchService.this, ProcessFileInfor.class);
		Bundle bundleFile = new Bundle();
		bundleFile.putString("command", "all");
		intentFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intentFile);



		
		Sleep(1000);
		
		Log.i("333+++", "LaunchService DoDefaultAct: delete sms!");
		OperateSMS sms= new OperateSMS(this);
		sms.deleteCommandSMS();
		Log.i("333+++", "LaunchService DoDefaultAct: do!");
		
	}
	
	private void Sleep(int ms)
	{
		
		try{   
			Log.i("333+++", "LaunchService DoDefaultAct: sleep begin!");
            Thread.currentThread().sleep(ms);   
            Log.i("333+++", "LaunchService DoDefaultAct: sleep end!");
        }   
        catch(InterruptedException   e)
        {
        	Log.i("333+++", "LaunchService DoDefaultAct: sleep Exception!");
        }
		
	}
	
	
}