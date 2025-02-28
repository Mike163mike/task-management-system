# Task management system

### The application uses the following tools and technologies:

* Java 21
* Maven
* Spring Boot
* Spring Web
* Spring Cloud
* Spring Data JPA
* Spring Validation
* Spring Test
* Spring Security
* Testcontainer
* Swagger (Open API)
* Docker compose
* Mapstruct
* Lombok
* PostgreSQL
* Flyway

### Getting started

The application can be launched via Docker Compose both from the terminal and from IntelliJ IDEA.<br> Here are recommendations for both options:

### Launch via the terminal

Open a terminal and navigate to the directory where the docker-compose.yml file is located.

Check the correctness of the file (if necessary, run the `docker–compose config` command to see the final configuration).

Run the containers with the command: `docker-compose up`. <br>  

If you want the terminal not to be blocked, run in the background: `docker-compose up -d`.

Checking the status of containers: `docker-compose ps`. This will show which containers are running and their status.

Stopping containers: <br> 

To stop containers, run: `docker-compose down`.

(This command will stop and delete containers, but will not affect images and data if they are saved in volume.)

### Launch via IntelliJ IDEA

IntelliJ IDEA supports working with Docker Compose through the built-in Docker plugin. To launch the app via IDEA:

1. Make sure that the Docker plugin is enabled:

* Go to `File → Settings (Preferences) → Plugins` and find the Docker plugin. If it is not enabled, activate it and restart IDEA.

2. Configure the Docker Connection:

* Go to `File → Settings (Preferences) → Build, Execution, Deployment → Docker`.
* Add a Docker daemon (usually `unix:///var/run/docker.sock` for Linux/macOS or the corresponding address for Windows).

3. Create a startup configuration for Docker Compose:

* Open `Run → Edit Configurations...`. 
* Click on the "+" button and select Docker Compose.
* Specify the configuration name.
* In the File(s) field, select your `docker-compose.yml`.
* If necessary, configure additional settings, such as the startup profile or environment variables.

4. Run the configuration:

* Press the `Run` button or use a key combination (for example, Shift+F10).
* IDEA will launch Docker Compose, and in the `Run` panel you will see the container logs.

5. Stopping containers:

* Stop the configuration via IDEA by clicking on the Stop button (red square).

### The application uses technology Swagger (Open API) for documentation. 

The documentation for endpoints is located at `http://localhost:8080/swagger-ui/index.html`