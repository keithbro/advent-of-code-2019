defmodule Point do
  defstruct [:x, :y]

  def add(p1, p2) do
    %Point{x: p1.x + p2.x, y: p1.y + p2.y}
  end

  def origin() do
    %Point{x: 0, y: 0}
  end
end

defmodule Vector do
  defstruct [:direction, :distance]

  def from_str(str) do
    [head, tail] = Regex.run ~r/(\w)(\d+)/, str, capture: :all_but_first
    %Vector{direction: head, distance: String.to_integer(tail)}
  end

  def points(vector, origin) do
    direction_point = cond do
      vector.direction === "U" -> %Point{x:  0, y:  1}
      vector.direction === "D" -> %Point{x:  0, y: -1}
      vector.direction === "L" -> %Point{x: -1, y:  0}
      vector.direction === "R" -> %Point{x:  1, y:  0}
    end

    direction_point |> List.duplicate(vector.distance)
                    |> Enum.reduce([origin], fn p, acc -> acc ++ [Point.add(p, List.last(acc))] end)
  end
end

defmodule Wire do
  defstruct [:vectors, origin: Point.origin]

  def from_arr(arr) do
    %Wire{vectors: Enum.map(arr, &Vector.from_str/1)}
  end

  def from_str(str) do
    str |> String.split(",")
        |> Wire.from_arr
  end

  def points(wire) do
    wire.vectors |> Enum.reduce([wire.origin], fn v, acc -> acc ++ List.delete_at(Vector.points(v, List.last(acc)), 0) end)
  end
end


{:ok, data} = File.read "input.txt"

wires = data |> String.replace_trailing("\n", "")
             |> String.split("\n")
             |> Enum.map(&Wire.from_str/1)
wire = List.first(wires)
Wire.points(wire) |> Enum.each(fn x -> IO.inspect x end)
#IO.inspect Vector.points(Vector.from_str("U12"), Point.origin)

#points = Enum.reduce(List.first(paths), fn x, acc -> x <> acc end)

#IO.inspect points


