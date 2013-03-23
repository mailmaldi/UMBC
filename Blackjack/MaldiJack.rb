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
  attr_accessor :hands, :amount , :is_playing , :has_split, :player_number
  # when player is initialized, we also initialize the first hand
  # for splitting, I will have to break up the first hand into the 2nd hand and make the main game loop play proper
  def initialize(amount, player_number)
    @amount = amount
    @is_playing = true
    @player_number = player_number
    @hands= Array.new
    @hands[0] = Hand.new(0,Array.new)
    has_split = false
  end

  def reset()
    @hands = Array.new
    @hands[0] = Hand.new(0,Array.new)
    @is_playing = true
    has_split = false
  end

  def print_Player()
    print "Player : #{player_number} ,  Amount : #{amount} , "
    hands[0].print_hand
  end
end

class Blackjack

  attr_accessor :players, :num_decks , :deck, :deck_index, :max_deck_mod, :dealer , :cards
  def initialize()

    @players =  Array.new # Players are held in an array
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
    n = -1
    while n <= 0 or n > 7
      print "Enter a positive number (1-7) please : "
      n = gets.to_i
    end

    ## Get the number of Decks we're playing with
    puts "How many Decks ? :"
    decks = -1
    while decks <= 0 or decks > 8
      print "Enter a positive number (1-8) please : "
      decks = gets.to_i
      @num_decks = decks
    end

    ## Initialize all the players with  $1000 , a player id
    for i in 0...n
      @players[i] = Player.new( 1000,  i)
      #puts @players[i].to_s
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
    return @cards[temp_index % @max_deck_mod]
  end # end get_card

  def play_maldijack()
    # game will continue until people have run out of money
    while (true)
      betting_round()
      playing_round()
    end
  end

  def bookkeeping_before_round()
    ### At the start of each betting round do the following
    ### reset players hands and other local variables
    ### check if they got the money to play -TODO future, check for mimimum bet
    ### if no money then remove the player & keep playing till no players are left on the table
    @players.each do | player|
      player.reset()
    end

    @players.delete_if{| player| player.amount <= 0}

    if @players.size == 0
      puts "Go get some more Players"
      exit()
    end
  end # end bookkeeping

  def betting_round()

    # bookeeping resets players hands and removes players with no money
    bookkeeping_before_round()
    puts "#### THERE WE BEGIN ####"

    # Dealer gets 2 cards
    @dealer.hands[0].cards = [get_card, get_card]
    puts "|        DEALER CARD  |#{@dealer.hands[0].cards[0]}|  | #{@dealer.hands[0].cards[1]} |"
    # get players bets & then give them 2 cards
    @players.each do | player|

      player.hands[0].cards = [get_card, get_card]

      while (player.hands[0].bet <= 0 or player.hands[0].bet > player.amount)
        print "Player #{player.player_number}, enter bet amount between 1 & #{player.amount} : "
        player.hands[0].bet = gets.to_i
      end
      player.amount = player.amount - player.hands[0].bet
      player.print_Player

    end

    # Print all the hands ( hide the dealers 2nd card???)
    print_game()

  end

  def playing_round()

    @players.each do | p|
      puts "###### PLAYER #{p.player_number} ######"
      while p.is_playing

        ## first do all this for p.hands[0] then if necessary for p.hands[1] if split was true
        play_internally(p,0)

        if p.has_split
          play_internally(p,1)
        end

      end # end while player is playing
    end # end for each player

    distribute_money()
  end # end playing round

  # generic function that handles common stuff for both hands[0] & [1]
  def play_internally(player,hand_index)
    p = player
    i = hand_index

    while p.hands[i].is_playing
      if p.hands[i].blackjack()
        puts "Blackjack was acheived!"
        p.hands[i].print_hand()
        p.hands[i].is_playing = false
        break
      end # end blackjack if

      p.hands[i].print_hand()
      print "Please choose from the following {hit, stay, split, double}: "
      decision = gets.chomp

      if decision == "hit"
        p.hands[i].cards.push(get_card)
      elsif decision ==  "stay"
        p.hands[i].is_playing = false
      elsif decision == "split"
        puts "Not implemented split functionality, sorry :("
        ## create p.hands[1] here after checking if he can indeed split
      elsif decision == "double"
        if p.hands[i].bet * 2 <= p.amount and p.hands[i].cards.length == 2 ## can double after split so not putting that condition
          p.hands[i].bet *= 2
          p.hands[i].cards.push(get_card)
          p.hands[i].is_playing = false
          p.hands[i].print_hand()
          puts "Double was called"
        else
          puts "Double not allowed as not enough money in account or not first round"
        end

      end# end hit, stay, split, double if

      if p.hands[i].value() == 21
        puts "21 was acheived!"
        p.hands[i].print_hand()
        p.hands[i].is_playing = false
      elsif p.hands[i].value() > 21
        puts "You got bust!"
        p.hands[i].print_hand()
        p.hands[i].is_playing = false
      end

    end # end while hand is being played out

  end # end playing internally

  def print_game()

    puts "+===========================+"
    puts "|         GAME STATE        |"
    puts "+===========================+"
    puts "|        DEALER CARD  #{@dealer.hands[0].cards[0]}  #{@dealer.hands[0].cards[1]} "
    puts "+===========================+"

    @players.each do | player|
      puts "|        PLAYER: #{player.player_number}   |"
      player.hands[0].print_hand
      if player.has_split
        player.hands[1].print_hand
      end
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
      @players.each do | p|
        if p.hands[0].blackjack()
          #dealer bust, player blackjack
          p.amount += (p.hands[0].bet * 1.5)
          puts "Player #{p.player_number} got blackjack and has #{p.amount} left in their account"
        elsif p.hands[0].value < 21
          # dealer bust, player safe
          p.amount += 2*p.hands[0].bet
          puts "Player #{p.player_number} won and has #{p.amount} left in their account"
        else
          # both got busted, just return bet amount
          p.amount += p.hands[0].bet
          puts "Player #{p.player_number} bust with dealer and has #{p.amount} left in their account"
        end
      end
    else
      @players.each do | p|
        if p.hands[0].blackjack()
          p.amount += (p.hands[0].bet * 2.5)
          puts "Player #{p.player_number} got blackjack and has #{p.amount} left in their account"
        elsif p.hands[0].value() > dealer_value and p.hands[0].value() <= 21
          p.amount += 2*p.bet
          puts "Player #{p.player_number} won and has #{p.amount} left in their account"
        elsif p.hands[0].value() == dealer_value
          p.amount += p.bet
          puts "Player #{p.player_number} drew and has #{p.amount} left in their account"
        else
          puts "Player #{p.player_number} lost and has #{p.amount} left in their account"
        end

      end
    end

  end

end

blackjack = Blackjack.new()
