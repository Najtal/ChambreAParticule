package util;
import java.util.Random;

/**
 * Created by jvdur on 15/01/2016.
 */
public class Util {

    /**
     * Retourne un tableau mélangé
     * @param arraySize la taille du tableau souhaité
     * @return un tableau de int mélangé entre 0 et arraySize-1
     */
    public static int[] getRandomTable(int arraySize) {
        // On génère le tableau
        int[] array = getTable(arraySize);

        // On mélange le tableau
        int n = array.length;
        for (int i = 0; i < array.length; i++) {
            int random = i + (int) (Math.random() * (n - i));
            int randomElement = array[random];
            array[random] = array[i];
            array[i] = randomElement;
        }

        return array;
    }

    static int[] getTable(int arraySize) {
        int[] array = new int[arraySize];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        return array;
    }

    public static boolean validationDesArguments(int nbAgents, int gridSizeX, int gridSizeY, int ballSize, boolean grille, boolean isTorique) {

        boolean ce = false;

        if (nbAgents < 1) ce = printValid(3,ce,"Le nombre d'agents doit êttre suppérieur à zéro");
        if (nbAgents < 2000) ce = printValid(2,ce,"Attention ca fait beaucoup de billes");
        if (gridSizeX*gridSizeY < nbAgents) ce = printValid(3,ce,"Il y a trop peu de cases pour le nombre de billes !");
        if (nbAgents/(gridSizeX*gridSizeY) > 0.8) ce = printValid(2,ce,"Il ne reste pas beaucoup de place pour les billes, tableau rempli à " + nbAgents/gridSizeX*gridSizeY *100 + '%');
        if (ballSize < 0) ce = printValid(3,ce,"La taille des billes doit etre positif !");

        if (ce) return ce;

        // INFO
        print(1, "Nombre de loups \t " + nbAgents);
        print(1, "Taille du plateau \t " + gridSizeX + "x" + gridSizeY);
        print(1, "Taille des billes \t " + ballSize);
        print(1, "La grille " + ((!grille)?"ne sera pas":"sera") + " affichée");
        print(1, "Le monde " + ((!isTorique)?"n'est pas":"est") + " torique");

        return ce;
    }

    private static boolean printValid(int i, boolean ce, String s) {
        print(i,s);
        if (ce) return ce;
        if (!ce && i == 3) return false;
        else return ce;
    }

    private static void print(int i,String s) {
        if (i == 1) {
            System.out.println("INFO: " + s);
        } else if (i == 2) {
            System.out.println("WARNING: " + s);
        } else {
            System.out.println("ERROR: " + s);
        }

    }

    private boolean greathan(int x, int y) {
        return (x > y);
    }
}
