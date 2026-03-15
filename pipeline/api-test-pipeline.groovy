node('api-test-runner') {

    currentBuild.description = "<p style='color: blue;'>API tests</p>"

    stage('Checkout') {
        checkout scm
    }

    stage('Build') {
        sh "mvn clean compile"
    }

    stage('Run API tests') {
        sh "mvn clean test"
    }

    stage('Publish results') {
        junit 'target/surefire-reports/*.xml'
    }

}