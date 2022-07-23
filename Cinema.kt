package cinema

const val CLASS1_SEAT = 10
const val CLASS2_SEAT = 8
const val SMALL_THEATER = 60
const val MENU = "\n1. Show the seats" +
                 "\n2. Buy a ticket" +
                 "\n3. Statistics" +
                 "\n0. Exit"

class Cinema(private val rows: Int, private val seats: Int) {
    private val seatLayout = List(rows) { MutableList(seats) { 'S' } }
    private var currentIncome = 0

    private fun totalIncome() = if (rows * seats <= SMALL_THEATER) rows * seats * CLASS1_SEAT else (rows / 2 * CLASS1_SEAT + (rows - rows / 2) * CLASS2_SEAT) * seats

    private fun purchasedTickets() = seatLayout.flatten().count { it == 'B' }

    fun ticketPrice(row: Int) = if (rows * seats > SMALL_THEATER && row > rows / 2) CLASS2_SEAT else CLASS1_SEAT

    fun bookSeat(row: String, seat: String): Boolean {
        if (!Regex("\\d+").matches(row) || !Regex("\\d+").matches(seat) || row.toInt() !in 1..rows || seat.toInt() !in 1..seats) {
            println("Wrong input!")
            return false
        }
        else if (seatLayout[row.toInt() - 1][seat.toInt() - 1] == 'B') {
            println("That ticket has already been purchased!")
            return false
        }
        else {
            seatLayout[row.toInt() - 1][seat.toInt() - 1] = 'B'
            currentIncome += ticketPrice(row.toInt())
            return true
        }
    }

    fun statistics() = "\nNumber of purchased tickets: ${purchasedTickets()}\n" +
                       "Percentage: ${"%.2f".format(100 * purchasedTickets().toDouble() / (rows * seats))}%\n" +
                       "Current income: $$currentIncome\n" +
                       "Total income: $${totalIncome()}"

    override fun toString(): String {
        var seatMap = "\nCinema:\n "
        for (seatNumber in 1..seats) seatMap += " $seatNumber"
        for (row in 0 until rows) seatMap += "\n${row + 1} ${seatLayout[row].joinToString(" ")}"
        return seatMap
    }
}

fun buyTicket(theater: Cinema) {
    var rowNumber: String
    do {
        println("\nEnter a row number:")
        rowNumber = readln()
        println("Enter a seat number in that row:")
        val seatNumber = readln()
    } while (!theater.bookSeat(rowNumber, seatNumber))
    println("\nTicket price: $${theater.ticketPrice(rowNumber.toInt())}")
}

fun main() {
    println("Enter the number of rows:")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    val seats = readln().toInt()
    val mainTheater = Cinema(rows, seats)
    while (true) {
        println(MENU)
        when (readln()) {
            "0" -> break
            "1" -> println(mainTheater)
            "2" -> buyTicket(mainTheater)
            "3" -> println(mainTheater.statistics())
        }
    }
}