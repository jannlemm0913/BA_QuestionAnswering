echo "Pipeline 23_Stanford-NER-AGDISTIS-NED_RNLIWOD_OKBAQA-DM-CLS_SINA"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NER-stanford\target && java -Xmx1G -jar qa.NER-Standford-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-AGDISTIS\target && java -jar qa.NED-AGDISTIS-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-RELNLIOD\target && java -jar qanary_component-REL-RELNLIOD-1.0.1.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-DiambiguationClass-OKBQA\target && java -jar qa.qanary_component-DiambiguationClass-OKBQA-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-QB-Sina\target && java -jar qanary_component-QB-Sina-1.0.0.jar"