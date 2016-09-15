package iboltz.expatmig.models;

import java.util.ArrayList;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class GroupsModel {
    private static final long serialVersionUID = 85681452;

    public int GroupID ;
    public String Description;
    public String Slug;
    public Boolean IsActive;
    public int SeqNo;
    public int CreatedBy;
    public String CreatedDate;
    public int ModifiedBy;
    public String ModifiedDate;
    public ArrayList<ThreadsModel> AllThreads;

    public GroupsModel(){

    }
    public GroupsModel(String Description, ArrayList<ThreadsModel> MyThreads) {
        super();
        this.Description = Description;
        this.AllThreads = MyThreads;
    }

}

