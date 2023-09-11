import { exampleInput, input } from "./Day16Input";

describe("Day16", function () {
  test("getting the distances", () => {
    const input = `
Valve AA has flow rate=0; tunnel leads to valve BB
Valve BB has flow rate=0; tunnels lead to valves CC, AA
Valve CC has flow rate=0; tunnels lead to valves BB
    `.trim();
    const { distances } = parseInput(input);
    expect(distances).toEqual(
      new Map([
        [
          "AA",
          new Map([
            ["AA", 0],
            ["BB", 1],
            ["CC", 2],
          ]),
        ],
        [
          "BB",
          new Map([
            ["AA", 1],
            ["BB", 0],
            ["CC", 1],
          ]),
        ],
        [
          "CC",
          new Map([
            ["AA", 2],
            ["BB", 1],
            ["CC", 0],
          ]),
        ],
      ])
    );
  });
  test("getting the potential flow rate for a valve", () => {
    const input = `
Valve AA has flow rate=0; tunnel leads to valve BB
Valve BB has flow rate=2; tunnels lead to valves AA, CC
Valve CC has flow rate=10; tunnel leads to valve BB
     `.trim();
    const { distances, flowRate } = parseInput(input);
    const potentialFlowRate = createPotentialFlowRate(distances, flowRate);
    const bestValve = createBestValve(distances, flowRate);
    expect(potentialFlowRate(30, "AA", "AA")).toEqual(0);
    expect(potentialFlowRate(30, "AA", "BB")).toEqual(
      // (time remaining - travel time - 1 minute to open) * flow rate
      (30 - 1 - 1) * 2
    );
    expect(potentialFlowRate(30, "AA", "CC")).toEqual((30 - 2 - 1) * 10);
    expect(potentialFlowRate(2, "AA", "BB")).toEqual(0);
    expect(potentialFlowRate(2, "AA", "CC")).toBeLessThan(0);

    // bestValve uses potential flow rate
    expect(bestValve(new Set(["AA", "BB", "CC"]), "AA", 30)).toEqual("CC");
    expect(bestValve(new Set(["AA", "BB"]), "AA", 30)).toEqual("BB");
    expect(bestValve(new Set(["AA", "BB", "CC"]), "AA", 3)).toEqual("BB");
  });
  test("example one", () => {
    expect(partOne(exampleInput)).toEqual(1651);
  });

  test("part one", () => {
    expect(partOneImplOne(input)).toEqual(5506);
  });

  test("example two", () => {
    expect(partTwo(exampleInput)).toEqual(140);
  });
  test("part two", () => {
    expect(partTwo(input)).toEqual(21756);
  });
});

const partOne = (input: string) => {
  const { flowRate, tunnels, distances, valves } = parseInput(input);
  let open = new Set<string>([...valves]);
  let position = "AA";
  let releasedPressure = 0;
  let timeRemaining = 30;

  const bestValve = createBestValve(distances, flowRate);
  const potentialFlowRate = createPotentialFlowRate(distances, flowRate);
  const distanceTo = (other: string) => distances.get(position)!.get(other)!;

  while (timeRemaining) {
    const nextValve = bestValve(open, position, timeRemaining);
    open.delete(nextValve);
    releasedPressure += potentialFlowRate(timeRemaining, position, nextValve);
    timeRemaining -= distanceTo(nextValve) - 1;
  }

  return releasedPressure;
};

const createBestValve = (
  distances: Map<string, Map<string, number>>,
  flowRate: Map<string, number>
) => {
  const potentialFlowRate = createPotentialFlowRate(distances, flowRate);
  return function bestValve(
    open: Set<string>,
    position: string,
    timeRemaining: number
  ) {
    return maxBy(open, (valve) =>
      potentialFlowRate(timeRemaining, position, valve)
    );
  };
};

const createPotentialFlowRate =
  (
    distances: Map<string, Map<string, number>>,
    flowRate: Map<string, number>
  ) =>
  (timeRemaining: number, currentPosition: string, other: string) => {
    const distanceToOther = distances.get(currentPosition)!.get(other)!;
    const flowRateOfOther = flowRate.get(other)!;

    const remainingTimeAfterOpening = timeRemaining - distanceToOther - 1;
    return remainingTimeAfterOpening * flowRateOfOther;
  };

const maxBy = <T>(
  collection: Iterable<T>,
  toComparable: (item: T) => number
): T => {
  const array = Array.from(collection);
  if (array.length < 1) {
    throw new Error("can't compare empty array");
  }
  let best: T = array.pop()!;
  let highestScore = toComparable(best);
  while (array.length) {
    const compare = array.pop()!;
    const newScore = toComparable(compare);
    if (newScore > highestScore) {
      highestScore = newScore;
      best = compare;
    }
  }
  return best;
};

const partOneImplOne = (input: string) => {
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
    const memoKey = pathTaken.join(",");
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

const parseInput = (input: string) => {
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

  const valves = new Set<string>();
  const flowRate = new Map<string, number>();
  const tunnels = new Map<string, Array<string>>();

  for (const [valve, valveFlowRate, valveTunnels] of parsed) {
    valves.add(valve);
    flowRate.set(valve, valveFlowRate);
    tunnels.set(valve, valveTunnels);
  }

  const distances = calcDistances(valves, tunnels);

  return { valves, flowRate, tunnels, distances };
};

const calcDistances = (valves: Set<string>, tunnels: Map<string, string[]>) => {
  const distances = new Map<string, Map<string, number>>();
  for (const start of valves) {
    const distanceMap = new Map<string, number>();
    distances.set(start, distanceMap);
    for (const end of valves) {
      distanceMap.set(end, calcDistance(start, end));
    }
  }

  return distances;

  function calcDistance(start: string, end: string) {
    const visited = new Set<string>();
    const queue = [{ valve: start, distance: 0 }];
    while (queue.length > 0) {
      const { valve, distance } = queue.shift()!;
      if (valve === end) return distance;
      visited.add(valve);
      for (const tunnel of tunnels.get(valve) ?? []) {
        if (!visited.has(tunnel)) {
          queue.push({ valve: tunnel, distance: distance + 1 });
        }
      }
    }
    throw new Error("No path found");
  }
};

const add = <T>(set: Set<T>, val: T) => {
  const clone = new Set(set);
  clone.add(val);
  return clone;
};
const reduce = <In, Out>(
  open: Set<In>,
  accumulator: (acc: Out, valve: In) => Out,
  start: Out
) => Array.from(open).reduce(accumulator, start);
