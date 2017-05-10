
public class NamespaceEntry
{
	@Override
	public String toString()
	{
		return "NamespaceEntry [prefix=" + prefix + ", uri=" + uri + "]";
	}

	String prefix;
	String uri;

	public NamespaceEntry(){}
	public NamespaceEntry(String uri, String localName, String qName)
	{
		if (uri==null) throw new NullPointerException("null uri");
		this.uri=uri;
		prefix=extractPrefix(localName,qName);
	}
	public static String extractPrefix(String localName, String qName)
	{
		if (localName.equals(qName)) return "";
		
		int len = qName.length();
		len-=localName.length();
		return qName.substring(0, len-1);
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamespaceEntry other = (NamespaceEntry) obj;
		if (prefix == null)
		{
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (uri == null)
		{
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
	
	public static void main(String[] args)
	{
		String[] qnames={
				"p:d",
				"p:eee"
				
		};
		String[] locals={
				"d",
				"eee"
		};
		
		for (int i=0; i<qnames.length; ++i)
		{
			System.out.println(extractPrefix(locals[i],qnames[i]));
		}
		
	}
	
}
