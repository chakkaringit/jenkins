pipeline {
  stage('Deploy'){
      kubernetesDeploy configs: 'values/deployment.yaml', 
                       kubeconfigId: 'kubernate-cluster'
  }
}
