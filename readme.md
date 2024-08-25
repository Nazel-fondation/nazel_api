# Nazel API 🌐

**Nazel API** est une API conçue pour assurer la liaison entre le Nazel Launcher et le panel Pterodactyl. Cette API permet de créer, supprimer, et récupérer des informations sur les serveurs de jeux, en particulier pour les utilisateurs de la plateforme Nazel.

## Fonctionnalités 🔧

- **Création de serveurs** : Créez des serveurs de jeux au nom des utilisateurs via le Nazel Launcher.
- **Suppression de serveurs** : Supprimez des serveurs de jeux associés à un utilisateur lorsque cela est nécessaire.
- **Récupération d'informations** : Obtenez des informations détaillées sur un serveur, telles que son statut, ses ressources, et bien plus.

## Prérequis 📋

- Un panel [Pterodactyl](https://pterodactyl.io/) configuré et fonctionnel.
- Une version récente du jdk java
- Les clés API du panel Pterodactyl pour l'interaction avec celui-ci.

## Installation 🚀

### Étapes d'installation et de configuration 🔧

1. Clonez le dépôt :

    ```bash
    git clone https://github.com/Nazel-fondation/nazel_api.git
    cd nazel_api
    ```

2. Configurez l'API :

    - Renommez le fichier `.env.example` en `.env`.
    - Renseignez les variables d'environnement dans le fichier `.env` avec vos informations Pterodactyl et Nazel Launcher :

    ```
      PTERODACTYL_API_TOKEN=token
      PTERODACTYL_API_IP=0.0.0.0
      SERVER_IP=0.0.0.0
    ```

L'API sera accessible à l'adresse `http://localhost:8080` (ou selon votre configuration).

## Endpoints de l'API 📄

### Création de serveur

- **POST** `/minigames/createserver`
  
  Crée un nouveau serveur pour un utilisateur spécifié.
  
  **Paramètres :**
  - `userId`: Identifiant de l'utilisateur
  - `eggName`: egg à utiliser (grossièrement c'est la config du serveur dans pterodactyl)
  
  **Exemple de réponse :**
  ```json
    {
      "status" : "success",
      "ip": "0.0.0.0:30009"
    }
  ```

  ### Changer d'opérateur (op)

- **POST** `/minigames/changeowner`
  
  Crée un nouveau serveur pour un utilisateur spécifié.
  
  **Paramètres :**
  - `serverName`: Identifiant du serveur (id de l'utilisateur)
  - `newOwnerPseudo`: Pseudo du nouvel utilisateur
  
  **Exemple de réponse :**
  ```json
    {
      "status" : "success",
      "servername": "Un joli nom de serveur"
    }
  ```

  ### Récupérer l'ip d'un serveur depuis un userID

- **GET** `/minigames/serveridentifierinfo/{serveridentifier}`
    
  
  **Exemple de réponse :**
  ```json
    {
      "status" : "success",
      "ip": 0.0.0.0
    }
  ```

## Contribution 🤝

Les contributions sont les bienvenues ! Voici les étapes de base pour contribuer :

1. Forkez le projet. 🍴
2. Créez une branche pour votre fonctionnalité ou correction (`git checkout -b feature/ma-fonctionnalite`). 🌿
3. Commitez vos modifications (`git commit -m 'Ajout de ma fonctionnalité'`). 💬
4. Poussez votre branche (`git push origin feature/ma-fonctionnalite`). 📤
5. Ouvrez une Pull Request. 🔄

Merci de respecter les bonnes pratiques de code lors de vos contributions.

## Licence 📄

Ce projet est sous licence MIT. Voir le fichier [LICENSE](./LICENSE) pour plus de détails.

## Contact 📬

Pour toute question ou support, vous pouvez me contacter via :

- Email : thibaultfalezan@gmail.com
- Discord : Vupilex
  
