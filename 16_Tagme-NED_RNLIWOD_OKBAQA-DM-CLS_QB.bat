echo "Pipeline 16_Tagme-NED_RNLIWOD_OKBAQA-DM-CLS_QB"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-tagme\target && java -jar qanary_component-NED-tagme-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-RELNLIOD\target && java -jar qanary_component-REL-RELNLIOD-1.0.1.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-DiambiguationClass-OKBQA\target && java -jar qa.qanary_component-DiambiguationClass-OKBQA-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-QueryBuilder\target && java -jar qa.qanary_component-QueryBuilder-1.0.2.jar"
