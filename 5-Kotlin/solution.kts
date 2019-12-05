
val program = "3,225,1,225,6,6,1100,1,238,225,104,0,1102,91,92,225,1102,85,13,225,1,47,17,224,101,-176,224,224,4,224,1002,223,8,223,1001,224,7,224,1,223,224,223,1102,79,43,225,1102,91,79,225,1101,94,61,225,1002,99,42,224,1001,224,-1890,224,4,224,1002,223,8,223,1001,224,6,224,1,224,223,223,102,77,52,224,1001,224,-4697,224,4,224,102,8,223,223,1001,224,7,224,1,224,223,223,1101,45,47,225,1001,43,93,224,1001,224,-172,224,4,224,102,8,223,223,1001,224,1,224,1,224,223,223,1102,53,88,225,1101,64,75,225,2,14,129,224,101,-5888,224,224,4,224,102,8,223,223,101,6,224,224,1,223,224,223,101,60,126,224,101,-148,224,224,4,224,1002,223,8,223,1001,224,2,224,1,224,223,223,1102,82,56,224,1001,224,-4592,224,4,224,1002,223,8,223,101,4,224,224,1,224,223,223,1101,22,82,224,1001,224,-104,224,4,224,1002,223,8,223,101,4,224,224,1,223,224,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,8,226,677,224,102,2,223,223,1005,224,329,1001,223,1,223,1007,226,226,224,1002,223,2,223,1006,224,344,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,359,1001,223,1,223,107,226,677,224,102,2,223,223,1006,224,374,101,1,223,223,8,677,677,224,102,2,223,223,1006,224,389,1001,223,1,223,1008,226,677,224,1002,223,2,223,1006,224,404,101,1,223,223,7,677,677,224,1002,223,2,223,1005,224,419,101,1,223,223,1108,226,677,224,1002,223,2,223,1005,224,434,101,1,223,223,1108,226,226,224,102,2,223,223,1005,224,449,1001,223,1,223,107,226,226,224,102,2,223,223,1005,224,464,101,1,223,223,1007,677,677,224,102,2,223,223,1006,224,479,101,1,223,223,1007,226,677,224,102,2,223,223,1005,224,494,1001,223,1,223,1008,226,226,224,1002,223,2,223,1005,224,509,1001,223,1,223,1108,677,226,224,1002,223,2,223,1006,224,524,1001,223,1,223,108,677,677,224,1002,223,2,223,1005,224,539,101,1,223,223,108,226,677,224,1002,223,2,223,1005,224,554,101,1,223,223,1008,677,677,224,1002,223,2,223,1006,224,569,1001,223,1,223,1107,677,677,224,102,2,223,223,1005,224,584,1001,223,1,223,7,677,226,224,102,2,223,223,1005,224,599,1001,223,1,223,8,677,226,224,1002,223,2,223,1005,224,614,1001,223,1,223,7,226,677,224,1002,223,2,223,1006,224,629,101,1,223,223,1107,677,226,224,1002,223,2,223,1005,224,644,1001,223,1,223,1107,226,677,224,102,2,223,223,1006,224,659,1001,223,1,223,107,677,677,224,1002,223,2,223,1005,224,674,101,1,223,223,4,223,99,226"

var integers = program.split(",").map { it.toInt() }
var instructionPointer = 0
var exit = false
val input = 5

while (instructionPointer < integers.size && !exit) {
  val result = evolve(instructionPointer, input, integers)
  instructionPointer = result.instructionPointer
  exit = result.exit
  integers = result.integers
}

println(integers)

data class Result(val instructionPointer: Int, val integers: List<Int>, val exit: Boolean = false)

fun evolve(instructionPointer: Int, input: Int, integers: List<Int>): Result {
  val opcodeAndModes = integers[instructionPointer].toString().padStart(5, '0')
  val opcode = opcodeAndModes.takeLast(2).toInt()
  val modes = opcodeAndModes.dropLast(2).reversed().toList().map { it.toString().toInt() }
  println("opcode " + opcode)
  println(integers)

  return when (opcode) {
    1    -> add(instructionPointer, modes, integers)
    2    -> multiply(instructionPointer, modes, integers)
    3    -> storeInput(instructionPointer, input, integers)
    4    -> outputValue(instructionPointer, modes, integers)
    5    -> jumpIfTrue(instructionPointer, modes, integers)
    6    -> jumpIfFalse(instructionPointer, modes, integers)
    7    -> lessThan(instructionPointer, modes, integers)
    8    -> equals(instructionPointer, modes, integers)
    99   -> Result(instructionPointer, integers, true)
    else -> throw IllegalArgumentException("Invalid opcode " + opcode)
  }
}

fun resolveValues(instructionPointer: Int, modes: List<Int>, integers: List<Int>): List<Int> {
  val xIdx = integers[instructionPointer + 1]
  val yIdx = integers[instructionPointer + 2]
  val targetIdx = integers.getOrElse(instructionPointer + 3, { 0 })

  val x = if (modes[0] == 1) xIdx
          else integers.getOrElse(xIdx, { 0 })
  val y = if (modes[1] == 1) yIdx
          else integers.getOrElse(yIdx, { 0 })

  return listOf(x, y, targetIdx)
}

fun add(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x, y, targetIdx) = resolveValues(instructionPointer, modes, integers)
  return Result(instructionPointer + 4, update(integers, targetIdx, x + y))
}

fun multiply(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x, y, targetIdx) = resolveValues(instructionPointer, modes, integers)
  return Result(instructionPointer + 4, update(integers, targetIdx, x * y))
}

fun storeInput(instructionPointer: Int, newValue: Int, integers: List<Int>): Result {
  val targetIdx = integers[instructionPointer + 1]
  return Result(instructionPointer + 2, update(integers, targetIdx, newValue))
}

fun outputValue(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x) = resolveValues(instructionPointer, modes, integers)
  println("OUTPUT: " + x)
  return Result(instructionPointer + 2, integers)
}

fun jumpIfTrue(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x, y) = resolveValues(instructionPointer, modes, integers)
  val newOpcodeIdx = if (x != 0) y
                     else instructionPointer + 3

  return Result(newOpcodeIdx, integers)
}

fun jumpIfFalse(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x, y) = resolveValues(instructionPointer, modes, integers)
  val newOpcodeIdx = if (x == 0) y
                     else instructionPointer + 3

  return Result(newOpcodeIdx, integers)
}

fun lessThan(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x, y, targetIdx) = resolveValues(instructionPointer, modes, integers)

  val newValue = if (x < y) 1
                 else 0

  return Result(instructionPointer + 4, update(integers, targetIdx, newValue))
}

fun equals(instructionPointer: Int, modes: List<Int>, integers: List<Int>): Result {
  val (x, y, targetIdx) = resolveValues(instructionPointer, modes, integers)

  val newValue = if (x == y) 1
                 else 0

  return Result(instructionPointer + 4, update(integers, targetIdx, newValue))
}

data class IndexValue(val idx: Int, val value: Int)

fun update(integers: List<Int>, targetIdx: Int, newValue: Int): List<Int> {
  return integers.mapIndexed { index, integer ->
    if (index == targetIdx) newValue
    else integer
  }
}
