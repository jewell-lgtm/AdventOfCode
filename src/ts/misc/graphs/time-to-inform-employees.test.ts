describe("Graphs Question: Employees", () => {
  // https://leetcode.com/problems/time-needed-to-inform-all-employees/
  // represents who is manager of whom, e.g. employee 0 is managed by employee 2,
  // employee 2 is managed by employee 4, who is the head of the company,
  // therefore has a manager of -1
  const managers = [
    /* 0 */ 2, /* 1 */ 2, /* 2 */ 4, /* 3 */ 6, /* 4 */ -1, /* 5 */ 4,
    /* 6 */ 4, /* 7 */ 5,
  ];
  // the length of time it takes for each manager to inform their subordinates of news
  const informTime = [
    /* 0 */ 0, /* 1 */ 0, /* 2 */ 4, /* 3 */ 0, /* 4 */ 7, /* 5 */ 3, /* 6 */ 6,
    /* 7 */ 0,
  ];

  it("can construct an adjacency list for the graph", () => {
    const expected = [
      /* 0 */ [],
      /* 1 */ [],
      /* 2 */ [0, 1],
      /* 3 */ [],
      /* 4 */ [2, 5, 6],
      /* 5 */ [7],
      /* 6 */ [3],
      /* 7 */ [],
    ];
    const actual = adjacencyList(managers);
    expect(actual).toEqual(expected);
  });

  it("can use dfs calculate the total time it takes to inform all employees of news", () => {
    const head = 4;
    const actual = dfs(adjacencyList(managers), informTime, head);

    expect(actual).toEqual(13);
  });
});

function adjacencyList(input: number[]) {
  const result = arrFill(input.length, (): number[] => []);

  for (const [employee, manager] of pairs(input)) {
    if (manager === -1) continue;
    result[manager].push(employee);
  }

  return result;
}

function dfs(
  adjacencyList: number[][],
  informTime: number[],
  startNode: number
) {
  function performSearch(node: number, prevTime: number) {
    const subordinates = adjacencyList[node];
    const timeTaken = informTime[node];
    if (subordinates.length === 0) return timeTaken + prevTime;
    const childTimes = subordinates.map((sub) =>
      performSearch(sub, prevTime + timeTaken)
    );
    return Math.max(...childTimes);
  }

  return performSearch(startNode, 0);
}

const arrFill = <T>(length: number, fill: (index: number) => T) => {
  const result: T[] = [];
  for (let i = 0; i < length; i++) {
    result.push(fill(i));
  }
  return result;
};

const pairs = <T>(list: T[]): [number, T][] =>
  Object.entries(list).map(([indexStr, val]) => [parseInt(indexStr), val]);
