-- Create User
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'canal_user') THEN
CREATE ROLE canal_user WITH LOGIN PASSWORD 'canal123';
END IF;
END
$$;

-- Création DB
CREATE DATABASE canal_db OWNER canal_user;

-- Donne tout les privilèges
GRANT ALL PRIVILEGES ON DATABASE canal_db TO canal_user;

-- A faire dans après s'être connecté avec canal_user

CREATE TABLE canal (
                       PRIMARY KEY (id),
                       id               SERIAL,
                       nom              VARCHAR(100) NOT NULL,
                       isPublic         BOOLEAN NOT NULL,
                       dateCreation     TIMESTAMP,
                       dateModification TIMESTAMP,
                       idUtilisateur    INT NOT NULL
);

CREATE TABLE message (
                         PRIMARY KEY (id),
                         id               SERIAL,
                         contenu          VARCHAR(500),
                         dateEnvoi        TIMESTAMP,
                         dateModification TIMESTAMP,
                         idCanal          INT NOT NULL,
                         idUtilisateur    INT NOT NULL
);

CREATE TABLE participe (
                           PRIMARY KEY (idCanal, idUtilisateur),
                           idCanal       INT NOT NULL,
                           idUtilisateur INT NOT NULL
);

CREATE TABLE utilisateur (
                             PRIMARY KEY (id),
                             id                SERIAL,
                             nom               VARCHAR(100) NOT NULL,
                             mdp               BYTEA NOT NULL,
                             dateInscription   TIMESTAMP,
                             derniereConnexion TIMESTAMP
);

ALTER TABLE canal ADD FOREIGN KEY (idUtilisateur) REFERENCES utilisateur (id);

ALTER TABLE message ADD FOREIGN KEY (idUtilisateur) REFERENCES utilisateur (id);
ALTER TABLE message ADD FOREIGN KEY (idCanal) REFERENCES canal (id);

ALTER TABLE participe ADD FOREIGN KEY (idUtilisateur) REFERENCES utilisateur (id);
ALTER TABLE participe ADD FOREIGN KEY (idCanal) REFERENCES canal (id);