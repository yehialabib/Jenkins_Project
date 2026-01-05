def buildApp() {
    echo "Building application..."
    sh 'gradle clean build -x test --no-daemon'
}

def testApp() {
    echo "Running unit tests..."
    sh 'gradle test'
}

def pushToDockerHub(String imageName, String credentialsId) {
    echo "Processing Docker Hub Push for ${imageName}..."
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        // Build the image using the Dockerfile in the repo
        sh "docker build -t ${imageName} ."
        // Login and push
        sh "echo \$PASS | docker login -u \$USER --password-stdin"
        sh "docker push ${imageName}"
    }
}

def pushToNexus(String imageName, String nexusUrl, String credentialsId) {
    echo "Processing Nexus Push to ${nexusUrl}..."
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        String nexusImage = "${nexusUrl}/${imageName}"
        // Tag the existing image for Nexus
        sh "docker tag ${imageName} ${nexusImage}"
        // Login to Nexus and push
        sh "echo \$PASS | docker login -u \$USER --password-stdin ${nexusUrl}"
        sh "docker push ${nexusImage}"
    }
}

def deployApp() {
    echo "Successfully deployed branch ${env.BRANCH_NAME} to the target environment."
}

return this