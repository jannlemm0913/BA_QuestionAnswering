start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NER-stanford\target && java -Xmx1G -jar qa.NER-Standford-1.0.0.jar"
TIMEOUT 5
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-NED-AGDISTIS\target && java -jar qa.NED-AGDISTIS-1.0.0.jar"
TIMEOUT 5
::start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-REL-RELNLIOD\target && java -jar qanary_component-REL-RELNLIOD-1.0.1.jar"
::start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-DiambiguationProperty-OKBQA\target && java -jar qa.qanary_component-DiambiguationProperty-OKBQA-1.0.1.jar"
::start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-CLS-CLSNLIOD\target && java -jar qanary_component-CLS-CLSNLIOD-1.0.0.jar"
start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-DiambiguationClass-OKBQA\target && java -jar qa.qanary_component-DiambiguationClass-OKBQA-1.0.0.jar"
TIMEOUT 5
::start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qa.qanary_component-QueryBuilder\target && java -jar qa.qanary_component-QueryBuilder-1.0.2.jar"
::start cmd /k "cd %BA_HOME%\BA\Qanary-question-answering-components\qanary_component-QB-Sina\target && java -jar qanary_component-QB-Sina-1.0.0.jar"