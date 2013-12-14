package Boomerang_android15.com;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Vector;
import java.io.InputStream;
import java.io.OutputStream;



public class mailText
{
	private String m_from = new String(); //邮件发件人列表
	private Vector <String> m_to = new Vector<String>(); //邮件收件人列表
	private Vector <String> m_cc = new Vector<String>(); //邮件抄送人列表
	private Vector <String> m_bcc = new Vector<String>(); //邮件暗送人列表

	private String m_subject = new String();	//邮件主题
	private String m_bodyText = new String();	//信体纯文本内容
	private Vector <String> m_attachments = new Vector<String>(); //附件文件的路径
	private boolean m_isAttachment = false; //判断是否带附件
	
	
	//private String m_dateHead = new String();
	private String m_dateBody = new String();  //对正文进行base64编码后的内容
	
	private String m_subSplitTag = "====SubsplitTag====";
	private String m_majorSplitTag = "====MajorSplitTag====";
	private final int BLOCK_LEN = 3420;  //发送附件时,每次读取文件的长度 76*3*15 = 3420
	private final int LINE_LEN = 76;	//附件base64编码后,每行的长度.

	//设置发件人列表
	public void SetMailFrom(String from)
	{
		m_from = from;
	}
	
	//设置邮件收件人信息
	public void SetMailTo(Vector<String> to)
	{
		m_to = to;
	}
	
	//设置邮件的抄送列表
	public void SetMailCc(Vector<String> cc)
	{
		m_cc = cc;
	}
	
	//设置邮件的密送列表信息
	public void SetMailBcc(Vector<String> bcc)
	{
		m_bcc = bcc;
	}
	
	//设置邮件主题
	public void SetMailSubject(String subject)
	{
		m_subject = subject;
	}
	
	//设置正文内容
	public void SetMailText(String bodyText)
	{
		m_bodyText = bodyText;
	}
	
	//设置添加附件的路径
	public void SetMailAttachment(Vector <String> attachments)
	{
		m_attachments = attachments;
	}
	
	//设置是否有附件,true为有,false为没有 
	public void IsAddAttachement(boolean isAttachment)
	{
		m_isAttachment = isAttachment;
	}
	
	
	
	
	//bAttachment,
	
	public mailText()
	{		
		m_from = "anbingchun@sina.com.cn";
		m_to.add("anzijin@sina.com.cn");
		m_subject = "test subject";
		m_bodyText = "test email body";
		
		
	}
	//////////////////////////////////////////////////////////////////
	//
	//函数功能：构造邮件头
	//返回值：返回构造好的邮件头部分
	//
	//
	///////////////////////////////////////////////////////////////////
	private String  StructDataHead()
	{
		
		String head = new String();
		String tempStr = null; 
		
		head = Date(); //获取时间字段
		head +=From(); //发信人字符段 
		
		//收件人字段
		tempStr = To();
		if (tempStr != null)
		{
			head += tempStr;
		}
			
		//抄送人字段
		tempStr = Cc();
		if (tempStr != null)
		{
			head += tempStr;
		}
		
		//暗送人字段
		tempStr = Bcc();
		if (tempStr != null)
		{
			head += tempStr;
		}
		
			
		head += Subject();	//暗送主题
		head += MessageID(); 	//MessageId字段
		head += XMailer(); //XMailer字段
		head += MimeVersion(); //Mime-Version字段
		
		//head += "\r\n"; //为支持QQ新加,不成功
		//这里只支持纯文本加附件的电子邮件
		if (m_isAttachment == false)
		{
			//纯文本文件
			head += "Content-Type: text/plain;\r\n\tcharset=\"gb2312\"\r\n";
			//head += "Content-Transfer-Encoding: base64\r\n"; //新浪 
			head += "Content-Transfer-Encoding: base64 \r\n\r\n"; //qq
		}
		else
		{
			//带附件的邮件
			head += "Content-Type: multipart/mixed;\r\n\t boundary=\"" + m_majorSplitTag + "\"\r\n";
		}
		
		
		m_majorSplitTag = "--" + m_majorSplitTag + "\r\n"; //在主分界符前面加入两个前置字符“==”，邮件接受客户端程序将根据这两个前置横线符判断出这是一个主分界标志。
		
		
		return head;
		
		
	}
	
	/////////////////////////////////////////////////////////
	//
	//函数功能：获取系统的时间，构造成邮件需要的格式 
	//
	//
	//注：Mon, 4 Jan 2010 16:19:03 +0800
	///////////////////////////////////////////////////////////
	private String Date()
	{
		String DateStr = new String();
		int myYear;
		int myMonth;
		int myWeek;
		int myDay;
		int myHour;
		int myMinute;
		int mySecond;
		int myTimeZone;
		 
		String MonthStr[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};                      
		//JANUARY	FEBRUARY MARCH	APRIL	MAY	JUNE	JULY	AUGUST	SEPTEMBER	OCTOBER	NOVEMBER DECEMBER
		String WeekStr[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		//SUNDAY		MONDAY		TUESDAY		WEDNESDAY		THURSDAY		FRIDAY		SATURDAY 
		
		
		Calendar c=Calendar.getInstance();
		myYear = c.get(Calendar.YEAR);
		myMonth = c.get(Calendar.MONTH);
		myWeek = c.get(Calendar.DAY_OF_WEEK);
		myDay = c.get(Calendar.DAY_OF_MONTH);
		myHour = c.get(Calendar.HOUR_OF_DAY);
		myMinute = c.get(Calendar.MINUTE);
		mySecond = c.get(Calendar.SECOND);
		myTimeZone =c.get(Calendar.ZONE_OFFSET);  //时区的数值需要调试时调整。
		
		////Mon, 4 Jan 2010 16:19:03 +0800
		DateStr = "Date: "; 
		DateStr += WeekStr[myWeek-1]+", ";
		DateStr += String.valueOf(myDay)+" ";
		DateStr += MonthStr[myMonth]+" ";		
		DateStr += String.valueOf(myYear)+ " ";
		DateStr += String.valueOf(myHour)+ ":";
		DateStr += String.valueOf(myMinute)+ ":";
		DateStr += String.valueOf(mySecond)+ " ";
		DateStr += "+0800";
		
		DateStr +=  "\r\n";
		return  DateStr;
	}

	/////////////////////////////////////////////////////////////////
	//
	//函数功能：构造From字段，发送字段
	//String addr 发送者邮件地址
	//From: "anzijin" <anzijin@sina.com.cn>
	//
	///////////////////////////////////////////////////////////////////////
	private String From()
	{
		String strFrom;
		strFrom = "From: " + m_from + "\r\n";;
		return strFrom;
		
		
	}
	
	//////////////////////////////////////////////////////////////
	//
	//函数功能:构造To字段，收件人列表
	//
	//
	//////////////////////////////////////////////////////////////
	private String To()
	{
		String strTo = "To:";
		
		for (int i=0; i<m_to.size(); i++)
		{
			if(i!=0)
			{
				strTo += ",";
			}
			
			strTo += m_to.elementAt(i);
			
		}
		
		strTo +=  "\r\n";
		return strTo;
	}
	
	//////////////////////////////////////////////////////////////
	//
	//函数功能:构造CC字段，抄送者列表
	//
	//
	//////////////////////////////////////////////////////////////
	private String Cc()
	{
		
		String strCc = "Cc: ";
		if (m_cc.size()==0)
		{
			return null;
			
		}
		
		for (int i=0; i<m_cc.size(); i++)
		{
			if (i!=0)
			{
				strCc += ",";
				strCc += m_cc.elementAt(i);
			}
			
			
		}
		
		strCc +=  "\r\n";
		return strCc;
	}
	
	//////////////////////////////////////////////////////////////
	//
	//函数功能:构造Bcc字段，密送者列表
	//
	//
	//////////////////////////////////////////////////////////////
	private String Bcc()
	{
		String strBcc = "Bcc: ";
		
		if (m_bcc.size()==0)
		{
			return null;
		}
		
		for (int i=0; i< m_bcc.size(); i++)
		{
			if (i!=0)
			{
				strBcc += ",";
				strBcc += m_bcc.elementAt(i);
			}
			
		}
		strBcc +=  "\r\n";
		return strBcc;
	}
	
	//////////////////////////////////////////////////////////////
	//
	//函数功能:构造邮件的主题
	//
	//
	//////////////////////////////////////////////////////////////
	private String Subject()
	{
		//没有处理base64编码的情况
		byte [] bodyTextb64 = null;
		try
		{
			bodyTextb64 = base64.encode(m_subject.getBytes("GB2312"));
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
		//m_dateBody.
		//buffer = bodyTextb64.toString();
		String buffer = new String(bodyTextb64);
		String strSubject = "Subject: =?gb2312?B?";
		
		strSubject += buffer + "?=\r\n";
		
		return  strSubject;
	}
	
	
	////////////////////////////////////////////////////////////////
	//
	//函数功能: 构造XMailer
	//
	//////////////////////////////////////////////////////////////////
	private String XMailer()
	{
		
		String strXmailer = "X-mailer: Foxmail 6, 15, 201, 22 fql_mailer \r\n";
		
		/*
		 原C代码
		 char str[BUFFER_BLOCK_SIZE];
		 EncodingBase64("自己的Mailer", str);
		 buffer = string("X-mailer: =?GB2312?B?") +str + "?=\r\n";
		 */
		
		
		return strXmailer;
	}
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////
	//
	//函数功能: 构造MessageId
	//方法：取当前时间，加上发送者邮箱的域名
	//
	//
	//-	tB	0x0012c804 "Message-ID: <1262683956@sina.com.cn>
	//////////////////////////////////////////////////////////////////
	private String MessageID()
	{
		String strMessageId = "Message-ID: ";
		long t = System.currentTimeMillis();
		int index = m_from.indexOf("@");
		strMessageId += String.valueOf(t);
		strMessageId += m_from.substring(index) + "\r\n";
		
		return strMessageId;
		
	}
	
	//////////////////////////////////////////////////////////
	//
	//函数功能：构造Mime-Version字段 
	//
	/////////////////////////////////////////////////////////////
	private String MimeVersion()
	{
		String strMimeVer = "Mime-Version: 1.0\r\n";
		
		return strMimeVer;
	}
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////
	//
	//函数功能：将中转化成base64码
	//
	//暂时不使用，投中都使用ascii码
	//
	//////////////////////////////////////////////////////////////////////
	private void HeadTextTemple(String command, String addr, String buf)
	{
		for (int i=0; i<addr.length(); i++)
		{
			char a = addr.charAt(i);
			
			
		}
		
		
	}
	
	
	

	///////////////////////////////////////////////////////////////////
	//
	//函数功能:构造纯文本邮件的有简体部分
	//返回值:返回构造好的字符串
	//
	//////////////////////////////////////////////////////////////////
	private String StructDataBodyPureText()
	{
		String buffer= new String();
		int length = (m_bodyText.length()/3 +1)*4;  //m_bodyText 文本内容
		byte [] bodyTextb64 = null;
		
		try
		{
			bodyTextb64 = base64.encode(m_bodyText.getBytes("GB2312"));
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
		//m_dateBody.
		//buffer = bodyTextb64.toString();
		buffer = new String(bodyTextb64);
		buffer += "\r\n.\r\n"; //结束符
		return buffer;
	}
	
	
	////////////////////////////////////////////////////////////////////////
	//
	//函数功能：构造邮件主体的文本部分
	//
	//
	////////////////////////////////////////////////////////////////////////
	private String StructDateBodyWithAttach()
	{
				
		// m_dateBody		
		String buffer = new String();
		//m_subSplitTag = "====SubsplitTag====";
		int length = (m_bodyText.length()/3 +1)*4;  //m_bodyText 文本内容
		byte [] bodyTextb64 =null;
		
		try
		{
			bodyTextb64 = base64.encode(m_bodyText.getBytes("GB2312"));
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
		//m_dateBody.
		//m_dateBody = bodyTextb64.toString();
		m_dateBody = new String(bodyTextb64);
		
		//添加文本和附件内容
		buffer = "This is a multi-part message in MIME format. \r\n"; //辅助冗余信息，该信息将被邮件接收客户端解析程序忽略掉
		buffer += "\r\n";  //此回车符是邮件体内容开始标志。 
		buffer += m_majorSplitTag; //起始主分界符
		
		//text/plain标志该区域将是纯文本内容
		buffer += "Content-Type: text/plain; \r\n\tcharset=\"gb2312\"\r\n";
		//标志该纯文本内容采用base64编码
		buffer += "Content-Transfer-Encoding: base64\r\n";
		buffer += "\r\n"; //该回车换行符是该区域内容起始标志
		buffer += m_dateBody; 	//base64 编码后的纯文本。
		buffer += "\r\n";
		
		

		
		return buffer;
			
	}
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//函数功能：构造邮件主体的附件部分
	//参数:DataOutputStream m_dos 用于输出编码后邮件附件的内容
	//返回值:无
	//
	/////////////////////////////////////////////////////////////////////////////
	private String ProcessAttachments(DataOutputStream dos)
	{

	
	
		String buffer = new String();		
		//处理附件部分
		String fileName = new String();
		String fileName64 = new String();
		String fileContent = new String();
		String str = new String();
		File file ;
		for (int i=0; i<m_attachments.size(); i++)
		{
			File attachmentFile = new File(m_attachments.elementAt(i));
			
			//对附件的文件名进行base64编码
			fileName = attachmentFile.getName();
			//fileName64 = (Base64.encode(fileName.getBytes())).toString();
			try
			{
				fileName64 = new String(base64.encode(fileName.getBytes("GB2312")));
			}
			catch (Exception e)
			{
				e.printStackTrace();				
			}
			
			str = "=?gb2312?B?"+ fileName64 + "?=";  //将附件文件名称填入邮件主体。
			buffer = m_majorSplitTag;
			
			//标志二进制数据类型
			buffer += "Content-Type: application/octet-stream;\r\n\tname=\"" + str +"\"\r\n";
			//标志该附件内容采用base64编码
			buffer += "Content-Transfer-Encoding: base64\r\n";
			//附件名信息标志，采用多行编码
			buffer += "Content-Disposition: attachment;\r\n\t";
			buffer += "filename=\"" +str + "\"\r\n";  //添加附件的名称
			buffer += "\r\n";
			try
			{
				dos.write(buffer.getBytes());
			}
			catch (Exception e)
			{
				e.printStackTrace();			
					
			}//end catch
			
			EncodeFileBase64(m_attachments.elementAt(i), dos);
			
			
			
			try
			{
				
				buffer = "\r\n";
				dos.write(buffer.getBytes());
			}
			catch (Exception e)
			{
				e.printStackTrace();			
					
			}//end catch
			
		} //end for
		
		buffer = m_majorSplitTag;
		buffer+="\r\n.\r\n";  //结束符标志
		try
		{
			dos.write(buffer.getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();			
				
		}//end catch
	
		return null;
	}	
	
	
	///////////////////////////////////////////////////////////////////////////////
	//
	//函数功能：对一个文件进行base64编码
	//参数：String fileName	文件的全路径名称
	//返回值：String 返回文件内容的base64编码
	//注：下一步考虑一边编码一边发送
	//////////////////////////////////////////////////////////////////////////////
	private String EncodeFileBase64(String fileName, DataOutputStream dos)
	{

		
	
		String fileContent64 = null;
		
		byte source [] = new byte[BLOCK_LEN];
		byte tail[] =null;
		//byte source [] = null;
		byte fileBase64[] = null;
		
		
		int realBlockLen =0; //一次读取文件内容块的长度
		int base64Len = 0;	//进行base64编码后的长度
		int offset = 0;	//记录以76字节长度为单位,写入过程中的偏移量.
		int leftLen = 0; //记录以76字节长度为单位写入过程中剩下多长的字节还没有写入.
		String LineEnd = "\r\n"; //每写入76字节
		try
		{
			FileInputStream in=new FileInputStream(fileName);
			
			do 
			{
				realBlockLen = in.read(source, 0, BLOCK_LEN);
				if (realBlockLen<=0)
				{
					//如果读取的长度为0，说明文件中已经没有内容，直接退出读取循环
					break;
				}
				
				if (realBlockLen<BLOCK_LEN)
				{
					tail = new byte[realBlockLen];
					for (int i=0; i<realBlockLen; i++)
					{
						tail[i] = source[i];
					}
					fileBase64 = base64.encode(tail);
				}
				else
				{
					fileBase64 = base64.encode(source);
				}
				//fileContent64 = new String(source);
				
				fileContent64 = new String(fileBase64);
				//base64Len = (realBlockLen/3+1)*4; 
				//dos.write(base64); //对编码后的数据进行发送
				
				leftLen = fileBase64.length;
				offset =0;
				do
				{
					if(leftLen>LINE_LEN)
					{
						dos.write(fileBase64, offset, LINE_LEN);
					}
					else
					{
						dos.write(fileBase64, offset, leftLen);
					}
					dos.write(LineEnd.getBytes());
					leftLen -= LINE_LEN;
					offset += LINE_LEN;
				}while(leftLen >0);
				
				//dos.write(base64, 0, base64Len);
				fileBase64 = null;
				
			
				
			}while(realBlockLen == BLOCK_LEN ); //如果小于则说明 读到最后一块
			
			dos.flush();
		}//end try
		catch (Exception e)
		{
			e.printStackTrace();			
				
		}//end catch
		
		return fileContent64;

	}//end function
	
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//函数功能:构造并输出(发送)电子邮件,包括邮件的头和邮件的主体部分
	//参数:DataOutputStream dos 有的输出路径
	//返回值:无
	//
	/////////////////////////////////////////////////////////////////////////////
	public  void SendMailBody(DataOutputStream dos)
	{
		String buffer = new String();
		if (m_isAttachment == false)
		{
			//邮件发送纯文本内容
			try
			{
				buffer = StructDataHead();//构造邮件包头
				dos.write(buffer.getBytes());
				buffer = StructDataBodyPureText(); //构造邮件体部分
				dos.write(buffer.getBytes());
				dos.flush();
			}
			catch (Exception e)
			{
				e.printStackTrace();			
					
			}//end catch
		}
		else
		{
			//处理邮件中有附件的情况
			try
			{
				buffer = StructDataHead();//构造邮件包头
				dos.write(buffer.getBytes());
				buffer = StructDateBodyWithAttach(); //构造邮件体中除了附件的部分
				dos.write(buffer.getBytes());
				ProcessAttachments(dos); //对附件内容进行编码并(输出)发送;
				dos.flush();
			
			}
			catch (Exception e)
			{
				e.printStackTrace();			
					
			}//end catch
			
			
		}// end if 
		
		
		
	}//end function

	

	
}//end class
	
	
	
