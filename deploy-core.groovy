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
    
    stage('Check pods status') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
          sh 'chmod u+x ./kubectl'  
          sh './kubectl -n ns-volt-dev-v1 get pods'
        }
      }
    }
    
    stage('read') {
      steps {
        script {
          def data = readFile(file: 'values/deployment.yaml')
          def after = data.replaceAll("IMAGE_NAME","${params.IMAGE_NAME}").replaceAll("IMAGE_TAG","${params.IMAGE_TAG}")
          println(data)
          println(after)
         }
      }
    }
    
    stage('Deploy service to kubernetes') {
      steps{
         withKubeConfig([credentialsId: 'kubernate-cluster']) {
          sh 'cat values/deployment.yaml |  sed 's/IMAGE_NAME/${params.IMAGE_NAME}/g' | ./kubectl -n ns-volt-dev-v1 apply -f -'
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
