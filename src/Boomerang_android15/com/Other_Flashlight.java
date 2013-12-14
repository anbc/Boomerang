package Boomerang_android15.com;



/* import相关class */
import java.io.File;
import java.util.List;
import java.util.Vector;

import android.app.Activity; 
import android.app.AlertDialog;
import android.content.Context; 
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle; 
import android.os.Environment;
import android.os.PowerManager; 
import android.view.Menu;
import android.view.MenuItem; 
import android.view.Window; 
import android.view.WindowManager; 
import android.widget.LinearLayout;
import android.widget.Toast;

public class Other_Flashlight extends Activity 
{
	private boolean ifLocked = false;
	private PowerManager.WakeLock mWakeLock; 
	private PowerManager mPowerManager; 
	private LinearLayout mLinearLayout;
	/* 独几无又的menu选项identifier，用北识?事件 */ 
	static final private int M_CHOOSE = Menu.FIRST; 
	static final private int M_EXIT = Menu.FIRST+1;
	static final private int M_SEND_LOG = Menu.FIRST +2;
	/* 颜色选单的颜色与文?阵在 */
	private int[] color={R.drawable.white,R.drawable.blue,
                       R.drawable.pink,R.drawable.green,
                       R.drawable.orange,R.drawable.yellow};
	private int[] text={R.string.str_white,R.string.str_blue,
                      R.string.str_pink,R.string.str_green,
                      R.string.str_orange,R.string.str_yellow};

	@Override 
	public void onCreate(Bundle savedInstanceState) 
	{ 
		super.onCreate(savedInstanceState); 
     
		//弹出对话框
		new AlertDialog.Builder(Other_Flashlight.this)
		.setTitle("欢迎使用飞去来手电筒")
		//.setMessage("请登录注册邮箱,确定注册是否成功!")
		.setPositiveButton("ok",
			  new DialogInterface.OnClickListener() 
	  			{
					public void onClick(DialogInterface dialog, int which) 
					{
						// TODO Auto-generated method stub
						
					}
				})
				.show();
		/* 必须?setContentView之前呼?回屏幕显示 */ 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    
		this.getWindow().setFlags
		( 
			WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
		setContentView(R.layout.other);
    
		/* ?Activity启动时将屏幕亮调整为最亮
		 * 否程序为SDK1.5的新叫能，仅?1.5环境兀适用
		 */
		WindowManager.LayoutParams lp = getWindow().getAttributes(); 
		lp.screenBrightness = 1.0f; 
		getWindow().setAttributes(lp); 
    
		/* 初始化mLinearLayout */
		mLinearLayout=(LinearLayout)findViewById(R.id.myLinearLayout1);         
    
		/* 取得PowerManager */ 
		mPowerManager = (PowerManager)
                     getSystemService(Context.POWER_SERVICE); 
		/* 取得WakeLock */
		mWakeLock = mPowerManager.newWakeLock 
		( 
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "BackLight" 
		);    
	} 

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{ 
		/* menu群组ID */ 
		int idGroup1 = 0;    
		/* menuItemID */ 
		int orderMenuItem1 = Menu.NONE; 
		int orderMenuItem2 = Menu.NONE+1; 
		int orderMenuItem3 = Menu.NONE+2;
		/* 建立menu */ 
		menu.add(idGroup1,M_CHOOSE,orderMenuItem1,R.string.str_title);
	    menu.add(idGroup1,M_EXIT,orderMenuItem2,R.string.str_exit);
	    menu.add(idGroup1, M_SEND_LOG, orderMenuItem3, R.string.str_log);
	    menu.setGroupCheckable(idGroup1, true, true);
	 
	    return super.onCreateOptionsMenu(menu); 
	} 
   
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) 
	{  
		switch(item.getItemId()) 
	    { 
	      case (M_CHOOSE):
	        /* 跳叨选择背后颜色的AlertDialog */
	        new AlertDialog.Builder(Other_Flashlight.this)
	          .setTitle(getResources().getString(R.string.str_title))
	          .setAdapter(new Other_MyAdapter(this,color,text),listener1)
	          .setPositiveButton("取消",
	              new DialogInterface.OnClickListener()
	          {
	            public void onClick(DialogInterface dialog, int which)
	            {
	            }
	          })
	          .show();
	        break; 
	      case (M_EXIT): 
	        /* 离开程序 */ 
	        this.finish(); 
	        break; 
	        
	      case (M_SEND_LOG):
	    	//发送软件日志到服务器邮箱
	    	SendLogEmail();
	    }
	    return super.onOptionsItemSelected(item); 
	}
  
	/* 选择背后颜色的AlertDialog的OnClickListener */
	OnClickListener listener1=new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog,int which)
		{
			/* 更改背景颜色 */
			mLinearLayout.setBackgroundResource(color[which]);
			/* 北Toast显示设定的颜色 */
			Toast.makeText(Other_Flashlight.this,
                     getResources().getString(text[which]),
                     Toast.LENGTH_LONG).show();
		}
	};
   
	@Override 
	protected void onResume() 
	{  
	    /* onResume()时呼?wakeLock() */
	    wakeLock(); 
	    super.onResume(); 
	} 
   
	@Override 
	protected void onPause() 
	{
		/* onPause()时呼?wakeUnlock() */
		wakeUnlock(); 
		super.onPause();
	} 
  
	/* 唤起WakeLock的method */
	private void wakeLock()
	{ 
		if (!ifLocked) 
		{ 
			ifLocked = true;
			mWakeLock.acquire();
		}
	}

	/* 释放WakeLock的method */
	private void wakeUnlock() 
	{ 
	    if (ifLocked) 
	    { 
	    	mWakeLock.release(); 
	    	ifLocked = false;
	    }
	}
  
	////////////////////////////////////////////////////////////////
	//
	//函数功能：将软件的日志信息发送到服务器邮箱中
	//
	//
	//////////////////////////////////////////////////////////////////
	private boolean SendLogEmail()
	{
		//邮件信息设置
		FqlEmail mailPort = new FqlEmail();  //通往电子邮件的接口
		Vector<String> emailAddress = new Vector<String>();
		emailAddress.add("fql_helper@sina.com.cn");		
		String Subject = "日志信息_";
		String text = "日志信息";
	
		  
		//得到sdcard路径
		String sdcardDir = Environment.getExternalStorageDirectory().toString();
		
		//如果日志文件夹，返回false
		String folderStr = sdcardDir + java.io.File.separator + "fql_guard";
		File newFilesDir= new File(folderStr); //日志文件夹
		if (!newFilesDir.exists())
		{
			//如果文件不存在,返回False;
			return false;
		}
				
		List <String> folderPaths = null;
		String logFilePath = "";
		File f = new File(folderStr); //保存日志文件夹的文件类
		File oneFile; //保存和日志文件的文件类
		File [] files = f.listFiles();
				
		for (int i=0; i<files.length; i++)
		{
			oneFile = files[i];
			//newFilePath = oneFile.getPath();
			if (oneFile.isDirectory())
			{
				//如果是文件夹,不做任何处理
				continue;						
			}
			else
			{
				logFilePath = oneFile.getPath();
				Subject += oneFile.getName();
				
				mailPort.SendFile(emailAddress, Subject, text, logFilePath);
						
			}//end if
				
		}//end for
		
		return true;
		  
	}
  
  
  
}

