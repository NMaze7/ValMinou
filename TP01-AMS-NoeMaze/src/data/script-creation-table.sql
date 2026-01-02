DROP TABLE IF EXISTS Prescription_Soin CASCADE;
DROP TABLE IF EXISTS Modele_Semaine_Type CASCADE;
DROP TABLE IF EXISTS Participation_Animal_Activite CASCADE;
DROP TABLE IF EXISTS Planning_Activite CASCADE;
DROP TABLE IF EXISTS Creneau CASCADE;
DROP TABLE IF EXISTS Type_Activite CASCADE;
DROP TABLE IF EXISTS Historique_Emplacement CASCADE;
DROP TABLE IF EXISTS Animal CASCADE;
DROP TABLE IF EXISTS Benevole CASCADE;
DROP TABLE IF EXISTS Box CASCADE;
DROP TABLE IF EXISTS Famille CASCADE;

CREATE TABLE Famille (
                         id_famille SERIAL PRIMARY KEY,
                         nom VARCHAR(50) NOT NULL,
                         prenom VARCHAR(50) NOT NULL,
                         adresse TEXT,
                         telephone VARCHAR(15),
                         type_famille VARCHAR(20) NOT NULL
);

CREATE TABLE Box (
                     id_box SERIAL PRIMARY KEY,
                     nom_reference VARCHAR(100),
                     localisation VARCHAR(100),
                     type_animal VARCHAR(10) NOT NULL,
                     capacite_max INT NOT NULL
);

CREATE TABLE Benevole (
                          id_benevole SERIAL PRIMARY KEY,
                          nom VARCHAR(50) NOT NULL,
                          prenom VARCHAR(50) NOT NULL,
                          telephone VARCHAR(15),
                          adresse TEXT,
                          role VARCHAR(20) DEFAULT 'Benevole'
);

CREATE TABLE Animal (
                        id_animal SERIAL PRIMARY KEY,
                        nom VARCHAR(50) NOT NULL,
                        type_animal VARCHAR(10) NOT NULL,
                        num_identification VARCHAR(50) UNIQUE,
                        race VARCHAR(50),
                        annee_naissance INT,
                        date_arrivee DATE NOT NULL,
                        statut VARCHAR(20) DEFAULT 'En attente',
                        ok_humains BOOLEAN DEFAULT NULL,
                        ok_chiens  BOOLEAN  DEFAULT NULL,
                        ok_chats   BOOLEAN   DEFAULT NULL,
                        ok_bebes   BOOLEAN   DEFAULT NULL,
                        id_famille_origine INT,
                        CONSTRAINT fk_animal_origine FOREIGN KEY (id_famille_origine) REFERENCES Famille(id_famille)
);

CREATE TABLE Historique_Emplacement (
                                        id_historique SERIAL PRIMARY KEY,
                                        id_animal INT NOT NULL,
                                        type_emplacement VARCHAR(20) NOT NULL,
                                        id_box INT,
                                        id_famille INT,
                                        date_debut DATE NOT NULL,
                                        date_fin DATE,
                                        commentaire TEXT,
                                        CONSTRAINT fk_hist_animal FOREIGN KEY (id_animal) REFERENCES Animal(id_animal),
                                        CONSTRAINT fk_hist_box FOREIGN KEY (id_box) REFERENCES Box(id_box),
                                        CONSTRAINT fk_hist_famille FOREIGN KEY (id_famille) REFERENCES Famille(id_famille)
);

CREATE TABLE Type_Activite (
                               id_type_activite SERIAL PRIMARY KEY,
                               libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Creneau (
                         id_creneau SERIAL PRIMARY KEY,
                         date_creneau DATE NOT NULL,
                         heure_debut TIME NOT NULL,
                         heure_fin TIME NOT NULL,
                         nb_benevoles_min INT DEFAULT 1,
                         est_semaine_type BOOLEAN DEFAULT FALSE
);

CREATE TABLE Planning_Activite (
                                   id_planning SERIAL PRIMARY KEY,
                                   id_creneau INT NOT NULL,
                                   id_benevole INT,
                                   id_type_activite INT NOT NULL,
                                   CONSTRAINT fk_plan_creneau FOREIGN KEY (id_creneau) REFERENCES Creneau(id_creneau),
                                   CONSTRAINT fk_plan_benevole FOREIGN KEY (id_benevole) REFERENCES Benevole(id_benevole),
                                   CONSTRAINT fk_plan_activite FOREIGN KEY (id_type_activite) REFERENCES Type_Activite(id_type_activite)
);

CREATE TABLE Participation_Animal_Activite (
                                               id_planning INT,
                                               id_animal INT,
                                               PRIMARY KEY (id_planning, id_animal),
                                               CONSTRAINT fk_part_planning FOREIGN KEY (id_planning) REFERENCES Planning_Activite(id_planning),
                                               CONSTRAINT fk_part_animal FOREIGN KEY (id_animal) REFERENCES Animal(id_animal)
);

CREATE TABLE Modele_Semaine_Type (
                                     id_modele SERIAL PRIMARY KEY,
                                     jour_semaine VARCHAR(15) NOT NULL,
                                     heure_debut TIME NOT NULL,
                                     heure_fin TIME NOT NULL,
                                     nb_benevoles_min_defaut INT DEFAULT 1,
                                     id_type_activite_defaut INT,
                                     CONSTRAINT fk_modele_activite FOREIGN KEY (id_type_activite_defaut) REFERENCES Type_Activite(id_type_activite)
);

CREATE TABLE Prescription_Soin (
                                   id_soin SERIAL PRIMARY KEY,
                                   id_animal INT NOT NULL,
                                   description_soin TEXT NOT NULL,
                                   frequence VARCHAR(100),
                                   date_fin_traitement DATE,
                                   CONSTRAINT fk_soin_animal FOREIGN KEY (id_animal) REFERENCES Animal(id_animal)
);