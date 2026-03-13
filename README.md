# Redux — Jeu de Labyrinthe avec Bille (IPO – L2 Informatique)

> Jeu de type Enigma / Oxyd développé en Java Swing : une bille se déplace dans un labyrinthe,
> contrôlée par la souris, avec physique complète (frottement, rebonds, vitesse max),
> cases spéciales, multi-niveaux et tests automatisés.

---

## Présentation

Redux est un jeu de labyrinthe réalisé dans le cadre du module **IPO (Introduction à la Programmation Orientée Objet)** en L2 Informatique à l'Université Paris-Saclay.

Le joueur guide une bille dans un labyrinthe en déplaçant la souris. La bille est soumise à une physique réaliste : accélération, frottement progressif, rebonds sur les murs, et limitation de vitesse. Le labyrinthe est lu depuis des fichiers texte, ce qui permet de créer facilement de nouveaux niveaux.

Réalisé en binôme : **Aly KONATE** & **Youssef ABOU HASHISH**

---

## Fonctionnalités

- **Contrôle à la souris** — la bille accélère dans la direction du mouvement de la souris
- **Physique complète** — frottement progressif, vitesse maximale, rebonds vectoriels sur les murs
- **9 niveaux** — labyrinthes de complexité croissante chargés depuis des fichiers `.txt`
- **Cases spéciales** — 8 types de cases modifiant la physique ou l'état du jeu
- **Multi-niveaux** — passage automatique au niveau suivant après la victoire
- **Menu principal** — navigation au clavier (Jouer / Choisir niveau / Quitter)
- **Sélection de niveau** — choisir directement un niveau depuis le menu
- **Écran de fin** — écran de félicitations après le dernier niveau
- **Timer** — affichage du temps écoulé sur chaque niveau
- **Textures graphiques** — sprites pour chaque type de case et pour la bille
- **Tests automatisés** — 6 classes de tests unitaires et d'intégration

---

## Stack technique

| Composant | Technologie |
|---|---|
| Langage | Java (JDK 8+) |
| Interface graphique | Java Swing (JPanel, JFrame, Timer) |
| Boucle de jeu | javax.swing.Timer (~50 FPS) |
| Niveaux | Fichiers texte (.txt) |
| Tests | Tests "maison" (sans JUnit) |
| Architecture | MVC (Model / View / Controller) |

---

## Prérequis

- Java JDK 8 ou supérieur
- Aucune dépendance externe — Java pur

---

## Installation et lancement

### 1. Cloner le projet

```bash
git clone https://github.com/AlyyyJR/Redux.git
cd Redux
```

### 2. Compiler

```bash
javac -d out $(find src -name "*.java")
```

### 3. Lancer le jeu

```bash
java -cp out redux.controller.Game
```

> **Important** : toujours lancer depuis la **racine du projet** (`Redux/`).
> Les niveaux sont lus depuis `levels/` et les textures depuis `src/images/`.

---

## Lancer les tests

```bash
# Tests unitaires sur la bille
java -cp out redux.test.BallTest

# Tests sur le plateau
java -cp out redux.test.BoardTest

# Tests sur l'état du jeu
java -cp out redux.test.GameStateTest

# Tests des effets des cases spéciales
java -cp out redux.test.SquareEffectTest

# Tests de chargement de tous les niveaux
java -cp out redux.test.LevelsLoadTest

# Tests d'intégration (bille sur un vrai plateau)
java -cp out redux.test.IntegrationTest
```

---

## Contrôles

| Action | Touche / Souris |
|---|---|
| Déplacer la bille | Mouvement de la souris |
| Naviguer dans le menu | ↑ / ↓ |
| Valider une option | ENTRÉE |
| Choisir un niveau (sélection) | ← / → |
| Recommencer après Game Over | R |
| Niveau suivant après victoire | ESPACE |
| Retour au menu (sélection de niveau) | ÉCHAP |
| Quitter le jeu (écran de fin) | ÉCHAP |

---

## Architecture du projet

```
Redux/
├── src/
│   └── redux/
│       ├── model/                       # Logique du jeu et physique
│       │   ├── Ball.java                # Bille : position, vitesse, frottement, accélération
│       │   ├── Board.java               # Plateau : grille de cases, collisions, chargement niveau
│       │   ├── GameState.java           # État global : win, gameOver, reset
│       │   ├── Square.java              # Classe abstraite parente de toutes les cases
│       │   ├── EmptySquare.java         # Case vide : aucun effet
│       │   ├── WallSquare.java          # Mur : obstacle infranchissable, rebond
│       │   ├── HoleSquare.java          # Trou : Game Over si la bille y entre
│       │   ├── ExitSquare.java          # Sortie : victoire si la bille y entre
│       │   ├── IceSquare.java           # Glace : frottement très réduit (glisse)
│       │   ├── SandSquare.java          # Sable : frottement augmenté (ralentit)
│       │   ├── BoostSquare.java         # Boost : augmente instantanément la vitesse (+40%)
│       │   ├── SlowSquare.java          # Slow : réduit instantanément la vitesse (-60%)
│       │   ├── ReverseSquare.java       # Reverse : inverse les commandes de la souris
│       │   └── TeleporterSquare.java    # Téléporteur : téléporte la bille vers la case paire
│       │
│       ├── view/
│       │   └── GamePanel.java           # Vue Swing : plateau, bille, menus, overlays
│       │
│       ├── controller/
│       │   └── Game.java                # Contrôleur : boucle de jeu, souris, clavier, menus
│       │
│       └── test/
│           ├── BallTest.java            # Tests : accélération, frottement, limitSpeed
│           ├── BoardTest.java           # Tests : chargement niveau, dimensions, bords
│           ├── GameStateTest.java       # Tests : win, gameOver, reset
│           ├── IntegrationTest.java     # Tests d'intégration : trou et reverse sur vrai plateau
│           ├── LevelsLoadTest.java      # Tests : chargement des 8 niveaux, présence d'une sortie
│           └── SquareEffectTest.java    # Tests : effets de toutes les cases spéciales
│
├── levels/
│   ├── level1.txt                       # Labyrinthe simple (introduction)
│   ├── level2.txt                       # Labyrinthe avec trou
│   ├── level3.txt                       # Glace et sable
│   ├── level4.txt                       # Boost et ralentissement
│   ├── level5.txt                       # Cases à commandes inversées
│   ├── level6.txt                       # Téléporteurs, glace, sable, boost
│   ├── level7.txt                       # Toutes les cases combinées
│   ├── level8.txt                       # Niveau complet avancé
│   ├── level9.txt                       # Niveau final
│   ├── integration_hole.txt             # Niveau de test pour le trou
│   └── integration_reverse.txt          # Niveau de test pour le reverse
│
├── src/images/                          # Textures du jeu
│   ├── Ball.png
│   ├── Wall.png
│   ├── Ground.png
│   ├── Ice.png
│   ├── Sand.png
│   ├── Trap.png
│   ├── Exit.png
│   ├── Teleport.png
│   ├── Boost.png
│   ├── Slow.png
│   └── Reverse.png
│
├── out/                                 # Classes compilées (générées par javac)
├── AUTHORS.txt                          # Auteurs du projet
└── README.md
```

---

## Architecture MVC

```
┌────────────────────────────────────────────────────┐
│                   CONTROLLER                       │
│                   Game.java                        │
│  - boucle de jeu (Timer 50 FPS)                    │
│  - gestion souris (accélération bille)             │
│  - gestion clavier (menus, restart, niveaux)       │
│  - physique globale (friction, limitSpeed)         │
└────────────┬──────────────────────┬────────────────┘
             │                      │
             ▼                      ▼
┌────────────────────┐   ┌───────────────────────────┐
│      MODEL         │   │          VIEW             │
│                    │   │       GamePanel.java      │
│  Ball.java         │   │                           │
│  Board.java        │   │  - dessin des cases       │
│  GameState.java    │   │  - dessin de la bille     │
│  Square (et        │   │  - menu principal         │
│  sous-classes)     │   │  - sélection de niveau    │
│                    │   │  - overlay Game Over/Win  │
│                    │   │  - écran de fin           │
└────────────────────┘   └───────────────────────────┘
```

---

## Pipeline physique (par frame)

```
Déplacement souris détecté
        │
        ▼
ball.accelerate(sx, sy, FA)        ← accélération proportionnelle au mouvement
        │
        ▼
ball.limitSpeed(VMAX)              ← plafonnement de la vitesse
        │
        ▼
ball.tickTeleportCooldown()        ← décrémente le cooldown de téléportation
        │
        ▼
ball.updatePosition()              ← x += vx, y += vy
        │
        ▼
board.handleCollisions(ball)       ← rebonds sur bords + murs + cases spéciales
        │
        ▼
sq = board.getSquareAt(x, y)      ← case sous la bille
        │
        ├── IceSquare     → friction × 0.05
        ├── SandSquare    → friction × 8.0
        ├── BoostSquare   → friction × 0.3
        ├── SlowSquare    → friction × 12.0
        └── (défaut)      → friction de base
        │
        ▼
ball.applyFriction(friction)       ← réduction progressive de la vitesse
        │
        ▼
panel.repaint()                    ← affichage
```

---

## Cases spéciales

| Symbole | Case | Classe | Effet |
|---|---|---|---|
| `#` | Mur | `WallSquare` | Infranchissable, rebond vectoriel |
| `.` | Vide | `EmptySquare` | Aucun effet |
| `T` | Trou | `HoleSquare` | Game Over immédiat |
| `E` | Sortie | `ExitSquare` | Victoire, passage au niveau suivant |
| `I` | Glace | `IceSquare` | Frottement × 0.05 → glisse longtemps |
| `S` | Sable | `SandSquare` | Frottement × 8.0 → ralentit fort |
| `B` | Boost | `BoostSquare` | Vitesse × 1.4 au contact |
| `L` | Slow | `SlowSquare` | Vitesse × 0.4 au contact |
| `R` | Reverse | `ReverseSquare` | Commandes inversées tant que la bille est dessus |
| `A` / `Z` | Téléporteur | `TeleporterSquare` | Téléporte vers la case paire, cooldown 50 frames |

---

## Format des fichiers de niveaux

Les niveaux sont des fichiers `.txt` dans le dossier `levels/`.
Chaque caractère représente une case. Toutes les lignes doivent avoir la **même longueur**.

```
#####################
#...............#..E#
#.#####.#####.###.###
#.....#.....#.....#.#
###.#.###.#.#####.#.#
#...#.....#.....#...#
#.#########.###.#####
#...........#.......#
#.###########.#######
#.....#.....#.......#
#####.#.###.#####.#.#
#.....#...#.....#.#.#
#.#########.###.#.#.#
#...............#...#
#####################
```

**Règles importantes :**
- Toujours entourer le niveau de murs `#`
- Toutes les lignes doivent avoir exactement la même longueur
- Chaque niveau doit contenir au moins une sortie `E`
- Les téléporteurs `A` et `Z` doivent toujours apparaître en paire

---

## Tests

6 classes de tests couvrent l'ensemble du projet :

| Classe | Ce qui est testé |
|---|---|
| `BallTest` | Accélération, frottement (arrêt + diminution), limitation de vitesse |
| `BoardTest` | Chargement de level1.txt, dimensions, bords en murs, cohérence getSquareAt |
| `GameStateTest` | Valeurs initiales, setGameOver, setWin, reset |
| `SquareEffectTest` | Effets de HoleSquare, ExitSquare, BoostSquare, SlowSquare, ReverseSquare |
| `LevelsLoadTest` | Chargement des 8 niveaux, dimensions > 0, présence d'une ExitSquare |
| `IntegrationTest` | Bille sur HoleSquare réel → gameOver, bille sur ReverseSquare → inversion + leave |

---

## Auteurs

**Aly KONATE** & **Youssef ABOU HASHISH**  
L2 Informatique — Université Paris-Saclay  
Module IPO — Introduction à la Programmation Orientée Objet