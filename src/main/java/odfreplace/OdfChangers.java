package main.java.odfreplace;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.navigation.InvalidNavigationException;
import org.odftoolkit.simple.common.navigation.TextNavigation;
import org.odftoolkit.simple.common.navigation.TextSelection;

class OdfChangers {
    InputStream documentStream;

    public OdfChangers(InputStream documentStream) {
        this.documentStream = documentStream;
    }

    public List<TextDocument> generateDocuments(List<Map<String, String>> replacesCollection) throws Exception {
        List<TextDocument> documents = new ArrayList<>();
        for (Map<String, String> replaces : replacesCollection) {
            TextDocument document = generateDocument(replaces);
            documents.add(document);
        }

        return documents;
    }

    public TextDocument generateDocument(Map<String, String> replaces) throws Exception {
        documentStream.reset();
        try (TextDocument document = TextDocument.loadDocument(documentStream)) {
            for (Map.Entry<String, String> replace : replaces.entrySet()) {
                replaceText(document, replace.getKey(), replace.getValue());
            }
            return document;
        }
    }

    private void replaceText(TextDocument document, String find, String replace) {
        TextNavigation search = new TextNavigation(find, document);
        while (search.hasNext()) {
            TextSelection item = (TextSelection) search.nextSelection();
            try {
                item.replaceWith(replace);
            } catch (InvalidNavigationException e) {
            }
        }
    }
}
