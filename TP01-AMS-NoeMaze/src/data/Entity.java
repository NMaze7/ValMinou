package data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Classe mère abstraite représentant une entité
 * Elle implémente IData et Serializable pour toutes les classes filles
 */
public abstract class Entity implements IData, Serializable {
    protected int id;
    protected String values;
    protected HashMap<String, fieldType> map;



    public Entity() {
        this.map = new HashMap<>();
        this.values = "";
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public HashMap<String, fieldType> getMap() {
        return map;
    }

    @Override
    public String getValues() {
        return values;
    }

    /**
     * Vérifie que l'objet Java correspond bien à la structure de la table SQL.
     */
    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        for (String key : map.keySet()) {
            // Si la colonne n'existe pas en base
            if (!tableStruct.containsKey(key)) return false;
            // Si le type de donnée ne correspond pas
            if (map.get(key) != tableStruct.get(key)) return false;
        }
        return true;
    }

    /**
     * Méthode abstraite : chaque classe fille (Animal, Box...) devra
     * définir elle-meme comment elle remplit sa structure (getStruct)
     */
    @Override
    public abstract void getStruct();

    public abstract String getPkName();
    public abstract String getUpdateClause();

}