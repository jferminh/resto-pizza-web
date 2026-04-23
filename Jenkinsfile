pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "jferminh/resto-pizza-web"
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
        JAVA_VERSION = '25'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify --no-transfer-progress'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:latest -t ${DOCKER_IMAGE}:${env.GIT_COMMIT[0..6]} ."
            }
        }

        stage('Docker Push') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: DOCKER_CREDENTIALS,
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_TOKEN'
                )]) {
                    sh """
                        echo \$DOCKER_TOKEN | docker login -u \$DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE}:latest
                        docker push ${DOCKER_IMAGE}:${env.GIT_COMMIT[0..6]}
                        docker logout
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline resto-pizza-web terminé avec succès !'
        }
        failure {
            echo '❌ Pipeline échoué — vérifier les logs ci-dessus.'
        }
    }
}