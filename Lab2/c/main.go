package main

import (
	"fmt"
	"math/rand"
	"time"
)

func max(x, y int) int {
	if x >= y {
		return x
	} else {
		return y
	}
}

func get_competion_winner(ki_power []int, output_channel chan int) {
	if len(ki_power) == 1 {
		output_channel <- ki_power[0]
	} else {
		ch1 := make(chan int)
		ch2 := make(chan int)
		go get_competion_winner(ki_power[0:len(ki_power)/2], ch1)
		go get_competion_winner(ki_power[len(ki_power)/2:len(ki_power)], ch2)
		winner1 := <-ch1
		winner2 := <-ch2
		fmt.Println(winner1, " vs ", winner2, " is done")
		output_channel <- max(winner1, winner2)
	}
}

func main() {
	// input
	fmt.Print("Number of competitors: ")
	var num_comp int
	fmt.Scanf("%d", &num_comp)

	// random generation of data
	source := rand.NewSource(time.Now().UnixNano())
	generator := rand.New(source)
	ki_power := make([]int, num_comp)
	for i := 0; i < len(ki_power); i++ {
		ki_power[i] = generator.Intn(100)
	}

	// printing generated data
	for i := 0; i < len(ki_power); i++ {
		fmt.Print(ki_power[i], " ")
	}
	fmt.Print("\n")

	// computing and printing the winner
	output_channel := make(chan int)
	go get_competion_winner(ki_power, output_channel)
	winner := <-output_channel
	fmt.Println("Winner ki power: ", winner)
}
