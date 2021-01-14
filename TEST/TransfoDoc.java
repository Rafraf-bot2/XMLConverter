package TEST;
import org.w3c.dom.Document;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class TransfoDoc {
    //***initialisation des fichiers en entrée && création des fichiers de sortie***
    public static void TraitementDoc(File f) throws Exception { //création des fichiers resultants des traitements effectués dans la classe MethodTransfo
        switch (f.getName())
        {
            case "M457.xml": //M457.xml
                DocumentBuilder parseur0 = Parser.parseur();
                Document docsrc0 = parseur0.parse(f);
                Document docres0 = MethodTransfo.transformM(docsrc0,14,0);
                DOMSource ds0 = new DOMSource(docres0);
                StreamResult res0 = new StreamResult(new File("sortie2.xml"));
                TransformerFactory transform0 = TransformerFactory.newInstance();
                Transformer tr0 = transform0.newTransformer();
                docres0.setXmlStandalone(true);
                tr0.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"dom.dtd");
                tr0.setOutputProperty(OutputKeys.INDENT,"yes");
                tr0.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr0.transform(ds0,res0);
                break;
            case "M674.xml": //M674.xml
            DocumentBuilder parseur = Parser.parseur();
            Document docsrc = parseur.parse(f);
            Document docres = MethodTransfo.transformM(docsrc,6,1);
            DOMSource ds = new DOMSource(docres);
            StreamResult res = new StreamResult(new File("sortie1.xml"));
            TransformerFactory transform = TransformerFactory.newInstance();
            Transformer tr = transform.newTransformer();
            docres.setXmlStandalone(true);
            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"dom.dtd");
            tr.setOutputProperty(OutputKeys.INDENT,"yes");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tr.transform(ds,res);
                break;
            case "fiches.txt": //fiches2.txt
                    Document docres2 =MethodTransfo.TransfoF(LireFiches(f));
                    DOMSource ds2 = new DOMSource(docres2);
                    StreamResult res2 = new StreamResult(new File("fiches2.xml"));
                    TransformerFactory transform2 = TransformerFactory.newInstance();
                    Transformer tr2 = transform2.newTransformer();
                    docres2.setXmlStandalone(true);
                    tr2.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
                    tr2.setOutputProperty(OutputKeys.INDENT,"yes");
                    tr2.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    tr2.transform(ds2,res2);

                    //fiches1.xml
                     docres2 =MethodTransfo.TransfoF1(LireFiches(f));
                     ds2 = new DOMSource(docres2);
                     res2 = new StreamResult(new File("fiches1.xml"));
                     transform2 = TransformerFactory.newInstance();
                     tr2 = transform2.newTransformer();
                     docres2.setXmlStandalone(true);
                     tr2.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
                     tr2.setOutputProperty(OutputKeys.INDENT,"yes");
                     tr2.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                     tr2.transform(ds2,res2);

                break;
            case "boitedialog.fxml": //boitedialog.fxml
                DocumentBuilder parseur6 = Parser.parseur();
                Document docsrc6 = parseur6.parse(f);
                Document docres6 = MethodTransfo.TranfoFxml(docsrc6);
                DOMSource ds6 = new DOMSource(docres6);
                StreamResult res6 = new StreamResult(new File("javafx.xml"));
                TransformerFactory transform6 = TransformerFactory.newInstance();
                Transformer tr6 = transform6.newTransformer();
                docres6.setXmlStandalone(true);
                tr6.setOutputProperty(OutputKeys.INDENT,"yes");
                tr6.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
                tr6.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr6.transform(ds6,res6);
                break;
            case "renault.html": //renault.html
                DocumentBuilder parseur4 = Parser.parseur();
                Document docsrc4 = parseur4.parse(f);
                Document docres4 = MethodTransfo.TransfoHTML(docsrc4);
                DOMSource ds4 = new DOMSource(docres4);
                StreamResult res4 = new StreamResult(new File("renault.xml"));
                TransformerFactory transform4 = TransformerFactory.newInstance();
                Transformer tr4 = transform4.newTransformer();
                docres4.setXmlStandalone(true);
                tr4.setOutputProperty(OutputKeys.INDENT,"yes");
                tr4.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
                tr4.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr4.transform(ds4,res4);
                //System.out.println("renault.xml created");
                break;
            case "poeme.txt": //poeme.txt
                Document docres5 =MethodTransfo.TransfoPoeme(LirePoeme(f));
                DOMSource ds5 = new DOMSource(docres5);
                StreamResult res5 = new StreamResult(new File("neruda.xml"));
                TransformerFactory transform5 = TransformerFactory.newInstance();
                Transformer tr5 = transform5.newTransformer();
                docres5.setXmlStandalone(true);
                tr5.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"neruda.dtd");
                tr5.setOutputProperty(OutputKeys.INDENT,"yes");
                tr5.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                tr5.transform(ds5,res5);
                break;
        }
    }
    public static String[] LireFiches(File fichier) throws IOException { //
        BufferedReader in = new BufferedReader(new FileReader(fichier));
        String line;
        StringBuilder entier = new StringBuilder();
        while ((line = in.readLine()) != null) {
                entier.append(line);
                entier.append("\n");
        }
        String rpl = entier.toString().replaceAll("\r|\n|\r\n|( ){20,}", "_");

        return rpl.split("_+");
    }

    public static String[] LirePoeme(File fichier) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fichier));
        String line;
        StringBuilder entier = new StringBuilder();
        while ((line = in.readLine()) != null) {
            entier.append(line);
            entier.append("\n");
        }
        String rpl = entier.toString().replaceAll("(\n){2,}", "_");
        String[] yo = rpl.split("_+");
        String[] fnl = new String[28];
        int k=0;
        for (String value : yo) {
            String[] buf = value.split("\n");
            fnl[k] = "__";
            k++;
            for (String s : buf) {
                fnl[k] = s;
                k++;
            }
        }
        return fnl;
    }

}
