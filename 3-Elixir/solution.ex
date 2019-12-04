defmodule Point do
  def add(p1, p2) do
    [List.first(p1) + List.first(p2), List.last(p1) + List.last(p2)]
  end

  def origin() do
    [0,0]
  end

  def distance_from_origin(point) do
    point |> Enum.map(&Kernel.abs/1) |> Enum.sum
  end

  def distance_map(points, wires) do
    wires |> Enum.map(fn wire -> Wire.distance_map(wire, points) end)
          |> DistanceMap.merge
  end
end

defmodule Direction do
  def extend_points(direction, points) do
    point = Direction.to_point(direction)

    points ++ [Point.add(point, List.last(points))]
  end

  def to_point(direction) do
    cond do
       direction === "U" -> [0,1]
       direction === "D" -> [0,-1]
       direction === "L" -> [-1,0]
       direction === "R" -> [1,0]
     end
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

  def extend_points(vector, points) do
    points ++ List.delete_at(Vector.points(vector, List.last(points)), 0)
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
    wire.vectors |> Enum.reduce([wire.origin], &Vector.extend_points/2)
  end

  def intersection(wires) do
    wires |> Enum.map(&Wire.points/1)
          |> Enum.map(&MapSet.new/1)
          |> Enum.reduce(MapSet.new(%{}), fn map_set, acc -> if MapSet.size(acc) === 0, do: map_set, else: MapSet.intersection(acc, map_set) end)
          |> MapSet.delete(Point.origin)
  end

  def distance_map(wire, points) do
    wire |> Wire.points
         |> Enum.with_index
         |> Enum.reduce(%{}, fn {wire_point, idx}, acc ->
              if Enum.member?(points, wire_point) do
                Map.put(acc, wire_point, idx)
              else
                acc
              end
            end)
  end
end

defmodule DistanceMap do
  def minimum(distance_map) do
    Enum.min_by(distance_map, fn {_, d} -> d end)
  end

  def merge(distance_maps) do
    Enum.reduce(distance_maps, %{}, fn distance_map, acc -> Map.merge(acc, distance_map, fn _, d1, d2 -> d1 + d2 end) end)
  end
end

{:ok, data} = File.read "input.txt"

wires = data |> String.replace_trailing("\n", "")
             |> String.split("\n")
             |> Enum.map(&Wire.from_str/1)

intersection = Wire.intersection(wires)

intersection |> Enum.min_by(&Point.distance_from_origin/1)
             |> Point.distance_from_origin
             |> IO.inspect

intersection |> Point.distance_map(wires)
             |> DistanceMap.minimum
             |> IO.inspect
