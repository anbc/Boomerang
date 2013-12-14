package Boomerang_android15.com;



public class base64 
{   
  /* 
     将原始数据编码为base64编码  
    */  
    static public byte[] encode(byte[] data)   
    {   
    	byte[] out = new byte[((data.length + 2) / 3) * 4];   
  
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4)   
        {   
                boolean quad = false;   
                boolean trip = false;   
                int val = (0xFF & (int) data[i]);   
                val <<= 8;   
                if ((i + 1) < data.length)   
                {   
                        val |= (0xFF & (int) data[i + 1]);   
                        trip = true;   
                }   
                val <<= 8;   
                if ((i + 2) < data.length)   
                {   
                        val |= (0xFF & (int) data[i + 2]);   
                        quad = true;   
                }   
                out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];   
                val >>= 6;   
                out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];   
                val >>= 6;   
                out[index + 1] = alphabet[val & 0x3F];   
                val >>= 6;   
                out[index + 0] = alphabet[val & 0x3F];   
        }   
        return out;   
    }   
    /**  
    * 将base64编码的数据解码成原始数据  
    */  
    static public byte[] decode(byte[] data)   
    {   
      int len = ((data.length + 3) / 4) * 3;   
      if(data.length > 0 && data[data.length - 1] == '=')   
        --len;   
      if(data.length > 1 && data[data.length - 2] == '=')   
        --len;   
      byte[] out = new byte[len];   
      int shift = 0;   
      int accum = 0;   
      int index = 0;   
      for(int ix = 0; ix < data.length; ix++)   
      {   
        int value = codes[data[ix] & 0xFF];   
        if(value >= 0)   
        {   
          accum <<= 6;   
          shift += 6;   
          accum |= value;   
          if(shift >= 8)   
          {   
            shift -= 8;   
            out[index++] = (byte)((accum >> shift) & 0xff);   
          }   
        }   
      }   
      if(index != out.length)   
        throw new Error("miscalculated data length!");   
      return out;   
    }   
  
    static private byte[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".getBytes();   
  
    static private byte[] codes = new byte[256];   
    static {   
            for (int i = 0; i < 256; i++)   
                    codes[i] = -1;   
            for (int i = 'A'; i <= 'Z'; i++)   
                    codes[i] = (byte) (i - 'A');   
            for (int i = 'a'; i <= 'z'; i++)   
                    codes[i] = (byte) (26 + i - 'a');   
            for (int i = '0'; i <= '9'; i++)   
                    codes[i] = (byte) (52 + i - '0');   
            codes['+'] = 62;   
            codes['/'] = 63;   
    }   
    
  /*
    public static void main(String[] args) throws Exception   
    {   
      //加密成base64   
      String strSrc = "你好吗";   
      String strOut = new String(base64.encode(strSrc.getBytes()));   
      System.out.println(strOut);   
  
      strSrc = "你好----重点文件目录";   
      strOut = new String(base64.encode(strSrc.getBytes()));   
      System.out.println(strOut); 
      
      
      String strOut2 = new String(base64.decode(strOut.getBytes()));   
      System.out.println(strOut2);   
    }   
    */
    
    
}  
