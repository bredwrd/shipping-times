package brian.canadaShipping;
// This class is a Forward Station Area, or the first three digits of a postal code.

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;

public class CpstcFsa {
	
	
	// VARIABLES
	
	private String fsaName = ""; // first three digits of the postal code (e.g. A0E)
	private boolean valid = false; // is format ok (i.e. letter-number-letter)
	private boolean major = false; // is a major, not a minor FSA
	private String dpfName = ""; // Dedicated Processing Facility city name associated with the FSA
	
	
	// GETTERS & SETTERS
	
	public boolean getMajor()
	{
		return major;
	}
	
	public boolean getValid()
	{
		return valid;
	}
	
	public String getDpfName()
	{
		return dpfName;
	}
	
	
	// CONSTRUCTOR
	
	public CpstcFsa(String name)
	{
		fsaName = name;
		valid = determineIfValid();
		determineDpfNameAndIfMajor();
	}
	
	// METHODS
	
	private boolean determineIfValid()
	{
		boolean rtnBoolean = false;
		
		// attempt to match regex with valid FSA format and return if match is found
	    Pattern validFsaPattern = Pattern.compile("[ABCEGHJKLMNPRSTVXY]\\d[A-Z]");
		Matcher validFsaMatcher = validFsaPattern.matcher(fsaName);
		if (validFsaMatcher.find())
		{
			rtnBoolean = true;
		}
	    return rtnBoolean;
	}

	// sets dpfName and major
	public void determineDpfNameAndIfMajor()
	{	
		NodeList dpfNodeData = CpstcFileParser.getDpfNodes();
		for (int idx = 0; idx < dpfNodeData.getLength(); idx += 3)
		{
			int dpfNameIndex = idx;
			int listOfMajorFsaIndex = idx + 1;
			int listOfNonMajorFsaIndex = idx + 2;
			
		    String tmpDpfName = dpfNodeData.item(dpfNameIndex).getTextContent().replaceAll("\\W","");
		    
		    String listOfMajorFsa = dpfNodeData.item(listOfMajorFsaIndex).getTextContent().replaceAll("\\s","");
			if (listOfMajorFsa.contains(fsaName) || determineIfRangedListContainsFsa(listOfMajorFsa))
			{
				major = false;
				dpfName = tmpDpfName;
				break;
			}
		    
		    String listOfNonMajorFsa = dpfNodeData.item(listOfNonMajorFsaIndex).getTextContent().replaceAll("\\s","");
			if (listOfNonMajorFsa.contains(fsaName) || determineIfRangedListContainsFsa(listOfNonMajorFsa))
			{
				major = false;
				dpfName = tmpDpfName;
				break;
			}
		}
	}
	
	// checks a list containing a mix of individual and ranged FSAs
	// and determines if the fsaName is contained within that list
	private boolean determineIfRangedListContainsFsa( String listToCheck)
	{
		boolean rtnBoolean = false;
		
		Pattern fsaRangePattern = Pattern.compile("[ABCEGHJKLMNPRSTVXY]\\d[A-Z]-[ABCEGHJKLMNPRSTVXY]\\d[A-Z]");
		Matcher fsaRangeMatcher = fsaRangePattern.matcher(listToCheck);
		while (fsaRangeMatcher.find() && !rtnBoolean)
		{
			String tmpFsaRange = fsaRangeMatcher.group();
			
			Pattern rangeStartPattern = Pattern.compile("^[ABCEGHJKLMNPRSTVXY]\\d[A-Z]");
			Matcher rangeStartMatcher = rangeStartPattern.matcher(tmpFsaRange);
			rangeStartMatcher.find();
			String tmpRangeStart = rangeStartMatcher.group();
			
			Pattern rangeEndPattern = Pattern.compile("[ABCEGHJKLMNPRSTVXY]\\d[A-Z]$");
			Matcher rangeEndMatcher = rangeEndPattern.matcher(tmpFsaRange);
			rangeEndMatcher.find();
			String tmpRangeEnd = rangeEndMatcher.group();
			
			if (fsaName.compareTo(tmpRangeStart) > 0 && fsaName.compareTo(tmpRangeEnd) < 0)
			{
				rtnBoolean = true;
			}
		}
		
		return rtnBoolean;
	}
}
