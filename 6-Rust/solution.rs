use std::collections::HashMap;
use std::io::{BufRead, BufReader};
use std::fs::File;

#[derive(Debug)]
struct Orbit {
    satellite: String,
    parent: String,
}

fn main() {
    let filename = "input.txt";

    let orbit_map = parse(&filename);
    let orbit_counts = get_orbit_counts(orbit_map);
    println!("With text:\n{:?}", orbit_counts);
}

fn get_orbit_count(orbit_map: &HashMap<String, String>, body: &str) -> u32 {
    let mut x = body;
    let mut count = 0;

    while orbit_map.contains_key(x) {
        count += 1;
        x = orbit_map.get(x).unwrap();
    }

    count
}

fn get_orbit_counts(orbit_map: HashMap<String, String>) -> u32 {
    orbit_map.values().fold(0, |mut count, satellite| {
        count += get_orbit_count(&orbit_map, &satellite);
        count
    })
}

fn parse(filename: &str) -> HashMap<String, String> {
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    reader.lines().fold(HashMap::new(), |mut acc, result| {
        let line = result.unwrap();
        let ids : Vec<String> = line.split(')').map(|x| x.to_string()).collect();
        acc.insert(ids[1].to_string(), ids[0].to_string());
        acc
    })
}
