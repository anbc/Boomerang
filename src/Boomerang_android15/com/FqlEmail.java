package Boomerang_android15.com;

import java.io.File;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class FqlEmail
{
	fqlSmtp m_smtp [] = new fqlSmtp[15];;
	int smtpCount = 0; //有效的smtp服务器信息数
	
	
	public FqlEmail()
	{

		//完成新浪smtp配置	
		
		//第一组
		m_smtp[0]= new fqlSmtp();
		m_smtp[0].SetServerHost("smtp.sina.com.cn");
		m_smtp[0].SetServerPort(25);
		m_smtp[0].SetUserName("fql_free_server");
		m_smtp[0].SetPassword("wcmddszcf");
		m_smtp[0].SetMailSender("<fql_free_server@sina.com.cn>");
		m_smtp[0].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[0].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[1]= new fqlSmtp();
		m_smtp[1].SetServerHost("smtp.163.com");
		m_smtp[1].SetServerPort(25);
		m_smtp[1].SetUserName("fql_free_server");
		m_smtp[1].SetPassword("wcmddszcf");
		m_smtp[1].SetMailSender("<fql_free_server@163.com>");
		m_smtp[1].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[1].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[2]= new fqlSmtp();
		m_smtp[2].SetServerHost("smtp.qq.com");
		m_smtp[2].SetServerPort(25);
		m_smtp[2].SetUserName("fql_free_server");
		m_smtp[2].SetPassword("wcmddszcf");
		m_smtp[2].SetMailSender("<fql_free_server@qq.com>");
		m_smtp[2].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[2].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		//第二组
		m_smtp[3]= new fqlSmtp();
		m_smtp[3].SetServerHost("smtp.sina.com.cn");
		m_smtp[3].SetServerPort(25);
		m_smtp[3].SetUserName("fql_ser_free_01");
		m_smtp[3].SetPassword("wcmddszcf");
		m_smtp[3].SetMailSender("<fql_ser_free_01@sina.com.cn>");
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[4]= new fqlSmtp();
		m_smtp[4].SetServerHost("smtp.163.com");
		m_smtp[4].SetServerPort(25);
		m_smtp[4].SetUserName("fql_ser_free_01");
		m_smtp[4].SetPassword("wcmddszcf");
		m_smtp[4].SetMailSender("<fql_ser_free_01@163.com>");
		m_smtp[4].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[4].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		
		//第三组	
		m_smtp[5]= new fqlSmtp();
		m_smtp[5].SetServerHost("smtp.sina.com.cn");
		m_smtp[5].SetServerPort(25);
		m_smtp[5].SetUserName("fql_ser_free_02");
		m_smtp[5].SetPassword("wcmddszcf");
		m_smtp[5].SetMailSender("<fql_ser_free_02@sina.com.cn>");
		m_smtp[5].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[5].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		
		m_smtp[6]= new fqlSmtp();
		m_smtp[6].SetServerHost("smtp.163.com");
		m_smtp[6].SetServerPort(25);
		m_smtp[6].SetUserName("fql_ser_free_02");
		m_smtp[6].SetPassword("wcmddszcf");
		m_smtp[6].SetMailSender("<fql_ser_free_02@163.com>");
		m_smtp[6].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[6].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
			
		
		//第四组
		m_smtp[7]= new fqlSmtp();
		m_smtp[7].SetServerHost("smtp.sina.com.cn");
		m_smtp[7].SetServerPort(25);
		m_smtp[7].SetUserName("fql_ser_free_03");
		m_smtp[7].SetPassword("wcmddszcf");
		m_smtp[7].SetMailSender("<fql_ser_free_03@sina.com.cn>");
		m_smtp[7].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[7].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[8]= new fqlSmtp();
		m_smtp[8].SetServerHost("smtp.163.com");
		m_smtp[8].SetServerPort(25);
		m_smtp[8].SetUserName("fql_ser_free_03");
		m_smtp[8].SetPassword("wcmddszcf");
		m_smtp[8].SetMailSender("<fql_ser_free_03@163.com>");
		m_smtp[8].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[8].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		//第五组
		m_smtp[9]= new fqlSmtp();
		m_smtp[9].SetServerHost("smtp.sina.com.cn");
		m_smtp[9].SetServerPort(25);
		m_smtp[9].SetUserName("fql_ser_free_04");
		m_smtp[9].SetPassword("wcmddszcf");
		m_smtp[9].SetMailSender("<fql_ser_free_04@sina.com.cn>");
		m_smtp[9].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[9].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
			
		m_smtp[10]= new fqlSmtp();
		m_smtp[10].SetServerHost("smtp.163.com");
		m_smtp[10].SetServerPort(25);
		m_smtp[10].SetUserName("fql_ser_free_04");
		m_smtp[10].SetPassword("wcmddszcf");
		m_smtp[10].SetMailSender("<fql_ser_free_04@163.com>");
		m_smtp[10].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[10].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个


		
/*	
		//完成QQ smtp配置
	
		
		m_smtp[3]= new fqlSmtp();
		m_smtp[3].SetServerHost("smtp.qq.com");
		m_smtp[3].SetServerPort(25);
		m_smtp[3].SetUserName("fql_ser_free_01");
		m_smtp[3].SetPassword("wcmddszcf");
		m_smtp[3].SetMailSender("fql_ser_free_01@qq.com");
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[3]= new fqlSmtp();
		m_smtp[3].SetServerHost("smtp.qq.com");
		m_smtp[3].SetServerPort(25);
		m_smtp[3].SetUserName("fql_ser_free_02");
		m_smtp[3].SetPassword("wcmddszcf");
		m_smtp[3].SetMailSender("fql_ser_free_02@qq.com");
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[3]= new fqlSmtp();
		m_smtp[3].SetServerHost("smtp.qq.com");
		m_smtp[3].SetServerPort(25);
		m_smtp[3].SetUserName("fql_ser_free_03");
		m_smtp[3].SetPassword("wcmddszcf");
		m_smtp[3].SetMailSender("fql_ser_free_03@qq.com");
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
		
		m_smtp[3]= new fqlSmtp();
		m_smtp[3].SetServerHost("smtp.qq.com");
		m_smtp[3].SetServerPort(25);
		m_smtp[3].SetUserName("fql_ser_free_04");
		m_smtp[3].SetPassword("wcmddszcf");
		m_smtp[3].SetMailSender("fql_ser_free_04@qq.com");
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@sina.com"); //添加附属备份邮箱,
		m_smtp[3].AddOneRecptAddr("fql_free_mobile@163.com"); //安全起见添加多个
*/
		
		this.smtpCount=11;
		
		Log.i("333+++", " FqlEmail :construct function end ");
	}
	
	/*
	    fqlSmtp myMail = new fqlSmtp();
        myMail.Sendmail();
      
	 * */
	
	
	//////////////////////////////////////////////////////////////////////////////////
    //
    //函数功能:使用电子邮件发送一个文件
    //参数:	String emailAddress  邮件地址
    //		String Subject  邮件主题
    //		String text 邮件内容
    //		String filePath 附件的文件路径(即要通过电子邮件发送的文件)
    //返回值:void
    //可以有改善之处,不是都从第一个stmp服务器开始
    //////////////////////////////////////////////////////////////////////////////////
    public void SendFile(String emailAddress, String Subject, String text, String filePath)
    {
    	Log.i("333+++", "FqlEmail SendFile_1: begin!");
    	    	
    	Random r=new java.util.Random(); 
    	int randomNum = r.nextInt(this.smtpCount); 	
    	boolean flag = false;


    	for (int i=0; i<this.smtpCount; i++)
    	{
    		m_smtp[i].AddOneRecptAddr(emailAddress);
    		m_smtp[i].AddOneMailTo(emailAddress);
    		m_smtp[i].SetMailSubject(Subject);
    		m_smtp[i].SetMailBodytext(text);
    		m_smtp[i].AddMailAttachment(filePath);
    		
    		if (!filePath.equals(""))
    		{
    			m_smtp[i].AddMailAttachment(filePath);
    		}
    		
    		flag = m_smtp[i].SendMail();
    		if (flag == true)
    		{
    			//如果发送成功跳出循环
    			break;
    		} //end if
    		
    		
    	}// end for
    
    	
    }
    
    //////////////////////////////////////////////////////////////////////////////////
    //
    //函数功能:使用电子邮件发送多个文件
    //参数:	String emailAddress  邮件地址
    //		String Subject  邮件主题
    //		String text 邮件内容
    //		Vector<String> filePath 附件的文件路径(即要通过电子邮件发送的文件)
    //返回值:void
    //
    //////////////////////////////////////////////////////////////////////////////////
    public void SendFile(String emailAddress, String Subject, String text, Vector<String> filePaths)
    {
    	Log.i("333+++", "FqlEmail SendFile_2: begin!");
      	boolean flag = false;
    	for (int i=0; i<this.smtpCount; i++)
    	{
    		m_smtp[i].AddOneRecptAddr(emailAddress);
    		m_smtp[i].AddOneMailTo(emailAddress);
    		m_smtp[i].SetMailSubject(Subject);
    		m_smtp[i].SetMailBodytext(text);
    		for (int j=0; j<filePaths.size(); j++)
    		{
    			if (!filePaths.elementAt(j).equals(""))
        		{
    				m_smtp[i].AddMailAttachment(filePaths.elementAt(j));
        		}
    			
    		}//end for
    		
    		flag = m_smtp[i].SendMail();
    		if (flag == true)
    		{
    			//如果发送成功跳出循环
    			break;
    		}//end if
    	}//end for
    	
    	
    }

    public void SendFile(Vector<String> emailAddress, String Subject, String text, Vector<String> filePaths)
    {
    	Log.i("333+++", "FqlEmail SendFile_3: begin!");
     	boolean flag = false;
     	String emailAddr = new String();  //用户注册的邮箱
    	for (int i=0; i<this.smtpCount; i++)
    	{
    		for (int j=0; j<emailAddress.size(); i++)
    		{//AddOneRecptAddr
    			m_smtp[i].AddOneRecptAddr(emailAddress.elementAt(j));
    			m_smtp[i].AddOneMailTo(emailAddress.elementAt(j));
    		}
    		m_smtp[i].SetMailSubject(Subject);
    		m_smtp[i].SetMailBodytext(text);
    		for (int j=0; j<filePaths.size(); j++)
    		{
    			m_smtp[i].AddMailAttachment(filePaths.elementAt(j));
    		}//end for
    		
    		flag = m_smtp[i].SendMail();
    		if (flag == true)
    		{
    			//如果发送成功跳出循环
    			break;
    		}//end if
    	}//end for
    	
    }

    /////////////////////////////////////////////////////////////
    //
    //函数功能：被调用的发送函数
    //
    //
    //////////////////////////////////////////////////////////////
    public void SendFile(Vector<String> emailAddress, String subject, String text, String filePath)
    {

    	
    	Random r=new java.util.Random(); 
    	int randomNum = r.nextInt(this.smtpCount); 	
    	boolean flag = false;
    	
    	
    	for (int i=0; i<this.smtpCount; i++)
    	{
    		flag = sendOneEmail(randomNum, emailAddress, subject, text, filePath); 		
    		
    		randomNum++;
    		if (randomNum >= this.smtpCount)
    		{
    			//超出进行循环处理
    			randomNum  = randomNum % this.smtpCount;
    		}
    		
    		if (flag == true)
    		{
    			//如果显示发送成功，再重新发送一遍。如果发送失败重新选择先一个邮件服务器尝试。
    			flag = sendOneEmail(randomNum, emailAddress, subject, text, filePath);
    			break;
    		}
    		
    	}//end for 
    
    	
    	
    }//end function


    ////////////////////////////////////////////////////////////////////////////
    //
    //函数功能：	向指定的邮件服务器发送一个邮件
    //参数：		int serverIndex	 邮件服务器的标号
    //			Vector<String> emailAddress  邮件地址
    //			String Subject  邮件主题
    //			String text 邮件内容
    //			String filePath 附件的文件路径(即要通过电子邮件发送的文件)
    //
    //////////////////////////////////////////////////////////////////////////////
    private boolean sendOneEmail(int serverIndex, Vector<String> emailAddress, String subject, String text, String filePath)
    {
    	
    	String emailAddr = new String();  //用户注册的邮箱
    	boolean flag = false;
    	
    	for (int j=0; j<emailAddress.size(); j++)
		{
			emailAddr = emailAddress.elementAt(j);
			m_smtp[serverIndex].AddOneRecptAddr(emailAddr);
			m_smtp[serverIndex].AddOneMailTo(emailAddr);
		}
		m_smtp[serverIndex].SetMailSubject(subject);
		m_smtp[serverIndex].SetMailBodytext(text);
		
		if (!filePath.equals(""))
		{
			m_smtp[serverIndex].AddMailAttachment(filePath);
		}
		
		Log.i("333+++", "FqlEmail SendFile: begin send file!");
		flag = m_smtp[serverIndex].SendMail();
		
		
		return flag;
    	
    }
}