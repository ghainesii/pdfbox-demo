package net.ghaines.pdfboxdemo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@SpringBootApplication
public class PdfboxDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfboxDemoApplication.class, args);
	}

}

@RestController
@RequestMapping
class PdfController {

	@GetMapping("/pdf")
	ResponseEntity<byte[]> getPdf(@RequestParam(required = false) String name) throws IOException {
		try (PDDocument doc = new PDDocument()) {
			PDPage page = new PDPage();
			doc.addPage(page);

			PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

			try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
				contents.beginText();
				contents.setFont(font, 12);
				contents.newLineAtOffset(100, 700);
				contents.showText(name == null ? "Hello World" : "Hello, " + name);
				contents.endText();
			}

			var outStream = new ByteArrayOutputStream();
			doc.save(outStream);
			var pdf = outStream.toByteArray();
			var headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

			return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

		}
	}

}