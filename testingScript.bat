start cmd /k "cd %STARDOG_HOME% & stardog-admin server start --web-console"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary\qanary_pipeline-template\target && java -jar qa.pipeline-1.1.2.jar"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-DBpedia-Spotlight\target && java -jar qa.NED-DBpedia-Spotlight-1.0.0.jar"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NER-DBpedia-Spotlight\target && java -jar qa.NER-DBpedia-Spotlight-1.0.0.jar"