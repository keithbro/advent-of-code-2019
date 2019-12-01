package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	file, err := os.Open("input1.txt")
	defer file.Close()

	if err != nil {
		fmt.Println(err)
		return
	}

  reader := bufio.NewReader(file)

	line := ""
	totalFuel := 0
	for {
		line, err = reader.ReadString('\n')
		if err != nil {
			break
		}

		mass, err := strconv.Atoi(strings.TrimSpace(line))
		if err != nil {
			fmt.Println(err)
			break
		}

		fuel := calcCumulativeFuel(mass)
		fmt.Printf("Mass: %d, Fuel: %d\n", mass, fuel)

		totalFuel += fuel
	}

	fmt.Println(totalFuel)
}

func calcCumulativeFuel(mass int) int {
	x := mass
	cumulativeFuel := 0

	for {
		fuel := calcFuel(x)
		if fuel == 0 {
			break
		}

		cumulativeFuel += fuel
		x = fuel
	}

	return cumulativeFuel
}

func calcFuel(mass int) int {
	fuel := int(float64(mass) / 3) - 2

	if fuel > 0 {
		return fuel
	} else {
		return 0
	}
}
