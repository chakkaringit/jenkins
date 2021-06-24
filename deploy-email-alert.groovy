pipeline {
  
  agent any
  
  environment {
    namespace = 'ns-volt-dev-v1'
    valueFile = "email-alert_deployment.yaml"
  }
  
  parameters {
    string(name: 'IMAGE_NAME', defaultValue: '', description: 'Enter the image name')
    string(name: 'IMAGE_TAG', defaultValue: '', description: 'Enter the image tag version')
 }
  
  stages {
    
    stage('Check pods status') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
          sh 'chmod u+x ./kubectl'  
           sh './kubectl -n ${namespace} get pods'
        }
      }
    }
    
    stage('read') {
      steps {
        script {
          def data = readFile(file: "values/${valueFile}")
          def after = data.replaceAll("IMAGE_NAME","${params.IMAGE_NAME}").replaceAll("IMAGE_TAG","${params.IMAGE_TAG}")
          println(after)
          writeFile(file: "tmp/${valueFile}", text: after)
         }
      }
    }
    
    stage('Deploy service to kubernetes') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
           sh './kubectl -n ${namespace} apply -f tmp/${valueFile}'
        }
      }
    }
  }
  
  post {
    success {
      echo '\033[0;32m *** Job SUCCESSFUL ***'
    }
    unstable {
      echo '\033[0;33m *** Job UNSTABLE ***'
    }
    failure {
      echo '\033[0;33m *** Job FAILURE ***'
    }
  }
  
}
