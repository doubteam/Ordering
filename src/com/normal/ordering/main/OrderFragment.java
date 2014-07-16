package com.normal.ordering.main;


import com.normal.ordering.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**订餐--Fragment
 * @author Vaboon
 * @date 2014-6-2
 */
public class OrderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.text_content, container, false);
        TextView text = (TextView) fragmentView.findViewById(android.R.id.text1);
        text.setText("Ordering");
        
        return fragmentView;
    }

}
