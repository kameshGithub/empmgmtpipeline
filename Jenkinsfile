node {
    agent any
    def app
    tools {
        maven 'M3'
    }
    stages {
         stage ("Clone Repository Stage"){
            steps {
               
                checkout scm
            }   
        }
        stage ("Compile Stage"){
            steps {
               
                    sh 'mvn clean compile'
               
            }

        }

        
        stage ('Build Image Stage') {
            steps {
                
                app = docker.Build("getintodevops/helloworld")
               
            }
        }
        stage ('Test Image Stage') {
            steps {
               
                  //  sh 'mvn test'
                app.inside {
                    sh 'eho "Tests passed"'
                }
            }
        }
        stage ('Push Image') {
            steps {
                /**
                * Set two tags , incremental build number and latest
                */               
                docker.withRegistry('htps://registry.hub.docker.com','docker-hub-credentials'){
                    app.push("${env.BUILD_NUMBER}")
                    app.push("latest")
                }
            }
        }
        stage ('Deploy Stage') {
            steps {
                
                   // sh 'mvn deploy'
               
            }
        }
     }
}
