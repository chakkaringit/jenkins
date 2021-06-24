retryCount=0
runningCount=0
rollback=0
statusMessage="with new version"
while true
do
  status=`./kubectl -n ${1} get pods -lapp=${2} --output=jsonpath='{.items[*].status.phase}'`
  echo "Pod status ${status}"
  
  if [ "$status" = "Running" ] && [ ${runningCount} -ge 2 ]
  then
    if [ ${rollback} -eq 1 ]
    then
      statusMessage="with rollback version"
    fi
    
    echo "Service is Running ${statusMessage}"
    break;
  fi
  
  if [ "$status" != "Running" ] && [ ${retryCount} -ge 2 ] && [ ${rollback} -eq 0 ]
  then
    echo "Start rollback process"
    revision=`./kubectl -n ${1} rollout history deployment ${3} | awk '{print $1}' | head -3 | tail -1`
    ./kubectl -n ${1} rollout history deployment ${3} 
    
    echo "Rollback to revision ${revision}"
    ./kubectl -n ${1} rollout undo deployment/${3} --to-revision=${revision}
    
    rollback=`expr ${rollback} + 1`
    runningCount=0
    retryCount=0

  fi
  
  if [ "$status" != "Running" ] && [ ${retryCount} -ge 2 ] && [ ${rollback} -eq 1 ]
  then
    echo "Service is Not Running and Rollback not success!!!"
    break;
  fi
  
  sleep 60
  runningCount=`expr ${runningCount} + 1`
  retryCount=`expr ${retryCount} + 1`
done

