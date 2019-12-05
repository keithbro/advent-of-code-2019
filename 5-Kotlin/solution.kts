
val program = "3,0,4,0,99"

var integers = program.split(",").map { it.toInt() }
var opcode_idx = 0
var exit = false
val input = 1

println(integers)

while (opcode_idx < integers.size && !exit) {
  val result = evolve(opcode_idx, input, integers)
  opcode_idx = result.opcode_idx
  exit = result.exit
  integers = result.integers
}

println(integers)

data class Result(val opcode_idx: Int, val integers: List<Int>, val exit: Boolean = false)

fun evolve(opcode_idx: Int, input: Int, integers: List<Int>): Result {
  val opcode = integers[opcode_idx]

  return when (opcode) {
    1    -> add(opcode_idx, integers)
    2    -> multiply(opcode_idx, integers)
    3    -> storeInput(opcode_idx, input, integers)
    4    -> outputValue(opcode_idx, integers)
    99   -> Result(opcode_idx, integers, true)
    else -> throw IllegalArgumentException("Invalid opcode " + opcode)
  }
}

fun add(opcodeIdx: Int, integers: List<Int>): Result {
  val (targetIdx, newValue) = binaryFunction(opcodeIdx, integers, { a, b -> a + b })
  return Result(opcodeIdx + 4, update(integers, targetIdx, newValue))
}

fun multiply(opcodeIdx: Int, integers: List<Int>): Result {
  val (targetIdx, newValue) = binaryFunction(opcodeIdx, integers, { a, b -> a * b })
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

fun binaryFunction(opcodeIdx: Int, integers: List<Int>, action: (Int, Int) -> Int): IndexValue {
  val xIdx = integers[opcodeIdx + 1]
  val yIdx = integers[opcodeIdx + 2]
  val targetIdx = integers[opcodeIdx + 3]
  val x = integers[xIdx]
  val y = integers[yIdx]

  return IndexValue(targetIdx, action(x, y))
}

fun update(integers: List<Int>, targetIdx: Int, newValue: Int): List<Int> {
  return integers.mapIndexed { index, integer ->
    if (index == targetIdx) newValue
    else integer
  }
}
