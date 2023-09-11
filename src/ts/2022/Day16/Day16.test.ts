import { exampleInput, input } from "./Day16Input";

describe("Day16", function () {
  test("example one", () => {
    expect(partOne(exampleInput)).toEqual(1651);
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

const partOne = (input: string) => {
  const { flowRate, tunnels } = parseInput(input);
  const memoTable = new Map<string, number>();

  function calcPressure(open: Set<string>) {
    let result = 0;
    for (const openValve of open) {
      result += flowRate.get(openValve) ?? 0;
    }
    return result;
  }

  function explore(
    open: Set<string>,
    position: string,
    pressureReleased: number,
    timeRemaining: number,
    pathTaken: string[]
  ): number {
    const memoKey = `${[...open]
      .sort()
      .join(",")}:${position}:${timeRemaining}`;
    if (memoTable.has(memoKey)) return memoTable.get(memoKey)!;

    pathTaken = [...pathTaken, position];
    const newTime = timeRemaining - 1;
    if (newTime <= 0) return pressureReleased;
    const newPressure = calcPressure(open) + pressureReleased;
    const possibilities: ReturnType<typeof explore>[] = [];

    if (!open.has(position) && (flowRate.get(position) ?? 0) > 0) {
      possibilities.push(
        explore(add(open, position), position, newPressure, newTime, pathTaken)
      );
    }
    const valveTunnels = tunnels.get(position);
    if (valveTunnels) {
      possibilities.push(
        ...valveTunnels.map((tunnel) => {
          return explore(open, tunnel, newPressure, newTime, pathTaken);
        })
      );
    }

    const result = Math.max(...possibilities);
    memoTable.set(memoKey, result);
    return result;
  }

  return explore(new Set(), "AA", 0, 30, []);
};

const partTwo = (input: string) => 123;

const parseInput = (
  input: string
): { flowRate: Map<string, number>; tunnels: Map<string, Array<string>> } => {
  const parsed = input.split("\n").map((line): [string, number, string[]] => {
    const [_, valve, flowRateStr, tunnelsStr] =
      /Valve (\w+) has flow rate=(\d+); tunnels? leads? to valves? (.*)/.exec(
        line
      ) ?? [];

    return [
      valve,
      parseInt(flowRateStr),
      tunnelsStr.split(", ").filter((v) => v !== ""),
    ];
  });

  const flowRate = new Map<string, number>();
  const tunnels = new Map<string, Array<string>>();

  for (const [valve, valveFlowRate, valveTunnels] of parsed) {
    flowRate.set(valve, valveFlowRate);
    tunnels.set(valve, valveTunnels);
  }

  return { flowRate, tunnels };
};

const add = <T>(set: Set<T>, val: T) => {
  const clone = new Set(set);
  clone.add(val);
  return clone;
};
