# Glorb26

Projet réalisé dans le cadre du cours de POO (Programmation Orientée Objet) et de Conduite de Projet (L2 Informatique, UPC).

Glorb26 est un jeu d'artillerie 2D au tour par tour inspiré de Worms. Des équipes d'Aliens s'affrontent sur un terrain destructible, en utilisant un arsenal varié, et soumis à une simulation physique (gravité, collisions, etc).

## Fonctionnalités du jeu

* **Terrain destructible :** Sol modifiable par les explosions et présence d'un plan d'eau mortel en bas de carte.
* **Système de jeu :** Combat par équipes, gestion du tour par tour et des points de vie.
* **Physique :** Gestion de la gravité pour les entités et trajectoires balistiques pour les projectiles.
* **Arsenal :** 
  * Armes explosives (Bazooka, Grenade)
  * Armes à feu (Uzi, Shotgun)
  * Outils tactiques (Mines, Jetpack, etc)
* **Double Affichage :** Interface graphique (Swing) et interface console.

## Installation et Compilation

Le projet utilise **Maven** pour la gestion des dépendances et la compilation.

### 1 - A partir du code source du jeu
Après avoir téléchargé le code source :

1. **Compiler le projet :**
   ```bash
   mvn clean package

2. **Lancer le jeu (Mode GUI) :**
    ```bash
   java -cp target/Glorb26-1.0.jar
   
3. **Lancer le jeu (Mode Console) :**
    ```bash
   java -cp target/Glorb26-1.0.jar upc.MainConsole

### 2 - Avec le fichier exécutable (GUI seulement)
1. **Se rendre sur la page des [releases](https://moule.informatique.univ-paris-diderot.fr/daimi/worms/-/releases)**

2. **Cliquer sur le lien "Télécharger le jeu"**

3. **Se rendre sur le répertoire du fichier .jar téléchargé et exécuter**
   ```bash
   java -jar Glorb26-1.0.jar

## Structure globale du code

Le projet respecte l'architecture **MVC** (Modèle-Vue-Contrôleur) :

* **Modèle (`upc.model`) :** Gestion de l'état du jeu, de la physique (`PhysicsEngine`) et de la logique des entités (Aliens, Projectiles).
* **Vue (`upc.view`) :** Rendu graphique Swing (`MainFrame`) et rendu textuel (`ConsoleView`).
* **Contrôleur (`upc.controller`) :** Gestion des entrées utilisateur (clavier/souris) et cycle de jeu.
* **Assets (`upc.assets`) :** Gestion des ressources (SFX, animations, images...).

## Qualité du Code et Tests
* Tests Unitaires : Les composants critiques (Physique, Inventaire, Équipes) sont testés via JUnit.


* Style : Respect des conventions de codage via checkstyle.xml.


* CI/CD : Intégration continue configurée via .gitlab-ci.yml pour l'automatisation des tests et la vérification du build.


## Equipe
- Ilyess Daimi
- Karim Ait Lahcen
- Lucas Alves Salgado
- Nael Awada
- Ruben Foalem
- Ata Allah El May