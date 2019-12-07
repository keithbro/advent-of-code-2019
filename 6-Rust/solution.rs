use std::io::{BufRead, BufReader};
use std::fs::File;

#[derive(Debug)]
struct Orbit {
    satellite: String,
    parent: String,
}

fn main() {
    let filename = "input.txt";
    let file = File::open(filename).unwrap();
    let reader = BufReader::new(file);

    let orbits = reader.lines().fold(vec![], |mut acc, result| {
        let line = result.unwrap();
        let ids : Vec<String>  = line.split(')').map(|x| x.to_string()).collect();
        let orbit = Orbit { satellite: ids[0].to_string(), parent: ids[1].to_string() };

        //println!("{}", line);
        acc.push(orbit);
        acc
        //acc.push(line.split(')'));
    });

    println!("With text:\n{:?}", orbits);
}
