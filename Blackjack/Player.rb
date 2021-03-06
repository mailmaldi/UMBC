class Player
  attr_accessor :hands, :amount , :is_playing , :has_split, :player_number , :has_surrendered
  # when player is initialized, we also initialize the first hand
  # for splitting, I will have to break up the first hand into the 2nd hand and make the main game loop play proper
  def initialize(amount, player_number)
    @amount = amount
    @is_playing = true
    @player_number = player_number
    @hands= Array.new
    @hands[0] = Hand.new(0,Array.new)
    @has_split = false
    @has_surrendered = false
  end

  def reset()
    @hands = Array.new
    @hands[0] = Hand.new(0,Array.new)
    @is_playing = true
    @has_split = false
    @has_surrendered = false
  end

  def print_Player()
    print "Player : #{player_number} ,  Amount : #{amount} , has_split : #{has_split} \n"
    print_hand(0)
    if has_split
      print_hand(1)
    end
  end

  # wrapper function to push card to the hand indicated by index
  def push_card(hand_index , card)
    #TODO error checking if hand_index indeed exists in the hands array
    @hands[hand_index].push_card(card)
  end

  ## split is allowed if,
  ## 1. player has equivalent bet amount
  ## 2. has_split is false
  ## 3. number of cards == 2
  ## 4. the 2 cards are of same value if integer and if a String,
  ## NOTE: this entire condition can be encapsulated in a single function call for cleanliness, but mfuture
  def can_split()
    return (@has_split == false  and @hands[0].cards_length() == 2 and get_bet(0)  <= @amount and validate_split_cards_internal(@hands[0].get_card(0),@hands[0].get_card(1)) )
  end # end can split?

  ## Create a new hand , i.e. hand[1] if hand can be split
  def create_new_hand_for_split()
    if can_split()
      @hands[1] = Hand.new(get_bet(0), Array.new)  # create new hand
      modify_account(get_bet(0),-1)       # reduce the available amount
      push_card(1,@hands[0].get_card(0))          # push a card from 0 to 1
      @hands[0].delete_card(0)                   # delete the card from hand 0
      @has_split = true                              # set split flag
    end
  end # end create new hand

  # return true only if the hand's length is 2 i.e. has taken no hits till now & also has the money to place the double bet
  def can_double(i)
    return (@hands[i].cards_length() == 2 and get_bet(i)  <= @amount)
  end # end double

  def modify_for_double(i)
    if can_double(i)                              # only if i can double currently will i double
      modify_account(get_bet(i),-1)               # reduce the available amount
      @hands[i].bet *= 2                           # double the bet
      disable_playing(i)                          # stand down
    end
  end # end modify for double

  # return true only if the hand's length is 2 i.e. has taken no hits till now & also has not split, surrender is the first call
  def can_surrender()
    return (@hands[0].cards_length() == 2 and @has_split  == false)
  end # end can surrender

  def has_surendered()
    return (@has_surrendered == true)
  end

  # when surrendering, player gets half of his bet back
  def surrender()
    if can_surrender()
      disable_playing(0)                      # stop playing the hand
      modify_account(get_bet(0),0.5)       # return half the bet money
      @has_surrendered = true                 # set surrendered flag
    end
  end # end can surrender

  def modify_account(bet , mod_factor)
    @amount += (bet * mod_factor)
  end # end modify amount

  def get_bet(hand_index)
    #TODO error checking Out of Bounds
    return @hands[hand_index].bet
  end

  def set_bet(hand_index, bet)
    #TODO error checking Out of Bounds
    @hands[hand_index].bet = bet
  end

  def get_value(hand_index)
    #TODO error checking Out of Bounds
    return @hands[hand_index].value()
  end

  def print_hand(hand_index)
    #TODO error checking OOB
    @hands[hand_index].print_hand(hand_index)
  end

  def is_playing(hand_index)
    #TODO error checking OOB
    return @hands[hand_index].is_playing
  end

  def disable_playing(hand_index)
    #TODO error checking OOB
    @hands[hand_index].is_playing = false
  end

  def blackjack(hand_index)
    #TODO error checking OOB
    return @hands[hand_index].blackjack
  end

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