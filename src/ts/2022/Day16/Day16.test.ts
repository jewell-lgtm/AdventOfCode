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

    expect(potentialFlowRate(30, "AA", "AA")).toEqual(0);
    expect(potentialFlowRate(30, "AA", "BB")).toEqual(
      // (time remaining - travel time - 1 minute to open) * flow rate
      (30 - 1 - 1) * 2
    );
    expect(potentialFlowRate(30, "AA", "CC")).toEqual((30 - 2 - 1) * 10);
    expect(potentialFlowRate(2, "AA", "BB")).toEqual(0);
    expect(potentialFlowRate(2, "AA", "CC")).toBeLessThan(0);
  });
  test("example one", () => {
    expect(partOne(exampleInput)).toEqual(1651);
  });

  test("part one", () => {
    expect(partOne(input)).toEqual(2320);
  });

  // way too slow, and returns the wrong answer
  test.skip("example two", () => {
    expect(partTwo(exampleInput)).toEqual(1707);
  });
  test.skip("part two", () => {
    expect(partTwo(input)).toEqual(21756);
  });
});

const partOne = (input: string) => {
  const { flowRate, distances, valves } = parseInput(input);

  const potentialFlowRate = createPotentialFlowRate(distances, flowRate);
  const distanceTo = (from: string, to: string) =>
    distances.get(from)!.get(to)!;
  const closed = (open: Set<any>) =>
    [...valves.values()].filter((v) => !open.has(v) && flowRate.get(v)! > 0);

  let bestPressure = 0;
  const queue = [
    { position: "AA", releasedPressure: 0, open: new Set(), timeRemaining: 30 },
  ];

  let i = 0;
  while (queue.length) {
    const { position, releasedPressure, open, timeRemaining } = queue.shift()!;
    const check = closed(open);
    if (++i % 10000 === 0)
      console.log({
        check: check.length,
        i,
        length: queue.length,
        bestPressure,
        timeRemaining,
      });
    for (const newPosition of check) {
      const newTimeRemaining =
        timeRemaining - distanceTo(position, newPosition) - 1;
      if (newTimeRemaining >= 0) {
        const newPressure =
          releasedPressure +
          potentialFlowRate(timeRemaining, position, newPosition);
        const newOpen = add(open, newPosition);
        bestPressure = Math.max(bestPressure, newPressure);
        queue.push({
          position: newPosition,
          releasedPressure: newPressure,
          open: newOpen,
          timeRemaining: newTimeRemaining,
        });
      }
    }
  }

  return bestPressure;
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

const add = <T>(set: Set<T>, ...vals: T[]) =>
  vals.reduce((acc, val) => {
    acc.add(val);
    return acc;
  }, new Set(set));

const partTwo = (input: string) => {
  return 123;
};
