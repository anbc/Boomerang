package Boomerang_android15.com;

////////////////////////////////////////////////////////////////////
//
//函数功能:负责phone启动时需要完成的工作
//注:没有@@@@联合测试
//
////////////////////////////////////////////////////////////////////



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpReceiver extends BroadcastReceiver
{
 	
	public void onReceive(Context context, Intent intent)
	{
		
		//开机后首先检查sim卡是否被更换
		CheckSimCard(context);
	  
	  
		/*
    	// 当收到Receiver时，指定打开此程序（EX06_16.class）
    	Intent mBootIntent = new Intent(context, EX06_16.class);    
    	// 设置Intent打开为FLAG_ACTIVITY_NEW_TASK 
    	mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
    	//将Intent以startActivity传送给操作系统 
    	context.startActivity(mBootIntent);
		 */
	}//end function
	
	/////////////////////////////////////////////////////////////////
	//
	//函数功能:对sim卡进行检测
	//参数:Context context
	//
	////////////////////////////////////////////////////////////////////
	private void CheckSimCard(Context context)
	{
		OperatePhoneInfor  oldPhoneInfor = new OperatePhoneInfor(context);
		OperatePhoneInfor  newPhoneInfor = new OperatePhoneInfor(context);
		
		//需要处理文件被删除的情况
		oldPhoneInfor.ReadPhoneInfor();  //从文件中读取老的信息
		String oldSimNumber =oldPhoneInfor.GetSimNumber();//获得原来的sim卡号码
		
		newPhoneInfor.GetPhoneInfor(); //从系统中获取当先系统信息
		String newSimNumber = newPhoneInfor.GetSimNumber();//获得当前的sim卡号码
		
		if (oldSimNumber.equals(newSimNumber)!= true)
		{
			//检测出更换sim卡
			//把新的sim卡信息保存到本地
			newPhoneInfor.WritePhoneInfor();
			
			//发出报警邮件
			newPhoneInfor.SendPhoneAlarm();
		}//end if
		
		
		
	}//end function
	
	
}