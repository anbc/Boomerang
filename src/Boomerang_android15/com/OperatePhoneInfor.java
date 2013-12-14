package Boomerang_android15.com;

//模块功能:该模块功能是获取手机自身的信息,如手机号


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class OperatePhoneInfor
{	
	private Context m_context;
	private TelephonyManager m_TelMgr;
	private ContentResolver m_cv;
	
	private String m_simState = new String();  //取得SIM卡状态 
	private String m_simNumber = new String();	//SIM卡卡号 
	private String m_simOperator = new String(); //SIM卡供货商代码
	private String m_simOperName = new String(); //SIM卡供货商名称
	private String m_simCountryIso = new String(); //SIM卡国别 	
	private String m_telNum = new String();	//手机电话号码 ##
	private String m_netCountry = new String(); //电信网络国别
	private String m_companyCode = new String();	//电信公司代码
	private String m_companyName = new String();	//电信公司名称
	private String m_communiteType = new String();	//移动通信类型
	private String m_netType = new String();	//网络类型 
	private String m_roamingState = new String();//漫游状态
	private String m_imeiId = new String();	//手机IMEI 
	private String m_imeiSVId = new String(); //IMEI SV
	private String m_imsi = new String();//手机IMSI
	private String m_bluetoothState = new String(); //蓝牙状态
	private String m_wifiState = new String() ;//WIFI状态
	private String m_airplaneMode = new String();  //飞行模式是否打开
	private String m_dataRoamingState = new String(); //数据漫游是否打开
	
	//文件相关变量申请
	private File m_fileDir;	//当前程序的路径
	private String m_phoneInforFile;	//用来保存文件目录内容 的文件
	private File m_filePhone;		//保存文件目录文件的文件对象
	private BufferedWriter m_bw;	//用于将文件目录信息写入文件目录文件中.
	private BufferedReader m_br; 		//用于读出文件目录信息;
	
	//配置文件相关	
	private SetInformation m_settingInfor;		//配置文件的设置信息
	private String m_configFilePath;	//配置文件的路径
	
    //网络相关
    private FqlEmail m_mailPort ;  //通往电子邮件的接口
    

    //支持加密功能
	private encryption myEncrpt = new encryption();
	
	
	
	OperatePhoneInfor(Context context)
	{
		m_context = context;
		initialize();
	}

	
    private void initialize()
	{
		////////////////////////////////////
		// 1.获得当前程序和sd卡 的路径
		///////////////////////////////////////
		Log.i("333+++", "ProcessFileInfor initialize: begin!");
		m_fileDir = m_context.getFilesDir();		
		
			
			
		///////////////////////////////////////
		// 2. 初始化目录文件的路径		
		///////////////////////////////////////
		m_configFilePath = m_fileDir+ java.io.File.separator + m_context.getString(R.string.config_file);
			
		//获取配置文件信息
		m_settingInfor = new  SetInformation(m_configFilePath);     
		m_settingInfor.ReadConfigFile();
			
		m_phoneInforFile = m_fileDir + java.io.File.separator + m_settingInfor.GetPhoneInforName();
			
		////////////////////////////////////////
		// 3.创建用来保存文件目录信息的文件的文件对象.
		///////////////////////////////////////////
		m_filePhone = new File(m_phoneInforFile);
		

	}
	
	
	
    public void GetPhoneInfor()
    {
    	m_TelMgr = (TelephonyManager)m_context.getSystemService(m_context.TELEPHONY_SERVICE); 
  
    	//取得SIM卡状态 
    	if(m_TelMgr.getSimState()==m_TelMgr.SIM_STATE_READY)
    	{
    		m_simState = "良好";
    	}
    	else if(m_TelMgr.getSimState()==m_TelMgr.SIM_STATE_ABSENT)
    	{
    		m_simState = "无SIM卡";
  
   	    }
    	else
        {
    		m_simState = "SIM卡被锁定或未知的状态";         
        }
    	
    	// 取得SIM卡卡号 
        if(m_TelMgr.getSimSerialNumber()!=null)
        {
        	m_simNumber  = m_TelMgr.getSimSerialNumber();
         
        }
        else
        {
        	m_simNumber  = "无法取得";          
        }
    	
        /* 取得SIM卡供货商代码 */
        if(m_TelMgr.getSimOperator().equals(""))
        {
        	m_simOperator = "无法取得";
        }
        else
        {
        	m_simOperator = m_TelMgr.getSimOperator();
          
        }
    	
        /* 取得SIM卡供货商名称 */
        if(m_TelMgr.getSimOperatorName().equals(""))
        {
        	m_simOperName = "无法取得";
        }
        else
        {
        	m_simOperName = m_TelMgr.getSimOperatorName();
        
        }
        
        /* 取得SIM卡国别 */
        if(m_TelMgr.getSimCountryIso().equals(""))
        {
        	m_simCountryIso = "无法取得";
          
        }
        else
        {
        	m_simCountryIso = m_TelMgr.getSimCountryIso();
          
        }
    	
    	
    	
    	
    	
    	//取得手机电话号码 
    	if(m_TelMgr.getLine1Number()!=null)
    	{
    		m_telNum = m_TelMgr.getLine1Number();
    	}
    	else
    	{
    		m_telNum = "无法取得";
    		
    	}//end if
    	
    	//取得电信网络国别
    	if(m_TelMgr.getNetworkCountryIso().equals(""))
        {
    		m_netCountry = "无法取得";
        }
        else
        {
        	m_netCountry = m_TelMgr.getNetworkCountryIso();
        }
    	
    	//取得电信公司代码
        if(m_TelMgr.getNetworkOperator().equals(""))
        {
        	m_companyCode = "无法取得";
        }
        else
        {
        	m_companyCode = m_TelMgr.getNetworkOperator();
        }

        
        //取得电信公司名称
        if(m_TelMgr.getNetworkOperatorName().equals(""))
        {
         
          	m_companyName = "无法取得";
        }
        else
        {
        	m_companyName = m_TelMgr.getNetworkOperatorName();
        }
        
        
        // 取得行动通信类型 
        if(m_TelMgr.getPhoneType()==m_TelMgr.PHONE_TYPE_GSM)
        {
        	m_communiteType = "GSM";
          
        }
        else
        {
        	m_communiteType = "未知";
        }
        
        
        // 取得网络类型 
        
        if(m_TelMgr.getNetworkType()==m_TelMgr.NETWORK_TYPE_EDGE)
        {
        	m_netType = "EDGE";
         
        }
        else if(m_TelMgr.getNetworkType()==m_TelMgr.NETWORK_TYPE_GPRS)
        {
        	m_netType = "GPRS";
          
        }
        else if(m_TelMgr.getNetworkType()==m_TelMgr.NETWORK_TYPE_UMTS)
        {
        	m_netType = "UMTS";
          
        }
        else if(m_TelMgr.getNetworkType()==4)
        {
        	m_netType = "HSDPA";
          
        }
        else
        {
        	m_netType = "未知";
         
        }
        
        
        //取得漫游状态 
        if(m_TelMgr.isNetworkRoaming())
        {
        	m_roamingState = "漫游中";
          
        }
        else
        {
        	m_roamingState = "无漫游";
         
        }
        
        
        
        
        //取得手机IMEI 
        m_imeiId  = m_TelMgr.getDeviceId();

        
        //取得IMEI SV        
        if(m_TelMgr.getDeviceSoftwareVersion()!=null)
        {
        	m_imeiSVId = m_TelMgr.getDeviceSoftwareVersion();
         
        }
        else
        {
        	m_imeiSVId = "无法取得";
        }
        
        //取得手机IMSI 
        if(m_TelMgr.getSubscriberId()!=null)
        {
        	m_imsi  = m_TelMgr.getSubscriberId();
          
        }
        else
        {
        	m_imsi = "无法取得";
        }
        
        ///////////////////////////////////////////////////////////////////////////
        //取得ContentResolver 
        m_cv = m_context.getContentResolver();
        String tmpS="";
        
        //取得蓝牙状态
        tmpS=android.provider.Settings.System.getString(m_cv, android.provider.Settings.System.BLUETOOTH_ON);
        if(tmpS.equals("1"))
        {
        	m_bluetoothState  = "已打开";
      
        }
        else
        {
        	m_bluetoothState = "未打开";
         
        }
        
        /* 取得WIFI状态 */
        tmpS=android.provider.Settings.System.getString(m_cv, android.provider.Settings.System.WIFI_ON);
        if(tmpS.equals("1"))
        {
        	m_wifiState = "已打开";
          
        }
        else
        {
        	m_wifiState = "未打开";
        
        }
        
        /* 取得飞行模式是否打开 */
        tmpS=android.provider.Settings.System.getString(m_cv, android.provider.Settings.System.AIRPLANE_MODE_ON);
        if(tmpS.equals("1"))
        {
        	m_airplaneMode = "打开中";
          
        }
        else
        {
        	m_airplaneMode = "未打开";
          
        }
        
        /* 取得数据漫游是否打开 */
        tmpS=android.provider.Settings.System.getString(m_cv, android.provider.Settings.System.DATA_ROAMING);    
		if(tmpS.equals("1"))
        {
        	m_dataRoamingState = "打开中";
         
        }
        else
        {
        	m_dataRoamingState = "未打开";
         
        }
        
        
    }
    
    public void WritePhoneInfor()
    {
    	String InforString = new String();
    	
		try
		{
			OutputStream out = new FileOutputStream(m_filePhone); 
			m_bw  = new BufferedWriter(new OutputStreamWriter(out, "GBK")); 
			//m_bw = new BufferedWriter(new FileWriter(m_filePhone));
			
			InforString = "手机自身信息\r\n";
			InforString += "SIM卡状态:" + m_simState + "\r\n";			
			InforString += "SIM卡卡号:" + m_simNumber + "\r\n";
			InforString += "SIM卡供应商代号:" + m_simOperator + "\r\n";
			InforString += "SIM卡供应商名称:" + m_simOperName + "\r\n";
			InforString += "SIM卡国别:" + m_simCountryIso + "\r\n";				
			InforString += "手机电话号码:" + m_telNum + "\r\n";
			InforString += "电信网络国别:" + m_netCountry + "\r\n";
			InforString += "电信公司代码:" + m_companyCode + "\r\n";
			//InforString += "电信公司名称:" + m_companyName + "\r\n";
			InforString += "移动通信类型:" + m_communiteType + "\r\n";
			InforString += "网络类型:" + m_netType + "\r\n";
			InforString += "手机漫游状态:" + m_roamingState + "\r\n";			
			InforString += "手机IMEI:" + m_imeiId + "\r\n";
			InforString += "IMEI SV:" + m_imeiSVId + "\r\n";
			InforString += "手机IMSI:" + m_imsi + "\r\n";			
			InforString += "蓝牙状态:" + m_bluetoothState + "\r\n";
			InforString += "WIFI状态:" + m_wifiState + "\r\n";
			InforString += "飞行模式:" + m_airplaneMode + "\r\n";
			InforString += "数据漫游:" + m_dataRoamingState + "\r\n\r\n";
			
			m_bw.write(InforString);	
			
			m_bw.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
		
    }
    
    public void ReadPhoneInfor()
    {
    	String InforLine = new String();
    	int index =0;
    	
		try
		{
			
			m_br = new BufferedReader(new FileReader(m_filePhone));
			
		
			//手机自身信息
			InforLine = m_br.readLine();
			
			//SIM卡状态:	
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_simState = InforLine.substring(index+1);			
			
			//SIM卡卡号
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_simNumber = InforLine.substring(index+1);
			
			//SIM卡供应商代号
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_simOperator = InforLine.substring(index+1);			
			
			//SIM卡供应商名称
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_simOperName = InforLine.substring(index+1);
			
			//SIM卡国别		
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_simCountryIso = InforLine.substring(index+1);
			
			//手机电话号码
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_telNum = InforLine.substring(index+1);			
			
			//电信网络国别
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_netCountry  = InforLine.substring(index+1);
			
			//电信公司代码
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_companyCode  = InforLine.substring(index+1);
			
			//电信公司名称
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_companyName  = InforLine.substring(index+1);			
			
			//移动通信类型
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_communiteType  = InforLine.substring(index+1);
			
			//网络类型
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_netType  = InforLine.substring(index+1);			
			
			//手机漫游状态
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_roamingState  = InforLine.substring(index+1);			
			
			//手机IMEI
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_imeiId  = InforLine.substring(index+1);	
			
			//IMEI SV
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_imeiSVId  = InforLine.substring(index+1);			
			
			//手机IMSI
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_imsi = InforLine.substring(index+1);	
			
			//蓝牙状态
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_bluetoothState = InforLine.substring(index+1);	
			
			//WIFI状态
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_wifiState = InforLine.substring(index+1);	
			
			//飞行模式
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_airplaneMode = InforLine.substring(index+1);	
			
			//数据漫游;
			InforLine = m_br.readLine();
			index = InforLine.indexOf(":");
			m_dataRoamingState = InforLine.substring(index+1);
	
			m_br.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
    	
    }
    
    
    ///////////////////////////////////////////////////
    //
    //函数功能:取得SIM卡状态
    //
    /////////////////////////////////////////////////////
    public String GetSimState()
    {
    	return m_simState;
    }
    
    
    ///////////////////////////////////////////////////
    //
    //函数功能:取得SIM卡卡号 
    //
    /////////////////////////////////////////////////////
    public String GetSimNumber()
    {
    	return m_simNumber;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:取得SIM卡供货商代码
    //
    /////////////////////////////////////////////////////
    public String GetSimOperator()
    {
    	return m_simOperator;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:取得SIM卡供货商名称
    //
    /////////////////////////////////////////////////////
    public String GetSimOperName()
    {
    	return m_simOperName;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取SIM卡国别
    //
    /////////////////////////////////////////////////////
    public String GetSimCountryIso()
    {
    	return m_simCountryIso;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取手机电话号码 ##
    //
    /////////////////////////////////////////////////////
    public String GetTelNum()
    {
    	return m_telNum;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取电信网络国别
    //
    /////////////////////////////////////////////////////
    public String GetNetCountry()
    {
    	return m_netCountry;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取电信公司代码
    //
    /////////////////////////////////////////////////////
    public String GetCompanyCode()
    {
    	return m_companyCode;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取电信公司名称
    //
    /////////////////////////////////////////////////////
    public String GetCompanyName()
    {
    	return m_companyName;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取移动通信类型
    //
    /////////////////////////////////////////////////////
    public String GetCommuniteType()
    {
    	return m_communiteType;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取网络类型 
    //
    /////////////////////////////////////////////////////
    public String GetNetType()
    {
    	return m_netType;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取漫游状态
    //
    /////////////////////////////////////////////////////
    public String GetRoamingState()
    {
    	return m_roamingState;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取手机IMEI 
    //
    /////////////////////////////////////////////////////
    public String GetImeiId()
    {
    	return m_imeiId;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取meiSVId
    //
    /////////////////////////////////////////////////////
    public String GetImeiSVId()
    {
    	return m_imeiSVId;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取手机IMSI
    //
    /////////////////////////////////////////////////////
    public String GetImsi()
    {
    	return m_imsi;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取蓝牙状态
    //
    /////////////////////////////////////////////////////
    public String GetBluetoothState()
    {
    	return m_bluetoothState;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取WIFI状态
    //
    /////////////////////////////////////////////////////
    public String GetWifiState()
    {
    	return m_wifiState;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取飞行模式是否打开
    //
    /////////////////////////////////////////////////////
    public String GetAirplaneMode()
    {
    	return m_airplaneMode;
    }
    
    ///////////////////////////////////////////////////
    //
    //函数功能:获取数据漫游是否打开
    //
    /////////////////////////////////////////////////////
    public String GetDataRoamingState()
    {
    	return m_dataRoamingState;
    }
    
    //////////////////////////////////////////////////////////////
    //
    //函数功能:获得保存phone自身信息的文件的名称(全路径)
    //
    //m_phoneInforFile
    /////////////////////////////////////////////////////////////////
    public String GetPhoneInforFile()
    {
    	
    	return m_phoneInforFile;
    }
    
    public void SendPhoneInfor()
    {
       	Log.i("333+++", "ProcessPhoneBook SendPhoneBookFile: begin!");
    	Vector<String> emailAddress = new Vector<String>();
    	emailAddress.add(m_settingInfor.GetEmail_1());
    	emailAddress.add(m_settingInfor.GetEmail_2());
    	String Subject = new String();
    	String text = new String();
    	String filePath = new String();
	  
    	Subject = m_context.getString(R.string.email_suject); //设置邮件主题
    	Subject += "----手机自身信息";
    	
    	//设置邮件内容
    	text = m_context.getString(R.string.email_text);	//设置软件信息
    	text += m_context.getString(R.string.app_version); //软件版本信息
    	text += "\r\n 手机信息可以帮助您取证! \r\n";
    	filePath = this.m_phoneInforFile;	//设置邮件附件的路径
    	
    	
    	m_mailPort = new FqlEmail();
    	m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
	  
    	Log.i("333+++", "ProcessPhoneBook SendPhoneBookFile: begin!");
    }
    
    /////////////////////////////////////////////////////////////////////
    //
    //函数功能:当检测到sim卡被更换时,发出报警邮件
    //
    ////////////////////////////////////////////////////////////////////
    public void SendPhoneAlarm()
    {
       	Log.i("333+++", "ProcessPhoneBook SendPhoneBookFile: begin!");
    	Vector<String> emailAddress = new Vector<String>();
    	emailAddress.add(m_settingInfor.GetEmail_1());
    	emailAddress.add(m_settingInfor.GetEmail_2());
    	String Subject = new String();
    	String text = new String();
    	String filePath = new String();
	  
    	Subject = m_context.getString(R.string.email_suject); //设置邮件主题
    	Subject += "----报警";
    	
    	//设置邮件内容
    	text = m_context.getString(R.string.email_text);	//设置软件信息
    	text += m_context.getString(R.string.app_version); //软件版本信息
    	text += "\r\n 您手机的sim卡被更换! \r\n" ;
    	text +="新的手机号是:" + this.m_telNum + "\r\n";
    	
    	filePath = this.m_phoneInforFile;	//设置邮件附件的路径
    	
    	
    	m_mailPort = new FqlEmail();
    	m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
	  
    	Log.i("333+++", "ProcessPhoneBook SendPhoneBookFile: begin!");
    }
    
    
    ////////////////////////////////////////////////////////////////////
    //
    //函数功能:修改手机信息文件的文件名后缀
    //
    //
    /////////////////////////////////////////////////////////////////////
    public void ChangeFileName()
    {
    	
    	
    }
    
    public void EncryptPhoneInfor()
    {
    	
    	
    }
    
}





