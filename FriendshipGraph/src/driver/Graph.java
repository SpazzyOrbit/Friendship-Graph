package driver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

public class Graph
{
	private int size; // number of vertices in graph
	private HashMap<String, Person> people; // name, person
	private String[] names;


	/**
	 * This class holds the representation of our graph.
	 * 
	 * @param scanner
	 *            The scanner containing the information about the graph.
	 * */
	public Graph()
	{
		people = new HashMap<String, Person>();
	}


	public void getFileGraph(Scanner scanner)
	{
		setVertices(scanner);
		setEdges(scanner);
		scanner.close();
	}


	/**
	 * Sets the two-way connections between different people.
	 * 
	 * @param scanner
	 *            The scanner at the point where the connection information begins.
	 * */
	private void setEdges(Scanner scanner)
	{
		while (scanner.hasNext())
		{
			String line = scanner.nextLine();
			String name1 = line.substring(0, line.indexOf('|')).toLowerCase();
			String name2 = line.substring(name1.length() + 1).toLowerCase();
			people.get(name1).addFriend(people.get(name2));
			people.get(name2).addFriend(people.get(name1));
		}
	}


	/**
	 * Creates our Person nodes.
	 * 
	 * @param scanner
	 *            The scanner at the beginning of the file.
	 * */
	private void setVertices(Scanner scanner)
	{
		this.size = Integer.parseInt(scanner.nextLine());
		this.names = new String[size];
		for(int i = 0; i < size; i++)
		{
			String line = scanner.nextLine();
			String name = line.substring(0, line.indexOf('|')).toLowerCase();
			String school = null;
			switch (line.charAt(name.length() + 1))
			{
				case 'n':
					break;
				case 'y':
					school = line.substring(name.length() + 3).toLowerCase();
					break;
				default:
					break;
			}
			Integer vertexNum = Integer.valueOf(i + 1);
			people.put(name, new Person(name, school, vertexNum));
			names[vertexNum - 1] = name;
		}
	}


	public String toString()
	{
		return size() + verticesToString() + edgesToString();
	}


	private String size()
	{
		String size = Integer.toString(this.size) + "\n";
		return size;
	}


	private String verticesToString()
	{
		String vertices = "";
		for(Iterator<Entry<String, Person>> iterator = people.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<String, Person> entry = iterator.next();
			vertices += entry.getKey() + "|";
			Person person = entry.getValue();
			if (person.getSchool() == null)
			{
				vertices += "n\n";
			}
			else
			{
				vertices += "y|" + person.getSchool() + "\n";
			}
		}
		return vertices;
	}


	private String edgesToString()
	{
		String edges = "";
		boolean[][] record = new boolean[size][size];
		for(int numP = 1; numP <= size; numP++)
		{
			String name = names[numP - 1];
			Person person = this.getPerson(name);
			for(Iterator<Person> fIterator = person.getFriends().iterator(); fIterator.hasNext();)
			{
				Person friend = fIterator.next();
				int numF = friend.getVertexNumber();
				if (!record[numF - 1][numP - 1])
				{
					edges += name + "|" + friend.getName() + "\n";
					record[numP - 1][numF - 1] = true;
				}
			}
		}
		edges.trim();
		return edges;
	}


	public Graph subgraph(String school)
	{
		// TODO
		Graph graph = new Graph();
		graph.names = new String[this.size];
		Integer newSize = 0;
		for(Integer i = 1; i <= size; i++)
		{
			String name = this.getName(i);
			Person person = this.getPerson(name);
			if (person.getSchool() == null || !person.getSchool().equals(school))
			{
				continue;
			}
			HashSet<Person> friends = person.getFriends();
			Person newPerson = graph.getPerson(name);
			if (newPerson == null)
			{
				newPerson = new Person(name, school, ++newSize);
				graph.addPerson(newPerson);
			}
			for(Iterator<Person> iterator = friends.iterator(); iterator.hasNext();)
			{
				Person friend = iterator.next();
				if (friend.getSchool() != null && friend.getSchool().equals(school))
				{
					Person existingFriend = graph.getPerson(friend.getName());
					if (existingFriend != null)
					{
						newPerson.addFriend(graph.getPerson(friend.getName()));
					}
					else
					{
						Person newFriend = new Person(friend.getName(), school, ++newSize);
						newPerson.addFriend(newFriend);
						newFriend.addFriend(newPerson);
						graph.addPerson(newFriend);
					}
				}
			}
		}
		graph.setSize(newSize);
		String[] temp = new String[newSize];
		System.arraycopy(graph.names, 0, temp, 0, newSize);
		graph.names = temp;
		return graph;
	}


	private void setSize(int size)
	{
		this.size = size;
	}


	private Person getPerson(String name)
	{
		return people.get(name);
	}


	private String getName(Integer numVertex)
	{
		return names[numVertex - 1];
	}


	private void addPerson(Person newPerson)
	{
		people.put(newPerson.getName(), newPerson);
		names[newPerson.getVertexNumber() - 1] = newPerson.getName();
	}


	public HashSet<Person> shortestPath(String start, String end)
	{
		// TODO
		return null;
	}


	public HashSet<Person> cliques(String school)
	{
		// TODO note: if clique is not in core graph, it will be empty
		return null;
	}


	public String connectors()
	{
		// TODO
		return null;
	}
}