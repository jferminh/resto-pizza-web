// ============================================================
// Jenkinsfile — resto-pizza-web
// Pipeline CI/CD : Clean → Checkout → Build/Test → Allure
//                → Docker Build → Docker Push (main only)
// Java 25 / Spring Boot 4.0.5 — Allure 2.34.0
// ============================================================
pipeline {
    agent any

    options {
        // Désactive le checkout automatique Jenkins
        // → évite le double clone après cleanWs()
        skipDefaultCheckout(true)
    }

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE       = "julitox/resto-pizza-web"
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
    }

    stages {

        // ─────────────────────────────────────────────────────
        // 1. Nettoyage du workspace — environnement reproductible
        // ─────────────────────────────────────────────────────
        stage('🧹 Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        // ─────────────────────────────────────────────────────
        // 2. Checkout — après cleanWs(), on clone une seule fois
        //    env.DOCKER_TAG défini ici car GIT_COMMIT n'existe
        //    qu'après le checkout (pas disponible dans environment{})
        // ─────────────────────────────────────────────────────
        stage('📥 Checkout') {
            steps {
                checkout scm
                script {
                    env.DOCKER_TAG = env.GIT_COMMIT
                        ? env.GIT_COMMIT[0..6]
                        : env.BUILD_NUMBER
                }
            }
        }

        // ─────────────────────────────────────────────────────
        // 3. Build + Tests
        //    allure-junit5 génère target/allure-results/*.json
        //    via le ServiceLoader JUnit 5 — sans plugin Maven
        // ─────────────────────────────────────────────────────
        stage('🔨 Build & Tests') {
            steps {
                sh 'mvn clean verify --no-transfer-progress'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        // ─────────────────────────────────────────────────────
        // 4. Docker Build — deux tags simultanés
        //    :latest       → toujours la version courante
        //    :abc1234      → SHA Git = traçabilité commit ↔ image
        // ─────────────────────────────────────────────────────
        stage('🐳 Docker Build') {
            steps {
                sh """
                    docker build \\
                        -t ${DOCKER_IMAGE}:latest \\
                        -t ${DOCKER_IMAGE}:${env.DOCKER_TAG} \\
                        .
                """
            }
        }

        // ─────────────────────────────────────────────────────
        // 5. Docker Push — uniquement sur la branche main
        //    \$DOCKER_TOKEN : backslash empêche l'interpolation
        //    Groovy → le token n'apparaît jamais dans les logs
        // ─────────────────────────────────────────────────────
        stage('🚀 Docker Push') {
            when {
                expression { env.GIT_BRANCH == 'origin/main' }
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
                        docker push ${DOCKER_IMAGE}:${env.DOCKER_TAG}
                        docker logout
                    """
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    // POST — rapport Allure généré TOUJOURS, même en cas d'échec
    // Le plugin Jenkins lit target/allure-results/ directement
    // ─────────────────────────────────────────────────────────
    post {
        always {
            allure([
                includeProperties: false,
                jdk              : '',
                properties       : [],
                reportBuildPolicy: 'ALWAYS',
                results          : [[path: 'target/allure-results']]
            ])

            // Nettoyage de l'image taguée par SHA uniquement
            // (pas :latest — peut être utilisée par un conteneur actif)
            sh "docker rmi ${DOCKER_IMAGE}:${env.DOCKER_TAG} || true"
        }

        success {
            echo "✅ Pipeline SUCCESS — ${DOCKER_IMAGE}:${env.DOCKER_TAG} poussée sur Docker Hub"
        }

        failure {
            echo "❌ Pipeline FAILURE — Consulter les logs et le rapport Allure ci-dessus"
        }
    }
}