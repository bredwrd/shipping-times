package brian.canadaShipping;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* This class parses local html tables into RAM for use by the application's
 * various lookups.
 */
public class CpstcFileParser {
	
	// paths for html files pulled from Canada Post's website
	// TODO- put this in a static class (e.g. CpstcPaths)
	private static final String dirtyRemoteHtmlFilePath = "/tmp/brian/canadaShipping/remoteLookup.html";
	private static final String cleanRemoteHtmlFilePath = "/tmp/brian/canadaShipping/remoteLookup.html"; // not necessary to use cleaned xml file for now
	
	private static final String dirtyDpfHtmlFilePath = "/tmp/brian/canadaShipping/dpfLookup.html";
	private static final String cleanDpfHtmlFilePath = "/tmp/brian/canadaShipping/dpfLookup.xml";
	
	private static final String dirtyXpresspostUsaDpfHtmlPath = "/tmp/brian/tmp/xpresspostUsaDpfLookup.html";
	private static final String cleanXpresspostUsaDpfHtmlPath = "/tmp/brian/tmp/xpresspostUsaDpfLookup.html"; // not necessary to use cleaned xml file for now
	
	// Data for postal source/destinations definitions
	private static NodeList remoteNodes;
	private static NodeList dpfNodes;
	private static NodeList majorUsCities;
	private static NodeList xpresspostUsaDpfNodes;

	
	// GETTERS & SETTERS
	
	public static NodeList getXpresspostUsaDpfNodes() {
		return xpresspostUsaDpfNodes;
	}

	public static NodeList getRemoteNodes()
	{
		return remoteNodes;
	}
	
	public static NodeList getDpfNodes()
	{
		return dpfNodes;
	}
	
	public static NodeList getMajorUsCities()
	{
		return majorUsCities;
	}
	
	// METHODS
	
	public static void parseRemoteLookupFile()
	{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
			CpstcHtmlCleaner.cleanHtmlFile(new File(dirtyRemoteHtmlFilePath));
			Document doc = builder.parse(cleanRemoteHtmlFilePath);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
		    
			XPathExpression expr = xpath.compile("//tr/td");
		    Object result = expr.evaluate(doc, XPathConstants.NODESET);
		    remoteNodes = (NodeList) result;
	
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void parseDpfLookupFile()
	{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
			CpstcHtmlCleaner.cleanHtmlFile(new File(dirtyDpfHtmlFilePath));
			Document doc = builder.parse(cleanDpfHtmlFilePath);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
			XPathExpression expr = xpath.compile("//tr/td");
		    Object result = expr.evaluate(doc, XPathConstants.NODESET);
		    dpfNodes = (NodeList) result;			    
	
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseXpresspostUsaDpfNodesFile()
	{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
			CpstcHtmlCleaner.cleanHtmlFile(new File(dirtyXpresspostUsaDpfHtmlPath));
			Document doc = builder.parse(cleanXpresspostUsaDpfHtmlPath);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			
			XPathExpression expr = xpath.compile("//tr/td");
		    Object result = expr.evaluate(doc, XPathConstants.NODESET);
		    xpresspostUsaDpfNodes = (NodeList) result;			    
	
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseMajorUsCitiesLookupFile()
	{
		// TODO parse Major U.S. Cities table from Canada Post
	}
}
