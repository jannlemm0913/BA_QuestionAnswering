# BA_QuestionAnswering
## Introduction
This repository was created for our bachelor thesis where we tried to evaluate QA pipelines. The pipelines consist of [Qanary](https://github.com/WDAqua/Qanary) [components](https://github.com/WDAqua/Qanary-question-answering-components). We have updated the components with active APIs and fixed some bugs. There are two new components (EARL Relation Linker and SINA Query Builder [1][2]) that we were allowed to make publicly available.

## How to use
Clone this repository. Make sure you have [Stardog](https://www.stardog.com/) installed, we used version 6 during our work. Versions older than 5 will not work. We have used two environment variables to keep our scripts flexible. 
`%STARDOG_HOME%` is used for Stardog as its working directory, we use that variable in our scripts aswell. `%BA_HOME%` is the path to where you cloned this repository. 

Build all the needed [components](https://github.com/jannlemm0913/BA_QuestionAnswering/tree/master/Qanary-question-answering-components) and the [pipeline](https://github.com/jannlemm0913/BA_QuestionAnswering/tree/master/Qanary/qanary_pipeline-template) with `mvn install -DskipDockerBuild`. This will create a `target` folder in the component's folder or pipeline's folder. We have not used Docker in our work and cannot say if the components will work via Docker instances. This [issue](https://github.com/WDAqua/Qanary/issues/20) from the Qanary repository may help implementing the components with Docker.

To run Stardog and the pipeline (no components yet), use [StardogPipeline.bat](https://github.com/jannlemm0913/BA_QuestionAnswering/blob/master/StardogPipeline.bat), if you want to use the web console in Stardog to query the triplesore, use [StardogWithWebConsolePipeline.bat](https://github.com/jannlemm0913/BA_QuestionAnswering/blob/master/StardogWithWebConsolePipeline.bat). This might take a short while until everything is ready to use.

Then you can either use any of the scripts to start a pipeline (e.g. [9_Tagme-NED_Earl_CLS-CLISNLIOD_SINA.bat](https://github.com/jannlemm0913/BA_QuestionAnswering/blob/master/9_Tagme-NED_Earl_CLS-CLISNLIOD_SINA.bat) ) or you can start the components from their `target` folder which was created during the build process. Make sure you start from the `target` folder, as there are relative paths in the components. 

When the components have registered themselves to the pipeline, go to [localhost:8080/#/overview](http://localhost:8080/#/overview) to see a Spring overview of the pipeline. Here you can see logs and whether the component is online or offline. Go to [localhost:8080/startquestionansweringwithtextquestion](http://localhost:8080/startquestionansweringwithtextquestion) to test the pipeline with a question of your choice. Select and drag the components so you have a useful pipeline.

## Evaluation
A big part of our work was evaluating the different pipelines with QALD-9 [3]. To evaluate and compare the pipelines, we have developed an [evaluator](https://github.com/silvanknecht/QA_Evaluator). 

## Authors
* [Jann Lemm](https://github.com/jannlemm0913/)
* [Silvan Knecht](https://github.com/silvanknecht/)

## Thanks to
* The [Qanary](https://github.com/WDAqua/Qanary) team
* The [GERBIL](https://github.com/dice-group/gerbil) team
* The [EARL](https://github.com/AskNowQA/EARL/issues) team


## Citations
<ul>
<li>
<p>[1] Kuldeep Singh, Arun Sethupat Radhakrishna, Andreas Both, Saeedeh Shekarpour, Ioanna Lytra, Ricardo Usbeck, Akhilesh Vyas, Akmal Khikmatullaev, Dharmen Punjani, Christoph Lange, Maria-Esther Vidal, Jens Lehmann, Sören Auer:
Why Reinvent the Wheel: Let's Build Question Answering Systems Together. WWW 2018: 1247-1256
</li>
<li>
<p>[2] Kuldeep Singh, Andreas Both, Arun Sethupat Radhakrishna, Saeedeh Shekarpour:
Frankenstein: A Platform Enabling Reuse of Question Answering Components. ESWC 2018: 624-638
</p>
</li>
<li>
<p>[3] R. Usbeck, R. H. Gusmita, M. Saleem, A.-C. Ngonga Ngomo: 9th Challenge on Question Answering over Linked Data (QALD-9), 2018.
</p>
</li>
</ul>
