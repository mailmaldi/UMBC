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
    @has_split = false
  end

  def reset()
    @hands = Array.new
    @hands[0] = Hand.new(0,Array.new)
    @is_playing = true
    @has_split = false
  end

  def print_Player()
    print "Player : #{player_number} ,  Amount : #{amount} , has_split : #{has_split} , "
    hands[0].print_hand
    if has_split
      hands[1].print_hand
    end
  end
end