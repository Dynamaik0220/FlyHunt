# chmod a+x compileAndExecute_linux.bash # to make file executable

#!/bin/bash
fileToExecute=MainGame #.java (leave out file extension)
dirOfFiles=game/         #remember tailing / slash # leave emty to use containing directory
addToClasspath=lanterna-3.1.0.jar     #leave emty to not interfere with classpath
cd  $(dirname "${BASH_SOURCE[0]}")
if [[ -f $dirOfFiles$fileToExecute.java ]]; then  javac $( if [[ -n "$addToClasspath" ]]; then echo -cp .:$addToClasspath; fi ) $dirOfFiles*.java && suckcess=1 ||  echo "!!!: Compiling no success, returned errors"  ; else echo !!!: Cant find file ./"$dirOfFiles$fileToExecute.java"; fi #compile files
if [[ $suckcess = 1 ]] ; then echo iii: Compiler returned no errors && java $( if [[ -n "$addToClasspath" ]]; then echo -cp .:$addToClasspath ;  fi ) $dirOfFiles$fileToExecute && reset && echo iii: $fileToExecute executed suckcessfully and reset terminal || echo "!!!: Execution returned errors" ;  fi   #executee file (java can amybe handle compiling as well)
