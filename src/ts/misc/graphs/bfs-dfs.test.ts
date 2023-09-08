// numeric representation of a graph.
// each item in the array represents the connections each node has,
// e.g. node 0 is connected to nodes 1 & 3, node 1 to node 0 etc
const adjacencyList = [
  [1, 3],
  [0],
  [3, 8],
  [0, 4, 5, 2],
  [3, 6],
  [3],
  [4, 7],
  [6],
  [2],
];

describe("Breadth First Search", () => {
  it("visits all nodes in the expected order", () => {
    const expected = [0, 1, 3, 4, 5, 2, 6, 8, 7];
    const actual = bfs(adjacencyList);
    expect(actual).toEqual(expected);
  });
});

describe("Depth First Search", () => {
  it("visits all nodes in the expected order", () => {
    const expected = [0, 1, 3, 4, 6, 7, 5, 2, 8];
    const actual = dfs(adjacencyList);
    expect(actual).toEqual(expected);
  });
});

function bfs(adjacencyList: number[][], start = 0) {
  const queue = [start];
  const seen = new Set([start]);

  while (queue.length > 0) {
    const vertex = queue.shift();
    const edges = adjacencyList[vertex];
    for (const edge of edges) {
      if (!seen.has(edge)) {
        queue.push(edge);
      }
      seen.add(edge);
    }
  }

  return Array.from(seen);
}

function dfs(adjacencyList: number[][], start = 0) {
  const seen = new Set([start]);

  function searchVertex(vertex: number) {
    seen.add(vertex);
    const edges = adjacencyList[vertex];
    for (const edge of edges) {
      if (!seen.has(edge)) {
        searchVertex(edge);
      }
    }
  }

  searchVertex(start);

  return Array.from(seen);
}
