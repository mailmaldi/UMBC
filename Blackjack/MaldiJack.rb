SUITE = [2, 3, 4, 5, 6, 7, 8, 9, 10, "J", "Q", "K", "A"]
# simply *4 to get 1 full deck and *4*n to get n decks and then .shuffle a few times

class Hand
  attr_accessor :cards, :bet , :is_playing, :hits
  def initialize(bet,cards)
    @is_playing= true
    @bet = bet
    @cards = cards
    @hits = 0
  end

  # Gets the value of the hand
  def value()
    sum = 0
    numAces = 0
    @cards.each do |i|
      if [ "J", "Q", "K" ].include?(i)
        sum += 10
      elsif i == "A"
        numAces += 1
      else
        sum += i
      end
    end # end cards.each
    #now we have the hard sum, and number of aces, find the closest sum to 21
    if numAces > 0
      tempSum1 = 21 - (sum + 11 + numAces -1)
      tempSum2 = 21 - (sum + numAces)
      if tempSum1 < 0
        sum = sum + numAces
      else
        sum = sum + 11 + numAces -1
      end
    end # end numAces>0
    return sum
  end #end value()

  # gets the hardest value possible i.e. consider all Aces as 1
  def hard_value()
    sum = 0
    numAces = 0
    @cards.each do |i|
      if ["Q", "J", "K"].include?(i)
        sum += 10
      elsif i == "A"
        sum += 1
      else
        sum += i
      end
    end # end cards.each
    return sum
  end

  def print_hand()
    print "Hand : "
    @cards.each {|card| print "#{card} "}
    print ", Value = #{value()}\n"
  end

  def blackjack()
    return (value() == 21)
  end

end #end class Hand

class Player
  attr_accessor :hands, :amount , :is_playing
  # when player is initialized, we also initialize the first hand
  # for splitting, I will have to break up the first hand into the 2nd hand and make the main game loop play proper
  def initialize(amount, player_number)
    @amount = amount
    @is_playing = true
    @player_number = player_number
    @hands= Array.new
    @hands[0] = Hand.new(bet,cards)
  end

  def reset()
    @hands = Array.new
    @is_playing = true
  end
end

class Blackjack
  def initialize()

    @players =  {} # Players are held in an array
    @num_decks = 1 # Number of card decks

    @deck = Array.new # Universal cards that are held with the dealer
    @deck_index = 0
    @max_deck_mod = 52
    @dealer = Player.new(0, -1) # Dealer is a special kind of player with infinite money & a special player id

    initialize_maldijack()
    play_maldijack()

  end

  def initialize_maldijack()

    puts "### Welcome to Maldi's BlackJack! ###"

    ## Get the number of Players
    puts "How many Players ? :"
    n = 0
    while n <= 0 and n > 7
      print "Enter a positive number (1-7) please : "
      n = gets.to_i
    end

    ## Get the number of Decks we're playing with
    puts "How many Decks ? :"
    decks = 0
    while decks <= 0 and decks > 8
      print "Enter a positive number (1-8) please : "
      @num_decks = gets.to_i
    end

    ## Initialize all the players with  $1000 , a player id
    for i in 1...n+1
      @players[i] = Player.new( 1000,  i)
    end

    ## Initializing the card deck
    @cards = SUITE * @num_decks * 4
    @max_deck_mod = @max_deck_mod * @num_decks
    5.times {@cards.shuffle! }

  end # end initializing the game

  # Gets a random card from the deck
  def get_card()
    ## TODO, once deck_index is reaching max, we should reshuffle!!!
    temp_index = @deck_index
    @deck_index += 1
    return cards[temp_index % @max_deck_mod]
  end # end get_card

  def play_maldijack()
    # game will continue until people have run out of money
    while (true)
      betting_round()
      playing_round()
    end
  end

  def betting_round()

    @players.each do |k, p|
      p.reset()
    end

    @dealer.cards = [get_card, get_card]

    # Check if players are bust
    @players.delete_if{|k, p| p.amount <= 0}

    if @players.size == 0
      puts "Game Over"
      exit()
    end

    puts "+===========================+"
    puts "|         NEW ROUND         |"
    puts "+===========================+"

    # give each player 2 initial cards and get their bets
    @players.each do |k, p|

      p.cards = [get_card, get_card]

      while (p.bet <=0 or p.bet > p.amount)
        print "Player #{k}, please enter your bet, a number less than or equal to #{p.amount}: "
        p.bet = gets.to_i
      end

    end

    print_game()

  end

  def playing_round()

    @players.each do |k, p|
      puts "###### PLAYER #{k} ######"
      while p.is_playing

        if p.blackjack()
          puts "Blackjack was acheived!"
          p.print_hand()
          p.is_playing = false
          break
        end

        p.print_hand()
        print "Please choose from the following {hit, stay, split, double}: "
        decision = gets.chomp

        if decision == "hit"
          p.cards.push(get_card)
        elsif decision ==  "stay"
          p.is_playing = false
        elsif decision == "split"
          puts "Not implemented split functionality, sorry :("
        elsif decision == "double"
          if p.bet * 2 <= p.amount and p.cards.length == 2
            p.bet *= 2
            p.cards.push(get_card)
            p.is_playing = false
            p.print_hand()
            puts "Double was called"
          else
            puts "Double not allowed as not enough money in account or not first round"
          end

        end

        if p.value() == 21
          puts "21 was acheived!"
          p.print_hand()
          p.is_playing = false
        elsif p.value() > 21
          puts "You got bust!"
          p.print_hand()
          p.is_playing = false
        end

      end
    end

    distribute_money()
  end

  def print_game()

    puts "+===========================+"
    puts "|         GAME STATE        |"
    puts "+===========================+"
    puts "|        DEALER CARD  |#{@dealer.cards[0]}|    |"
    puts "+===========================+"

    @players.each do |k, p|
      puts "|        PLAYER: #{k}   #{p.cards[0]}, #{p.cards[1]}   |"
      puts "+===========================+"
    end

  end

  def distribute_money()

    puts "We reached the end of the round"

    # Dealer sorts out their cards
    while @dealer.value() < 17
      @dealer.cards.push(get_card)
    end
    puts "The dealer got the following hand:"
    @dealer.print_hand()

    dealer_value = @dealer.value()

    # Calculating the gains and losses for each player

    if dealer_value > 21
      puts "The dealer lost."
      @players.each do |k, p|
        if p.blackjack()
          p.amount += (p.bet * 1.5)
          puts "Player #{k} got blackjack and has #{p.amount} left in their account"
        else
          p.amount += p.bet
          puts "Player #{k} won and has #{p.amount} left in their account"
        end
      end
    else
      @players.each do |k, p|
        if p.blackjack()
          p.amount += (p.bet * 1.5)
          puts "Player #{k} got blackjack and has #{p.amount} left in their account"
        elsif p.value() > dealer_value and p.value() <= 21
          p.amount += p.bet
          puts "Player #{k} won and has #{p.amount} left in their account"
        elsif p.value() == dealer_value
          puts "Player #{k} drew and has #{p.amount} left in their account"
        else
          p.amount -= p.bet
          puts "Player #{k} lost and has #{p.amount} left in their account"
        end

      end
    end

  end

end

blackjack = Blackjack.new()
