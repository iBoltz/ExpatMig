package iboltz.expatmig.controls;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnCreatedListener;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.facades.GroupsFacade;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.LogHelper;


/**
 * Created by dev on 07-12-2015.
 */
public class NewGroupControl extends LinearLayout {

    Context CurrentContext;
    Dialog ThisDialog;
    private OnCreatedListener Watcher;

    public NewGroupControl(Context CurrentContext, Dialog dialog) {
        super(CurrentContext);
        String ThisLayoutInflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater ThisInflater = (LayoutInflater) getContext().getSystemService(ThisLayoutInflater);
        ThisInflater.inflate(R.layout.newgroup, this);
        this.CurrentContext = CurrentContext;
        this.ThisDialog = dialog;
        InitControls();
    }

    public void setEventListener(OnCreatedListener listener) {
        Watcher = listener;
    }

    public void RaiseOnCreated() {
        EventObject eObj = new EventObject(new Object());

        if (Watcher != null)
            Watcher.OnCreated(eObj); // event object :)
    }

    private void InitControls() {


        final EditText txtDetails = (EditText) findViewById(R.id.txtGroupName);

        final Button btnNewGroup = (Button) findViewById(R.id.btnNewGroup);
        btnNewGroup.setClickable(true);
        btnNewGroup.setEnabled(true);
        btnNewGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtDetails.getText())) {
                    Toast.makeText(getContext(), "Invalid group name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CreateGroup(txtDetails.getText().toString());

                btnNewGroup.setClickable(false);
                btnNewGroup.setEnabled(false);
                ThisDialog.dismiss();

            }
        });
        Button btnCloseVoid = (Button) findViewById(R.id.btnClose);
        btnCloseVoid.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ThisDialog.dismiss();
            }
        });
    }

    private void CreateGroup(String GroupName) {
        try {
            GroupsModel ThisGroup = new GroupsModel();
            ThisGroup.Description = GroupName;
            ThisGroup.Slug = "";
            ThisGroup.IsActive = true;
            ThisGroup.SeqNo=1;
            ThisGroup.CreatedDate= AppConstants.StandardDateFormat
                    .format(new Date());
            ThisGroup.CreatedBy = AppCache.HisUserID;
            ThisGroup.ModifiedBy=AppCache.HisUserID;
            ThisGroup.ModifiedDate=AppConstants.StandardDateFormat
                    .format(new Date());
            SendNewGroup(ThisGroup);
        }catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void SendNewGroup(final GroupsModel ThisGroup) {
        //    final Long StartTime = System.currentTimeMillis();
        try{
        GroupsFacade VoidOf = new GroupsFacade(CurrentContext);
        VoidOf.SaveNewGroup(ThisGroup);
            VoidOf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {

                Toast.makeText(CurrentContext, "New Group created successfully", Toast.LENGTH_SHORT).show();
                ThisDialog.dismiss();
                RaiseOnCreated();
            }
        });
    }catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

}
