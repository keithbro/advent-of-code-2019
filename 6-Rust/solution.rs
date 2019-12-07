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
    println!("{:?}", orbit_counts);
}

fn get_orbits(orbit_map: &HashMap<String, String>, body: &str) -> Vec<String> {
    let mut cur = body;
    let mut orbits = vec![];

    while orbit_map.contains_key(cur) {
        cur = orbit_map.get(cur).unwrap();
        orbits.push(cur.to_string());
    }

    orbits
}

fn get_orbit_counts(orbit_map: HashMap<String, String>) -> usize {
    orbit_map.keys().fold(0, |mut count, satellite| {
        let orbits = get_orbits(&orbit_map, &satellite);
        count += orbits.len();
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
