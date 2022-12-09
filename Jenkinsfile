pipeline {

    agent any
    
    tools {
      maven '3.8.6'
    }
    
    environment {
        JAVA_HOME = '/opt/java/openjdk'
    }
    
    stages {
        stage('Build') {
            steps {
                git (url: 'https://github.com/kosma-hyzoe/rest-api.git',
                    branch: 'main'
                )
                sh 'mvn validate'
                sh 'mvn test-compile'
            }
        
        }
    
    
        stage('Test'){
            steps {
              sh 'mvn test'
            }
            
            post {
                success {
                    
                    echo 'get TestNG raport'
                    echo 'package it'    
                }
            }
        }
        
        stage('CleanUp'){
            steps {
                sh 'mvn clean'
            }
        }
    }
}
