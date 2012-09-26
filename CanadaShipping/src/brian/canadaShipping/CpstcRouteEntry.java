package brian.canadaShipping;
// TODO- work in progress; to be implemented with trip detailing

public class CpstcRouteEntry {

	
	// VARIALES
	
	private short numberOfBusinessDays;
	private String locationName = "";
	
	
	// GETTERS & SETTERS
	
	public short getNumberOfBusinessDays()
	{
		return numberOfBusinessDays;
	}

	public String getLocationName() {
		return locationName;
	}
	
	
	// CONSTRUCTOR
	
	public CpstcRouteEntry(String tmpLocationName, short tmpNumberOfBusinessDays)
	{
		locationName = tmpLocationName;
		numberOfBusinessDays = tmpNumberOfBusinessDays;
	}
}
