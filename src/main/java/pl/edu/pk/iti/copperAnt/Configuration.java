package pl.edu.pk.iti.copperAnt;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;


public class Configuration {
	private static Configuration instance = null;
	public static String ROOT_NAME = "device";
	private List<Properties> m_data = new ArrayList<Properties>();
        private static final String path = "./src/main/resources/logs";
	public static Configuration getInstance() {
		  if(instance == null) {
		         instance = new Configuration();
		  }
		      return instance;
		   
	}
	public Configuration() {
		
		
	}
	public List<Properties> getData() {
		return m_data;
	}
		
	public void init(String fileName) {
		try {
		
			File file = new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName(Configuration.ROOT_NAME);
			for (int s = 0; s < nodeLst.getLength(); ++s) {

			    Node fstNode = nodeLst.item(s);
			    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			  
			      Element device = (Element) fstNode;
			      NamedNodeMap deviceAttr = device.getAttributes();
			      Properties prop = new Properties();
			      for (int i = 0, len = deviceAttr.getLength(); i < len; ++i) {
			    	  Node attr = deviceAttr.item(i);
			    	  prop.setProperty(attr.getNodeName(), attr.getNodeValue());
			    	  
			      }
			      m_data.add(prop);
			      
			    }

			  }
			  } catch (Exception e) {
				  //TODO logger
				  System.out.println("Error reading file!");
			    e.printStackTrace();
			  }
			 
		}	
	
	public static FileAppender generateAppender(String pathToFile, Integer numberOfFile) throws IOException{
            return new FileAppender(new PatternLayout("%d %p (%t) [%c] - %m%n"),path+pathToFile+numberOfFile+".out",true);
        }	 
}