echo "Pipeline 6_DBpedia-Spotlight-NERD_RNLIWOD_CLS-CLISNLIOD_QB"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NERD-DBpedia-Spotlight\target && java -jar qa.NERD-DBpedia-Spotlight-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-RELNLIOD\target && java -jar qanary_component-REL-RELNLIOD-1.0.1.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-CLS-CLSNLIOD\target && java -jar qanary_component-CLS-CLSNLIOD-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-QueryBuilder\target && java -jar qa.qanary_component-QueryBuilder-1.0.2.jar"

