pipeline {
  agent any
  stage('Deploy'){
      kubernetesDeploy configs: 'values/deployment.yaml', 
                       kubeconfigId: 'kubernate-cluster'
  }
}
