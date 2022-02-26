def mainDir="/var/jenkins_home/workspace/test-pipeline"
def repository="testing"
def nickName="portfolio"

/* pipeline stage */
pipeline{
    /* job execute server setting */
    agent any
    /* job staging */
    stages{
        // get git repository
        stage("Pull Codes"){
            steps{
                 // checkout branch master
                checkout scm
            }
        }
        // get service config file
        stage("Get Configuration"){
            steps{
                sh"""
                #!/bin/bash
                echo 'get configuration'
                """
            }
        }
        // build test
        stage("Test Codes"){
            steps{
                sh """
                #!/bin/bash
                echo 'Testing '
                ./mvnw clean test
                """
            }
        }
        // build maven file
        stage("Build Codes"){
        steps{
            sh """
                #!/bin/bash
                echo 'Testing build'
                ./mvnw clean package
                """
            }
        }
        // step docker build
        // step docker server send
        // deploy spring boot
        stage("Deploy Spring boot"){
            steps{
                sh """
                    java -jar portfolioIoT-0.0.1-SNAPSHOT.jar
                """
            }
        }

    }
}