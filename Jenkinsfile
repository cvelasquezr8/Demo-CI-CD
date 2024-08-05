pipeline {
  agent any

  tools {
    jdk 'jdk-11'
    maven 'mvn-3.6.3'
  }

  stages { 
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

     stage('Test Docker') {
      steps {
        sh 'docker --version'
      }
    }


    stage('Test Docker') {
      steps {
        sh 'which docker || echo "Docker not found!"'
        sh 'docker --version'
      }
    }

    stage('Env Variables') {
      steps {
        sh 'env'
      }
    }

    stage('Build') {
      steps {
        withMaven(maven: 'mvn-3.6.3') {
          sh "mvn clean package"
        }
      }
    }

    stage('OWASP Dependency-Check Vulnerabilities') {
      steps {
        withMaven(maven: 'mvn-3.6.3') {
          sh 'mvn dependency-check:check'
        }
        dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
      }
    }

    stage('PMD SpotBugs') {
      steps {
        withMaven(maven: 'mvn-3.6.3') {
          sh 'mvn pmd:pmd pmd:cpd spotbugs:spotbugs'
        }
        recordIssues enabledForFailure: true, tool: spotBugs(pattern: '**/target/spotbugsXml.xml')
        recordIssues enabledForFailure: true, tool: cpd(pattern: '**/target/cpd.xml')
        recordIssues enabledForFailure: true, tool: pmdParser(pattern: '**/target/pmd.xml')
      }
    }

    stage('ZAP') {
      steps {
        withMaven(maven: 'mvn-3.6.3') {
          sh 'mvn zap:analyze'
          publishHTML (target: [
                allowMissing: false,
                alwaysLinkToLastBuild: false,
                keepAll: true,
                reportDir: 'target/zap-reports',
                reportFiles: 'zapReport.html',
                reportName: "ZAP report"
              ])
        }
      }
    }

    stage('SonarQube analysis') {
      environment {
        SONAR_TOKEN = 'squ_eb6d7654812ddd3ca1b0009a01d2e5127a3883fe' // Directamente usando el token en el script
      }
      steps {
        withSonarQubeEnv('sonarqube-server') {
          withMaven(maven: 'mvn-3.6.3') {
            sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN -Dsonar.dependencyCheck.jsonReportPath=target/dependency-check-report.json -Dsonar.dependencyCheck.xmlReportPath=target/dependency-check-report.xml -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html -Dsonar.java.pmd.reportPaths=target/pmd.xml -Dsonar.java.spotbugs.reportPaths=target/spotbugsXml.xml -Dsonar.zaproxy.reportPath=target/zap-reports/zapReport.xml -Dsonar.zaproxy.htmlReportPath=target/zap-reports/zapReport.html'
          }
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          docker.withRegistry('https://registry.hub.docker.com', 'docker-credentials-id') {
            def app = docker.build("myapp:${env.BUILD_ID}")
            app.push('latest')
            app.push("${env.BUILD_ID}")
          }
        }
      }
    }

    stage('Deploy') {
      steps {
        sh 'docker-compose down'
        sh 'docker-compose up --build -d'
      }
    }
  }

  post {
    always {
      cleanWs() // Limpiar el workspace después de la ejecución
    }
  }
}
