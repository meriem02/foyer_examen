pipeline {
    agent any

    environment {
        MY_VAR = 'M2_HOME'
        SONARQUBE_URL = 'http://192.168.50.4:9000/'
        SONARQUBE_TOKEN = 'squ_da0c1211f0d71ee90f9d3839d6a0438de8cb1a4b'
        NEXUS_URL = "http://192.168.50.4:8081/repository/maven-releases"
        ARTIFACT_PATH = 'tn/esprit/tp-foyer/5.0.0/tp-foyer-5.0.0.jar'
        OUTPUT_DIR = 'target'
        DOCKER_IMAGE_NAME = 'tp-foyer'
        DOCKER_IMAGE_TAG = '5.0.0'
       
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
        stage('Download From Nexus'){
            steps{
                script{
                    sh """
                        mkdir -p $OUTPUT_DIR 
                        wget $NEXUS_URL/$ARTIFACT_PATH  -O $OUTPUT_DIR/tp-foyer-5.0.0.jar 
                    """
                }
            }
        }
           stage('Build Docker Image'){
            steps{
                script{
                    sh """
                        docker build -t $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG -f Dockerfile  .
                    """
                }
            }
        }     

    }
}
