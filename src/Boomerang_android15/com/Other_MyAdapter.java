package Boomerang_android15.com;


/* import相关class */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* 告定义的Adapter，继承android.widget.BaseAdapter */
public class Other_MyAdapter extends BaseAdapter
{
  private LayoutInflater mInflater;
  private int[] color;
  private int[] text;

  public Other_MyAdapter(Context context,int[] _color,int[] _text)
  {
    mInflater = LayoutInflater.from(context);
    color = _color;
    text = _text;
  }
  
  /* 帆继承BaseAdapter，需覆写北兀method */

  public int getCount()
  {
    return text.length;
  }


  public Object getItem(int position)
  {
    return text[position];
  }
  

  public long getItemId(int position)
  {
    return position;
  }
  

  public View getView(int position,View convertView,ViewGroup par)
  {
    ViewHolder holder;
    
    if(convertView == null)
    {
      /* 使用告定义的change_color囫为Layout */
      convertView = mInflater.inflate(R.layout.change_color, null);
      /* 初始化holder的text */
      holder = new ViewHolder();
      holder.mText=(TextView)convertView.findViewById(R.id.myText);
      convertView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.mText.setText(text[position]);
    holder.mText.setBackgroundResource(color[position]);
     
    return convertView;
  }
  
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView mText;
  }
}

