package Boomerang_android15.com;

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;


import android.util.Log;



public class fqlSmtp
{
	private String m_serverHostName = new String();		//ESTMP服务器名
	private int m_serverPort = 25;			//ESTMP服务器监听端口
	private String m_username = new String();		//登录用户名
	private String m_pass = new String();			//登录密码
	private String m_senderAddr = new String();		//邮件发送者地址
	private Vector <String> m_recptAddress = new Vector<String> (); //邮件接收者地址
	private String m_from = new String(); //邮件发件人列表
	private Vector <String> m_to = new Vector<String> (); //邮件收件人列表
	private Vector <String> m_cc = new Vector<String> (); //邮件抄送人列表
	private Vector <String> m_bcc  = new Vector<String> (); //邮件暗送人列表
	
	private String m_subject;	//邮件主题
	private String m_bodytext;	//信体纯文本内容
	
	private Vector<String> m_attachments = new Vector<String>();
	private boolean m_isAttachment = false;
	
	
	mailText testMailText = new mailText();

	
	Socket m_socket;     
	InputStream m_is;     
	OutputStream m_os;     
	DataInputStream m_dis;    
	DataOutputStream m_dos;
	
	
	//////////////////////////////////////////////////////////////////
	//
	//函数功能:构造函数
	//
	//////////////////////////////////////////////////////////////////
	public fqlSmtp()
	{		
		Log.i("333+++", " fqlSmtp :construct function");
		//RegisterSina();
		//RegisterYahoo();
		//RegisterQQ();
		//RegisterGmail();
		
	}
	
	private void RegisterSina()
	{
		m_serverHostName = "smtp.sina.com.cn";		//ESTMP服务器名
		m_username = "anbingchun";		//登录用户名
		m_pass = "zwwjsssdxz";			//登录密码
		m_senderAddr = "anbingchun@sina.com.cn";		//邮件发送者地址
		m_from = "anbingchun@sina.com.cn";
		
		m_recptAddress.add("anzijin@sina.com.cn");
		m_recptAddress.add("1321490265@QQ.com");
		m_recptAddress.add("anbingchun8@msn.com");
		m_recptAddress.add("feiqulaimobile@gmail.com");
		m_recptAddress.add("feiqulaimobile@hotmail.com");
		m_recptAddress.add("anbingchun@yahoo.com.cn");
		
		m_to.add("1321490265@QQ.com");
		m_subject = "test";
		m_bodytext = "fei qu lai testing!";  //base64编码:ZmVpIHF1IGxhaSB0ZXN0aW5nIQ==
		m_isAttachment =true;
		m_attachments.add("/sdcard/try/email.txt");
		m_attachments.add("/sdcard/try/IMG_2424.jpg");
		
	}
	
	private void RegisterQQ()
	{
		m_serverHostName = "smtp.qq.com";		//ESTMP服务器名smtp.qq.com
		m_username = "94128203";		//登录用户名
		m_pass = "dskjzyhdzw";			//登录密码
		m_senderAddr = "94128203@qq.com";		//邮件发送者地址
		m_from = "94128203@qq.com";
		
		
		m_recptAddress.add("anzijin@sina.com.cn");
		m_recptAddress.add("1321490265@QQ.com");
		m_recptAddress.add("anbingchun8@msn.com");
		m_recptAddress.add("feiqulaimobile@gmail.com");
		m_recptAddress.add("feiqulaimobile@hotmail.com");
		m_recptAddress.add("anbingchun@yahoo.com.cn");
		
		
		m_to.add("1321490265@QQ.com");
		m_subject = "test";
		m_bodytext = "fei qu lai testing!";  //base64编码:ZmVpIHF1IGxhaSB0ZXN0aW5nIQ==
		m_isAttachment =true;
		m_attachments.add("/sdcard/try/email.txt");
		m_attachments.add("/sdcard/try/IMG_2424.jpg");
		
	}
	
	private void RegisterYahoo()
	{
		m_serverHostName = "smtp.mail.yahoo.com.cn";		//ESTMP服务器名smtp.qq.com
		m_username = "anbingchun";		//登录用户名
		m_pass = "zwwjsssdxz";			//登录密码
		m_senderAddr = "anbingchun@yahoo.com.cn";		//邮件发送者地址
		m_from = "anbingchun@yahoo.com.cn";
		
		
		m_recptAddress.add("anzijin@sina.com.cn");
		m_recptAddress.add("1321490265@QQ.com");
		m_recptAddress.add("anbingchun8@msn.com");
		m_recptAddress.add("feiqulaimobile@gmail.com");
		m_recptAddress.add("feiqulaimobile@hotmail.com");
		m_recptAddress.add("anbingchun@yahoo.com.cn");
		
		
		m_to.add("1321490265@QQ.com");
		m_subject = "test";
		m_bodytext = "fei qu lai testing!";  //base64编码:ZmVpIHF1IGxhaSB0ZXN0aW5nIQ==
		m_isAttachment =true;
		m_attachments.add("/sdcard/try/email.txt");
		m_attachments.add("/sdcard/try/IMG_2424.jpg");
		
	}
	
	private void RegisterGmail()
	{
		//必须使用ssl加密协议
		m_serverHostName = "smtp.gmail.com";		//ESTMP服务器名smtp.qq.com
		m_username = "feiqulaimobile";		//登录用户名
		m_pass = "dskjzyhdzw";			//登录密码
		m_senderAddr = "feiqulaimobile@gmail.com";		//邮件发送者地址
		m_from = "feiqulaimobile@gmail.com";
		
		
		m_recptAddress.add("anzijin@sina.com.cn");
		m_recptAddress.add("1321490265@QQ.com");
		m_recptAddress.add("anbingchun8@msn.com");
		m_recptAddress.add("feiqulaimobile@gmail.com");
		m_recptAddress.add("feiqulaimobile@hotmail.com");
		m_recptAddress.add("anbingchun@yahoo.com.cn");
		
		

		m_to.add("1321490265@QQ.com");
		m_subject = "test";
		m_bodytext = "fei qu lai testing!";  //base64编码:ZmVpIHF1IGxhaSB0ZXN0aW5nIQ==
		m_isAttachment =true;
		m_attachments.add("/sdcard/try/email.txt");
		m_attachments.add("/sdcard/try/IMG_2424.jpg");
		
		
	}
	
	/////////////////////////////////////////////////////////////
	//
	//函数功能:设置smtp服务器的信息
	//参数:	String serverHost	stmp服务器域名
	//		String serverPort	服务器端口
	//		String username		用户名
	//		String password		用户密码
	//		String senderAddress	发送者邮箱
	//		String recptAddress		接受者邮箱
	//
	//////////////////////////////////////////////////////////////
	public void SetSmtpInfor(
			String serverHost,
			int serverPort,
			String username,
			String password,
			String senderAddress,
			String recptAddress
			)
	{
		SetServerHost(serverHost);
		SetServerPort(serverPort);
		SetUserName(username);
		SetPassword(password);
		SetMailSender(senderAddress);
		AddOneMailTo(recptAddress);
		
		
	}
	//设制邮件服务器域名
	public void SetServerHost(String serverHostName)
	{
		Log.i("333+++", " fqlSmtp :SetServerHost ");
		m_serverHostName = serverHostName;
	}
	
	//设置邮件服务器端口
	public void SetServerPort(int port)
	{
		m_serverPort = port;
	}
	
	//设置用户名信息
	public void SetUserName(String username)
	{
		m_username = username;
	}
	
	//设置密码信息
	public void SetPassword(String password)
	{
		m_pass = password;
	}
	
	////////////////////////////////////////////////
	//
	//函数功能:设置邮件发送者信息
	//注:m_senderAddr和m_from设为相同的值
	//
	/////////////////////////////////////////////////
	public void SetMailSender(String sender)
	{
		m_senderAddr = sender;
		m_from = sender;
	}
	
	/////////////////////////////////////////////////////
	//
	//函数功能:添加一个地址到接收邮件列表
	//决定谁能收到邮件
	/////////////////////////////////////////////////////
	public void AddOneRecptAddr(String recptAddr)
	{
		
		m_recptAddress.add(recptAddr);
	}
	
	/////////////////////////////////////////////////////////
	//
	//函数功能:添加一个收件人地址
	//只影响邮件内容的显示
	///////////////////////////////////////////////////////////
	public void AddOneMailTo(String oneTo)
	{
		
		m_to.add(oneTo);
	}
	
	//添加一个抄送人地址
	public void AddOneMailCc(String oneCc)
	{
		m_cc.add(oneCc);
	}
	
	//添加一个密送人地址
	public void AddOneMailBcc(String oneBcc)
	{
		m_bcc.add(oneBcc);
	}
	
	//设置邮件主题
	public void SetMailSubject(String subject)
	{
		m_subject =  subject;
	}
	
	//设置邮件正文
	public void SetMailBodytext(String bodytext)
	{
		m_bodytext = bodytext;
	}

	//添加附件列表
	public void AddMailAttachment(String attachmentFile)
	{
		m_isAttachment = true;
		m_attachments.add(attachmentFile);
	}
	
	
	
	

	/////////////////////////////////////////////////////////////////////
	//
	//函数功能:
	//参数:
	//
	////////////////////////////////////////////////////////////////////
	public boolean SendMail(String subject, 
			String bodytext,
			Vector <String> attachmentList
			)
	{
		
		SetMailSubject(subject);
		SetMailBodytext(bodytext);
		
		if (0 == attachmentList.size())
		{
			m_isAttachment = false;
		}
		else
		{
			m_attachments = attachmentList;
		}
		
		
		
		SendMail();
		
		return true;
	}
	
	
	
	//////////////////////////////////////////////////////////////
	//
	//函数功能:发送邮件
	//
	//注:包括连接邮件服务器,调用mailText类构建邮件,发送邮件,关闭连接
	//
	///////////////////////////////////////////////////////////////////
	public boolean SendMail()
	{
		Log.i("333+++", "fqlSmtp  SendMail: begin!");
		
		String sendStr = "";
		String recvStr = "";
		//byte [] b_base64 = new byte[0x100];
		int result =0;
		boolean flag = false;
		//
		flag = SetMailTextInfor();
		if (flag == false)
		{
			return false;
			//Log.i("call SetMailTextInfor false!\n");
		}
		
		try
		{
			InetAddress serverAddr = InetAddress.getByName(m_serverHostName);//TCPServer.SERVERIP 
			m_socket = new Socket(serverAddr, m_serverPort);
			
			
			
			//获得对应的socket的输入、输出流
			m_is = m_socket.getInputStream();
			m_os = m_socket.getOutputStream();
			
			//建立数据流
			m_dis = new DataInputStream(m_is);
			m_dos = new DataOutputStream(m_os);
			
			//1   得到ESMTP服务器欢迎词，成功响应码是220
			result = GetResponseCode (220, recvStr);
			if (result !=0)
			{
				return false;
			}
	
			//2   向ESMTP服务器打招呼，EHLO。成功响应码是250
			sendStr = "EHLO fql" + m_senderAddr + "\r\n";  //QQ的情况
			//sendStr = "EHLO " + m_senderAddr + "\r\n"; //新浪已经成功
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode (250, recvStr);
			if (result !=0)
			{
				return false;
			}
			
			
			//3	登录ESMTP服务器，AUTH。由于绝大部分ESMTP服务器都支持LOGIN登录方式，所有直接采用该登录方式，
			//事实上，正确的做法是先判断EHLO的响应码中是否包含AUTH LOGIN字符串。
			sendStr = "AUTH LOGIN\r\n";
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode (334, recvStr);
			if (result !=0)
			{
				return false;
			}
			
			
			//4	向服务器发送base64编码的用户名。成功响应码是334
			//b_base64 = Base64.encode(this.m_senderAddr.getBytes());  //QQ邮箱
			//b_base64 = base64.encode(m_username.getBytes());  //已经支持的新浪
			sendStr = new String(base64.encode(m_username.getBytes()));
			//sendStr += " \r\n"; //QQ
			sendStr += "\r\n"; //yahoo
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode (334, recvStr);
			if (result !=0)
			{
				return false;
			}
			
				
			//5	向ESTMP服务器发送base64编码的密码，成功响应码为235
			//b_base64 = base64.encode(m_pass.getBytes()); 
			sendStr = new String(base64.encode(m_pass.getBytes()));
			sendStr += "\r\n";
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode (235, recvStr);
			if (result !=0)
			{
				return false;
			}
			
			//6   发送发件人地址，mail from  .成功响应码为 250
			sendStr = "MAIL FROM:" + m_senderAddr + "\r\n";
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode (250, recvStr);
			if (result !=0)
			{
				return false;
			}
			
			//7 发送收件人地址 RCPT TO ，成功响应码为250
			for (int i=0; i<m_recptAddress.size(); i++)
			{
				sendStr = "RCPT TO: <" + m_recptAddress.get(i) + ">\r\n";
				m_dos.write(sendStr.getBytes());
				m_dos.flush();
				result = GetResponseCode (250, recvStr);
				if (result !=0)
				{
					return false;
				}
				
			}//end for
			
			
			//8 发送DATA名命令，表示准备发送邮件内容，成功响应码354.
			sendStr = "DATA\r\n";
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode (354, recvStr); //354 go ahead
			if (result !=0)
			{
				return false;
			}
			
			
			//9发送信件
			testMailText.SendMailBody(m_dos);
			
			result = GetResponseCode(250, recvStr);
			if (result !=0)
			{
				return false;
			}
			
						
			
			//10  特到信件结束返回码
			
			
			//11推出邮件服务器
			sendStr = "QUIT\r\n";
			m_dos.write(sendStr.getBytes());
			m_dos.flush();
			result = GetResponseCode(221, recvStr);
			if (result != 0)
			{
				return false;
			}
			
			return true;
			   
		}
		catch (Exception e)
		{
			e.printStackTrace();			
			//e.getMessage();
		}
		
		return true;
	}
	
	
	////////////////////////////////////////////////////////////////////
	//
	//函数功能:获取邮件服务器的返回信息
	//参数:	int correctCode	正确的返回编码
	//		String retString 返回字符串
	//返回值:如果调用成功返回0, 否则返回1;
	//
	////////////////////////////////////////////////////////////////////
	private int GetResponseCode (int correctCode, String retString)
	{
		int retCode;
		String recvStr;
		int result ;
		int waitCount = 1;
		try
		{
			result = m_is.available();
			while(result==0)
			{
				//Thread.sleep(1000);   //以后通过这个函数处理监听几秒后无响应,返回false
 			    
 			    /*
 			    Thread.sleep(20*waitCount);
 			    result = m_is.available();
 			    waitCount *=2;	//等待时间指数增长
 			    if(waitCount > 256) //256=c^8 
 			    {
 			    	//超时
 			    	 return -1;
 			    } 
 			     * */
				
 			    result = m_is.available();

			}			
			
			byte[] data = new byte[result];
		    m_is.read(data);
		    recvStr = new String(data);
		     
		    //这里少了GetResponseCode 函数
		    String code = recvStr.substring(0, 3);
		     
		    int i_code = 0;
		    i_code = Integer.parseInt(code);
		     
		    if (i_code == correctCode)
		    {
		    	//220 irxd5-203.sinamail.sina.com.cn ESMTP\r\n
		    	//500 #5.5.1 command not recognized\r\n
		    	//501 #5.5.4 cannot decode AUTH parameter [B@435a24c0\r\n
		    	//501 #5.5.4 cannot decode AUTH parameter [B@435a24a0 \r\n
		    	//返回消息正确
		    	return 0;
		    }
		    else
		    {
		    	return 1;
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();				
		}
		return 0;
	}
	
	private boolean SetMailTextInfor()
	{
		//设置发件者邮箱
		if (m_from != null)
		{
			testMailText.SetMailFrom(this.m_from);
		}
		else
		{
			return false;
		}
		
		//设置收件人邮箱
		if (m_to != null)
		{
			testMailText.SetMailTo(this.m_to);
		}
		else
		{
			return false;
		}
		
		
		//设置抄送列表
		if (m_cc != null)
		{
			if ( m_cc.size() != 0)
			{
				testMailText.SetMailCc(this.m_cc);
			}
		}
		
		//设置密送邮件列表
		if (m_bcc != null)
		{
			if (m_bcc.size() != 0)
			{
				testMailText.SetMailBcc(this.m_bcc);
			}
		}
	
		
		testMailText.SetMailSubject(this.m_subject);  //设置邮件主题
		testMailText.SetMailText(this.m_bodytext);		//设置邮件正文
		
		
		//设置附件相关的信息
		if (m_isAttachment == true)
		{
			if (m_attachments != null)
			{
				if (m_attachments.size()!= 0)
				{
					testMailText.IsAddAttachement(true);
					testMailText.SetMailAttachment(this.m_attachments);
				}
				else
				{
					testMailText.IsAddAttachement(false);
				}
			}
			else
			{
				testMailText.IsAddAttachement(false);
			}
			
		}
		else
		{
			testMailText.IsAddAttachement(false);
		}
				
		return true;
		
	}



}
