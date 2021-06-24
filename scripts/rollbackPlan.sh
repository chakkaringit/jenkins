status=`./kubectl -n ${1} get pods -lapp=email-alert-master-pod`
echo "=== Check pod status ${status}"
