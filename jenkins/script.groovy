def buildApp() {
    echo "building the application..."
    sh 'mvn install'
} 
def testApp() {
    echo "building the application..."
    sh 'mvn test'
} 
def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t aeramzy9/java-maven-app:latest .'
        sh "echo \$PASS | docker login -u \$USER --password-stdin"
        sh 'docker push aeramzy9/java-maven-app:latest'
    }
}

def deployApp() {
    echo 'deploying the application...'
} 

return this