#!/bin/env perl

use strict;
use warnings;
use v5.30;

my $filename = 'input.txt';

open(my $fh, '<:encoding(UTF-8)', $filename) or die $!;

my $row = <$fh>;

my @integers = split /,/, $row;
$integers[1] = 12;
$integers[2] = 2;

print "Input is: ". join ',', @integers;

my $opcode_idx = 0;
my $opcode = "";
while($opcode_idx < scalar @integers && $opcode ne "99") {
    ($opcode_idx, $opcode) = evolve($opcode_idx, \@integers);
    say "Output is: " . join ',', @integers;
}

sub evolve {
    my ($opcode_idx, $integers) = @_;
    say "opcode_idx: $opcode_idx";

    my $opcode = $integers->[$opcode_idx];
    say "opcode: $opcode";
    return $opcode_idx, $opcode if $opcode eq "99";

    my $x = $integers->[$integers->[$opcode_idx + 1]];
    my $y = $integers->[$integers->[$opcode_idx + 2]];
    my $dest_idx = $integers->[$opcode_idx + 3];

    my $z = {
        "1"  => sub { $x + $y },
        "2"  => sub { $x * $y },
    }->{$opcode}();

    $integers->[$dest_idx] = $z;

    return ($opcode_idx + 4, $opcode);
}
