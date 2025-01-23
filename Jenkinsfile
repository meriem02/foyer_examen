pipeline {
    agent any

    environment {
        MY_VAR = 'M2_HOME'
        SONARQUBE_URL = 'http://192.168.50.4:9000/'
        SONARQUBE_TOKEN = 'squ_cd767184fe8883a08d392b60cedd96ca9bdc03c6'
        NEXUS_URL = "http://192.168.50.4:8081/repository/maven-releases"
        ARTIFACT_PATH = 'tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar'
        OUTPUT_DIR = 'target'
        DOCKER_IMAGE_NAME = 'tp-foyer'
        DOCKER_IMAGE_TAG = '5.0.0'
        DOCKER_USERNAME = 'meriem01'  
        DOCKER_PASSWORD = 'mimi987654321M'
        DOCKER_HUB_URL = 'https://hub.docker.com/' 
    }

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('git') {
            steps {
                git(
                    branch: 'meriem',
                    url: 'https://github.com/meriem02/foyer_examen.git'
                )
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                    mvn sonar:sonar \
                    -Dsonar.host.url=$SONARQUBE_URL \
                    -Dsonar.login=$SONARQUBE_TOKEN
                """
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy'
            }
        }

        stage('Download From Nexus') {
            steps {
                script {
                    sh """
                        mkdir -p $OUTPUT_DIR 
                        wget $NEXUS_URL/$ARTIFACT_PATH -O $OUTPUT_DIR/tp-foyer-5.0.0.jar
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                    # Construire l'image localement
                    docker build -t tp-foyer:5.0.0 -f Dockerfile .
                    
                    # Retagger l'image pour Docker Hub
                    docker tag tp-foyer:5.0.0 meriem01/tp-foyer:5.0.0
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    sh """
                    # Connexion à Docker Hub
                    echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                    
                    # Pousser l'image vers Docker Hub
                    docker push meriem01/tp-foyer:5.0.0
                    """
                }
            }
        }

 stage('Docker Compose'){
                                     steps {
                                        sh 'docker compose up'
                                     }
                                  }
    }
}
