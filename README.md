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

La problématique principale est de gérer efficacement le cycle de vie d’un projet, depuis sa création jusqu’au paiement des phases, tout en garantissant la sécurité des accès selon les rôles utilisateurs .

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
  - frontend (optionnel)
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



---

## 🚀 Installation et exécution

### 🔹 Prérequis
- Docker
- Docker Compose
- Java 17, Maven, Node.js

---

### 🔹 Étapes d’installation


----
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

----

## 📦 Fonctionnalités principales

- Gestion des organismes
- Gestion des employés
- Gestion des projets
- Gestion des phases
- Affectation des employés
- Gestion des livrables
- Gestion des documents
- Gestion des factures

  ----

## 📊 Dashboard / Reporting

- Tableau de bord global
- Phases terminées non facturées
- Phases facturées non payées
- Phases payées
- Suivi des projets


  ## 📡 API Documentation


  ----
  
 ## 🧪 Tests Swagger


 ----

 ## 🐳 Conteneurisation

 - **Dockerfile** : backend
 - **docker-compose** : orchestration
   

  
