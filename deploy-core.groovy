pipeline {
  
  agent any
  
  environment {
    namespace = 'ns-volt-dev-v1'
  }
  
  stages {
    stage('Check pods status') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
          sh 'chmod u+x ./kubectl'  
          sh './kubectl -n ${env.namespace} get pods'
        }
      }
    }
    stage('Deploy service to kubernetes') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh './kubectl -n ${env.namespace} apply -f values/deployment.yaml'
        }
      }
    }
  }
  
}
