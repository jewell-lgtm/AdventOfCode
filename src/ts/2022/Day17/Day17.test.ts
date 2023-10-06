import { exampleInput, input } from "./Day17Input";

describe("Day17", function () {
  test("part one example", () => {
    expect(solvePuzzle(exampleInput, 2022)).toEqual(3068);
  });
  test("part one", () => {
    expect(solvePuzzle(input, 2022)).toEqual(3181);
  });

  // guessing this should be solved with some periodicity check
  test.skip("part two example", () => {
    expect(
      solvePuzzle(exampleInput, 1000000000000 /* this is a very big number */)
    ).toEqual(-1);
  });
});

const solvePuzzle = (input: string, maxRocks: number) => {
  const nextJet = createNextJet(input);
  const [getRockCount, nextRock] = createNextRock();
  let cols = ["aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg"];
  let rock;
  let jet;
  // let drawn;

  while (getRockCount() < maxRocks) {
    rock = nextRock();
    let falling = true;
    const highest = Math.max(...cols.map((it) => it.lastIndexOf("#")));
    let y = highest + 4;
    let x = 2;
    let prevY = y;
    cols = cols.map((it) => it.padEnd(y + rock.length, "."));
    // drawn = draw(cols, { shape: rock, x, y });
    // console.log(draw(cols, { shape: rock, x, y }));

    while (falling) {
      jet = nextJet();
      // console.log("jet", jet);
      x = moveX(cols, rock, x, y, jet);
      // drawn = draw(cols, { shape: rock, x, y });
      // console.log(draw(cols, { shape: rock, x, y }));
      y = moveY(cols, rock, x, y);
      falling = y !== prevY;
      prevY = y;
      // drawn = draw(cols, { shape: rock, x, y });
      // console.log(draw(cols, { shape: rock, x, y }));
    }
    cols = fallenRock(cols, rock, x, y);
    // drawn = draw(cols, { shape: [], x, y });
  }

  return Math.max(...cols.map((it) => it.lastIndexOf("#") + 1));
};

function draw(
  cols: string[],
  rock: {
    shape: string[];
    x: number;
    y: number;
  }
) {
  const rows: string[][] = [];

  for (let x = 0; x < cols.length; x++) {
    for (let y = 0; y < cols[x].length; y++) {
      if (rows[y] === undefined) rows[y] = [];
      if (rock.shape[y - rock.y]?.[x - rock.x] === "@") {
        rows[y][x] = "@";
      } else {
        rows[y][x] = cols[x][y];
      }
    }
  }

  return rows
    .reverse()
    .map((it) => it.join(""))
    .join("\n");
}
const partTwo = (input: string) => -1;

const moveX = (
  cols: string[],
  rock: string[],
  x: number,
  y: number,
  jet: "<" | ">"
) => {
  if (jet === ">") {
    if (isValidPosition(rock, cols, x + 1, y)) return x + 1;
  }
  if (jet === "<") {
    if (isValidPosition(rock, cols, x - 1, y)) return x - 1;
  }
  return x;
};

function moveY(cols: string[], rock: string[], x: number, y: number) {
  if (isValidPosition(rock, cols, x, y - 1)) return y - 1;
  return y;
}

function isValidPosition(
  rock: string[],
  cols: string[],
  startX: number,
  startY: number
) {
  if (startX < 0) return false;
  if (startY < 0) return false;
  for (let y = 0; y < rock.length; y++) {
    for (let x = 0; x < rock[y].length; x++) {
      if (startX + x >= cols.length) return false;
      const rockCell = rock[y][x];
      if (rockCell === ".") continue;
      const gridCell = cols[startX + x][startY + y];
      if (gridCell === "#") return false;
    }
  }
  return true;
}

const createNextJet = (input: string) => {
  let i = -1;
  return () => input[++i % input.length] as "<" | ">";
};

const createNextRock = () => {
  // prettier-ignore
  const rocks = [
        [
            "@@@@"
        ],
        [
            ".@.",
            "@@@",
            ".@."
        ],
        [
            "..@",
            "..@",
            "@@@",
        ].reverse(),
        [
            "@",
            "@",
            "@",
            "@"
        ],
        [
            "@@",
            "@@"
        ],
    ];
  let i = 0;

  return [
    () => i,
    () => {
      const results = rocks[i % rocks.length];
      i++;
      return results;
    },
  ] as const;
};

function fallenRock(
  cols: string[],
  rock: string[],
  startX: number,
  startY: number
) {
  for (let y = 0; y < rock.length; y++) {
    for (let x = 0; x < rock[y].length; x++) {
      if (rock[y][x] === ".") continue;
      cols[startX + x] =
        cols[startX + x].slice(0, startY + y) +
        "#" +
        cols[startX + x].slice(startY + y + 1);
    }
  }
  return cols;
}
