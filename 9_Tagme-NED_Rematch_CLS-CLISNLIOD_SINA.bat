echo "Pipeline 9_Tagme-NED_Rematch_CLS-CLISNLIOD_SINA"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-QueryBuilder\target && java -jar qa.qanary_component-QueryBuilder-1.0.2.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-ReMatch\target && java -jar qanary_component-REL-ReMatch-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-CLS-CLSNLIOD\target && java -jar qanary_component-CLS-CLSNLIOD-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-QB-Sina\target && java -jar qanary_component-QB-Sina-1.0.0.jar"
