class Hand
  attr_accessor :cards, :bet , :is_playing
  def initialize(bet,cards)
    @is_playing= true
    @bet = bet
    @cards = cards
  end

  def get_card(card_index)
    #TODO raise exception if OOB
    return @cards[card_index]
  end

  def delete_card(card_index)
    #TODO raise exception if OOB
    @cards.delete_at(card_index)
  end

  def push_card(card)
    @cards.push(card)
  end

  def cards_length()
    return @cards.length
  end

  # Gets the value of the hand that the player has
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

  def print_hand(i)
    print "Hand #{i} : "
    @cards.each {|card| print "#{card} "}
    print ",Bet = #{bet} , Value = #{value()} , Hard-Value = #{hard_value()}\n"
  end

  def blackjack()
    return (value() == 21)
  end

end #end class Hand