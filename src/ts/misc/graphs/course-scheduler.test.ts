describe("Course Scheduler", () => {
  // Leetcode Question: https://leetcode.com/problems/course-schedule/
  // given a number n, there are n-1 courses 0,1,...,n-1
  // some courses have prerequisite courses, expressed as an array of pairs
  // e.g. deps  = [[1, 0]] indicates you must complete course 1 before attempting
  // course 0.
  // Write a function that returns true if it is possible to complete all courses
  // and false if not.
  const trueCase: [number, number][] = [
    [1, 0],
    [2, 1],
    [2, 5],
    [0, 3],
    [4, 3],
    [3, 5],
    [4, 5],
  ];
  const falseCase: [number, number][] = [
    [0, 3],
    [1, 0],
    [2, 1],
    [4, 5],
    [6, 4],
    [5, 6],
  ];
  test("true case", () => {
    expect(naive(trueCase)).toEqual(true);
  });
  test("false case", () => {
    expect(naive(falseCase)).toEqual(false);
  });
});

function naive(prerequisites: [number, number][]): boolean {
  const startingNodes = Array.from(uniq(prerequisites));
  const adjacency = adjacencyList(prerequisites);

  return !startingNodes.some((node) => hasCycle(node));

  function hasCycle(startNode: number): boolean {
    const queue: number[] = [];

    queue.push(...Array.from(adjacency[startNode] ?? []));

    while (queue.length > 0) {
      const node = queue.shift();
      if (node === startNode) return true;
      queue.push(...Array.from(adjacency[node] ?? []));
    }

    return false;
  }
}

function adjacencyList(prerequisites: [number, number][]) {
  const result: Record<number, Set<number>> = {};

  for (const [from, to] of prerequisites) {
    result[to] = result[to] ?? new Set<number>();
    result[to].add(from);
  }

  return result;
}

const uniq = <T>(input: T[][]) => {
  const result = new Set<T>();
  for (const row of input) {
    for (const col of row) {
      result.add(col);
    }
  }
  return result;
};
