pipeline {
  
  agent any
  
  environment {
    namespace = 'ns-volt-dev-v1'
  }
  
  parameters {
    string(name: 'IMAGE_NAME', defaultValue: '', description: 'Enter the image name')
    string(name: 'IMAGE_TAG', defaultValue: '', description: 'Enter the image tag version')
 }
  
  stages {
    
    stage('Pull docker image') {
      steps{
        script { 
          docker.withTool('DockerCLI'){
            docker.withRegistry('', 'dockerhub-credential') {
              def image = docker.image('knowesis/sift-core:4.0.1-SCB')
              image.pull()
            }
          }
        }
      }
    }
    
    stage('Check pods status') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
          sh 'chmod u+x ./kubectl'  
          sh './kubectl -n ns-volt-dev-v1 get pods'
        }
      }
    }
    
    stage('Deploy service to kubernetes') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh './kubectl -n ns-volt-dev-v1 apply -f values/deployment.yaml'
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
