package main

import (
	"fmt"
	"math/rand"
	"sync"
)

func sum(arr []int) int {
	sum := 0
	for i := 0; i < len(arr); i++ {
		sum += arr[i]
	}
	return sum
}

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
		prevSum := sum(barrier.arrays[0])
		for i := 1; i < len(barrier.arrays); i++ {
			curSum := sum(barrier.arrays[i])
			if prevSum != curSum {
				barrier.arraysEqual = false
				break
			}
			prevSum = curSum
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
	barrier := makeBarrier([][]int{[]int{1, 3, 1, 3, 1}, []int{1, 2, 1, 2, 1}, []int{4, 0, 1, 1, 1}}, 3)
	var wg sync.WaitGroup
	wg.Add(3)
	go changeArray(0, barrier, &wg)
	go changeArray(1, barrier, &wg)
	go changeArray(2, barrier, &wg)
	wg.Wait()
	fmt.Println("Final arrays:")
	fmt.Printf("%v", barrier.arrays)
}
