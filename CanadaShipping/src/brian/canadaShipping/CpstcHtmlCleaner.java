package brian.canadaShipping;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CpstcHtmlCleaner {
	private static File htmlFileToClean;
	
	// takes an html file, converts to validated xml
	// andsaves it as <fileName>.xml
	public static void cleanHtmlFile(File htmlFile)
	{
		htmlFileToClean = htmlFile;
		String xmlFileName = convertFilenameToXml(htmlFileToClean.getName());
		
		CleanerProperties props = new CleanerProperties();
		 
		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);
		 
		// do parsing
		TagNode tagNode;
		try {
			tagNode = new HtmlCleaner(props).clean(htmlFileToClean);
	 
			// serialize to xml file
			new PrettyXmlSerializer(props).writeToFile(
			    tagNode, "/tmp/brian/canadaShipping/" + xmlFileName, "utf-8"
			);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// taking a <fileName>.<ext> filename, 
	// converts to and returns <fileName>.xml
	private static String convertFilenameToXml(String filename)
	{
		String rtnString = "";

		Pattern fsaNamePattern = Pattern.compile("^[\\w]+");
		Matcher fsaNameMatcher = fsaNamePattern.matcher(filename);
		if (fsaNameMatcher.find())
		{
			rtnString = fsaNameMatcher.group() + ".xml";
		}
		
		return rtnString;
	}
	
	 

	 

	
	/*
	// optionally find parts of the DOM or modify some nodes
	TagNode[] myNodes = node.getElementsByXXX(...);
	// and/or
	Object[] myNodes = node.evaluateXPath(xPathExpression);
	// and/or
	aNode.removeFromTree();
	// and/or
	aNode.addAttribute(attName, attValue);
	// and/or
	aNode.removeAttribute(attName, attValue);
	// and/or
	cleaner.setInnerHtml(aNode, htmlContent);
	// and/or do some other tree manipulation/traversal
	 
	// serialize a node to a file, output stream, DOM, JDom...
	new XXXSerializer(props).writeXmlXXX(aNode, ...);
	myJDom = new JDomSerializer(props, true).createJDom(aNode);
	myDom = new DomSerializer(props, true).createDOM(aNode);
	*/
}
