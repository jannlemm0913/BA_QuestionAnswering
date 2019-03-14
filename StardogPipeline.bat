start cmd /k "cd %STARDOG_HOME% & stardog-admin server start"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary\qanary_pipeline-template\target && java -jar qa.pipeline-1.1.2.jar"
TIMEOUT 5
