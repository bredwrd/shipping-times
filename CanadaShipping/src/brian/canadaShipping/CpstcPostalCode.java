package brian.canadaShipping;

import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.w3c.dom.NodeList;

/* 
 * This class stores and determines data associated with a Postal Code.
 * The postal code name will be "sanitized" from user input to a proper PC 
 * format (e.g. a1a-2b2 -> A1A2B2)
 */
public class CpstcPostalCode {
	// From table 2, stores if a PC is remote (which adds 1-4 days to delivery time)
	public boolean remote;
	
	private String name = ""; // 'name' of PC (e.g. A1A1A1)
	private String fsaName = ""; // Forward Station Area (e.g. A1A)
	private String lduName = ""; // Local Delivery Unit (e.g. 1A1)
	private String dpfName = ""; // Dedicated Processing Facility (e.g. LondonON)
	private String geoArea = ""; // TODO- for domestic parcel support
	private boolean valid = false; // format ok?
	private boolean major = false; // is a major (true) or minor (false) PC (can add time)
	CpstcFsa fsa; // creates Fsa object, since it is a complex piece of information
	
	// returns if postal code is valid and exists	 
	public boolean isValid() {
		return valid;
	}

	public String getGeoArea() {
		return geoArea;
	}

	public void setGeoArea(String geoArea) {
		this.geoArea = geoArea;
	}
	
	public boolean getMajor()
	{
		return major;
	}
	
	public String getDpfName()
	{
		return dpfName;
	}
	
	public boolean getRemote()
	{
		return remote;
	}

	/*
	 * Constructor
	 * postalCode is converted to form [ABCEGHJKLMNPRSTVXY]\d[A-Z]\d[A-Z]\d
	 */
	public CpstcPostalCode(String postalCodeName)
	{
		name = sanitizeName(postalCodeName);
		valid = determineIfValidPostalCode();
		fsaName = determineFsa(name);
		lduName = determineLdu(name);
		fsa = new CpstcFsa(fsaName);
		// set dpf name and if major or non-major
		major = fsa.getMajor();
		dpfName = fsa.getDpfName();
		remote = determineIfRemote();
	}
	
	/*
	 *  determines if postal code is valid and exists
	 */
	private boolean determineIfValidPostalCode()
	{
		boolean rtnBoolean = false;
		
		Pattern tmpPattern = Pattern.compile("[ABCEGHJKLMNPRSTVXY]\\d[A-Z]\\d[A-Z]\\d");
		Matcher tmpMatcher = tmpPattern.matcher(name);
		if (tmpMatcher.find())
		{
			rtnBoolean = true;
			
		}
		return rtnBoolean;
	}
	
	/*
	 *  selects, converts to upper case, and returns the first 6 alphanumeric chars
	 *  returns "" if six alphanumeric chars aren't found
	 */
	private String sanitizeName(String unsanitizedName)
	{
		String rtnString = "";
		// strip non-alphanumeric chars
		unsanitizedName = unsanitizedName.replaceAll("[^\\p{L}\\p{N}]", "");
		// match six alphanumeric chars
		Pattern tmpPattern = Pattern.compile("^[\\w][\\w][\\w][\\w][\\w][\\w]$");
		Matcher tmpMatcher = tmpPattern.matcher(unsanitizedName);
		if (tmpMatcher.find())
		{
			rtnString = tmpMatcher.group().toUpperCase(Locale.ENGLISH);
		}
		return rtnString;
	}
	
	/*
	 * selects and returns first 3 alphanumeric chars (AKA FSA)
	 *  returns "" if a valid FSA isn't found
	 */
	private String determineFsa(String postalCodeName)
	{
		String rtnString = "";
		// match the first three alphanumeric chars
		Pattern tmpPattern = Pattern.compile("[ABCEGHJKLMNPRSTVXY]\\d[A-Z]");
		Matcher tmpMatcher = tmpPattern.matcher(postalCodeName);
		if (tmpMatcher.find())
		{
			rtnString = tmpMatcher.group();
		}
		return rtnString;
	}
	
	/*
	 *  selects and returns last 3 alphanumeric chars (AKA LDU)
	 *  returns "" if a valid LDU isn't found
	 */

	private String determineLdu(String postalCodeName)
	{
		String rtnString = "";
		// match the first three alphanumeric chars
		Pattern tmpPattern = Pattern.compile("[0-9][A-Z][0-9]$");
		Matcher tmpMatcher = tmpPattern.matcher(postalCodeName);
		if (tmpMatcher.find())
		{
			rtnString = tmpMatcher.group();
		}
		return rtnString;
	}
	
	/*
	 *  sets remote by using LDU and DPF
	 */
	private boolean determineIfRemote()
	{
		boolean rtnBoolean = false;
		
		// for now, all remote codes are of format [A-Z]0[A-Z] [0-9][A-Z][O-9]
		// (zero is second char) so only check under that condition
		char secondChar = fsaName.charAt(1);
		if (secondChar == '0')
		{
			NodeList remoteLookupNodes = CpstcFileParser.getRemoteNodes();
		    for (int idx = 0; idx < remoteLookupNodes.getLength(); idx += 2) {
		    	String tmpFsaData = remoteLookupNodes.item(idx).getTextContent();
		    	// First, match FSA, and if it matches, then try to match LDU
		    	if (tmpFsaData.contains(fsaName))
		    	{
		    		String tmpLduData = remoteLookupNodes.item(idx + 1).getTextContent();
		    		if (tmpLduData.contains(lduName) || (tmpLduData.contains("-") && determineIfRangedListContainsLdu(tmpLduData)))
    				{
		    			// is remote
		    			rtnBoolean = true;
		    			break;
    				}
		    	}
		    }
		}
	    
		return rtnBoolean;
	}
	
	/*
	 *  returns DPF name which outgoing USA Xpresspost from this PC is sent through
	 */
	public String determineXpresspostUsaOutgoingDpfNode()
	{
		boolean tmpMatchFound = false;
		String rtnString = "";
		
		NodeList xpressPostUsaDpfNodes = CpstcFileParser.getRemoteNodes();
	    for (int idx = 0; idx < xpressPostUsaDpfNodes.getLength(); idx += 11) {
	    	for (int jdx = 0; jdx < 11; jdx += 2)
	    	{
	    		String tmpPcList = xpressPostUsaDpfNodes.item(idx).getTextContent();
		    	if (tmpPcList.contains(fsaName))
		    	{
		    		// TODO - complete US Xpresspost back-end support
		    		//String tmpLduData = remoteLookupNodes.item(idx + 1).getTextContent();
		    		//if (tmpLduData.contains(lduName) || (tmpLduData.contains("-") && determineIfRangedListContainsLdu(tmpLduData)))
					//{
		    		//	tmpMatchFound = true;
		    		//	break;
					//}
		    	}
	    	}
	    }
		
		return rtnString;
	}
	
	/* checks a list containing a mix of individual and ranged LDUs
	 * and determines if the fsaName is contained within that list
	 */
	private boolean determineIfRangedListContainsLdu( String listToCheck)
	{
		boolean rtnBoolean = false;
		
		// pattern matching any ranged LDU list (e.g. A1A-2B2)
		Pattern fsaRangePattern = Pattern.compile("\\d[A-Z]\\d-\\d[A-Z]\\d");
		Matcher fsaRangeMatcher = fsaRangePattern.matcher(listToCheck);
		while (fsaRangeMatcher.find() && !rtnBoolean)
		{
			// ranged LDU list found and match is not found
			
			String tmpFsaRange = fsaRangeMatcher.group();
			
			// initialize pattern and variable for start of range (e.g. A1A)
			Pattern rangeStartPattern = Pattern.compile("^\\d[A-Z]\\d");
			Matcher rangeStartMatcher = rangeStartPattern.matcher(tmpFsaRange);
			rangeStartMatcher.find();
			String tmpRangeStart = rangeStartMatcher.group();
			
			// initialize pattern and variable for end of range (e.g. B2B)
			Pattern rangeEndPattern = Pattern.compile("\\d[A-Z]\\d$");
			Matcher rangeEndMatcher = rangeEndPattern.matcher(tmpFsaRange);
			rangeEndMatcher.find();
			String tmpRangeEnd = rangeEndMatcher.group();
			
			// check if the LDU in question is within the start - end range
			if (lduName.compareTo(tmpRangeStart) > 0 && lduName.compareTo(tmpRangeEnd) < 0)
			{
				// match found
				rtnBoolean = true;
			}
		}
		
		return rtnBoolean;
	}
	
}
