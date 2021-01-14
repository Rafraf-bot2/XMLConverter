package TEST;
import java.io.File;
import java.util.List;

public class ApiDom {
    //***Recuperation du repertoire en argument && Lancement des traitements***
    public static void main(String[] args) throws Exception {
        File dos;
        //File dos = new File("C:\\Users\\Raf\\Documents\\L3\\S2\\Doc-Str\\Projet\\projet");
        try {
             dos = new File(args[0]);
        }catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Attention vous avez oublié de spécifier le nom du répertoire a traiter !");
            return;
        }
        List<File> fichiers = Parcours.getFilesInFolder(dos);
        for (File fichier : fichiers) {
            TransfoDoc.TraitementDoc(fichier);

        }
    }
}
