// ============================================================
// Jenkinsfile — resto-pizza-web
// Pipeline CI/CD : Clean → Checkout → Build/Test → Allure
//                → Docker Build → Docker Push (main only)
// Jenkins en conteneur (DooD) — Java 25 / Spring Boot 4.0.5
// Tag Docker = SHA Git court (traçabilité commit ↔ image)
// ============================================================
pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        DOCKER_IMAGE       = "julitox/resto-pizza-web"
        DOCKER_CREDENTIALS = 'dockerhub-credentials'
    }

    stages {

        // ─────────────────────────────────────────────────────
        // 1. Nettoyage du workspace
        //    Garantit un environnement reproductible à chaque run
        //    Principe 12 facteurs : parité dev/prod
        // ─────────────────────────────────────────────────────
        stage('🧹 Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        // ─────────────────────────────────────────────────────
        // 2. Récupération du code source
        //    checkout scm : utilise la config SCM du pipeline
        //    Jenkins (URL + branche) → pas d'URL hardcodée
        //    env.DOCKER_TAG est défini ici car GIT_COMMIT
        //    n'est disponible qu'APRÈS le checkout
        // ─────────────────────────────────────────────────────
        stage('📥 Checkout') {
            steps {
                checkout scm
                script {
                    // SHA court du commit courant (7 caractères)
                    // Fallback sur BUILD_NUMBER si GIT_COMMIT est absent
                    env.DOCKER_TAG = env.GIT_COMMIT
                        ? env.GIT_COMMIT[0..6]
                        : env.BUILD_NUMBER
                }
            }
        }

        // ─────────────────────────────────────────────────────
        // 3. Build Maven + Tests
        //    mvn clean verify :
        //      - compile le projet
        //      - lance les tests (JUnit 5 + Allure JUnit5)
        //      - génère target/allure-results/*.json
        //      - génère target/surefire-reports/*.xml
        //    Les tests Allure et JUnit sont générés en même temps
        // ─────────────────────────────────────────────────────
        stage('🔨 Build & Tests') {
            steps {
                sh 'mvn clean verify --no-transfer-progress'
            }
            post {
                always {
                    // Barre de tendance JUnit dans Jenkins (verte/rouge)
                    // ** : compatible avec les projets Maven multi-modules
                    junit allowEmptyResults: true,
                          testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        // ─────────────────────────────────────────────────────
        // 4. Construction de l'image Docker
        //    Deux tags simultanés :
        //      - :latest       → toujours la dernière version
        //      - :abc1234      → SHA Git court (traçabilité)
        //    On peut retrouver l'image d'un commit précis avec
        //    : docker pull julitox/resto-pizza-web:abc1234
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
        // 5. Push sur Docker Hub
        //    when : ne s'exécute QUE sur la branche main
        //    → les branches feature sont buildées/testées
        //      mais leurs images ne sont pas poussées
        //    Sécurité : \$ (backslash dollar) empêche Groovy
        //    d'interpoler le token → jamais visible dans les logs
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
    // POST — exécuté après tous les stages, peu importe le statut
    // ─────────────────────────────────────────────────────────
    post {
        always {
            // Rapport Allure HTML — généré depuis target/allure-results/
            // Visible dans : icône Allure sur le build + lien "Allure Report"
            // ALWAYS : rapport généré même si les tests échouent
            allure([
                includeProperties: false,
                jdk              : '',
                properties       : [],
                reportBuildPolicy: 'ALWAYS',
                results          : [[path: 'target/allure-results']]
            ])

            // Nettoyage des images Docker locales (libère l'espace Jenkins)
            // || true : ne fait pas échouer le pipeline si l'image n'existe pas
            sh "docker rmi ${DOCKER_IMAGE}:${env.DOCKER_TAG} || true"
            sh "docker rmi ${DOCKER_IMAGE}:latest || true"
        }

        success {
            echo "✅ Pipeline SUCCESS — ${DOCKER_IMAGE}:${env.DOCKER_TAG} poussée sur Docker Hub"
        }

        failure {
            echo "❌ Pipeline FAILURE — Consulter les logs et le rapport Allure ci-dessus"
        }
    }
}