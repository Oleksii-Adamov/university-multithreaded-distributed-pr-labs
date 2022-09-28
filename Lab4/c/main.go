package main

import "fmt"

type Edge struct {
	vertex string
	cost   int
}

//type Vertex struct {
//	name  string
//	edges []Edge
//}

type Graph struct {
	//vertices map[string]map[Edge]bool
	vertices map[string]map[string]int
}

type Way struct {
	cost     int
	vertices []string
}

func (graph *Graph) addEdge(vertex1 string, vertex2 string, cost int) {
	//graph.vertices[vertex1][Edge{vertex: vertex2, cost: cost}] = true
	//graph.vertices[vertex2][Edge{vertex: vertex1, cost: cost}] = true
	graph.vertices[vertex1][vertex2] = cost
	graph.vertices[vertex2][vertex1] = cost
	//graph.vertices[vertex1] = append(graph.vertices[vertex1], Edge{vertex: vertex2, cost: cost})
	//graph.vertices[vertex2] = append(graph.vertices[vertex2], Edge{vertex: vertex1, cost: cost})
}

func (graph *Graph) removeEdge(vertex1 string, vertex2 string /*, cost int*/) {
	delete(graph.vertices[vertex1], vertex2)
	delete(graph.vertices[vertex2], vertex1)
	//delete(graph.vertices[vertex1], Edge{vertex: vertex2, cost: cost})
	//delete(graph.vertices[vertex2], Edge{vertex: vertex1, cost: cost})
}

func (graph *Graph) addVertex(vertex string) {
	//graph.vertices[vertex] = make(map[Edge]bool)
	graph.vertices[vertex] = make(map[string]int)
}

func (graph *Graph) removeVertex(vertex string) {
	delete(graph.vertices, vertex)
}

func (graph *Graph) changeCost(vertex1 string, vertex2 string, new_cost int) {
	graph.vertices[vertex1][vertex2] = new_cost
	graph.vertices[vertex2][vertex1] = new_cost
}

func (graph *Graph) findWay(vertex1 string, vertex2 string) Way {
	var ret_way Way
	ways_queue := make([]Way, 1)
	ways_queue[0] = Way{0, make([]string, 1)}
	ways_queue[0].vertices[0] = vertex1
	for len(ways_queue) != 0 {
		way := ways_queue[0]
		ways_queue = ways_queue[1:]
		vertex := way.vertices[len(way.vertices)-1]
		if vertex == vertex2 {
			ret_way = way
			break
		}
		for next_vertex, cost := range graph.vertices[vertex] {
			ways_queue = append(ways_queue, Way{way.cost + cost, append(way.vertices, next_vertex)})
		}
	}
	return ret_way
}

func main() {
	fmt.Println("Hello, World!")
}
