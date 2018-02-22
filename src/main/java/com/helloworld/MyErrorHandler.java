package com.helloworld;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
 
public class MyErrorHandler implements ErrorHandler {
 
    public void warning(SAXParseException ex) {
        System.err.println(ex.getMessage());
    }
 
    public void error(SAXParseException ex) {
        System.err.println(ex.getMessage());
    }
 
    public void fatalError(SAXParseException ex) throws SAXParseException {
        throw ex;
    }
 
}