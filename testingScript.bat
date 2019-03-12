start cmd /k "cd %STARDOG_HOME% & stardog-admin server start --web-console"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary\qanary_pipeline-template\target && java -jar qa.pipeline-1.1.2.jar"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NER-stanford\target && java -jar qa.NER-Standford-1.0.0.jar"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-AGDISTIS\target && java -jar qa.NED-AGDISTIS-1.0.0.jar"
TIMEOUT 5
::start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-RELNLIOD\target && java -jar qanary_component-REL-RELNLIOD-1.0.1.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-CLS-CLSNLIOD\target && java -jar qanary_component-CLS-CLSNLIOD-1.0.0.jar"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-QB-Sina\target && java -jar qanary_component-QB-Sina-1.0.0.jar"