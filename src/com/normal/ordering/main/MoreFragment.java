package com.normal.ordering.main;



import com.normal.ordering.R;
import com.normal.ordering.discountfragment.MoreFragmentAdapter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**更多--Fragment
 * @author Vaboon
 * @date 2014-6-2
 */
public class MoreFragment extends Fragment {

	//标题
	private final static String[] moreApplications=new String[]{
		"设置",
		"其他"
	};
	//设置标题的listview内容
	private final static String[] setUp=new String[]{
		"消息提醒",
		"分享设置",
		"邀请好友使用",
		"字体大小",
		"清空缓存"
	};
	//其他标题的listview内容
	private final static String[] other=new String[]{
		"扫一扫",
		"意见反馈",
		"帮助",
		"检查更新",
		"关于Ordering",
		"诊断网络"
	};
	
	private MoreFragmentAdapter adapter;
	private ListView moreApplicationListView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_more, container, false);
        
        //初始化
        this.adapter=new MoreFragmentAdapter(getActivity());
        ArrayAdapter<String> listAdapter=null;
        
        /*
         * 给每个标题绑定listview
         */
        
       for(String moreApplication:moreApplications){
    	   if(moreApplication.equals("设置")){
    		   listAdapter= new ArrayAdapter<String>(getActivity(), R.layout.fragment_more_item, setUp);
    		   adapter.addSection(moreApplication,listAdapter);
    	   }else if(moreApplication.equals("其他")){
    		   listAdapter= new ArrayAdapter<String>(getActivity(), R.layout.fragment_more_item, other);
    		   adapter.addSection(moreApplication,listAdapter);
    	   }
        }
        moreApplicationListView=(ListView) fragmentView.findViewById(R.id.fragment_more_list);
        moreApplicationListView.setAdapter(adapter);
        //listview的点击事件
        moreApplicationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long duration) {
				String item = (String)adapter.getItem(position);
				Toast.makeText(getActivity().getApplicationContext(), item, Toast.LENGTH_SHORT).show();
			}
		});
        return fragmentView;
    }
  
}
