-- ============================================================
--  test_data.sql
--  Jeu de données de test (mdp hashés en SHA-256)
--
--  Mots de passe en clair :
--    alice   -> password123
--    bob     -> password123
--    charlie -> password123
--    diana   -> password123
-- ============================================================

-- Connexion à la bonne base avant d'exécuter :
-- \c canal_db canal_user

-- ============================================================
--  Utilisateurs
-- ============================================================
INSERT INTO utilisateur (nom, mdp, dateInscription, derniereConnexion) VALUES
                                                                           (
                                                                               'alice',
                                                                               -- password123
                                                                               decode('ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'hex'),
                                                                               '2024-01-10 08:30:00',
                                                                               '2024-03-15 14:22:00'
                                                                           ),
                                                                           (
                                                                               'bob',
                                                                               -- letmein456
                                                                               decode('ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'hex'),
                                                                               '2024-01-15 09:00:00',
                                                                               '2024-03-14 10:05:00'
                                                                           ),
                                                                           (
                                                                               'charlie',
                                                                               -- qwerty789
                                                                               decode('ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'hex'),
                                                                               '2024-02-01 11:15:00',
                                                                               '2024-03-13 16:40:00'
                                                                           ),
                                                                           (
                                                                               'diana',
                                                                               -- secret000
                                                                               decode('ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'hex'),
                                                                               '2024-02-20 13:45:00',
                                                                               '2024-03-10 09:30:00'
                                                                           );

-- ============================================================
--  Canaux
-- ============================================================
INSERT INTO canal (nom, isPublic, dateCreation, dateModification, nomCreateur) VALUES
                                                                                   ('général',        true,  '2024-01-10 09:00:00', '2024-01-10 09:00:00', 'alice'),
                                                                                   ('blague-du-jour', true,  '2024-01-15 10:00:00', '2024-02-01 08:00:00', 'bob'),
                                                                                   ('projet-secret',  false, '2024-02-01 12:00:00', '2024-02-01 12:00:00', 'charlie'),
                                                                                   ('random',         true,  '2024-02-20 14:00:00', '2024-03-01 11:00:00', 'alice');

-- ============================================================
--  Participations
-- ============================================================
INSERT INTO participe (idCanal, nomUtilisateur) VALUES
                                                    (1, 'alice'),   -- alice   dans général
                                                    (1, 'bob'),     -- bob     dans général
                                                    (1, 'charlie'), -- charlie dans général
                                                    (1, 'diana'),   -- diana   dans général
                                                    (2, 'bob'),     -- bob     dans blague-du-jour
                                                    (2, 'charlie'), -- charlie dans blague-du-jour
                                                    (3, 'alice'),   -- alice   dans projet-secret
                                                    (3, 'charlie'), -- charlie dans projet-secret
                                                    (4, 'alice'),   -- alice   dans random
                                                    (4, 'diana');   -- diana   dans random

-- ============================================================
--  Messages
-- ============================================================
INSERT INTO message (contenu, dateEnvoi, dateModification, idCanal, nomAuteur) VALUES
                                                                                   ('Bonjour tout le monde !',                                '2024-03-01 08:00:00', '2024-03-01 08:00:00', 1, 'alice'),
                                                                                   ('Salut Alice !',                                          '2024-03-01 08:05:00', '2024-03-01 08:05:00', 1, 'bob'),
                                                                                   ('Quoi de neuf ?',                                         '2024-03-01 08:10:00', '2024-03-01 08:10:00', 1, 'charlie'),
                                                                                   ('Pas grand chose, et vous ?',                             '2024-03-01 08:15:00', '2024-03-01 08:15:00', 1, 'diana'),
                                                                                   ('Pourquoi le vélo tombe ? Parce que c''est deux roues !', '2024-03-10 09:00:00', '2024-03-10 09:00:00', 2, 'bob'),
                                                                                   ('Haha excellente celle-là',                               '2024-03-10 09:10:00', '2024-03-10 09:10:00', 2, 'charlie'),
                                                                                   ('Réunion à 14h pour le projet ?',                         '2024-03-12 10:00:00', '2024-03-12 10:00:00', 3, 'alice'),
                                                                                   ('OK pour moi !',                                          '2024-03-12 10:05:00', '2024-03-12 10:05:00', 3, 'charlie'),
                                                                                   ('Quelqu''un a vu le dernier film sorti ?',               '2024-03-14 17:00:00', '2024-03-14 17:00:00', 4, 'alice'),
                                                                                   ('Oui, j''ai adoré !',                                     '2024-03-14 17:30:00', '2024-03-14 17:30:00', 4, 'diana');