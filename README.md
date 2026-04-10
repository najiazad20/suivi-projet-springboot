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
- API REST
- Architecture en couches (Controller → Service → Repository → Entity)
- Gestion métier centralisée
- Validation et gestion des exceptions

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

<img width="313" height="425" alt="image" src="https://github.com/user-attachments/assets/fb9a2400-2963-4073-ab7d-b1734c1bfa35" />
