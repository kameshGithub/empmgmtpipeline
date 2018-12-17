pipeline {
    
    agent any
    tools {
        maven 'M3'
    }
    stages{ 
    stage ("Compile Stage"){
            steps {
                sh 'mvn clean compile'
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
           
        }
    }
}
