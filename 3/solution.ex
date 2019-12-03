defmodule Point do
  def add(p1, p2) do
    [List.first(p1) + List.first(p2), List.last(p1) + List.last(p2)]
  end

  def origin() do
    [0,0]
  end

  def distance_from_origin(p) do
    abs(List.first(p)) + abs(List.last(p))
  end

  def for_direction(direction) do
    cond do
       direction === "U" -> [0,1]
       direction === "D" -> [0,-1]
       direction === "L" -> [-1,0]
       direction === "R" -> [1,0]
     end
  end
end

defmodule Direction do
  def extend_points(direction, points) do
    point = Point.for_direction(direction)

    points ++ [Point.add(point, List.last(points))]
  end
end

defmodule Vector do
  defstruct [:direction, :distance]

  def from_str(str) do
    [head, tail] = Regex.run ~r/(\w)(\d+)/, str, capture: :all_but_first
    %Vector{direction: head, distance: String.to_integer(tail)}
  end

  def points(vector, origin) do
    vector.direction |> List.duplicate(vector.distance)
                     |> Enum.reduce([origin], &Direction.extend_points/2)
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
                 |> MapSet.new
  end

  def intersection(w1, w2) do
    MapSet.intersection(Wire.points(w1), Wire.points(w2)) |> MapSet.delete(Point.origin)
  end
end

{:ok, data} = File.read "input.txt"

wires = data |> String.replace_trailing("\n", "")
             |> String.split("\n")
             |> Enum.map(&Wire.from_str/1)

Wire.intersection(List.first(wires), List.last(wires)) |> Enum.min_by(&Point.distance_from_origin/1)
                                                       |> Point.distance_from_origin
                                                       |> IO.inspect

