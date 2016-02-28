package odtReplace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Scanner;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.navigation.InvalidNavigationException;
import org.odftoolkit.simple.common.navigation.TextNavigation;
import org.odftoolkit.simple.common.navigation.TextSelection;

class odfToolkit {

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String encodedDocument = scanner.nextLine();
		scanner.close();

		byte[] decodedDocument = Base64.getDecoder().decode(encodedDocument);
		InputStream documentStream = new ByteArrayInputStream(decodedDocument);

		for (int i = 0; i < args.length; ++i) {
			documentStream.reset();
			TextDocument document = (TextDocument) TextDocument.loadDocument(documentStream);
			createDocument(document, base64Decode(args[i]).split(" "));
		}

	}

	public static void createDocument(TextDocument document, String[] replaces)
			throws UnsupportedEncodingException, InvalidNavigationException {
		String[] argumentPair;
		for (int i = 0; i < replaces.length; ++i) {
			argumentPair = replaces[i].split(":", 2);
			replaceText(document, argumentPair[0], argumentPair.length > 1 ? base64Decode(argumentPair[1]) : "");
		}

		printOdf(document);
	}

	public static void replaceText(TextDocument document, String find, String replace)
			throws InvalidNavigationException {

		TextNavigation search = new TextNavigation(find, document);
		while (search.hasNext()) {
			TextSelection item = (TextSelection) search.nextSelection();
			item.replaceWith(replace);
		}
	}

	public static boolean printOdf(TextDocument document) {
		try {
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			document.save(buf);
			System.out.println(Base64.getEncoder().encodeToString(buf.toByteArray()));
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static String base64Decode(String text) {
		return new String(Base64.getDecoder().decode(text.getBytes()), Charset.forName("UTF-8"));
	}
}
