SUITE = [2, 3, 4, 5, 6, 7, 8, 9, 10, "J", "Q", "K", "A"]
# simply *4 to get 1 full deck and *4*n to get n decks and then .shuffle a few times

class Hand
  attr_accessor :cards, :bet , :is_playing
  def initialize(cards,bet)
    @is_playing=true
    @bet = bet
    @cards= cards
  end

  def print_hand()
    print "Hand :"
    @cards.each {|card| print "#{card} "}
    puts "Value: #{value()}"
  end

  def blackjack()
    return (value() == 21)
  end

  # Gets the value of the hand for the player according to the player cards
  def value()
    sum = 0
    # Partition the array by string and integer then reverse to put aces at back
    @cards.partition{|x| x.is_a? String}.map(&:sort).flatten.reverse_each do |i|
      if ["Q", "J", "K"].include?(i)
        sum += 10
      elsif i == "A"
        if sum + 11 > 21
          sum += 1
        else
          sum += 11
        end
      else
        sum += i
      end
    end
    return sum
  end

end

class Player
  attr_accessor :hands, :amount , :is_playing,
  def initialize(amount, player_number)
    @amount = amount
    @is_playing = true
    @bet = 0
    @player_number = player_number
    @hands= Array.new
    @hands[0] = Hands.new()
  end

  def reset()
    @hands = Array.new
    @is_playing = true
  end
end

class Blackjack
  def initialize()

    @players =  {} # Players array
    @num_players = 0
    @num_decks = 4 # Number of card decks
    @cards = Array.new # Universal cards that are held with the dealer
    @dealer = Player.new(0, Array.new, -1) # Dealer

    init_game()
    play_game()

  end

  def play_game()
    # game will continue until people have run out of money
    while (true)
      init_round()
      round()
    end
  end

  def init_round()

    # Initializing the card deck
    deck_size = @num_decks * 4
    deck_size.times {@cards += SUITE}

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

  def round()

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

    end_round()
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

  def end_round()

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

  def init_game()

    puts "+===========================+"
    puts "|         BLACKJACK         |"
    puts "+===========================+"

    puts "Please enter the number of players for the game:"
    n = 0

    # We mush get atleast one player at the table
    while n <= 0
      print "Enter a number greater than 0: "
      n = gets.to_i
    end

    # Initializing the players
    for i in 1...n+1
      @players[i] = Player.new(1000, Array.new, i)
    end
  end

  # Gets a random card from the deck
  def get_card()
    return @cards.delete_at(rand(@cards.length))
  end

end

blackjack = Blackjack.new()
