package iboltz.expatmig.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import iboltz.expatmig.R;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.models.TopicsModel;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class ChatMessageAdapter extends ArrayAdapter<TopicsModel>
{
    private final Context context;
    private final ArrayList<TopicsModel> values;

    public ChatMessageAdapter(Context context,
                              ArrayList<TopicsModel> objects)
    {
        super(context, R.layout.rightsidechat_itemtemplate, objects);

        this.context = context;
        this.values = objects;

    }

    @Override
    public TopicsModel getItem(int position)
    {
        return values.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        try
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View rowView = inflater.inflate(
                    R.layout.rightsidechat_itemtemplate, parent, false);
            TopicsModel item = values.get(position);
            TextView lblRightSideMsg = (TextView) rowView
                    .findViewById(R.id.lblRightSideMsg);
            lblRightSideMsg.setText(item.Description);
            lblRightSideMsg.setTypeface(AppCache.FontQuickRegular);
            return rowView;
        } catch (Exception ex)
        {

            return null;
        }
    }
}

