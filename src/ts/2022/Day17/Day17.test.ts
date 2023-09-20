import { exampleInput, input } from "./Day17Input";

describe("Day16", function () {
  test("part one example", () => {
    expect(partOne(exampleInput)).toEqual(71);
  });
  test("part one", () => {
    expect(partOne(input)).toEqual(0);
  });
});

const partOne = (input: string) => {
  const nextJet = createNextJet(input);
  const nextRock = createNextRock();

  let fallenRocks = 1;
  const highest = [0, 0, 0, 0, 0, 0, 0];
  while (fallenRocks < 2022) {
    let rock = nextRock();
    let y = Math.max(...highest) + 3;
    let x = 0;
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    let jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
    jet = nextJet();
    rock = moveX(jet, rock);
    console.log("the rocks position is as follows:\n\n", drawRock(rock, y));
  }
  return 123;
};

function moveX(jet: "<" | ">", rock: string[]) {
  rock = rock.map((row) => row.padEnd(7, "."));
  if (jet === "<") {
    if (!rock.some((row) => row[0] === "#")) {
      rock = rock.map((row) => row.slice(1) + ".");
    }
  }
  if (jet === ">") {
    if (!rock.some((row) => row[row.length - 1] === "#")) {
      rock = rock.map((row) => "." + row.slice(0, row.length - 1));
    }
  }
  return rock;
}

function drawRock(rock: string[], y: number) {
  const result = [];
  for (let iY = 0; iY < y; iY++) {
    result.unshift(["."]);
  }
  for (let iRow = 0; iRow < rock.length; iRow++) {
    result.unshift(rock[iRow]);
  }
  return result.join("\n");
}

const createNextJet = (input: string) => {
  let i = -1;
  return () => input[++i % input.length] as "<" | ">";
};

const createNextRock = () => {
  let i = -1;
  const rocks = [
    ["..####"],
    ["...#.", "..###", "...#."],
    ["....#", "....#", "..###"],
    ["..#", "..#", "..#", "..#"],
    ["..##", "..##"],
  ];

  return () => rocks[++i % rocks.length];
};
