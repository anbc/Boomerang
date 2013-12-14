package Boomerang_android15.com;

///////////////////////////////////////////////////////////////////////
//
//模块功能:接收短信模块.
//
//
//////////////////////////////////////////////////////////////////////


import android.content.BroadcastReceiver;
import android.content.Context; 
import android.content.Intent; 
import android.os.Bundle; 
import android.widget.Toast;
import android.util.Log;

/*必须引用telephoney.gsm.SmsMessage来收取短信*/
import android.telephony.gsm.SmsMessage; 
/*必须引用Toast类来告知用户收到短信*/
//import android.widget.Toast; 
import android.util.Log;

/* 自定义继承自BroadcastReceiver类,聆听系统服务广播的信息 */
public class FqlSMSReceiver extends BroadcastReceiver 
{ 
  /*声明静态字符串,并使用android.provider.Telephony.SMS_RECEIVED
  作为Action为短信的依据*/
	private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED"; 


  
	public void onReceive(Context context, Intent intent) 
	{ 
		
		System.setProperty("file.encoding", "GBK"); //设置编码方式
		
		/* 判断传来Intent是否为短信*/
		if (intent.getAction().equals(mACTION)) 
		{ 
			/*建构一字符串集合变量sb*/
			StringBuilder sb = new StringBuilder(); 
			/*接收由Intent传来的数据*/
			Bundle bundle = intent.getExtras(); 
			/*判断Intent是有数据*/
			if (bundle != null) 
			{ 
				/* pdus为 android内置短信参数 identifier
				 * 通过bundle.get("")返回一包含pdus的对象*/
				Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
				
				
				/*构建短信对象array,并依据收到的对象长度来创建array的大小*/
				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
        
				//这里messages的数量就是 收到短信的数量,下面是对每条
				for (int i = 0; i<myOBJpdus.length; i++)
				{  
					messages[i] = SmsMessage.createFromPdu((byte[]) myOBJpdus[i]);
				}// for (int i = 0; i<myOBJpdus.length; i++)
          
				
				/* 将送来的短信合并自定义信息于StringBuilder当中 */  
				for (SmsMessage currentMessage : messages) 
				{  
					/*
					sb.append("接收到来自:\n");  						
					sb.append(currentMessage.getDisplayOriginatingAddress());	// 来讯者的电话号码  		
					
					sb.append("\n------传来的短信------\n");  				
					sb.append(currentMessage.getDisplayMessageBody());   // 取得传来信息的BODY  
					
					*/
					ParseSmsMessage (currentMessage, context); //成员函数,解析每一个收到的短息，识别有效的信息
					
				}//end   for (SmsMessage currentMessage : messages) 
        
			}  //end if (bundle != null)  
    

       

      
      
		}//end if (intent.getAction().equals(mACTION)) 
    
	}//end onReceive(Context context, Intent intent) 
  
	/////////////////////////////////////////////////////////////////////////
	//
	//函数功能：解析每一个收到的短息，识别有效的信息
	//
	//
	//
	/////////////////////////////////////////////////////////////////////////////
	private void ParseSmsMessage (SmsMessage messages , Context context)
	{
		boolean isCorrectSMS = false;  //标示是否是有效短信，默认是无效短息。
		boolean isRootCommand = true;  //标示是否是根指令，默认是跟指令
		//java.lang.String
		String messageBody=  messages.getDisplayMessageBody();
		String phoneNum = messages.getDisplayOriginatingAddress();
		String standardMessage = PreTreatSMSString(messageBody);  //对短信进行预处理的成员函数

		//先判断是否有关键字"FQL",这里的值可以通过配置文件设置。
		int index = standardMessage.indexOf("fql");
		if (-1 == index)
		{
			//非有效短信息
			return;
			
		}
		
		String ruleMessage = standardMessage.substring(index); //取子字符串
		
		
		//启动短信指令处理服务,LaunchService服务
		Intent itent = new Intent(Intent.ACTION_RUN);
		itent.setClass(context, LaunchService.class); 
		Bundle bundle = new Bundle();
		bundle.putString("MESSAGE", ruleMessage);
		bundle.putString("PHONE_NUM", phoneNum);
		itent.putExtras(bundle);
			
		//*设置让以一个全新的task来运行
		itent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		Log.i("ParseSmsMessage", "begin");
		context.startService(itent);   //启动LaunchService服务
		
		
	}
	
	////////////////////////////////////////////////////////////////////
	//
	//函数功能：对短息的信息进行预处理
	//1、转换为大写字符，
	//2、过滤回车符
	//3、过滤换行符
	////////////////////////////////////////////////////////////////////
  	private String PreTreatSMSString(String preString)
  	{
  		String standardMessage_1 = preString.toLowerCase();
  		String standardMessage_2 = standardMessage_1.replace('\n', ' ');
  		String standardMessage_3 = standardMessage_2.replace('\r', ' ');
  		
  		return standardMessage_3;
  		
  	}
  
  
  
  
  
  
}

