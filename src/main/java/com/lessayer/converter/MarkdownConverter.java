package com.lessayer.converter;

import java.util.Arrays;
import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownConverter extends ContentConverter {

	private List<Extension> extensions = Arrays.asList(TablesExtension.create());
	private Parser parser = Parser.builder().extensions(extensions).build();
	private HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
	
	@Override
	public String convertContentToHTMLString(String content) {
		Node document = parser.parse(content);
		
		return renderer.render(document);
	}

}
