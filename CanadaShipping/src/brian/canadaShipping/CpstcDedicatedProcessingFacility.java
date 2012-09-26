package brian.canadaShipping;

public class CpstcDedicatedProcessingFacility {
	
	
	// CONSTANTS
	
	private static final int numOfDpf = 25;
	
	public static int getNumOfDpf() {
		return numOfDpf;
	}

	
	// VARIABLES
	
	private int[] dpfBaseTimeList = new int[numOfDpf];
	public static String[] dpfNameList = new String[numOfDpf];
	private static int currentNumOfDpf = 0;

	private static int indexOfNamesList = 0;
	private String name = "";
	
	// GETTERS & SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String[] getDpfNameList() {
		return dpfNameList;
	}

	public static void setDpfNameList(String[] dpfNameList) {
		CpstcDedicatedProcessingFacility.dpfNameList = dpfNameList;
	}
	
	
	// CONSTRUCTOR
	
	public CpstcDedicatedProcessingFacility(String dpfName, int... dpfBaseDistanceList)
	{
		setName(dpfName);
		if (indexOfNamesList <= numOfDpf)
		{
			dpfNameList[indexOfNamesList] = dpfName;
			indexOfNamesList++;
		}
		
		dpfBaseTimeList = dpfBaseDistanceList;
	}
	
	
	// METHODS
	
	/*
	 * Returns the base time between DPF
	 * 	-1 if not found in list
	 *
	 */
	public int getBaseTime(String destination)
	{
		int indexOfDestination = getIndexOf(destination);
		if (indexOfDestination != -1)
		{
			return dpfBaseTimeList[indexOfDestination];
		} else
		{
			return -1;
		}
	}
	
	/*
	 * Returns the index of the provided dpf name
	 * 	-1 if not found in list
	 */
	public static int getIndexOf(String dpfName)
	{
		int indexOfDpf = -1;
		int dpfNameListLength = dpfNameList.length;
		for (int i = 0; i < dpfNameListLength; i++)
		{
			if (dpfNameList[i].equals(dpfName))
			{
				indexOfDpf = i;
			}
		}
		return indexOfDpf;
	}
	
	/*
	 * Returns and increases currentNumOfDpf
	 */
	public static int getNextIndex()
	{
		if (currentNumOfDpf < numOfDpf)
		{
			currentNumOfDpf ++;
			return currentNumOfDpf - 1;
		} else
		{
			return -1;
		}
	}
}
