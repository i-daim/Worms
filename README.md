***
## Projet jeu Worms 

## Description
Projet universitaire réalisé dans le cadre du cours de Programmation Orientée Objet et le cours de Conduite de Projet.  
Ce projet consiste à développer un jeu inspiré de Worms, où plusieurs joueurs contrôlent des vers armés et s'affrontent tour à tour dans un environnement destructible.
Un jeu de stratégie en 2D développé en Java, combinant tir, physique et tour par tour.

## Fonctionnalités principales
- Tour par tout entre plusieurs vers/joueurs
- Gestion de la gravité et des collisions
- Terrain destructible et des collisions
- Lancement de projectiles(ex : grenades, roquettes,etc.)
- Interface graphique
- Score, point de vie, tours de jeu

## Architecture du projet
- Entite/ Worm : représente un joueur/vers
- Monde : représente le terrain de jeu
- Partie : représente la boucle principale du jeu
- VueConsole : Interface textuelle du jeu 
- EcranAccueil : affichage de l'écran d'accueil
- EcranInventaire : affichage de l'écran de l'inventaire
- EcranJeu : affichage du jeu
- EcranScore : affchage du score 

## Installation
- Première étape : Ouvrir le terminal et exécutez

    git clone https://moule.informatique.univ-paris-diderot.fr/daimi/worms.git
- Deuxième étape : Se déplacer dans le dossier du projet  

    cd worms
- Troisième étape : Compiler le projet avec Maven  
    mvn compile
- Quatrième étape : Lancer le jeu    
    mvn exec:java -Dexec.mainClass="main.Main"

## Utilisation
Après le projet compilé et lancé avec Maven :
Choisissez le nombre de joueurs et leurs pseudos.  
Chaque joueur contrôle un ver(worm) à tour de rôle.  
Les projectiles suivent une trajectoire influencée par la gravité.  
Vous pouvez :  
    - Vous déplacer avec les flèches directionnellles,  
    - viser avec la souris ou les touches dédiées,   
    - tirer avec la barre d'espace  
La partie continue jusqu'à ce qu'un joueur n'ait plus de points de vie.  
Le dernier ver encore en vie remporte la partie.

## Support
En cas de bug ou de question :  
Ouvrez une issue GitHub dans ce dépôt : https://moule.informatique.univ-paris-diderot.fr/daimi/worms/-/issues?sort=created_date&state=opened&first_page_size=100  
Ou contactez l'équipe à : .....

## Authors
Ilyess Daimi  
Karim Ait Lahcen  
Lucas Alves Salgado  
Nael Awada  
Ruben Foalem  
Ata Allah El May


## Project status
En cours de développement 