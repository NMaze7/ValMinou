package exception;

/**
 * Levée lorsqu'on tente d'accéder à un élément (Animal, Box...) qui n'existe pas
 */
public class ElementIntrouvableException extends SpaException {
    public ElementIntrouvableException(String message) {
        super("Élément introuvable : " + message);
    }



    public ElementIntrouvableException(String typeEntite, int id) {
        super("Impossible de trouver " + typeEntite + " avec l'ID " + id);
    }
}