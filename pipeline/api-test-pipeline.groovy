node('api-test-runner') {

    currentBuild.description = "<p style='color: blue;'>API tests</p>"

    stage('Checkout') {
        checkout scm
    }

    stage('Run API tests') {
        dir("${env.WORKSPACE}") {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                sh "ansible-playbook -i playbook/inventory/hosts playbook/run_api_test.yaml -e \"workspace=\$(pwd)\""
            }
        }
    }

    stage('Publish results') {
        junit 'target/surefire-reports/*.xml'
    }

    stage('allure publish') {
        allure ([
                includeProperties: false,
                jdk: '',
                properties: [],
                reportBuildPolicy: 'ALWAYS',
                results: [[path: 'target/allure-results']]
        ])
    }

    stage('Archive allure results') {
        archiveArtifacts artifacts: 'target/allure-results/**',
                allowEmptyArchive: true,
                fingerprint: true
    }
}
