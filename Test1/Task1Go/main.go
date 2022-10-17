package main

import (
	"fmt"
	"sync"
	"time"
)

type Ship struct {
	id         int
	cargoQueue chan int
}

type Harbor struct {
	cargoQueue chan int
	docksSem   chan int
}

func makeShip(id int, capacity int, cargoArr []int) *Ship {
	ship := Ship{cargoQueue: make(chan int, capacity), id: id}
	for i := 0; i < len(cargoArr); i++ {
		ship.cargoQueue <- cargoArr[i]
	}
	return &ship
}

func makeHarbor(numDocks int, capacity int, cargoArr []int) *Harbor {
	harbor := Harbor{cargoQueue: make(chan int, capacity), docksSem: make(chan int, numDocks)}
	for i := 0; i < len(cargoArr); i++ {
		harbor.cargoQueue <- cargoArr[i]
	}
	return &harbor
}

func (harbor *Harbor) moor(ship *Ship, numToGive int, numToTake int, external_wg *sync.WaitGroup) {
	defer external_wg.Done()

	harbor.docksSem <- 0
	fmt.Println(ship.id, " moored")
	time.Sleep(1000 * time.Microsecond)
	var wg sync.WaitGroup

	putRoutine := func() {
		for numToGive > 0 {
			time.Sleep(1000 * time.Microsecond)
			cargo := <-ship.cargoQueue
			harbor.cargoQueue <- cargo
			fmt.Println(cargo, " taken from ship ", ship.id)
			numToGive--
		}
		wg.Done()
	}

	takeRoutine := func() {
		for numToTake > 0 {
			time.Sleep(1000 * time.Microsecond)
			cargo := <-harbor.cargoQueue
			ship.cargoQueue <- cargo
			fmt.Println(cargo, " given to ship ", ship.id)
			numToTake--
		}
		wg.Done()
	}

	wg.Add(2)
	go putRoutine()
	go takeRoutine()
	wg.Wait()

	fmt.Println(ship.id, " unmoored")
	<-harbor.docksSem
}

func main() {
	harbor := makeHarbor(2, 10, []int{1, 2, 3, 4, 5})
	var wg sync.WaitGroup
	wg.Add(3)
	go harbor.moor(makeShip(1, 2, []int{6, 7}), 2, 2, &wg)
	go harbor.moor(makeShip(2, 3, []int{8, 9, 10}), 2, 1, &wg)
	go harbor.moor(makeShip(3, 2, []int{11, 12}), 2, 2, &wg)
	wg.Wait()
}
