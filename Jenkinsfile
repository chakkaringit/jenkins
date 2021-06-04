pipeline {
  agent {
    kubernetes {
      	cloud 'kubernetes'
      }
    }
  stages {
    stage('Build') {
      steps {  // no container directive is needed as the maven container is the default
        sh "kubectl get nodes"   
      }
    }
    
  }
}
