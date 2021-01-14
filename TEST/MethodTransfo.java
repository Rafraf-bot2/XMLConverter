package TEST;
import org.w3c.dom.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class MethodTransfo {
    //***Traitement et transformation des fichiers reçu***
    public static Document transformM(Document docsrc,int nb,int t) throws Exception{
        DOMImplementation domimp = Parser.imp();
        Document docres = domimp.createDocument(null,"TEI_S",null);
        Element rootres = docres.getDocumentElement();
        Element rootsrc = docsrc.getDocumentElement();
        String Sprem;
        if(t==1)
            Sprem="M674.xml";
        else
            Sprem="M457.xml";
        Element prem_elt_res =docres.createElement(Sprem);
        rootres.appendChild(prem_elt_res);
        Element chell = docres.createElement("texte");

        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();
        String expression = "/TEI/teiHeader/fileDesc/publicationStmt/availability/p"; //recuperation du premier element
        String str = (String) path.evaluate(expression,rootsrc, XPathConstants.STRING);
        prem_elt_res.appendChild(chell);
        chell.appendChild(docres.createTextNode(str));
        for (int i=1;i<=nb;i++)
        {
            expression ="/TEI/text/body/div["+i+"]/p";
            String n = (String) path.evaluate(expression,rootsrc,XPathConstants.STRING);
            String[] lignes =n.split("\n");
            for (int j = 0; j<lignes.length;j++)
            {
                if(!(i==nb && j==lignes.length-1))
                {
                    chell = docres.createElement("texte");
                    prem_elt_res.appendChild(chell);
                    chell.appendChild(docres.createTextNode(lignes[j]));
                }
            }
        }
        return docres;
    }
    public static Document TransfoHTML(Document docsrc) throws Exception
    {
        DOMImplementation domimp = Parser.imp();
        Document docres = domimp.createDocument(null,"Concessionnaires",null);
        Element rootres = docres.getDocumentElement();
        Element rootsrc = docsrc.getDocumentElement();

        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();

        for (int i=1;i<14;i++)
        {
            String expression = "//div[@id=\"contentsingle\"]/div/p["+i+"]";
            String str = (String) path.evaluate(expression,rootsrc, XPathConstants.STRING);
            String rpl = str.replaceAll("[^\\S ]", " "); // match tab and newline but not space
            String rpl2 = rpl.replaceAll("( ){2,}", "_");
            String[] ligne = rpl2.split("_");
            if(i==1)
            {
                for (int j = 1; j<ligne.length;j++)
                {
                    if(j==1)
                    {
                        Element elt = docres.createElement("Nom");
                        rootres.appendChild(elt);
                        elt.appendChild(docres.createTextNode(ligne[1]));
                    }
                    else
                    {
                        String[] ligne2 = ligne[j].split(": ");
                        for (int k = 0; k<ligne2.length;k++)
                        {
                            if(k%2==0)
                            {
                                String isis;
                                if (j==3)
                                    isis="Num_téléphone";
                                else
                                    isis= ligne2[k];
                                Element elt = docres.createElement(isis);
                                rootres.appendChild(elt);
                                elt.appendChild(docres.createTextNode(ligne2[k+1]));
                            }
                        }
                    }
                }
            }
            else
            {
                for (int k = 1; k<ligne.length;k++)
                {
                    switch (k)
                    {
                        case 1:
                            Element elt = docres.createElement("Nom");
                            rootres.appendChild(elt);
                            elt.appendChild(docres.createTextNode(ligne[1]));
                        break;
                        case 2:
                            Element elt2 = docres.createElement(ligne[k].substring(0,ligne[k].length()-2));
                            rootres.appendChild(elt2);
                            elt2.appendChild(docres.createTextNode(ligne[k+1]));
                        break;
                        case 4:
                            Element elt4 = docres.createElement("Num_téléphone");
                            rootres.appendChild(elt4);
                            elt4.appendChild(docres.createTextNode(ligne[k+1]));
                        break;
                    }
                }
            }
        }
        return docres;
    }

    public static Document TranfoFxml(Document docsrc) throws Exception
    {
        DOMImplementation domimp = Parser.imp();
        Document docres = domimp.createDocument(null,"Racine",null);
        Element rootres = docres.getDocumentElement();
        rootres.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:fx","http://javafx.com/fxml");
        Element rootsrc = docsrc.getDocumentElement();
        Element elt;
        XPathFactory xpf = XPathFactory.newInstance();
        XPath path = xpf.newXPath();

        String expression = "//@*";
        NodeList nody =(NodeList) path.evaluate(expression,rootsrc,XPathConstants.NODESET);
        String[] att= new String[nody.getLength()];
        String[] val = new String[nody.getLength()];

        for (int i = 0; i <nody.getLength() ; i++) {
            Node n = nody.item(i);
            att[i]=n.getNodeName();
            val[i]=n.getTextContent();
        }
        for (int i = 0; i <att.length ; i++) {
           elt = docres.createElement("texte");
           rootres.appendChild(elt);
           elt.setAttribute(att[i],"x");
           elt.appendChild(docres.createTextNode(val[i]));
           if(i==6) //les noeuds "xmlns" et "xmlns.fx" sont ajoutés manuellement
           {
                elt = docres.createElement("texte");
                rootres.appendChild(elt);
                elt.setAttribute("xmlns","x");
                elt.appendChild(docres.createTextNode("http://javafx.com/javafx/8.0.40"));

                elt = docres.createElement("texte");
                rootres.appendChild(elt);
                elt.setAttribute("xmlns:fx","x");
                elt.appendChild(docres.createTextNode("http://javafx.com/fxml/1"));
           }
        }
    return docres;
    }

    public static Document TransfoF(String[] filesrc) {
        DOMImplementation domimp = Parser.imp();
        Document docres = domimp.createDocument(null,"FICHES",null);
        Element rootres = docres.getDocumentElement();

        boolean bAR = false;
        boolean bFR = false;
        boolean fNT = false;

        String sAR="AR";
        String sFR="FR";
        Element id,lAR,lFR;

        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","1");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");
        for (int i = 0; i <22 ; i++) //si  on est au 1er PNR
        {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR) //si on est pas encore arrivé à la ligne "AR"
            {
                Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                id.appendChild(elt);
                if(i==0) //si on est à la ligne "PNR..."
                {
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR)) //si on est à la ligne "AR"
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR) //si on a depassé la ligne "AR" mais pas encore la ligne "FR"
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT"))) //si on est pas arrivé à la premiere ligne qui se finit par "NT"
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT")) //si on est à la ligne qui se finit par "NT"
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT"))) //si on a depassé la premiere ligne qui se finit par "NT"
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR))) //si on est à la ligne "FR"
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                }
            }
            if(bFR && !(filesrc[i].equals(sFR))) //si on a depassé la ligne "FR"
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT"))) // si on n'est pas encore arrivé à la 1ere ligne qui se finit par "NT
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT")) //si on est arrivé à la 1ere ligne qui se finit par "NT"
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT"))) //si on a depassé la premiere ligne se finit par "NT"
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }
        bAR=false; bFR=false; fNT=false;
        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","2");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");

        for (int i = 22; i <44 ; i++) //si  on est au 2eme PNR     on reprend le meme raisonnement que pour la précedente boucle
        {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                id.appendChild(elt);
                if(i==22)
                {
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }

        bAR=false; bFR=false; fNT=false;
        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","3");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");
        for (int i = 44; i <64 ; i++) //si  on est au 3eme PNR     on reprend le meme raisonnement que pour les précedentes boucles
        {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                id.appendChild(elt);
                if(i==44)
                {
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }
        bAR=false; bFR=false; fNT=false;
        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","4");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");
        for (int i = 64; i <84 ; i++) //si  on est au 4eme PNR     on reprend le meme raisonnement que pour les précedentes boucles
        {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                id.appendChild(elt);
                if(i==64)
                {
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }
        return docres;
    }

    public static Document TransfoF1(String[] filesrc) {
        //on applique ici le meme raisonnement que pour TransfoF
        //la seule difference ici est que  les nodes DO et SD sont des enfants des  balises "Langue"
        // et non FICHES

        DOMImplementation domimp = Parser.imp();
        Document docres = domimp.createDocument(null,"FICHES",null);
        Element rootres = docres.getDocumentElement();
        boolean bAR = false;
        boolean bFR = false;
        boolean fNT = false;
        String sAR="AR";
        String sFR="FR";
        Element id;
        Element lAR;
        Element lFR;

        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","1");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");
        for (int i = 0; i <22 ; i++)
        {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                if(i==0)
                {
                    Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                    id.appendChild(elt);
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    if(i!=2 && i!=3)
                    {
                        Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                        id.appendChild(elt);
                        String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                        elt.appendChild(docres.createTextNode(text));
                    }

                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                    Element elt =docres.createElement("DO");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                     elt =docres.createElement("SD");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));

                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                    Element elt =docres.createElement("DO");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }
        bAR=false; bFR=false; fNT=false;
        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","2");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");

        for (int i = 22; i <44 ; i++)
        {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                if(i==22)
                {
                    Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                    id.appendChild(elt);
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    if(i!=24 && i!=25)
                    {
                        Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                        id.appendChild(elt);
                        String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                        elt.appendChild(docres.createTextNode(text));
                    }

                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                    Element elt =docres.createElement("DO");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));

                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                    Element elt =docres.createElement("DO");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }

        bAR=false; bFR=false; fNT=false;
        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","3");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");
        for (int i = 44; i <64 ; i++) {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                if(i==44)
                {
                    Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                    id.appendChild(elt);
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    if(i!=46 && i!=47)
                    {
                        Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                        id.appendChild(elt);
                        String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                        elt.appendChild(docres.createTextNode(text));
                    }

                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                    Element elt =docres.createElement("DO");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));

                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                    Element elt =docres.createElement("DO");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }
        bAR=false; bFR=false; fNT=false;
        id = docres.createElement("FICHE");
        rootres.appendChild(id);
        id.setAttribute("id","4");
        lAR = docres.createElement("Langue");
        lFR = docres.createElement("Langue");
        for (int i = 64; i <84 ; i++) {
            if(!(filesrc[i].substring(0,2).equals(sAR)) && !bAR)
            {
                if(i==64)
                {
                    Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                    id.appendChild(elt);
                    elt.appendChild(docres.createTextNode(filesrc[i].substring(0,filesrc[i].length()-2)));
                }
                else
                {
                    if(i!=66 && i!=67)
                    {
                        Element elt = docres.createElement(filesrc[i].substring(filesrc[i].length()-2));
                        id.appendChild(elt);
                        String text =filesrc[i].substring(filesrc[i].length()-2)+" : "+filesrc[i].substring(0,filesrc[i].length()-2);
                        elt.appendChild(docres.createTextNode(text));
                    }
                }
            }
            else
            {
                if(filesrc[i].substring(0,2).equals(sAR))
                {
                    bAR = true;
                    id.appendChild(lAR);
                    lAR.setAttribute("id","AR");
                    Element elt =docres.createElement("DO");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lAR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));

                }
            }
            if(bAR && !(filesrc[i].equals(sFR)) && !(filesrc[i].equals(sAR)) && !bFR)
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lAR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lAR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
            else
            {
                if((filesrc[i].equals(sFR)))
                {
                    fNT=false;
                    bFR=true;
                    id.appendChild(lFR);
                    lFR.setAttribute("id","FR");
                    Element elt =docres.createElement("DO");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("DO : Médecine & Orthophonie\t"));
                    elt =docres.createElement("SD");
                    lFR.appendChild(elt);
                    elt.appendChild(docres.createTextNode("SD : ORL; Audiologie; Surdité\t"));
                }
            }
            if(bFR && !(filesrc[i].equals(sFR)))
            {
                int t = filesrc[i].length();
                if(!fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                }
                else if(!fNT && filesrc[i].substring(t-4,t-2).equals("NT"))
                {
                    Element elt = docres.createElement(filesrc[i].substring(t-4,t-2));
                    lFR.appendChild(elt);
                    String text = filesrc[i].substring(t-4,t-2)+" : "+filesrc[i].substring(0,t-4);
                    elt.appendChild(docres.createTextNode(text));
                    fNT=true;
                }
                if (fNT && !(filesrc[i].substring(t-4,t-2).equals("NT")))
                {
                    Element elt = docres.createElement("RF");
                    lFR.appendChild(elt);
                    String text = "RF | "+filesrc[i];
                    elt.appendChild(docres.createTextNode(text));
                }
            }
        }
        return docres;
    }

    public static Document TransfoPoeme(String[] filesrc) {
        DOMImplementation domimp = Parser.imp();
        Document docres = domimp.createDocument(null,"poema",null);
        Element rootres = docres.getDocumentElement();
        Element paragraphe = docres.createElement("estrofa");
        Element vers;
        Element titre = docres.createElement("titulo");
        rootres.appendChild(titre);
        titre.appendChild(docres.createTextNode(filesrc[1]));
        // on a separé les paragraphes par un "__" dans la méthode TansfoDoc.LirePoeme
        for (int i = 2; i <filesrc.length ; i++)
        {
            if(filesrc[i].equals("__"))
            {
                paragraphe = docres.createElement("estrofa");
                rootres.appendChild(paragraphe);
            }
            else
            {
                vers = docres.createElement("verso");
                paragraphe.appendChild(vers);
                vers.appendChild(docres.createTextNode(filesrc[i]));
            }
        }
        return docres;
    }
}
