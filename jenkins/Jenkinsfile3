#!/usr/bin/env groovy

library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'https://github.com/ahmedramzygi/SharedLibrary.git',
         credentialsId: 'jenkins'
        ]
)


def gv

pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage("build jar") {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage("build and push image") {
            steps {
                script {
                    buildImage 'aeramzy9/java-maven-app:1.0.2'
                    dockerLogin()
                    dockerPush 'aeramzy9/java-maven-app:1.0.2'
                }
            }
        }
        stage("deploy") {
            steps {
                script {
                    deployApp()
                }
            }
        }
    }
}
