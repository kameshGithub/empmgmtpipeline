pipeline {
    agent any
    tools {
        maven 'Maven_3.5.0' 
    }
    stages {
        stage ("Compile Stage"){
            steps {
                withMaven(maven : 'M3') {
                    sh 'mvn clean compile'
                }
            }

        }

        stage ('Test Stage') {
            steps {
                withMaven(maven : 'M3') {
                    sh 'mvn test'
                }
            }
        }

        stage ('Deploy Stage') {
            steps {
                withMaven(maven : 'M3') {
                    sh 'mvn deploy'
                }
            }
        }
     }
}
