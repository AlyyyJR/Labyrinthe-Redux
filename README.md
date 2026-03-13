# Projet Redux – Labyrinthe avec bille (IPO – L2 Informatique)

Projet réalisé dans le cadre du module **Introduction à la Programmation Orientée Objet (IPO)**
Le but est de programmer un petit jeu de type **Enigma / Oxyd** :  
une **bille** se déplace dans un **labyrinthe**, influencée par les mouvements de la **souris**, avec **frottement**, **rebonds**, **cases spéciales** et **lecture de niveaux** depuis des fichiers texte

## 1. Structure du projet

Structure du projet :

Redux/
├─ src/
│   └─ redux/
│        ├─ model/
│        │    ├─ Ball.java
│        │    ├─ Board.java
│        │    ├─ GameState.java
│        │    ├─ Square.java
│        │    ├─ EmptySquare.java
│        │    ├─ WallSquare.java
│        │    ├─ HoleSquare.java
│        │    ├─ ExitSquare.java
│        │    ├─ IceSquare.java
│        │    ├─ SandSquare.java
│        │    ├─ BoostSquare.java
│        │    ├─ SlowSquare.java
│        │    ├─ ReverseSquare.java
│        │    └─ TeleporterSquare.java
│        │
│        ├─ view/
│        │    └─ GamePanel.java
│        │
│        ├─ controller/
│        │    └─ Game.java
│        │
│        └─ test/
│             ├─ BallTest.java
│             ├─ BoardTest.java
│             ├─ GameStateTest.java
│             ├─ IntegrationTest.java
│             ├─ LevelsLoadTest.java
│             └─ SquareEffectTest.java
│
├─ levels/
│     ├─ level1.txt
│     ├─ level2.txt
│     ├─ …
│     └─ level8.txt
│
├─ images/          <- textures du jeu
├─ out/             <- fichiers .class générés
└─ README.md

Le projet suit une architecture **MVC** :
- **model/** : logique du jeu et physique (bille, plateau, cases…)
- **view/** : affichage graphique avec Swing
- **controller/** : boucle de jeu et gestion des entrées utilisateur
- **test/** : tests unitaires et tests d’intégration “maison”

## 2. Objectifs du projet

- Implémenter une bille avec :
  - position réelle `(x, y)`
  - vitesse `(vx, vy)`
  - accélération via la souris
  - frottement progressif
- Gérer un labyrinthe lu depuis un fichier texte
- Implémenter des rebonds réalistes sur les murs
- Gérer plusieurs niveaux successifs
- Ajouter des cases spéciales modifiant la physique
- Afficher des écrans (menu, victoire, défaite, fin du jeu)
- Concevoir un code orienté objet propre et extensible

## 3. Fichiers de niveaux

Les niveaux sont décrits dans des fichiers texte situés dans le dossier `levels/`
Symboles utilisés (alignés avec le code) :
- `#` : mur
- `.` : case vide
- `T` : trou → **Game Over**
- `E` : sortie → **victoire**
- `I` : glace (faible frottement)
- `S` : sable / boue (fort frottement)
- `B` : boost de vitesse
- `L` : ralentissement fort
- `R` : commandes inversées
- `A` / `Z` : téléporteurs liés

Exemple de niveau :
############
#..T.......#
#.####.....#
#......#...#
#..##..#E..#
#......#...#
#........T.#
############

## 4. Contrôle de la bille

La bille est contrôlée par les déplacements de la souris
Soit `(sx, sy)` le déplacement de la souris entre deux frames :
- vx += fa * sx
- vy += fa * sy

Puis application du frottement :
- si `v ≤ f` → la bille s’arrête
- sinon → réduction proportionnelle de la vitesse

La vitesse est limitée à `VMAX` pour garder un comportement contrôlable
Le frottement dépend de la case sous la bille :
- **glace (`I`)** → frottement réduit → glisse longue
- **sable (`S`)** → frottement augmenté → ralentissement fort


## 5. Boucle de jeu
La boucle de jeu repose sur un `javax.swing.Timer` (~50 FPS) :

ball.updatePosition();
board.handleCollisions(ball);

Square sq = board.getSquareAt(ball.getX(), ball.getY());

double friction = FRICTION;
if (sq instanceof IceSquare) {
	friction *= 0.3;
} else if (sq instanceof SandSquare) {
	friction *= 3.0;
}

ball.applyFriction(friction);
ball.limitSpeed(VMAX);
panel.repaint();

Le **Controller** orchestre la boucle, le **Model** gère la logique, et la **View** affiche le résultat


##  6. Gestion des collisions

Les collisions sont gérées par `Board` :
- rebonds sur les bords du plateau
- détection des murs via les côtés et coins de la bille
- réflexion vectorielle de la vitesse
- appel de `sq.enter(b)` pour déclencher les effets :
  - trou → Game Over
  - sortie → victoire
  - boost, ralentissement, inversion, téléportation, etc


## 7. Tests
Des tests unitaires et d’intégration sont fournis dans le package `redux.test` :
- **BallTest** : accélération, frottement, limitation de vitesse
- **GameStateTest** : gestion des états win / game over
- **BoardTest** : chargement des niveaux
- **IntegrationTest** : effets sur un vrai plateau
- **LevelsLoadTest** : chargement de tous les niveaux
- **SquareEffectTest** : effets des cases spéciales

Lancement des tests :
javac -d out $(find src -name "*.java")
java -cp out redux.test.BallTest

## 8. Compilation et exécution
Compilation & Exécution : 
javac -d out $(find src -name "*.java")
java -cp out redux.controller.Game

## 9. Extensions réalisées
- Multi-niveaux
- Menu principal et sélection de niveau
- Écran de fin
- Téléporteurs
- Cases à effets avancés
- Textures graphiques
- Tests automatisés

## 10. Auteurs

Projet réalisé par :
- Aly KONATE
- Youssef ABOU HASHISH
L2 Informatique – Université Paris-Saclay
IPO – Introduction à la Programmation Orientée Objet# Labyrinthe-Redux
