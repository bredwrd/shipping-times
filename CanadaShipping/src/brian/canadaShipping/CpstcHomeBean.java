package brian.canadaShipping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;


import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.jdk.DateKitCalculatorsFactory;


@ManagedBean(name= "home")
@SessionScoped
public class CpstcHomeBean implements Serializable {
	
	// VARIABLES
	
	// General

	private String destinationType = "Domestic";
	private String shippingType = "Lettermail";
	private String userSelectedShippingType = shippingType + " (" + destinationType + ")";

	// Rendered Panels
	private boolean domesticDestinationPanelRendered = true;
	private boolean usaDestinationPanelRendered = false;
	private boolean selectUsStateMapDialogRendered = false;

	// U.S. values
	private String userSelectedUsStateCode = "";

	private static final long serialVersionUID = 1L;
	public String userOriginDpfName = "";
	public String userDestinationDpfName = "";
	public String pageResult = "";
	private String userOriginPcName = "";
	private String userDestinationPcName = "";
	private Date selectedInputDate = new Date(System.currentTimeMillis());
	private Date selectedOutputDate;
	@SuppressWarnings("unused") private Date sentDate;
	private Date calculatedArrivalBeginDate = new Date();
	private Date calculatedArrivalEndDate = new Date();
	private DateCalculator<Date> dateCalculator;
	private boolean rangedArrivalDate = false;
	
	private boolean displayOutput = false;
	
	// day counts
	private int baseTime = 0;
	private final int remoteTime = 4;
	private int totalTime = 0;
	
	// route entries
	private ArrayList<CpstcRouteEntry> routeEntryList = new ArrayList<CpstcRouteEntry>();

	private CpstcDedicatedProcessingFacility[] dpfGrid = CpstcApplicationBean.getDpfGrid();
	
	
	//GETTERS & SETTERS	
	
	public ArrayList<CpstcRouteEntry> getRouteEntryList() {
		return routeEntryList;
	}
	
	public boolean getDisplayOutput()
	{
		return displayOutput;
	}
	
	public Date getSelectedOutputDate()
	{
		return selectedOutputDate;
	}
	
	public void setSelectedOutputDate(Date selectedOutputDate)
	{
		this.selectedOutputDate = selectedOutputDate;
	}
	
	public Date getCalculatedArrivalBeginDate() {
		return calculatedArrivalBeginDate;
	}

	public Date getCalculatedArrivalEndDate() {
		return calculatedArrivalEndDate;
	}
	
	public Date getSelectedInputDate() {
		return selectedInputDate;
	}
	
    public void setSelectedInputDate(Date selectedDate) {
        this.selectedInputDate = selectedDate;
    }

	
	public String getUserOriginPcName() {
		return userOriginPcName;
	}

	public String getUserDestinationPcName() {
		return userDestinationPcName;
	}

	public void setUserDestinationPcName(String userDestinationPcName) {
		this.userDestinationPcName = userDestinationPcName;
	}

	public void setUserOriginPcName(String userOriginPcName) {
		this.userOriginPcName = userOriginPcName;
	}

	public String getPageResult() {
		return pageResult;
	}

	public void setPageResult(String pageResult) {
		this.pageResult = pageResult;
	}
	
	public String getUserDestinationDpfName() {
		return userDestinationDpfName;
	}

	public void setUserDestinationDpfName(String userDestinationDpfName) {
		this.userDestinationDpfName = userDestinationDpfName;
	}

	public String getUserOriginDpfName() {
		return userOriginDpfName;
	}

	public void setUserOriginDpfName(String userSourceName) {
		this.userOriginDpfName = userSourceName;
	}

	public String getUserSelectedUsStateCode() {
		return userSelectedUsStateCode;
	}
	
	public void setUserSelectedUsStateCode(String userSelectedUsStateCode) {
		this.userSelectedUsStateCode = userSelectedUsStateCode;
	}
	
	public String getUserSelectedShippingType() {
		return userSelectedShippingType;
	}
	
	public boolean isDomesticDestinationPanelRendered() {
		return domesticDestinationPanelRendered;
	}

	public void setDomesticDestinationPanelRendered(
			boolean domesticDestinationPanelRendered) {
		this.domesticDestinationPanelRendered = domesticDestinationPanelRendered;
	}

	public boolean isUsaDestinationPanelRendered() {
		return usaDestinationPanelRendered;
	}

	public void setUsaDestinationPanelRendered(boolean usaDestinationPanelRendered) {
		this.usaDestinationPanelRendered = usaDestinationPanelRendered;
	}

	public boolean isSelectUsStateMapDialogRendered() {
		return selectUsStateMapDialogRendered;
	}
	
	public void toggleSelectUsStateMapDialogRendered(ActionEvent evt)
	{
		selectUsStateMapDialogRendered = !selectUsStateMapDialogRendered;
	}
	
	// CONSTRUCTOR
	
	public CpstcHomeBean()
	{
		super();
		init();
		System.out.println("Hello, HomeBean!");
	}
	
	
	// METHODS
	
	private void init()
	{
		displayOutput = false;
	}
	
	/*
	 * @called: when "Submit" button is activated
	 */
	public void submitAction(ActionEvent evt)
	{	
		pageResult = "";
		totalTime = 0;
		rangedArrivalDate = false;
		
		// Origin input; common for all destinations and shipping type
		sentDate = selectedInputDate;
		CpstcPostalCode userOriginPostalCode = new CpstcPostalCode(userOriginPcName);
		
		if (destinationType.equals("Domestic") && shippingType.equals("Lettermail"))
		{

			CpstcPostalCode userDestinationPostalCode = new CpstcPostalCode(userDestinationPcName);
			
			userOriginDpfName = userOriginPostalCode.getDpfName();
			userDestinationDpfName = userDestinationPostalCode.getDpfName();
			
			int sourceIndex = CpstcDedicatedProcessingFacility.getIndexOf(userOriginDpfName);
			try {
				baseTime = dpfGrid[sourceIndex].getBaseTime(userDestinationDpfName);
				totalTime += baseTime;
				pageResult = Integer.toString(baseTime);
				// for now, do not differentiate between which PC (source or dest.)
				// is remote, but it would be possible.
				if (userOriginPostalCode.getRemote() || userDestinationPostalCode.getRemote())
				{
					// is remote transaction
					totalTime += remoteTime;
					rangedArrivalDate = true;
				}
				
				// handle ranged arrival time due to remote PC
				calculatedArrivalBeginDate = addBusinessDays(baseTime);
				if (rangedArrivalDate)
				{
					pageResult += " to " + Integer.toString(baseTime + remoteTime);
					calculatedArrivalEndDate = addBusinessDays(totalTime);
				} else
				{
					calculatedArrivalEndDate = calculatedArrivalBeginDate;
				}

				// display resulting output
				selectedOutputDate = calculatedArrivalEndDate;
				pageResult += " business days";
				displayOutput = true;
				
			} catch(ArrayIndexOutOfBoundsException e)
			{
				pageResult = "Source or destination not found.";
			}
		} else if (destinationType.equals("USA") && shippingType.equals("Xpresspost"))
		{
			System.out.println("USA! USA! USA!");
			String tmpOutgoingDpfNode = userOriginPostalCode.determineXpresspostUsaOutgoingDpfNode();
		}
		

		

	}
    
    /*
     * @param daysToAdd - number of business days to add to selectedInputDate
     * @returns selectedInputDate plus daysToAdd counted by business days
     */
    private Date addBusinessDays(int daysToAdd)
    {
        HashSet<Date> holidays = new HashSet<Date>();

        DefaultHolidayCalendar<Date> holidayCalendar =
            new DefaultHolidayCalendar<Date>(holidays);

        DateKitCalculatorsFactory.getDefaultInstance()
                .registerHolidays("example", holidayCalendar);
        dateCalculator = DateKitCalculatorsFactory.getDefaultInstance()
                .getDateCalculator("example", HolidayHandlerType.FORWARD);
        dateCalculator.setStartDate(selectedInputDate);
        return (dateCalculator).moveByBusinessDays(daysToAdd).getCurrentBusinessDate();
    }
    
    public String selectUsState(String stateCode)
    {
    	System.out.println(stateCode);
    	return "";
    }
    
    public void changeUserSelectedShippingType(ActionEvent evt)
    {
		Map<String, Object> attributes = evt.getComponent().getAttributes();
		String selectedShippingType = (String) attributes.get("shippingType");
    	// TODO- figure out why it's not reaching this line
		String selectedDestinationType = (String) attributes.get("destinationType");
		
    	// Debug
    	System.out.println("Hello, changeUserSelectedShippingType!");
    	System.out.println("Changing to: " + selectedShippingType + " to " + selectedDestinationType);
		
		if (selectedDestinationType.equals("Domestic"))
		{
			setUsaDestinationPanelRendered(false);
			setDomesticDestinationPanelRendered(true);
		} else if (selectedDestinationType.equals("USA"))
		{
			setDomesticDestinationPanelRendered(false);
			setUsaDestinationPanelRendered(true);
		}
    	userSelectedShippingType = selectedShippingType + " (" + selectedDestinationType + ")";
    }
    


}