# Execution Guide

This project contains the main Java application that provides the following features:
- **User Functionality**:
    - Registration
    - Login
    - Browsing a list of users

- **Implemented Security Features**:
    - Passive defense, uses ValidatingObjectInputStream to type check the object before deserialization

Follow the steps below to set up, run, and interact with the application:

1. **Extract the Zipfile**

2. **Install Docker Desktop (if not already installed)**
    - Download and install from [here](https://www.docker.com/products/docker-desktop/).

3. **Open Docker Desktop**

4. **Navigate to the Project Root Directory**
    - After extracting the files, ensure the following files and folders are present in the root directory:
        - `.idea` folder
        - `.mvn` folder
        - `db-init` folder
        - `src` folder
        - `.gitattributes`
        - `.gitignore`
        - `compose.yaml`
        - `DockerFile`
        - `HELP.md`
        - `mvnw`
        - `mvnw.cmd`
        - `pom.xml`

5. **Open Command Prompt**
    - Navigate (`cd`) into the project directory.

6. **Build the Application**
    - Execute the following command to build the application:
      ```bash
      docker build -t teamproject .
      ```  
    - This process generates the necessary JAR file for deployment.

7. **Start the Containers**
    - Ensure you are still in the project root directory in the command prompt.
    - Run the following command to launch the containers:
      ```bash
      docker compose up
      ```

8. **Access the Application**
    - Open a web browser and go to [http://localhost:8080](http://localhost:8080) to use the application.
    - Register/Login
    - Navigate to the "Upload File" section
    - Upload the malicious_data.txt file (stored in the same directory as main classes) and notice how the application does not accept the file.

9. **Stop the Containers**
    - When you are finished, shut down the application by running the following command:
      ```bash
      docker compose down -v
      ```
