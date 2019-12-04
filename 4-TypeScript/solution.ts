const minimum = 130254;
const maximum = 678275;

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

console.log("Tests");
console.log(" - areTwoAdjacentNumbersTheSame");
if (areTwoAdjacentNumbersTheSame(123456) === true) { throw new Error(); }
if (areTwoAdjacentNumbersTheSame(122456) === false) { throw new Error(); }

console.log(" - digitsIncreaseOrStayTheSame");
if (digitsIncreaseOrStayTheSame(123456) === false) { throw new Error('123456'); }
if (digitsIncreaseOrStayTheSame(123455) === false) { throw new Error('123455'); }
if (digitsIncreaseOrStayTheSame(123454) === true) { throw new Error('123454'); }

console.log("All tests have passed.");

const greeter = (person: string) => "Hello, " + person;

console.log(greeter("Keith"));
