package eu.wdaqua.qanary.qald.evaluator.qaldreader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FileReader {

    private HashMap<Integer, QaldQuestion> questions = new HashMap<>();

    public FileReader() throws UnsupportedEncodingException, IOException {
        Reader reader = new InputStreamReader(FileReader.class.getResourceAsStream("/qald-benchmark/qald-9-test-multilingual.json"),
        //Reader reader = new InputStreamReader(FileReader.class.getResourceAsStream("/qald-benchmark/qald-9-test-multilingual.json"),
        //Reader reader = new InputStreamReader(FileReader.class.getResourceAsStream("/qald-benchmark/qald-9-train-multilingual.json"),
        //Reader reader = new InputStreamReader(FileReader.class.getResourceAsStream("/qald-benchmark/qald6-train-questions.json"), // Evaluierung möglich ohne QB
                "UTF-8");
        Gson gson = new GsonBuilder().create();
        JsonObject json = gson.fromJson(reader, JsonObject.class);

        JsonArray questions = json.get("questions").getAsJsonArray();

        for (int i = 0; i < questions.size(); i++) {
            /*try {
                this.addQuestion(new QaldQuestion(questions.get(i).getAsJsonObject()));
            } catch (Exception e) {
                continue;
            }*/
           this.addQuestion(new QaldQuestion(questions.get(i).getAsJsonObject()));
        }
    }

    /**
     * register a question
     */
    private void addQuestion(QaldQuestion qaldQuestion) {
        this.questions.put(qaldQuestion.getQaldId(), qaldQuestion);
    }

    /**
     * retrieve a QaldQuestion via the QALD question id
     */
    public QaldQuestion getQuestion(int qaldId) {
        return this.questions.get(qaldId);
    }

    /**
     * returns all QaldQuestions parsed from the QALD file
     */
    public Collection<QaldQuestion> getQuestions() {
        return this.questions.values();
    }
}
