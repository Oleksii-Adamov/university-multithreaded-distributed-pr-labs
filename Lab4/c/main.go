package main

import (
	"fmt"
	"sync"
	"time"
)

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
	rwlock   sync.RWMutex
	wg       sync.WaitGroup
}

func makeGraph() *Graph {
	return &Graph{vertices: make(map[string]map[string]int, 0)}
}

type Way struct {
	cost     int
	vertices []string
}

func (graph *Graph) addEdge(vertex1 string, vertex2 string, cost int) {
	defer graph.wg.Done()
	graph.rwlock.Lock()
	time.Sleep(3 * time.Second)
	//graph.vertices[vertex1][Edge{vertex: vertex2, cost: cost}] = true
	//graph.vertices[vertex2][Edge{vertex: vertex1, cost: cost}] = true
	_, ok1 := graph.vertices[vertex1]
	_, ok2 := graph.vertices[vertex2]
	if ok1 && ok2 {
		graph.vertices[vertex1][vertex2] = cost
		graph.vertices[vertex2][vertex1] = cost
		fmt.Println("Added edge: ", vertex1, " ", vertex2, " ", cost)
	} else {
		fmt.Println("Cannot add edge: ", vertex1, " ", vertex2, " ", cost)
	}
	//graph.vertices[vertex1] = append(graph.vertices[vertex1], Edge{vertex: vertex2, cost: cost})
	//graph.vertices[vertex2] = append(graph.vertices[vertex2], Edge{vertex: vertex1, cost: cost})
	graph.rwlock.Unlock()
}

func (graph *Graph) removeEdge(vertex1 string, vertex2 string /*, cost int*/) {
	defer graph.wg.Done()
	graph.rwlock.Lock()
	time.Sleep(3 * time.Second)
	_, ok1 := graph.vertices[vertex1]
	_, ok2 := graph.vertices[vertex2]
	if ok1 && ok2 {
		delete(graph.vertices[vertex1], vertex2)
		delete(graph.vertices[vertex2], vertex1)
		fmt.Println("Removed edge: ", vertex1, " ", vertex2)
	} else {
		fmt.Println("Cannot remove edge: ", vertex1, " ", vertex2)
	}
	//delete(graph.vertices[vertex1], Edge{vertex: vertex2, cost: cost})
	//delete(graph.vertices[vertex2], Edge{vertex: vertex1, cost: cost})
	graph.rwlock.Unlock()
}

func (graph *Graph) addVertex(vertex string) {
	defer graph.wg.Done()
	graph.rwlock.Lock()
	time.Sleep(3 * time.Second)
	//graph.vertices[vertex] = make(map[Edge]bool)
	_, ok := graph.vertices[vertex]
	if !ok {
		graph.vertices[vertex] = make(map[string]int)
		fmt.Println("Added vertex: ", vertex)
	} else {
		fmt.Println("Cannot add vertex: ", vertex)
	}
	graph.rwlock.Unlock()
}

func (graph *Graph) removeVertex(vertex string) {
	defer graph.wg.Done()
	graph.rwlock.Lock()
	time.Sleep(3 * time.Second)
	_, ok := graph.vertices[vertex]
	if ok {
		delete(graph.vertices, vertex)
		fmt.Println("Removed vertex: ", vertex)
	} else {
		fmt.Println("Cannot remove vertex: ", vertex)
	}
	graph.rwlock.Unlock()
}

func (graph *Graph) changeCost(vertex1 string, vertex2 string, new_cost int) {
	defer graph.wg.Done()
	graph.rwlock.Lock()
	time.Sleep(3 * time.Second)
	_, ok1 := graph.vertices[vertex1]
	_, ok2 := graph.vertices[vertex2]
	if ok1 && ok2 {
		_, ok1_2 := graph.vertices[vertex1][vertex2]
		_, ok2_1 := graph.vertices[vertex2][vertex1]
		if ok1_2 && ok2_1 {
			graph.vertices[vertex1][vertex2] = new_cost
			graph.vertices[vertex2][vertex1] = new_cost
			fmt.Println("Changed cost: ", vertex1, "->", vertex2, " to ", new_cost)
		} else {
			fmt.Println("Cannot Change cost: ", vertex1, "->", vertex2, " to ", new_cost)
		}
	} else {
		fmt.Println("Cannot Change cost: ", vertex1, "->", vertex2, " to ", new_cost)
	}
	graph.rwlock.Unlock()
}

func (graph *Graph) findWay(vertex1 string, vertex2 string) {
	defer graph.wg.Done()
	graph.rwlock.RLock()
	time.Sleep(3 * time.Second)
	ret_way := Way{0, make([]string, 0)}
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
		_, ok := graph.vertices[vertex]
		if ok {
			for next_vertex, cost := range graph.vertices[vertex] {
				ways_queue = append(ways_queue, Way{way.cost + cost, append(way.vertices, next_vertex)})
			}
		}
	}

	if len(ret_way.vertices) == 0 {
		fmt.Println("No path from", vertex1, " to ", vertex2)
	} else {
		for i, vertex := range ret_way.vertices {
			if i < len(ret_way.vertices)-1 {
				fmt.Print(vertex, "->")
			} else {
				fmt.Println(vertex, " Cost = ", ret_way.cost)
			}
		}
		//fmt.Println()
	}

	graph.rwlock.RUnlock()

	//return ret_way
}

func main() {
	graph := makeGraph()
	graph.wg.Add(13)
	go graph.addVertex("A")
	go graph.addVertex("B")
	go graph.addVertex("C")
	go graph.addEdge("A", "B", 1)
	go graph.addEdge("B", "C", 2)
	go graph.findWay("A", "C")
	go graph.findWay("A", "B")
	go graph.findWay("B", "C")
	go graph.changeCost("A", "B", 2)
	go graph.removeEdge("B", "C")
	go graph.findWay("A", "C")
	go graph.findWay("A", "B")
	go graph.removeVertex("C")
	graph.wg.Wait()
	fmt.Println("First part ended")
	graph.wg.Add(3)
	go graph.findWay("A", "C")
	go graph.findWay("A", "B")
	go graph.findWay("B", "C")
	graph.wg.Wait()
	fmt.Println("Second part ended")
}
