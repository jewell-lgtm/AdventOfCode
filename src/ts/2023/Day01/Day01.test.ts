import { first, get, last, reverse, sortBy, sum } from "lodash";
import { exampleInput, exampleInput2, input } from "./Day01input";

import { ok as assertOkay } from "assert";

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

  test("eightwo", () => {
    const solution = partTwo("eightwo\neightwo");
    expect(solution).toEqual(164);
  });

  test("PartTwo", () => {
    const solution = partTwo(input);
    expect(solution).toEqual(54087); // not sure why this is wrong
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
  const names = lines.map((it) => firstLastDigitOrNumber(it));
  const digits = names.map((it) => it.map((name) => toDigit(name)));
  const numbers = digits.map(([first, last]) => parseInt(`${first}${last}`));

  return sum(numbers);
};

const numbers = ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
const sortedDigits = sortBy(
  ["one", "two", "three", "four", "five", "six", "seven", "eight", "nine"],
  (it) => it.length
);
const firstLastDigitOrNumber = (input: string) => {
  const search = [...numbers, ...sortedDigits];
  let firstDigit: string;
  let buffer = "";
  for (const char of input) {
    buffer += char;
    const match = search.find((it) => buffer.endsWith(it));
    if (match) {
      firstDigit = match;
      break;
    }
  }
  assertOkay(firstDigit!);
  buffer = "";
  const backwardsSearch = search.map((it) => reverseString(it));
  for (const char of reverseString(input)) {
    buffer += char;
    const match = backwardsSearch.find((it) => buffer.endsWith(it));
    if (match) {
      return [firstDigit, reverseString(match)];
    }
  }
  throw new Error("couldn't find a match");
};

const reverseString = (input: string) => reverse(input.split("")).join("");

const toDigit = (input: string) =>
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
