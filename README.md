# Projet WebServices

> GRENIER Lilas  
> MONPRIVE Cédric

## Méthodes implémentées

### Page login

[Lien de la page de login](http://localhost:8080/webservice_projet_war_exploded/users/login)

- **Méthode POST** : authentification. Le corps de la commande doit contenir les champs "nom" et "mdp" sous format JSON.
La méthode renvoie un token permettant l'authentification sur les futures commandes. 

*Exemple :*
> ```
> curl -X POST http://localhost:8080/webservice_projet_war_exploded/users/login
> -d {
>    "nom": "<nom d'utilisateur>",
>    "mdp": "<mot de passe>"
>    }
> ```

L'authentification se fera par la suite dans les requêtes par la méthode Bearer, 
comme illustré dans les exemples suivants. 

### Page canals

[Lien de la page des canals](http://localhost:8080/webservice_projet_war_exploded/channels/)

- **Méthode GET** : consultations des canals accessibles à l'utilisateur. 
Si ce dernier fournit un token valide, les canals privés auxquels il a accès s'afficheront également.

*Exemple :*
> ```
> curl -X GET http://localhost:8080/webservice_projet_war_exploded/channels/
> -H "Authorization: Bearer <token>"
> ```


### Page channel / page messages d'un channel

[Lien de la page du 1er channel](http://localhost:8080/webservice_projet_war_exploded/channels/1)

- **Méthode GET** : consultations des messages du channel.
Si le channel est privé, l'utilisateur doit fournir un token valide pour sa consultation. 

*Exemple :*
> ```
> curl -X GET http://localhost:8080/webservice_projet_war_exploded/channels/1
> -H "Authorization: Bearer <token>"
> ```

- **Méthode POST** : publication d'un message dans le channel. 
Le contenu du message est fourni sous format JSON avec la balise "text". 
L'authentification via un token est obligatoire, à channel privé ou non. 

*Exemple :*
> ```
> curl -X POST http://localhost:8080/webservice_projet_war_exploded/channels/1
> -H "Authorization: Bearer <token>"
> -d {
>    "text": "<contenu du message>",
>    }
> ```

### Page message

[Lien de la page du 1er message, dans le 1er channel](http://localhost:8080/webservice_projet_war_exploded/channels/1/messages/1)

- **Méthode GET** : consultation du message.
  Si le channel est privé, l'utilisateur doit fournir un token valide pour sa consultation.

*Exemple :*
> ```
> curl -X GET http://localhost:8080/webservice_projet_war_exploded/channels/1/messages/1
> -H "Authorization: Bearer <token>"
> ```

- **Méthode PATCH** : modification du message. 
  Le nouveau texte du message est passé sous format JSON avec la clé "text".
  L'utilisateur doit fournir un token vérifiant qu'il est bien l'auteur du message.

*Exemple :*
> ```
> curl -X PATCH http://localhost:8080/webservice_projet_war_exploded/channels/1/messages/1
> -H "Authorization: Bearer <token>"
> -d {
>    "text": "<nouveau contenu du message>",
>    }
> ```

- **Méthode DELETE** : délétion du message.
  L'utilisateur doit fournir un token vérifiant qu'il est l'auteur du message ou l'administrateur du channel.

*Exemple :*
> ```
> curl -X DELETE http://localhost:8080/webservice_projet_war_exploded/channels/1/messages/1
> -H "Authorization: Bearer <token>"
> ```
