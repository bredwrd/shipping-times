package brian.canadaShipping;

import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.w3c.dom.NodeList;

public class CpstcPostalCode {
	public boolean remote;
	
	private String name = "";
	private String fsaName = ""; // Forward Station Area (e.g. A1A)
	private String lduName = ""; // Local Delivery Unit (e.g. 1A1)
	private String dpfName = ""; // Dedicated Processing Facility (e.g. LondonON)
	private String geoArea = ""; // TODO
	private boolean valid = false; // format ok?
	private boolean major = false;
	CpstcFsa fsa;
	
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
	
	// determines if postal code is valid and exists
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
	
	// selects, converts to upper case, and returns the first 6 alphanumeric chars
	// returns "" if six alphanumeric chars aren't found
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
	
	
	// selects and returns first 3 alphanumeric chars (AKA FSA)
	// returns "" if a valid FSA isn't found
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
	
	// selects and returns last 3 alphanumeric chars (AKA LDU)
	// returns "" if a valid LDU isn't found
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
	
	// sets remote by using LDU and DPF
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
		    	if (tmpFsaData.contains(fsaName))
		    	{
		    		String tmpLduData = remoteLookupNodes.item(idx + 1).getTextContent();
		    		if (tmpLduData.contains(lduName) || (tmpLduData.contains("-") && determineIfRangedListContainsLdu(tmpLduData)))
    				{
		    			rtnBoolean = true;
		    			break;
    				}
		    	}
		    }
		}
	    
		return rtnBoolean;
	}
	
	// checks a list containing a mix of individual and ranged LDUs
	// and determines if the fsaName is contained within that list
	private boolean determineIfRangedListContainsLdu( String listToCheck)
	{
		boolean rtnBoolean = false;
		
		Pattern fsaRangePattern = Pattern.compile("\\d[A-Z]\\d-\\d[A-Z]\\d");
		Matcher fsaRangeMatcher = fsaRangePattern.matcher(listToCheck);
		while (fsaRangeMatcher.find() && !rtnBoolean)
		{
			String tmpFsaRange = fsaRangeMatcher.group();
			
			Pattern rangeStartPattern = Pattern.compile("^\\d[A-Z]\\d");
			Matcher rangeStartMatcher = rangeStartPattern.matcher(tmpFsaRange);
			rangeStartMatcher.find();
			String tmpRangeStart = rangeStartMatcher.group();
			
			Pattern rangeEndPattern = Pattern.compile("\\d[A-Z]\\d$");
			Matcher rangeEndMatcher = rangeEndPattern.matcher(tmpFsaRange);
			rangeEndMatcher.find();
			String tmpRangeEnd = rangeEndMatcher.group();
			
			if (lduName.compareTo(tmpRangeStart) > 0 && lduName.compareTo(tmpRangeEnd) < 0)
			{
				rtnBoolean = true;
			}
		}
		
		return rtnBoolean;
	}
	
}
