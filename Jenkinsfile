pipeline {
  agent any
  stages {
    stage('Deploy'){
      steps { 
        kubernetesDeploy configs: 'values/deployment.yaml', 
                         kubeconfigId: 'kubernate-cluster'
      }
    }
  }
}
