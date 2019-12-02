#!/bin/env perl

use strict;
use warnings;
use v5.30;

my $filename = 'input.txt';

open(my $fh, '<:encoding(UTF-8)', $filename) or die $!;

my $row = <$fh>;
print "Input is: $row";

my @integers = split /,/, $row;
my $opcode = $integers[0];

given ($opcode) {
    when("1") {
        my $x = $integers[$integers[1]];
        my $y = $integers[$integers[2]];
        my $z = $x + $y;
        my $dest = $integers[$integers[3]];
        say "Adding $x + $y (= $z) and storing it at position $dest...";
        $integers[$dest] = $z;
        say "Done."
    }
    default {
        die "Unhandled opcode ($opcode)";
    }
}

say "Output is: " . join ',', @integers;
