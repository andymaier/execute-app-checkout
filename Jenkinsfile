node {
        stage("Checkout") {
            checkout scm
        }

        stage('Maven Build') {
            sh "mvn -DskipTests=true package"
        }

        stage('Docker image') {
             docker.build("checkout")
        }

        stage("Deploy") {
            sh "docker rm -f checkout || echo 'ok'"
            sh "docker run -d --name checkout --net cp-all-in-one_default -p 11082:8082 checkout"
        }
}
