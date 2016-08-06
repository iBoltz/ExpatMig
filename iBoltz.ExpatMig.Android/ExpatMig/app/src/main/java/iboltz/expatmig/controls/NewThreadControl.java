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
import iboltz.expatmig.facades.ThreadsFacade;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.LogHelper;


/**
 * Created by dev on 07-12-2015.
 */
public class NewThreadControl extends LinearLayout {

    Context CurrentContext;
    Dialog ThisDialog;
    private OnCreatedListener Watcher;

    public NewThreadControl(Context CurrentContext, Dialog dialog) {
        super(CurrentContext);
        String ThisLayoutInflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater ThisInflater = (LayoutInflater) getContext().getSystemService(ThisLayoutInflater);
        ThisInflater.inflate(R.layout.newthread, this);
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


        final EditText txtThreadName = (EditText) findViewById(R.id.txtThreadName);

        final Button btnNewThread = (Button) findViewById(R.id.btnNewThread);
        btnNewThread.setClickable(true);
        btnNewThread.setEnabled(true);
        btnNewThread.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtThreadName.getText())) {
                    Toast.makeText(getContext(), "Invalid thread name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CreateThread(txtThreadName.getText().toString(),AppCache.SelectedGroup.GroupID);

                btnNewThread.setClickable(false);
                btnNewThread.setEnabled(false);
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

    private void CreateThread(String ThreadName,int GroupID) {
        try {
            ThreadsModel ThisThread = new ThreadsModel();
            ThisThread.GroupID=GroupID;
            ThisThread.Description = ThreadName;
            ThisThread.Slug = "";
            ThisThread.IsActive = true;
            ThisThread.SeqNo=1;
            ThisThread.CreatedDate= AppConstants.StandardDateFormat
                    .format(new Date());
            ThisThread.CreatedBy = AppCache.HisUserID;
            ThisThread.ModifiedBy=AppCache.HisUserID;
            ThisThread.ModifiedDate=AppConstants.StandardDateFormat
                    .format(new Date());
            SendNewThread(ThisThread);
        }catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void SendNewThread(final ThreadsModel ThisThread) {
        //    final Long StartTime = System.currentTimeMillis();
        try{
        ThreadsFacade tf = new ThreadsFacade(CurrentContext);
        tf.SaveNewThread(ThisThread);
            tf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {

                Toast.makeText(CurrentContext, "New Thread created successfully", Toast.LENGTH_SHORT).show();
                ThisDialog.dismiss();
                RaiseOnCreated();
            }
        });
    }catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

}
