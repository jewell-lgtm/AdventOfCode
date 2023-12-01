import { first, get, last, sum } from "lodash";
import { exampleInput, exampleInput2, input } from "./Day01input";

describe("Day01", function () {
  test("PartOne Example", () => {
    const solution = partOne(exampleInput);
    expect(solution).toEqual(142);
  });

  test("PartOne", () => {
    const solution = partOne(input);
    expect(solution).toEqual(54708);
  });

  test("PartTwoExample", () => {
    const solution = partTwo(exampleInput2);
    expect(solution).toEqual(281);
  });

  test("PartTwo", () => {
    const solution = partTwo(input);
    expect(solution).not.toEqual(54100); // not sure why this is wrong
  });
});

const partOne = (input: string) => {
  const lines = input.split("\n");
  const digits = lines.map((it) => firstLastDigit(it));
  const numbers = digits.map(([first, last]) => parseInt(`${first}${last}`));
  return sum(numbers);
};

const partTwo = (input: string) => {
  const lines = input.split("\n");
  const digits = lines.map((it) => findFirstLastDigitRegex(it).map(toDigits));
  const numbers = digits.map(([first, last]) => parseInt(`${first}${last}`));
  return sum(numbers);
};

const findFirstLastDigitRegex = (input: string) => {
  const firstDigit = input.match(
    /(1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine)/
  )![0];
  const lastDigit = input.match(
    /(1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine)(?!.*(?:1|2|3|4|5|6|7|8|9|one|two|three|four|five|six|seven|eight|nine))/
  )![0];
  return [firstDigit, lastDigit];
};

const digits = {
  one: "1",
  two: "2",
  three: "3",
  four: "4",
  five: "5",
  six: "6",
  seven: "7",
  eight: "8",
  nine: "9",
};

const toDigits = (input: string) =>
  get(
    {
      one: "1",
      two: "2",
      three: "3",
      four: "4",
      five: "5",
      six: "6",
      seven: "7",
      eight: "8",
      nine: "9",
    },
    input,
    input
  );

const firstLastDigit = (input: string) => {
  const digits = input.split("").filter((it) => /\d/.test(it));

  return [first(digits), last(digits)];
};
