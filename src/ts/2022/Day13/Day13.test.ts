import { exampleInput, input } from "./Day13Input";

describe("Day13", function () {
  test("example one", () => {
    expect(partOne(exampleInput)).toEqual(13);
  });

  test("part one", () => {
    expect(partOne(input)).toEqual(5506);
  });

  test("example two", () => {
    expect(partTwo(exampleInput)).toEqual(140);
  });
  test("part two", () => {
    expect(partTwo(input)).toEqual(21756);
  });
});

const partOne = (input: string) =>
  calculatePackets(input)
    .filter((it) => it[1])
    .map((it) => it[0] + 1)
    .reduce((a, b) => a + b, 0);

const partTwo = (input: string) => {
  const newInput = [input.trim().replace("\n\n", "\n"), `[[2]]`, `[[6]]`].join(
    "\n"
  );
  const parsed = parseInput(newInput).flatMap((it) => it);

  parsed.sort((a, b) => {
    if (comparePackets(deepcopy(a), deepcopy(b))) return -1;
    return 1;
  });

  // cursed
  const index0 =
    parsed.indexOf(parsed.find((it) => JSON.stringify(it) === "[[2]]")) + 1;
  const index1 =
    parsed.indexOf(parsed.find((it) => JSON.stringify(it) === "[[6]]")) + 1;
  return index0 * index1;
};

const deepcopy = (v) => JSON.parse(JSON.stringify(v));

const calculatePackets = (input: string) =>
  parseInput(input).reduce(
    (result, [left, right], index) => [
      ...result,
      [index, comparePackets(left, right)],
    ],
    []
  );

type Packet = Number | Packet[];

const parseInput = (input: string) =>
  input
    .trim()
    .split("\n\n")
    .map((pairStr) =>
      pairStr.split("\n").map((packetStr) => JSON.parse(packetStr))
    ) as [Packet, Packet][];

const comparePackets = (left: Packet, right: Packet): null | boolean => {
  if (a(left) && a(right)) {
    if (left.length === 0 && right.length === 0) return null;
    if (left.length === 0 && right.length !== 0) return true;
    if (left.length !== 0 && right.length === 0) return false;
    const result = comparePackets(left.shift(), right.shift());
    if (result !== null) return result;
    return comparePackets(left, right);
  }
  if (n(left) && n(right)) {
    if (left < right) return true;
    if (right < left) return false;
    return null;
  }
  if (n(left) && a(right)) {
    return comparePackets([left], right);
  }
  if (a(left) && n(right)) {
    return comparePackets(left, [right]);
  }

  throw new Error(
    `unreachable: ${JSON.stringify(left)} ${JSON.stringify(right)}`
  );
};
const n = (v: unknown): v is number => typeof v === "number";
const a = <T>(v: T | T[]): v is T[] => typeof v === "object";
