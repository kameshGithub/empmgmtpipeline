node {
    def app

    stage('Checkout repository') {
        /* Let's make sure we have the repository cloned to our workspace */
        git credentialsId: 'git-creds', url:'https://github.com/kameshGithub/empmgmtpipeline'
        /*checkout scm*/
    }
    
    stage('Build App') {
       def mvnHome = tool name: 'maven-3', type: 'maven'
       def mvnCMD = "${mvnHome}/bin/mvn"
       sh "${mvnCMD} -DskipTests clean package"
    } 
    
    stage('Build image') {
        /* This builds the actual image; synonymous to
         * docker build on the command line */
         docker.withRegistry('','docker-hub-credentials') {
            app = docker.build("kameshc/empmgmtbe:${env.BUILD_NUMBER}")
        //   app.push("latest")
        }
        
    }

    stage('Test image') {
        /* Ideally, we would run a test framework against our image.
         * For this example, we're using a Volkswagen-type approach ;-) */

        app.inside {
            sh 'echo "Tests passed"'
        }
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        
       /*  docker.withRegistry('','docker-hub-credentials') {
            app = docker.build("kameshc/empmgmtbe:${env.BUILD_NUMBER}")
        }   */
       
       
        /* withCredentials([string(credentialsId: 'dockerpwd', variable: 'dockerHubPwd')]) {
          sh "docker login -u kameshc -p ${dockerHubPwd}"
          app.push()
          
        } */
      
    }
}
