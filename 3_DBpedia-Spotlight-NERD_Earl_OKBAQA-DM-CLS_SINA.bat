echo "Pipeline 3: DBpedia-NERD_Earl_OKBAQA-DM-CLS_SINA"

start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NERD-DBpedia-Spotlight\target && java -jar qa.NERD-DBpedia-Spotlight-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-Earl-RelationLinking\target && java -jar qa.qanary_component-Earl-RelationLinking-0.0.1.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-DiambiguationClass-OKBQA\target && java -jar qa.qanary_component-DiambiguationClass-OKBQA-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-QB-Sina\target && java -jar qanary_component-QB-Sina-1.0.0.jar"