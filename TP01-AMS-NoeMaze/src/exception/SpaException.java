package exception;

/**
 * Exception parente de toutes les exceptions métier de l'SPA
 * Elle est abstraite pour forcer l'utilisation d'exceptions plus précises
 */
public abstract class SpaException extends Exception {
    public SpaException(String message) {
        super(message);
    }
}