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

  def can_split()
    return (@has_split == false  and @hands[0].cards.length == 2 and @hands[0].bet  <= @amount and validate_split_cards_internal(@hands[0].cards[0],@hands[0].cards[1]) )
  end # end can split?

  def create_new_hand_for_split()
    if can_split()
      @hands[1] = Hand.new(@hands[0].bet, Array.new)  # create new hand
      @amount = @amount - @hands[0].bet          # reduce the available amount
      @hands[1].cards.push(@hands[0].cards[0])      # push a card from 0 to 1
      @hands[0].cards.delete_at(0)                   # delete the card from hand 0
      @has_split = true                              # set split flag
    end
  end # end create new hand

  def can_double(i)
    return (@hands[i].cards.length == 2 and @hands[i].bet  <= @amount)
  end # end double

  def modify_for_double(i)
    @amount = @amount - @hands[i].bet          # reduce the available amount
    @hands[i].bet *= 2                           # double the bet
    @hands[i].is_playing = false                 # stand down
  end # end modify for double

end # end class

# Again, am sure I can write a better fn than this, but I dont have time to research ruby
def validate_split_cards_internal(card1,card2)
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