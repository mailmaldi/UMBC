require 'Hand.rb'
require 'Player.rb'
require 'Shoe.rb'

class Blackjack

  attr_accessor :players, :dealer  , :shoe
  def initialize()
    @players =  Array.new # Players are held in an array
    @dealer = Player.new(0, -1) # Dealer is a special kind of player with infinite money & a special player id, we use id in the play_internally later to prevent dealer from double, etc
    @shoe = Shoe.new()
  end # end initialize

  # Gets a random card from the shoe
  def get_card()
    @shoe.get_card()
  end # end get_card

  ## the main game play is simple. initialize and then the betting & playing rounds loop infinitely.
  def play_maldijack()
    initialize_maldijack() # get number of players & decks and initialize their classes & shoe!
    while (true)
      bookkeeping_before_betting()  # do some bookkeeping
      betting_round() # every player places a bet and gets 2 cards in return, we exit() if no more players left with any money
      playing_round() # every player plays, then dealer plays & then money is doled out
      distribute_money() # now check every player with the dealer & distribute winnings
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

    ## Initialize all the players with  $1000 , a player id
    for i in 0...n
      @players[i] = Player.new( 1000,  i)
    end

    @shoe.create_deck(decks)
    puts "\n\n\nCOMMENT THIS OUT IN ACTUAL RUNS, ITS FOR DEBUGGING ONLY"
    @shoe.print_shoe()
  end # end initializing the game

  def betting_round()
    puts "\n\n\n#### THERE WE BEGIN ####\n\n"

    # Dealer gets 2 cards, we could do dealing cards in round-robin, but thats just additional work, doesnt really matter, just an extra block of code
    @dealer.push_card(0,get_card)
    @dealer.push_card(0,get_card)
    puts "*************** DEALER CARDS  [#{@dealer.hands[0].get_card(0)}]  [__] *********************" # {@dealer.hands[0].cards[1]}

    # get players bets & then give them 2 cards
    # TODO if player bets 0 or less, maybe exit them from the game? code is short, just puts "PLAYER exiting" and delete it from the @players array
    @players.each do | player|
      #player.hands[0].cards = [get_card, get_card] # give player 2 cards
      player.push_card(0,get_card)
      player.push_card(0,get_card)
      player.print_hand(0)
      while (player.get_bet(0) <= 0 or player.get_bet(0) > player.amount)
        print "Player #{player.player_number}, enter bet amount between 1 & #{player.amount} : "
        player.set_bet(0,gets.to_i) # get a bet from player which he can afford
      end
      #player.amount = player.amount - player.hands[0].bet # reduce player's available amount by bet amount
      player.modify_account(player.get_bet(0),-1)
      player.print_Player # print player
      puts ""
    end # end for each player
    puts "================================================================="
  end # end betting round

  # TODO future, check for mimimum bet
  # bookeeping resets players hands and removes players with no money
  def bookkeeping_before_betting()
    @dealer.reset() ## very important to reset dealer
    @players.delete_if{|player| player.amount <= 0} ## remove broke players
    if @players.size == 0
      puts "**********WE NEED MORE PLAYERS**************"
      exit() # exit if no more players left
    end
    @players.each do | player|
      player.reset()  ## reset remaining players
    end # end reset
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
    while @dealer.get_value(0) < 17
      #@dealer.hands[0].cards.push(get_card)
      @dealer.push_card(0,get_card)
    end
    #### NOTE: comment this block if dealer needs to stop automatically at 17 , i.e. auto-stand at 17.
    if @dealer.get_value(0) < 21
      play_internally(@dealer,0)
    end
    puts "================== FINAL DEALER HAND:======================"
    @dealer.print_hand(0)

    @players.each do | p|
      puts "===================== PLAYER #{p.player_number} ====================="
      p.print_Player
    end

  end # end playing round

  # generic function that handles common stuff for both hands[0] & [1]
  def play_internally(player,hand_index)
    p = player
    i = hand_index

    while p.is_playing(i)
      p.print_hand(i) # print the current hand

      #if blackjack, no need to play further, only a moron would hit more, they'd obviously stand.
      if p.blackjack(i)
        p.disable_playing(i)
        puts "***Blackjack!!! No need to play further!" # TODO maybe play for a hard blackjack, in which case just comment out this BLOCK! code will be shorter
        break
      end # end blackjack if

      print "Please choose from the following {hit, stand, split, double, surrender}: "
      decision = gets.chomp

      if decision == "hit"
        p.push_card(i,get_card)
      elsif decision ==  "stand"
        p.disable_playing(i)  # just finish this hand
      elsif decision == "split" and p.player_number >=0         # dealer cant split
        if i == 0  and p.can_split()                            # i==0 is redundant actually. p.can_split() just checks
          p.create_new_hand_for_split()                         # create new hands
          p.push_card(0,get_card)                                 # offer one more card for Hand 0
          p.push_card(1,get_card)                                 # offer one more card for Hand 1
          puts "Player #{p.player_number} Split  call was done on hand #{i}"
          p.print_Player                                        # print the players new set of Hands
        else
          puts "Player #{p.player_number} Split  call was denied on hand #{i}"
        end
      elsif decision == "double" and p.player_number >=0 #dealer cant double
        ## Player can double his hand after splitting so not putting that condition
        if p.can_double(i)                              # check if its ok to double, note that a split hand can indeed be doubled
          p.modify_for_double(i)                        # modify bet for doubling
          p.push_card(i,get_card)                       # take one more card & stand down
          puts "Player #{p.player_number} has called Double on his hand #{i}"
          p.print_Player
        else
          puts "Player #{p.player_number} Double call was denied on hand #{i}"
        end
      elsif decision == "surrender" and p.player_number >=0 #dealer cant surrnder
        if p.can_surrender()
          p.surrender()
          puts "Player #{p.player_number} Surrender on hand #{i}"
        else
          puts "Player #{p.player_number} Surrender call was denied on hand #{i}"
        end
      end# end hit, stand, split, double if

      # If busted, can't play further
      if p.get_value(i) > 21
        p.print_hand(i)
        puts "***BUST!!! Can't Play Further!"
        p.disable_playing(i)
      end

    end # end while hand is being played out

  end # end playing internally

  def distribute_money()
    puts "================= DISTRIBUTING WINNINGS =================="
    dealer_total = @dealer.hands[0].value()

    # For every player, distribute money for each Hand individually.
    @players.each do |player|
      unless player.has_surrendered()
        # do for hand 0
        distribute_money_internal_2(dealer_total,player,0)
        # if split is true, then do for hand 1
        if player.has_split
          distribute_money_internal_2(dealer_total,player,1)
        end # end check if player split
      end # end unless
    end # end for each player
  end # end distribute money

  # for explanation of conditions, I've a payoff matrix on paper :)
  # Haven't considered the case of Hard blackjack vs normal
  def distribute_money_internal_2(dealer_total, player , hand_index)
    i = hand_index
    dt = dealer_total
    hv = player.get_value(i)
    bet = player.get_bet(i)
    pn = player.player_number

    # instead of modifiying amount directly, should use a function call to increment player amount by payoff factor
    if (hv == 21 and (dt > 21 or dt < 21) )
      #player.amount += (bet * 2.5)
      player.modify_account(bet,2.5)
      puts "Player #{pn} H#{i} #{hv} Blackjack - Dealer #{dt} , Amount= #{player.amount}"
    elsif (hv < 21 and dt > 21) or (dt <= 21 and hv <= 21 and hv > dt)
      #player.amount += (bet * 2)
      player.modify_account(bet,2)
      puts "Player #{pn} H#{i} #{hv} - Dealer #{dt} , Amount= #{player.amount}"
    elsif (dt > 21 and hv > 21) or ((hv == 21) and dt == 21 ) or (hv == dealer_total and dt <= 21 and hv <= 21)
      #player.amount += (bet * 1)
      player.modify_account(bet,1)
      puts "Player #{pn} H#{i} #{hv} - Dealer #{dt} , Amount= #{player.amount}"
    else
      puts "Player #{pn} H#{i} #{hv} - Dealer #{dt} , Amount= #{player.amount}"
    end

  end # end distribute internal_2

end # End BlackJack class

blackjack = Blackjack.new()
blackjack.play_maldijack()