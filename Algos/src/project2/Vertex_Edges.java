package project2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Vertex_Edges implements Serializable {

	String vertex;
	// Map<Object, Object> edge = new HashMap<>();
	Map<Vertex_Edges, Float> edge_mapper = new HashMap<>();
	Map<String, Float> tree_edge_mapper = new TreeMap<>();
	boolean is_vertex_available = true;
	Map<Vertex_Edges, Boolean> edge_availaility = new HashMap<>();
	float shortest_node_dist = 0;
	Vertex_Edges prev_short_node;
	String shortest_path;
	boolean visited = false;

	// check if edge already exists then replace the distance
	boolean add_edge(Vertex_Edges head, float dist) {
		if (edge_mapper.containsKey(head)) {
			edge_mapper.replace(head, dist);
			// edge.replace(tail, edge_mapper);
		} else {
			edge_mapper.put(head, dist);
			edge_availaility.put(head, true);
			// edge.put(tail, edge_mapper);
		}

		return false;
	}

	// check if edge exists and delete
	void deleteedge(Vertex_Edges head) {
		if (edge_mapper.containsKey(head)) {
			edge_mapper.remove(head);
			edge_availaility.remove(head);
		}
	}

	// set vertex distance to infinity
	void max_desitance_set() {
		this.shortest_node_dist = Integer.MAX_VALUE;
	}

	// if edge exists down it
	void edgedown(Vertex_Edges head) {
		if (edge_availaility.containsKey(head))
			edge_availaility.replace(head, false);

	}

	// if edge exists up it
	void edgeup(Vertex_Edges head) {
		if (edge_availaility.containsKey(head))
			edge_availaility.replace(head, true);
	}

	public Vertex_Edges(String vertex) {
		super();
		this.vertex = vertex;
		shortest_path = vertex;
	}

	// vertex down
	void vextexdown() {
		is_vertex_available = false;
	}

	// vertex up
	void vextexup() {
		is_vertex_available = true;
	}

}
