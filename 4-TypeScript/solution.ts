import * as assert from 'assert';

const countOccurrences = (acc: Map<string, number>, cur: string, idx: number, src: Array<string>): Map<string, number> => {
  const prev : string | null = src[idx - 1];
  if (prev === cur) { acc.set(cur, (acc.get(cur) || 1) + 1) }

  return acc;
}

const areGroupsPresent = (i: number) : boolean => {
  const digits = i.toString().split('');
  const digitCounts = digits.reduce(countOccurrences, new Map());
  const values = Array.from(digitCounts.values());

  return values.some(x => x > 1);
};

const arePairsPresent = (i: number) : boolean => {
  const digits = i.toString().split('');
  const digitCounts = digits.reduce(countOccurrences, new Map());
  const values = Array.from(digitCounts.values());

  return values.some(x => x === 2);
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

const countMatching = (min: number, max: number) : object => {
  let counts = { one: 0, two: 0 };

  for (let i = min; i <= max; i++) {
    if (digitsIncreaseOrStayTheSame(i)) {
      if (areGroupsPresent(i)) {
        counts.one++;
      }
      if (arePairsPresent(i)) {
        counts.two++;
      }
    }
  }

  return counts;
};

console.log("Tests");
assert.strictEqual(arePairsPresent(123456), false);
assert.strictEqual(arePairsPresent(122456), true);
assert.strictEqual(arePairsPresent(112233), true);
assert.strictEqual(arePairsPresent(123444), false);
assert.strictEqual(arePairsPresent(111122), true);
assert.strictEqual(arePairsPresent(123451), false);
assert.strictEqual(arePairsPresent(111111), false);

assert.strictEqual(areGroupsPresent(123456), false);
assert.strictEqual(areGroupsPresent(122456), true);
assert.strictEqual(areGroupsPresent(112233), true);
assert.strictEqual(areGroupsPresent(123444), true);
assert.strictEqual(areGroupsPresent(111122), true);
assert.strictEqual(areGroupsPresent(123451), false);
assert.strictEqual(areGroupsPresent(111111), true);

assert.strictEqual(digitsIncreaseOrStayTheSame(123456), true);
assert.strictEqual(digitsIncreaseOrStayTheSame(123455), true);
assert.strictEqual(digitsIncreaseOrStayTheSame(123454), false);
assert.strictEqual(digitsIncreaseOrStayTheSame(112233), true);
assert.strictEqual(digitsIncreaseOrStayTheSame(123444), true);
assert.strictEqual(digitsIncreaseOrStayTheSame(111122), true);

assert.deepEqual(countMatching(100000, 100010), { one: 0, two: 0 });
assert.deepEqual(countMatching(111111, 111121), { one: 9, two: 0 });
assert.deepEqual(countMatching(123456, 234567), { one: 1022, two: 727 });
assert.deepEqual(countMatching(130254, 678275), { one: 2090, two: 1419 });

console.log("All tests have passed.");

console.log(countMatching(130254, 678275));
