package eu.wdaqua.qanary.spotlightNERD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import eu.wdaqua.qanary.commons.QanaryMessage;
import eu.wdaqua.qanary.commons.QanaryQuestion;
import eu.wdaqua.qanary.commons.QanaryUtils;
import eu.wdaqua.qanary.component.QanaryComponent;

import java.lang.ProcessBuilder;

/**
 * combines NER and NED Spotlight to reduce number of requests to API
 *
 * @author jannlemm0913, silvanknecht
 */

@Component
public class DBpediaSpotlightNERD extends QanaryComponent {
    private static final Logger logger = LoggerFactory.getLogger(DBpediaSpotlightNERD.class);
    private String service = "http://api.dbpedia-spotlight.org/en/annotate/";

    public QanaryMessage process(QanaryMessage myQanaryMessage) throws Exception {
        logger.info("process: {}", myQanaryMessage);
        long startTime = System.currentTimeMillis();
        //STEP 1: Retrieve the information needed for the computations

        // retrieve the question
        QanaryUtils myQanaryUtils = this.getUtils(myQanaryMessage);
        QanaryQuestion<String> myQanaryQuestion = this.getQanaryQuestion(myQanaryMessage);
        String myQuestion = myQanaryQuestion.getTextualRepresentation();
        logger.info("Question: {}", myQuestion);

        // STEP2: Call the DBpedia service
        logger.info("==== Calling the DBpedia service at {}",service);
        RestTemplate myRestTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //Set Body
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("text", myQuestion);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        String response = myRestTemplate.postForObject(service, request, String.class);
        // Now the output of DBPediaNED, which is JSON, is parsed below to
        // fetch the corresponding URIs
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response);
        String jsonString = json.toString();
        JSONArray arr = (JSONArray) json.get("Resources");
        logger.info("==== Resources: {}",arr);

        ArrayList<Link> links = new ArrayList<Link>();
        int cnt = 0;
        if (arr != null) {
            Iterator i = arr.iterator();
            while (i.hasNext()) {
                JSONObject obj = (JSONObject) i.next();
                String uri = (String) obj.get("@URI");
                String offset = (String) obj.get("@offset");
                String surfaceForm = (String) obj.get("@surfaceForm");

                Link link = new Link();
                link.link = uri;
                link.begin = Integer.parseInt(offset);
                link.end = (surfaceForm.length() + Integer.parseInt(offset));
                logger.info("Spot start {}, end {}", link.begin, link.end);
                logger.info("recognized: {} at ({},{})", link.link, link.begin, link.end);
                links.add(link);

                cnt++;
            }
        }

        if (cnt == 0) {
            logger.warn("nothing recognized for \"{}\": {}", myQuestion, json);
        } else {
            logger.info("recognized {} entities: {}", cnt, json);
        }

        logger.debug("Apply commons alignment on outgraph.");

        // STEP3: Push the result of the component to the triplestore

        // TODO: prevent that duplicate entries are created within the
        // triplestore, here the same data is added as already exit (see
        // previous SELECT query)
        for (Link l : links) {
            String sparql = "PREFIX qa: <http://www.wdaqua.eu/qa#> " //
                    + "PREFIX oa: <http://www.w3.org/ns/openannotation/core/> " //
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " //
                    + "INSERT { " + "GRAPH <" + myQanaryQuestion.getOutGraph() + "> { " //
                    + "  ?a a qa:AnnotationOfInstance . " //
                    + "  ?a oa:hasTarget [ " //
                    + "           a    oa:SpecificResource; " //
                    + "           oa:hasSource    <" + myQanaryQuestion.getUri() + ">; " //
                    + "           oa:hasSelector  [ " //
                    + "                    a oa:TextPositionSelector ; " //
                    + "                    oa:start \"" + l.begin + "\"^^xsd:nonNegativeInteger ; " //
                    + "                    oa:end  \"" + l.end + "\"^^xsd:nonNegativeInteger  " //
                    + "           ] " //
                    + "  ] . " //
                    + "  ?a oa:hasBody <" + l.link + "> ;" //
                    + "     oa:annotatedBy <https://github.com/dbpedia-spotlight/dbpedia-spotlight> ; " //
                    + "	    oa:AnnotatedAt ?time  " + "}} " //
                    + "WHERE { " //
                    + "  BIND (IRI(str(RAND())) AS ?a) ."//
                    + "  BIND (now() as ?time) " //
                    + "}";
            logger.debug("Sparql query: {}", sparql);
            myQanaryUtils.updateTripleStore(sparql, myQanaryMessage.getEndpoint().toString());
        }
        long estimatedTime = System.currentTimeMillis() - startTime;
        logger.info("Time {}", estimatedTime);

        return myQanaryMessage;
    }

    class Link {
        public int begin;
        public int end;
        public String link;
    }

}
