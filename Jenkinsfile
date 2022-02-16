def mainDir="portfolio-IoT"
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
        // build test
        stage("Test Codes"){
            steps{
                sh """
                cd ${mainDir}
                ./mvnw clean test
                """
            }
        }
        // build maven file
        stage("Build Codes"){
        steps{
            sh """
                cd ${mainDir}
                ./mvnw clean package
                """
            }
        }
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