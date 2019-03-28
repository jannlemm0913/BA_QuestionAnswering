echo "Pipeline 1: Stanford-NER-DBpedia-Spotlight-NED_Rematch_CSL_CLSNLIOD_SINA"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NER-stanford\target && java -Xmx1G -jar qa.NER-Standford-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-DBpedia-Spotlight\target && java -jar qa.NED-DBpedia-Spotlight-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-ReMatch\target && java -jar qanary_component-REL-ReMatch-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-CLS-CLSNLIOD\target && java -jar qanary_component-CLS-CLSNLIOD-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-QB-Sina\target && java -jar qanary_component-QB-Sina-1.0.0.jar"
