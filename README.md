<div align="center">
  <h1 align="center">
    <img width="900" height="500" alt="Banner" src="src/main/resources/img-readme/FOROHub Portada.png" />

  </h1>
</div>
<br> </br>

## 🎓 ForoHub API

API REST construida con **Spring Boot 3**, diseñada como un foro de discusión. Los usuarios pueden autenticarse. crear, leer, actualizar y eliminar tópicos (CRUD), respuestas a esos tópicos de acuerdo a los diferentes cursos y mantener la seguridad mediante **JWT (JSON Web Tokens)**.

---

## 🚀 Características

- Registro y autenticación de usuarios con **JWT**.
- Manejo de entidades principales:  
  - **Usuario** 👤  
  - **Curso** 📘  
  - **Tópico** 💬  
  - **Respuesta** 📝

- Seguridad implementada con **Spring Security** + **Filter JWT**.
- Persistencia con **Spring Data JPA** y **MySQL** (configurable).
- Migraciones de base de datos con **Flyway**.

---

## 🛠️ Tecnologías utilizadas

- Java 17 ☕
- Spring Boot 3
- Spring Security
- JPA / Hibernate
- Flyway
- JWT (Auth0 library)
- MySQL (o H2 para pruebas)



🛠️ Instalación y ejecución

Clonar el repositorio:

git clone [https://github.com/ginailyn/ChallegeForoHub.git](https://github.com/ginailyn/ChallegeForoHub.git)



Configurar la base de datos en application.properties:

```java environment
spring.datasource.url=jdbc:mysql://localhost/foro_hub
spring.datasource.username=root
spring.datasource.password=tu_password
spring.jpa.hibernate.ddl-auto=update

```


## 🗄️ Estructura del proyecto

```
src/main/java/com/alura/desafios/apiForoHub/
│── domain/          # Entidades (Usuario, Curso, Topico, Respuesta)
│── infra/security/  # Seguridad y JWT (TokenService, SecurityFilter, etc.)
│── controller/      # Controladores REST
│── repository/      # Repositorios JPA
```
## Compilar y ejecutar la aplicación:

mvn spring-boot:run


Acceder a la API en:
http://localhost:8080/swagger-ui/index.html



## 🌟 Base de Datos, Tablas y Manejo Información almacenada en DB ya consumida de la API-Gutendex 🌟

## 📝 Tablas DB 📝

![Captura de pantalla de la Base de Datos](src/main/resources/img-readme/diagramaER-BD.png)

## 📝 Información Almacenada 📝

## Autor: ✒️

* **Gina Arias** -
* **Linkedin* - ((https://www.linkedin.com/in/gina-a-arias-aranguren-882098133/))
* **Github* - ([(https://github.com/ginailyn)](https://github.com/ginailyn))

## 🤝 Redes sociales 🤝

<h3 align="left">Connect with me</h3>

<a href="https://www.linkedin.com/in/[tu-usuario-linkedi](https://www.linkedin.com/in/gina-a-arias-aranguren-882098133/)/" target="_blank"> <img src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/linked-in-alt.svg" alt="LinkedIn" height="30" width="40" /> </a> <a href="https://github.com/[tu-usuario-github](https://github.com/ginailyn)" target="_blank"> <img src="https://raw.githubusercontent.com/rahuldkjain/github-profile-readme-generator/master/src/images/icons/Social/github.svg" alt="GitHub" height="30" width="40" /> </a>
