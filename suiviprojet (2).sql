-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 09 avr. 2026 à 20:09
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `suiviprojet`
--

-- --------------------------------------------------------

--
-- Structure de la table `document`
--

CREATE TABLE `document` (
  `id` int(11) NOT NULL,
  `chemin` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `projet_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `document`
--

INSERT INTO `document` (`id`, `chemin`, `code`, `description`, `libelle`, `projet_id`) VALUES
(1, '/uploads/projets/1/cahier_des_charges_v2.pdf', 'DOC-2026-CDC', 'Document validé par le client définissant l\'ensemble des fonctionnalités attendues.', 'Cahier des Charges Fonctionnel', 3),
(2, '/uploads/projets/1/final_signed_cdc.pdf', 'DOC-2026-CDC-V2', 'Ce document contient les signatures du client et clôture la phase de spécifications.', 'Cahier des Charges - VERSION FINALE VALIDÉE', 4);

-- --------------------------------------------------------

--
-- Structure de la table `employe`
--

CREATE TABLE `employe` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `matricule` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `profil_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `employe`
--

INSERT INTO `employe` (`id`, `email`, `login`, `matricule`, `nom`, `password`, `prenom`, `telephone`, `profil_id`) VALUES
(16, 'najiazad@example.com', 'najia', 'ABCDEFG', 'Zad', '$2a$10$7SULBYD9po2IFrQysUiykubkcd/NgDj2OhKzJ2PKRhWLYLwUIRC3a', 'Najia', '0686754354', 7),
(18, 'khadijaaziz670@gmail.com', 'aziz', 'ABC', 'KHADIJA', '$2a$10$80bDPR20szKth7AJkCSwo.YT0krABFi.MaUCi.ZNjIoZCUcqJ1nn2', 'AZIZ', '0698765432', 5),
(19, 'KHADIJAaziz@example.com', 'oumaima', 'ABCDEFGH', 'AZIZ', '$2a$10$yTbsQmgbzWkw7TvNEs7ny.BJ3qdWxQqJRSQd4l/iwgipMfO3Fiqim', 'KHADIJA', '0678543212', 6),
(20, 'k.aziz2178@uca.ac.ma', 'kaoutar', 'ABCDEFGH-12', 'Aziz', '$2a$10$dDD2qR9lNJuGZJMgH9f6k.26Lv.KMbDVChC1D7O6hK53TMQXTJrg2', 'kaoutar', '0678543219', 7),
(21, 'oumaimaaziz@gmail.com', 'oum', 'ABCDEFGH-123', 'Aziz', '$2a$10$lGeOWoeTKDHDMgohrwGWCudgje3A114cuSD7jKctvvQUoM9lHsHTq', 'oumaima', '0678543219', 8),
(22, 'oumaimaait@gmail.com', 'oumaimalarbi', 'ABCDEFGH-1234', 'aitlarbibenhachmi', '$2a$10$LVjMiDkuBCp0b83PFqlPFe34HkZ/EFLoWuxkWs19jxlVVUYH8daj6', 'oumaima', '0678543210', 9);

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

CREATE TABLE `facture` (
  `id` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `date_facture` date DEFAULT NULL,
  `phase_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `facture`
--

INSERT INTO `facture` (`id`, `code`, `date_facture`, `phase_id`) VALUES
(1, 'FACT-2026-0042', '2026-06-01', 1),
(2, 'FACTA-2026-0030', '2026-06-12', 2),
(3, 'FACTA-2026-2435', '2026-06-12', 6),
(4, 'FACTA-2026-243', '2026-02-09', 7);

-- --------------------------------------------------------

--
-- Structure de la table `ligne_employe_phase`
--

CREATE TABLE `ligne_employe_phase` (
  `id` int(11) NOT NULL,
  `date_debut` datetime(6) DEFAULT NULL,
  `date_fin` datetime(6) DEFAULT NULL,
  `employe_id` int(11) DEFAULT NULL,
  `phase_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `ligne_employe_phase`
--

INSERT INTO `ligne_employe_phase` (`id`, `date_debut`, `date_fin`, `employe_id`, `phase_id`) VALUES
(1, '2026-05-16 00:00:00.000000', '2026-06-14 00:00:00.000000', 16, 1),
(2, '2025-11-02 00:00:00.000000', '2026-03-30 00:00:00.000000', 20, 7),
(3, '2026-06-09 00:00:00.000000', '2026-07-10 00:00:00.000000', 18, 6);

-- --------------------------------------------------------

--
-- Structure de la table `livrable`
--

CREATE TABLE `livrable` (
  `id` int(11) NOT NULL,
  `chemin` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `phase_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `livrable`
--

INSERT INTO `livrable` (`id`, `chemin`, `code`, `description`, `libelle`, `phase_id`) VALUES
(1, '/uploads/projets/1/phases/2/architecture_v1.pdf', 'LIV-2026-ARCH-01', 'Document complet détaillant l\'architecture micro-services et le schéma de base de données.', 'Dossier d\'Architecture Technique', 2),
(2, 'https://mon-cloud-entreprise.com/share/projet1/archi_finale.pdf', 'LIV-2026-ARCH-01', 'Version validée après revue technique du 15/05/2026.', 'Dossier d\'Architecture Technique - VERSION FINALE', 1);

-- --------------------------------------------------------

--
-- Structure de la table `organisme`
--

CREATE TABLE `organisme` (
  `id` int(11) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `email_contact` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `nom_contact` varchar(255) DEFAULT NULL,
  `site_web` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `organisme`
--

INSERT INTO `organisme` (`id`, `adresse`, `code`, `email_contact`, `nom`, `nom_contact`, `site_web`, `telephone`) VALUES
(1, '39 Quai du Président Roosevelt, 92130 Issy-les-Moulineaux', 'ORG-2024-MCF', 'j.dupont@microsoft.com', 'Microsoft France', 'Jean Dupont', 'https://www.microsoft.com/fr-fr', '01 55 69 55 69'),
(2, '26-50 Avenue du Professeur André Lemierre, 75020 Paris', 'CNAM-TS-75', 'contact.rh@assurance-maladie.fr', 'Caisse Nationale de l\'Assurance Maladie', 'Marie Lefebvre', 'https://www.ameli.fr', '08 11 70 36 46');

-- --------------------------------------------------------

--
-- Structure de la table `phase`
--

CREATE TABLE `phase` (
  `id` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `etat_facturation` bit(1) NOT NULL,
  `etat_paiement` bit(1) NOT NULL,
  `etat_realisation` bit(1) NOT NULL,
  `libelle` varchar(255) DEFAULT NULL,
  `montant` double NOT NULL,
  `projet_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `phase`
--

INSERT INTO `phase` (`id`, `code`, `date_debut`, `date_fin`, `description`, `etat_facturation`, `etat_paiement`, `etat_realisation`, `libelle`, `montant`, `projet_id`) VALUES
(1, 'PH-2026-DEV-01', '2026-05-15', '2026-06-15', 'Réalisation des Services, Repositories et intégration de la sécurité JWT.', b'1', b'1', b'1', 'Développement des modules Backend', 5000, 3),
(2, 'PH-2026-FINAL', '2026-06-02', '2026-07-12', 'Tests de non-régression finaux, formation des utilisateurs et livraison du rapport d\'audit.', b'1', b'1', b'1', 'Recette technique et Clôture', 7500, 4),
(4, 'PH-DEV-01', '2026-05-16', '2026-06-12', 'Mise en place de l\'API REST et de la base de données PostgreSQL.', b'0', b'0', b'1', 'Développement du Backend', 15000.5, 3),
(6, 'PH-2026-01-ANALYSIS', '2026-06-08', '2026-07-12', 'Rédaction du cahier des charges fonctionnel et technique avec le client.', b'1', b'0', b'1', 'Analyse des besoins et Spécifications', 5000, 4),
(7, 'PH-2026-LIV-FINALE', '2025-11-01', '2026-04-01', 'Déploiement final sur les serveurs de production et transfert de compétences.', b'1', b'1', b'1', 'Livraison et Mise en Production', 25000, 5);

-- --------------------------------------------------------

--
-- Structure de la table `profil`
--

CREATE TABLE `profil` (
  `id` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `libelle` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `profil`
--

INSERT INTO `profil` (`id`, `code`, `libelle`) VALUES
(5, 'administrateur', 'ADMINISTRATEUR'),
(6, 'directeur', 'DIRECTEUR'),
(7, 'chef de projet', 'CHEF_PROJET'),
(8, 'comptable', 'COMPTABLE'),
(9, 'secretaire', 'SECRETAIRE');

-- --------------------------------------------------------

--
-- Structure de la table `projet`
--

CREATE TABLE `projet` (
  `id` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `montant` double NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `chef_projet_id` int(11) DEFAULT NULL,
  `organisme_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `projet`
--

INSERT INTO `projet` (`id`, `code`, `date_debut`, `date_fin`, `description`, `montant`, `nom`, `chef_projet_id`, `organisme_id`) VALUES
(3, 'PRJ-2026-001', '2026-05-01', '2026-12-31', 'Déploiement de la fibre optique dans le bâtiment principal', 75000, 'Installation Réseau Fibre', 16, 1),
(4, 'DEV-APP-2026', '2026-06-01', '2026-11-30', 'Développement d\'une app iOS/Android pour l\'inventaire en temps réel', 45000.5, 'Application Mobile Gestion Stock', 20, 2),
(5, 'PRJ-2026-BCP-001', '2025-10-01', '2026-04-05', 'Projet achevé. Toutes les phases ont été livrées, facturées et payées. Recette client signée le 05/04/2026.', 450000, 'Portail Client BCP v2 - CLÔTURÉ', 20, 2);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgy6n2e7cn5h13qxlxk3kw1ysd` (`projet_id`);

--
-- Index pour la table `employe`
--
ALTER TABLE `employe`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKt5g5av1xex3c9ccwemgrdc1xl` (`profil_id`);

--
-- Index pour la table `facture`
--
ALTER TABLE `facture`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKtin7aye6apcxobiw3l23a4u79` (`phase_id`);

--
-- Index pour la table `ligne_employe_phase`
--
ALTER TABLE `ligne_employe_phase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkrlsvfd7umx68hns21slee7xk` (`employe_id`),
  ADD KEY `FKmlg7ic0ym6i9j6n8m1jst0mjf` (`phase_id`);

--
-- Index pour la table `livrable`
--
ALTER TABLE `livrable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKq1q6trltxoga0bgbwemdrw8rs` (`phase_id`);

--
-- Index pour la table `organisme`
--
ALTER TABLE `organisme`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `phase`
--
ALTER TABLE `phase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKg3uyainpis0ceqongbqjum6cx` (`projet_id`);

--
-- Index pour la table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `projet`
--
ALTER TABLE `projet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9i33a7h88adj6v2idj723nbuo` (`chef_projet_id`),
  ADD KEY `FKficbnknfp5biu5qad3y7i0262` (`organisme_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `document`
--
ALTER TABLE `document`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `employe`
--
ALTER TABLE `employe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `facture`
--
ALTER TABLE `facture`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `ligne_employe_phase`
--
ALTER TABLE `ligne_employe_phase`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `livrable`
--
ALTER TABLE `livrable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `organisme`
--
ALTER TABLE `organisme`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `phase`
--
ALTER TABLE `phase`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `profil`
--
ALTER TABLE `profil`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `projet`
--
ALTER TABLE `projet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `document`
--
ALTER TABLE `document`
  ADD CONSTRAINT `FKgy6n2e7cn5h13qxlxk3kw1ysd` FOREIGN KEY (`projet_id`) REFERENCES `projet` (`id`);

--
-- Contraintes pour la table `employe`
--
ALTER TABLE `employe`
  ADD CONSTRAINT `FKt5g5av1xex3c9ccwemgrdc1xl` FOREIGN KEY (`profil_id`) REFERENCES `profil` (`id`);

--
-- Contraintes pour la table `facture`
--
ALTER TABLE `facture`
  ADD CONSTRAINT `FKn90dvbqwgusj3845i6p92x04t` FOREIGN KEY (`phase_id`) REFERENCES `phase` (`id`);

--
-- Contraintes pour la table `ligne_employe_phase`
--
ALTER TABLE `ligne_employe_phase`
  ADD CONSTRAINT `FKkrlsvfd7umx68hns21slee7xk` FOREIGN KEY (`employe_id`) REFERENCES `employe` (`id`),
  ADD CONSTRAINT `FKmlg7ic0ym6i9j6n8m1jst0mjf` FOREIGN KEY (`phase_id`) REFERENCES `phase` (`id`);

--
-- Contraintes pour la table `livrable`
--
ALTER TABLE `livrable`
  ADD CONSTRAINT `FKq1q6trltxoga0bgbwemdrw8rs` FOREIGN KEY (`phase_id`) REFERENCES `phase` (`id`);

--
-- Contraintes pour la table `phase`
--
ALTER TABLE `phase`
  ADD CONSTRAINT `FKg3uyainpis0ceqongbqjum6cx` FOREIGN KEY (`projet_id`) REFERENCES `projet` (`id`);

--
-- Contraintes pour la table `projet`
--
ALTER TABLE `projet`
  ADD CONSTRAINT `FK9i33a7h88adj6v2idj723nbuo` FOREIGN KEY (`chef_projet_id`) REFERENCES `employe` (`id`),
  ADD CONSTRAINT `FKficbnknfp5biu5qad3y7i0262` FOREIGN KEY (`organisme_id`) REFERENCES `organisme` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
