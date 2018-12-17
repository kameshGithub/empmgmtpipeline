pipeline {
    agent any
    tools {
        maven 'M3'
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
