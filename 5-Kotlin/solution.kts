
val program = "1002,4,3,4,33"

var integers = program.split(",").map { it.toInt() }
var opcodeIdx = 0
var exit = false
val input = 1

println(integers)

while (opcodeIdx < integers.size && !exit) {
  val result = evolve(opcodeIdx, input, integers)
  opcodeIdx = result.opcodeIdx
  exit = result.exit
  integers = result.integers
}

println(integers)

data class Result(val opcodeIdx: Int, val integers: List<Int>, val exit: Boolean = false)

fun evolve(opcodeIdx: Int, input: Int, integers: List<Int>): Result {
  val opcodeAndModes = integers[opcodeIdx].toString()
  val opcode = opcodeAndModes.takeLast(2).toInt()
  val modes = opcodeAndModes.dropLast(2).reversed().toList().map { it.toString().toInt() }

  return when (opcode) {
    1    -> add(opcodeIdx, modes, integers)
    2    -> multiply(opcodeIdx, modes, integers)
    3    -> storeInput(opcodeIdx, input, integers)
    4    -> outputValue(opcodeIdx, integers)
    99   -> Result(opcodeIdx, integers, true)
    else -> throw IllegalArgumentException("Invalid opcode " + opcode)
  }
}

fun add(opcodeIdx: Int, modes: List<Int>, integers: List<Int>): Result {
  val (targetIdx, newValue) = binaryFunction(opcodeIdx, modes, integers, { a, b -> a + b })
  return Result(opcodeIdx + 4, update(integers, targetIdx, newValue))
}

fun multiply(opcodeIdx: Int, modes: List<Int>, integers: List<Int>): Result {
  val (targetIdx, newValue) = binaryFunction(opcodeIdx, modes, integers, { a, b -> a * b })
  return Result(opcodeIdx + 4, update(integers, targetIdx, newValue))
}

fun storeInput(opcodeIdx: Int, newValue: Int, integers: List<Int>): Result {
  val targetIdx = integers[opcodeIdx + 1]
  return Result(opcodeIdx + 2, update(integers, targetIdx, newValue))
}

fun outputValue(opcodeIdx: Int, integers: List<Int>): Result {
  val targetIdx = integers[opcodeIdx + 1]
  println(integers[targetIdx])
  return Result(opcodeIdx + 2, integers)
}

data class IndexValue(val idx: Int, val value: Int)

fun binaryFunction(opcodeIdx: Int, modes: List<Int>, integers: List<Int>, action: (Int, Int) -> Int): IndexValue {
  val xIdx = integers[opcodeIdx + 1]
  val yIdx = integers[opcodeIdx + 2]
  val targetIdx = integers[opcodeIdx + 3]
  val x = if (modes[0] == 1) xIdx
          else integers[xIdx]
  val y = if (modes[1] == 1) yIdx
          else integers[yIdx]

  return IndexValue(targetIdx, action(x, y))
}

fun update(integers: List<Int>, targetIdx: Int, newValue: Int): List<Int> {
  return integers.mapIndexed { index, integer ->
    if (index == targetIdx) newValue
    else integer
  }
}
