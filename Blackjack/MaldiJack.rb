require 'Hand.rb'
require 'Player.rb'

SUITE = [2, 3, 4, 5, 6, 7, 8, 9, 10, "J", "Q", "K", "A"]
# simply *4 to get 1 full deck and *4*n to get n decks and then .shuffle a few times

class Blackjack

  attr_accessor :players, :num_decks , :deck, :deck_index, :max_deck_mod, :dealer , :cards
  def initialize()

    @players =  Array.new # Players are held in an array
    @num_decks = 1 # Number of card decks
    @deck_index = 0 # starting index of shoe
    @max_deck_mod = 52 # ending index  modulo of shoe with just the 1 deck
    @dealer = Player.new(0, -1) # Dealer is a special kind of player with infinite money & a special player id, we use id in the play_internally later to prevent dealer from double, etc

  end # end initialize

  ## the main game play is simple. initialize and then the betting & playing rounds loop infinitely.
  def play_maldijack()
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
    end
    @num_decks = decks

    ## Initialize all the players with  $1000 , a player id
    for i in 0...n
      @players[i] = Player.new( 1000,  i)
      #puts @players[i].to_s
    end

    create_deck()

    puts "\n\n\nCOMMENT THIS OUT IN ACTUAL RUNS, ITS FOR DEBUGGING ONLY"
    print "SHOE: #{@cards.inspect}\n\n\n"

  end # end initializing the game

  # create_deck & get_card can be put in their own class called Shoe
  def create_deck()
    ## Initializing the card deck
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
      create_deck()
    end
    return card
  end # end get_card

  def betting_round()

    # bookeeping resets players hands and removes players with no money
    bookkeeping_before_betting()

    puts "\n\n\n#### THERE WE BEGIN ####\n\n"

    # Dealer gets 2 cards, we could do dealing cards in round-robin, but thats just additional work, doesnt really matter, just an extra block of code
    @dealer.hands[0].cards = [get_card, get_card]
    puts "*************** DEALER CARDS  [#{@dealer.hands[0].cards[0]}]  [__] *********************" # {@dealer.hands[0].cards[1]}

    # get players bets & then give them 2 cards
    # TODO if player bets 0 or less, maybe exit them from the game? code is short, just puts "PLAYER exiting" and delete it from the @players array
    @players.each do | player|
      while (player.hands[0].bet <= 0 or player.hands[0].bet > player.amount)
        print "Player #{player.player_number}, enter bet amount between 1 & #{player.amount} : "
        player.hands[0].bet = gets.to_i # get a bet from player which he can afford
      end
      player.amount = player.amount - player.hands[0].bet # reduce player's available amount by bet amount
      player.hands[0].cards = [get_card, get_card] # give player 2 cards
      player.print_Player # print player
      puts ""

    end # end for each player
    puts "================================================================="
  end # end betting round

  def bookkeeping_before_betting()
    # TODO future, check for mimimum bet
    @dealer.reset() ## very important to reset dealer
    @players.delete_if{|player| player.amount <= 0} ## remove broke players
    if @players.size == 0
      puts "**********WE NEED MORE PLAYERS**************"
      exit() # exit if no more players left
    end
    @players.each do | player|
      player.reset()  ## reset remaining players
    end

  end # end bookkeeping

  def playing_round()

    ## NOW play for each player and each of their hands
    @players.each do | p|
      puts "===================== PLAYER #{p.player_number} ====================="

      ## first do all this for p.hands[0] then if necessary for p.hands[1] if split was true
      puts "PLAYER #{p.player_number} Hand 0 START"
      play_internally(p,0) # play hand[0]
      puts "PLAYER #{p.player_number} Hand 0 END"

      if p.has_split == true
        puts "PLAYER #{p.player_number} Hand 1 START"
        play_internally(p,1) # play hand[1]
        puts "PLAYER #{p.player_number} Hand 1 END"
      end

      puts "PLAYER #{p.player_number} DONE Playing"

    end # end for each player

    puts "=================== ENTERED DEALER ROUND ====================================="

    @dealer.print_Player
    # Dealer will always hit until at least 17
    while @dealer.hands[0].value() < 17
      @dealer.hands[0].cards.push(get_card)
    end
    #### NOTE: comment this block if dealer needs to stop automatically at 17 , i.e. auto-stand at 17.
    if @dealer.hands[0].value() < 21
      play_internally(@dealer,0)
    end
    puts "================== FINAL DEALER HAND:======================"
    @dealer.hands[0].print_hand(0)

    @players.each do | p|
      puts "===================== PLAYER #{p.player_number} ====================="
      p.print_Player
    end

    distribute_money() #### now check every player with the dealer & distribute winnings

  end # end playing round

  # generic function that handles common stuff for both hands[0] & [1]
  def play_internally(player,hand_index)
    p = player
    i = hand_index

    while p.hands[i].is_playing

      p.hands[i].print_hand(i) # print the current hand

      #if blackjack, no need to play further, only a moron would hit more, they'd obviously stand.
      if p.hands[i].blackjack()
        p.hands[i].is_playing = false
        #p.hands[i].print_hand(i)
        puts "***Blackjack!!! No need to play further!" # TODO maybe play for a hard blackjack, in which case just comment out this BLOCK! code will be shorter
        break
      end # end blackjack if

      print "Please choose from the following {hit, stand, split, double}: "
      decision = gets.chomp

      if decision == "hit"
        p.hands[i].cards.push(get_card) # hit a new card
      elsif decision ==  "stand"
        p.hands[i].is_playing = false  # just finish this hand
      elsif decision == "split" and p.player_number >=0         # dealer cant split
        if i == 0  and p.can_split()                            # i==0 is redundant actually. p.can_split() just checks
          p.create_new_hand_for_split()                         # create new hands
          p.hands[0].cards.push(get_card)                       # offer one more card for Hand 0
          p.hands[1].cards.push(get_card)                       # offer one more card for Hand 1
          puts "Player #{p.player_number} Split  call was done on hand #{i}"
          p.print_Player                                        # print the players new set of Hands
        else
          puts "Player #{p.player_number} Split  call was denied on hand #{i}"
        end
      elsif decision == "double" and p.player_number >=0 #dealer cant double
        ## Player can double his hand after splitting so not putting that condition
        if p.can_double(i)                              # check if its ok to double, note that a split hand can indeed be doubled
          p.modify_for_double(i)                        # modify bet for doubling
          p.hands[i].cards.push(get_card)                # take one more card & stand down
          puts "Player #{p.player_number} has called Double on his hand #{i}"
          p.print_Player
        else
          puts "Player #{p.player_number} Double call was denied on hand #{i}"
        end

      end# end hit, stand, split, double if

      # If busted, can't play further
      if p.hands[i].value() > 21
        p.hands[i].print_hand(i)
        puts "***BUST!!! Can't Play Further!"
        p.hands[i].is_playing = false
      end

    end # end while hand is being played out

  end # end playing internally

  def distribute_money()

    puts "================= DISTRIBUTING WINNINGS =================="
    dealer_total = @dealer.hands[0].value()

    # For every player, distribute money for each Hand individually.
    @players.each do |player|
      # do for hand 0
      distribute_money_internal(dealer_total,player,0)
      # if split is true, then do for hand 1
      if player.has_split
        distribute_money_internal(dealer_total,player,1)
      end
    end # end for each player

  end # end distribute money

  ### THIS can be a whole lot cleaner, but I dont have time to clean it up, too much homework left!
  def distribute_money_internal(dealer_total, player , hand_index)
    i = hand_index
    if dealer_total > 21
      puts "Dealer got Busted"
      if player.hands[i].blackjack()
        #dealer bust, player blackjack
        player.amount += (player.hands[i].bet * 2.5)
        puts "Player #{player.player_number} Blackjack - Dealer Bust , Amount= #{player.amount}"
      elsif player.hands[i].value < 21
        # dealer bust, player safe
        player.amount += 2*player.hands[i].bet
        puts "Player #{player.player_number} Won - Dealer Bust , Amount= #{player.amount}"
      else
        # both got busted, just return bet amount
        player.amount += player.hands[i].bet
        puts "Player #{player.player_number} Bust - Dealer Bust , Amount= #{player.amount}"
      end
    else
      if player.hands[i].blackjack() and dealer_total == 21
        player.amount += (player.hands[i].bet * 1)
        puts "Player #{player.player_number} Blackjack - Dealer Blackjack , Amount= #{player.amount}"
      elsif player.hands[i].blackjack() # and dealer_total < 21 implied
        player.amount += (player.hands[i].bet * 2.5)
        puts "Player #{player.player_number} Blackjack - Dealer Less , Amount= #{player.amount}"
      elsif player.hands[i].value > 21 # and dealer_total <= 21 implied
        puts "Player #{player.player_number} Bust - Dealer Safe , Amount= #{player.amount}"
      elsif   player.hands[i].value < 21 and dealer_total == 21
        puts "Player #{player.player_number} Safe - Dealer Blackjack , Amount= #{player.amount}"
      elsif player.hands[i].value() == dealer_total
        player.amount += player.hands[i].bet
        puts "Player #{player.player_number} Dealer Equal , Amount= #{player.amount}"
      elsif player.hands[i].value() > dealer_total #  and player.hands[i].value() < 21 implied
        player.amount += 2*player.hands[i].bet
        puts "Player #{player.player_number} Won , Dealer Safe , Amount= #{player.amount}"
      else # player < dealer implied
        puts "Player #{player.player_number} Lost, Dealer Safe , Amount= #{player.amount}"
      end
    end # end large if
  end # end distribute internal

end # End BlackJack class

blackjack = Blackjack.new()
blackjack.play_maldijack()