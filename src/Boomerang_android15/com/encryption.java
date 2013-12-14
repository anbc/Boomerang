package Boomerang_android15.com;


import java.io.*;
import java.security.spec.KeySpec;    
//提供了加密/签名接口      

import javax.crypto.Cipher;     
//Cipher为加密和解密提供密码功能。它构成了 Java Cryptographic Extension (JCE) 框架的核心。  
//主要方法有：init：用密钥初始化此 Cipher  
//            doFinal:按单部分操作加密或解密数据，或者结束一个多部分操作。数据将被加密或解密（具体取决于此 Cipher 的初始化方式）。   
 
import javax.crypto.SecretKey;  //密钥   
import javax.crypto.SecretKeyFactory;  //密钥工厂   
//密钥工厂用来将密钥（类型 Key 的不透明加密密钥）转换为密钥规范（底层密钥材料的透明表示形式）。秘密密钥工厂只对秘密（对称）密钥进行操作。密钥工厂为双工模式，即其允许根据给定密钥规范（密钥材料）构建不透明密钥对象，或以适当格式获取密钥对象的底层密钥材料。密钥工厂的参数包括算法 ，加密模式，填充，密码协议  

  
import javax.crypto.spec.DESKeySpec;      
//实现java.security.spec.KeySpec接口，创建一个 DESKeySpec 对象。  

import android.util.Log;

public class encryption 
{
	
	static String DES = "DES/ECB/NoPadding";      
	final int Block_Len=0x1000;
	

	/*设置加密方式，包括算法、模式、填充的参数，具体含义参见：http://java.sun.com/j2se/1.4.2/docs/guide/security/jce/JCERefGuide.html#AppA*/    
	//构造函数
	
	byte key[] = new byte[8];
	byte plainBuf []= null;
	int plainLen=0; //明文长度
	byte cipherBuf[]= null;
	int cipherLen = 0;
	
	public encryption()
	{
		//密钥初始化
		for(int i=0; i<8; i++)
		{
			key[i] = 0;			
		}
		
		//buf赋予初始值0
	
	}
	
	public boolean SetKey(String strKey)
	{
		if (strKey ==null)
		{	
			//密钥字符串不存在
			return false;
		}
		
		if (strKey.equals(""))
		{
			return false;
		}
		
		key = strKey.getBytes();
	
		return true;
	}
	
	public String GetKey()
	{
		String strKey = "";
		strKey.valueOf(key);
		
		return strKey;
	}
	
	
	
	
	////////////////////////////////////////////////////////////
	//
	//函数功能:对文件中所有内容进行加密
	//参数:String fileName 文件名称
	//
	/////////////////////////////////////////////////////////////////////
	public void encrptFileAll(String fileName)
	{
		File destiFile = new File(fileName);
		RandomAccessFile raf;
		
		long length = 0;
		int count =0; //可以分成几个加密块, 包括最后一个数据块
		int readLen =0;
		plainBuf = new byte[Block_Len];
		for (int i=0; i<Block_Len; i++)
		{
			plainBuf[i]= 0x0;
		}
		
		try
		{
			raf = new RandomAccessFile(destiFile, "rw");
			length = raf.length();
			count = (int)length/Block_Len +1;
			
			for (int i=0; i<count; i++)
			{
				
				readLen = raf.read(plainBuf, 0, Block_Len);
				if (readLen<Block_Len)
				{
					//处理最后一个数据包,文件内容长度不满一个块长度的情况
					for (int j=readLen; j<Block_Len; j++)
					{
						plainBuf[j] = 0x0;
					}
				}
				encrptBuf();
				raf.seek(i*Block_Len); 
				raf.write(cipherBuf, 0, cipherLen);
				
			}
			//加密后将原有文件的长度,写在最后.
			raf.writeLong(length);
			raf.close();
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		} 
	
		
	}
	
	
	
	
	////////////////////////////////////////////////////////////
	//
	//函数功能:对文件中所有内容进行解密
	//参数:String fileName 文件名称
	//
	/////////////////////////////////////////////////////////////////////
	public void decrptFileAll(String fileName)
	{
		File destiFile = new File(fileName);
		String tempFile = destiFile.getParent()+ "/plain_" + destiFile.getName();
		RandomAccessFile raf;
		FileOutputStream out = null;
		
		long length = 0;	//打开文件的实际长度
		long fileDataLen = 0;	//去掉末尾保存的数据后的,真实加密数据长度
		long originalLen =0;	//明文数据长度
		int count =0; //通过多少个数据块把所有文件内容都读出来.不包括最后一个数据块
		int lastBlock =0; //最后一个数据块的长度
		
		cipherBuf =  new byte[Block_Len];

		try
		{
			out = new FileOutputStream(tempFile);
			raf = new RandomAccessFile(destiFile, "rw");
			length = raf.length()-0x8;
			raf.seek(length);
			originalLen = raf.readLong();
			
			count = (int)(length/Block_Len);
			lastBlock = (int)originalLen %Block_Len;
			
			raf.seek(0);
			for (int i=0; i<count-1; i++)
			{
				raf.read(cipherBuf, 0, Block_Len);
				
				decrptBuf();
				 
				out.write(plainBuf, 0, Block_Len);
				//raf.write(plainBuf, 0, cipherLen);				
			}
			//处理最后一个块
			
			raf.read(cipherBuf, 0, Block_Len);
			
			decrptBuf();
			
			out.write(plainBuf, 0, lastBlock);
			
			raf.close(); //关闭读出文件
			out.close(); //关闭写入文件
		
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		} 
		

	}
	
	/*
	 
		try
		{
			raf = new RandomAccessFile(destiFile, "rw");
			length =  raf.length();
		
			
			if (length>256)
			{
				cipherLen  = 256;
				raf.read(cipherBuf, 0, 256);
			}
			else
			{
				//小于256字节的文件从中读取文件的全部内容进行加密.
				cipherLen = (int)length;
				raf.read(cipherBuf, 0, cipherLen );
			}
			
			decrptBuf();
			
			raf.seek(0); //是否需要进一步确定
			//写入加密数据
			raf.write(plainBuf, 0, plainLen);
			
			
			plainBuf = null;	
			cipherBuf = null;
			raf.close();
			

		}
	  */
	////////////////////////////////////////////////////////////
	//
	//函数功能:对文件内容进行加密
	//参数:String fileName 文件名称
	//
	/////////////////////////////////////////////////////////////////////
	public void encrptFile(String fileName)
	{
		File destiFile = new File(fileName);
		RandomAccessFile raf;
		
		Log.i("333+++", "encryption encrptFile : begin!");
		
		long length=0;
	
		plainBuf = new byte[256];
		for (int i=0; i<256; i++)
		{
			plainBuf[i]= 0x0;
		}
		
		try
		{
			raf = new RandomAccessFile(destiFile, "rw");
			length =  raf.length();
		
			
			if (length>=256)
			{
				plainLen = 256;
				raf.read(plainBuf, 0, 256);
			}
			else
			{
				//小于256字节的文件从中读取文件的全部内容进行加密.
				plainLen= (int)length;
				raf.read(plainBuf, 0, plainLen );
			}
			
			encrptBuf();
			
			raf.seek(0); //是否需要进一步确定
			//写入加密数据
			raf.write(cipherBuf, 0, cipherLen);
			
			plainBuf = null;
			cipherBuf = null;
			raf.close();
			/*
			 小于64字节的密文,回变成64字节.
			 */

		}
		catch(Exception e)
		{
			e.printStackTrace();	
		} 
		
	}
	
		

	////////////////////////////////////////////////////////////
	//
	//函数功能:对文件内容进行解密
	//参数:String fileName 文件名称
	//
	/////////////////////////////////////////////////////////////////////
	public void decrptFile(String fileName)
	{
		File destiFile = new File(fileName);
		RandomAccessFile raf;

		
		long length=0;
		cipherBuf = new byte[256];
		for (int i=0; i<256; i++)
		{
			cipherBuf[i] = 0x0;
		}
	

		try
		{
			raf = new RandomAccessFile(destiFile, "rw");
			length =  raf.length();
		
			
			if (length>256)
			{
				cipherLen  = 256;
				raf.read(cipherBuf, 0, 256);
			}
			else
			{
				//小于256字节的文件从中读取文件的全部内容进行加密.
				cipherLen = (int)length;
				raf.read(cipherBuf, 0, cipherLen );
			}
			
			decrptBuf();
			
			raf.seek(0); //是否需要进一步确定
			//写入加密数据
			raf.write(plainBuf, 0, plainLen);
			
			
			plainBuf = null;	
			cipherBuf = null;
			raf.close();
			/*
			 小于64字节的密文,回变成64字节.
			 */

		}
		catch(Exception e)
		{
			e.printStackTrace();	
		} 
		
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	//
	//函数功能:对256字节长度的数据进行加密
	//参数: String plaintext   明文
	//		String key   建议密钥长度为8字节
	//		String ciphertext	密文
	//
	//
	///////////////////////////////////////////////////////////////////////////
	
	private boolean encrptBuf()
	{

	 
		
		Log.i("333+++", "encryption encrptBuf : begin!");
		
		des_crypt(); /*进行加密*/  
		//长度进行识别.
		
		return true;
		
		
	}

	///////////////////////////////////////////////////////////////////////////
	//
	//函数功能:对256字节长度的数据进行解密
	//参数: String ciphertext  明文
	//		String key   建议密钥长度为8字节
	//		String plaintext	密文
	//
	//
	///////////////////////////////////////////////////////////////////////////
	private boolean decrptBuf()
	{
		
		
		des_decrypt(); /*进行加密*/  
		
		
		//处理补位问题.
		return true;
		
	}
	
	////////////////////////////////////////////////////////////////
	//
	//函数功能:加密数据
	//
	/////////////////////////////////////////////////////////////////
	public void des_crypt() 
	{     
		byte buf[] = null;
	    try 
	    {     
	    	
	        KeySpec ks = new DESKeySpec(key);  //新建密钥规范
	        
	        /* 设置密钥工厂模式为DES，可以设置为AES、DES、DESede、PBEWith等模式  
	    	http://java.sun.com/j2se/1.4.2/docs/guide/security/jce/JCERefGuide.html#AppA  
	    	*/    
	        SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");    
	 
	        SecretKey ky = kf.generateSecret(ks);  //生成密钥 
	        Cipher c = Cipher.getInstance(DES);  //设置加密功能/
	        c.init(Cipher.ENCRYPT_MODE, ky);  //加密初始化/
	        
	        
	        buf = c.doFinal(plainBuf);  //加密运算
	        cipherLen = buf.length;
	        cipherBuf = null;
	        cipherBuf = buf;
	        
	        /*
	        //cipherBuf =  c.doFinal(plainBuf);  //加密运算
	        //获得密文
	        cipherLen = cipherBuf.length; 
	        */
	       
	    } 
	    catch (Exception e) 
	    {     
	        e.printStackTrace();     
	        
	    }     
	}    

	//////////////////////////////////////////////////////////////////////////
	//
	//函数功能:解密数据
	//
	//
	//////////////////////////////////////////////////////////////////////////
	public  void des_decrypt() 
	{     
		byte buf[]=null;
	    try 
	    {     
	        KeySpec ks = new DESKeySpec(key);     
	        SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");     
	        SecretKey ky = kf.generateSecret(ks);     
	        Cipher c = Cipher.getInstance(DES);     
	        c.init(Cipher.DECRYPT_MODE, ky);  
	        
	        buf = c.doFinal(cipherBuf); 
	        plainLen = buf.length;
	        plainBuf = null;
	        plainBuf = buf;
	        
	        
	        /*
	        plainBuf = c.doFinal(cipherBuf);   
	        plainLen = plainBuf.length;
	        */
	        
	    } 
	    catch (Exception e) 
	    {     
	        e.printStackTrace();     
	         
	    }     
	  
	}


}

/*

byte des_key[]= enc_key.getBytes();//从输入框获取密钥 
byte data[] = new byte[64]; //初始化短信数据  
byte tempdata[]= et_text.getText().toString().getBytes();   
//从输入框获取短信明文  
for (int i=0;i<tempdata.length;i++)   
    data[i]=tempdata[i];   
if (tempdata.length <64){ //复制短信内容至data，不足64位补0
        for (int i=data.length;i<64;i++)   
        data[i]='\0';   
        }   
byte result[] =des_crypt(des_key, data); //进行加密

 */