#!/bin/env perl

use strict;
use warnings;
use v5.30;

my $filename = 'input.txt';

open(my $fh, '<:encoding(UTF-8)', $filename) or die $!;

my $row = <$fh>;

for (my $noun = 0; $noun < 99; $noun++) {
    for (my $verb = 0; $verb < 99; $verb++) {
        my @integers = split /,/, $row;
        my $output = eval { calc($noun, $verb, \@integers) } || "";

        if ($output eq "19690720") {
            say "noun: $noun, verb: $verb";
            say "Answer is " . (100 * $noun + $verb);
            exit 0;
        }
    }
}

sub calc {
    my ($noun, $verb, $integers) = @_;

    $integers->[1] = $noun;
    $integers->[2] = $verb;

    print "Input is: ". join ',', @$integers;

    my $opcode_idx = 0;
    my $opcode = "";
    while($opcode_idx < scalar @$integers && $opcode ne "99") {
        ($opcode_idx, $opcode) = evolve($opcode_idx, $integers);
        say "Evolution is: " . join ',', @$integers;
    }

    my $output = $integers->[0];

    say "Output is $output";

    return $output;
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
