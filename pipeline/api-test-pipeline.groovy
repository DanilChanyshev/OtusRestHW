node('api-test-runner') {

    currentBuild.description = "<p style='color: blue;'>API tests</p>"

    stage('Checkout') {
        checkout scm
    }

    stage('Run API tests') {
        dir("${env.WORKSPACE}") {
            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                sh "ansible/run_api_tests.yml"
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

}