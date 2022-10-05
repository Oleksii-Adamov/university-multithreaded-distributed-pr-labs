package main

import (
	"fmt"
	"math/rand"
	"reflect"
	"sync"
)

type Barrier struct {
	arrays           [][]int
	numParties       int
	count            int
	notificationChan chan bool
	arraysEqual      bool
	mut              sync.Mutex
}

func makeBarrier(arrays [][]int, numParties int) *Barrier {
	var barrier Barrier
	barrier.arrays = arrays
	barrier.numParties = numParties
	barrier.notificationChan = make(chan bool, 0)
	return &barrier
}

func (barrier *Barrier) await() {
	barrier.mut.Lock()
	notificationChanRef := barrier.notificationChan
	barrier.count++
	if barrier.count == barrier.numParties {
		barrier.arraysEqual = true
		for i := 0; i < len(barrier.arrays)-1; i++ {
			if !reflect.DeepEqual(barrier.arrays[i], barrier.arrays[i+1]) {
				barrier.arraysEqual = false
				break
			}
		}
		barrier.reset()
		barrier.mut.Unlock()
		close(notificationChanRef)
	} else {
		barrier.mut.Unlock()
		<-notificationChanRef
	}
}

func (barrier *Barrier) reset() {
	barrier.notificationChan = make(chan bool, 0)
	barrier.count = 0
}

func changeArray(arrIndex int, barrier *Barrier, wg *sync.WaitGroup) {
	defer wg.Done()
	for {
		barrier.await()
		if !barrier.arraysEqual {
			index := rand.Intn(len(barrier.arrays[arrIndex]))
			action := rand.Intn(2)
			if action == 0 {
				fmt.Println(arrIndex, ") inc ", index)
				barrier.arrays[arrIndex][index]++
			} else {
				fmt.Println(arrIndex, ") dec ", index)
				barrier.arrays[arrIndex][index]--
			}
		} else {
			break
		}
	}
}

func main() {
	barrier := makeBarrier([][]int{[]int{2, 1, 1, 2, 1}, []int{1, 1, 1, 1, 1}, []int{1, 1, 1, 1, 1}}, 3)
	var wg sync.WaitGroup
	wg.Add(3)
	go changeArray(0, barrier, &wg)
	go changeArray(1, barrier, &wg)
	go changeArray(2, barrier, &wg)
	wg.Wait()
	fmt.Println("Final arrays:")
	fmt.Printf("%v", barrier.arrays)
}
