#!/bin/sh

cd $1
echo $(pwd)
pid_file="$1.pid"

if [ -e "$pid_file" ]
then
  PID=`cat $1.pid`;
  kill $PID
  (rm -f $pid_file)
  echo "$1 has been stopped"

else
  echo "No PID FILE";
fi




