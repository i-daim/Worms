# Changelog

Ce projet suit les directives du cours de Conduite de Projet (L2 Informatique).
Les versions suivent le format [SemVer](https://semver.org/).

## [1.0.0] - 2026-01-12
*Version Finale – Sprint 5 : Finalisation du Jeu*

### Added
- Bouton dédié pour la sauvegarde accessible directement en jeu.
- Remplacement du Jetpack par les "Wings" (permet le double saut et le vol).
- Animations avancées pour l'Alien (états AIR, LAND, FLY).
- Tests de performance et classes de tests unitaires.
- Documentation technique complète (DESIGN, View, Items, JavaDoc).

### Changed
- Changement des touches par défaut (Z pour sauter, Q/D pour se déplacer).
- Ajustement dynamique des dimensions de l'interface selon l'écran.
- Traduction et uniformisation des textes du jeu.
- Optimisation du lancement de la partie et de la gestion des équipes.

### Fixed
- Correction du bug lors du zoom de la caméra.
- Modification et équilibrage du système de combat.

---

## [0.4] - 2026-01-05
*Jeu avec arsenal complet – Sprint 4 : Complétion de l'arsenal, sauvegarde et maps*

### Added
- Système de sauvegarde complet et chargement de partie.
- 3 nouvelles maps avec environnements animés (Glace, Enfer, Poison).
- Sélection aléatoire de la map au lancement.
- Système de visée à la souris (clic gauche pour charger et tirer).
- Jauge de puissance de tir dynamique.
- Caméra intelligente qui suit les projectiles avec zoom fluide.
- Gestion avancée du son (polyphonie, bruits d'impacts, ambiance).
- Musique et animation de victoire en fin de partie.
- Menu paramètres complet (volume, choix musique).
- Animation de marche pour les Glorbs et Zlerps.

### Changed
- Retour à un affichage console natif (suppression de Lanterna) pour plus de stabilité.
- Refactoring majeur du contrôleur de jeu et gestion des entrées clavier.
- Optimisation du pipeline CI/CD (allègement de l'artefact).

### Fixed
- Correction des bugs physiques (vent, explosions, impacts de balles).
- Correction du focus de l'inventaire.

---

## [0.3] - 2025-12-15
*Sprint 3 : utilisation des armes, animations, inventaire, sauvegarde du jeu*

### Added
- Possibilité d’utiliser les armes en jeu.
- Inventaire fonctionnel pour les équipes (consultation et utilisation d’armes).
- Affichage simple de l’inventaire dans l’interface.
- Lien entre armes et projectiles (création des projectiles lors de l’utilisation d’une arme).
- Sauvegarde du jeu.
- Tests de la classe projectile.
- Fenêtre dédiée aux paramètres.
- Caméra centrée sur l’alien actif.
- Ajout des images d’animation pour les explosions.
- Ajout des debug et tests supplémentaires.

### Fixed
- Bug : la partie ne se termine pas lorsque tous les aliens d’une équipe meurent.
- Bug : les aliens ne meurent pas dans l’eau.
- Bug : collisions des worms pas assez précises.
- Bug : le tour ne se termine pas lorsque le worm actuel meurt.
- Bug : un alien doit pouvoir utiliser (et avoir) une arme active.
- Bug sur le saut qui ne marche pas toujours comme prévu.

---

## [0.2] - 2025-11-28
*Jeu avec vue graphique – Sprint 2 : vue graphique, maps et physiques du jeu*

### Added
- Lancement du jeu avec le menu graphique (nouvelle partie).
- Système de gravité intégrant les dégâts de chute.
- Affichage de la map avec fond.
- Affichage des aliens en temps réel.
- Intégration des animations (eau en mouvement).
- Interface de jeu : timer du tour, barres de vie, indicateur de l’alien actif.

### Started for next sprint (migré ensuite dans Unreleased)
- Ajout de la physique sur les projectiles.
- Création de toutes les classes de projectiles.
- Direction des worms (optimisation des déplacements).

---

## [0.1] - 2025-11-14
*Jeu avec vue console – Sprint 1 : structure de base du jeu*

### Added
- Lancement du jeu via console, avec initialisation d’une partie.
- Création de 2 équipes, avec 2 worms chacune, automatiquement.
- Affichage des commandes, de la carte du jeu et de l’état du jeu sous forme de texte.
- Déplacement des worms.
- Attaque sur une position précise (avec dégâts sur le worm présent dans la position).
- Possibilité de passer son tour.
- Retour textuel pour chaque action effectuée.

### Planned for next sprint (0.2)
- Vue graphique (menu du jeu déjà disponible).
- Création de maps personnalisées affichables sur l’interface graphique.
- Grand arsenal d’armes (implémenté, à rattacher au jeu).
- Physique du jeu : gravité, vitesse, collisions, projectiles traversant le terrain.