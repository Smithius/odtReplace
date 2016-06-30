package main.java.odfreplace;

import org.odftoolkit.simple.TextDocument;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        CliStream stream = new CliStream(args);

        InputStream documentStream = stream.getInputData();
        OdfChangers odfChangers = new OdfChangers(documentStream);

        List<Map<String, String>> arguments = stream.getParsedAttributes();
        List<TextDocument> documents = odfChangers.generateDocuments(arguments);

        stream.printOdfs(documents);
    }
}
