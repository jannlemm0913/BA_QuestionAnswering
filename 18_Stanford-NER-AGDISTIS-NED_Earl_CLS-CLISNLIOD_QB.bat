echo "Pipeline 18_Stanford-NER-AGDISTIS-NED_Earl_CLS-CLISNLIOD_QB"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NER-stanford\target && java -Xmx1G -jar qa.NER-Standford-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-AGDISTIS\target && java -jar qa.NED-AGDISTIS-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-Earl-RelationLinking\target && java -jar qa.qanary_component-Earl-RelationLinking-0.0.1.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-CLS-CLSNLIOD\target && java -jar qanary_component-CLS-CLSNLIOD-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-QueryBuilder\target && java -jar qa.qanary_component-QueryBuilder-1.0.2.jar"