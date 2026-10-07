# 🚗 Auto-École Management System

Application web complète de gestion d'auto-école développée avec **Spring Boot 4.1.1** et **MySQL 8.3**.

## 📋 Description

Ce projet gère l'ensemble de l'activité administrative et pédagogique d'une auto-école :

- Inscription des élèves (apprenants)
- Gestion du planning des cours théoriques (CODE) et pratiques (CONDUITE)
- Suivi des moniteurs et de leur disponibilité
- Affectation des véhicules de la flotte
- Suivi des paiements des frais de formation
- Génération de reçus imprimables

## 🛠️ Technologies utilisées

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Data JPA | inclus |
| Hibernate | 7.4.5 |
| MySQL | 8.3 (via WAMP) |
| Maven | Wrapper |
| Lombok | inclus |
| Thymeleaf | inclus |
| Spring Security | 6 |
| Bootstrap | 5.3.2 |
| Chart.js | 4.4.0 |


## 🏗️ Architecture

Le projet suit une architecture en couches :

Controller → Service → Repository → Entity → MySQL
                ↑
            DTO + Mapper

### Structure des packages

com.autoecole/
├── AutoEcoleApplication.java    # Point d'entrée
├── config/                      # CORS, Security, DataInitializer
├── controller/                  # API REST + Web (HTML)
├── dto/                         # Data Transfer Objects
├── entity/                      # Entités JPA
├── exception/                   # Gestion d'erreurs
├── mapper/                      # Conversion Entité ↔ DTO
├── repository/                  # Accès aux données
└── service/                     # Logique métier

## 🗄️ Modèle de données

6 entités principales :

1. **Eleve** — apprenant (avec corbeille)
2. **Moniteur** — professeur (avec corbeille)
3. **Vehicule** — voiture de la flotte (avec corbeille)
4. **SeanceCours** — séance de CODE ou CONDUITE (avec corbeille)
5. **Paiement** — règlement de l'élève (avec corbeille)
6. **Utilisateur** — compte admin (login)


## 🚀 Installation et exécution

### Prérequis

- **Java 17 ou 21** (recommandé : 21)
- **WAMP** (avec MySQL 8.x démarré, icône verte)
- **Maven** (via le wrapper `mvnw.cmd`)

### 1. Créer la base de données

Dans phpMyAdmin (`http://localhost/phpmyadmin`), onglet SQL :

```sql
CREATE DATABASE auto_ecole_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;


## 👨‍💻 Auteur

**Nom :** Bahizire Benjamin
**Projet :** Examen Spring Boot — Application Web de Gestion d'Auto-École
**Date :** Octobre 2026