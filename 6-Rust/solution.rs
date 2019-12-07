use std::io::{BufRead, BufReader};
use std::fs::File;

#[derive(Debug)]
struct Orbit {
    satellite: String,
    parent: String,
}

fn main() {
    let filename = "input.txt";

    let orbits = parse(&filename);
    println!("With text:\n{:?}", orbits);
}

fn parse(filename: &str) -> Vec<Orbit> {
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    reader.lines().map(|result| {
        let line = result.unwrap();
        let ids : Vec<String>  = line.split(')').map(|x| x.to_string()).collect();
        Orbit { satellite: ids[0].to_string(), parent: ids[1].to_string() }
    }).collect()
}
