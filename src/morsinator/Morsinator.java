package morsinator;

public class Morsinator {
    public static void main(String[] args) {
        if(args.length != 4) {
            System.err.println("morsinator <option> <table-conversion> <fichier-entrée> <fichier-sortie>\n\n" +
                               "Options :\n" +
                               "    -tm  --texte-morse    Convertit de texte vers morse\n" +
                               "    -mt  --morse-texte    Convertit de morse vers texte");
            System.exit(1);
        }
    }
}