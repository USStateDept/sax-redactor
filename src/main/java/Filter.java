import java.io.OutputStream;
import java.io.PrintStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class Filter extends DefaultHandler
{
	private static final int MAXLEN = 1024;
	private PrintStream out;

	public Filter(OutputStream out)
	{
		if (out==null) throw new NullPointerException();
		if (out instanceof PrintStream) this.out=(PrintStream) out;
		this.out=new PrintStream(out);
	}

	NamespaceStack ns=new NamespaceStack();
	
	public String xmlRedact(String value)
	{
		if (value==null) return null;
		return xmlEscape(value.replaceAll("[a-zA-Z]", "X").replaceAll("[0-9]", "8"));
	}
	
	public String xmlEscape(String value)
	{
		if (value==null) return null;
		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		out.print("<");
		out.print(qName);
		
		NamespaceStack.NamespaceEntryWrapper w=ns.findOrAdd(uri,localName,qName);
		if (w.justcreated)
		{
//			System.err.println(uri+"\n"+localName);
			out.print(" xmlns:");
			out.print(w.entry.prefix);
			out.print("='");
			out.print(xmlEscape(w.entry.uri));
			out.print("'");
		}
		for (int i=0; i<attributes.getLength(); ++i)
		{
			out.print(" ");
			out.print(attributes.getQName(i));
			out.print("='");
			out.print(xmlRedact(attributes.getValue(i)));
			out.print("'");
			
//			System.err.println("\n\n\n"+attributes.getValue(i)+"\n\n\n");
		}
		
		out.print(">");
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		out.print("</");
		out.print(qName);
		out.print(">");
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		while (length>MAXLEN)
		{
			characters(ch, start, MAXLEN);
			start+=MAXLEN;
			length-=MAXLEN;
		}
		String res = String.valueOf(ch,start,length);
		res=xmlRedact(res);
		out.print(res);			
	}
	
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
	{
		while (length>MAXLEN)
		{
			ignorableWhitespace(ch, start, MAXLEN);
			start+=MAXLEN;
			length-=MAXLEN;
		}
		String res = String.valueOf(ch,start,length);
		res=xmlRedact(res);
		out.print(res);			
	}
}
