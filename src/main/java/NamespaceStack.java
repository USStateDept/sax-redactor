import java.util.Stack;

public class NamespaceStack extends Stack<NamespaceEntry>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6084287096112166698L;


	public static class NamespaceEntryWrapper
	{
		@Override
		public String toString()
		{
			return "NamespaceEntryWrapper [entry=" + entry + ", justcreated=" + justcreated + "]";
		}
		public NamespaceEntry entry;
		public boolean justcreated=false;
	}
	
	public NamespaceEntryWrapper findOrAdd(String uri, String localName, String qName)
	{
		NamespaceEntryWrapper res = new NamespaceEntryWrapper();
		res.entry=new NamespaceEntry(uri,localName,qName);
		int i=indexOfRecentNamespace(res.entry);
//		System.err.println(res+":"+i);
		if (i>=0)
		{
			res.justcreated=false;
			res.entry=get(i);
		}
		else
		{
			res.justcreated=true;
			push(res.entry);
		}
		
		return res;
	}
	
	
	public int indexOfRecentNamespace(NamespaceEntry ns)
	{
//		System.err.println("\n\n\nin indexOfRecentNamespace for ns="+ns);
		for (int i=size()-1; i>=0; --i)
		{
//			System.err.println(i);
			NamespaceEntry nsi = get(i);
//			System.err.println(nsi);
			if (ns.equals(nsi)) return i;
			String pref = nsi.prefix;
			if (pref.equals(ns.prefix)) return -1;
		}
		return -1;
	}

}
