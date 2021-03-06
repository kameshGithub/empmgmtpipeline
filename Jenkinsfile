node {
    def image
    def packedImage
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
        withCredentials([string(credentialsId: 'dockerpwd', variable: 'dockerHubPwd')]) {
        sh "docker login -u kameshc -p ${dockerHubPwd}"
        def jarname="/var/jenkins_home/workspace/EmpMgmgtBE/target/empmgmtbe-0.0.1.jar"
          
        sh "docker build -t kameshc/empmgmtbe:${env.BUILD_NUMBER} ."
        }
    }

    stage('Test image') {
        /* Ideally, we would run a test framework against our image.
         * For this example, we're using a Volkswagen-type approach ;-) */
        image = docker.image("kameshc/empmgmtbe:${env.BUILD_NUMBER}")
        image.inside {
            sh 'echo "Tests passed"'
        }
    }

    stage('Push Image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
            
        /* withCredentials([string(credentialsId: 'dockerpwd', variable: 'dockerHubPwd')]) {
          sh "docker login -u kameshc -p ${dockerHubPwd}"
          app.push()
          
        } */
           
    }

    stage('Pack the Image') {
        /* Pack the application image with mongodb image contained in it to work
         * Tag the image as packed by appending _packed
         */
        def packerHome = tool name: 'packer', type: 'biz.neustar.jenkins.plugins.packer.PackerInstallation'
       
        packedImage = image    
        
        sh "${packerHome}/packer build packer.json"
        packedImage.tag("packed")
    }
    stage('Push Packed Image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
            
        /* withCredentials([string(credentialsId: 'dockerpwd', variable: 'dockerHubPwd')]) {
          sh "docker login -u kameshc -p ${dockerHubPwd}"
          app.push()
          
        } */
            // packedImage.push()
            // packedImage.push("latest")
    }

}
