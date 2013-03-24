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
    print "Player : #{player_number} ,  Amount : #{amount} , has_split : #{has_split} \n"
    hands[0].print_hand(0)
    if has_split
      hands[1].print_hand(1)
    end
  end

  ## split is allowed if,
  ## 1. player has equivalent bet amount
  ## 2. has_split is false
  ## 3. number of cards == 2
  ## 4. the 2 cards are of same value if integer and if a String,
  ## NOTE: this entire condition can be encapsulated in a single function call for cleanliness, but mfuture
  def can_split()
    return (@has_split == false  and @hands[0].cards.length == 2 and @hands[0].bet  <= @amount and validate_split_cards_internal(@hands[0].cards[0],@hands[0].cards[1]) )
  end # end can split?

  ## Create a new hand , i.e. hand[1] if hand can be split
  def create_new_hand_for_split()
    if can_split()
      @hands[1] = Hand.new(@hands[0].bet, Array.new)  # create new hand
      @amount = @amount - @hands[0].bet          # reduce the available amount
      @hands[1].cards.push(@hands[0].cards[0])      # push a card from 0 to 1
      @hands[0].cards.delete_at(0)                   # delete the card from hand 0
      @has_split = true                              # set split flag
    end
  end # end create new hand

  # return true only if the hand's length is 2 i.e. has taken no hits till now & also has the money to place the double bet
  def can_double(i)
    return (@hands[i].cards.length == 2 and @hands[i].bet  <= @amount)
  end # end double

  def modify_for_double(i)
    if can_double(i)                              # only if i can double currently will i double
      @amount = @amount - @hands[i].bet           # reduce the available amount
      @hands[i].bet *= 2                           # double the bet
      @hands[i].is_playing = false                 # stand down
    end
  end # end modify for double

end # end class

### This function is outside the class, doesn't need to be inside
# Again, am sure I can write a better fn than this, but I dont have time to research ruby
def validate_split_cards_internal(card1,card2)
  arr = ["J","Q","K","A"]
  if card1.is_a?Integer and card2.is_a?Integer
    return (card1 == card2)
  elsif arr.any? { |s| s.include?(card1) } and arr.any? { |s| s.include?(card2) }
    return (card1 == card2)
  else
    return false
  end
end # end validate_splittable_cards