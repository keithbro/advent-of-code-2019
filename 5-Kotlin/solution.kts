
val input = "1,9,10,3,2,3,11,0,99,30,40,50"

var integers = input.split(",").map { it.toInt() }
var opcode = integers[0]
var opcode_idx = 0

println(integers)

while (opcode_idx < integers.size && opcode != 99) {
  println("opcode: " + opcode)
  val result = evolve(opcode_idx, integers)
  opcode_idx = result.opcode_idx
  opcode = result.opcode
  integers = result.integers
}

println(integers)

data class Result(val opcode_idx: Int, val opcode: Int, val integers: List<Int>)

fun evolve(opcode_idx: Int, integers: List<Int>): Result {
  val opcode = integers[opcode_idx]
  if (opcode == 99) return Result(opcode_idx, opcode, integers)

  val x_idx = integers[opcode_idx + 1]
  val y_idx = integers[opcode_idx + 2]
  val z_idx = integers[opcode_idx + 3]
  val x = integers[x_idx]
  val y = integers[y_idx]

  val z =
    when (opcode) {
      1 -> x + y
      2 -> x * y
      else -> throw IllegalArgumentException("Invalid opcode " + opcode)
    }

  return Result(opcode_idx + 4, opcode, integers.mapIndexed { index, integer ->
    if (index == z_idx) z
    else integer
  })
}
