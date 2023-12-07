package days.y2023.day07


import days.y2023.PuzzleInput
import days.y2023.PuzzleLine
import util.InputReader

typealias PuzzleLine = String
typealias PuzzleInput = List<PuzzleLine>

class Day07(val input: PuzzleInput) {
    val hands = input.map { it.toHand() }.sortedWith(::sortFn).reversed()

    fun partOne() = hands
        .sortedWith(::sortFn)
        .reversed()
        .mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.sum()

    fun partTwo(): Int {
        val thing = hands.sortedWith(::sortFnJokersWild)
            .reversed()
            .mapIndexed { index, hand -> hand to hand.bid to index + 1 }

        return hands.sortedWith(::sortFnJokersWild)
            .reversed()
            .mapIndexed { index, hand -> hand.bid * (index + 1) }
            .sum()
    }
}


fun sortFn(a: Hand, b: Hand): Int {
    // first sort by the hand type
    val handRankCompare = a.handRank.compareTo(b.handRank)

    if (handRankCompare != 0) {
        return handRankCompare
    }

    for (i in 0..4) {
        val ai = a[i]
        val bi = b[i]
        val cardCompare = ai.compareTo(bi)
        if (cardCompare != 0) {
            return cardCompare
        }
    }


    return 0
}


fun sortFnJokersWild(a: Hand, b: Hand): Int {
    val superA = a.improve()
    val superB = b.improve()

    // first sort by the hand type
    val handRankCompare = superA.handRank.compareTo(superB.handRank)
    if (handRankCompare != 0) {
        return handRankCompare
    }

    // card ranking is different for jokers wild
    for (i in 0..4) {
        val ai = a[i]
        val bi = b[i]
        val cardCompare = ai.jokerCompareTo(bi)
        if (cardCompare != 0) {
            return cardCompare
        }
    }


    return 0
}

data class Card(val value: Char) {

    fun compareTo(other: Card): Int {
        val values = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
        return values.indexOf(value).compareTo(values.indexOf(other.value))
    }

    fun jokerCompareTo(other: Card): Int {
        val jokerValues = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
        return jokerValues.indexOf(value).compareTo(jokerValues.indexOf(other.value))
    }

    override fun toString() = value.toString()


}

enum class HandType {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPair,
    OnePair,
    HighCard,
}


data class Hand(
    val a: Card,
    val b: Card,
    val c: Card,
    val d: Card,
    val e: Card,
    val bid: Int,
) {
    operator fun get(i: Int): Card {
        return when (i) {
            0 -> a
            1 -> b
            2 -> c
            3 -> d
            4 -> e
            else -> throw IllegalArgumentException("Invalid index $i")
        }
    }

    val handType = when (eqCounts(this)) {
        listOf(5) -> HandType.FiveOfAKind
        listOf(4, 1) -> HandType.FourOfAKind
        listOf(3, 2) -> HandType.FullHouse
        listOf(3, 1, 1) -> HandType.ThreeOfAKind
        listOf(2, 2, 1) -> HandType.TwoPair
        listOf(2, 1, 1, 1) -> HandType.OnePair
        listOf(1, 1, 1, 1, 1) -> HandType.HighCard
        else -> TODO("${eqCounts(this)} $a $b $c $d $e")
    }

    val handRank = when (handType) {
        HandType.FiveOfAKind -> 1
        HandType.FourOfAKind -> 2
        HandType.FullHouse -> 3
        HandType.ThreeOfAKind -> 4
        HandType.TwoPair -> 5
        HandType.OnePair -> 6
        HandType.HighCard -> 7
    }

    fun improve(): Hand {
        if (none { it == Card('J') }) {
            return this
        }

        // find every combination of other hands that do not contain jokers
        val jokerPositions = mapIndexedNotNull { index, card ->
            if (card == Card('J')) index else null
        }

        val matchingNoneJokers = nonJokers.filter { it in this }
        if (matchingNoneJokers.isEmpty()) {
            return copyBest()
        }

        val improvedHands = jokerPositions.fold(listOf(this)) { acc, pos ->
            acc.flatMap { hand -> matchingNoneJokers.map { hand.copy(pos, it) } }
        }

        return improvedHands.minWith(::sortFn)
    }


    private fun copy(at: Int, with: Card): Hand = copy(
        a = if (at == 0) with else a,
        b = if (at == 1) with else b,
        c = if (at == 2) with else c,
        d = if (at == 3) with else d,
        e = if (at == 4) with else e,
    )

    // treat a hand like a list of a set
    private val list = listOf(a, b, c, d, e)
    private val set = list.toSet()

    private fun none(fn: (card: Card) -> Boolean): Boolean = list.none { fn(it) }

    private operator fun contains(it: Card): Boolean = set.contains(it)

    private fun mapIndexedNotNull(fn: (index: Int, card: Card) -> Int?) = list.mapIndexedNotNull(fn)

    fun copyBest():Hand {
        return Hand.best(bid)
    }

    companion object {
        fun best(bid: Int = 0): Hand {
            return Hand(
                a = Card('K'),
                b = Card('K'),
                c = Card('K'),
                d = Card('K'),
                e = Card('K'),
                bid = bid
            )
        }
    }


}

val nonJokers = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2').map { Card(it) }

fun eqCounts(hand: Hand): List<Int> {
    val (a, b, c, d, e) = hand
    val groupBy = listOf(a, b, c, d, e).groupBy { it.value }
    return groupBy.values.map { it.size }.sorted().reversed()
}

fun PuzzleLine.toHand(): Hand {
    val (cards, bid) = this.split(" ")
    val (a, b, c, d, e) = cards.toCharArray()
    return Hand(
        a = Card(a),
        b = Card(b),
        c = Card(c),
        d = Card(d),
        e = Card(e),
        bid = bid.toInt()
    )
}

fun main() {
    val year = 2023
    val day = 7

    val potato = listOf<PuzzleLine>(
        "32T3K 765", "T55J5 684", "KK677 28", "KTJJT 220", "QQQJA 483"
    ).map { it.toHand() to it.toHand().improve() }


    val exampleInput: PuzzleInput = InputReader.getExampleLines(year, day)
    val puzzleInput: PuzzleInput = InputReader.getPuzzleLines(year, day)

    fun partOne(input: PuzzleInput) = Day07(input).partOne()
    fun partTwo(input: PuzzleInput) = Day07(input).partTwo()


    println("Example 1: ${partOne(exampleInput)}")
    println("Part 1: ${partOne(puzzleInput)}")
    println("Example 2: ${partTwo(exampleInput)}")
    println("Part 2: ${partTwo(puzzleInput)}")
}

