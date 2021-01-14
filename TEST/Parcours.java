package TEST;
import java.io.File;
import java.util.*;

public class Parcours {
    //***Parcours du repertoire***
    public static List<File> getFilesInFolder(File folder)
    {
        List<File> files = new ArrayList<>();
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory()) //Si c'est un dossier, on ajoute tous ce qui est dans ce dossier
            {
                List<File> filesInFolder = getFilesInFolder(f); //(Appel récursif) On récupère tout les fichiers
                files.addAll(filesInFolder); //On les ajoute à note list de départ
            } else
            {
                files.add(f); //Si c'est un fichier on l'ajoute
            }
        }
        return files;
    }
}
