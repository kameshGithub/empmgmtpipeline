pipeline {
    
    agent any
    tools {
        maven 'M3'
    ]   
    stage ("Compile Stage"){
            steps {
                sh 'mvn clean compile'
             }
     }
    stage ('Test Image Stage') {
            steps {
               
                  //  sh 'mvn test'
             
            }
        }
        
  stage ('Build Container Stage') {
    agent {
        dockerfile {
        label 'klable'
        registryUrl 'htps://registry.hub.docker.com'
        registryCredentialsId 'docker-hub-credentials'
        additionalBuildArgs  '--build-arg version=1.0.2'
        }
    }
            steps {
                
                   // sh 'mvn deploy'
               
            }
        }
     
}

