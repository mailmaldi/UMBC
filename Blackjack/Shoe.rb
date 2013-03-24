SUITE = [2, 3, 4, 5, 6, 7, 8, 9, 10, "J", "Q", "K", "A"]
# simply *4 to get 1 full deck and *4*n to get n decks and then .shuffle a few times

class Shoe
  attr_accessor  :num_decks , :deck, :deck_index, :max_deck_mod , :cards
  def initialize()
    @num_decks = 1 # Number of card decks
    @deck_index = 0 # starting index of shoe
    @max_deck_mod = 52 # ending index  modulo of shoe with just the 1 deck
  end # end initialize

  # create_deck & get_card can be put in their own class called Shoe
  def create_deck(num_decks)
    ## Initializing the card deck
    @num_decks = num_decks
    @cards = SUITE * @num_decks * 4 ## replicate the suite 4 times to form 1 deck, and replicate 1 dec num times to form num decks in the shoe
    @max_deck_mod = @max_deck_mod * @num_decks ## figure out the max modulo, in this impl , i will just repeat cards from 0 ... modulo-1 , 0 ...
    10.times {@cards.shuffle! } ## Shuffle the shoe 10 times
    @deck_index = 0
  end # end create_deck

  # Gets a random card from the deck
  def get_card()
    card =  @cards[@deck_index % @max_deck_mod]
    @deck_index += 1
    if @deck_index == @max_deck_mod
      #recreate shoe
      puts "\n\n****CREATING NEW DECK****\n\n"
      create_deck(@num_decks  )
    end
    return card
  end # end get_card

  def print_shoe()
    print "SHOE: #{@cards.inspect}\n\n\n"
  end

end # end class