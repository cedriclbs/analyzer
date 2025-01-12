# les biens-lothsavan-projet-cpoo5-24-25

# Projet CPOO : Analyseur de texte et Évaluateur de disposition clavier

## Description

Ce projet se compose de deux modules principaux :

1. **Analyseur de texte** : Un outil qui analyse un corpus de texte pour compter les occurrences des N-grammes (séquences de 1, 2 ou 3 caractères).
2. **Évaluateur de disposition clavier** : Un programme qui évalue l'efficacité des dispositions de clavier en fonction de critères ergonomiques.

---

## Structure des répertoires

```
.
├── README.md
├── build.gradle
├── out
│   └── main/java (compilation des fichiers sources)
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── Main.java (point d'entrée principal)
│   │   │   ├── logiciel1 (analyseur de texte)
│   │   │   └── logiciel2 (évaluateur de disposition clavier)
│   │   └── resources
│   │       ├── config (fichiers de configuration JSON)
│   │       ├── input (fichiers d'entrée, corpus)
│   │       └── output (résultats des analyses)
```

---

## Prérequis

- **Java** : Version 21 ou supérieure.
- **Gradle** : Utilisé pour la gestion des dépendances et la compilation.

---

## Compilation

Pour compiler le projet, exécutez la commande suivante à la racine du projet :

```bash
gradle build
```

---

## Exécution

Deux modules principaux peuvent être exécutés.
```bash
gradle run
```
pour voir les options de lancement sinon directement :


### 1. Analyseur de texte (Logiciel 1)

Commande :

```bash
java -jar build/libs/les-biens-lothsavan-projet-cpoo5-24-25-all.jar 1
```

- **Entrée** : Défini dans `src/main/resources/input` (par exemple, `sample-corpus1.txt`).
- **Sortie** : Les résultats des N-grammes sont exportés au format CSV dans `src/main/resources/output`.

### 2. Évaluateur de disposition clavier (Logiciel 2)

Commande :

```bash
java -jar build/libs/les-biens-lothsavan-projet-cpoo5-24-25-all.jar 2
```

- **Entrée** : Corps de texte à analyser provenant de `src/main/resources/input`.
- **Configuration** : Dispositions clavier définies dans `src/main/resources/config/keymap.json` et `keyboards.json`.
- **Sortie** : Évaluations exportées dans `src/main/resources/output`.

---

## Configuration des fichiers

Les configurations pour les dispositions clavier sont dans le dossier `src/main/resources/config`.


---

## Dépendances

- **Gradle** : Pour la compilation et la gestion des dépendances.

---

## Nettoyage

Pour supprimer les fichiers générés par Gradle, exécutez :

```bash
gradle clean
```

---

Nous avons conçu ce projet avec sérieux et détermination : juste assez pour nous challenger, mais pas au point de nous envoyer à la NASA.