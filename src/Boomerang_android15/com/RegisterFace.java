package Boomerang_android15.com;

///////////////////////////////////////////////////////////////////////
//
//模块功能:显示登录界面,为用户提供信息输入
//
//
//////////////////////////////////////////////////////////////////////


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;



public class RegisterFace extends Activity
{
	//界面相关变量
	RegisterFaceView m_registerFaceView;
	
	
	//private TextView m_resultText;    //输入信息由错误时,输出提示信息
	
	//输入信息相关变量
	String m_strUsername; 
	String m_strPassword;
	String m_strEmail1;
	String m_strEmail2;
	
	String m_configFilePath;
	SetInformation m_regInfor;
	
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);		
		
		System.setProperty("file.encoding", "GBK"); //设置编码方式
		
		LogPrint("applicatio run");
		
		
		LogPrint("applicatio run_2");
		initialize();	//初始化
		boolean flag = IsNeedBoomerang(); //判断弹出界面
		if (flag == true)
		{
			//需要注册
			BoomerangFace();
		}
		else
		{
			//已经注册
			OtherFace();
		}
		
	}//end onCreate
	
	/////////////////////////////////////////////////////////////
	//
	//函数功能:用于数据的初始化
	//
	//
	//////////////////////////////////////////////////////////////
	private void initialize()
	{
		//设置竖屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
		
		
		//获取配置文件目录
		m_configFilePath = this.getFilesDir()+ java.io.File.separator + this.getString(R.string.config_file);

		//读取配置信息
		m_regInfor = new SetInformation(m_configFilePath); 
		m_regInfor.ReadConfigFile();	//读取配置文件信息
		
		
	}

	
	////////////////////////////////////////////////////////////////////////////
	//
	//函数功能:判断是否弹出注册界面
	//
	//
	///////////////////////////////////////////////////////////////////////////////
	private boolean IsNeedBoomerang()
	{
		String username = new String();
		String password = new String();
		String email_1 = new String();
		String email_2 = new String();
		
		username = m_regInfor.GetUserName();
		if (username.equals(""))
		{
			//用户名为空需要注册
			return true;
		}
	
		password = m_regInfor.GetPassWord();
		if (password.equals(""))
		{
			//密码为空需要注册
			return true;
		}
		
		email_1 = m_regInfor.GetEmail_1();
		if (email_1.equals(""))
		{
			//第一个邮箱为空需要注册
			return true;
		}
		
		email_2 = m_regInfor.GetEmail_2();
		if (email_2.equals(""))
		{
			//第二个邮箱为空需要注册
			return true;
		}
		
		return false;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	//
	//函数功能:出现飞去来注册界面
	//
	//
	/////////////////////////////////////////////////////////////////////////////////
	/*
	private void BoomerangFace_old()
	{
		
		setContentView(R.layout.registerface);
		Log.i("BoomerangFace", "begin");
		
		
		//关联界面
		m_userName = (EditText)findViewById(R.id.EditTextUsername27);
		m_password = (EditText)findViewById(R.id.EditTextPassword30);
		m_email_1 = (EditText)findViewById(R.id.EditTextEmail132);
		m_email_2 = (EditText)findViewById(R.id.EditTextEmail234);
		
		m_finish = (Button)findViewById(R.id.ButtonFinish37);
		m_cancel = (Button)findViewById(R.id.ButtonCancel38);
		
		m_logoImage = (ImageButton)findViewById(R.id.ImageButtonLogo42);
		m_logoImage.setImageResource(R.drawable.icon);
		//m_logoImage.setImageBitmap(bm);
		
		//响应图片按钮
		m_logoImage.setOnClickListener(new ImageButton.OnClickListener()
		{
			public void onClick(View v)
			{
				new AlertDialog.Builder(RegisterFace.this)
				.setTitle("关于")
				.setMessage("飞去来手机卫士 1.0 \n  email:fql_helper@sina.com")
				.setPositiveButton
				(
						R.string.str_Ok_alert,
						new DialogInterface.OnClickListener() 
						{							
							
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								
							}//onClick
						}//end DialogInterface.OnClickListener
				)
				.show();//end setPositiveButton;
				
			}//end onClick
			
		});

		//响应完成按钮
		m_finish.setOnClickListener( new Button.OnClickListener()
		{
			public void onClick(View v)
			{

				String result = new String();
				result = ProcessRegInfor();
				
				if (result.equals(""))
				{
					//注册成功
					 new AlertDialog.Builder(RegisterFace.this)
			    	  .setTitle("注册成功")
			    	  .setMessage("请登录注册邮箱进行确认!")
			    	  .setPositiveButton("ok",
			    			  new DialogInterface.OnClickListener() 
			    	  			{
									public void onClick(DialogInterface dialog, int which) 
									{
										// TODO Auto-generated method stub
										 RegisterFace.this.finish();
									}
								})
			    	  .show();
					
				}
				else
				{
					 new AlertDialog.Builder(RegisterFace.this)
			    	  .setTitle("注册错误")
			    	  .setMessage(result)
			    	  .setPositiveButton("ok",
			    			  new DialogInterface.OnClickListener() {
									
									
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								})
			    	  .show();
					//进行错误处理
				}
			}
		}//end Button.OnClickListener()
		);//SetOnClickListener
		
		
		//响应取消按钮
		m_cancel.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				new AlertDialog.Builder(RegisterFace.this)
				.setTitle(R.string.str_cancel_register)
				.setMessage(R.string.str_cancel_register_message)
				.setPositiveButton
				(
						R.string.str_Ok_alert,
						new DialogInterface.OnClickListener() 
						{							
							
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								RegisterFace.this.finish();
							}//onClick
						}//end DialogInterface.OnClickListener
				)//end setPositiveButton
				.setNegativeButton
				(
						R.string.str_cancel_alert,
						new DialogInterface.OnClickListener()
						{							
						
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								
							}
						}
				
				)
				.show();
			}
			
		}//end Button.OnClickListener()
		);//end setOnClickListener
		
		
	}
	*/
	private void BoomerangFace()
	{
		Log.i("BoomerangFace", "begin");
		
		//获取分辨率信息
		DisplayMetrics dm = new DisplayMetrics();
		RegisterFace.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		m_registerFaceView = new RegisterFaceView(RegisterFace.this, dm);
		AbsoluteLayout abslayout = m_registerFaceView.GetRegisterFaceView();
		setContentView(abslayout);
		//setContentView(R.layout.registerface);
	
		
		
		Button okButton;
		Button cancelButton;
		
		ImageButton logoImageButton;  //显示logo图片
		
		//关联界面

		
		okButton = m_registerFaceView.GetOKButton();
		cancelButton = m_registerFaceView.GetCancelButton();
		
		logoImageButton = m_registerFaceView.GetLogoImageButton();
		
		//m_logoImage.setImageBitmap(bm);
		
		//响应图片按钮
		logoImageButton.setOnClickListener(new ImageButton.OnClickListener()
		{
			public void onClick(View v)
			{
				new AlertDialog.Builder(RegisterFace.this)
				.setTitle("关于")
				.setMessage("飞去来手机卫士 1.0 \n  email:fql_helper@sina.com")
				.setPositiveButton
				(
						R.string.str_Ok_alert,
						new DialogInterface.OnClickListener() 
						{							
							
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								
							}//onClick
						}//end DialogInterface.OnClickListener
				)
				.show();//end setPositiveButton;
				
			}//end onClick
			
		});

		//响应完成按钮
		okButton.setOnClickListener( new Button.OnClickListener()
		{
			public void onClick(View v)
			{

				String result = new String();
				result = ProcessRegInfor();
				
				if (result.equals(""))
				{
					//注册成功
					 new AlertDialog.Builder(RegisterFace.this)
			    	  .setTitle("注册成功")
			    	  .setMessage("请登录注册邮箱进行确认!")
			    	  .setPositiveButton("ok",
			    			  new DialogInterface.OnClickListener() 
			    	  			{
									public void onClick(DialogInterface dialog, int which) 
									{
										// TODO Auto-generated method stub
										 RegisterFace.this.finish();
									}
								})
			    	  .show();
					
				}
				else
				{
					 new AlertDialog.Builder(RegisterFace.this)
			    	  .setTitle("注册错误")
			    	  .setMessage(result)
			    	  .setPositiveButton("ok",
			    			  new DialogInterface.OnClickListener() {
									
									
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								})
			    	  .show();
					//进行错误处理
				}
			}
		}//end Button.OnClickListener()
		);//SetOnClickListener
		
		
		//响应取消按钮
		cancelButton.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				new AlertDialog.Builder(RegisterFace.this)
				.setTitle(R.string.str_cancel_register)
				.setMessage(R.string.str_cancel_register_message)
				.setPositiveButton
				(
						R.string.str_Ok_alert,
						new DialogInterface.OnClickListener() 
						{							
							
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								RegisterFace.this.finish();
							}//onClick
						}//end DialogInterface.OnClickListener
				)//end setPositiveButton
				.setNegativeButton
				(
						R.string.str_cancel_alert,
						new DialogInterface.OnClickListener()
						{							
						
							public void onClick(DialogInterface dialog, int which) 
							{
								// TODO Auto-generated method stub
								
							}
						}
				
				)
				.show();
			}
			
		}//end Button.OnClickListener()
		);//end setOnClickListener
		
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	//
	//函数功能:其他功能界面
	//注:调用另一个activity 以达到显示其他界面的目的
	///////////////////////////////////////////////////////////////////////////////
	private void OtherFace()
	{
        /* new一个Intent对象，并指定要启动的class */
        Intent intent = new Intent();
    	intent.setClass(RegisterFace.this, Other_Flashlight.class);
    	  
    	/* 调用一个新的Activity */
    	startActivity(intent);
    	  /* 关闭原本的Activity */
    	RegisterFace.this.finish();
	}
	////////////////////////////////////////////////////////////////////////////
	//
	//函数功能:处理填写的注册信息
	//返回值:int 返回处理的结果,0为顺利完成, 其他表示各种错误 
	// 1:用户名为空
	// 2:密码为空
	//
	//////////////////////////////////////////////////////////////////////////////
	private String  ProcessRegInfor()
	{
		//获取填写信息
		
		m_strUsername = this.m_registerFaceView.GetUserNameEditTextString();
		m_strPassword = this.m_registerFaceView.GetPasswordEditTextString();
		m_strEmail1 = this.m_registerFaceView.GetFirstEmailEditTextString();
		m_strEmail2 = this.m_registerFaceView.GetSecondEmailEditTextString();
		
		String result = new String();
		
		/*
		m_strUsername = "anbingchun";
		m_strPassword = "cxykzqj123";
		m_strEmail1 = "anbingchun@yahoo.com.cn";
		m_strEmail2 = "anzijin@sina.com";
		*/
		
		if (m_strUsername.equals(""))
		{
			//用户名为空
			return "用户名为空";
		}
		
		if (m_strPassword.equals(""))
		{
			//密码为空
			return "密码为空";
		}
		
		if (m_strPassword.length() < 8)
		{
			//密码太短
			return "密码小于8位";
		}
		
		boolean isLegal;
		isLegal = IsEmail(m_strEmail1);
		if (isLegal == false)
		{
			//第一邮箱地址不是一个有效的邮箱
			return "第一邮箱地址不合法";
		}
		
		isLegal = IsEmail(m_strEmail2);
		if (isLegal == false)
		{
			//第二邮箱地址不是一个有效的邮箱
			return "第二邮箱地址不合法";
		}
		
	
		
		//m_regInfor = new SetInformation(m_configFilePath); 
		//m_regInfor.ReadConfigFile();	//读取配置文件信息
	
		m_regInfor.SetUserName(this.m_strUsername);
		m_regInfor.SetPassWord(m_strPassword);
		m_regInfor.SetEmail_1(m_strEmail1);
		m_regInfor.SetEmail_2(m_strEmail2);
		
		m_regInfor.SetDataPath(this.getFilesDir().toString()); //将当前目录设置为默认数据存储目录
		
		m_regInfor.WriteConfigFile();	//向配置文件中写信息
		
		SendRegisterMail();
		return "";
	}
	
	

	
	///////////////////////////////////////////////////////////////////
	//
	//函数功能:判断是否是一个合法的email地址
	//参数:String strEmail 待检测的电子邮箱字符串
	//返回值:合法返回true,不合法返回false
	//
	//////////////////////////////////////////////////////////////////
	private static boolean IsEmail(String strEmail)
	{
		if (strEmail.equals(""))
		{
			//遇过邮箱为空,返回false
			return false;
		}
		
		//QQ邮箱有问题
		
	    String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"; 
	    Pattern p = Pattern.compile(strPattern); 
	    Matcher m = p.matcher(strEmail); 
	    return m.matches(); 
	    
		//return true;
	}
	
	private void SendRegisterMail()
	{
      	Log.i("333+++", "RegisterFace SendRegisterMail: begin!");
    	Vector<String> emailAddress = new Vector<String>();
    	FqlEmail mailPort;
    	OperatePhoneInfor  PhoneInfor = new OperatePhoneInfor(this);
    	String phoneNum = new String();
    	
    	PhoneInfor.GetPhoneInfor();
    	PhoneInfor.WritePhoneInfor();
    	
    	emailAddress.add(m_regInfor.GetEmail_1());
    	emailAddress.add(m_regInfor.GetEmail_2());
    	//emailAddress.add("fql_free_backup@sina.com"); //不在使用主动备份
    	String Subject = new String();
    	String text = new String();
    	String filePath = new String();
	  
    	Subject = this.getString(R.string.email_suject); //设置邮件主题
    	Subject += "----注册成功";
    	
    	//设置邮件内容
    	text = this.getString(R.string.email_text);	//软件信息
    	text += this.getString(R.string.app_version); //软件版本信息
    	text += "\r\n";
    	text += "以下是您的注册信息: \r\n" ;
    	text +=	"手机号:" + PhoneInfor.GetTelNum() + "\r\n";
    	text += "用户名:" + this.m_strUsername + "\r\n";
    	text += "密码:" + this.m_strPassword + "\r\n";
    	text += "第一邮箱:" + this.m_strEmail1 + "\r\n";
    	text += "第二邮箱:" + this.m_strEmail2 + "\r\n";
    	
    	text += "\r\n \r\n   附件中文档是您手机的详细资料. \r\n";
    	text += "\r\n \r\n     请您妥善保存该邮件,以便紧急情况下使用! \r\n";
    	
    	
    	filePath = PhoneInfor.GetPhoneInforFile();	//设置邮件附件的路径
    	
    	
    	mailPort = new FqlEmail();
    	mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
	  
    	Log.i("333+++", "RegisterFace SendRegisterMail: begin!");
		
	}
	
	
	private void LogPrint(String logText)
	{
		
		//得到sdcard路径
		String sdcardDir = Environment.getExternalStorageDirectory().toString();

		//创建日志文件夹
		String folderStr = sdcardDir + java.io.File.separator + "fql_guard";
		File newFilesDir= new File(folderStr); //日志文件夹
		if (!newFilesDir.exists())
		{
			//如果文件不存在,返回False;
			newFilesDir.mkdir();
		}
		
		
		//日志文件
		String logFilePath = folderStr  + java.io.File.separator + "fql.log";
		File logFile= new File(logFilePath); //日志文件
		RandomAccessFile raf;
		
		//判断日志文件是否存在
		if (logFile.exists() == false)
		{
			//如果文件不存在,返回False;这时创建文件			
			try
			{
				logFile.createNewFile();
			}
			catch (Exception e)
			{
				Log.i("333+++", "wronng create a new log file ");
				e.printStackTrace();				
			}
		}
	

		//写入新的日志信息
		try
		{
			raf = new RandomAccessFile(logFile, "rw");
			//length =  raf.length();
			raf.seek(raf.length());
			raf.writeBytes(logText+"\r\n");
			raf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		} 
		
		
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//函数功能：检查是否可以连接互联网
	//返回值：	可以连接返回true，不可以连接返回false
	//注：testing
	/////////////////////////////////////////////////////////////////////////////
	private boolean IsConnectNet()
	{
		
		
		
		
		return true;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//函数功能：检查指定网络是否连通
	//参数：	hostUrl 主机域名
	//		int port 端口号
	//注：通过连接指定的网络来检查网络是否连通
	//注：testing
	/////////////////////////////////////////////////////////////////////////////
	private boolean CheckNet(String hostUrl, int port)
	{
		Socket checkSocket; 
		try
		{
			InetAddress serverAddr = InetAddress.getByName(hostUrl);//TCPServer.SERVERIP 
			checkSocket = new Socket(serverAddr, port);
		}
		catch (Exception e)
		{
			e.printStackTrace();	
			return false;
			//e.getMessage();
		}		
		
		return true;
	}
}//RegisterFace
