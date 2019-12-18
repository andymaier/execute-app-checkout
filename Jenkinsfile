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

        stage('Docker push') {             
             sh "docker tag checkout localhost:5000/checkout"
             sh "docker push localhost:5000/checkout"
        }

        stage("Deploy") {
            sh "docker rm -f checkout || echo 'ok'"
            sh "docker pull localhost:5000/checkout"
            sh "docker run -d --name checkout --net cp-all-in-one_default -p 11081:8081 localhost:5000/checkout"
        }
}
