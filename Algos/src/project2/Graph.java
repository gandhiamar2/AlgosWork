package project2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Graph {

	static String line, file, s, query_file, output_file;
	static File f;
	static BufferedReader bfr;
	static StringBuilder sbr;
	static StringTokenizer str;
	Vertex_Edges[] heap_array;
	Map<String, Vertex_Edges> vertex_map = new HashMap<>();
	private Vertex_Edges vertex;
	Queue<Vertex_Edges> que, sub_que;
	ArrayList<String> toprint = new ArrayList<>();
	// Path function is implemented in two ways using Heap and priority queue
	PriorityQueue<Vertex_Edges> queue = new PriorityQueue<>(new Comparator<Vertex_Edges>() {

		@Override
		public int compare(Vertex_Edges o1, Vertex_Edges o2) {

			return o1.shortest_node_dist < o2.shortest_node_dist ? -1
					: o1.shortest_node_dist == o2.shortest_node_dist ? 0 : 1;

		}
	});

	// the network file is loaded into stringbuffer and is split for every
	// record and addedge functions are being called
	public static void main(String[] args) throws IOException {
		Graph g = new Graph();
		// file = args[0];
		file = "C:/Users/gandh/Documents/CS/sem2/algos/project2_example/network.txt";
		f = new File(file);
		BufferedReader bfr = new BufferedReader(new FileReader(f));
		s = bfr.readLine();
		sbr = new StringBuilder();
		while (s != null) {
			sbr.append(s);
			s = bfr.readLine();
		}
		FileReader fin = new FileReader(f);
		Scanner graphFile = new Scanner(fin);
		String[] part = sbr.toString().split("(?<=\\d)(?=[a-zA-Z])");
		String[] part_line;
		for (String string : part) {
			part_line = string.split(" ");
			g.addedge(part_line[0], part_line[1], Float.parseFloat(part_line[2]));
			g.addedge(part_line[1], part_line[0], Float.parseFloat(part_line[2]));
		}

		Scanner input = new Scanner(System.in);
		while (processRequest(input, g))
			;

	}

	// process all the queries and call respective functions
	private static boolean processRequest(Scanner input, Graph g) {
		try {
			line = input.nextLine();
			String arr[];
			arr = line.split(" ");
			if (arr[0].equals("addedge") && arr.length == 4)
				g.addedge(arr[1], arr[2], Float.parseFloat(arr[3]));
			else if (arr[0].equals("print") && arr.length == 1) {
				g.print();
				System.out.println(" ");
			} else if (arr[0].equals("path") && arr.length == 3) {
				g.path_heap(arr[1], arr[2]);
				System.out.println(" ");
			} else if (arr[0].equals("edgedown") && arr.length == 3)
				g.edgedown(arr[1], arr[2]);
			else if (arr[0].equals("vertexdown") && arr.length == 2)
				g.vertexdown(arr[1]);
			else if (arr[0].equals("reachable") && arr.length == 1) {
				g.reachable();
				System.out.println(" ");
			} else if (arr[0].equals("edgeup") && arr.length == 3)
				g.edgeup(arr[1], arr[2]);
			else if (arr[0].equals("vertexup") && arr.length == 2)
				g.vertexup(arr[1]);
			else if (arr[0].equals("deleteedge") && arr.length == 3)
				g.deleteedge(arr[1], arr[2]);
			else if (arr[0].equals("quit") && arr.length == 1)
				g.quit();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	// add edge method check for if vertex is already present if not creates a
	// vertex and store the edge details in all tailing vertex.
	void addedge(String tail, String head, float dist) {

		if (!vertex_map.containsKey(head)) {
			vertex = new Vertex_Edges(head);
			vertex_map.put(head, vertex);
		}

		if (!vertex_map.containsKey(tail)) {
			vertex = new Vertex_Edges(tail);
			vertex_map.put(tail, vertex);
		}

		vertex_map.get(tail).add_edge(vertex_map.get(head), dist);

	}

	// check for vertex existence and deletes the edge if exists
	void deleteedge(String tail, String head) {
		if (vertex_map.containsKey(tail) && vertex_map.containsKey(head)) {
			vertex_map.get(tail).deleteedge(vertex_map.get(head));
		}
	}

	// check for vertex existence and down the edge if exists
	void edgedown(String tail, String head) {
		if (vertex_map.containsKey(tail) && vertex_map.containsKey(head)) {
			vertex_map.get(tail).edgedown(vertex_map.get(head));
		}
	}

	// check for vertex existence and up the edge if exists
	void edgeup(String tail, String head) {
		if (vertex_map.containsKey(tail) && vertex_map.containsKey(head)) {
			vertex_map.get(tail).edgeup(vertex_map.get(head));
		}
	}

	// check for vertex existence and downs it
	void vertexdown(String tail) {
		if (vertex_map.containsKey(tail)) {
			vertex_map.get(tail).vextexdown();
		}
	}

	// check for vertex existence and ups it
	void vertexup(String tail) {
		if (vertex_map.containsKey(tail)) {
			vertex_map.get(tail).vextexup();
		}
	}

	// this path method is implemented using priority queue
	void path(String tail, String head) {
		Vertex_Edges headv = vertex_map.get(head);
		Vertex_Edges tailv = vertex_map.get(tail);
		queue.clear();
		if (headv != null && tailv != null && headv.is_vertex_available && tailv.is_vertex_available) {

			for (String s : vertex_map.keySet()) {
				vertex_map.get(s).visited = false;
				vertex_map.get(s).shortest_node_dist = 0;
				vertex_map.get(s).shortest_path = vertex_map.get(s).vertex;
			}
			tailv.shortest_node_dist = 0;
			queue.add(vertex_map.get(tail));
			do {
				Vertex_Edges temp = queue.poll();
				temp.visited = true;
				for (Vertex_Edges edges : temp.edge_mapper.keySet()) {
					if (edges.is_vertex_available && temp.edge_availaility.get(edges)) {
						if ((edges.shortest_node_dist > temp.shortest_node_dist + temp.edge_mapper.get(edges))
								&& !edges.visited) {
							edges.shortest_node_dist = temp.shortest_node_dist + temp.edge_mapper.get(edges);
							edges.shortest_path = temp.shortest_path + " " + edges.vertex;
						} else if ((edges.shortest_node_dist == 0) && !edges.visited) {
							edges.shortest_node_dist = temp.shortest_node_dist + temp.edge_mapper.get(edges);
							edges.shortest_path = temp.shortest_path + " " + edges.vertex;

							queue.add(edges);
						}
					}
				}
				// System.out.println(temp.shortest_path+"
				// "+temp.shortest_node_dist);
			} while (!(queue.peek() == vertex_map.get(head)));

			System.out.println(vertex_map.get(head).shortest_path + " " + vertex_map.get(head).shortest_node_dist);

		}

	}

	// this path method is implemented using binary heap
	void path_heap(String tail, String head) {
		Vertex_Edges headv = vertex_map.get(head);
		Vertex_Edges tailv = vertex_map.get(tail);
		// check if tail and head exists and are up
		if (headv != null && tailv != null && headv.is_vertex_available && tailv.is_vertex_available) {
			int size = 1, set = 1;
			// make distance infinite for all vertex and 0 for tail
			for (String s : vertex_map.keySet()) {
				if (vertex_map.get(s).is_vertex_available) {
					size++;
					vertex_map.get(s).max_desitance_set();
					vertex_map.get(s).shortest_path = "";
				}
			}
			vertex_map.get(tail).shortest_node_dist = 0;
			vertex_map.get(tail).shortest_path = vertex_map.get(tail).vertex;
			Vertex_Edges tail_v = vertex_map.get(tail);
			heap_array = new Vertex_Edges[size];
			// make an array of vertex
			for (String s : vertex_map.keySet()) {
				if (vertex_map.get(s).is_vertex_available) {
					heap_array[set] = vertex_map.get(s);
					set++;
				}
			}
			// building heap initially
			heap_build_min(heap_array, size - 1);
			size--;
			Vertex_Edges vert = null;
			while (size > 0) {
				// extract the topmost/min element from heap
				vert = heap_min_extract(heap_array, size);
				size--;
				// check if new distance is less than existing for all vertex
				// linked to current min vertex and update to min distance
				for (Vertex_Edges vertex_Edges : vert.edge_mapper.keySet()) {
					if (vert.edge_availaility.get(vertex_Edges) && vertex_Edges.is_vertex_available) {
						if (vertex_Edges.shortest_node_dist > vert.edge_mapper.get(vertex_Edges)
								+ vert.shortest_node_dist) {
							vertex_Edges.shortest_node_dist = vert.edge_mapper.get(vertex_Edges)
									+ vert.shortest_node_dist;
							vertex_Edges.prev_short_node = vert;
							vertex_Edges.shortest_path = vert.shortest_path + " " + vertex_Edges.vertex;
						}
					}
				}
				// build heap till all vertex are extracted
				heap_build_min(heap_array, size);
			}

			Vertex_Edges head_v = vertex_map.get(head);
			System.out.println(head_v.shortest_path + " " + (Math.round(head_v.shortest_node_dist * 100.0) / 100.0));
			toprint.add(head_v.shortest_path + " " + (Math.round(head_v.shortest_node_dist * 100.0) / 100.0));
			toprint.add("\n\n");
		}

	}

	// calls the heapify_min function iteratively
	void heap_build_min(Vertex_Edges[] heap_array, int n) {

		for (int i = n / 2; i >= 1; i--) {
			heapify_min(heap_array, i, n);

		}
	}

	// check for smallest among i left right and reorder the heap
	void heapify_min(Vertex_Edges[] heap_array, int i, int n) {
		int left = 2 * i, right = left + 1, small = i;
		if (left <= n)
			if (heap_array[left].shortest_node_dist < heap_array[i].shortest_node_dist)
				small = left;
		if (right <= n)
			if (heap_array[right].shortest_node_dist < heap_array[small].shortest_node_dist)
				small = right;
		if (small != i) {
			Vertex_Edges x = heap_array[i];
			heap_array[i] = heap_array[small];
			heap_array[small] = x;
			heapify_min(heap_array, small, n);
		}

	}

	// extracting top most min vertex and replacing with last vertex
	Vertex_Edges heap_min_extract(Vertex_Edges[] heap_array, int n) {
		Vertex_Edges min = null;
		if (n < 1)
			System.out.println("heap under flow");
		else {
			min = heap_array[1];
			heap_array[1] = heap_array[n];

		}

		return min;

	}

	// this method prints all vertex and their edges with down and up
	void print() {
		// tree map and collections to sort alphabetically
		Map<String, Vertex_Edges> vertex_tree_map = new TreeMap<>(vertex_map);
		String local;
		for (String s : vertex_tree_map.keySet()) {
			List<Vertex_Edges> list = new ArrayList<>(vertex_tree_map.get(s).edge_mapper.keySet());
			Collections.sort(list, new Comparator<Vertex_Edges>() {

				@Override
				public int compare(Vertex_Edges o1, Vertex_Edges o2) {
					// TODO Auto-generated method stub
					return o1.vertex.compareTo(o2.vertex) < 0 ? -1 : 1;
				}
			});

			// check is Vertex is down
			if (!vertex_tree_map.get(s).is_vertex_available) {
				System.out.println(s + " DOWN");
				toprint.add(s + " DOWN");
				toprint.add("\n");
			} else {
				System.out.println(s);
				toprint.add(s);
				toprint.add("\n");
			}

			// printing all edges for the vertex with check if down
			for (Vertex_Edges vertex_Edges : list) {
				if (vertex_tree_map.get(s).edge_availaility.get(vertex_Edges)) {
					System.out.println(
							" " + vertex_Edges.vertex + " " + vertex_tree_map.get(s).edge_mapper.get(vertex_Edges));
					toprint.add(" " + vertex_Edges.vertex + " " + vertex_tree_map.get(s).edge_mapper.get(vertex_Edges));
					toprint.add("\n");
				}

				else {
					System.out.println(" " + vertex_Edges.vertex + " "
							+ vertex_tree_map.get(s).edge_mapper.get(vertex_Edges) + " DOWN");
					toprint.add((" " + vertex_Edges.vertex + " " + vertex_tree_map.get(s).edge_mapper.get(vertex_Edges)
							+ " DOWN"));
					toprint.add("\n");
				}

			}
		}
		toprint.add("\n");

	}

	// this method prints reachable vertex for every vertex similar to BFS
	// running time is O(V+E) for running reachability of each vertex
	/*pseudo code:
	 * alphabetically arrange available vertex's
	 * pick a vertex from sorted map
	 * check if it is up and mark it visited and all other vertex's as not-visited
	 * add the current vertex to queue
	 * while queue is not empty poll vertex from queue check the immediate reachable vertex's that are not-visited from it on up-edges
	 * set these explored edges as visited and add them to queue and a tree map
	 * print the tree map
	 */
	void reachable() {

		Map<String, Vertex_Edges> vertex_tree_map = new TreeMap<>(vertex_map);

		// selecting vertex in alphabetical order from all vertex set
		for (String s1 : vertex_tree_map.keySet()) {

			// setting all vertex's visited = false (similar to colors in BFS)
			for (String s : vertex_map.keySet()) {
				vertex_map.get(s).visited = false;
			}
			Vertex_Edges ve, sub_ve;
			sub_que = new LinkedList<>();
			ve = vertex_tree_map.get(s1);
			// if the vertex is available then add all immediate reachable
			// vertex to queue and also add these reachable vertex to another
			// map marking visited = true
			if (ve.is_vertex_available) {
				Map<String, Vertex_Edges> sub_tree_map = new TreeMap<>();
				System.out.println(ve.vertex);
				toprint.add(ve.vertex);
				toprint.add("\n");
				ve.visited = true;
				// add the vertex to queue
				sub_que.add(ve);
				// while there are elements in queue check for
				// immediate reachable vertex's from already visited vertex's
				// and add them to queue and map
				while (sub_que.size() != 0) {
					sub_ve = sub_que.poll();
					for (Vertex_Edges ve_sub : sub_ve.edge_mapper.keySet()) {
						if (sub_ve.edge_availaility.get(ve_sub) && ve_sub.is_vertex_available && !ve_sub.visited) {
							ve_sub.visited = true;
							sub_tree_map.put(ve_sub.vertex, ve_sub);
							sub_que.add(ve_sub);
						}
					}
				}

				// print all identified reachable vertex in alphabetical order
				for (String s2 : sub_tree_map.keySet()) {
					System.out.println(" " + s2);
					toprint.add(" " + s2);
					toprint.add("\n");
				}

			}
		}
		toprint.add("\n");

	}

	// will stop the program
	void quit() {
		System.exit(0);
	}
}
