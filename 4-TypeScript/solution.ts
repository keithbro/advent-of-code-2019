import * as assert from 'assert';

const areTwoAdjacentNumbersTheSame = (i: number) : boolean => {
  const str = i.toString();
  return str.match(/(\d)\1/) !== null
};

const digitsIncreaseOrStayTheSame = (i: number) : boolean => {
  const digits = i.toString().split('').map(c => parseInt(c));

  let previousValue = -1;
  for (const digit of digits) {
    if (digit < previousValue) { return false }
    previousValue = digit;
  }

  return true;
};

const countMatching = (min: number, max: number) : number => {
  let count = 0;

  for (let i = min; i <= max; i++) {
    if (areTwoAdjacentNumbersTheSame(i) && digitsIncreaseOrStayTheSame(i)) {
      count++;
    }
  }

  return count;
};

console.log("Tests");
assert.strictEqual(areTwoAdjacentNumbersTheSame(123456), false);
assert.strictEqual(areTwoAdjacentNumbersTheSame(122456), true);

assert.strictEqual(digitsIncreaseOrStayTheSame(123456), true);
assert.strictEqual(digitsIncreaseOrStayTheSame(123455), true);
assert.strictEqual(digitsIncreaseOrStayTheSame(123454), false);

assert.strictEqual(countMatching(100000, 100010), 0);
assert.strictEqual(countMatching(111111, 111121), 9);

console.log("All tests have passed.");

console.log(countMatching(130254, 678275));


