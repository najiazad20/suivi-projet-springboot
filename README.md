# 📊 Suivi Projet

## 📌 Nom du Projet
**Système de Suivi de Projets**

---

## 📖 Description

Ce projet consiste en la conception et le développement d’une application web de gestion et de suivi de projets destinée à une entreprise de services.

L’objectif est de centraliser les données relatives aux projets afin d’assurer :
- une meilleure visibilité sur l’avancement
- une coordination efficace entre les intervenants
- un contrôle rigoureux des opérations métiers et financières

La problématique principale est de gérer efficacement le cycle de vie d’un projet, depuis sa création jusqu’au paiement des phases, tout en garantissant la sécurité des accès selon les rôles des utilisateurs .

---

## 🎯 Objectifs

- Centraliser la gestion des projets et des ressources
- Assurer le suivi des phases (réalisation, facturation, paiement)
- Gérer les affectations des employés aux projets
- Implémenter un système de sécurité basé sur les rôles
- Fournir un tableau de bord et des outils de reporting
- Conteneuriser l’application avec Docker

---

## 🏗️ Architecture du projet

### 🔹 Backend (Spring Boot)

Le backend est basé sur une architecture **modulaire et en couches**.  
Il est structuré par domaines métier (`organisation`, `projet`, `facturation`, `reporting`, `security`) et chaque module contient des packages tels que `controllers`, `services`, `repositories`, `entities`, `dto` et `mappers`.

Cette organisation permet de séparer clairement :
- l’exposition des API REST
- la logique métier
- l’accès aux données
- la sécurité JWT
- la gestion centralisée des exceptions

### 🔹 Frontend (React)
- Application SPA (Single Page Application)
- Gestion du routing avec React Router
- Appels API via Axios
- Gestion d’état (Context API / Redux)

### 🔹 Base de données
- MySQL 8
- Modélisation relationnelle avec JPA / Hibernate
- Gestion des relations entre entités

### 🔹 Infrastructure (Docker)
- Conteneurisation complète de l’application
- Services :
  - backend
  - base de données
  - frontend 
- Communication via réseau Docker interne 

---

## 🧩 Schéma de l’architecture

<img width="958" height="704" alt="Capture d&#39;écran 2026-04-09 181148" src="https://github.com/user-attachments/assets/c48f06d9-eeab-4718-a613-e587721f846e" />



---

## ⚙️ Technologies utilisées

### 🔹 Backend
- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security (JWT)
- Swagger / OpenAPI
- Maven

### 🔹 Frontend
- React
- Axios
- React Router
- Tailwind / Bootstrap

### 🔹 Base de données
- MySQL 8

### 🔹 DevOps
- Docker
- Docker Compose

---

## 📁 Structure du projet

### Backend


<img width="313" height="425" alt="image" src="https://github.com/user-attachments/assets/65108e18-f361-40e4-87ae-16b3d1aa4daf" />


### Frontend
<img width="332" height="562" alt="image" src="https://github.com/user-attachments/assets/b92aeb8b-57a8-4178-b549-6d676f283600" />



---

## 🚀 Installation et exécution

### 🔹 Prérequis
- Docker
- Docker Compose
- Java 17, Maven, Node.js

---

### 🔹 Étapes d’installation

<img width="813" height="211" alt="image" src="https://github.com/user-attachments/assets/e5963319-6b3f-4cce-88a1-97b81e1a8db9" />
---

## 🐳 Lancer avec Docker

```bash
docker-compose up --build

```

---

## 💻 Lancer sans Docker

### 🔹 Backend

```bash
cd backend
mvn spring-boot:run
```
### 🔹 Frontend

```bash
cd frontend
npm install
npm start

```
---

## 🔐 Sécurité

**Type d’authentification** : JWT (JSON Web Token)

**Gestion des rôles** :

- Administrateur
- Secrétaire
- Chef de projet
- Comptable
- Directeur

  
**Protection des routes** :
  
**Backend** : Spring Security

**Frontend** : PrivateRoute / RoleRoute

---

## 📦 Fonctionnalités principales

- Gestion des organismes
- Gestion des employés
- Gestion des projets
- Gestion des phases
- Affectation des employés
- Gestion des livrables
- Gestion des documents
- Gestion des factures

---

## 📊 Dashboard / Reporting

- Tableau de bord global
- Phases terminées non facturées
- Phases facturées non payées
- Phases payées
- Suivi des projets


## 📡 API Documentation
## http://localhost:8080/v3/api-docs

---
## 🎥 Demonstration video :
  https://github.com/user-attachments/assets/15a7eef2-def9-4c6f-a9a7-328f511a11b1

## 🎥 Vidéos de démonstration par rôle

### 👑 Administrateur


### 📊 Chef de Projet





   ## 🧪 Tests Swagger


   
   ### 🛡️Tests de Succès (Validation Fonctionnelle)
   #### *Login:
<img width="1825" height="941" alt="image" src="https://github.com/user-attachments/assets/31551547-802f-44c5-ab41-261b47b62230" />

#### * Extraction des phases facturées non payées
![WhatsApp Image 2026-04-10 at 18 01 37](https://github.com/user-attachments/assets/1a7115cb-3a36-4741-ac60-048cd0b26055)

#### * Validation d'un paiement
![WhatsApp Image 2026-04-10 at 18 01 00](https://github.com/user-attachments/assets/4044b75c-b374-45e6-b673-4ead06349c3e)


#### * Recherche d'employés disponibles
![WhatsApp Image 2026-04-10 at 18 02 58](https://github.com/user-attachments/assets/6e2d33ac-6a67-444c-8ed9-6d2fd875e339)

#### *Mise à jour de la réalisation technique
![WhatsApp Image 2026-04-10 at 18 00 37](https://github.com/user-attachments/assets/1acd31f2-bc56-4e29-b0bd-e6cb6f38e322)




#### * Consultation du profil utilisateur (/me)

![WhatsApp Image 2026-04-10 at 17 57 20](https://github.com/user-attachments/assets/3bf491f4-42e2-47fe-b34d-f55295c691ae)

  ### 🛠️ Tests de Robustesse (Gestion des Erreurs)
#### *Erreur - Chevauchement de dates (Affectation)
![WhatsApp Image 2026-04-10 at 17 59 45](https://github.com/user-attachments/assets/935ea4a5-4703-4dc9-bd22-0d5c54c109c8)

#### *Erreur - Incohérence des dates du Projet
![WhatsApp Image 2026-04-10 at 17 58 43](https://github.com/user-attachments/assets/85088752-9db8-4231-99eb-38ad20a72908)

#### *Erreur - Dépassement budgétaire (Phases)
![WhatsApp Image 2026-04-10 at 17 58 54](https://github.com/user-attachments/assets/8fe8fb8f-a90f-48f1-87d8-f7252e02a4dc)


#### *Erreur - Phase déjà facturée

![WhatsApp Image 2026-04-10 at 17 59 15](https://github.com/user-attachments/assets/953e9413-706c-451f-929b-38e405ec9c1c)

#### *Erreur - Organisme introuvable
![WhatsApp Image 2026-04-10 at 17 56 30](https://github.com/user-attachments/assets/5eaa652d-ee6d-404f-9e45-2e9aa17b4f77)


#### *Erreur - Livrable inexistant

![WhatsApp Image 2026-04-10 at 17 59 57](https://github.com/user-attachments/assets/b050dc76-bfb6-4d97-ad68-d8b9b5b3b337)
#### *Suppression d'un organisme inexistant
![WhatsApp Image 2026-04-10 at 18 14 08](https://github.com/user-attachments/assets/ef90c4f6-f423-4743-a96c-e505ef6393ad)

#### *Violation d'unicité du Matricule
![WhatsApp Image 2026-04-10 at 16 32 07](https://github.com/user-attachments/assets/e61bc99e-749e-40ce-8323-776c86a558c2)

#### *Login erroné
![WhatsApp Image 2026-04-10 at 18 14 09](https://github.com/user-attachments/assets/372788e2-c7d1-4230-b3c1-bdbe85df2e86)





---

 ## 🐳 Conteneurisation

 - **Dockerfile** : backend
 - **docker-compose** : orchestration
   
---

## 👤 Auteur

 👨‍💻**Ait Larbi Ben Hachmi Oumaima**     👨‍💻 **Aziz Khadija**     👨‍💻 **Zad Najia**
