package exception;

/**
 * Levée lorsqu'une donnée saisie ne respecte pas les règles
 * Nom vide, date de fin antérieure à la date de début, âge négatif...
 */
public class DonneeInvalideExceptions extends SpaException {
    public DonneeInvalideExceptions(String message) {
        super("Donnée invalide : " + message);
    }
}