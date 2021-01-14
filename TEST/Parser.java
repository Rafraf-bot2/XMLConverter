package TEST;
import javax.xml.parsers.*;
import org.w3c.dom.DOMImplementation;
public class Parser {
    //***Creation du parseur***
    static DocumentBuilder parseur;
    public static  DocumentBuilder parseur() throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //ces 2 lignes ci-dessous servent a ne pas prendre en compte les éventuelles DTD ("TEIFrantext.dtd")
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);
        parseur= factory.newDocumentBuilder();
        return parseur;
    }
    public static  DOMImplementation imp() {
        return parseur.getDOMImplementation();
    }
}
