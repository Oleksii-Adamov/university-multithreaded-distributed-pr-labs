package main

import (
	"math/rand"
	"sync"
	"time"
)

type SmokersPipeline struct {
	mut        sync.Mutex
	is_smoking bool
	cond_chan  chan int
}

func NewSmokersPipeline() *SmokersPipeline {
	smokers_pipeline := new(SmokersPipeline)
	smokers_pipeline.cond_chan = make(chan int)
	return smokers_pipeline
}

func (pip *SmokersPipeline) smoker_routine(smoker_id int, ch chan int) {
	for {
		println("Smoker ", smoker_id, " is waiting")
		<-ch
		println("Smoker ", smoker_id, " is smoking for 3s")
		time.Sleep(3 * time.Second)
		pip.is_smoking = false
		pip.cond_chan <- 1
	}
}

func (pip *SmokersPipeline) mediator_routine(ch []chan int) {
	source := rand.NewSource(time.Now().UnixNano())
	generator := rand.New(source)
	for {
		for pip.is_smoking {
			<-pip.cond_chan
		}
		index := generator.Intn(len(ch))
		println("Cigarette for ", index, " is ready")
		pip.is_smoking = true
		ch[index] <- 1
	}
}

func main() {
	var ch [3]chan int
	for i := 0; i < len(ch); i++ {
		ch[i] = make(chan int)
	}
	smPip := NewSmokersPipeline()
	go smPip.mediator_routine(ch[0:3])
	for i := 0; i < len(ch); i++ {
		go smPip.smoker_routine(i, ch[i])
	}
	<-make(chan int)
}
