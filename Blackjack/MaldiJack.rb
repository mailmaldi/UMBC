SUITE = [2, 3, 4, 5, 6, 7, 8, 9, 10, "J", "Q", "K", "A"]
# simply *4 to get 1 full deck and *4*n to get n decks and then .shuffle a few times

require Hand
require Player

class Blackjack

  attr_accessor :players, :num_decks , :deck, :deck_index, :max_deck_mod, :dealer , :cards
  def initialize()

    @players =  Array.new # Players are held in an array
    @num_decks = 1 # Number of card decks

    @deck = Array.new # Universal cards that are held with the dealer
    @deck_index = 0
    @max_deck_mod = 52
    @dealer = Player.new(0, -1) # Dealer is a special kind of player with infinite money & a special player id

  end # end initialize

  def play_game()
    initialize_maldijack() # get number of players & decks and initialize their classes & shoe!
    while (true)
      betting_round() # every player places a bet and gets 2 cards in return, we exit() if no more players left with any money
      playing_round() # every player plays, then dealer plays & then money is doled out
    end # end while
  end # end play_game

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

  ### TODO this can be easily extended to add more decks if the current shoe is running below some threshold! Just add a function to refresh @cards & @deck_index
  # Gets a random card from the deck
  def get_card()
    ## TODO, once deck_index is reaching max, we should reshuffle!!!
    temp_index = @deck_index
    @deck_index += 1
    return @cards[temp_index % @max_deck_mod]
  end # end get_card

  def bookkeeping_before_betting()
    ### At the start of each betting round do the following
    ### reset players hands and other local variables
    ### check if they got the money to play -TODO future, check for mimimum bet
    ### if no money then remove the player & keep playing till no players are left on the table
    @players.each do | player|
      player.reset()
      if player.amount <=0
        @players.delete(player)
      end
    end

    if @players.size == 0
      puts "Go get some more Players"
      exit()
    end
  end # end bookkeeping

  def betting_round()

    # bookeeping resets players hands and removes players with no money
    bookkeeping_before_betting()
    puts "#### THERE WE BEGIN ####"

    # Dealer gets 2 cards, we could do dealing cards in round-robin, but thats just additional work, doesnt really matter, just an extra block of code
    @dealer.hands[0].cards = [get_card, get_card]
    puts "DEALER CARDS  #{@dealer.hands[0].cards[0]} #{@dealer.hands[0].cards[1]} " # Hide cards[1] later, this is for debugging

    # get players bets & then give them 2 cards
    @players.each do | player|
      while (player.hands[0].bet <= 0 or player.hands[0].bet > player.amount)
        print "Player #{player.player_number}, enter bet amount between 1 & #{player.amount} : "
        player.hands[0].bet = gets.to_i
      end
      player.amount = player.amount - player.hands[0].bet # reduce player's available amount by bet amount
      player.hands[0].cards = [get_card, get_card] # give player 2 cards
      player.print_Player # print player

    end # end for each player, at this point we consider only players who still have money

  end # end betting round

  def playing_round()

    @players.each do | p|
      puts "###### PLAYER #{p.player_number} ######"

      ## first do all this for p.hands[0] then if necessary for p.hands[1] if split was true
      puts "PLAYER #{p.player_number} Hand 0 START"
      play_internally(p,0)
      puts "PLAYER #{p.player_number} Hand 0 END"

      if p.has_split == true
        puts "PLAYER #{p.player_number} Hand 1 START"
        play_internally(p,1)
        puts "PLAYER #{p.player_number} Hand 1 END"
      end

      puts "PLAYER #{p.player_number} DONE Playing"

    end # end for each player

    distribute_money()
  end # end playing round

  # generic function that handles common stuff for both hands[0] & [1]
  def play_internally(player,hand_index)
    p = player
    i = hand_index

    while p.hands[i].is_playing

      #if blackjack, no need to play further, only a moron would hit more, they'd obviously stand.
      if p.hands[i].blackjack()
        puts "Blackjack!"
        p.hands[i].print_hand()
        p.hands[i].is_playing = false
        break
      end # end blackjack if

      p.hands[i].print_hand()
      print "Please choose from the following {hit, stand, split, double}: "
      decision = gets.chomp

      if decision == "hit"
        p.hands[i].cards.push(get_card)
        p.hands[i].print_hand
      elsif decision ==  "stand"
        p.hands[i].is_playing = false
        p.hands[i].print_hand
      elsif decision == "split"
        ## split is allowed if,
        ## 1. player has equivalent bet amount
        ## 2. has_split is false
        ## 3. number of cards == 2
        ## 4. the 2 cards are of same value if integer and if a String, then

        if p.has_split == false and  i == 0  and p.hands[i].cards.length == 2 and p.hands[i].bet  <= p.amount and validate_split_cards(p.hands[i].cards[0],p.hands[i].cards[0])
          p.hands[1] = Hand.new(p.hands[i].bet, Array.new)  # create new hand
          p.hands[1].cards.push(p.hands[0].cards[0])      # push a card from 0 to 1
          p.hands[0].cards.delete_at(0)                   # delete the card from hand 0
          p.has_split = true                              # set split flag
          p.hands[0].push(get_card)
          p.hands[1].push(get_card)
          p.hands[0].print_hand
          p.hands[1].print_hand
        else
          puts "Player #{p.player_number} Split  call was denied on hand #{i}"
        end
        ## create p.hands[1] here after checking if he can indeed split
      elsif decision == "double"
        ## for doubling, it is enough that player has bet amount left in amount & has taken no hit, i.e. length == 2
        ## Player can double his hand after splitting so not putting that condition
        if p.hands[i].cards.length == 2 and p.hands[i].bet  <= p.amount
          p.hands[i].bet *= 2 # double the bet
          p.amount = p.amount - p.hands[i].bet # reduce the available amount
          p.hands[i].cards.push(get_card) # take one more card
          p.hands[i].is_playing = false # stand down
          p.hands[i].print_hand()
          puts "Player #{p.player_number} has called Double on his hand #{i}"
        else
          puts "Player #{p.player_number} Double call was denied on hand #{i}"
        end

      end# end hit, stand, split, double if

      # If busted, can't play further
      if p.hands[i].value() > 21
        puts "You got bust!"
        p.hands[i].print_hand()
        p.hands[i].is_playing = false
      end

    end # end while hand is being played out

  end # end playing internally

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

  end # end distribute money

  def validate_split_cards(card1,card2)
    arr = ["J","Q","K"]
    if card1.is_a?Integer and card2.is_a?Integer
      return (card1 == card2)
    elsif card1 == 'A' and card2 == 'A'
      return true
    elsif arr.any? { |s| s.include?(card1) } and arr.any? { |s| s.include?(card2) }
      return true
    else
      return false
    end
  end # end validate_splittable_cards

end # End BlackJack class

blackjack = Blackjack.new()
blackjack.play_game()