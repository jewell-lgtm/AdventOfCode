describe("Depth First Search", () => {
  it("visits all nodes in the expected order", () => {
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
    const expected = [0, 1, 3, 4, 5, 2, 6, 8, 7];
    const actual = dfs(adjacencyList, 0);
    expect(actual).toEqual(expected);
  });
});

function dfs(adjacencyList: number[][], start: number) {
  const queue = [start];
  const answer = new Set([start]);

  while (queue.length > 0) {
    const vertex = queue.shift();
    const edges = adjacencyList[vertex];
    for (const edge of edges) {
      if (!answer.has(edge)) {
        queue.push(edge);
      }
      answer.add(edge);
    }
  }

  return Array.from(answer);
}
