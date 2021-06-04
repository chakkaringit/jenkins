
  node {
  stage('Apply Kubernetes files') {
    withKubeConfig([credentialsId: 'kubernate-cluster', serverUrl: 'https://47.91.41.213:6443']) {
      sh 'kubectl get nodes'
    }
  }
}
