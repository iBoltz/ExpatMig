package iboltz.expatmig.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import iboltz.expatmig.R;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.utils.DateUtils;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class ChatMessageAdapter extends ArrayAdapter<TopicsModel> {
    private final Context context;
    private final ArrayList<TopicsModel> values;

    public ChatMessageAdapter(Context context,
                              ArrayList<TopicsModel> objects) {
        super(context, R.layout.rightsidechat_itemtemplate, objects);

        this.context = context;
        this.values = objects;

    }

    @Override
    public TopicsModel getItem(int position) {
        return values.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View rowView = inflater.inflate(
                    R.layout.rightsidechat_itemtemplate, parent, false);
            TopicsModel item = values.get(position);
            TextView lblRightSideMsg = (TextView) rowView
                    .findViewById(R.id.lblRightSideMsg);
            TextView lblUserName = (TextView) rowView
                    .findViewById(R.id.lblUserName);
            TextView lblCreatedDate = (TextView) rowView
                    .findViewById(R.id.lblCreatedDate);
if(item.CreatedBy == AppCache.HisUserID){
    lblRightSideMsg.setGravity(Gravity.RIGHT);
}

            lblRightSideMsg.setText(item.Description);
            lblRightSideMsg.setTypeface(AppCache.FontQuickRegular);

            lblUserName.setText(item.UserName);
            lblUserName.setTypeface(AppCache.FontQuickRegular);
                String CreatedDate = DateUtils.DisplayDate(item.CreatedDate);

            lblCreatedDate.setText(CreatedDate);

            if (TextUtils.isEmpty(CreatedDate)) {
                lblCreatedDate.setText(item.CreatedDate);
            }

            lblCreatedDate.setTypeface(AppCache.FontQuickRegular);


            return rowView;
        } catch (Exception ex) {

            return null;
        }
    }
}

