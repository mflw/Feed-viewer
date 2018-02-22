
package com.helloworld;
//package src.main.java.com.helloworld;
import java.util.Scanner;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import java.io.IOException;
import java.util.HashSet;

public class Parser {
    //HashSet offers = new HashSet();
    private Document feed;
    private XPath xpath = XPathFactory.newInstance().newXPath();

    public Category[] categoriesArr = null;
    public Offer[] offersArr = null;

    public Parser(String input) throws IOException, SAXException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            ErrorHandler errhandler = new MyErrorHandler();
            builder.setErrorHandler(errhandler);
            feed = builder.parse(input);
        
        } catch (ParserConfigurationException e) {
            System.out.println("Market feed parse error!");
            e.printStackTrace();
        }
    }

    public String getDate() {
        try {
        XPathExpression categories_expression = xpath.compile("//yml_catalog");
        NodeList ymlCatalog = (NodeList) categories_expression.evaluate(feed, XPathConstants.NODESET);
        NamedNodeMap ymlCatalogAttr = ymlCatalog.item(0).getAttributes();
        return ymlCatalogAttr.getNamedItem("date").getTextContent();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return "Can't find <yml_catalog>.";
        }
    }

    public Offer[] getOffers() {
        //парсинг офферов
        NodeList offerElements;
        String name ="Element doesnt exist";
        Category category = null;
        int price = 0;
        Double priceDouble = 0.0;
        int o = 0;
        long id = 0;
    	try {
            XPathExpression categories_expression = xpath.compile("//offer");
            NodeList offers = (NodeList) categories_expression.evaluate(feed, XPathConstants.NODESET);
            offersArr = new Offer[offers.getLength()];
            for (int i = 0; i < offers.getLength(); i++) {
                NamedNodeMap offerAttr = offers.item(i).getAttributes();
                id = Long.parseLong(offerAttr.getNamedItem("id").getTextContent());
                boolean available = Boolean.parseBoolean(offerAttr.getNamedItem("available").getTextContent());
                offerElements = offers.item(i).getChildNodes();
                for(int j = 0; j<offerElements.getLength(); j++) {
                    if (offerElements.item(j).getNodeName()=="name") name = offerElements.item(j).getTextContent();
                    if (offerElements.item(j).getNodeName()=="price") {
                        try {
                            price = Integer.parseInt(offerElements.item(j).getTextContent());
                        } catch (NumberFormatException e) {
                            priceDouble = Double.parseDouble(offerElements.item(j).getTextContent());
                            price = priceDouble.intValue();
                            o++;
                        }
                    }
                    if (offerElements.item(j).getNodeName()=="categoryId") {
                        for (int k = 0; k<categoriesArr.length; k++) {
                            if (categoriesArr[k].getId()==Long.parseLong(offerElements.item(j).getTextContent())) {
                                category = categoriesArr[k];
                            }
                        }
                    }                    
                }
                offersArr[i] = new Offer(id, available, name, category, price);
            }
            if (o>0) System.out.println("Warning: <price> value is not integer number and will be rounded! offerId: '"+id+"' and "+o+" offers more.");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return offersArr;
    }

    public Offer findOffer(Long id) {
        for(int i=0; i<this.offersArr.length; i++){
                if(this.offersArr[i].getId()==id) {
                    return this.offersArr[i];   
                }
            }
        return null;
    }

    public void validateCategories() {
        //проверка категорий
        //long[] categoriesIds = null;
        NodeList categories;
        try {
            XPathExpression categories_expression = xpath.compile("//category");
            categories = (NodeList) categories_expression.evaluate(feed, XPathConstants.NODESET);
            //categoriesIds = new long[categories.getLength()];
            categoriesArr = new Category[categories.getLength()];
            for (int i = 0; i < categories.getLength(); i++) {
                NamedNodeMap attr = categories.item(i).getAttributes();
                long categoryId = Long.parseLong(attr.getNamedItem("id").getTextContent());
                //categoriesIds[i] = categoryId;
                Long categoryParentId = null;
                try {
                    categoryParentId = Long.parseLong(attr.getNamedItem("parentId").getTextContent());
                    Category category = new Category(categoryId, categoryParentId);
                    categoriesArr[i] = category;
                } catch (NullPointerException e) {
                    Category category = new Category(categoryId);
                    categoriesArr[i] = category;
                }     
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return;
        }
        Category[] notUniqueCategories = new Category[categoriesArr.length*2];
        Category[] loopLinkCategories = new Category[categoriesArr.length*2];
        int k = 0;
        int u = 0;
        int l = 0;
        Category[] notExistingParentCategories = new Category[categoriesArr.length];
        for (int i = 0; i<categoriesArr.length; i++) {
            boolean validParentCategory = false;
            //проверка сущ. родительских категорий
            for(int y=0; y<categoriesArr.length; y++){
                try {
                    if(categoriesArr[i].getParentId()==categoriesArr[y].getId()){
                        validParentCategory = true;
                        if(i==y) {
                            loopLinkCategories[l] = categoriesArr[i];
                            l++;
                        }    
                    }
                } catch (NullPointerException e) {
                    validParentCategory = true;   
                }
            }
            if (!validParentCategory) {
                notExistingParentCategories[u]=categoriesArr[i];
                u++;
            }
            //проверка уникальности id категорий
            for (int j = i+1; j<categoriesArr.length; j++) {
                if (categoriesArr[i].getId()==categoriesArr[j].getId()) {
                notUniqueCategories[k]=categoriesArr[i];
                k++;
                }
            }
        }
        if (k!=0) {
            for (int i = 0; i<k; i++) {
                System.out.println("Не уникальная категория с id = "+notUniqueCategories[i].getId());
            }
        }
        for (int i=0; i<u; i++) {
            System.out.println("Категория с id = "+notExistingParentCategories[i].getId()+" содержит несуществующую родительскую категорию с parentId = "+notExistingParentCategories[i].getParentId());
        }
        for (int i=0; i<l; i++) {
            System.out.println("Категория с id = "+loopLinkCategories[i].getId()+" ссылается сама на себя (id = parentId)");
        }
    }
}