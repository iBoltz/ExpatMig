package iboltz.expatmig.models;

import java.util.ArrayList;

public class UsersModel
{
	private static final long serialVersionUID = 30590061;

	public int UserID;
	public String UserName;
	public String Password;
	public String FirstName;
	public String LastName;
	public String Address;
	public String PhoneNo;
	public String EmailAddress;
	public int SeqNo;
	public String IsActive;
	public int CreatedBy;
	public String CreatedDate;
	public int ModifiedBy;
	public String ModifiedDate;
	public ArrayList<UserRolesModel> UserRoles;
	public ArrayList<Integer> DesignationIDList;
	public UserDevicesModel HisUserDevice;
}
