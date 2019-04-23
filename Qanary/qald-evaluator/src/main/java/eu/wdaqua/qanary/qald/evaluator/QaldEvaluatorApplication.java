package eu.wdaqua.qanary.qald.evaluator;

import eu.wdaqua.qanary.qald.evaluator.qaldreader.FileReader;
import eu.wdaqua.qanary.qald.evaluator.qaldreader.QaldQuestion;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//added imports
import org.json.*;
import java.io.PrintWriter;
import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.Pattern;

/**
 * start the spring application
 *
 * @author AnBo
 */
@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class QaldEvaluatorApplication {
    private static final Logger logger = LoggerFactory.getLogger(QaldEvaluatorApplication.class);

    String uriServer = "http://localhost:8080/startquestionansweringwithtextquestion";

    // prepare data to print to csv file
    List<String[]> dataLines = new ArrayList<>();

    private void process(String components, int maxQuestionsToBeProcessed)
            throws UnsupportedEncodingException, IOException {
        Double globalPrecision = 0.0;
        Double globalRecall = 0.0;
        Double globalFMeasure = 0.0;
        int count = 0;
        
        // add header to csv file
        dataLines.add(new String[] {"QuestionID","QuestionString", "ResourceURLs", "PropertyURLs", "OntologyURLs", "SPARQLQuery", "NrExpected","NrSystem","NrCorrect"});

        ArrayList<Integer> fullRecall = new ArrayList<Integer>();
        ArrayList<Integer> fullFMeasure = new ArrayList<Integer>();

        String uriServer = "http://localhost:8080/startquestionansweringwithtextquestion";

        FileReader filereader = new FileReader();

        filereader.getQuestion(1).getUris();

        // send to pipeline
        List<QaldQuestion> questions = new LinkedList<>(filereader.getQuestions());

        for (int i = 0; i < questions.size(); i++) {
            // Test auf Entitäten
            //List<String> expectedAnswers = questions.get(i).getResourceUrisAsString();
            // Test auf Antworten, nicht auf Entitäten
            List<String> expectedAnswers = questions.get(i).getAnswersAsString();
            logger.info("{}. Question: {}", questions.get(i).getQaldId(), questions.get(i).getQuestion());

            String questionString = questions.get(i).getQuestion();
            Integer questionId = questions.get(i).getQaldId();

            // Send the question 
            RestTemplate restTemplate = new RestTemplate();
            UriComponentsBuilder service = UriComponentsBuilder.fromHttpUrl(uriServer);

            MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
            bodyMap.add("question", questions.get(i).getQuestion());
            bodyMap.add("componentlist[]", components);
            String response = restTemplate.postForObject(service.build().encode().toUri(), bodyMap, String.class);
            logger.info("Response pipeline: {}", response);

            // Retrieve the computed uris
            JSONObject responseJson = new JSONObject(response);
            String endpoint = responseJson.getString("endpoint").concat("/query");  // Stardog v5 changes
            String namedGraph = responseJson.getString("outGraph"); // statt graph neu outGraph... ffs
            logger.debug("{}. named graph: {}", questions.get(i).getQaldId(), namedGraph);
            /*String sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " //
                    + "SELECT ?uri { " //
                    + "  GRAPH <" + namedGraph + "> { " //
                    + "    ?a a qa:AnnotationOfInstance . " //
                    + "    ?a oa:hasBody ?uri " //
                    + "} }";
            */
            //custom sparql (jannlemm0913) to get answers instead of entities
            String sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "SELECT ?body FROM <" + namedGraph + "> { " //
                    + "    ?s oa:hasBody ?body .  " //
                    + "    ?s rdf:type qa:AnnotationOfAnswerJSON . " //
                    + "}"; // custom sparql finished

            logger.debug("SPARQL: {}", sparql);
            ResultSet r = selectTripleStore(sparql, endpoint);
            List<String> systemAnswers = new ArrayList<String>();
           /*
            while (r.hasNext()) {
                QuerySolution s = r.next();
                if (s.getResource("uri") != null && !s.getResource("uri").toString().endsWith("null")) {
                    logger.info("System answers: {} ", s.getResource("uri").toString());
                    systemAnswers.add(s.getResource("uri").toString());
                }
            }
            */
            //custom result handling to get answer uris from string
            while (r.hasNext()) {
                QuerySolution s = r.next();
                if (s.getLiteral("body") != null && !s.getLiteral("body").toString().endsWith("null")) {
                    JSONObject bodyJson = new JSONObject(s.getLiteral("body"));
                    logger.info(bodyJson.toString());
                    try {
                        // dateityp aus head vars auslesen todo
                        JSONObject testJson = new JSONObject(bodyJson.getString("value"));
                        logger.info("==== Got the value");
                        JSONObject headJson = testJson.getJSONObject("head");
                        logger.info("==== head {}", headJson);
                        String typeJson = headJson.getJSONArray("vars").get(0).toString();
                        logger.info("==== type {}", typeJson);
                        JSONArray bindingsJson = testJson.getJSONObject("results").getJSONArray("bindings");
                        logger.info("{}",testJson.getJSONObject("results"));
                        logger.info("==== Got the bindings");
                        if(bindingsJson != null) {
                            for(int j = 0; j < bindingsJson.length(); j++) {
                                JSONObject test2Json = bindingsJson.getJSONObject(j);
                                String answerUri = test2Json.getJSONObject(typeJson).getString("value");
                                logger.info("System answers: {} ", answerUri);
                                systemAnswers.add(answerUri);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("==== Not a JSON Object in value!");
                    }
                }
            }

            // Retrieve the expected resources from the SPARQL query
            //List<String> expectedAnswers = questions.get(i).getResourceUrisAsString();
            for (String expected : expectedAnswers) {
                logger.info("Expected answers: {} ", expected);
            }

            // Compute precision and recall
            Metrics m = new Metrics();
            int correctlyAnswered = m.compute(expectedAnswers, systemAnswers);
            globalPrecision += m.precision;
            globalRecall += m.recall;
            globalFMeasure += m.fMeasure;
            count++;

            if (m.recall == 1) {
                fullRecall.add(1);
            } else {
                fullRecall.add(0);
            }

            // get entities from kb by sparql
            sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " //
                    + "SELECT ?uri { " //
                    + "  GRAPH <" + namedGraph + "> { " //
                    + "    ?a a qa:AnnotationOfInstance . " //
                    + "    ?a oa:hasBody ?uri " //
                    + "} }";
            ResultSet rs = selectTripleStore(sparql, endpoint);
            List<String> resourceUris = new ArrayList<String>();
            
            while (rs.hasNext()) {
                QuerySolution s = rs.next();
                if (s.getResource("uri") != null && !s.getResource("uri").toString().endsWith("null")) {
                    resourceUris.add(s.getResource("uri").toString());
                }
            }

            // get properties from kb by sparql
            sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " //
                    + "SELECT ?uri { " //
                    + "  GRAPH <" + namedGraph + "> { " //
                    + "    ?a a qa:AnnotationOfRelation . " //
                    + "    ?a oa:hasBody ?uri " //
                    + "} }";
            ResultSet rss = selectTripleStore(sparql, endpoint);
            List<String> propertyUris = new ArrayList<String>();
            
            while (rss.hasNext()) {
                QuerySolution s = rss.next();
                if(Pattern.matches(".*dbpedia\\.org.*",s.getResource("uri").getURI())) {
                    if (s.getResource("uri") != null && !s.getResource("uri").toString().endsWith("null")) {
                        propertyUris.add(s.getResource("uri").toString());
                    }
                }
            }

            // get ontologies from kb by sparql
            sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " //
                    + "SELECT ?uri { " //
                    + "  GRAPH <" + namedGraph + "> { " //
                    + "    ?a a qa:AnnotationOfClass . " //
                    + "    ?a oa:hasBody ?uri " //
                    + "} }";
            ResultSet rsss = selectTripleStore(sparql, endpoint);
            List<String> ontologyUris = new ArrayList<String>();
            
            while (rsss.hasNext()) {
                QuerySolution s = rsss.next();
                if(Pattern.matches(".*dbpedia\\.org.*",s.getResource("uri").getURI())) {
                    if (s.getResource("uri") != null && !s.getResource("uri").toString().endsWith("null")) {
                        ontologyUris.add(s.getResource("uri").toString());
                    }
                }
            }

            // get the query from kb by sparql
            sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " //
                    + "SELECT ?body { " //
                    + "  GRAPH <" + namedGraph + "> { " //
                    + "    ?a a qa:AnnotationOfAnswerSPARQL . " //
                    + "    ?a oa:hasBody ?body " //
                    + "} }";
            ResultSet rssss = selectTripleStore(sparql, endpoint);
            List<String> sparqlQueries = new ArrayList<String>();
            
            while (rssss.hasNext()) {
                QuerySolution s = rssss.next();
                if (s.getLiteral("body") != null && !s.getLiteral("body").toString().endsWith("null")) {
                    sparqlQueries.add(s.getLiteral("body").toString());
                }
            }

            // add string array to dataLines
            String resourceUrisString = makeStringFromArray(resourceUris);
            String propertyUrisString = makeStringFromArray(propertyUris);
            String ontologyUrisString = makeStringFromArray(ontologyUris);
            String sparqlQueriesString = makeStringFromArray(sparqlQueries);
            dataLines.add(new String[]
            {questionId.toString(), questionString, resourceUrisString, propertyUrisString, ontologyUrisString, sparqlQueriesString, 
                 Integer.toString(expectedAnswers.size()), Integer.toString(systemAnswers.size()), Integer.toString(correctlyAnswered)});
        }

        //print data to csv file
        csvToOutput();

        logger.info("Global Precision={}", globalPrecision / count);
        logger.info("Global Recall={}", globalRecall / count);
        logger.info("Global F-measure={}", globalFMeasure / count);
    }

    // convert data to csv
    public String convertToCSV(String[] data) {
        return Stream.of(data)
            .collect(Collectors.joining("|"));
    }

    public void csvToOutput() throws IOException {
        File csvOutputFile = new File("../../eval-results/21_Stanford-NER-AGDISTIS-NED_RNLIWOD_CLS-CLISNLIOD_SINA.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                .map(this::convertToCSV)
                .forEach(pw::println);
        }
    }

    public String makeStringFromArray(List<String> input) {
        String endprodukt = "";
        for(String s : input) {
            endprodukt += (s + ", ");
        }
        return endprodukt;
    }

    class Metrics {
        private Double precision = 0.0;
        private Double recall = 0.0;
        private Double fMeasure = 0.0;


        public int compute(List<String> expectedAnswers, List<String> systemAnswers) {
            //Compute the number of retrieved answers
            int correctRetrieved = 0;
            for (String s : systemAnswers) {
                if (expectedAnswers.contains(s)) {
                    correctRetrieved++;
                }
            }
            //Compute precision and recall following the evaluation metrics of QALD
            if (expectedAnswers.size() == 0) {
                if (systemAnswers.size() == 0) {
                    recall = 1.0;
                    precision = 1.0;
                    fMeasure = 1.0;
                } else {
                    recall = 0.0;
                    precision = 0.0;
                    fMeasure = 0.0;
                }
            } else {
                if (systemAnswers.size() == 0) {
                    recall = 0.0;
                    precision = 1.0;
                } else {
                    precision = (double) correctRetrieved / systemAnswers.size();
                    recall = (double) correctRetrieved / expectedAnswers.size();
                }
                if (precision == 0 && recall == 0) {
                    fMeasure = 0.0;
                } else {
                    fMeasure = (2 * precision * recall) / (precision + recall);
                }
            }

            return correctRetrieved;
        }
    }

    private ResultSet selectTripleStore(String sparqlQuery, String endpoint) {
        Query query = QueryFactory.create(sparqlQuery);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query);
        return qExe.execSelect();
    }

    public static void main(String... args) throws UnsupportedEncodingException, IOException {

        // TODO:
        int maxQuestions = 350; // 50; has no impact?

        QaldEvaluatorApplication app = new QaldEvaluatorApplication();

        List<String> componentConfigurations = new LinkedList<>();

        List<String> nerComponents = new LinkedList<>();
        List<String> nedComponents = new LinkedList<>();

        // TODO: move to config
        //nerComponents.add("StanfordNER");
        //nerComponents.add("DBpediaSpotlightSpotter");
        //nerComponents.add("FOX");

        // TODO: move to config
        //nedComponents.add("agdistis");
        //nedComponents.add("DBpediaSpotlightNED");

        // monolithic configurations (NER+NED)
        // ====   componentConfigurations.add("Alchemy-NERD");
        //componentConfigurations.add("luceneLinker");

        // create all configurations
        /* ===== for (String ner : nerComponents) {
            for (String ned : nedComponents) {
                componentConfigurations.add(ner + "," + ned);
            }
        } */

        // Test für erste Pipeline
        //componentConfigurations.add("NER-Stanford,NED-AGDISTIS,DiambiguationProperty,ClsNliodCls,QueryBuilder");
        //componentConfigurations.add("NER-Stanford,NED-AGDISTIS,DiambiguationProperty,DiambiguationClass,QueryBuilder");
        //componentConfigurations.add("NER-Stanford,NED-AGDISTIS,RelNliodRel,ClsNliodCls,QueryBuilder");
        //componentConfigurations.add("NERD-DBpediaSpotlight,RelNliodRel,ClsNliodCls,QueryBuilder");
        //componentConfigurations.add("NER-Stanford,NED-AGDISTIS,EarlRelationLinking,DiambiguationClass,SINA");
        componentConfigurations.add("NER-Stanford,NED-AGDISTIS,RelNliodRel,ClsNliodCls,SINA");


        for (String componentConfiguration : componentConfigurations) {
            app.process(componentConfiguration, maxQuestions);
        }
    }
}
