package iboltz.expatmig.models;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class TopicsModel {
    private static final long serialVersionUID = 85681452;
    public TopicsModel(){

    }
    public TopicsModel(TopicsModel Item){
        this.UserName=Item.UserName;
        this.TopicID=Item.TopicID;
        this.ThreadID=Item.ThreadID;
        this.Description=Item.Description;
        this.CreatedBy=Item.CreatedBy;
        this.CreatedDate=Item.CreatedDate;
        this.Slug=Item.Slug;
        this.IsActive=Item.IsActive;
        this.SeqNo=Item.SeqNo;
        this.ModifiedBy=Item.ModifiedBy;
        this.ModifiedDate=Item.ModifiedDate;
        this.AttachmentType=Item.AttachmentType;
        this.AttachmentURL=Item.AttachmentURL;
        this.UserDeviceID=Item.UserDeviceID;
        this.IsAndroid=Item.IsAndroid;
        this.ThreadName=Item.ThreadName;
    }
    public String UserName;
    public int TopicID;
    public int ThreadID;
    public String Description;
    public int CreatedBy;
    public String CreatedDate;
    public String Slug;
    public Boolean IsActive;
    public int SeqNo;
    public int ModifiedBy;
    public String ModifiedDate;
    public String AttachmentType;
    public String AttachmentURL;
    public  int UserDeviceID;
    public Boolean IsAndroid;
    public String ThreadName;
}

