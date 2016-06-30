package main.java.odfreplace;

import org.odftoolkit.simple.TextDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

public class CliStream {
    String[] arguments;

    public CliStream(String[] arguments) {
        this.arguments = arguments;
    }

    public InputStream getInputData() {
        try (Scanner scanner = new Scanner(System.in)) {
            byte[] decodedInput = Base64Adapter.base64Decode(scanner.nextLine());
            return new ByteArrayInputStream(decodedInput);
        }
    }

    public List<Map<String, String>> getParsedAttributes() {
        List<Map<String, String>> documentsReplaces = new ArrayList<>();

        for (String codedReplacesPairs : arguments) {
            String[] replacements = Base64Adapter.base64DecodeToString(codedReplacesPairs).split(" ");

            Map<String, String> replacementsMap = new HashMap<>();
            for (String codedReplaces : replacements) {
                String[] argumentPair = codedReplaces.split(":", 2);

                String replace = argumentPair.length > 1 ? Base64Adapter.base64DecodeToString(argumentPair[1]) : "";
                replacementsMap.put(argumentPair[0], replace);
            }

            documentsReplaces.add(replacementsMap);
        }
        return documentsReplaces;
    }

    public void printOdfs(List<TextDocument> documents) {
        documents.stream()
                .forEach(document -> printOdf(document));
    }

    public void printOdf(TextDocument document) {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            document.save(buffer);
            System.out.println(Base64Adapter.base64Encode(buffer.toByteArray()));
        } catch (Exception e) {
        }
    }

    final static class Base64Adapter {
        public static String base64DecodeToString(String data) {
            return new String(base64Decode(data), Charset.forName("UTF-8"));
        }

        public static byte[] base64Decode(String data) {
            return Base64.getDecoder().decode(data.getBytes());
        }

        public static String base64Encode(byte[] data) {
            return Base64.getEncoder().encodeToString(data);
        }
    }
}
