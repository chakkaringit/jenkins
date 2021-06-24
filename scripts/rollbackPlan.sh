retryCount=0
runningCount=0
while true
do
  status=`./kubectl -n ${1} get pods -lapp=${2} --output=jsonpath='{.items[*].status.phase}'`
  echo "Pod status ${status}"
  
  if [ "$status" = "Running" ] && [ ${runningCount} -ge 2 ]
  then
    break;
  fi
  
  if [ "$status" != "Running" ] && [ ${runningCount} -ge 2 ]
  then
    echo "Need to rollback"
    break;
  fi
  
  sleep 5
  runningCount=`expr ${runningCount} + 1`
  retryCount=`expr ${retryCount} + 1`
done

