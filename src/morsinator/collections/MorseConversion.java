package morsinator.collections;

import morsinator.table.*;

public interface MorseConversion {
    /**
     * Ajoute une nouvelle paire lettre/morse à cette table de conversion
     * 
     * @param letter lettre à ajouter
     * @param morse  code morse représentant la lettre
     */
    public void addRow(char letter, String morse);

    /**
     * Supprime une ligne de la table de conversion
     * 
     * @param letter lettre à supprimer
     * @param morse  code morse représentant la lettre à suprimer
     */
    public void removeRow(char letter, String morse);

    /**
     * Rentourne la lettre du code morse demandé
     * 
     * @param morse code morse à traduire
     * @return
     */
    public char getLetter(String morse);

    /**
     * Vide le contenu de cette table de conversion
     */
    public void clear();
}
