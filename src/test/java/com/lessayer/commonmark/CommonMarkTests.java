package com.lessayer.commonmark;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.jupiter.api.Test;

public class CommonMarkTests {
	
	private static final String HTML_TEMPLATE_HEAD = 
			"<!DOCTYPE html>\n"
			+ "<html xmlns:th=\"http://www.thymeleaf.org\">\n"
			+ "<head>\n"
			+ "<meta charset=\"UTF-8\">\n"
			+ "<title>Insert title here</title>\n"
			+ "</head>\n"
			+ "<body>\n";
	
	private static final String HTML_TEMPLATE_TAIL = 
			"</body>\n"
			+ "</html>";
	
	private static final String CURRENT_PATH = "src/test/java/com/lessayer/commonmark/";
	private static final String HTML_TEMPLATE_FILENAME = "template.html";
	
	private List<Extension> extensions = Arrays.asList(TablesExtension.create());
	private Parser parser = Parser.builder().extensions(extensions).build();
	private HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
	
	@Test
	public void markdownCreateHeaderTest() {
		String header1String = "# This is level 1 ";
		String header2String = "## This is level 2 ";
		String header3String = "### This is level 3 ";
		String header4String = "#### This is level 4 ";
		String header5String = "##### This is level 5 ";
		
		Node header1Node = parser.parse(header1String);
		Node header2Node = parser.parse(header2String);
		Node header3Node = parser.parse(header3String);
		Node header4Node = parser.parse(header4String);
		Node header5Node = parser.parse(header5String);
		
		String HTMLContent = generateHTMLString(renderer.render(header1Node) + renderer.render(header2Node) + renderer.render(header3Node) +
				renderer.render(header4Node) + renderer.render(header5Node));
		
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateTextTest() {
		String plainText = "This is a Sparta!!!";
		String boldText = "This is a **Sparta!!!**";
		String italicText = "This is a *Sparta!!!*";
		String boldAndItalicText = "This is a ***Sparta!!!***";
		
		Node plainTextNode = parser.parse(plainText);
		Node boldTextNode = parser.parse(boldText);
		Node italicTextNode = parser.parse(italicText);
		Node boldAndItalicTextNode = parser.parse(boldAndItalicText);
		
		String HTMLContent = generateHTMLString(renderer.render(plainTextNode) + renderer.render(boldTextNode) +
				renderer.render(italicTextNode) + renderer.render(boldAndItalicTextNode));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateListTest() {
		String orderedListItem1String = "1. This is a Sparta!";
		String orderedListItem2String = "2. This is a Sparta!!";
		String orderedListItem3String = "3. This is a Sparta!!!";
		String unorderedListItem1String = "- This is a **Sparta!";
		String unorderedListItem2String = "- This is a **Sparta!!";
		String unorderedListItem3String = "- This is a **Sparta!!!";
		
		Node orderedListItem1Node = parser.parse(orderedListItem1String);
		Node orderedListItem2Node = parser.parse(orderedListItem2String);
		Node orderedListItem3Node = parser.parse(orderedListItem3String);
		Node unorderedListItem1Node = parser.parse(unorderedListItem1String);
		Node unorderedListItem2Node = parser.parse(unorderedListItem2String);
		Node unorderedListItem3Node = parser.parse(unorderedListItem3String);
		
		String HTMLContent = generateHTMLString(renderer.render(orderedListItem1Node) + renderer.render(orderedListItem2Node) 
			+ renderer.render(orderedListItem3Node) + renderer.render(unorderedListItem1Node)
			+ renderer.render(unorderedListItem2Node) + renderer.render(unorderedListItem3Node));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateTableTest() {
        String tableHeader = 
                "| First Header  | Second Header |\n" +
                "| ------------- | ------------- |\n" +
                "| Content Cell  | Content Cell  |\n" +
                "| Content Cell  | Content Cell  |";
		Node tableHeaderNode = parser.parse(tableHeader);
		String HTMLContent = generateHTMLString(renderer.render(tableHeaderNode));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateLinkTest() {
        String linkString = "Click the [link](https://github.com/commonmark/commonmark-java/blob/main/README.md)";
		Node linkNode = parser.parse(linkString);
		String HTMLContent = generateHTMLString(renderer.render(linkNode));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateImageTest() {
        String imageLinkString = "# Title\n"
        		+ "\n"
        		+ "## Header 1\n"
        		+ "\n"
        		+ "### Header 1-1\n"
        		+ "\n"
        		+ "Some text here.\n"
        		+ "\n"
        		+ "This is a **bold text!**\n"
        		+ "\n"
        		+ "### Header 1-2\n"
        		+ "\n"
        		+ "Show a list below:\n"
        		+ "\n"
        		+ "- List item 1\n"
        		+ "- List item 2\n"
        		+ "- List item 3\n"
        		+ "\n"
        		+ "### Header 1-3\n"
        		+ "\n"
        		+ "A table example.\n"
        		+ "\n"
        		+ "| Header  | Header | Header |\n"
        		+ "| ------- | ------- | ------- |\n"
        		+ "| Content | Content | Content |\n"
        		+ "\n"
        		+ "## Header 2\n"
        		+ "\n"
        		+ "### Header 2-1\n"
        		+ "\n"
        		+ "Make sure the following *links* are availabe.\n"
        		+ "\n"
        		+ "1. [Oracle](https://www.oracle.com/index.html)\n"
        		+ "2. [Udemy](https://www.udemy.com/)\n"
        		+ "3. [France24](https://www.france24.com/en/live)\n"
        		+ "\n"
        		+ "### Header 2-2\n"
        		+ "\n"
        		+ "*Image 1*\n"
        		+ "![tree](https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg)\n"
        		+ "\n"
        		+ "*Image 2*\n"
        		+ "![ocean](https://new-media.dhakatribune.com/en/uploads/2022/04/14/unnamed-1605686494471.jpeg)\n"
        		+ "\n"
        		+ "*Image 3*\n"
        		+ "![snow](https://img.rasset.ie/0015d72f-800.jpg)\n";
		Node imageLinkNode = parser.parse(imageLinkString);
		String HTMLContent = generateHTMLString(renderer.render(imageLinkNode));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateCodeSectionTest() {
        String codeSectionString = "```\n"
        		+ "System.out.println();\n"
        		+ "System.out.println();\n"
        		+ "```\n";
		Node codeSectionNode = parser.parse(codeSectionString);
		String HTMLContent = generateHTMLString(renderer.render(codeSectionNode));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	@Test
	public void markdownCreateArticleTest() {
        String articleString = "```\n"
        		+ "System.out.println();\n"
        		+ "System.out.println();\n"
        		+ "```\n";
		Node document = parser.parse(articleString);
		String HTMLContent = generateHTMLString(renderer.render(document));
		writeToFile(HTML_TEMPLATE_FILENAME, HTMLContent);
	}
	
	private String generateHTMLString(String content) {
		return HTML_TEMPLATE_HEAD + content + HTML_TEMPLATE_TAIL;
	}
	
	private void writeToFile(String fileName, String content) {
		try {
			FileWriter myWriter = new FileWriter(CURRENT_PATH + fileName);
			myWriter.write(content);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
