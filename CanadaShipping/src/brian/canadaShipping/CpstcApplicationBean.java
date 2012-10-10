package brian.canadaShipping;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/* 
 * This class is run once during application initialization, and performs
 * long-running non-lazy initialization of data, such as reading html tables
 * from a hard disk or networked medium.
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class CpstcApplicationBean {

	// VARIABLES
	
	// array with a name of a DPF, with values of all DPF "base times" for Lettermail as the values
	private static CpstcDedicatedProcessingFacility[] dpfGrid = 
			new CpstcDedicatedProcessingFacility[CpstcDedicatedProcessingFacility.getNumOfDpf()]; 
	
	
	// GETTERS & SETTERS
	
	public static CpstcDedicatedProcessingFacility[] getDpfGrid()
	{
		return dpfGrid;
	}
	
	
	// CONSTRUCTOR
	
	public CpstcApplicationBean()
	{
		super();
		init();
		// Debug
		System.out.println("Hello, ApplicationBean!");
		System.out.println("Context Path = " + FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
	}
	
	
	// METHODS
	
	private void init()
	{
		setupDpfGrid();
		CpstcFileParser.parseRemoteLookupFile();
		CpstcFileParser.parseDpfLookupFile();
	}
	
	/*
	 *  initializes dpfGrid with values from Delivery Standards site
	 */
	private void setupDpfGrid()
	{
		// HTML document only contains an image, no parsable text, so hardcoded
		// TODO- parse data from pdf available from Delivery Standards site
		CpstcDedicatedProcessingFacility dpfCalgary = new CpstcDedicatedProcessingFacility("CalgaryAB", 			2, 4, 3, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfCalgary;
		CpstcDedicatedProcessingFacility dpfCharlottetown = new CpstcDedicatedProcessingFacility("CharlottetownPE", 4, 2, 4, 4, 3, 4, 4, 6, 4, 4, 3, 4, 4, 4, 4, 3, 4, 3, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfCharlottetown;
		CpstcDedicatedProcessingFacility dpfEdmonton = new CpstcDedicatedProcessingFacility("EdmontonAB", 			3, 4, 2, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfEdmonton;
		CpstcDedicatedProcessingFacility dpfFredericton = new CpstcDedicatedProcessingFacility("FrederictonNB", 	4, 4, 4, 2, 3, 4, 5, 6, 4, 4, 3, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfFredericton;
		CpstcDedicatedProcessingFacility dpfHalifax = new CpstcDedicatedProcessingFacility("HalifaxNS", 			4, 3, 4, 3, 2, 4, 4, 6, 4, 4, 3, 4, 4, 4, 4, 3, 4, 3, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfHalifax;
		CpstcDedicatedProcessingFacility dpfHamilton = new CpstcDedicatedProcessingFacility("HamiltonON", 			4, 4, 4, 4, 4, 2, 5, 6, 3, 3, 4, 4, 3, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfHamilton;
		CpstcDedicatedProcessingFacility dpfHappyValley = new CpstcDedicatedProcessingFacility("HappyValleyNL", 	5, 4, 5, 5, 4, 5, 2, 6, 5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfHappyValley;
		CpstcDedicatedProcessingFacility dpfIqualuit = new CpstcDedicatedProcessingFacility("IqaluitNU", 			6, 6, 6, 6, 6, 6, 6, 2, 6, 6, 6, 4, 5, 5, 6, 6, 6, 6, 5, 6, 6, 6, 6, 6, 6);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfIqualuit;
		CpstcDedicatedProcessingFacility dpfKitchener = new CpstcDedicatedProcessingFacility("KitchenerON", 		4, 4, 4, 4, 4, 3, 5, 6, 2, 3, 4, 4, 3, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfKitchener;
		CpstcDedicatedProcessingFacility dpfLondon = new CpstcDedicatedProcessingFacility("LondonON", 				4, 4, 4, 4, 4, 3, 5, 6, 3, 2, 4, 4, 3, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfLondon;
		CpstcDedicatedProcessingFacility dpfMoncton = new CpstcDedicatedProcessingFacility("MonctonNB", 			4, 3, 4, 3, 3, 4, 4, 6, 4, 4, 2, 4, 4, 4, 4, 3, 4, 3, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfMoncton;
		CpstcDedicatedProcessingFacility dpfMontreal = new CpstcDedicatedProcessingFacility("MontralQC", 			4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 2, 3, 3, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfMontreal;
		CpstcDedicatedProcessingFacility dpfOttawa = new CpstcDedicatedProcessingFacility("OttawaONGatineauQC", 	4, 4, 4, 4, 4, 3, 5, 5, 3, 3, 4, 3, 2, 3, 4, 4, 4, 4, 3, 4, 4, 4, 3, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfOttawa;
		CpstcDedicatedProcessingFacility dpfQuebec = new CpstcDedicatedProcessingFacility("QubecQC", 				4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 3, 3, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfQuebec;
		CpstcDedicatedProcessingFacility dpfRegina = new CpstcDedicatedProcessingFacility("ReginaSK", 				4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 2, 4, 3, 4, 4, 4, 4, 4, 4, 3, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfRegina;
		CpstcDedicatedProcessingFacility dpfStJohn = new CpstcDedicatedProcessingFacility("StJohnNB", 				4, 3, 4, 3, 3, 4, 4, 6, 4, 4, 3, 4, 4, 4, 4, 2, 4, 3, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfStJohn;
		CpstcDedicatedProcessingFacility dpfSaskatoon = new CpstcDedicatedProcessingFacility("SaskatoonSK", 		4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 3, 4, 2, 4, 4, 4, 4, 4, 4, 3, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfSaskatoon;
		CpstcDedicatedProcessingFacility dpfStJohns = new CpstcDedicatedProcessingFacility("StJohnsNL", 			4, 3, 4, 4, 3, 4, 3, 6, 4, 4, 3, 4, 4, 4, 4, 3, 4, 2, 4, 4, 4, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfStJohns;
		CpstcDedicatedProcessingFacility dpfToronto = new CpstcDedicatedProcessingFacility("TorontoON",				4, 4, 4, 4, 4, 3, 5, 5, 3, 3, 4, 3, 3, 4, 4, 4, 4, 4, 2, 4, 4, 4, 3, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfToronto;
		CpstcDedicatedProcessingFacility dpfVancouver = new CpstcDedicatedProcessingFacility("VancouverBC", 		4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 3, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfVancouver;
		CpstcDedicatedProcessingFacility dpfVictoria = new CpstcDedicatedProcessingFacility("VictoriaBC",			4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 2, 4, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfVictoria;
		CpstcDedicatedProcessingFacility dpfWhitehorse = new CpstcDedicatedProcessingFacility("WhitehorseYT",		4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 4, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfWhitehorse;
		CpstcDedicatedProcessingFacility dpfWindsor = new CpstcDedicatedProcessingFacility("WindsorON",				4, 4, 4, 4, 4, 3, 5, 6, 3, 3, 4, 4, 3, 4, 4, 4, 4, 4, 3, 4, 4, 4, 2, 4, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfWindsor;
		CpstcDedicatedProcessingFacility dpfWinnipeg = new CpstcDedicatedProcessingFacility("WinnipegMB", 			4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 3, 4, 3, 4, 4, 4, 4, 4, 4, 2, 4);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfWinnipeg;
		CpstcDedicatedProcessingFacility dpfYellowknife = new CpstcDedicatedProcessingFacility("YellowknifeNT",		4, 4, 4, 4, 4, 4, 5, 6, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2);
		dpfGrid[CpstcDedicatedProcessingFacility.getNextIndex()] = dpfYellowknife;
	}
}
