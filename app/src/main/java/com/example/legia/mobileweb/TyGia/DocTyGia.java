package com.example.legia.mobileweb.TyGia;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DocTyGia {
    public DocTyGia() {
    }

    public static Document parse (InputStream is) {
        Document ret = null;
        DocumentBuilderFactory domFactory;
        DocumentBuilder builder;

        try {
            domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(false);
            domFactory.setNamespaceAware(false);
            builder = domFactory.newDocumentBuilder();

            ret = builder.parse(is);
        }
        catch (Exception ex) {
            System.out.println("unable to load XML: " + ex);
        }
        return ret;
    }

    public static  double giaBan(){
        double giaBan =0;
        try {
            URL xmlUrl = new URL("https://www.vietcombank.com.vn/ExchangeRates/ExrateXML.aspx");
            InputStream in = xmlUrl.openStream();
            Document doc = parse(in);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Exrate");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                // kiểm tra

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    /*System.out.println("Ký hiệu: " + element.getAttribute("CurrencyCode"));
                    System.out.println("Nước: " + element.getAttribute("CurrencyName"));
                    System.out.println("Giá mua: " + element.getAttribute("Buy"));
                    System.out.println("Giá bán: " + element.getAttribute("Sell"));
                    System.out.println("Giá chuyển: " + element.getAttribute("Transfer"));*/
                    if(element.getAttribute("CurrencyCode").equals("USD")){
                        giaBan = Integer.parseInt(element.getAttribute("Sell"));

                    }

                }
            }

        } catch (MalformedURLException ex) {

        } catch (IOException ex) {

        }

        return giaBan;
    }


}
