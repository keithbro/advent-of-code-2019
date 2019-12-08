use std::collections::{HashMap, HashSet};
use std::io::{BufRead, BufReader};
use std::iter::FromIterator;
use std::fs::File;

#[derive(Debug)]
struct Orbit {
    satellite: String,
    parent: String,
}

fn main() {
    let filename = "input.txt";

    let orbit_map = parse(&filename);
    let orbit_counts = get_orbit_counts(&orbit_map);
    println!("{:?}", orbit_counts);

    let my_orbits : HashSet<String> = HashSet::from_iter(get_orbits(&orbit_map, &"YOU"));
    println!("{:?}", my_orbits);
    let santas_orbits : HashSet<String> = HashSet::from_iter(get_orbits(&orbit_map, &"SAN"));
    println!("{:?}", santas_orbits);
    let min_orbital_transfers = my_orbits.symmetric_difference(&santas_orbits).count();
    println!("{:?}", min_orbital_transfers);
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

fn get_orbit_counts(orbit_map: &HashMap<String, String>) -> usize {
    let mut orbit_counts : HashMap<String, usize> = HashMap::new();

    for satellite in orbit_map.keys() {
        if orbit_counts.contains_key(satellite) { continue; }

        let mut orbits = get_orbits(&orbit_map, &satellite);
        orbits.insert(0, satellite.to_string());
        orbit_counts = orbits.iter().rev().enumerate().fold(orbit_counts, |mut acc, (i, s)| {
            acc.insert(s.to_string(), i);
            acc
        });
    }

    orbit_counts.values().sum()
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
