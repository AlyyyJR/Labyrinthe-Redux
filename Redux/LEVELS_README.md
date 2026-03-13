# Guide de création des niveaux – Projet Redux

Ce document explique comment créer, modifier et tester des niveaux pour le jeu **Redux** 
Les niveaux sont stockés dans le dossier : /levels/
Chaque niveau est un fichier texte nommé :
- level1.txt
- level2.txt
- level3.txt
...

## 1. Structure d’un niveau

Un niveau est un **rectangle de caractères**, où chaque caractère représente une case du labyrinthe
Exemple :

############
#..T.......#
#.####.....#
#......#...#
#..##..#E..#
#......#...#
#........T.#
############

**Règle importante**  
Toutes les lignes doivent avoir **exactement la même longueur**, sinon le chargement du niveau échoue

## 2. Symboles disponibles

Voici la liste complète des symboles reconnus par le moteur du jeu (cf. `Board.loadFromFile()`)
| Symbole | Case / Élément            | Classe Java            | Effet                   |
|--------|----------------------------|------------------------|-------------------------|
| `#`    | Mur                        | `WallSquare`           | Infranchissable, rebond |
| `.`    | Case vide                  | `EmptySquare`          | Aucun effet             |
| `T`    | Trou                       | `HoleSquare`           | Game Over               |
| `E`    | Sortie                     | `ExitSquare`           | Niveau terminé          |
| `I`    | Glace                      | `IceSquare`            | Très faible frottement  |
| `S`    | Sable / boue               | `SandSquare`           | Fort frottement         |
| `B`    | Boost                      | `BoostSquare`          | Accélération            |
| `L`    | Ralentissement             | `SlowSquare`           | Ralentissement fort     |
| `R`    | Commandes inversées        | `ReverseSquare`        | Contrôles inversés      |
| `A`    | Téléporteur A              | `TeleporterSquare`     | Téléporte vers Z        |
| `Z`    | Téléporteur Z              | `TeleporterSquare`     | Téléporte vers A        |

## 3. Taille d’un niveau

La taille est déterminée automatiquement lors du chargement :
- **largeur** = nombre de caractères par ligne
- **hauteur** = nombre de lignes du fichier

Pour rappel : Toutes les lignes doivent avoir la **même largeur**

## 4. Position de départ de la bille

La position initiale de la bille est actuellement définie dans `Game.java` : ball = new Ball(1.5, 1.5, R);
Les coordonnées sont exprimées en cases, avec des valeurs réelles : (1.5, 1.5) correspond au centre de la case (1,1)
Il est possible d’adapter cette position pour chaque niveau si nécessaire

## 5. Ajouter un nouveau type de case (bonus)

Pour ajouter une nouvelle case personnalisée :
1. Créer une classe qui hérite de Square
2. Implémenter les méthodes :
- isEmpty()
- enter(Ball b)
- leave(Ball b)
- touch(Ball b)
3. Ajouter un symbole dans Board.loadFromFile() :
case 'X':
    grid[i][j] = new MyNewSquare();
    break;
4. Utiliser ce symbole dans un fichier de niveau

## 6. Exemple de niveau complet

###############
#......I......#
#.#######.#####
#..S....#....E#
#..S....#.....#
#......T......#
###############

Ce niveau contient :
- de la glace (I)
- du sable (S)
- un piège (T)
- une sortie (E)

## 7. Tester un niveau
1. Créer ou modifier un fichier dans le dossier :levels/levelX.txt
2. Compiler le projet : javac -d out $(find src -name "*.java")
3. Lancer le jeu : java -cp out redux.controller.Game
Le passage au niveau suivant se fait automatiquement après une victoire

## 8. Bonnes pratiques
- Toujours entourer le niveau de murs (#)
- Tester la cohérence des largeurs de lignes
- Éviter de placer la bille initiale trop près d’un mur
- Tester chaque nouveau niveau individuellement

