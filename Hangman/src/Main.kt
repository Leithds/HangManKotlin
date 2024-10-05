import java.util.*
import kotlin.system.exitProcess

// Data class
class HangmanGame(wordList: List<String>, private val maxAttempts: Int = 6) {
    private val random = Random()
    private var word: String = wordList[random.nextInt(wordList.size)].uppercase()
    private val guessedLetters: MutableSet<Char> = mutableSetOf()
    private var remainingAttempts: Int = maxAttempts
    private var gameWon: Boolean = false

    // Start game loop
    fun start() {
        println("\nWelcome to Hangman!")
        println("You have a total of $maxAttempts incorrect guesses to save our boy.")
        println("\nLet's start the game!\n")

        while (remainingAttempts > 0 && !gameWon) {
            displayGameState()
            val guess = getUserGuess()
            processGuess(guess)
            checkWinCondition()
        }
        endGame()
    }

    // Check if won
    fun isGameWon(): Boolean {
        return gameWon
    }

    // Display current game state
    private fun displayGameState() {
        println("\nWord: ${getDisplayWord()}")
        println("Guessed Letters: ${guessedLetters.joinToString(", ")}")
        println("Remaining Attempts: $remainingAttempts")
        displayHangman()
    }

    // Get valid guess
    private fun getUserGuess(): Char {
        while (true) {
            print("Enter a letter to guess: ")
            val input = readlnOrNull()?.trim()?.uppercase()
            if (input.isNullOrEmpty()) {
                println("Please enter a valid letter.")
                continue
            }
            val guess = input[0]
            if (!guess.isLetter()) {
                println("Please enter a LETTER.")
                continue
            }
            if (guessedLetters.contains(guess)) {
                println("You've already guessed '$guess'. Try a different letter.")
                continue
            }
            return guess
        }
    }

    // Process user guess
    private fun processGuess(guess: Char) {
        guessedLetters.add(guess)
        if (word.contains(guess)) {
            println("Good job! '$guess' is in the word.")
        } else {
            remainingAttempts--
            println("Sorry, '$guess' is not in the word.")
        }
        displayHangman()
    }

    // Check win con
    private fun checkWinCondition() {
        gameWon = word.all { guessedLetters.contains(it) }
    }

    // End game message
    private fun endGame() {
        if (gameWon) {
            println("\nCongratulations! You guessed the word: $word")
        } else {
            println("\nGame Over! You've run out of attempts, and Steve is dead\n He had a wife and child you monster!.")
            println("The word was: $word")
        }
    }

    // Get the display word
    private fun getDisplayWord(): String {
        return word.map { if (guessedLetters.contains(it)) it else '_' }
            .joinToString(" ")
    }

    // Display the hangman ASCII art (fun fact I had difficulty levels here but, I could not get them to work with the art here in time)
    private fun displayHangman() {
        val stages = listOf(
            """
               -----
               |   |
                   |
                   |
                   |
                   |
            =========
            """.trimIndent(),
            """
               -----
               |   |
               O   |
                   |
                   |
                   |
            =========
            """.trimIndent(),
            """
               -----
               |   |
               O   |
               |   |
                   |
                   |
            =========
            """.trimIndent(),
            """
               -----
               |   |
               O   |
              /|   |
                   |
                   |
            =========
            """.trimIndent(),
            """
               -----
               |   |
               O   |
              /|\  |
                   |
                   |
            =========
            """.trimIndent(),
            """
               -----
               |   |
               O   |
              /|\  |
              /    |
                   |
            =========
            """.trimIndent(),
            """
               -----
               |   |
               O   |
              /|\  |
              / \  |
                   |
            =========
            """.trimIndent()
        )
        val index = maxAttempts - remainingAttempts
        if (index in stages.indices) {
            println(stages[index])
        }
    }
}

// Track statistics and manage games
class HangmanApp(private val wordList: List<String>) {
    private var gamesPlayed: Int = 0
    private var gamesWon: Int = 0
    private var gamesLost: Int = 0

    fun run() {
        while (true) {
            showMenu()
        }
    }

    // Main menu (added this late as it was very jarring without it)
    private fun showMenu() {
        println("\n--- Menu ---")
        println("1. Play a Game")
        println("2. View Statistics")
        println("3. Quit")
        print("Select an option (1-3): ")
        when (readlnOrNull()?.trim()) {
            "1" -> {
                playGame()
            }
            "2" -> displayStatistics()
            "3" -> {
                println("Thank you for playing Hangman! Goodbye!")
                exitProcess(0)
            }
            else -> {
                println("Invalid selection. Please choose a valid option.")
            }
        }
    }

    // Play a new game
    private fun playGame() {
        val game = HangmanGame(wordList)
        game.start()
        gamesPlayed++
        if (game.isGameWon()) {
            gamesWon++
        } else {
            gamesLost++
        }
    }

    // Display game statistics
    private fun displayStatistics() {
        println("\n--- Game Statistics ---")
        println("Total Games Played: $gamesPlayed")
        println("Total Games Won: $gamesWon")
        println("Total Games Lost: $gamesLost")
        println("------------------------")
    }
}

// Start the game
fun main() {
    val wordList = listOf( // Example words (praise Armok!)
        "Fortress", "Magma", "Caravan", "Miner", "Engraving", "Adamantine", "Cavern",
        "Workshop", "Goblin", "Artifact", "Siege", "Noble", "Dungeon", "Floodgate",
        "Mechanism", "Stockpile", "Tavern", "Beard", "Brewer", "Armok"
    )

    val app = HangmanApp(wordList)
    app.run()
}