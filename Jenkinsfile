pipeline {
    agent any
    
    tools {
        jdk 'jdk17'     
        gradle 'gradle'
    }

    environment {
        
        DOCKER_HUB_USER = "yehialabib" 
        IMAGE_BASE_NAME = "java-gradle-app"
        
        // Use 'localhost:8082' if Jenkins and Nexus are in the same network
        // or the specific Codespaces URL/IP for port 8082
        NEXUS_URL = "localhost:8082" 
        
        // Dynamic tagging for multiple branches (e.g., main-1, dev-3)
        IMAGE_TAG = "${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
        FULL_IMAGE_NAME = "${DOCKER_HUB_USER}/${IMAGE_BASE_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    pipelineScript = load 'script.groovy'
                    echo "Starting build for branch: ${env.BRANCH_NAME}"
                }
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    pipelineScript.buildApp()
                    pipelineScript.testApp()
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    pipelineScript.pushToDockerHub("${FULL_IMAGE_NAME}", 'docker-hub')
                }
            }
        }

        stage('Push to Nexus') {
            steps {
                script {
                    pipelineScript.pushToNexus("${FULL_IMAGE_NAME}", "${NEXUS_URL}", 'nexus-credentials')
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    pipelineScript.deployApp()
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline finished."
        }
    }
}